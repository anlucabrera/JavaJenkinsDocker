package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosHabilidades implements EjbHabilidades {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

////////////////////////////////////////////////////////////////////////////////Idiomas
    @Override
    public List<Idiomas> mostrarIdiomas(Integer claveTrabajador) throws Throwable {
        TypedQuery<Idiomas> q = em.createQuery("SELECT i FROM Idiomas i JOIN i.clavePersonal cp WHERE cp.clave = :clave ORDER BY i.estatus DESC", Idiomas.class);
        q.setParameter("clave", claveTrabajador);
        List<Idiomas> pr = q.getResultList();
        return pr;
    }

    @Override
    public Idiomas crearNuevoIdiomas(Idiomas nuevoIdiomas) throws Throwable {
        facade.setEntityClass(Idiomas.class);
        facade.create(nuevoIdiomas);
        facade.flush();
        return nuevoIdiomas;
    }

    @Override
    public Idiomas actualizarIdiomas(Idiomas nuevoIdiomas) throws Throwable {
        facade.setEntityClass(Idiomas.class);
        facade.edit(nuevoIdiomas);
        facade.flush();
        return nuevoIdiomas;
    }

    @Override
    public Idiomas eliminarIdiomas(Idiomas nuevoIdiomas) throws Throwable {
        facade.setEntityClass(Idiomas.class);
        facade.remove(nuevoIdiomas);
        facade.flush();
        return nuevoIdiomas;
    }
////////////////////////////////////////////////////////////////////////////////Lenguas

    @Override
    public List<Lenguas> mostrarLenguas(Integer claveTrabajador) throws Throwable {
        TypedQuery<Lenguas> q = em.createQuery("SELECT l FROM Lenguas l JOIN l.clavePersonal cp WHERE cp.clave = :clave ORDER BY l.estatus DESC", Lenguas.class);
        q.setParameter("clave", claveTrabajador);
        List<Lenguas> pr = q.getResultList();
        return pr;
    }

    @Override
    public Lenguas crearNuevoLenguas(Lenguas nuevoLenguas) throws Throwable {
        facade.setEntityClass(Lenguas.class);
        facade.create(nuevoLenguas);
        facade.flush();
        return nuevoLenguas;
    }

    @Override
    public Lenguas actualizarLenguas(Lenguas nuevoLenguas) throws Throwable {
        facade.setEntityClass(Lenguas.class);
        facade.edit(nuevoLenguas);
        facade.flush();
        return nuevoLenguas;
    }

    @Override
    public Lenguas eliminarLenguas(Lenguas nuevoLenguas) throws Throwable {
        facade.setEntityClass(Lenguas.class);
        facade.remove(nuevoLenguas);
        facade.flush();
        return nuevoLenguas;
    }
////////////////////////////////////////////////////////////////////////////////Habilidades Informaticas

    @Override
    public List<HabilidadesInformaticas> mostrarHabilidadesInformaticas(Integer claveTrabajador) throws Throwable {
        TypedQuery<HabilidadesInformaticas> q = em.createQuery("SELECT h FROM HabilidadesInformaticas h JOIN h.clavePersonal cp WHERE cp.clave = :clave ORDER BY h.estatus DESC", HabilidadesInformaticas.class);
        q.setParameter("clave", claveTrabajador);
        List<HabilidadesInformaticas> pr = q.getResultList();
        return pr;
    }

    @Override
    public HabilidadesInformaticas crearNuevoHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable {
        facade.setEntityClass(HabilidadesInformaticas.class);
        facade.create(nuevoHabilidadesInformaticas);
        facade.flush();
        return nuevoHabilidadesInformaticas;
    }

    @Override
    public HabilidadesInformaticas actualizarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable {
        facade.setEntityClass(HabilidadesInformaticas.class);
        facade.edit(nuevoHabilidadesInformaticas);
        facade.flush();
        return nuevoHabilidadesInformaticas;
    }

    @Override
    public HabilidadesInformaticas eliminarHabilidadesInformaticas(HabilidadesInformaticas nuevoHabilidadesInformaticas) throws Throwable {
        facade.setEntityClass(HabilidadesInformaticas.class);
        facade.remove(nuevoHabilidadesInformaticas);
        facade.flush();
        return nuevoHabilidadesInformaticas;
    }
}
