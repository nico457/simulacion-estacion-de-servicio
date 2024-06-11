package Clases.llegadas;

import Clases.interfaces.Llegada;

public class LlegadaCaja implements Llegada {
    private double rnd;
    private double llegada;
    private double media;


    public double getRnd() {
        return rnd;
    }


    public double getLlegada() {
        return llegada;

    }

    @Override
    public double getProxLlegada(double reloj) {
        return reloj + llegada;

    }

    public LlegadaCaja(double media) {
        this.rnd = Math.random();
        this.llegada = -media * Math.log(1 - rnd);
        this.media = media;
    }
}