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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EntregaFotografiasEstudiante;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoEntregaFotografiasEstadia implements Serializable, Comparable<DtoEntregaFotografiasEstadia>{
    @Getter @Setter @NonNull EntregaFotografiasEstudiante entregaFotografiasEstudiante;
    @Getter @Setter @NonNull AreasUniversidad programaEducativo;
    @Getter @Setter @NonNull Personal personalRecibio;
    
    @Override
    public int compareTo(DtoEntregaFotografiasEstadia o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoEntregaFotografiasEstadia dtoEntregaFotografiasEstadia){
         return dtoEntregaFotografiasEstadia.getProgramaEducativo().getNombre().concat(" ")
                 .concat(dtoEntregaFotografiasEstadia.getEntregaFotografiasEstudiante().getMatricula().getAspirante().getIdPersona().getApellidoPaterno().concat(" "))
                 .concat(dtoEntregaFotografiasEstadia.getEntregaFotografiasEstudiante().getMatricula().getAspirante().getIdPersona().getApellidoMaterno().concat(" "))
                 .concat(dtoEntregaFotografiasEstadia.getEntregaFotografiasEstudiante().getMatricula().getAspirante().getIdPersona().getNombre());
    }
}
