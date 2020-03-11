package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados2;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionTutor2 implements Comparador<EvaluacionTutoresResultados2>{

    @Override
    public boolean isCompleto(EvaluacionTutoresResultados2 resultado) {
        if(resultado.getR1() == null 
                || resultado.getR2() == null
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null){
            return false;
        }
        
        return !(resultado.getR1() == null 
                || resultado.getR2() == null
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null);
    }    
}
