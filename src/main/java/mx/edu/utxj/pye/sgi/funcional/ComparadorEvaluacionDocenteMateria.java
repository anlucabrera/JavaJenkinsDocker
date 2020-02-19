package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;

public class ComparadorEvaluacionDocenteMateria implements Comparador<EvaluacionDocentesMateriaResultados>{

    @Override
    public boolean isCompleto(EvaluacionDocentesMateriaResultados t) { 
       return t.getCompleto();
       
       /*if (t.getR1() == null
                || t.getR2() == null
                || t.getR3() == null
                || t.getR4() == null
                || t.getR5() == null
                || t.getR6() == null
                || t.getR7() == null
                || t.getR8() == null
                || t.getR9() == null
                || t.getR10() == null
                || t.getR11() == null
                || t.getR12() == null
                || t.getR14() == null
                || t.getR15() == null
                || t.getR16() == null
                || t.getR17() == null
                || t.getR18() == null
                || t.getR19() == null
                || t.getR20() == null
                || t.getR21() == null
                || t.getR22() == null
                || t.getR23() == null
                || t.getR24() == null
                || t.getR25() == null
                || t.getR26() == null
                || t.getR27() == null
                || t.getR28() == null
                || t.getR29() == null
                || t.getR30() == null
                || t.getR31() == null) {
            return false;
        }
        return true;*/
    }
    
}
