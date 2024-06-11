package Clases;

import Clases.Servidores.Caja;
import Clases.Servidores.EstacionLavado;
import Clases.Servidores.EstacionMantenimiento;
import Clases.Servidores.Surtidor;
import Clases.finAtencion.FinAtencionCaja;
import Clases.finAtencion.FinAtencionCombustible;
import Clases.finAtencion.FinAtencionLavado;
import Clases.finAtencion.FinAtencionMantenimiento;
import Clases.llegadas.LlegadaCaja;
import Clases.llegadas.LlegadaCombustible;
import Clases.llegadas.LlegadaLavado;
import Clases.llegadas.LlegadaMantenimiento;


import java.util.ArrayList;

public class Simulacion {
    private double relojActual;

    // Llegadas
    private LlegadaCaja llegadaCaja;
    private LlegadaCombustible llegadaCombustible;
    private LlegadaLavado llegadaLavado;
    private LlegadaMantenimiento llegadaMantenimiento;

    // Fin de atencion
    private FinAtencionCombustible finAtencionCombustible;
    private FinAtencionLavado finAtencionLavado;
    private FinAtencionMantenimiento finAtencionMantenimiento;
    private FinAtencionCaja finAtencionCaja;

    //Objetos permanentes

    private ArrayList<Caja> cajas;

    private ArrayList<Surtidor> surtidores;

    private ArrayList<EstacionMantenimiento> estacionesMantenimiento;

    private ArrayList<EstacionLavado> estacionesLavado;


    public void inicializar(double mediaCaja,double mediaCombustible, double mediaLavado, double mediaMantenimiento){
        this.llegadaCaja = new LlegadaCaja(mediaCaja);
        this.llegadaCombustible = new LlegadaCombustible(mediaCombustible);
        this.llegadaLavado = new LlegadaLavado(mediaLavado);
        this.llegadaMantenimiento = new LlegadaMantenimiento(mediaMantenimiento);
        this.cajas = new ArrayList<>(2);
        this.surtidores = new ArrayList<>(4);

        this.estacionesMantenimiento = new ArrayList<>(2);
        this.estacionesLavado = new ArrayList<>(2);

    }

    public Object calcularProximoEvento(){
        double min = findMin(llegadaCaja.getProxLlegada(relojActual),
                llegadaCombustible.getProxLlegada(relojActual),
                llegadaLavado.getProxLlegada(relojActual),
                llegadaMantenimiento.getProxLlegada(relojActual),
                finAtencionCombustible.getProxFin(relojActual),
                finAtencionLavado.getProxFin(relojActual),
                finAtencionMantenimiento.getProxFin(relojActual));

        if (min == llegadaCaja.getProxLlegada(relojActual)) {
            return llegadaCaja;
        }
        if (min == llegadaCombustible.getProxLlegada(relojActual)) {
            return llegadaCombustible;
        }
        if (min == llegadaLavado.getProxLlegada(relojActual)) {
            return llegadaLavado;
        }
        if (min == llegadaMantenimiento.getProxLlegada(relojActual)) {
            return llegadaMantenimiento;
        }
        if (min == finAtencionCombustible.getProxFin(relojActual)) {
            return finAtencionCombustible;
        }
        if (min == finAtencionLavado.getProxFin(relojActual)) {
            return finAtencionLavado;
        }
        if (min == finAtencionMantenimiento.getProxFin(relojActual)) {
            return finAtencionMantenimiento;
        }
        return null;


    }
    public static double findMin(double... numbers) {
        double min = Double.MAX_VALUE;
        for (double number : numbers) {
            min = Math.min(min, number);
        }
        return min;
    }

    public LlegadaCaja getLlegadaCaja() {
        return llegadaCaja;
    }

    public void setLlegadaCaja(LlegadaCaja llegadaCaja) {
        this.llegadaCaja = llegadaCaja;
    }

    public LlegadaCombustible getLlegadaCombustible() {
        return llegadaCombustible;
    }

    public void setLlegadaCombustible(LlegadaCombustible llegadaCombustible) {
        this.llegadaCombustible = llegadaCombustible;
    }

    public LlegadaLavado getLlegadaLavado() {
        return llegadaLavado;
    }

    public void setLlegadaLavado(LlegadaLavado llegadaLavado) {
        this.llegadaLavado = llegadaLavado;
    }

    public LlegadaMantenimiento getLlegadaMantenimiento() {
        return llegadaMantenimiento;
    }

    public void setLlegadaMantenimiento(LlegadaMantenimiento llegadaMantenimiento) {
        this.llegadaMantenimiento = llegadaMantenimiento;
    }

    public FinAtencionCombustible getFinAtencionCombustible() {
        return finAtencionCombustible;
    }

    public void setFinAtencionCombustible(FinAtencionCombustible finAtencionCombustible) {
        this.finAtencionCombustible = finAtencionCombustible;
    }

    public FinAtencionLavado getFinAtencionLavado() {
        return finAtencionLavado;
    }

    public void setFinAtencionLavado(FinAtencionLavado finAtencionLavado) {
        this.finAtencionLavado = finAtencionLavado;
    }

    public FinAtencionMantenimiento getFinAtencionMantenimiento() {
        return finAtencionMantenimiento;
    }

    public void setFinAtencionMantenimiento(FinAtencionMantenimiento finAtencionMantenimiento) {
        this.finAtencionMantenimiento = finAtencionMantenimiento;
    }

    public FinAtencionCaja getFinAtencionCaja() {
        return finAtencionCaja;
    }

    public void setFinAtencionCaja(FinAtencionCaja finAtencionCaja) {
        this.finAtencionCaja = finAtencionCaja;
    }

    public ArrayList<Caja> getCajas() {
        return cajas;
    }

    public void setCajas(ArrayList<Caja> cajas) {
        this.cajas = cajas;
    }

    public ArrayList<Surtidor> getSurtidores() {
        return surtidores;
    }

    public Surtidor getSurtidor(int indice) {
        return surtidores.get(indice-1);
    }

    public void setSurtidores(ArrayList<Surtidor> surtidores) {
        this.surtidores = surtidores;
    }

    public ArrayList<EstacionMantenimiento> getEstacionesMantenimiento() {
        return estacionesMantenimiento;
    }

    public void setEstacionesMantenimiento(ArrayList<EstacionMantenimiento> estacionesMantenimiento) {
        this.estacionesMantenimiento = estacionesMantenimiento;
    }

    public ArrayList<EstacionLavado> getEstacionesLavado() {
        return estacionesLavado;
    }

    public void setEstacionesLavado(ArrayList<EstacionLavado> estacionesLavado) {
        this.estacionesLavado = estacionesLavado;
    }



    public double getRelojActual() {
        return relojActual;
    }

    public void setRelojActual(double relojActual) {
        this.relojActual = relojActual;
    }
}
