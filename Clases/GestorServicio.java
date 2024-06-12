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

import java.util.ArrayList;

public class GestorServicio {
    private double reloj;
    private int cantSimulaciones;
    private int limiteSuperior;
    private int limiteInferior;
    private float acumEsperaCombustible;
    private float acumEsperaMantenimiento;
    private float acumEsperaLavado;
    private int clientesAtendidosCombustible;
    private int clientesAtendidosMantenimiento;
    private int clientesAtendidosLavado;
    private ArrayList<Simulacion> simulacionesRango;
    private Simulacion filaActual;
    private Simulacion filaAnterior;


    public GestorServicio() {
        this.reloj = 0;
        this.cantSimulaciones = 10;
        this.limiteSuperior = 3;
        this.limiteInferior = 1;
        this.acumEsperaCombustible = 0;
        this.acumEsperaMantenimiento = 0;
        this.acumEsperaLavado = 0;
        this.clientesAtendidosCombustible = 0;
        this.clientesAtendidosMantenimiento = 0;
        this.clientesAtendidosLavado = 0;
        this.simulacionesRango = new ArrayList<>();
        this.filaActual = new Simulacion();
        this.filaAnterior = new Simulacion();
    }

    public ArrayList<Simulacion> generarSimulacion() {

        filaActual.inicializar(1.5,2,4,6);
        filaAnterior = filaActual.clone(); // para la primer iteracion fila anterior es igual a la actual
        for (int i = 1; i < cantSimulaciones; i++) {
            Object proximoEvento = filaAnterior.calcularProximoEvento();
            //LLEGADAS
            if (proximoEvento instanceof Llegada) {
                filaActual.setRelojActual(((Llegada) proximoEvento).getProxLlegada(reloj));
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
                filaActual.setRelojActual(((FinAtencion) proximoEvento).getProxFin(reloj));
                reloj = filaActual.getRelojActual();
                //FIN ATENCION COMBUSTIBLE
                if (proximoEvento instanceof FinAtencionCombustible) {
                    FinAtencionCombustible objetoFin = (FinAtencionCombustible) proximoEvento;
                    calcularFinAtencionCombustible(objetoFin);
                } else if (proximoEvento instanceof FinAtencionLavado) {
                    //FIN ATENCION LAVADO
                } else if (proximoEvento instanceof FinAtencionMantenimiento) {
                    //FIN ATENCION MANTENIMIENTO
                } else {
                    //FIN ATENCION CAJA

                }

            }

            if (i >= limiteInferior && i <= limiteSuperior) {
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
        filaActual.setLlegadaCombustible(new LlegadaCombustible(2));
        //hago una copia de los surtidores anteriores para no cambiarlos y actualizarlos para el actual
        ArrayList<Surtidor> surtidoresAct = filaAnterior.getSurtidores();
        for (Surtidor surtidor : filaAnterior.getSurtidores()) {
            int colaMinima = Integer.MAX_VALUE;
            if (surtidor.esLibre()) {
                surtidoresAct.get(surtidor.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.setFinAtencionCombustible(new FinAtencionCombustible(3,surtidor.getSubindice()));
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
        filaActual.setLlegadaLavado(new LlegadaLavado(4));

        ArrayList<EstacionLavado> estacionesAct = filaAnterior.getEstacionesLavado();
        for (EstacionLavado estacionLavado : filaAnterior.getEstacionesLavado()) {
            int colaMinima = Integer.MAX_VALUE;
            if (estacionLavado.esLibre()) {
                estacionesAct.get(estacionLavado.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.setFinAtencionLavado(new FinAtencionLavado(6,estacionLavado.getSubindice()));
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
        filaActual.setLlegadaMantenimiento(new LlegadaMantenimiento(6));

        ArrayList<EstacionMantenimiento> estacionesAct = filaAnterior.getEstacionesMantenimiento();
        for (EstacionMantenimiento estacionMantenimiento : filaAnterior.getEstacionesMantenimiento()) {
            int colaMinima = Integer.MAX_VALUE;
            if (estacionMantenimiento.esLibre()) {
                estacionesAct.get(estacionMantenimiento.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.setFinAtencionMantenimiento(new FinAtencionMantenimiento(12,estacionMantenimiento.getSubindice()));
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
        filaActual.setLlegadaCaja(new LlegadaCaja(1.5));
        //hago una copia de los surtidores anteriores para no cambiarlos y actualizarlos para el actual
        ArrayList<Caja> cajasAct = filaAnterior.getCajas();
        for (Caja caja : filaAnterior.getCajas()) {
            int colaMinima = Integer.MAX_VALUE;
            if (caja.esLibre()) {
                cajasAct.get(caja.getSubindice()).setEstado("ocupado");
                algunoLibre = true;
                //aca se actualizan los surtidores actuales
                filaActual.setFinAtencionCaja(new FinAtencionCaja(4, caja.getSubindice()));
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
        surtidor.eliminarCliente();
        if (surtidor.getCola() == 0) {
            surtidor.setEstado("libre");
        } else {
            surtidor.restarCola();
            surtidor.buscarSiguiente();
            filaActual.setFinAtencionCombustible(new FinAtencionCombustible(3,surtidor.getSubindice()));

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
            estacionLavado.buscarSiguiente();
            filaActual.setFinAtencionLavado(new FinAtencionLavado(6,estacionLavado.getSubindice()));

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
            estacionMantenimiento.buscarSiguiente();
            filaActual.setFinAtencionMantenimiento(new FinAtencionMantenimiento(12,estacionMantenimiento.getSubindice()));

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
            caja.buscarSiguiente();
            filaActual.setFinAtencionCaja(new FinAtencionCaja(4,caja.getSubindice()));

        }
    }

}
