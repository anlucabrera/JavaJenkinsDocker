/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaEmpleadores;


/**
 *
 * @author Planeación
 */
public class ComparadorEncuestaEmpleadores implements Comparador<EncuestaEmpleadores>{
    
    @Override
    public boolean isCompleto(EncuestaEmpleadores resultado){
        if(resultado.getNombreEmpresa() == null
                || resultado.getEncuestado() == null
                || resultado.getCargo() == null
                || resultado.getNombreEgresado() == null
                || resultado.getCarrera() == null
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
                || resultado.getR22() == null){
            return false;
        }
        return true;
    }
}
