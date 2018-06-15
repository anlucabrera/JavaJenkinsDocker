/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCapacitadoCursos;

/**
 *
 * @author UTXJ
 */
@Local
public interface PersonalCapacitadoCursosFacadeLocal {

    void create(PersonalCapacitadoCursos personalCapacitadoCursos);

    void edit(PersonalCapacitadoCursos personalCapacitadoCursos);

    void remove(PersonalCapacitadoCursos personalCapacitadoCursos);

    PersonalCapacitadoCursos find(Object id);

    List<PersonalCapacitadoCursos> findAll();

    List<PersonalCapacitadoCursos> findRange(int[] range);

    int count();
    
}
