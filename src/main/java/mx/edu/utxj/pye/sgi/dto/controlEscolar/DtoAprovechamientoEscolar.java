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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoAprovechamientoEscolar implements Serializable, Comparable<DtoAprovechamientoEscolar> {
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
    @Getter @Setter @NonNull Double promedio;
    
    @Override
    public int compareTo(DtoAprovechamientoEscolar o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoAprovechamientoEscolar  dtoAprovechamientoEscolar){
         return dtoAprovechamientoEscolar.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoAprovechamientoEscolar.getPrograma().getNombre().concat(" "))
                .concat(String.valueOf(dtoAprovechamientoEscolar.getPlanEstudioMateria().getGrado()).concat(" "))
                .concat(dtoAprovechamientoEscolar.getPlanEstudioMateria().getClaveMateria());
    }
}
