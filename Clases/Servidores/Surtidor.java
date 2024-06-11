package Clases.Servidores;

import Clases.clientes.ClienteCombustible;

import java.util.ArrayList;


public class Surtidor {
    private String estado;
    private int cola;
    private int subindice;
    private ArrayList<ClienteCombustible> clientesCombustible;

    public Surtidor(int subindice) {
        estado = "libre";
        cola = 0;
        this.subindice = subindice;
    }

    public void sumarCola() {
        cola++;
    }
    public void restarCola() {
        cola--;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCola() {
        return cola;
    }

    public int getSubindice() {
        return subindice;
    }
    public boolean esLibre() {
        return estado.equals("libre");
    }

    public void agregarCliente(ClienteCombustible cliente){
        clientesCombustible.add(cliente);
    }

    public void eliminarCliente() {
        clientesCombustible.remove(getCliente());
    }

    public ClienteCombustible getCliente() {
        for (ClienteCombustible cliente : clientesCombustible) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
    }
    public void buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteCombustible clienteMenor = null;
        for (ClienteCombustible cliente : clientesCombustible) {
            if (cliente.getTiempoLlegada() <= menor) {
                clienteMenor = cliente;
            }
        }
        clienteMenor.setEstado("SA");
    }
}
