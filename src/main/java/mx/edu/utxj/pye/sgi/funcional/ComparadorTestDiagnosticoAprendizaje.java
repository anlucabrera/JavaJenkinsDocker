/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.TestDiagnosticoAprendizaje;


/**
 *
 * @author Planeación
 */
public class ComparadorTestDiagnosticoAprendizaje implements Comparador<TestDiagnosticoAprendizaje>{
    
    @Override
    public boolean isCompleto(TestDiagnosticoAprendizaje resultado){
        if(resultado.getR1()==null
                || resultado.getR2()==null
                || resultado.getR3()==null
                || resultado.getR4()==null
                || resultado.getR5()==null
                || resultado.getR6()==null
                || resultado.getR7()==null
                || resultado.getR8()==null
                || resultado.getR9()==null
                || resultado.getR10()==null
                || resultado.getR11()==null
                || resultado.getR12()==null
                || resultado.getR13()==null
                || resultado.getR14()==null
                || resultado.getR15()==null
                || resultado.getR16()==null
                || resultado.getR17()==null
                || resultado.getR18()==null
                || resultado.getR19()==null
                || resultado.getR20()==null
                || resultado.getR21()==null
                || resultado.getR22()==null
                || resultado.getR23()==null
                || resultado.getR24()==null
                || resultado.getR25()==null
                || resultado.getR26()==null
                || resultado.getR27()==null
                || resultado.getR28()==null
                || resultado.getR29()==null
                || resultado.getR30()==null
                || resultado.getR31()==null
                || resultado.getR32()==null
                || resultado.getR33()==null
                || resultado.getR34()==null
                || resultado.getR35()==null
                || resultado.getR36()==null){
            return false;
        }
        return true;
    }
}
