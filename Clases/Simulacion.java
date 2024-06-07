package Clases;

import Clases.Servidores.Caja;
import Clases.Servidores.EstacionLavado;
import Clases.Servidores.EstacionMantenimiento;
import Clases.Servidores.Surtidor;
import Clases.clientes.ClienteCaja;
import Clases.clientes.ClienteCombustible;
import Clases.clientes.ClienteLavado;
import Clases.clientes.ClienteMantenimiento;
import Clases.llegadas.LlegadaCaja;
import Clases.llegadas.LlegadaCombustible;
import Clases.llegadas.LlegadaLavado;
import Clases.llegadas.LlegadaMantenimiento;

public class Simulacion {
    private LlegadaCaja llegadaCaja;
    private LlegadaCombustible llegadaCombustible;
    private LlegadaLavado llegadaLavado;
    private LlegadaMantenimiento llegadaMantenimiento;
    private float finAtencionCombustible;
    private float finAtencionLavado;
    private float finAtencionMantenimiento;
    private Caja caja;
    private Surtidor surtidorCombustible;
    private EstacionMantenimiento estacionMantenimiento;
    private EstacionLavado estacionLavado;
    private ClienteCaja clienteCaja;
    private ClienteCombustible clienteCombustible;
    private ClienteLavado clienteLavado;
    private ClienteMantenimiento clienteMantenimiento;
}
