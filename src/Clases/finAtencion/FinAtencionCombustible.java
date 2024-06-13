package Clases.finAtencion;


import Clases.interfaces.FinAtencion;

public class FinAtencionCombustible implements FinAtencion, Cloneable {
    private double constante;
    private int subindice;
    private double proxFin;
    @Override
    public double getProxFin() {
        return proxFin;
    }

    public FinAtencionCombustible(double constante, int subindice,double reloj) {
        this.constante = constante;
        this.subindice = subindice;
        this.proxFin = reloj + constante;
    }



    public int getSubindice() {
        return subindice;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "FinAtencionCombustible{" + "subindice=" + subindice + ", proxFin=" + proxFin + '}';
    }
    
    

}
