package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;

@Local
public interface EjbProduccionProfecional {

////////////////////////////////////////////////////////////////////////////////LibrosPub
    
    public List<LibrosPub> mostrarLibrosPub(Integer claveTrabajador) throws Throwable;

    public LibrosPub crearNuevoLibrosPub(LibrosPub nuevoLibrosPub) throws Throwable;

    public LibrosPub actualizarLibrosPub(LibrosPub nuevoLibrosPub) throws Throwable;

    public LibrosPub eliminarLibrosPub(LibrosPub nuevoLibrosPub) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Articulosp

    public List<Articulosp> mostrarArticulosp(Integer claveTrabajador) throws Throwable;

    public Articulosp crearNuevoArticulosp(Articulosp nuevoArticulosp) throws Throwable;

    public Articulosp actualizarArticulosp(Articulosp nuevoArticulosp) throws Throwable;

    public Articulosp eliminarArticulosp(Articulosp nuevoArticulosp) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Memoriaspub

    public List<Memoriaspub> mostrarMemoriaspub(Integer claveTrabajador) throws Throwable;

    public Memoriaspub crearNuevoMemoriaspub(Memoriaspub nuevoMemoriaspub) throws Throwable;

    public Memoriaspub actualizarMemoriaspub(Memoriaspub nuevoMemoriaspub) throws Throwable;

    public Memoriaspub eliminarMemoriaspub(Memoriaspub nuevoMemoriaspub) throws Throwable;

//////////////////////////////////////////////////////////////////////////////// Investigacion
    
    public List<Investigaciones> mostrarInvestigacion(Integer claveTrabajador) throws Throwable;

    public Investigaciones crearNuevoInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable;

    public Investigaciones actualizarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable;

    public Investigaciones eliminarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Congresos

    public List<Congresos> mostrarCongresos(Integer claveTrabajador) throws Throwable;

    public Congresos crearNuevoCongresos(Congresos nuevoCongresos) throws Throwable;

    public Congresos actualizarCongresos(Congresos nuevoCongresos) throws Throwable;

    public Congresos eliminarCongresos(Congresos nuevoCongresos) throws Throwable;
}
