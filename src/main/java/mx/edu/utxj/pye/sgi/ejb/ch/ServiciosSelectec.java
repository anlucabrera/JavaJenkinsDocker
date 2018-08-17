package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosSelectec implements EjbSelectec {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;
/////////////////////////////////////////////////////////////////////////Perfil Empleado\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleados() throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l ORDER BY l.clave", ListaPersonal.class);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleadosN(String nombre) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.nombre = :nombre", ListaPersonal.class);
        q.setParameter("nombre", nombre);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaDeEmpleadosAr(Short area) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.areaSuperior = :areaSuperior", ListaPersonal.class);
        q.setParameter("areaSuperior", area);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaPersonalPorCategoria(String categoria) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.categoriaOperativaNombre = :categoriaOperativaNombre", ListaPersonal.class);
        q.setParameter("categoriaOperativaNombre", categoria);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaPersonalPorActividad(String actividad) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.actividadNombre = :actividadNombre", ListaPersonal.class);
        q.setParameter("actividadNombre", actividad);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<ListaPersonal> mostrarListaPersonalPorAreaOpySu(String area) throws Throwable {
        TypedQuery<ListaPersonal> q = em.createQuery("SELECT l FROM ListaPersonal l WHERE l.areaOperativaNombre = :areaOperativaNombre OR l.areaSuperiorNombre = :areaSuperiorNombre", ListaPersonal.class);
        q.setParameter("areaOperativaNombre", area);
        q.setParameter("areaSuperiorNombre", area);
        List<ListaPersonal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Personal> mostrarListaDePersonalParaJefes(Short area) throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.areaOperativa = :areaOperativa OR p.areaSuperior=:areaSuperior AND  p.status <> :status ORDER BY p.clave", Personal.class);
        q.setParameter("status", 'B');
        q.setParameter("areaOperativa", area);
        q.setParameter("areaSuperior", area);
        List<Personal> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<Personal> mostrarListaDeEmpleadosTotalActivos() throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.status <> :status", Personal.class);
        q.setParameter("status", 'B');
        return q.getResultList();
    }

    @Override
    public List<Personal> mostrarListaDeEmpleadosBajas() throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p WHERE p.status=:status", Personal.class);
        q.setParameter("status", 'B');
        return q.getResultList();
    }

    @Override
    public List<Personal> mostrarListaDeEmpleadosTotal() throws Throwable {
        TypedQuery<Personal> q = em.createQuery("SELECT p FROM Personal p", Personal.class);
        return q.getResultList();
    }

}
