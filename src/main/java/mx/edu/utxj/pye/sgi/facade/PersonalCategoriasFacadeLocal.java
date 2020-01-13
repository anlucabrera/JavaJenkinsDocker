/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;

/**
 *
 * @author UTXJ
 */
@Local
public interface PersonalCategoriasFacadeLocal {

    void create(PersonalCategorias personalCategorias);

    void edit(PersonalCategorias personalCategorias);

    void remove(PersonalCategorias personalCategorias);

    PersonalCategorias find(Object id);

    List<PersonalCategorias> findAll();

    List<PersonalCategorias> findRange(int[] range);

    int count();
    
}
