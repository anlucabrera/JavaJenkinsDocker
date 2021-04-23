package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionParesAcademicos;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionParesPA implements Comparador<EvaluacionParesAcademicos> {

    @Override
    public boolean isCompleto(EvaluacionParesAcademicos resultado) {
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
                || resultado.getR11() == null
                || resultado.getR12() == null
                || resultado.getR13() == null


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
                || resultado.getR8() == null
                || resultado.getR9() == null
                || resultado.getR10() == null
                || resultado.getR11() == null
                || resultado.getR12() == null
                || resultado.getR13() == null

        );
    }

}
