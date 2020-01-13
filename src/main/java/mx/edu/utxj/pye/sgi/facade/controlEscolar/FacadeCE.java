/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade.controlEscolar;

import javax.ejb.Local;
import javax.persistence.EntityManager;
import java.util.List;

/**
 *
 * @author UTXJ
 * @param <T>
 */
@Local
public interface FacadeCE<T> {
    public void setEntityClass(Class<T> t);
    
    public List<T> findAll();
    
    public void create(T entity);
    
    public void edit(T entity);
    
    public void remove(T entity);
    
    public T find(Object id);
    
    public List<T> findRange(int[] range);
    
    public int count();
    
    public EntityManager getEntityManager();
    
    public void flush();
    
    public void refresh(T entity);
}
