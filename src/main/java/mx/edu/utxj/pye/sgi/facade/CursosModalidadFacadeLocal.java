/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;

/**
 *
 * @author UTXJ
 */
@Local
public interface CursosModalidadFacadeLocal {

    void create(CursosModalidad cursosModalidad);

    void edit(CursosModalidad cursosModalidad);

    void remove(CursosModalidad cursosModalidad);

    CursosModalidad find(Object id);

    List<CursosModalidad> findAll();

    List<CursosModalidad> findRange(int[] range);

    int count();
    
}
