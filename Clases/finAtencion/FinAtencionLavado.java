package Clases.finAtencion;

import Clases.interfaces.FinAtencion;

public class FinAtencionLavado implements FinAtencion {
    private double constante;
    private int subindice;

    private double proxFin;
    @Override
    public double getProxFin() {
        return proxFin;
    }

    public FinAtencionLavado(double constante, int subindice,double reloj) {
        this.constante = constante;
        this.subindice = subindice;
        this.proxFin = reloj + constante;
    }


    public double getConstante() {
        return constante;
    }

    public void setConstante(double constante) {
        this.constante = constante;
    }

    public int getSubindice() {
        return subindice;
    }

    public void setSubindice(int subindice) {
        this.subindice = subindice;
    }
}
