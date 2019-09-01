/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
@Stateful
@NoArgsConstructor
@RequiredArgsConstructor
public class FacadeService<T> implements Facade {

    @Getter @NonNull private Class<T> entityClass;

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = getEntityManager();
    }
    
    @Override
    public EntityManager getEntityManager() {
        try {
            Cache c = em.getEntityManagerFactory().getCache();
            c.evictAll();
            return em;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public void create(Object entity) {
//        setEntityClass(entity.getClass());
//        getEntityManager().persist(entity);
//        getEntityManager().flush();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<Object> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());

                System.out.println("jvv.aldesa.sgot.facade.AbstractFacade.create(" + entity.toString() + "): " + cv.getRootBeanClass().getSimpleName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        } else {
            setEntityClass(entity.getClass());
            em.persist(entity);
            em.flush();
        }
    }

    @Override
    public void edit(Object entity) {
//        setEntityClass(entity.getClass());
//        getEntityManager().merge(entity);
//        getEntityManager().flush();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(entity);
        if (constraintViolations.size() > 0) {
            Iterator<ConstraintViolation<Object>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<Object> cv = iterator.next();
                System.err.println(cv.getRootBeanClass().getName() + "." + cv.getPropertyPath() + " " + cv.getMessage());

                System.out.println("jvv.aldesa.sgot.facade.AbstractFacade.create(" + entity.toString() + "): " + cv.getRootBeanClass().getSimpleName() + "." + cv.getPropertyPath() + " " + cv.getMessage());
            }
        } else {
            setEntityClass(entity.getClass());
            em.merge(entity);
            em.flush();
        }
    }

    @Override
    public void remove(Object entity) {
        setEntityClass(entity.getClass());
        em.remove(em.merge(entity));
        em.flush();
    }

    @Override
    public T find(Object id) {
        return em.find(entityClass, id);
    }

    @Override
    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    @Override
    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(em.getCriteriaBuilder().count(rt));
        javax.persistence.Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

    @Override
    public void setEntityClass(Class t) {
        this.entityClass = t;
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void refresh(Object entity) {
        em.refresh(entity);
    }
}
