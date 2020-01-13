package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionTutor implements Comparador<EvaluacionTutoresResultados>{

    @Override
    public boolean isCompleto(EvaluacionTutoresResultados resultado) {
        if(resultado.getR1() == null 
                || resultado.getR2() == null
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR9() == null || resultado.getR9().trim().isEmpty()){
            return false;
        }
        
        return !(resultado.getR1() == null 
                || resultado.getR2() == null
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR9().trim().length() < 20);
    }    
}
