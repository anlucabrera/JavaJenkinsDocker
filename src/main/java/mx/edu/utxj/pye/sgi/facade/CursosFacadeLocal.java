/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Cursos;

/**
 *
 * @author UTXJ
 */
@Local
public interface CursosFacadeLocal {

    void create(Cursos cursos);

    void edit(Cursos cursos);

    void remove(Cursos cursos);

    Cursos find(Object id);

    List<Cursos> findAll();

    List<Cursos> findRange(int[] range);

    int count();
    
}
