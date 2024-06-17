package Clases;

import Clases.Servidores.Caja;
import Clases.Servidores.EstacionLavado;
import Clases.Servidores.EstacionMantenimiento;
import Clases.Servidores.Shop;
import Clases.Servidores.Surtidor;
import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteCombustible;
import Clases.clientes.ClienteLavado;
import Clases.clientes.ClienteMantenimiento;
import Clases.clientes.ClienteShop;
import Clases.finAtencion.FinAtencionCaja;
import Clases.finAtencion.FinAtencionCombustible;
import Clases.finAtencion.FinAtencionLavado;
import Clases.finAtencion.FinAtencionMantenimiento;
import Clases.finAtencion.FinAtencionShop;
import Clases.interfaces.FinAtencion;
import Clases.interfaces.Llegada;
import Clases.llegadas.LlegadaCaja;
import Clases.llegadas.LlegadaCombustible;
import Clases.llegadas.LlegadaLavado;
import Clases.llegadas.LlegadaMantenimiento;
import Clases.llegadas.LlegadaShop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import tp4.view.Pantalla;

public class GestorServicio implements ActionListener {
    private Pantalla views;
    DefaultTableModel model = new DefaultTableModel();
    private double reloj;
    private int cantSimulaciones;
    private int limiteSuperior;
    private int limiteInferior;
    private ArrayList<Simulacion> simulacionesRango;
    private Simulacion filaActual;
    private Simulacion filaAnterior;
    
    private double promEsperaCombustible;
    private double promEsperaLavado;
    private double promEsperaMantenimiento;
    private double promEsperaCaja;
    private double promEsperaShop;
    
    
    // Acumuladores clientes atendidos
    private double tiempoOcupadoCombustible;
    private double tiempoOcupadoLavado;
    private double tiempoOcupadoMantenimiento;
    private double tiempoOcupadoCaja;
    private double tiempoOcupadoShop;
    
    // Promedios de cantidad de cola de cada servicio
    private double promCantidadColaCombustible;
    private double promCantidadColaLavado;
    private double promCantidadColaMantenimiento;
    private double promCantidadColaCaja;
    private double promCantidadColaShop;
    
    // Tiempo minimo atencion
    private double tiempoMinimoAtencion;
    private String nombreServicioMinimo;


