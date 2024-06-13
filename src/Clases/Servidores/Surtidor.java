package Clases.Servidores;

import Clases.clientes.ClienteCombustible;

import java.util.ArrayList;


public class Surtidor implements Cloneable {
    private String estado;
    private int subindice;
    private ArrayList<ClienteCombustible> clientesCombustible;
    private int cola;

    public Surtidor(int subindice) {
        estado = "libre";
        this.subindice = subindice;
        clientesCombustible = new ArrayList<>();
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public ArrayList<ClienteCombustible> getClientesCombustible() {
        return clientesCombustible;
    }

    public void setClientesCombustible(ArrayList<ClienteCombustible> clientesCombustible) {
        this.clientesCombustible = clientesCombustible;
    }
    

    public ClienteCombustible getCliente() {
        for (ClienteCombustible cliente : clientesCombustible) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
    }
    public ClienteCombustible buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteCombustible clienteMenor = null;
        for (ClienteCombustible cliente : clientesCombustible) {
            if (cliente.getTiempoLlegada() <= menor) {
                clienteMenor = cliente;
            }
        }
        clienteMenor.setEstado("SA");
        
        return clienteMenor;
    }
         @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public int getCola(){
        return cola;
    }

    public void setCola() {
             cola =  clientesCombustible.size();
       
    }
    
}
