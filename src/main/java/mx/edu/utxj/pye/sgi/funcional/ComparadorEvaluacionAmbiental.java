/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDesempenioAmbientalUtxj;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionAmbiental implements Comparador<EvaluacionDesempenioAmbientalUtxj> {

    @Override
    public boolean isCompleto(EvaluacionDesempenioAmbientalUtxj t) {
        if (t.getR1() == null 
                ||t.getR2()== null
                ||t.getR3()== null
                ||t.getR4()== null
                ||t.getR5()== null
                ||t.getR6()== null
                ||t.getR7()== null
                ||t.getR8()== null
                ||t.getR9()== null
                ||t.getR111()== null
                ) {
            return false;
        } else {
            return true;
        }
    }

}
