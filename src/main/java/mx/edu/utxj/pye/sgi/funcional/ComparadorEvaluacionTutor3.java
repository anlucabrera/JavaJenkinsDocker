package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados3;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionTutor3 implements Comparador<EvaluacionTutoresResultados3>{

    @Override
    public boolean isCompleto(EvaluacionTutoresResultados3 resultado) {
        if(resultado.getR1() == null
                || resultado.getR2() == null
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null
                || resultado.getR9() == null
                || resultado.getR10() == null
                || comparadoreTexto(resultado.getR11(),11)

        ){
            return false;
        }

        return !(resultado.getR1() == null
                || resultado.getR2() == null
                || resultado.getR3() == null
                || resultado.getR4() == null
                || resultado.getR5() == null
                || resultado.getR6() == null
                || resultado.getR7() == null
                || resultado.getR8() == null)
                || resultado.getR9() == null
                || resultado.getR10() == null
                || comparadoreTexto(resultado.getR11(),11);
    }
    public boolean comparadoreTexto(String pregunta, int t) {
        if (pregunta == null) {
            return true;
        } else if (pregunta.trim().isEmpty()) {
            return true;
        }else if(pregunta.trim().length()< t){
            return true;
        } else {
            return false;
        }
    }
}
