package Clases.finAtencion;


import Clases.interfaces.FinAtencion;

public class FinAtencionCombustible implements FinAtencion {
    private double constante;
    private int subindice;
    @Override
    public double getProxFin(double reloj) {
        return constante + reloj;
    }

    public FinAtencionCombustible(double constante, int subindice) {
        this.constante = constante;
        this.subindice = subindice;
    }



    public int getSubindice() {
        return subindice;
    }

}
