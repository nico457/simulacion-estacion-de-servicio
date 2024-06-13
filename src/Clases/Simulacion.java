package Clases;

import Clases.Servidores.Caja;
import Clases.Servidores.EstacionLavado;
import Clases.Servidores.EstacionMantenimiento;
import Clases.Servidores.Surtidor;
import Clases.clientes.ClienteCombustible;
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
    private double relojActual;

    // Llegadas
    private LlegadaCaja llegadaCaja;
    private LlegadaCombustible llegadaCombustible;
    private LlegadaLavado llegadaLavado;
    private LlegadaMantenimiento llegadaMantenimiento;

    // Fin de atencion
    private ArrayList<FinAtencionCombustible> finAtencionCombustible;
    private ArrayList<FinAtencionLavado> finAtencionLavado;
    private ArrayList<FinAtencionMantenimiento> finAtencionMantenimiento;
    private ArrayList<FinAtencionCaja> finAtencionCaja;

    //Objetos permanentes

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
        this.relojActual = 0;
        this.llegadaCaja = new LlegadaCaja(mediaCaja,relojActual);
        this.llegadaCombustible = new LlegadaCombustible(mediaCombustible,relojActual);
        this.llegadaLavado = new LlegadaLavado(mediaLavado,relojActual);
        this.llegadaMantenimiento = new LlegadaMantenimiento(mediaMantenimiento,relojActual);
        
        this.finAtencionCombustible = new ArrayList<>();
        this.finAtencionCombustible.add(null);
        this.finAtencionCombustible.add(null);
        this.finAtencionCombustible.add(null);
        this.finAtencionCombustible.add(null);
        this.finAtencionLavado = new ArrayList<>();
        this.finAtencionLavado.add(null);
        this.finAtencionLavado.add(null);
        this.finAtencionMantenimiento = new ArrayList<>();
        this.finAtencionMantenimiento.add(null);
        this.finAtencionMantenimiento.add(null);
        this.finAtencionCaja = new ArrayList<>();
        this.finAtencionCaja.add(null);
        this.finAtencionCaja.add(null);
        
     
        
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
        this.estacionesMantenimiento.add(new EstacionMantenimiento(1));
        
        this.estacionesLavado = new ArrayList<>(2);
        this.estacionesLavado.add(new EstacionLavado(0));
        this.estacionesLavado.add(new EstacionLavado(1));
    }

    public Object calcularProximoEvento(){
        double min = findMin(llegadaCaja.getProxLlegada(),
                llegadaCombustible.getProxLlegada(),
                llegadaLavado.getProxLlegada(),
                llegadaMantenimiento.getProxLlegada(),
                (finAtencionCombustible.get(0) != null) ? finAtencionCombustible.get(0).getProxFin() : -1,
                (finAtencionCombustible.get(1) != null) ? finAtencionCombustible.get(1).getProxFin() : -1,
                (finAtencionCombustible.get(2) != null) ? finAtencionCombustible.get(2).getProxFin() : -1,
                (finAtencionCombustible.get(3) != null) ? finAtencionCombustible.get(3).getProxFin() : -1,
                (finAtencionLavado.get(0) != null) ? finAtencionLavado.get(0).getProxFin() : -1,
                (finAtencionLavado.get(1) != null) ? finAtencionLavado.get(1).getProxFin() : -1,
                (finAtencionMantenimiento.get(0) != null) ? finAtencionMantenimiento.get(0).getProxFin() : -1,
                (finAtencionMantenimiento.get(1) != null) ? finAtencionMantenimiento.get(1).getProxFin() : -1,
                (finAtencionCaja.get(0) != null) ? finAtencionCaja.get(0).getProxFin() : -1,
                (finAtencionCaja.get(1) != null) ? finAtencionCaja.get(1).getProxFin() : -1);
  

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
        if (finAtencionCombustible.get(0) != null && min == finAtencionCombustible.get(0).getProxFin()) {
            return finAtencionCombustible.get(0);
        }
         if (finAtencionCombustible.get(1) != null && min == finAtencionCombustible.get(1).getProxFin()) {
            return finAtencionCombustible.get(1);
        }
         if (finAtencionCombustible.get(2) != null && min == finAtencionCombustible.get(2).getProxFin()) {
            return finAtencionCombustible.get(2);
        }
         if (finAtencionCombustible.get(3) != null && min == finAtencionCombustible.get(3).getProxFin()) {
            return finAtencionCombustible.get(3);
        }
        if (finAtencionLavado.get(0) != null && min == finAtencionLavado.get(0).getProxFin()) {
            return finAtencionLavado.get(0);
        }
         if (finAtencionLavado.get(1) != null && min == finAtencionLavado.get(1).getProxFin()) {
            return finAtencionLavado.get(1);
        }
        if (finAtencionMantenimiento.get(0) != null && min == finAtencionMantenimiento.get(0).getProxFin()) {
            return finAtencionMantenimiento.get(0);
        }
        if (finAtencionMantenimiento.get(1) != null && min == finAtencionMantenimiento.get(1).getProxFin()) {
            return finAtencionMantenimiento.get(1);
        }
        if (finAtencionCaja.get(0) != null && min == finAtencionCaja.get(0).getProxFin()) {
            return finAtencionCaja.get(0);
        }
        if (finAtencionCaja.get(1) != null && min == finAtencionCaja.get(1).getProxFin()) {
            return finAtencionCaja.get(1);
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

    public void agregarFinAtencionCombustible(FinAtencionCombustible finAt){
        
        finAtencionCombustible.set(finAt.getSubindice(),finAt);
    }
    public void agregarFinAtencionLavado(FinAtencionLavado finAt){
        finAtencionLavado.set(finAt.getSubindice(),finAt);
    }
     public void agregarFinAtencionMantenimiento(FinAtencionMantenimiento finAt){
        finAtencionMantenimiento.set(finAt.getSubindice(),finAt);
    }
     public void agregarFinAtencionCaja(FinAtencionCaja finAt){
        finAtencionCaja.set(finAt.getSubindice(),finAt);
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

    public ArrayList<FinAtencionCombustible> getFinAtencionCombustible() {
        return finAtencionCombustible;
    }

    public ArrayList<FinAtencionLavado> getFinAtencionLavado() {
        return finAtencionLavado;
    }

    public ArrayList<FinAtencionMantenimiento> getFinAtencionMantenimiento() {
        return finAtencionMantenimiento;
    }

    public ArrayList<FinAtencionCaja> getFinAtencionCaja() {
        return finAtencionCaja;
    }
    
    

    @Override
    public String toString() {
        return "Simulacion{" +
                "relojActual=" + relojActual +
                ", llegadaCaja=" + llegadaCaja +
                ", llegadaCombustible=" + llegadaCombustible +
                ", llegadaLavado=" + llegadaLavado +
                ", llegadaMantenimiento=" + llegadaMantenimiento +
                ", finAtencionCombustible=" + finAtencionCombustible +
                ", finAtencionLavado=" + finAtencionLavado +
                ", finAtencionMantenimiento=" + finAtencionMantenimiento +
                ", finAtencionCaja=" + finAtencionCaja +
                ", cajas=" + cajas +
                ", surtidores=" + surtidores +
                ", estacionesMantenimiento=" + estacionesMantenimiento +
                ", estacionesLavado=" + estacionesLavado +
                '}';
    }
    @Override
public Simulacion clone() {
    try {
        Simulacion clonada = (Simulacion) super.clone();
        
        // Clonación profunda de llegadas
        clonada.llegadaCaja = (LlegadaCaja) llegadaCaja.clone();
        clonada.llegadaCombustible = (LlegadaCombustible) llegadaCombustible.clone();
        clonada.llegadaLavado = (LlegadaLavado) llegadaLavado.clone();
        clonada.llegadaMantenimiento = (LlegadaMantenimiento) llegadaMantenimiento.clone();

        // Clonación profunda de listas de fin de atencion
        clonada.finAtencionCombustible = new ArrayList<>();
        for (FinAtencionCombustible fin : finAtencionCombustible) {
            clonada.finAtencionCombustible.add(fin != null ? (FinAtencionCombustible) fin.clone() : null);
        }
        clonada.finAtencionLavado = new ArrayList<>();
        for (FinAtencionLavado fin : finAtencionLavado) {
            clonada.finAtencionLavado.add(fin != null ? (FinAtencionLavado) fin.clone() : null);
        }
        clonada.finAtencionMantenimiento = new ArrayList<>();
        for (FinAtencionMantenimiento fin : finAtencionMantenimiento) {
            clonada.finAtencionMantenimiento.add(fin != null ? (FinAtencionMantenimiento) fin.clone() : null);
        }
        clonada.finAtencionCaja = new ArrayList<>();
        for (FinAtencionCaja fin : finAtencionCaja) {
            clonada.finAtencionCaja.add(fin != null ? (FinAtencionCaja) fin.clone() : null);
        }
        // Clonación profunda de listas de objetos permanentes
        clonada.cajas = new ArrayList<>();
        for (Caja caja : cajas) {
            caja.setCola();
            clonada.cajas.add((Caja) caja.clone());
        }
        clonada.surtidores = new ArrayList<>();
        for (Surtidor surtidor : surtidores) {
            surtidor.setCola();
            clonada.surtidores.add((Surtidor) surtidor.clone());
         
        }
        clonada.estacionesMantenimiento = new ArrayList<>();
        for (EstacionMantenimiento estacion : estacionesMantenimiento) {
            estacion.setCola();
            clonada.estacionesMantenimiento.add((EstacionMantenimiento) estacion.clone());
        }
        clonada.estacionesLavado = new ArrayList<>();
        for (EstacionLavado estacion : estacionesLavado) {
            estacion.setCola();
            clonada.estacionesLavado.add((EstacionLavado) estacion.clone());
        }

       

        return clonada;
    } catch (CloneNotSupportedException e) {
        throw new AssertionError(); // Esto no debería ocurrir
    }
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

}
