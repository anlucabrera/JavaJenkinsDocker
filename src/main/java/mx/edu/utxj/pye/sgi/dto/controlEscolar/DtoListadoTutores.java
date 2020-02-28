/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoListadoTutores implements Serializable, Comparable<DtoListadoTutores>{
    @Getter @Setter @NonNull private Grupo grupo;
    @Getter @Setter private PersonalActivo tutor;
    @Getter @Setter private AreasUniversidad programaEducativo;
    
    @Override
    public int compareTo(DtoListadoTutores o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoListadoTutores dtoListadoTutores){
        return dtoListadoTutores.getProgramaEducativo().getSiglas().concat(" - ")
                .concat(String.valueOf(dtoListadoTutores.getGrupo().getGrado())).concat(dtoListadoTutores.getGrupo().getLiteral().toString()).concat(" - ");
    }
}
