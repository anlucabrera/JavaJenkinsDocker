package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesControlInternoResultados;

/**
 *
 * @author UTXJ
 */
public class ComparadorEvaluacionControlInterno implements Comparador<EvaluacionesControlInternoResultados>{

    @Override
    public boolean isCompleto(EvaluacionesControlInternoResultados resultado) {
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
                || resultado.getR22() == null
                || resultado.getR23() == null
                || resultado.getR24() == null
                || resultado.getR25() == null
                || resultado.getR26() == null
                || resultado.getR27() == null){
            return false;
        }
        
        return !(resultado.getR1().trim().isEmpty()
                || resultado.getR2().trim().isEmpty()
                || resultado.getR3().trim().isEmpty()
                || resultado.getR4().trim().isEmpty()
                || resultado.getR5().trim().isEmpty()
                || resultado.getR6().trim().isEmpty()
                || resultado.getR7().trim().isEmpty()
                || resultado.getR8().trim().isEmpty()
                || resultado.getR9().trim().isEmpty()
                || resultado.getR10().trim().isEmpty()
                || resultado.getR11().trim().isEmpty()
                || resultado.getR12().trim().isEmpty()
                || resultado.getR13().trim().isEmpty()
                || resultado.getR14().trim().isEmpty()
                || resultado.getR15().trim().isEmpty()
                || resultado.getR16().trim().isEmpty()
                || resultado.getR17().trim().isEmpty()
                || resultado.getR18().trim().isEmpty()
                || resultado.getR19().trim().isEmpty()
                || resultado.getR20().trim().isEmpty()
                || resultado.getR21().trim().isEmpty()
                || resultado.getR22().trim().isEmpty()
                || resultado.getR23().trim().isEmpty()
                || resultado.getR24().trim().isEmpty()
                || resultado.getR25().trim().isEmpty()
                || resultado.getR26().trim().isEmpty()
                || resultado.getR27().trim().isEmpty());
    }
    
}
