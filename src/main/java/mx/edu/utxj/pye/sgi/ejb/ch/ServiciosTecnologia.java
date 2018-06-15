package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosTecnologia implements EjbTecnologia {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

////////////////////////////////////////////////////////////////////////////////Desarrollos de Software
    @Override
    public List<DesarrolloSoftware> mostrarDesarrolloSoftware(Integer claveTrabajador) throws Throwable {
        TypedQuery<DesarrolloSoftware> q = em.createQuery("select d from DesarrolloSoftware d JOIN d.clavePersonal c where c.clave=:clave ORDER BY d.estatus DESC", DesarrolloSoftware.class);
        q.setParameter("clave", claveTrabajador);
        List<DesarrolloSoftware> pr = q.getResultList();
        return pr;
    }

    @Override
    public DesarrolloSoftware crearNuevoDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable {
        facade.setEntityClass(DesarrolloSoftware.class);
        facade.create(nuevoDesarrolloSoftware);
        facade.flush();
        return nuevoDesarrolloSoftware;
    }

    @Override
    public DesarrolloSoftware actualizarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable {
        facade.setEntityClass(DesarrolloSoftware.class);
        facade.edit(nuevoDesarrolloSoftware);
        facade.flush();
        return nuevoDesarrolloSoftware;
    }

    @Override
    public DesarrolloSoftware eliminarDesarrolloSoftware(DesarrolloSoftware nuevoDesarrolloSoftware) throws Throwable {
        facade.setEntityClass(DesarrolloSoftware.class);
        facade.remove(nuevoDesarrolloSoftware);
        facade.flush();
        return nuevoDesarrolloSoftware;
    }
////////////////////////////////////////////////////////////////////////////////Desarrollos Tecnologicos

    @Override
    public List<DesarrollosTecnologicos> mostrarDesarrollosTecnologicos(Integer claveTrabajador) throws Throwable {
        TypedQuery<DesarrollosTecnologicos> q = em.createQuery("select d from DesarrollosTecnologicos d JOIN d.clavePersonal c where c.clave= :clave ORDER BY d.estatus DESC", DesarrollosTecnologicos.class);
        q.setParameter("clave", claveTrabajador);
        List<DesarrollosTecnologicos> pr = q.getResultList();
        return pr;
    }

    @Override
    public DesarrollosTecnologicos crearNuevoDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable {
        facade.setEntityClass(DesarrollosTecnologicos.class);
        facade.create(nuevoDesarrollosTecnologicos);
        facade.flush();
        return nuevoDesarrollosTecnologicos;
    }

    @Override
    public DesarrollosTecnologicos actualizarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable {
        facade.setEntityClass(DesarrollosTecnologicos.class);
        facade.edit(nuevoDesarrollosTecnologicos);
        facade.flush();
        return nuevoDesarrollosTecnologicos;
    }

    @Override
    public DesarrollosTecnologicos eliminarDesarrollosTecnologicos(DesarrollosTecnologicos nuevoDesarrollosTecnologicos) throws Throwable {
        facade.setEntityClass(DesarrollosTecnologicos.class);
        facade.remove(nuevoDesarrollosTecnologicos);
        facade.flush();
        return nuevoDesarrollosTecnologicos;
    }
////////////////////////////////////////////////////////////////////////////////Innovaciones

    @Override
    public List<Innovaciones> mostrarInnovaciones(Integer claveTrabajador) throws Throwable {
        TypedQuery<Innovaciones> q = em.createQuery("select i from Innovaciones i JOIN i.clavePersonal c where c.clave= :clave ORDER BY i.estatus DESC", Innovaciones.class);
        q.setParameter("clave", claveTrabajador);
        List<Innovaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public Innovaciones crearNuevoInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable {
        facade.setEntityClass(Innovaciones.class);
        facade.create(nuevoInnovaciones);
        facade.flush();
        return nuevoInnovaciones;
    }

    @Override
    public Innovaciones actualizarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable {
        facade.setEntityClass(Innovaciones.class);
        facade.edit(nuevoInnovaciones);
        facade.flush();
        return nuevoInnovaciones;
    }

    @Override
    public Innovaciones eliminarInnovaciones(Innovaciones nuevoInnovaciones) throws Throwable {
        facade.setEntityClass(Innovaciones.class);
        facade.remove(nuevoInnovaciones);
        facade.flush();
        return nuevoInnovaciones;
    }
}
