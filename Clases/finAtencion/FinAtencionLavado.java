package Clases.finAtencion;

import Clases.interfaces.FinAtencion;

public class FinAtencionLavado implements FinAtencion {
    private double constante;
    private int subindice;
    @Override
    public double getProxFin(double reloj) {
        return constante + reloj;
    }

    public FinAtencionLavado(double constante, int subindice) {
        this.constante = constante;
        this.subindice = subindice;
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
