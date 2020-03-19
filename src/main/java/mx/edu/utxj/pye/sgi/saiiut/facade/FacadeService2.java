/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.saiiut.facade;

import java.util.List;
import javax.ejb.Stateful;
import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 * @param <T> Inyección
 * @EJB private Facade facade;
 *
 * Uso facade.setEntityClass(ListaPersonal.class); List<ListaPersonal> lp =
 * facade.findAll();
 */
//@Singleton
@Stateful
@NoArgsConstructor
@RequiredArgsConstructor
//@Startup
public class FacadeService2<T> implements Facade2 {

    @Getter    @NonNull    private Class<T> entityClass;
    // Comentar el siguiente Contexto de persistencia cuando falle saiiut //
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-saiiut_ejb_1.0PU")
    private EntityManager em;
    // Fin del Contexto de persistencia //

//    @PostConstruct
//    public void init(){
//        System.out.println("FacadeService2.init1");
//        try{
//            EntityManagerFactory factory = Persistence.createEntityManagerFactory("mx.edu.utxj.pye_sgi-ejb-saiiut_ejb_1.0PU");
//            em = factory.createEntityManager();
//            System.out.println("em = " + em);
//        }catch (Throwable e){}
//        System.out.println("FacadeService2.init2");
//    }

    @Override
    public EntityManager getEntityManager() {
//        try{
            Cache c=em.getEntityManagerFactory().getCache();
            c.evictAll();
            return em;
//        }catch (Exception e){
//            return null;
//        }
    }

    @Override
    public void create(Object entity) {
        getEntityManager().persist(entity);
    }

    @Override
    public void edit(Object entity) {
        getEntityManager().merge(entity);
    }

    @Override
    public void remove(Object entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    @Override
    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    @Override
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void setEntityClass(Class t) {
        this.entityClass = t;
    }
}
