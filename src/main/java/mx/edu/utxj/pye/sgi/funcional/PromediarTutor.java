/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;

/**
 *
 * @author UTXJ
 */
public class PromediarTutor implements Calculable<EvaluacionTutoresResultados> {

    @Override
    public Double promediar(EvaluacionTutoresResultados t) {
        if (t.getR5() == 4) {
            Integer suma = t.getR1()
                    + t.getR2()
                    + t.getR3()
                    + t.getR4()
                    + t.getR6()
                    + t.getR7()
                    + t.getR8();
            Double promedio = Double.parseDouble(suma.toString()) / 7;
            return promedio;
        } else {
            Integer suma2 = t.getR1()
                    + t.getR2()
                    + t.getR3()
                    + t.getR4()
                    + t.getR5()
                    + t.getR6()
                    + t.getR7()
                    + t.getR8();
            Double promedio2 = Double.parseDouble(suma2.toString()) / 8;
            return promedio2;
        }
    }

}
