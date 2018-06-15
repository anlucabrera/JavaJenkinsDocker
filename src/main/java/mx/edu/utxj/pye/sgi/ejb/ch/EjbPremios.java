package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;

@Local
public interface EjbPremios {

////////////////////////////////////////////////////////////////////////////////Distinciones
    public List<Distinciones> mostrarDistinciones(Integer claveTrabajador) throws Throwable;

    public Distinciones crearNuevoDistinciones(Distinciones nuevoDistinciones) throws Throwable;

    public Distinciones actualizarDistinciones(Distinciones nuevoDistinciones) throws Throwable;

    public Distinciones eliminarDistinciones(Distinciones nuevoDistinciones) throws Throwable;

}
