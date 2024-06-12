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
    }
}