    public GestorServicio(Pantalla views) {
        this.reloj = 0;
        this.views = views;
        this.views.btn_simular.addActionListener(this);
        this.simulacionesRango = new ArrayList<>();
        this.filaActual = new Simulacion();
        this.filaAnterior = new Simulacion();
        
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_simular){     
             cleanTable();
             resetVariables();
             generarSimulacion();
             
             // Actividades
             calcularPromediosEspera();
             calcularPorcentajeOcupacion();
             calcularMenorTiempoAtencion();
             calcularPromedioCola();
             
             mostrarSimulaciones();
             

             
        }
  
    }
    public ArrayList<Simulacion> generarSimulacion() {
        
        
        this.cantSimulaciones = Integer.parseInt(views.nro_simulaciones.getText());
        this.limiteInferior =  Integer.parseInt(views.desde.getText());
        this.limiteSuperior = Integer.parseInt(views.hasta.getText());
        filaActual.inicializar(Double.parseDouble(views.media_llegada_caja.getText()),
                (Double.parseDouble(views.media_llegada_combustible.getText())),
                (Double.parseDouble(views.media_llegada_lavado.getText())),
                (Double.parseDouble(views.media_llegada_mantenimiento.getText())),1);
        if(limiteInferior == 0){
          
            simulacionesRango.add(filaActual.clone());
        }
        filaAnterior = filaActual.clone(); // para la primer iteracion fila anterior es igual a la actual
        for (int i = 1; i < cantSimulaciones; i++) {
            Object proximoEvento = filaAnterior.calcularProximoEvento();
            //LLEGADAS
            if (proximoEvento instanceof Llegada) {
                filaActual.setRelojActual(((Llegada) proximoEvento).getProxLlegada());
                reloj = filaActual.getRelojActual();

                //LLEGADA COMBUSTIBLE
                if (proximoEvento instanceof LlegadaCombustible) {
                    llegadaCombustible();
                    determinarLlegadaShop(true);

                    //LLEGADA LAVADO
                } else if (proximoEvento instanceof LlegadaLavado) {
                    llegadaLavado();
                    determinarLlegadaShop(true);
                } else if (proximoEvento instanceof LlegadaMantenimiento) {

                    //LLEGADA MANTENIMIENTO
                    llegadaMantenimiento();
                    determinarLlegadaShop(true);
                } else if (proximoEvento instanceof LlegadaCaja) {
                    //LLEGADA CAJA
                    llegadaCaja();

                }else{
                    llegadaShop();
                }

            } else {
                // FIN ATENCION
                filaActual.setRelojActual(((FinAtencion) proximoEvento).getProxFin());
                reloj = filaActual.getRelojActual();
                //FIN ATENCION COMBUSTIBLE
                if (proximoEvento instanceof FinAtencionCombustible) {
                    FinAtencionCombustible objetoFin = (FinAtencionCombustible) proximoEvento;
                    calcularFinAtencionCombustible(objetoFin);
                    determinarLlegadaShop(false);
                } else if (proximoEvento instanceof FinAtencionLavado) {
                    FinAtencionLavado objetoFin = (FinAtencionLavado) proximoEvento;
                    calcularFinAtencionLavado(objetoFin);
                    determinarLlegadaShop(false);
                } else if (proximoEvento instanceof FinAtencionMantenimiento) {
                    FinAtencionMantenimiento objetoFin = (FinAtencionMantenimiento) proximoEvento;
                    calcularFinAtencionMantenimiento(objetoFin);
                    determinarLlegadaShop(false);
                } else if (proximoEvento instanceof FinAtencionCaja) {
                    //FIN ATENCION CAJA
                    FinAtencionCaja objetoFin = (FinAtencionCaja) proximoEvento;
                    calcularFinAtencionCaja(objetoFin);
                    determinarLlegadaShop(false);
                }else{
                    FinAtencionShop objetoFin = (FinAtencionShop) proximoEvento;
                    calcularFinAtencionShop(objetoFin);
                }

            }

            // Calcular tiempo ocupado
            calcularTiempoOcupacion();

            if (i >= limiteInferior && i <= limiteSuperior) {
                simulacionesRango.add(filaActual.clone());
            }
            // Guardo si es la ultima linea
            if (i == cantSimulaciones-1){
                simulacionesRango.add(filaActual.clone());
            }

            //al finalizar la linea actual la guardo en la anterior
            filaAnterior = filaActual.clone();
        }
        return simulacionesRango;
    }
    
    

    public void llegadaCombustible(){
        boolean algunoLibre = false;
        int menorSurtidorSubIndice = 0;
        filaActual.setLlegadaCombustible(new LlegadaCombustible(Double.parseDouble(views.media_llegada_combustible.getText()),reloj));
        //hago una copia de los surtidores anteriores para no cambiarlos y actualizarlos para el actual
        ArrayList<Surtidor> surtidoresAct = (ArrayList<Surtidor>) filaAnterior.getSurtidores().clone();
        int colaMinima = Integer.MAX_VALUE;
        for (Surtidor surtidor : filaAnterior.getSurtidores()) {
            
            
            if (surtidor.esLibre()) {
                surtidoresAct.get(surtidor.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionCombustible(new FinAtencionCombustible(Double.parseDouble(views.media_atencion_shop.getText()),surtidor.getSubindice(),reloj));
                surtidoresAct.get(surtidor.getSubindice()).agregarCliente(new ClienteCombustible("SA",filaActual.getRelojActual()));
                filaActual.setSurtidores((ArrayList<Surtidor>) surtidoresAct.clone());
                break;

            } else if (surtidor.getClientesCombustible().size() < colaMinima) {
                colaMinima = surtidor.getClientesCombustible().size();
                menorSurtidorSubIndice = surtidor.getSubindice();
            }
        }
        if (!algunoLibre) {
            surtidoresAct.get(menorSurtidorSubIndice).agregarCliente(new ClienteCombustible("EA",filaActual.getRelojActual()));
            filaActual.setSurtidores((ArrayList<Surtidor>) surtidoresAct.clone());
        }

    }

    public void llegadaLavado(){
        boolean algunoLibre = false;
        int menorEstacionSubIndice = 0;
        filaActual.setLlegadaLavado(new LlegadaLavado(Double.parseDouble(views.media_llegada_lavado.getText()),reloj));

        ArrayList<EstacionLavado> estacionesAct = (ArrayList<EstacionLavado>) filaAnterior.getEstacionesLavado().clone();
        int colaMinima = Integer.MAX_VALUE;
        for (EstacionLavado estacionLavado : filaAnterior.getEstacionesLavado()) {
            
            if (estacionLavado.esLibre()) {
                estacionesAct.get(estacionLavado.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionLavado(new FinAtencionLavado(Double.parseDouble(views.media_atencion_lavado.getText()),estacionLavado.getSubindice(),reloj));
                estacionesAct.get(estacionLavado.getSubindice()).agregarCliente(new ClienteLavado("SA",filaActual.getRelojActual()));
                filaActual.setEstacionesLavado((ArrayList<EstacionLavado>)estacionesAct.clone());
                break;

            } else if (estacionLavado.getClientesLavado().size() < colaMinima) {
                colaMinima = estacionLavado.getClientesLavado().size();
                menorEstacionSubIndice = estacionLavado.getSubindice();
            }
        }
        if (!algunoLibre) {
     
            estacionesAct.get(menorEstacionSubIndice).agregarCliente(new ClienteLavado("EA",filaActual.getRelojActual()));
            filaActual.setEstacionesLavado((ArrayList<EstacionLavado>)estacionesAct.clone());
        }

    }

    public void llegadaMantenimiento(){
        boolean algunoLibre = false;
        int menorEstacionSubIndice = 0;
        filaActual.setLlegadaMantenimiento(new LlegadaMantenimiento(Double.parseDouble(views.media_llegada_mantenimiento.getText()),reloj));
        int colaMinima = Integer.MAX_VALUE;    
        ArrayList<EstacionMantenimiento> estacionesAct = (ArrayList<EstacionMantenimiento>) filaAnterior.getEstacionesMantenimiento().clone();
        for (EstacionMantenimiento estacionMantenimiento : filaAnterior.getEstacionesMantenimiento()) {
            
            if (estacionMantenimiento.esLibre()) {
                estacionesAct.get(estacionMantenimiento.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionMantenimiento(new FinAtencionMantenimiento(Double.parseDouble(views.media_atencion_mantenimiento.getText()),estacionMantenimiento.getSubindice(),reloj));
                estacionesAct.get(estacionMantenimiento.getSubindice()).agregarCliente(new ClienteMantenimiento("SA",filaActual.getRelojActual()));
                filaActual.setEstacionesMantenimiento((ArrayList<EstacionMantenimiento>)estacionesAct.clone());
                break;

            } else if (estacionMantenimiento.getClientesMantenimiento().size() < colaMinima) {
                colaMinima = estacionMantenimiento.getClientesMantenimiento().size();
                menorEstacionSubIndice = estacionMantenimiento.getSubindice();
            }
        }
        if (!algunoLibre) {
            estacionesAct.get(menorEstacionSubIndice).agregarCliente(new ClienteMantenimiento("EA",filaActual.getRelojActual()));
            filaActual.setEstacionesMantenimiento((ArrayList<EstacionMantenimiento>)estacionesAct.clone());
        }

    }

    public void llegadaCaja() {
        boolean algunoLibre = false;
        int menorCajaSubIndice = 0;
        filaActual.setLlegadaCaja(new LlegadaCaja(Double.parseDouble(views.media_llegada_caja.getText()),reloj));
        int colaMinima = Integer.MAX_VALUE;
        //hago una copia de los surtidores anteriores para no cambiarlos y actualizarlos para el actual
        ArrayList<Caja> cajasAct = (ArrayList<Caja>)filaAnterior.getCajas().clone();
        for (Caja caja : filaAnterior.getCajas()) {
            
            if (caja.esLibre()) {
                cajasAct.get(caja.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionCaja(new FinAtencionCaja(Double.parseDouble(views.media_atencion_caja.getText()), caja.getSubindice(),reloj));
                cajasAct.get(caja.getSubindice()).agregarCliente(new ClienteCaja("SA", filaActual.getRelojActual()));
                filaActual.setCajas((ArrayList<Caja>) cajasAct.clone());
                break;

            } else if (caja.getClientesCaja().size() < colaMinima) {
                colaMinima = caja.getClientesCaja().size();
                menorCajaSubIndice = caja.getSubindice();
            }
        }
        if (!algunoLibre) {
            cajasAct.get(menorCajaSubIndice).agregarCliente(new ClienteCaja("EA",filaActual.getRelojActual()));
            filaActual.setCajas((ArrayList<Caja>) cajasAct.clone());
        }
    }
    
    public void determinarLlegadaShop(boolean esLlegada){

        double rnd = Math.random();
        if ((esLlegada && rnd < 0.25) || (!esLlegada && rnd < 0.1)) {
            llegadaShop();
        }
}
    public void llegadaShop(){
        filaActual.setLlegadaShop(new LlegadaShop(Double.valueOf(views.media_llegada_shop.getText()),reloj));
        Shop shop =  filaAnterior.getShop();
        if (shop.esLibre()) {
            shop.setEstado("ocupado");
            filaActual.setFinAtencionShop(new FinAtencionShop(Double.valueOf(views.media_atencion_shop.getText()),reloj));
            filaActual.getShop().agregarCliente(new ClienteShop("SA", filaActual.getRelojActual()));
            } else{                    
                filaActual.getShop().agregarCliente(new ClienteShop("EA",filaActual.getRelojActual()));
            }
    }

    

    public void calcularFinAtencionCombustible(FinAtencionCombustible objetoFin) {
        int indice = objetoFin.getSubindice();
        filaActual.getSurtidor(indice).eliminarCliente();
        if (filaAnterior.getSurtidor(indice).getClientesCombustible().isEmpty()) {
            filaActual.getSurtidor(indice).setEstado("libre");


        } else {
            
            ClienteCombustible cliente = filaActual.getSurtidor(indice).buscarSiguiente();
            filaActual.actualizarEsperaCombustible(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosCombustible();

            filaActual.agregarFinAtencionCombustible(new FinAtencionCombustible(Double.parseDouble(views.media_atencion_shop.getText()),filaActual.getSurtidor(indice).getSubindice(),reloj));

        }

    }
    public void calcularFinAtencionLavado(FinAtencionLavado objetoFin) {
        int indice = objetoFin.getSubindice();
        filaActual.getEstacionLavado(indice).eliminarCliente();
        if (filaAnterior.getEstacionLavado(indice).getClientesLavado().isEmpty()) {
            filaActual.getEstacionLavado(indice).setEstado("libre");

        } else {
             filaActual.getEstacionLavado(indice);
            // Busco siguiente cliente y calculo tiempo espera y sumo contador
            ClienteLavado cliente =  filaActual.getEstacionLavado(indice).buscarSiguiente();
            filaActual.actualizarEsperaLavado(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosLavado();
            filaActual.agregarFinAtencionLavado(new FinAtencionLavado(Double.parseDouble(views.media_atencion_lavado.getText()), filaActual.getEstacionLavado(indice).getSubindice(),reloj));

        }
    }
    public void calcularFinAtencionMantenimiento(FinAtencionMantenimiento objetoFin) {
        int indice = objetoFin.getSubindice();
        filaActual.getEstacionMantenimiento(indice).eliminarCliente();
        if (filaAnterior.getEstacionMantenimiento(indice).getClientesMantenimiento().isEmpty()) {
            filaActual.getEstacionMantenimiento(indice).setEstado("libre");

        } else {
            // Busco siguiente cliente y calculo tiempo espera y sumo contador
            ClienteMantenimiento cliente = filaAnterior.getEstacionMantenimiento(indice).buscarSiguiente();
            filaActual.actualizarEsperaMantenimiento(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosMantenimiento();
            filaActual.agregarFinAtencionMantenimiento(new FinAtencionMantenimiento(Double.parseDouble(views.media_atencion_mantenimiento.getText()),filaAnterior.getEstacionMantenimiento(indice).getSubindice(),reloj));
        }
    }
    public void calcularFinAtencionCaja(FinAtencionCaja objetoFin) {
        int indice = objetoFin.getSubindice();
        filaActual.getCaja(indice).eliminarCliente();
        if (filaAnterior.getCaja(indice).getClientesCaja().isEmpty()) {
            filaActual.getCaja(indice).setEstado("libre");

        } else {
            // Busco siguiente cliente y calculo tiempo espera y sumo contador
            ClienteCaja cliente =  filaActual.getCaja(indice).buscarSiguiente();
            filaActual.actualizarEsperaCaja(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosCaja();
            filaActual.agregarFinAtencionCaja(new FinAtencionCaja(Double.parseDouble(views.media_atencion_caja.getText()), filaActual.getCaja(indice).getSubindice(),reloj));

        }
    }
    
    public void calcularFinAtencionShop(FinAtencionShop objetoFin) {
        filaActual.getShop().eliminarCliente();
        if (filaAnterior.getShop().getClientesShop().isEmpty()) {
            filaActual.getShop().setEstado("libre");

        } else {
            // Busco siguiente cliente y calculo tiempo espera y sumo contador
            ClienteShop cliente =  filaActual.getShop().buscarSiguiente();
            filaActual.actualizarEsperaShop(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosShop();
            filaActual.setFinAtencionShop(new FinAtencionShop(Double.valueOf(views.media_atencion_shop.getText()),reloj));

        }
    }

    private void mostrarSimulaciones() {
            model = (DefaultTableModel) views.tabla_servicios.getModel();
     
            for (int i = 0; i < simulacionesRango.size(); i++) {
               
                Object[] row = new Object[49];
                row[0] = String.format("%.2f",simulacionesRango.get(i).getRelojActual());
                row[1] = String.format("%.2f",simulacionesRango.get(i).getLlegadaCombustible().getProxLlegada());
                row[2] = String.format("%.2f",simulacionesRango.get(i).getLlegadaLavado().getProxLlegada());
                row[3] = String.format("%.2f",simulacionesRango.get(i).getLlegadaMantenimiento().getProxLlegada());
                row[4] = String.format("%.2f",simulacionesRango.get(i).getLlegadaCaja().getProxLlegada());
                row[5] = String.format("%.2f",simulacionesRango.get(i).getLlegadaShop().getProxLlegada());

                  
               
                row[6] =(simulacionesRango.get(i).getFinAtencionCombustible().get(0) != null) ?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(0).getProxFin()): ' ';
                
                row[7] = (simulacionesRango.get(i).getFinAtencionCombustible().get(1) != null)?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(1).getProxFin()): ' ';
                
                row[8] = (simulacionesRango.get(i).getFinAtencionCombustible().get(2) != null)?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(2).getProxFin()):' ';
                
                row[9] = ((simulacionesRango.get(i).getFinAtencionCombustible().get(3) != null))?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(3).getProxFin()):' ';
                
                row[10] = ((simulacionesRango.get(i).getFinAtencionLavado().get(0) != null))?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionLavado().get(0).getProxFin()):' ';        
                
                row[11] = ((simulacionesRango.get(i).getFinAtencionLavado().get(1) != null))?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionLavado().get(1).getProxFin()):' '; 
                
                row[12] = ((simulacionesRango.get(i).getFinAtencionMantenimiento().get(0) != null))? 
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionMantenimiento().get(0).getProxFin()):' '; 
                
                row[13] =  ((simulacionesRango.get(i).getFinAtencionMantenimiento().get(1) != null))? 
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionMantenimiento().get(1).getProxFin()):' '; 
                
                row[14] = ((simulacionesRango.get(i).getFinAtencionCaja().get(0) != null))?  
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCaja().get(0).getProxFin()):' '; 
                
                row[15] = ((simulacionesRango.get(i).getFinAtencionCaja().get(1) != null))?  
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCaja().get(1).getProxFin()):' '; 
               
                row[16] =(simulacionesRango.get(i).getFinAtencionShop() != null) ?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionShop().getProxFin()): ' ';
                
                row[17] = simulacionesRango.get(i).getSurtidor(0).getEstado();
                row[18] = simulacionesRango.get(i).getSurtidor(0).getCola();
                row[19] = simulacionesRango.get(i).getSurtidor(1).getEstado();
                row[20] = simulacionesRango.get(i).getSurtidor(1).getCola();
                row[21] = simulacionesRango.get(i).getSurtidor(2).getEstado();
                row[22] = simulacionesRango.get(i).getSurtidor(2).getCola();
                row[23] = simulacionesRango.get(i).getSurtidor(3).getEstado();
                row[24] = simulacionesRango.get(i).getSurtidor(3).getCola();
                
                row[25] =simulacionesRango.get(i).getEstacionLavado(0).getEstado();
                row[26] =simulacionesRango.get(i).getEstacionLavado(0).getCola();
                row[27] =simulacionesRango.get(i).getEstacionLavado(1).getEstado();
                row[28] =simulacionesRango.get(i).getEstacionLavado(1).getCola();
                
                row[29] =simulacionesRango.get(i).getEstacionMantenimiento(0).getEstado();
                row[30] =simulacionesRango.get(i).getEstacionMantenimiento(0).getCola();
                row[31] =simulacionesRango.get(i).getEstacionMantenimiento(1).getEstado();
                row[32] =simulacionesRango.get(i).getEstacionMantenimiento(1).getCola();
                
                row[33] =simulacionesRango.get(i).getCaja(0).getEstado();
                row[34] =simulacionesRango.get(i).getCaja(0).getCola();
                row[35] =simulacionesRango.get(i).getCaja(1).getEstado();
                row[36] =simulacionesRango.get(i).getCaja(1).getCola();
                
                row[37] =simulacionesRango.get(i).getShop().getEstado();
                row[38] =simulacionesRango.get(i).getShop().getCola();
                
                row[39] =String.format("%.2f",simulacionesRango.get(i).getAcumEsperaCombustible());
                row[40] =String.format("%.2f",simulacionesRango.get(i).getAcumEsperaLavado());
                row[41] =String.format("%.2f",simulacionesRango.get(i).getAcumEsperaMantenimiento());
                row[42] =String.format("%.2f",simulacionesRango.get(i).getAcumEsperaCaja());
                row[43] =String.format("%.2f",simulacionesRango.get(i).getAcumEsperaShop());
                
                
                row[44] =simulacionesRango.get(i).getAtendidosCombustible();
                row[45] =simulacionesRango.get(i).getAtendidosLavado();
                row[46] =simulacionesRango.get(i).getAtendidosMantenimiento();
                row[47] =simulacionesRango.get(i).getAtendidosCaja();
                row[48] =simulacionesRango.get(i).getContShopAtendidos();
          
                model.addRow(row);
                
            }
            views.tabla_servicios.setModel(model);
            //promedios
            views.promedio_combustible.setText(String.format("%.2f",promEsperaCombustible));
            views.promedio_lavado.setText(String.format("%.2f",promEsperaLavado));
            views.promedio_mantenimiento.setText(String.format("%.2f",promEsperaMantenimiento));
            views.promedio_caja.setText(String.format("%.2f",promEsperaCaja));
            views.promedio_shop.setText(String.format("%.2f",promEsperaCaja));
            
            views.tiempo_minimo.setText(String.format("%.2f",tiempoMinimoAtencion));
            views.servicio_minimo.setText(nombreServicioMinimo);
            
            views.ocupacion_combustible.setText(String.format("%.2f",tiempoOcupadoCombustible) + '%');
            views.ocupacion_lavado.setText(String.format("%.2f", tiempoOcupadoLavado) + '%');
            views.ocupacion_mantenimiento.setText(String.format( "%.2f",tiempoOcupadoMantenimiento) + '%');
            views.ocupacion_caja.setText(String.format( "%.2f",tiempoOcupadoCaja) + '%');
            views.ocupacion_shop.setText(String.format( "%.2f",tiempoOcupadoShop) + '%');
            
            views.cola_combustible.setText(String.format( "%.2f",promCantidadColaCombustible));
            views.cola_lavado.setText(String.format( "%.2f",promCantidadColaLavado));
            views.cola_mantenimiento.setText(String.format( "%.2f",promCantidadColaMantenimiento));
            views.cola_caja.setText(String.format( "%.2f",promCantidadColaCaja));
            views.cola_shop.setText(String.format( "%.2f",promCantidadColaShop));
            
            
            
        
    }
    public void cleanTable() {
       DefaultTableModel model = (DefaultTableModel) views.tabla_servicios.getModel();
        model.setRowCount(0);
       
    }
    public void resetVariables(){
        this.reloj = 0;
        this.simulacionesRango = new ArrayList<>();
        this.filaActual = new Simulacion();
        this.filaAnterior = new Simulacion();
        
    }

    public void calcularTiempoOcupacion(){
        //Surtidores
        for(Surtidor surtidor : filaAnterior.getSurtidores()){
            if (surtidor.getEstado() == "ocupado"){
                filaActual.actualizarOcupadoCombustible(reloj - filaAnterior.getRelojActual());
                break;
            }
        }
        //Lavado
        for(EstacionLavado estacionLavado : filaAnterior.getEstacionesLavado()){
            if (estacionLavado.getEstado() == "ocupado"){
                filaActual.actualizarOcupadoLavado(reloj - filaAnterior.getRelojActual());
                break;
            }
        }
        //Mantenimiento
        for(EstacionMantenimiento estacionMantenimiento : filaAnterior.getEstacionesMantenimiento()){
            if (estacionMantenimiento.getEstado() == "ocupado"){
                filaActual.actualizarOcupadoMantenimiento(reloj - filaAnterior.getRelojActual());
                break;
            }
        }
        //Cajas
        for(Caja caja : filaAnterior.getCajas()){
            if (caja.getEstado() == "ocupado"){
                filaActual.actualizarOcupadoCaja(reloj - filaAnterior.getRelojActual());
                break;
            }
        }
    }
    
    public void calcularPromediosEspera(){
        this.promEsperaCombustible = filaActual.getAcumEsperaCombustible()/filaActual.getAtendidosCombustible();
        this.promEsperaLavado = filaActual.getAcumEsperaLavado()/filaActual.getAtendidosLavado();
        this.promEsperaMantenimiento = filaActual.getAcumEsperaMantenimiento()/filaActual.getAtendidosMantenimiento();
        this.promEsperaCaja = filaActual.getAcumEsperaCaja()/filaActual.getAtendidosCaja();
        this.promEsperaShop = filaActual.getAcumEsperaShop()/filaActual.getContShopAtendidos();
    }

    public void calcularPorcentajeOcupacion(){
        this.tiempoOcupadoCombustible = filaActual.getAcumOcupadoCombustible()/reloj *100;
        this.tiempoOcupadoLavado = filaActual.getAcumOcupadoLavado()/reloj *100;
        this.tiempoOcupadoMantenimiento = filaActual.getAcumOcupadoMantenimiento()/reloj *100;
        this.tiempoOcupadoCaja = filaActual.getAcumOcupadoCaja()/reloj *100;
        this.tiempoOcupadoShop = filaActual.getAcumEsperaShop()/reloj *100;
    
    }

    public void calcularMenorTiempoAtencion(){
        double tiempoCombustible = promEsperaCombustible + Double.parseDouble(views.media_atencion_shop.getText());
        double tiempoLavado = promEsperaLavado + Double.parseDouble(views.media_atencion_lavado.getText());
        double tiempoMantenimiento = promEsperaMantenimiento + Double.parseDouble(views.media_atencion_mantenimiento.getText());
        double tiempoCaja = promEsperaCaja + Double.parseDouble(views.media_atencion_caja.getText());
        
        if (tiempoCombustible < tiempoLavado && tiempoCombustible < tiempoMantenimiento && tiempoCombustible < tiempoCaja){
            this.tiempoMinimoAtencion = tiempoCombustible;
            this.nombreServicioMinimo = "Combustible";
        }
        else if (tiempoLavado < tiempoCombustible && tiempoLavado < tiempoMantenimiento && tiempoLavado < tiempoCaja){
            this.tiempoMinimoAtencion = tiempoLavado;
            this.nombreServicioMinimo = "Lavado";
        }
        else if (tiempoMantenimiento < tiempoLavado && tiempoMantenimiento < tiempoCombustible && tiempoMantenimiento < tiempoCaja){
            this.tiempoMinimoAtencion = tiempoMantenimiento;
            this.nombreServicioMinimo = "Mantenimiento";
        }
        else if (tiempoCaja < tiempoLavado && tiempoCaja < tiempoMantenimiento && tiempoCaja < tiempoCombustible){
            this.tiempoMinimoAtencion = tiempoCaja;
            this.nombreServicioMinimo = "Caja";
        }
 
    }
    public void calcularPromedioCola(){
        this.promCantidadColaCombustible = filaActual.obtenerTiempoTotalColasCombustible() / reloj;
        this.promCantidadColaLavado = filaActual.obtenerTiempoTotalColasLavado() / reloj;
        this.promCantidadColaMantenimiento = filaActual.obtenerTiempoTotalColasMantenimiento() / reloj;
        this.promCantidadColaCaja = filaActual.obtenerTiempoTotalColasCaja() / reloj;
        this.promCantidadColaShop = filaActual.obtenerTiempoTotalColasShop()/ reloj;

    }
}
