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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAprovechamientoEscolar implements Serializable, Comparable<DtoAprovechamientoEscolar> {
    @Getter @Setter @NonNull Estudiante estudiante;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull DatosMedicos datosMedicos;
    @Getter @Setter @NonNull String lenguaIndigena;
    @Getter @Setter @NonNull Double promedio;
    
    @Override
    public int compareTo(DtoAprovechamientoEscolar o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoAprovechamientoEscolar  dtoAprovechamientoEscolar){
         return dtoAprovechamientoEscolar.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoAprovechamientoEscolar.getPrograma().getNombre().concat(" "))
                .concat(dtoAprovechamientoEscolar.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                .concat(dtoAprovechamientoEscolar.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                .concat(dtoAprovechamientoEscolar.getEstudiante().getAspirante().getIdPersona().getNombre());
    }
    
}
