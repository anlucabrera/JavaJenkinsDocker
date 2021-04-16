/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAsigEvidenciasInstrumentosEval implements Serializable, Comparable<DtoAsigEvidenciasInstrumentosEval>{
    @Getter @Setter @NonNull UnidadMateriaConfiguracion unidadMateriaConfiguracion;
    @Getter @Setter @NonNull EvidenciaEvaluacion evidenciaEvaluacion;
    @Getter @Setter @NonNull InstrumentoEvaluacion instrumentoEvaluacion;
    @Getter @Setter @NonNull Integer valorPorcentual;
    @Getter @Setter @NonNull Integer metaInstrumento;
    
    @Override
    public int compareTo(DtoAsigEvidenciasInstrumentosEval o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoAsigEvidenciasInstrumentosEval dtoAsigEvidenciasInstrumentosEval){
         return String.valueOf(dtoAsigEvidenciasInstrumentosEval.getUnidadMateriaConfiguracion().getIdUnidadMateria().getIdUnidadMateria()).concat(" ")
                 .concat(String.valueOf(dtoAsigEvidenciasInstrumentosEval.getEvidenciaEvaluacion().getCriterio().getCriterio()).concat(" "))
                 .concat(dtoAsigEvidenciasInstrumentosEval.getEvidenciaEvaluacion().getDescripcion().concat(" "))
                 .concat(dtoAsigEvidenciasInstrumentosEval.getInstrumentoEvaluacion().getDescripcion());
    }
}
