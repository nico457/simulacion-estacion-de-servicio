
package Clases.Servidores;

import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteShop;
import java.util.ArrayList;

public class Shop implements Cloneable {
    
    private String estado;
    private int cola;
    private ArrayList<ClienteShop> clientesShop;

    public Shop()   {
        estado = "libre";
        clientesShop = new ArrayList<>();
    }

    public ArrayList<ClienteShop> getClientesShop() {
        return clientesShop;
    }

    public void setClientesShop(ArrayList<ClienteShop> clientesShop) {
        this.clientesShop = clientesShop;
    }
    


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void agregarCliente(ClienteShop cliente){
        clientesShop.add(cliente);
    }

    public void eliminarCliente() {
        clientesShop.remove(getClienteActual());
    }

    public ClienteShop buscarSiguiente(){
        double menor = Double.MAX_VALUE;
        ClienteShop clienteMenor = null;
        for (ClienteShop cliente : clientesShop) {
            if (cliente.getTiempoLlegada() <= menor) {
                clienteMenor = cliente;
            }
        }
        clienteMenor.setEstado("SA");
        
        return clienteMenor;
    }

    public ClienteShop getClienteActual() {
        for (ClienteShop cliente : clientesShop) {
            if (cliente.getEstado().equals("SA")) {
                return cliente;
            }
        }
        return null;
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
        
            if(clientesShop.isEmpty()){
                cola = 0;
            }else{
                cola =  clientesShop.size()-1;
            }       
       
    }
    
}
