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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoReportePlaneacionDocente implements Serializable, Comparable<DtoReportePlaneacionDocente> {
    @Getter @Setter @NonNull CargaAcademica cargaAcademica;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull Personal docente;
    @Getter @Setter @NonNull Integer unidadesMateria;
    @Getter @Setter @NonNull Integer unidadesConfiguradas;
    @Getter @Setter @NonNull Integer unidadesValidadas;
    @Getter @Setter @NonNull Integer evidenciasSer;
    @Getter @Setter @NonNull Integer evidenciasSaber;
    @Getter @Setter @NonNull Integer evidenciasSaberHacer;
    @Getter @Setter @NonNull Boolean asignacionCompleta;
    
    @Override
    public int compareTo(DtoReportePlaneacionDocente o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoReportePlaneacionDocente dtoReportePlaneacionDocente){
         return dtoReportePlaneacionDocente.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoReportePlaneacionDocente.getPrograma().getNombre().concat(" "))
                .concat(String.valueOf(dtoReportePlaneacionDocente.getCargaAcademica().getCveGrupo().getGrado()).concat(" "))
                .concat(String.valueOf(dtoReportePlaneacionDocente.getCargaAcademica().getCveGrupo().getLiteral()).concat(" "))
                .concat(dtoReportePlaneacionDocente.getPlanEstudioMateria().getClaveMateria());
    }
}
