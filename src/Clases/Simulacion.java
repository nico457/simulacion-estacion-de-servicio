package Clases;

import Clases.Servidores.Caja;
import Clases.Servidores.EstacionLavado;
import Clases.Servidores.EstacionMantenimiento;
import Clases.Servidores.Shop;
import Clases.Servidores.Surtidor;
import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteCombustible;
import Clases.clientes.ClienteLavado;
import Clases.clientes.ClienteMantenimiento;
import Clases.clientes.ClienteShop;
import Clases.finAtencion.*;
import Clases.llegadas.*;


import java.util.ArrayList;

public class Simulacion implements Cloneable {
    private double relojActual;

    // Llegadas
    private LlegadaCaja llegadaCaja;
    private LlegadaCombustible llegadaCombustible;
    private LlegadaLavado llegadaLavado;
    private LlegadaMantenimiento llegadaMantenimiento;
    private LlegadaShop llegadaShop;
    private LlegadaCorteDeLuz llegadaCorteDeLuz;

    // Fin de atencion
    private ArrayList<FinAtencionCombustible> finAtencionCombustible;
    private ArrayList<FinAtencionLavado> finAtencionLavado;
    private ArrayList<FinAtencionMantenimiento> finAtencionMantenimiento;
    private ArrayList<FinAtencionCaja> finAtencionCaja;
    private FinAtencionShop finAtencionShop;

    private FinCorteLuz finCorteLuz;

    //Objetos permanentes
    private ArrayList<Caja> cajas;
    private ArrayList<Surtidor> surtidores;
    private ArrayList<EstacionMantenimiento> estacionesMantenimiento;
    private ArrayList<EstacionLavado> estacionesLavado;
    private Shop shop;

    
   // Contadores y Acumuladores Actividades
    private double acumEsperaCombustible;
    private double acumEsperaLavado;
    private double acumEsperaMantenimiento;
    private double acumEsperaCaja;
    private double acumEsperaShop;

    private int contCombustibleAtendidos;
    private int contLavadoAtendidos;
    private int contMantenimientoAtendidos;
    private int contCajaAtendidos;
    private int contShopAtendidos;

    private double acumOcupadoCombustible;
    private double acumOcupadoLavado;
    private double acumOcupadoMantenimiento;
    private double acumOcupadoCaja;
    private double acumOcupadoShop;


    public void inicializar(double mediaCaja,double mediaCombustible, double mediaLavado, double mediaMantenimiento, double mediaShop){
        this.relojActual = 0;
        this.llegadaCaja = new LlegadaCaja(mediaCaja,relojActual);
        this.llegadaCombustible = new LlegadaCombustible(mediaCombustible,relojActual);
        this.llegadaLavado = new LlegadaLavado(mediaLavado,relojActual);
        this.llegadaMantenimiento = new LlegadaMantenimiento(mediaMantenimiento,relojActual);
        this.llegadaCorteDeLuz = new LlegadaCorteDeLuz(relojActual);
        
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
        
        this.shop = new Shop();
    }

