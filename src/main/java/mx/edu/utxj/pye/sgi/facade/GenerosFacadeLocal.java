/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.facade;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;

/**
 *
 * @author UTXJ
 */
@Local
public interface GenerosFacadeLocal {

    void create(Generos generos);

    void edit(Generos generos);

    void remove(Generos generos);

    Generos find(Object id);

    List<Generos> findAll();

    List<Generos> findRange(int[] range);

    int count();
    
}
