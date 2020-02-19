/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.interfaces.vin;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.pye2.CatalogoIemsRecurrentes;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbCatalogoIemsRecurrentes {
    
    /**
     * Obtiene la lista de Iems localizados en la zona de influencia de la universidad.
     * @return lista de Iems.
    */
    public List<CatalogoIemsRecurrentes> getCatalogoIEMS();
    
}