    public Object calcularProximoEvento(){
        double min = findMin(llegadaCaja.getProxLlegada(),
                llegadaCombustible.getProxLlegada(),
                llegadaLavado.getProxLlegada(),
                llegadaMantenimiento.getProxLlegada(),
                llegadaCorteDeLuz.getProxLlegada(),
                (llegadaShop != null) ? llegadaShop.getProxLlegada(): -1,
                (finAtencionCombustible.get(0) != null) ? finAtencionCombustible.get(0).getProxFin() : -1,
                (finAtencionCombustible.get(1) != null) ? finAtencionCombustible.get(1).getProxFin() : -1,
                (finAtencionCombustible.get(2) != null) ? finAtencionCombustible.get(2).getProxFin() : -1,
                (finAtencionCombustible.get(3) != null) ? finAtencionCombustible.get(3).getProxFin() : -1,
                (finAtencionLavado.get(0) != null) ? finAtencionLavado.get(0).getProxFin() : -1,
                (finAtencionLavado.get(1) != null) ? finAtencionLavado.get(1).getProxFin() : -1,
                (finAtencionMantenimiento.get(0) != null) ? finAtencionMantenimiento.get(0).getProxFin() : -1,
                (finAtencionMantenimiento.get(1) != null) ? finAtencionMantenimiento.get(1).getProxFin() : -1,
                (finAtencionCaja.get(0) != null) ? finAtencionCaja.get(0).getProxFin() : -1,
                (finAtencionCaja.get(1) != null) ? finAtencionCaja.get(1).getProxFin() : -1,
                (finAtencionShop != null) ? finAtencionShop.getProxFin() : -1,
                (finCorteLuz != null) ? finCorteLuz.getEnfriamientoTermica() : -1);

                
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
        if (min == llegadaCorteDeLuz.getProxLlegada()) {
            return llegadaCorteDeLuz;
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
        if (llegadaShop != null && min == llegadaShop.getProxLlegada()) {
            return llegadaShop;
        }
         if (finAtencionShop != null && min == finAtencionShop.getProxFin()) {
            return finAtencionShop;
         }
         if(finCorteLuz != null && min == finCorteLuz.getEnfriamientoTermica()){
             return finCorteLuz;
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
        clonada.llegadaCorteDeLuz = (LlegadaCorteDeLuz) llegadaCorteDeLuz.clone();
        if(llegadaShop != null){
            clonada.llegadaShop = (LlegadaShop) llegadaShop.clone();

        }
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
        if(finAtencionShop != null){
            clonada.finAtencionShop = (FinAtencionShop) finAtencionShop.clone();
        }
        if(finCorteLuz != null){
            clonada.finCorteLuz = (FinCorteLuz) finCorteLuz.clone();
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

        shop.setCola();
        clonada.shop = (Shop) shop.clone();

       

        return clonada;
    } catch (CloneNotSupportedException e) {
        throw new AssertionError(); // Esto no debería ocurrir
    }
}

public double obtenerTiempoTotalColasCombustible(){
    double tiempoCola = acumEsperaCombustible;
    for (Surtidor surtidor : surtidores){
        ArrayList<ClienteCombustible> clientesEnCola = surtidor.getClientesCombustible();
        for (ClienteCombustible cliente : clientesEnCola){
            tiempoCola += relojActual - cliente.getTiempoLlegada();
        }
    }

    return tiempoCola;
}

public double obtenerTiempoTotalColasLavado(){
    double tiempoCola = acumEsperaLavado;
    for (EstacionLavado estLavados : estacionesLavado){
        ArrayList<ClienteLavado> clientesEnCola = estLavados.getClientesLavado();
        for (ClienteLavado cliente : clientesEnCola){
            tiempoCola += relojActual - cliente.getTiempoLlegada();
        }
    }

    return tiempoCola;
}

public double obtenerTiempoTotalColasMantenimiento(){
    double tiempoCola = acumEsperaMantenimiento;
    for (EstacionMantenimiento estMantenimiento : estacionesMantenimiento){
        ArrayList<ClienteMantenimiento> clientesEnCola = estMantenimiento.getClientesMantenimiento();
        for (ClienteMantenimiento cliente : clientesEnCola){
            tiempoCola += relojActual - cliente.getTiempoLlegada();
        }
    }

    return tiempoCola;
}

public double obtenerTiempoTotalColasCaja(){
    double tiempoCola = acumEsperaCaja;
    for (Caja caja : cajas){
        ArrayList<ClienteCaja> clientesEnCola = caja.getClientesCaja();
        for (ClienteCaja cliente : clientesEnCola){
            tiempoCola += relojActual - cliente.getTiempoLlegada();
        }
    }
    return tiempoCola;
}
public double obtenerTiempoTotalColasShop(){
    double tiempoCola = acumEsperaShop;
     ArrayList<ClienteShop> clientesEnCola = shop.getClientesShop();
        for (ClienteShop cliente : clientesEnCola){
           tiempoCola += relojActual - cliente.getTiempoLlegada();
       }
        return tiempoCola;
}
     
    // Actualizar Acumuladores Esperas
    public void actualizarEsperaCombustible(double tiempo) {
        acumEsperaCombustible += tiempo;
    }

    public void actualizarEsperaLavado(double tiempo) {
        acumEsperaLavado += tiempo;
    }

    public void actualizarEsperaShop(double tiempo) {
        acumEsperaShop += tiempo;
    }

    public void actualizarEsperaMantenimiento(double tiempo) {
        acumEsperaMantenimiento += tiempo;
    }

    public void actualizarEsperaCaja(double tiempo) {
        acumEsperaCaja += tiempo;
    }

    // Actualizar contadores clientes atendidos
    public void actualizarAtendidosCombustible() {
        contCombustibleAtendidos++;
    }

    public void actualizarAtendidosLavado() {
        contLavadoAtendidos++;
    }

    public void actualizarAtendidosShop() {
        contShopAtendidos++;
    }

    public void actualizarAtendidosMantenimiento() {
        contMantenimientoAtendidos++;
    }

    public void actualizarAtendidosCaja() {
        contCajaAtendidos++;
    }

    // Gets acumuladores espera
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
    
    public double getAcumEsperaShop() {
        return acumEsperaShop;
    }

    // Gets contadores clientes atendidos
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

    public int getContShopAtendidos() {
        return contShopAtendidos;
    }

    // Actualizar tiempos ocupados
    public void actualizarOcupadoCombustible(double tiempo) {
        acumOcupadoCombustible += tiempo;
    }

    public void actualizarOcupadoLavado(double tiempo) {
        acumOcupadoLavado += tiempo;
    }

    public void actualizarOcupadoMantenimiento(double tiempo) {
        acumOcupadoMantenimiento += tiempo;
    }

    public void actualizarOcupadoCaja(double tiempo) {
        acumOcupadoCaja += tiempo;
    }

    public void actualizarOcupadoShop(double tiempo){
        acumOcupadoShop += tiempo;
    }

    // Gets tiempos ocupados
    public double getAcumOcupadoCombustible(){
        return acumOcupadoCombustible;
    }

    public double getAcumOcupadoLavado(){
        return acumOcupadoLavado;
    }

    public double getAcumOcupadoMantenimiento(){
        return acumOcupadoMantenimiento;
    }

    public double getAcumOcupadoCaja(){
        return acumOcupadoCaja;
    }

    public double getAcumOcupadoShop(){
        return acumOcupadoShop;
    }

    // Eventos shop
    public LlegadaShop getLlegadaShop() {
        return llegadaShop;
    }

    public void setLlegadaShop(LlegadaShop llegadaShop) {
        this.llegadaShop = llegadaShop;
    }

    public FinAtencionShop getFinAtencionShop() {
        return finAtencionShop;
    }

    public void setFinAtencionShop(FinAtencionShop finAtencionShop) {
        this.finAtencionShop = finAtencionShop;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public void setAcumEsperaShop(double acumEsperaShop) {
        this.acumEsperaShop = acumEsperaShop;
    }

    public void setContShopAtendidos(int contShopAtendidos) {
        this.contShopAtendidos = contShopAtendidos;
    }

    public LlegadaCorteDeLuz getLlegadaCorteDeLuz() {
        return llegadaCorteDeLuz;
    }

    public void setFinCorteLuz(FinCorteLuz finCorteLuz) {
        this.finCorteLuz = finCorteLuz;
    }

    public void setLlegadaCorteDeLuz(LlegadaCorteDeLuz llegadaCorteDeLuz) {
        this.llegadaCorteDeLuz = llegadaCorteDeLuz;
    }

    public FinCorteLuz getFinCorteLuz() {
        return finCorteLuz;
    }
}
