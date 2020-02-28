/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

/**
 *
 * @author UTXJ
 */
@FunctionalInterface
public interface Validador<T> {
    public boolean esCorrecta(T t);
}
