/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;

/**
 *
 * @author UTXJ
 */
@Local
public interface CursosTipoFacadeLocal {

    void create(CursosTipo cursosTipo);

    void edit(CursosTipo cursosTipo);

    void remove(CursosTipo cursosTipo);

    CursosTipo find(Object id);

    List<CursosTipo> findAll();

    List<CursosTipo> findRange(int[] range);

    int count();
    
}
