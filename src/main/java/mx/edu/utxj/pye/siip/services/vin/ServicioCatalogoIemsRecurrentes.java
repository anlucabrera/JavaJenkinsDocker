/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.services.vin;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.pye2.view.CatalogoIemsRecurrentes;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbCatalogoIemsRecurrentes;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioCatalogoIemsRecurrentes implements EjbCatalogoIemsRecurrentes{
    
    @EJB Facade f;
    @EJB EjbModulos ejbModulos;
    
    @Override
    public List<CatalogoIemsRecurrentes> getCatalogoIEMS() {
        List<CatalogoIemsRecurrentes> genLst = new ArrayList<>();
        TypedQuery<CatalogoIemsRecurrentes> query = f.getEntityManager().createQuery("SELECT c FROM CatalogoIemsRecurrentes c", CatalogoIemsRecurrentes.class);
        
        try {
            genLst = query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            genLst = null;

        }
          return genLst;
    }
    
}
