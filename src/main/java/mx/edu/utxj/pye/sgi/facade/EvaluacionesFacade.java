/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;

/**
 *
 * @author UTXJ
 */
@Stateless
public class EvaluacionesFacade extends AbstractFacade<Evaluaciones> implements EvaluacionesFacadeLocal {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb_ejb_1.0PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EvaluacionesFacade() {
        super(Evaluaciones.class);
    }
    
}
