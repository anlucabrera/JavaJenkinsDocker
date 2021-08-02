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

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoTramitarBajas implements Serializable, Comparable<DtoTramitarBajas>{
    @Getter @Setter @NonNull DtoDatosEstudiante dtoEstudiante;
    @Getter @Setter Generos genero;
    @Getter @Setter DtoRegistroBajaEstudiante dtoRegistroBaja;

    public DtoTramitarBajas() {
        
    }
     
    @Override
    public int compareTo(DtoTramitarBajas o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoTramitarBajas dtoTramitarBajas){
         return dtoTramitarBajas.getDtoRegistroBaja().getProgramaEducativo().getNivelEducativo().getNivel().concat(" ")
                 .concat(dtoTramitarBajas.getDtoRegistroBaja().getProgramaEducativo().getNombre().concat(" "))
                 .concat(dtoTramitarBajas.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                 .concat(dtoTramitarBajas.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoTramitarBajas.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre());
    }
    
    public int compareToTramiteBaja(DtoTramitarBajas o) {
        return toLabelTramiteBaja(this).compareTo(toLabelTramiteBaja(o));
    }

    public static String toLabelTramiteBaja(DtoTramitarBajas dtoTramitarBajas){
        return  dtoTramitarBajas.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoPaterno().concat(" ")
                 .concat(dtoTramitarBajas.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoTramitarBajas.getDtoEstudiante().getEstudiante().getAspirante().getIdPersona().getNombre());
    }

}
