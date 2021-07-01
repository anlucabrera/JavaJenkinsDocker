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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoMatricula implements Serializable, Comparable<DtoMatricula> {
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull Generos genero;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    
    @Override
    public int compareTo(DtoMatricula o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoMatricula dtoMatricula){
         return dtoMatricula.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoMatricula.getPrograma().getNombre().concat(" "))
                .concat(dtoMatricula.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                .concat(dtoMatricula.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                .concat(dtoMatricula.getEstudiante().getAspirante().getIdPersona().getNombre());
    }
}
