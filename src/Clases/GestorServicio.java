package Clases;

import Clases.Servidores.Caja;
import Clases.Servidores.EstacionLavado;
import Clases.Servidores.EstacionMantenimiento;
import Clases.Servidores.Surtidor;
import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteCombustible;
import Clases.clientes.ClienteLavado;
import Clases.clientes.ClienteMantenimiento;
import Clases.finAtencion.FinAtencionCaja;
import Clases.finAtencion.FinAtencionCombustible;
import Clases.finAtencion.FinAtencionLavado;
import Clases.finAtencion.FinAtencionMantenimiento;
import Clases.interfaces.FinAtencion;
import Clases.interfaces.Llegada;
import Clases.llegadas.LlegadaCaja;
import Clases.llegadas.LlegadaCombustible;
import Clases.llegadas.LlegadaLavado;
import Clases.llegadas.LlegadaMantenimiento;
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
                (Double.parseDouble(views.media_llegada_mantenimiento.getText())));
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

                    //LLEGADA LAVADO
                } else if (proximoEvento instanceof LlegadaLavado) {
                    llegadaLavado();
                } else if (proximoEvento instanceof LlegadaMantenimiento) {

                    //LLEGADA MANTENIMIENTO
                    llegadaMantenimiento();
                } else {

                    //LLEGADA CAJA
                    llegadaCaja();

                }

            } else {
                // FIN ATENCION
                filaActual.setRelojActual(((FinAtencion) proximoEvento).getProxFin());
                reloj = filaActual.getRelojActual();
                //FIN ATENCION COMBUSTIBLE
                if (proximoEvento instanceof FinAtencionCombustible) {
                    FinAtencionCombustible objetoFin = (FinAtencionCombustible) proximoEvento;
                    calcularFinAtencionCombustible(objetoFin);
                } else if (proximoEvento instanceof FinAtencionLavado) {
                    FinAtencionLavado objetoFin = (FinAtencionLavado) proximoEvento;
                    calcularFinAtencionLavado(objetoFin);
                } else if (proximoEvento instanceof FinAtencionMantenimiento) {
                    FinAtencionMantenimiento objetoFin = (FinAtencionMantenimiento) proximoEvento;
                    calcularFinAtencionMantenimiento(objetoFin);
                } else {
                    //FIN ATENCION CAJA
                    FinAtencionCaja objetoFin = (FinAtencionCaja) proximoEvento;
                    calcularFinAtencionCaja(objetoFin);
                }

            }

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
        filaActual.setLlegadaCombustible(new LlegadaCombustible(2,reloj));
        //hago una copia de los surtidores anteriores para no cambiarlos y actualizarlos para el actual
        ArrayList<Surtidor> surtidoresAct = filaAnterior.getSurtidores();
        for (Surtidor surtidor : filaAnterior.getSurtidores()) {
            
            int colaMinima = Integer.MAX_VALUE;
            if (surtidor.esLibre()) {
                surtidoresAct.get(surtidor.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionCombustible(new FinAtencionCombustible(Double.parseDouble(views.media_atencion_combustible.getText()),surtidor.getSubindice(),reloj));
                surtidoresAct.get(surtidor.getSubindice()).agregarCliente(new ClienteCombustible("SA",filaActual.getRelojActual()));
                filaActual.setSurtidores(surtidoresAct);
                break;

            } else if (surtidor.getCola() < colaMinima) {
                colaMinima = surtidor.getCola();
                menorSurtidorSubIndice = surtidor.getSubindice();
            }
        }
        if (!algunoLibre) {
            surtidoresAct.get(menorSurtidorSubIndice).sumarCola();
            surtidoresAct.get(menorSurtidorSubIndice).agregarCliente(new ClienteCombustible("EA",filaActual.getRelojActual()));
            filaActual.setSurtidores(surtidoresAct);
        }

    }

    public void llegadaLavado(){
        boolean algunoLibre = false;
        int menorEstacionSubIndice = 0;
        filaActual.setLlegadaLavado(new LlegadaLavado(4,reloj));

        ArrayList<EstacionLavado> estacionesAct = filaAnterior.getEstacionesLavado();
        for (EstacionLavado estacionLavado : filaAnterior.getEstacionesLavado()) {
            int colaMinima = Integer.MAX_VALUE;
            if (estacionLavado.esLibre()) {
                estacionesAct.get(estacionLavado.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionLavado(new FinAtencionLavado(Double.parseDouble(views.media_atencion_lavado.getText()),estacionLavado.getSubindice(),reloj));
                estacionesAct.get(estacionLavado.getSubindice()).agregarCliente(new ClienteLavado("SA",filaActual.getRelojActual()));
                filaActual.setEstacionesLavado(estacionesAct);
                break;

            } else if (estacionLavado.getCola() < colaMinima) {
                colaMinima = estacionLavado.getCola();
                menorEstacionSubIndice = estacionLavado.getSubindice();
            }
        }
        if (!algunoLibre) {
            estacionesAct.get(menorEstacionSubIndice).sumarCola();
            estacionesAct.get(menorEstacionSubIndice).agregarCliente(new ClienteLavado("EA",filaActual.getRelojActual()));
            filaActual.setEstacionesLavado(estacionesAct);
        }

    }

    public void llegadaMantenimiento(){
        boolean algunoLibre = false;
        int menorEstacionSubIndice = 0;
        filaActual.setLlegadaMantenimiento(new LlegadaMantenimiento(6,reloj));

        ArrayList<EstacionMantenimiento> estacionesAct = filaAnterior.getEstacionesMantenimiento();
        for (EstacionMantenimiento estacionMantenimiento : filaAnterior.getEstacionesMantenimiento()) {
            int colaMinima = Integer.MAX_VALUE;
            if (estacionMantenimiento.esLibre()) {
                estacionesAct.get(estacionMantenimiento.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionMantenimiento(new FinAtencionMantenimiento(Double.parseDouble(views.media_atencion_mantenimiento.getText()),estacionMantenimiento.getSubindice(),reloj));
                estacionesAct.get(estacionMantenimiento.getSubindice()).agregarCliente(new ClienteMantenimiento("SA",filaActual.getRelojActual()));
                filaActual.setEstacionesMantenimiento(estacionesAct);
                break;

            } else if (estacionMantenimiento.getCola() < colaMinima) {
                colaMinima = estacionMantenimiento.getCola();
                menorEstacionSubIndice = estacionMantenimiento.getSubindice();
            }
        }
        if (!algunoLibre) {
            estacionesAct.get(menorEstacionSubIndice).sumarCola();
            estacionesAct.get(menorEstacionSubIndice).agregarCliente(new ClienteMantenimiento("EA",filaActual.getRelojActual()));
            filaActual.setEstacionesMantenimiento(estacionesAct);
        }

    }

    public void llegadaCaja() {
        boolean algunoLibre = false;
        int menorCajaSubIndice = 0;
        filaActual.setLlegadaCaja(new LlegadaCaja(1.5,reloj));
        //hago una copia de los surtidores anteriores para no cambiarlos y actualizarlos para el actual
        ArrayList<Caja> cajasAct = filaAnterior.getCajas();
        for (Caja caja : filaAnterior.getCajas()) {
            int colaMinima = Integer.MAX_VALUE;
            if (caja.esLibre()) {
                cajasAct.get(caja.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.agregarFinAtencionCaja(new FinAtencionCaja(Double.parseDouble(views.media_atencion_caja.getText()), caja.getSubindice(),reloj));
                cajasAct.get(caja.getSubindice()).agregarCliente(new ClienteCaja("SA", filaActual.getRelojActual()));
                filaActual.setCajas(cajasAct);
                break;

            } else if (caja.getCola() < colaMinima) {
                colaMinima = caja.getCola();
                menorCajaSubIndice = caja.getSubindice();
            }
        }
        if (!algunoLibre) {
            cajasAct.get(menorCajaSubIndice).sumarCola();
            cajasAct.get(menorCajaSubIndice).agregarCliente(new ClienteCaja("EA",filaActual.getRelojActual()));
            filaActual.setCajas(cajasAct);
        }
    }

    public void calcularFinAtencionCombustible(FinAtencionCombustible objetoFin) {
        int indice = objetoFin.getSubindice();
        Surtidor surtidor = filaAnterior.getSurtidor(indice);
        filaAnterior.getSurtidor(indice).eliminarCliente();
        if (surtidor.getCola() == 0) {
            surtidor.setEstado("libre");


        } else {
            surtidor.restarCola();
            ClienteCombustible cliente = surtidor.buscarSiguiente();
            filaActual.actualizarEsperaCombustible(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosCombustible();

            filaActual.agregarFinAtencionCombustible(new FinAtencionCombustible(Double.parseDouble(views.media_atencion_combustible.getText()),surtidor.getSubindice(),reloj));

        }

    }
    public void calcularFinAtencionLavado(FinAtencionLavado objetoFin) {
        int indice = objetoFin.getSubindice();
        EstacionLavado estacionLavado = filaAnterior.getEstacionLavado(indice);
        estacionLavado.eliminarCliente();
        if (estacionLavado.getCola() == 0) {
            estacionLavado.setEstado("libre");

        } else {
            estacionLavado.restarCola();
            // Busco siguiente cliente y calculo tiempo espera y sumo contador
            ClienteLavado cliente = estacionLavado.buscarSiguiente();
            filaActual.actualizarEsperaLavado(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosLavado();
            filaActual.agregarFinAtencionLavado(new FinAtencionLavado(Double.parseDouble(views.media_atencion_lavado.getText()),estacionLavado.getSubindice(),reloj));

        }
    }
    public void calcularFinAtencionMantenimiento(FinAtencionMantenimiento objetoFin) {
        int indice = objetoFin.getSubindice();
        EstacionMantenimiento estacionMantenimiento = filaAnterior.getEstacionMantenimiento(indice);
        estacionMantenimiento.eliminarCliente();
        if (estacionMantenimiento.getCola() == 0) {
            estacionMantenimiento.setEstado("libre");

        } else {
            estacionMantenimiento.restarCola();
            // Busco siguiente cliente y calculo tiempo espera y sumo contador
            ClienteMantenimiento cliente = estacionMantenimiento.buscarSiguiente();
            filaActual.actualizarEsperaMantenimiento(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosMantenimiento();
            filaActual.agregarFinAtencionMantenimiento(new FinAtencionMantenimiento(Double.parseDouble(views.media_atencion_mantenimiento.getText()),estacionMantenimiento.getSubindice(),reloj));
        }
    }
    public void calcularFinAtencionCaja(FinAtencionCaja objetoFin) {
        int indice = objetoFin.getSubindice();
        Caja caja = filaAnterior.getCaja(indice);
        caja.eliminarCliente();
        if (caja.getCola() == 0) {
            caja.setEstado("libre");

        } else {
            caja.restarCola();
            // Busco siguiente cliente y calculo tiempo espera y sumo contador
            ClienteCaja cliente = caja.buscarSiguiente();
            filaActual.actualizarEsperaCaja(reloj - cliente.getTiempoLlegada());
            filaActual.actualizarAtendidosCaja();
            filaActual.agregarFinAtencionCaja(new FinAtencionCaja(Double.parseDouble(views.media_atencion_caja.getText()),caja.getSubindice(),reloj));

        }
    }

    private void mostrarSimulaciones() {
            model = (DefaultTableModel) views.tabla_servicios.getModel();
     
            for (int i = 0; i < simulacionesRango.size(); i++) {
               
                Object[] row = new Object[15];
                row[0] = String.format("%.2f",simulacionesRango.get(i).getRelojActual());
                row[1] = String.format("%.2f",simulacionesRango.get(i).getLlegadaCombustible().getProxLlegada());
                row[2] = String.format("%.2f",simulacionesRango.get(i).getLlegadaLavado().getProxLlegada());
                row[3] = String.format("%.2f",simulacionesRango.get(i).getLlegadaMantenimiento().getProxLlegada());
                row[4] = String.format("%.2f",simulacionesRango.get(i).getLlegadaCaja().getProxLlegada());
                  
               
                row[5] =(simulacionesRango.get(i).getFinAtencionCombustible().get(0) != null) ?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(0).getProxFin()): ' ';
                
                row[6] = (simulacionesRango.get(i).getFinAtencionCombustible().get(1) != null)?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(1).getProxFin()): ' ';
                
                row[7] = (simulacionesRango.get(i).getFinAtencionCombustible().get(2) != null)?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(2).getProxFin()):' ';
                
                row[8] = ((simulacionesRango.get(i).getFinAtencionCombustible().get(3) != null))?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCombustible().get(3).getProxFin()):' ';
                
                row[9] = ((simulacionesRango.get(i).getFinAtencionLavado().get(0) != null))?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionLavado().get(0).getProxFin()):' ';        
                
                row[10] = ((simulacionesRango.get(i).getFinAtencionLavado().get(1) != null))?
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionLavado().get(1).getProxFin()):' '; 
                
                row[11] = ((simulacionesRango.get(i).getFinAtencionMantenimiento().get(0) != null))? 
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionMantenimiento().get(0).getProxFin()):' '; 
                
                row[12] =  ((simulacionesRango.get(i).getFinAtencionMantenimiento().get(1) != null))? 
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionMantenimiento().get(1).getProxFin()):' '; 
                
                row[13] = ((simulacionesRango.get(i).getFinAtencionCaja().get(0) != null))?  
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCaja().get(0).getProxFin()):' '; 
                
                row[14] = ((simulacionesRango.get(i).getFinAtencionCaja().get(1) != null))?  
                        String.format("%.2f",simulacionesRango.get(i).getFinAtencionCaja().get(1).getProxFin()):' '; 
                model.addRow(row);
                
            }
            views.tabla_servicios.setModel(model);
        
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
   

}
