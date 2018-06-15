/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.util;

import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class Datos {
    public static String getPeriodoEscolarNombre(PeriodosEscolares pe){
        if(pe.getMesInicio().getNumero() < 8){
            return pe.getMesInicio().getMes().concat(" - ").concat(pe.getMesFin().getMes()).concat(" ").concat(String.valueOf(pe.getAnio()));
        }else{            
            return pe.getMesInicio().getMes().concat(" - ").concat(pe.getMesFin().getMes()).concat(" ").concat(String.valueOf(pe.getAnio()));
        }
    }
}
