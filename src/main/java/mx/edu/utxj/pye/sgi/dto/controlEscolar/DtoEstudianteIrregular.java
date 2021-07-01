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
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEstudianteIrregular implements Serializable, Comparable<DtoEstudianteIrregular> {
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
    @Getter @Setter @NonNull Personal docente;
    @Getter @Setter @NonNull String calificacion;
    @Getter @Setter @NonNull Personal tutor;
    
    @Override
    public int compareTo(DtoEstudianteIrregular o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoEstudianteIrregular dtoEstudianteIrregulare){
         return dtoEstudianteIrregulare.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoEstudianteIrregulare.getPrograma().getNombre().concat(" "))
                .concat(dtoEstudianteIrregulare.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                .concat(dtoEstudianteIrregulare.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                .concat(dtoEstudianteIrregulare.getEstudiante().getAspirante().getIdPersona().getNombre().concat(" "))
                .concat(dtoEstudianteIrregulare.getPlanEstudioMateria().getClaveMateria());
    }
    
}
