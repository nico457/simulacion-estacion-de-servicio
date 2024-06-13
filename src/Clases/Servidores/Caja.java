package Clases.Servidores;

import Clases.clientes.ClienteCaja;

import java.util.ArrayList;

public class Caja implements Cloneable {
    private String estado;
    private int subindice;
    private ArrayList<ClienteCaja> clientesCaja;

    public Caja(int subindice) {
        estado = "libre";
        this.subindice = subindice;
        clientesCaja = new ArrayList<>();
    }

    public ArrayList<ClienteCaja> getClientesCaja() {
        return clientesCaja;
    }

    public void setClientesCaja(ArrayList<ClienteCaja> clientesCaja) {
        this.clientesCaja = clientesCaja;
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

    public ClienteCaja buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteCaja clienteMenor = null;
        for (ClienteCaja cliente : clientesCaja) {
            if (cliente.getTiempoLlegada() <= menor) {
                clienteMenor = cliente;
            }
        }
        clienteMenor.setEstado("SA");
        
        return clienteMenor;
    }

    public ClienteCaja getClienteActual() {
        for (ClienteCaja cliente : clientesCaja) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
    }


    public int getSubindice() {
        return subindice;
    }
    public boolean esLibre() {
        return estado.equals("libre");
    }
         @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
