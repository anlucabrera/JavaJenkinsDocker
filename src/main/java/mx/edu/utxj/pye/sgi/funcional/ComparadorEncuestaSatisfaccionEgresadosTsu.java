/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;

/**
 *
 * @author Planeación
 */
public class ComparadorEncuestaSatisfaccionEgresadosTsu implements Comparador<ResultadosEncuestaSatisfaccionTsu>{
    
    @Override
    public boolean isCompleto(ResultadosEncuestaSatisfaccionTsu resultado){
        if(resultado.getR1()==null
                || resultado.getR2()==null
                || resultado.getR3()==null
                || resultado.getR4()==null
                || resultado.getR5()==null
                || resultado.getR6()==null
                || resultado.getR7()==null
                || resultado.getR8()==null
                || resultado.getR9()==null){
            return false;
        }
        return true;
    }
}
