package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
    public class ServiciosProduccionProfecional implements EjbProduccionProfecional {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;
    
////////////////////////////////////////////////////////////////////////////////LibrosPub
    
    @Override
    public List<LibrosPub> mostrarLibrosPub(Integer claveTrabajador) throws Throwable{
        TypedQuery<LibrosPub> q = em.createQuery("SELECT l FROM LibrosPub l JOIN l.clavePersonal cp WHERE cp.clave = :clave ORDER BY l.estatus DESC", LibrosPub.class);
        q.setParameter("clave", claveTrabajador);
        List<LibrosPub> pr = q.getResultList();
        return pr;
    }

    @Override
    public LibrosPub crearNuevoLibrosPub(LibrosPub nuevoLibrosPub) throws Throwable{
        facade.setEntityClass(LibrosPub.class);
        facade.create(nuevoLibrosPub);
        facade.flush();
        return nuevoLibrosPub;
    }

    @Override
    public LibrosPub actualizarLibrosPub(LibrosPub nuevoLibrosPub) throws Throwable{
        facade.setEntityClass(LibrosPub.class);
        facade.edit(nuevoLibrosPub);
        facade.flush();
        return nuevoLibrosPub;
    }

    @Override
    public LibrosPub eliminarLibrosPub(LibrosPub nuevoLibrosPub) throws Throwable{
        facade.setEntityClass(LibrosPub.class);
        facade.remove(nuevoLibrosPub);
        facade.flush();
        return nuevoLibrosPub;
    }
////////////////////////////////////////////////////////////////////////////////Articulosp

    @Override
    public List<Articulosp> mostrarArticulosp(Integer claveTrabajador) throws Throwable{
        TypedQuery<Articulosp> q = em.createQuery("SELECT a FROM Articulosp a JOIN a.clavePersonal cp WHERE cp.clave = :clave ORDER BY a.estatus DESC", Articulosp.class);
        q.setParameter("clave", claveTrabajador);
        List<Articulosp> pr = q.getResultList();
        return pr;
    }

    @Override
    public Articulosp crearNuevoArticulosp(Articulosp nuevoArticulosp) throws Throwable{
        facade.setEntityClass(Articulosp.class);
        facade.create(nuevoArticulosp);
        facade.flush();
        return nuevoArticulosp;
    }

    @Override
    public Articulosp actualizarArticulosp(Articulosp nuevoArticulosp) throws Throwable{
        facade.setEntityClass(Articulosp.class);
        facade.edit(nuevoArticulosp);
        facade.flush();
        return nuevoArticulosp;
    }

    @Override
    public Articulosp eliminarArticulosp(Articulosp nuevoArticulosp) throws Throwable{
        facade.setEntityClass(Articulosp.class);
        facade.remove(nuevoArticulosp);
        facade.flush();
        return nuevoArticulosp;
    }
////////////////////////////////////////////////////////////////////////////////Memoriaspub

    @Override
    public List<Memoriaspub> mostrarMemoriaspub(Integer claveTrabajador) throws Throwable{
        TypedQuery<Memoriaspub> q = em.createQuery("SELECT m FROM Memoriaspub m JOIN m.clavePersonal cp WHERE cp.clave = :clave ORDER BY m.estatus DESC", Memoriaspub.class);
        q.setParameter("clave", claveTrabajador);
        List<Memoriaspub> pr = q.getResultList();
        return pr;
    }

    @Override
    public Memoriaspub crearNuevoMemoriaspub(Memoriaspub nuevoMemoriaspub) throws Throwable{
        facade.setEntityClass(Memoriaspub.class);
        facade.create(nuevoMemoriaspub);
        facade.flush();
        return nuevoMemoriaspub;
    }

    @Override
    public Memoriaspub actualizarMemoriaspub(Memoriaspub nuevoMemoriaspub) throws Throwable{
        facade.setEntityClass(Memoriaspub.class);
        facade.edit(nuevoMemoriaspub);
        facade.flush();
        return nuevoMemoriaspub;
    }

    @Override
    public Memoriaspub eliminarMemoriaspub(Memoriaspub nuevoMemoriaspub) throws Throwable{
        facade.setEntityClass(Memoriaspub.class);
        facade.remove(nuevoMemoriaspub);
        facade.flush();
        return nuevoMemoriaspub;
    }

//////////////////////////////////////////////////////////////////////////////// Investigacion
    
    @Override
    public List<Investigaciones> mostrarInvestigacion(Integer claveTrabajador) throws Throwable{
        TypedQuery<Investigaciones> q = em.createQuery("SELECT i FROM Investigaciones i WHERE i.clavePerosnal = :clavePerosnal", Investigaciones.class);
        q.setParameter("clavePerosnal", claveTrabajador);
        List<Investigaciones> pr = q.getResultList();
        return pr;
    }

    @Override
    public Investigaciones crearNuevoInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable{
        facade.setEntityClass(Investigaciones.class);
        facade.create(nuevoInvestigacion);
        facade.flush();
        return nuevoInvestigacion;
    }

    @Override
    public Investigaciones actualizarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable{
        facade.setEntityClass(Investigaciones.class);
        facade.edit(nuevoInvestigacion);
        facade.flush();
        return nuevoInvestigacion;
    }

    @Override
    public Investigaciones eliminarInvestigacion(Investigaciones nuevoInvestigacion) throws Throwable{
        facade.setEntityClass(Investigaciones.class);
        facade.remove(nuevoInvestigacion);
        facade.flush();
        return nuevoInvestigacion;
    }
////////////////////////////////////////////////////////////////////////////////Congresos

    @Override
    public List<Congresos> mostrarCongresos(Integer claveTrabajador) throws Throwable{
        TypedQuery<Congresos> q = em.createQuery("SELECT c FROM Congresos c JOIN c.clavePersonal cp WHERE cp.clave = :clave ORDER BY c.estatus DESC", Congresos.class);
        q.setParameter("clave", claveTrabajador);
        List<Congresos> pr = q.getResultList();
        return pr;
    }

    @Override
    public Congresos crearNuevoCongresos(Congresos nuevoCongresos) throws Throwable{
        facade.setEntityClass(Congresos.class);
        facade.create(nuevoCongresos);
        facade.flush();
        return nuevoCongresos;
    }

    @Override
    public Congresos actualizarCongresos(Congresos nuevoCongresos) throws Throwable{
        facade.setEntityClass(Congresos.class);
        facade.edit(nuevoCongresos);
        facade.flush();
        return nuevoCongresos;
    }

    @Override
    public Congresos eliminarCongresos(Congresos nuevoCongresos) throws Throwable{
        facade.setEntityClass(Congresos.class);
        facade.remove(nuevoCongresos);
        facade.flush();
        return nuevoCongresos;
    }
}