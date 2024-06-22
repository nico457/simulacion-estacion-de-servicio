package Clases.llegadas;

import Clases.interfaces.Llegada;

public class LlegadaCorteDeLuz implements Llegada,Cloneable {

    private double rnd;
    private double llegada;
    private double proxLlegada;

    @Override
    public double getProxLlegada() {
        return proxLlegada;
    }
    public LlegadaCorteDeLuz(double reloj) {
        this.rnd = Math.random();
        this.llegada = calcularLlegada(rnd);
        this.proxLlegada = reloj + llegada;
    }
    public int calcularLlegada(double random){
        int t = 2;

        if(random < 0.20){
            return 4 * t;
        }
        if(random < 0.60){
            return 6 * t;
        }
        return 8 * t;
    }
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}

