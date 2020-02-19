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
public class PromediarDesempenio implements Calculable<DesempenioEvaluacionResultados>{

    @Override
    public Double promediar(DesempenioEvaluacionResultados t) {
        Integer suma = t.getR1()
                + t.getR2()
                + t.getR3()
                + t.getR4()
                + t.getR5()
                + t.getR6()
                + t.getR7()
                + t.getR8()
                + t.getR9()
                + t.getR10()
                + t.getR11()
                + t.getR12()
                + t.getR13()
                + t.getR14()
                + t.getR15()
                + t.getR16()
                + t.getR17()
                + t.getR18()
                + t.getR19()
                + t.getR20()              
                ;
        Double promedio = Double.parseDouble(suma.toString()) / 20;
        return promedio;
    }
    
}
