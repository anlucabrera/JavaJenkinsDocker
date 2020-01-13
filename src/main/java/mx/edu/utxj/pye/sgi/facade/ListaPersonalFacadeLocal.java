/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;

/**
 *
 * @author UTXJ
 */
@Local
public interface ListaPersonalFacadeLocal {

    void create(ListaPersonal listaPersonal);

    void edit(ListaPersonal listaPersonal);

    void remove(ListaPersonal listaPersonal);

    ListaPersonal find(Object id);

    List<ListaPersonal> findAll();

    List<ListaPersonal> findRange(int[] range);

    int count();
    
}
