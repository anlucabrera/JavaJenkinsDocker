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
public class DtoRegistroPrevioEvidInstEval implements Serializable, Comparable<DtoRegistroPrevioEvidInstEval>{
    @Getter @Setter @NonNull UnidadMateria unidadMateria;
    @Getter @Setter @NonNull Integer noUnidad;
    @Getter @Setter @NonNull Materia materia;
    @Getter @Setter @NonNull Integer grado;
    @Getter @Setter @NonNull Criterio criterio;
    @Getter @Setter @NonNull EvidenciaEvaluacion evidenciaEvaluacion;
    @Getter @Setter @NonNull InstrumentoEvaluacion instrumentoEvaluacion;
    @Getter @Setter @NonNull Integer metaInstrumento;

    public DtoRegistroPrevioEvidInstEval() {
    }
    
    @Override
    public int compareTo(DtoRegistroPrevioEvidInstEval o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoRegistroPrevioEvidInstEval dtoRegistroPrevioEvidInstEval){
         return String.valueOf(dtoRegistroPrevioEvidInstEval.getGrado()).concat("")
                 .concat(dtoRegistroPrevioEvidInstEval.getMateria().getNombre().concat(" "))
                 .concat(String.valueOf(dtoRegistroPrevioEvidInstEval.getNoUnidad()).concat(" "))
                 .concat(dtoRegistroPrevioEvidInstEval.getUnidadMateria().getNombre().concat(" "))
                 .concat(String.valueOf(dtoRegistroPrevioEvidInstEval.getCriterio().getTipo()).concat(" "))
                 .concat(dtoRegistroPrevioEvidInstEval.getEvidenciaEvaluacion().getDescripcion().concat(" "))
                 .concat(dtoRegistroPrevioEvidInstEval.getInstrumentoEvaluacion().getDescripcion());
    }
}