/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

/**
 *
 * @author UTXJ
 * @param <T>
 */
@FunctionalInterface
public interface Calculable<T> {
    public Double promediar(T t);
}
