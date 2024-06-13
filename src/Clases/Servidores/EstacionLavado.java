package Clases.Servidores;

import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteCombustible;
import Clases.clientes.ClienteLavado;

import java.util.ArrayList;

public class EstacionLavado  implements Cloneable  {
    private String estado;
    private int subindice;
    private int cola;
    private ArrayList<ClienteLavado> clientesLavado;

    public EstacionLavado(int subindice){
        estado = "libre";
        this.subindice = subindice;
        clientesLavado = new ArrayList<>();
    }

    public ArrayList<ClienteLavado> getClientesLavado() {
        return clientesLavado;
    }

    public void setClientesLavado(ArrayList<ClienteLavado> clientesLavado) {
        this.clientesLavado = clientesLavado;
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
    public ClienteLavado buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteLavado clienteMenor = null;
        for (ClienteLavado cliente : clientesLavado) {
            if (cliente.getTiempoLlegada() <= menor) {
                clienteMenor = cliente;
            }
        }
        clienteMenor.setEstado("SA");
        
        return clienteMenor;
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

    public int getCola() {
        return cola;
    }

    public void setCola() {
        cola = clientesLavado.size();
    }
    
    
    


}
