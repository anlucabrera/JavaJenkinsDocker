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

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEstudianteSituacionCartaResponsivaCursoIMSS implements Serializable, Comparable<DtoEstudianteSituacionCartaResponsivaCursoIMSS>{
    @Getter @Setter @NonNull DtoDatosEstudiante dtoEstudiante;
    @Getter @Setter String situacionValidacion;

    public DtoEstudianteSituacionCartaResponsivaCursoIMSS(DtoDatosEstudiante dtoEstudiante, String situacionValidacion) {
        this.dtoEstudiante = dtoEstudiante;
        this.situacionValidacion = situacionValidacion;
    }
    
    @Override
    public int compareTo(DtoEstudianteSituacionCartaResponsivaCursoIMSS o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoEstudianteSituacionCartaResponsivaCursoIMSS dtoEstudianteSituacionCartaResponsivaCursoIMSS){
         return dtoEstudianteSituacionCartaResponsivaCursoIMSS.getDtoEstudiante().getProgramaEducativo().getNombre().concat(" ")
                 .concat(dtoEstudianteSituacionCartaResponsivaCursoIMSS.getDtoEstudiante().getEstudiante().getGrupo().getLiteral().toString().concat(" "))
                 .concat(dtoEstudianteSituacionCartaResponsivaCursoIMSS.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                 .concat(dtoEstudianteSituacionCartaResponsivaCursoIMSS.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoEstudianteSituacionCartaResponsivaCursoIMSS.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre());
    }


   
}
