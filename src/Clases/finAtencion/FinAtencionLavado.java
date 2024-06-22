package Clases.finAtencion;

import Clases.interfaces.FinAtencion;

public class FinAtencionLavado implements FinAtencion, Cloneable {
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
    public FinAtencionLavado(int subindice,double reloj, double tr){
        this.subindice = subindice;
        this.proxFin = reloj + tr;
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
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
