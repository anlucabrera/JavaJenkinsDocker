/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroNoticias")
public class EjbRegistroNoticias {
    @EJB        Facade                      f;
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
}
