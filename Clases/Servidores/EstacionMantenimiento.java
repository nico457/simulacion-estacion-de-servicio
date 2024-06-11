package Clases.Servidores;


import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteMantenimiento;

import java.util.ArrayList;

public class EstacionMantenimiento {
    private String estado;
    private int cola;
    private int subindice;
    private ArrayList<ClienteMantenimiento> clientesMantenimiento;

    public EstacionMantenimiento(int subindice) {
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

    public void agregarCliente(ClienteMantenimiento cliente){
        clientesMantenimiento.add(cliente);
    }

    public ClienteMantenimiento getCliente() {
        for (ClienteMantenimiento cliente : clientesMantenimiento) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
    }
}
