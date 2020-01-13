package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;

@Local
public interface EjbTecnologia {

////////////////////////////////////////////////////////////////////////////////Desarrollos de Software
    public List<DesarrolloSoftware> mostrarDesarrolloSoftware(Integer claveTrabajador) throws Throwable;

    public DesarrolloSoftware crearNuevoDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable;

    public DesarrolloSoftware actualizarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable;

    public DesarrolloSoftware eliminarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Desarrollos Tecnologicos

    public List<DesarrollosTecnologicos> mostrarDesarrollosTecnologicos(Integer claveTrabajador) throws Throwable;

    public DesarrollosTecnologicos crearNuevoDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable;

    public DesarrollosTecnologicos actualizarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable;

    public DesarrollosTecnologicos eliminarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable;
////////////////////////////////////////////////////////////////////////////////Innovaciones

    public List<Innovaciones> mostrarInnovaciones(Integer claveTrabajador) throws Throwable;

    public Innovaciones crearNuevoInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable;

    public Innovaciones actualizarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable;

    public Innovaciones eliminarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable;
}
