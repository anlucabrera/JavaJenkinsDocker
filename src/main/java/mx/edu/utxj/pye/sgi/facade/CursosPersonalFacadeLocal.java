/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.CursosPersonal;

/**
 *
 * @author UTXJ
 */
@Local
public interface CursosPersonalFacadeLocal {

    void create(CursosPersonal cursosPersonal);

    void edit(CursosPersonal cursosPersonal);

    void remove(CursosPersonal cursosPersonal);

    CursosPersonal find(Object id);

    List<CursosPersonal> findAll();

    List<CursosPersonal> findRange(int[] range);

    int count();
    
}
