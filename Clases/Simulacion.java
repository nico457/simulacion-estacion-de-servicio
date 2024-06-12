package Clases;

import Clases.Servidores.Caja;
import Clases.Servidores.EstacionLavado;
import Clases.Servidores.EstacionMantenimiento;
import Clases.Servidores.Surtidor;
import Clases.finAtencion.FinAtencionCaja;
import Clases.finAtencion.FinAtencionCombustible;
import Clases.finAtencion.FinAtencionLavado;
import Clases.finAtencion.FinAtencionMantenimiento;
import Clases.llegadas.LlegadaCaja;
import Clases.llegadas.LlegadaCombustible;
import Clases.llegadas.LlegadaLavado;
import Clases.llegadas.LlegadaMantenimiento;


import java.util.ArrayList;

public class Simulacion implements Cloneable {
    private int linea;
    private double relojActual;

    // Llegadas
    private LlegadaCaja llegadaCaja;
    private LlegadaCombustible llegadaCombustible;
    private LlegadaLavado llegadaLavado;
    private LlegadaMantenimiento llegadaMantenimiento;

    // Fin de atencion
    private FinAtencionCombustible finAtencionCombustible;
    private FinAtencionLavado finAtencionLavado;
    private FinAtencionMantenimiento finAtencionMantenimiento;
    private FinAtencionCaja finAtencionCaja;

    // Objetos permanentes
    private ArrayList<Caja> cajas;
    private ArrayList<Surtidor> surtidores;
    private ArrayList<EstacionMantenimiento> estacionesMantenimiento;
    private ArrayList<EstacionLavado> estacionesLavado;

    // Contadores y Acumuladores Actividades
    private double acumEsperaCombustible;
    private double acumEsperaLavado;
    private double acumEsperaMantenimiento;
    private double acumEsperaCaja;
    private int contCombustibleAtendidos;
    private int contLavadoAtendidos;
    private int contMantenimientoAtendidos;
    private int contCajaAtendidos;


    public void inicializar(double mediaCaja,double mediaCombustible, double mediaLavado, double mediaMantenimiento){
        this.linea = 0;
        this.relojActual = 0;
        this.llegadaCaja = new LlegadaCaja(mediaCaja,relojActual);
        this.llegadaCombustible = new LlegadaCombustible(mediaCombustible,relojActual);
        this.llegadaLavado = new LlegadaLavado(mediaLavado,relojActual);
        this.llegadaMantenimiento = new LlegadaMantenimiento(mediaMantenimiento,relojActual);
        this.cajas = new ArrayList<>(2);
        this.cajas.add(new Caja(0));
        this.cajas.add(new Caja(1));
        this.surtidores = new ArrayList<>(4);
        this.surtidores.add(new Surtidor(0));
        this.surtidores.add(new Surtidor(1));
        this.surtidores.add(new Surtidor(2));
        this.surtidores.add(new Surtidor(3));
        this.estacionesMantenimiento = new ArrayList<>(2);
        this.estacionesMantenimiento.add(new EstacionMantenimiento(0));
        this.estacionesLavado = new ArrayList<>(2);
        this.estacionesLavado.add(new EstacionLavado(0));

    }

    public Object calcularProximoEvento(){
        double min = findMin(llegadaCaja.getProxLlegada(),
                llegadaCombustible.getProxLlegada(),
                llegadaLavado.getProxLlegada(),
                llegadaMantenimiento.getProxLlegada(),
                (finAtencionCombustible != null) ? finAtencionCombustible.getProxFin() : -1,
                (finAtencionLavado != null) ? finAtencionLavado.getProxFin() : -1,
                (finAtencionMantenimiento != null) ? finAtencionMantenimiento.getProxFin() : -1);

        if (min == llegadaCaja.getProxLlegada()) {
            return llegadaCaja;
        }
        if (min == llegadaCombustible.getProxLlegada()) {
            return llegadaCombustible;
        }
        if (min == llegadaLavado.getProxLlegada()) {
            return llegadaLavado;
        }
        if (min == llegadaMantenimiento.getProxLlegada()) {
            return llegadaMantenimiento;
        }
        if (finAtencionCombustible != null && min == finAtencionCombustible.getProxFin()) {
            return finAtencionCombustible;
        }
        if (finAtencionCombustible != null && min == finAtencionLavado.getProxFin()) {
            return finAtencionLavado;
        }
        if (finAtencionCombustible != null && min == finAtencionMantenimiento.getProxFin()) {
            return finAtencionMantenimiento;
        }
        return null;


    }
    public double findMin(double... numbers) {
        double min = Double.MAX_VALUE;
        for (double number : numbers) {
            if (number > relojActual) {
                min = Math.min(min, number);
            }

        }
        return min;
    }

    public LlegadaCaja getLlegadaCaja() {
        return llegadaCaja;
    }

    public void setLlegadaCaja(LlegadaCaja llegadaCaja) {
        this.llegadaCaja = llegadaCaja;
    }

    public LlegadaCombustible getLlegadaCombustible() {
        return llegadaCombustible;
    }

    public void setLlegadaCombustible(LlegadaCombustible llegadaCombustible) {
        this.llegadaCombustible = llegadaCombustible;
    }

    public LlegadaLavado getLlegadaLavado() {
        return llegadaLavado;
    }

    public void setLlegadaLavado(LlegadaLavado llegadaLavado) {
        this.llegadaLavado = llegadaLavado;
    }

    public LlegadaMantenimiento getLlegadaMantenimiento() {
        return llegadaMantenimiento;
    }

    public void setLlegadaMantenimiento(LlegadaMantenimiento llegadaMantenimiento) {
        this.llegadaMantenimiento = llegadaMantenimiento;
    }

