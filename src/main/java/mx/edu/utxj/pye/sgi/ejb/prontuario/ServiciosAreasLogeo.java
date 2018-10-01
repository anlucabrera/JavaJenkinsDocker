package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;
import mx.edu.utxj.pye.sgi.facade.Facade;

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
    public List<AreasUniversidad> mostrarAreasUniversidad() throws Throwable {
        TypedQuery<AreasUniversidad> q = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.vigente = :vigente", AreasUniversidad.class);
        q.setParameter("vigente", "1");
        List<AreasUniversidad> pr = q.getResultList();
        return pr;
    }
    @Override
    public AreasUniversidad mostrarAreasUniversidad(Short areaId) throws Throwable {
         facade.setEntityClass(AreasUniversidad.class);
        AreasUniversidad pr = facade.getEntityManager().find(AreasUniversidad.class, areaId);
        if (pr == null) {
            return null;
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
}
