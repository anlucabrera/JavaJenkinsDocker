package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionTutor implements Comparador<EvaluacionesTutoresResultados>{

    @Override
    public boolean isCompleto(EvaluacionesTutoresResultados resultado) {
        if(resultado.getR1() == null 
                || resultado.getR2() == null
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR9() == null){
            return false;
        }
        
        return !(resultado.getR1() == 0 
                || resultado.getR2() == 0
                || resultado.getR3() == 0
                || resultado.getR4() == 0
                || resultado.getR5() == 0
                || resultado.getR6() == 0
                || resultado.getR7() == 0
                || resultado.getR8() == 0
                || resultado.getR9().trim().length() < 20);
    }    
}
