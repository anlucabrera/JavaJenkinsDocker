package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados5;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionDocente2 implements Comparador<EvaluacionDocentesMateriaResultados5>{

    @Override
    public boolean isCompleto(EvaluacionDocentesMateriaResultados5 resultado) {
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
                || resultado.getR14() == null
                || resultado.getR15() == null
                || resultado.getR16() == null
                || resultado.getR17() == null
                || resultado.getR18() == null
                || resultado.getR19() == null
                || resultado.getR20() == null
                || resultado.getR21() == null
                || comparadoreTexto(resultado.getR22(),11)

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
                || resultado.getR14() == null
                || resultado.getR15() == null
                || resultado.getR16() == null
                || resultado.getR17() == null
                || resultado.getR18() == null
                || resultado.getR19() == null
                || resultado.getR20() == null
                || resultado.getR21() == null
                || comparadoreTexto(resultado.getR22(),11));
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
