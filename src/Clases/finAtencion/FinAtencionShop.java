
package Clases.finAtencion;

import Clases.interfaces.FinAtencion;

public class FinAtencionShop implements FinAtencion,Cloneable {
    private double constante;
    private double proxFin;
    
    @Override
    public double getProxFin() {
        return proxFin;
    }

    public FinAtencionShop(double constante,double reloj) {
        this.constante = constante;
        this.proxFin = reloj + constante;
    }


    public void setConstante(double constante) {
        this.constante = constante;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }   
    
    
    
}
