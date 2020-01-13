/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.CursosEvidencia;

/**
 *
 * @author UTXJ
 */
@Local
public interface CursosEvidenciaFacadeLocal {

    void create(CursosEvidencia cursosEvidencia);

    void edit(CursosEvidencia cursosEvidencia);

    void remove(CursosEvidencia cursosEvidencia);

    CursosEvidencia find(Object id);

    List<CursosEvidencia> findAll();

    List<CursosEvidencia> findRange(int[] range);

    int count();
    
}
