package Clases;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Crear un objeto de la clase Persona
        GestorServicio gestor = new GestorServicio();
        ArrayList<Simulacion> simulacions= gestor.generarSimulacion();
        for (Simulacion simulacion : simulacions) {
            System.out.println(simulacion.clone().toString());
            System.out.println("---------------------------------------------------");
        }

        // Actividad 1: Calcular promedios espera
        Simulacion ultimaFila = simulacions.get(simulacions.size()-1);
        double promEsperaCombustible = ultimaFila.getAcumEsperaCombustible()/ultimaFila.getAtendidosCombustible();
        double promEsperaLavado = ultimaFila.getAcumEsperaLavado()/ultimaFila.getAtendidosLavado();
        double promEsperaMantenimiento = ultimaFila.getAcumEsperaMantenimiento()/ultimaFila.getAtendidosMantenimiento();
        double promEsperaCaja = ultimaFila.getAcumEsperaCaja()/ultimaFila.getAtendidosCaja();

        double tiempoOcupadoCombustible = 3 * ultimaFila.getAtendidosCombustible();
        double tiempoOcupadoLavado = 6 * ultimaFila.getAtendidosCombustible();
        double tiempoOcupadoMantenimiento = 12 * ultimaFila.getAtendidosCombustible();
        double tiempoOcupadoCaja = 4 * ultimaFila.getAtendidosCombustible();

        System.out.println("Actividad 1:");
        System.out.println("Promedio espera combustible: " + promEsperaCombustible + " mins");
        System.out.println("Promedio espera lavado: " + promEsperaLavado + " mins");
        System.out.println("Promedio espera mantenimiento: " + promEsperaMantenimiento + " mins");
        System.out.println("Promedio espera caja: " + promEsperaCaja + " mins");
        System.out.println("Porcentaje ocupado combustible: " + (tiempoOcupadoCombustible / ultimaFila.getRelojActual()));
        System.out.println("Porcentaje ocupado lavado: " + (tiempoOcupadoLavado / ultimaFila.getRelojActual()));
        System.out.println("Porcentaje ocupado mantenimiento: " + (tiempoOcupadoMantenimiento / ultimaFila.getRelojActual()));
        System.out.println("Porcentaje ocupado caja: " + (tiempoOcupadoCaja / ultimaFila.getRelojActual()));

        // Actividad 2
        double tiempoCombustible = promEsperaCombustible + 3;
        double tiempoLavado = promEsperaLavado + 6;
        double tiempoMantenimiento = promEsperaMantenimiento + 12;
        double tiempoCaja = promEsperaCaja + 4;
        
        double minimo = 0;
        String servicio = "";

        if (tiempoCombustible < tiempoLavado && tiempoCombustible < tiempoMantenimiento && tiempoCombustible < tiempoCaja){
            minimo = tiempoCombustible;
            servicio = "Combustible";
        }
        else if (tiempoLavado < tiempoCombustible && tiempoLavado < tiempoMantenimiento && tiempoLavado < tiempoCaja){
            minimo = tiempoLavado;
            servicio = "Lavado";
        }
        else if (tiempoMantenimiento < tiempoLavado && tiempoMantenimiento < tiempoCombustible && tiempoMantenimiento < tiempoCaja){
            minimo = tiempoMantenimiento;
            servicio = "Mantenimiento";
        }
        else if (tiempoCaja < tiempoLavado && tiempoCaja < tiempoMantenimiento && tiempoCaja < tiempoCombustible){
            minimo = tiempoCaja;
            servicio = "Caja";
        }
        
        System.out.println("Actividad 2:");
        System.out.println("El servicio mas rapido es: " + servicio + " - tiempo: " + minimo);

    }
}