    public FinAtencionCombustible getFinAtencionCombustible() {
        return finAtencionCombustible;
    }

    public void setFinAtencionCombustible(FinAtencionCombustible finAtencionCombustible) {
        this.finAtencionCombustible = finAtencionCombustible;
    }

    public FinAtencionLavado getFinAtencionLavado() {
        return finAtencionLavado;
    }

    public void setFinAtencionLavado(FinAtencionLavado finAtencionLavado) {
        this.finAtencionLavado = finAtencionLavado;
    }

    public FinAtencionMantenimiento getFinAtencionMantenimiento() {
        return finAtencionMantenimiento;
    }

    public void setFinAtencionMantenimiento(FinAtencionMantenimiento finAtencionMantenimiento) {
        this.finAtencionMantenimiento = finAtencionMantenimiento;
    }

    public FinAtencionCaja getFinAtencionCaja() {
        return finAtencionCaja;
    }

    public void setFinAtencionCaja(FinAtencionCaja finAtencionCaja) {
        this.finAtencionCaja = finAtencionCaja;
    }

    public ArrayList<Caja> getCajas() {
        return cajas;
    }
    public Caja getCaja(int indice) {
        return cajas.get(indice);
    }

    public void setCajas(ArrayList<Caja> cajas) {
        this.cajas = cajas;
    }

    public ArrayList<Surtidor> getSurtidores() {
        return surtidores;
    }

    public Surtidor getSurtidor(int indice) {
        return surtidores.get(indice);
    }


    public void setSurtidores(ArrayList<Surtidor> surtidores) {
        this.surtidores = surtidores;
    }

    public ArrayList<EstacionMantenimiento> getEstacionesMantenimiento() {
        return estacionesMantenimiento;
    }

    public void setEstacionesMantenimiento(ArrayList<EstacionMantenimiento> estacionesMantenimiento) {
        this.estacionesMantenimiento = estacionesMantenimiento;
    }

    public ArrayList<EstacionLavado> getEstacionesLavado() {
        return estacionesLavado;
    }

    public EstacionMantenimiento getEstacionMantenimiento(int indice) {
        return estacionesMantenimiento.get(indice);
    }

    public EstacionLavado getEstacionLavado(int indice) {
        return estacionesLavado.get(indice);
    }

    public void setEstacionesLavado(ArrayList<EstacionLavado> estacionesLavado) {
        this.estacionesLavado = estacionesLavado;
    }

    public double getRelojActual() {
        return relojActual;
    }

    public void setRelojActual(double relojActual) {
        this.relojActual = relojActual;
    }

    public void actualizarEsperaCombustible(double tiempo) {
        acumEsperaCombustible += tiempo;
    }

    public void actualizarEsperaLavado(double tiempo) {
        acumEsperaLavado += tiempo;
    }

    public void actualizarEsperaMantenimiento(double tiempo) {
        acumEsperaMantenimiento += tiempo;
    }

    public void actualizarEsperaCaja(double tiempo) {
        acumEsperaCaja += tiempo;
    }

    public void actualizarAtendidosCombustible() {
        contCombustibleAtendidos++;
    }

    public void actualizarAtendidosLavado() {
        contLavadoAtendidos++;
    }

    public void actualizarAtendidosMantenimiento() {
        contMantenimientoAtendidos++;
    }

    public void actualizarAtendidosCaja() {
        contCajaAtendidos++;
    }

    public void setLinea(int i) {
        linea = i;
    }

    public double getAcumEsperaCombustible(){
        return acumEsperaCombustible;
    }

    public double getAcumEsperaLavado(){
        return acumEsperaLavado;
    }

    public double getAcumEsperaMantenimiento(){
        return acumEsperaMantenimiento;
    }

    public double getAcumEsperaCaja(){
        return acumEsperaCaja;
    }

    public int getAtendidosCombustible(){
        return contCombustibleAtendidos;
    }

    public int getAtendidosLavado(){
        return contLavadoAtendidos;
    }

    public int getAtendidosMantenimiento(){
        return contMantenimientoAtendidos;
    }

    public int getAtendidosCaja(){
        return contCajaAtendidos;
    }

    @Override
    public String toString() {
        return "Simulacion{" +
                "\n linea=" + linea +
                "\n relojActual=" + relojActual +
                "\n llegadaCaja=" + llegadaCaja +
                "\n llegadaCombustible=" + llegadaCombustible +
                "\n llegadaLavado=" + llegadaLavado +
                "\n llegadaMantenimiento=" + llegadaMantenimiento +
                "\n finAtencionCombustible=" + finAtencionCombustible +
                "\n finAtencionLavado=" + finAtencionLavado +
                "\n finAtencionMantenimiento=" + finAtencionMantenimiento +
                "\n finAtencionCaja=" + finAtencionCaja +
                "\n cajas=" + cajas +
                "\n surtidores=" + surtidores +
                "\n estacionesMantenimiento=" + estacionesMantenimiento +
                "\n estacionesLavado=" + estacionesLavado +
                "\n acumEsperaCombustible=" + acumEsperaCombustible +
                "\n acumEsperaLavado=" + acumEsperaLavado +
                "\n acumEsperaMantenimiento=" + acumEsperaMantenimiento +
                "\n acumEsperaCaja=" + acumEsperaCaja +
                "\n contCombustiblesAtendidos=" + contCombustibleAtendidos +
                "\n contLavadoAtendidos=" + contLavadoAtendidos +
                "\n contMantenimientoAtendidos=" + contMantenimientoAtendidos +
                "\n contCajaAtendidos=" + contCajaAtendidos +
                '}';
    }
    @Override
    public Simulacion clone() {
        try {
            return (Simulacion) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }
}
