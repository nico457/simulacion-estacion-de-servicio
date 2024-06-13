package Clases.Servidores;


import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteMantenimiento;

import java.util.ArrayList;

public class EstacionMantenimiento implements Cloneable {
    private String estado;
    private int subindice;
    private int cola;
    private ArrayList<ClienteMantenimiento> clientesMantenimiento;

    public EstacionMantenimiento(int subindice) {
        estado = "libre";
        this.subindice = subindice;
        clientesMantenimiento = new ArrayList<>();
    }

    public ArrayList<ClienteMantenimiento> getClientesMantenimiento() {
        return clientesMantenimiento;
    }

    public void setClientesMantenimiento(ArrayList<ClienteMantenimiento> clientesMantenimiento) {
        this.clientesMantenimiento = clientesMantenimiento;
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

    public void agregarCliente(ClienteMantenimiento cliente){
        clientesMantenimiento.add(cliente);
    }

    public void eliminarCliente() {
        clientesMantenimiento.remove(getCliente());
    }

    public ClienteMantenimiento getCliente() {
        for (ClienteMantenimiento cliente : clientesMantenimiento) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
    }
    public ClienteMantenimiento buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteMantenimiento clienteMenor = null;
        for (ClienteMantenimiento cliente : clientesMantenimiento) {
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
     public int getCola() {
        return cola;
    }

     public void setCola() {
        cola =  clientesMantenimiento.size();    
    }
}
