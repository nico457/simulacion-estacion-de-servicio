package Clases.finAtencion;


import Clases.interfaces.FinAtencion;

public class FinAtencionCaja implements FinAtencion {
    private double constante;

    private int subindice;
    @Override
    public double getProxFin(double reloj) {
        return constante + reloj;
    }

    public FinAtencionCaja(double constante, int subindice) {
        this.constante = constante;
        this.subindice = subindice;
    }

    public void setConstante(double constante) {
        this.constante = constante;
    }

    public int getSubindice() {
        return subindice;
    }

}
