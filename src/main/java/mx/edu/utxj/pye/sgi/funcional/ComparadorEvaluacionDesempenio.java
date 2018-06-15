/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionDesempenio implements Comparador<DesempenioEvaluacionResultados> {

    @Override
    public boolean isCompleto(DesempenioEvaluacionResultados t) {
        if (t.getR1() == null 
                ||t.getR2()== null
                ||t.getR3()== null
                ||t.getR4()== null
                ||t.getR5()== null
                ||t.getR6()== null
                ||t.getR7()== null
                ||t.getR8()== null
                ||t.getR9()== null
                ||t.getR10()== null
                ||t.getR11()== null
                ||t.getR12()== null
                ||t.getR13()== null
                ||t.getR14()== null
                ||t.getR15()== null
                ||t.getR16()== null
                ||t.getR17()== null
                ||t.getR18()== null
                ||t.getR19()== null
                ||t.getR20()== null
                ) {
            return false;
        } else {
            return true;
        }
    }

}
