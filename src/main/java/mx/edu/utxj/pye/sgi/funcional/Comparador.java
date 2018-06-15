/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

@FunctionalInterface
public interface Comparador<T> {
    public boolean isCompleto(T t);
}
