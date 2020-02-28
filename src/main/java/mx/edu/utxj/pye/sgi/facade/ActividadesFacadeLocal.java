/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;

/**
 *
 * @author UTXJ
 */
@Local
public interface ActividadesFacadeLocal {

    void create(Actividades actividades);

    void edit(Actividades actividades);

    void remove(Actividades actividades);

    Actividades find(Object id);

    List<Actividades> findAll();

    List<Actividades> findRange(int[] range);

    int count();
    
}
