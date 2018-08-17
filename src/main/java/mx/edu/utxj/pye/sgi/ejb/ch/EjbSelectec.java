package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.Historicoplantillapersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

@Local
public interface EjbSelectec {
/////////////////////////////////////////////////////////////////////////Perfil Empleado\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public List<ListaPersonal> mostrarListaDeEmpleados() throws Throwable;

    public List<ListaPersonal> mostrarListaDeEmpleadosN(String nombre) throws Throwable;

    public List<ListaPersonal> mostrarListaDeEmpleadosAr(Short area) throws Throwable;

    public List<ListaPersonal> mostrarListaPersonalPorCategoria(String categoria) throws Throwable;

    public List<ListaPersonal> mostrarListaPersonalPorActividad(String actividad) throws Throwable;

    public List<ListaPersonal> mostrarListaPersonalPorAreaOpySu(String area) throws Throwable;

    
    public List<Personal> mostrarListaDePersonalParaJefes(Short area) throws Throwable;

    public List<Personal> mostrarListaDeEmpleadosTotalActivos() throws Throwable;

    public List<Personal> mostrarListaDeEmpleadosBajas() throws Throwable;

    public List<Personal> mostrarListaDeEmpleadosTotal() throws Throwable;


}
