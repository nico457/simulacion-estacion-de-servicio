package Clases.llegadas;


import Clases.interfaces.Llegada;

public class LlegadaLavado implements Llegada, Cloneable {
    private double rnd;
    private double llegada;
    private double media;
    private double proxLlegada;

    public double getRnd() {
        return  rnd;
    }

     double getLlegada() {
        return llegada;
    }

    @Override
    public double getProxLlegada() {
        return proxLlegada;

    }

    public LlegadaLavado(double media, double reloj) {
        this.rnd = Math.random();
        this.llegada = -media * Math.log(1 - rnd);
        this.proxLlegada = reloj + llegada;
        this.media = media;
    }

    @Override
    public String toString() {
        return "LlegadaLavado{" +
                "rnd=" + rnd +
                ", llegada=" + llegada +
                '}';
    }
     @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
