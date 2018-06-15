package mx.edu.utxj.pye.sgi.ejb.ch;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.facade.Facade;

@Stateful
public class ServiciosAreasLogeo implements EjbAreasLogeo {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @EJB
    Facade facade;

    @Override
    public List<Areas> mostrarListaAreas(String tipo) throws Throwable {
        TypedQuery<Areas> q = em.createQuery("SELECT a FROM Areas a WHERE a.tipo = :tipo", Areas.class);
        q.setParameter("tipo", tipo);
        List<Areas> pr = q.getResultList();
        return pr;
    }
}
