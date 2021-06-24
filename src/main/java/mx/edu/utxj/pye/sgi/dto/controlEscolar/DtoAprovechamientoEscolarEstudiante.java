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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAprovechamientoEscolarEstudiante implements Serializable, Comparable<DtoAprovechamientoEscolarEstudiante> {
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull String discapacidad;
    @Getter @Setter @NonNull String lenguaIndigena;
    @Getter @Setter @NonNull String promedio;
    
    @Override
    public int compareTo(DtoAprovechamientoEscolarEstudiante o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoAprovechamientoEscolarEstudiante  dtoAprovechamientoEscolarEstudiante){
         return dtoAprovechamientoEscolarEstudiante.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoAprovechamientoEscolarEstudiante.getPrograma().getNombre().concat(" "))
                .concat(dtoAprovechamientoEscolarEstudiante.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                .concat(dtoAprovechamientoEscolarEstudiante.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                .concat(dtoAprovechamientoEscolarEstudiante.getEstudiante().getAspirante().getIdPersona().getNombre());
    }
    
}
