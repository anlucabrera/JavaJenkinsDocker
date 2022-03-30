package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosContinuidad;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Messages;

@Stateful
public class ServiciosAreasLogeo implements EjbAreasLogeo {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pro_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

    @Override
    public List<Categorias> mostrarCategorias() throws Throwable {
        TypedQuery<Categorias> q = em.createQuery("SELECT c FROM Categorias c", Categorias.class);
        List<Categorias> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<AreasUniversidad> mostrarAreasUniversidadActivas() throws Throwable {
        TypedQuery<AreasUniversidad> q = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.vigente = :vigente", AreasUniversidad.class);
        q.setParameter("vigente", "1");
        List<AreasUniversidad> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<AreasUniversidad> mostrarAreasUniversidadSubordinadas(Short area) throws Throwable {
        TypedQuery<AreasUniversidad> q = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.areaSuperior = :areaSuperior AND a.vigente = :vigente", AreasUniversidad.class);
        q.setParameter("areaSuperior", area);
        q.setParameter("vigente", "1");
        List<AreasUniversidad> pr = q.getResultList();
        if (pr.isEmpty()) {
            pr = new ArrayList<>();
        }
        return pr;
    }

    @Override
    public List<AreasUniversidad> getAreasUniversidadConPoa() {
        try {
            return em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.vigente = :vigente AND a.tienePoa = :tienePOA", AreasUniversidad.class)
                    .setParameter("vigente", "1")
                    .setParameter("tienePOA", true)
                    .getResultList();
        } catch (NoResultException e) {
            Messages.addGlobalWarn("<b>No se han encontrado Áreas para el filtrado de CMI </b>");
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public List<AreasUniversidad> mostrarAllAreasUniversidad() throws Throwable {
        TypedQuery<AreasUniversidad> q = em.createQuery("SELECT a FROM AreasUniversidad a", AreasUniversidad.class);
        List<AreasUniversidad> pr = q.getResultList();
        return pr;
    }

    @Override
    public AreasUniversidad mostrarAreasUniversidad(Short areaId) throws Throwable {
        if (areaId == null) {
            areaId = 61;
        }
        AreasUniversidad pr = em.find(AreasUniversidad.class, areaId);
        if (pr == null) {
            return new AreasUniversidad(areaId, "No se encintro área", "S/A", "1", true);
        } else {
            return pr;
        }
    }

    @Override
    public AreasUniversidad agregarAreasUniversidad(AreasUniversidad au) throws Throwable {
        facade.setEntityClass(AreasUniversidad.class);
        facade.create(au);
        facade.flush();
        return au;
    }

    @Override
    public AreasUniversidad actualizarAreasUniversidad(AreasUniversidad au) throws Throwable {
        facade.setEntityClass(AreasUniversidad.class);
        facade.edit(au);
        facade.flush();
        return au;
    }

    @Override
    public List<AreasUniversidad> mostrarAreasUniversidad() throws Throwable {
        TypedQuery<AreasUniversidad> q = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.vigente = :vigente", AreasUniversidad.class);
        q.setParameter("vigente", "1");
        List<AreasUniversidad> pr = q.getResultList();
        return pr;
    }

    @Override
    public List<AreasUniversidad> listaProgramasEducativos() {
        return facade.getEntityManager().createQuery("SELECT au FROM AreasUniversidad au WHERE au.categoria.categoria = 9 AND au.vigente = 1 ORDER BY au.nombre", AreasUniversidad.class)
                .getResultList();
    }
    
    @Override
    public List<ProgramasEducativosContinuidad> listaProgramasEducativosContinuidad(Short clave){
        return facade.getEntityManager().createQuery("SELECT au FROM ProgramasEducativosContinuidad au WHERE au.programaContinuidad =:clave AND au.activo = 1", ProgramasEducativosContinuidad.class).setParameter("clave", clave)
                .getResultList();
    }
    
    @Override
    public List<ProgramasEducativosContinuidad> listaProgramasEducativosContinuidadAlineaciones(Short clave){
        return facade.getEntityManager().createQuery("SELECT au FROM ProgramasEducativosContinuidad au WHERE (au.programaContinuidad =:clave OR au.programaTSU.area=:clave ) AND au.activo = 1", ProgramasEducativosContinuidad.class).setParameter("clave", clave)
                .getResultList();
    }
}
