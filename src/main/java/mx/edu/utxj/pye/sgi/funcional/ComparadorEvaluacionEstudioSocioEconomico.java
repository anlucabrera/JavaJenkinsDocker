/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesEstudioSocioeconomicoResultados;

/**
 *
 * @author Planeación
 */
public class ComparadorEvaluacionEstudioSocioEconomico implements Comparador<EvaluacionesEstudioSocioeconomicoResultados>{
    
    @Override
    public boolean isCompleto(EvaluacionesEstudioSocioeconomicoResultados resultado){
        if(resultado.getR1TienesHijos()                         == null
                || resultado.getR2MadrePadreSoltero()           == null
                || resultado.getR3Trabajas()                    == null
                || resultado.getR4PadresEnfermedadTerminal()    == null
                || resultado.getR5ProblemaAdverso()             == null
                || resultado.getR6PobrezaExtrema()              == null
                || resultado.getR7Desnutricion()                == null
                || resultado.getR8DeficienciaFisicaMental()     == null
                || resultado.getR9DependenciaEconomicaPadres()  == null
                || resultado.getR10IngresoMensualPadres()       == null
                || resultado.getR11EscolaridadMaximaPadre()     == null
                || resultado.getR12EscolaridadMaximaMadre()     == null){
            return false;
        }
        return true;
    }
}
