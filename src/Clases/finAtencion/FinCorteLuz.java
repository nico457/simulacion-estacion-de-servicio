package Clases.finAtencion;

import Clases.interfaces.FinAtencion;

public class FinCorteLuz implements Cloneable{

    private double enfriamientoTermica;

    public FinCorteLuz(double reloj) {
        this.enfriamientoTermica = calcularTiempoEnfriamiento (reloj);
    }

    public double calcularTiempoEnfriamiento(double c0){
        double h = 0.1;
        double c = c0;
        double t;
        for (t = 0; c >= 0 ; t+=h) {
            double k1 = 0.025 * t - 0.5 * c - 12.85;
            double k2 = 0.025 * (t +h/2)  - 0.5 * (c + h/2 * k1) - 12.85;
            double k3 = 0.025 * (t +h/2)  - 0.5 * (c + h/2 * k2) - 12.85;
            double k4 = 0.025 * (t +h)  - 0.5 * (c + h * k3) - 12.85;
            c = c + h/6 * (k1 + 2*k2 + 2*k3 + k4);
        }

        return (t*0.5) + c0;
    }

    public double getEnfriamientoTermica() {
        return enfriamientoTermica;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}