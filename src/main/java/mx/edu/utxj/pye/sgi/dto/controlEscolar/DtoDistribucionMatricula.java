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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoDistribucionMatricula implements Serializable, Comparable<DtoDistribucionMatricula> {
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull Integer grado;
    @Getter @Setter @NonNull Integer matriculaInicial;
    @Getter @Setter @NonNull Integer desercion;
    @Getter @Setter @NonNull Integer matriculaFinal;
    @Getter @Setter @NonNull Double porcentajeDesercion;
    
    @Override
    public int compareTo(DtoDistribucionMatricula o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoDistribucionMatricula  dtoDistribucionMatricula){
         return dtoDistribucionMatricula.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoDistribucionMatricula.getPrograma().getNombre().concat(" "))
                .concat(String.valueOf(dtoDistribucionMatricula.getGrado()));
    }
    
}
