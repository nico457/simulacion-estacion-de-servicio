package Clases.Servidores;

import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteCombustible;
import Clases.clientes.ClienteLavado;

import java.util.ArrayList;

public class EstacionLavado {
    private String estado;
    private int cola;
    private int subindice;
    private ArrayList<ClienteLavado> clientesLavado;

    public EstacionLavado(int subindice) {
        estado = "libre";
        cola = 0;
        this.subindice = subindice;
        clientesLavado = new ArrayList<>();
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

    public void agregarCliente(ClienteLavado cliente){
        clientesLavado.add(cliente);
    }
    public void eliminarCliente() {
        clientesLavado.remove(getCliente());
    }

    public ClienteLavado getCliente() {
        for (ClienteLavado cliente : clientesLavado) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
    }
    public void buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteLavado clienteMenor = null;
        for (ClienteLavado cliente : clientesLavado) {
            if (cliente.getTiempoLlegada() <= menor) {
                clienteMenor = cliente;
            }
        }
        clienteMenor.setEstado("SA");
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
