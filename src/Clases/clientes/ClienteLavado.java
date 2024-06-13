package Clases.clientes;

public class ClienteLavado {
    private String estado;
    private double tiempoLlegada;

    public ClienteLavado(String estado, double tiempoLlegada) {
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
}
