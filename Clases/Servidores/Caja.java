package Clases.Servidores;

import Clases.clientes.ClienteCaja;

import java.util.ArrayList;

public class Caja {
    private String estado;
    private int cola;
    private int subindice;
    private ArrayList<ClienteCaja> clientesCaja;

    public Caja(int subindice) {
        estado = "libre";
        cola = 0;
        this.subindice = subindice;
        clientesCaja = new ArrayList<>();
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

    public void agregarCliente(ClienteCaja cliente){
        clientesCaja.add(cliente);
    }

    public void eliminarCliente() {
        clientesCaja.remove(getClienteActual());
    }

    public void buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteCaja clienteMenor = null;
        for (ClienteCaja cliente : clientesCaja) {
            if (cliente.getTiempoLlegada() <= menor) {
                clienteMenor = cliente;
            }
        }
        clienteMenor.setEstado("SA");
    }

    public ClienteCaja getClienteActual() {
        for (ClienteCaja cliente : clientesCaja) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
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


}
