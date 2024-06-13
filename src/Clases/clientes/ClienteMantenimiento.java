package Clases.clientes;

public class ClienteMantenimiento implements Cloneable {
    private String estado;
    private double tiempoLlegada;

    public ClienteMantenimiento(String estado, double tiempoLlegada) {
        this.estado = estado;
        this.tiempoLlegada = tiempoLlegada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getTiempoLlegada() {
        return tiempoLlegada;
    }

    public void setTiempoLlegada(double tiempoLlegada) {
        this.tiempoLlegada = tiempoLlegada;
    }
     @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
