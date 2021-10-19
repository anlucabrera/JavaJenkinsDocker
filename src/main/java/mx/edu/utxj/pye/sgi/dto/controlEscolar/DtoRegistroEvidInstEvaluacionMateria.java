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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoRegistroEvidInstEvaluacionMateria implements Serializable, Comparable<DtoRegistroEvidInstEvaluacionMateria>{
    @Getter @Setter @NonNull EvaluacionSugerida evaluacionSugerida;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
    @Getter @Setter @NonNull String periodoEscolar;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    
    @Override
    public int compareTo(DtoRegistroEvidInstEvaluacionMateria o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoRegistroEvidInstEvaluacionMateria dtoRegistroEvidInstEvaluacionMateria){
         return dtoRegistroEvidInstEvaluacionMateria.getPlanEstudioMateria().getClaveMateria().concat(" ")
                 .concat(String.valueOf(dtoRegistroEvidInstEvaluacionMateria.getEvaluacionSugerida().getUnidadMateria().getNoUnidad()).concat(" "))
                 .concat(String.valueOf(dtoRegistroEvidInstEvaluacionMateria.getEvaluacionSugerida().getEvidencia().getCriterio().getCriterio()).concat(" "))
                 .concat(dtoRegistroEvidInstEvaluacionMateria.getEvaluacionSugerida().getEvidencia().getDescripcion().concat(" "))
                 .concat(dtoRegistroEvidInstEvaluacionMateria.getEvaluacionSugerida().getInstrumento().getDescripcion());
    }
    
}
