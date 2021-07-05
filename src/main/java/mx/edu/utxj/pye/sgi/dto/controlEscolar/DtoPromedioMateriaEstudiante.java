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
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoPromedioMateriaEstudiante implements Serializable, Comparable<DtoPromedioMateriaEstudiante>{
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    @Getter @Setter @NonNull Generos genero;
    @Getter @Setter @NonNull String discapacidad;
    @Getter @Setter @NonNull String lenguaIndigena;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
    @Getter @Setter @NonNull String promedioOrdinario;
    @Getter @Setter @NonNull String promedioNivelacion;
    
    @Override
    public int compareTo(DtoPromedioMateriaEstudiante o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoPromedioMateriaEstudiante dtoPromedioMateriaEstudiante){
         return dtoPromedioMateriaEstudiante.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoPromedioMateriaEstudiante.getPrograma().getNombre().concat(" "))
                .concat(dtoPromedioMateriaEstudiante.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                .concat(dtoPromedioMateriaEstudiante.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                .concat(dtoPromedioMateriaEstudiante.getEstudiante().getAspirante().getIdPersona().getNombre().concat(" "))
                .concat(dtoPromedioMateriaEstudiante.getPlanEstudioMateria().getClaveMateria());
    }
    
}
