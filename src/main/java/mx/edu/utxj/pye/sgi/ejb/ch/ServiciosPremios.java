package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosPremios implements EjbPremios {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

////////////////////////////////////////////////////////////////////////////////Distinciones
    @Override
    public List<Distinciones> mostrarDistinciones(Integer claveTrabajador) throws Throwable {
        TypedQuery<Distinciones> q = em.createQuery("SELECT d FROM Distinciones d JOIN d.claveEmpleado cp WHERE cp.clave = :clave ORDER BY d.estatus DESC", Distinciones.class);
        q.setParameter("clave", claveTrabajador);
        List<Distinciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public Distinciones crearNuevoDistinciones(Distinciones nuevoDistinciones) throws Throwable {
        facade.setEntityClass(Distinciones.class);
        facade.create(nuevoDistinciones);
        facade.flush();
        return nuevoDistinciones;
    }

    @Override
    public Distinciones actualizarDistinciones(Distinciones nuevoDistinciones) throws Throwable {
        facade.setEntityClass(Distinciones.class);
        facade.edit(nuevoDistinciones);
        facade.flush();
        return nuevoDistinciones;
    }

    @Override
    public Distinciones eliminarDistinciones(Distinciones nuevoDistinciones) throws Throwable {
        facade.setEntityClass(Distinciones.class);
        facade.remove(nuevoDistinciones);
        facade.flush();
        return nuevoDistinciones;
    }
}
