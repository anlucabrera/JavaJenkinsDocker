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
public class DtoReprobacionAsignatura implements Serializable, Comparable<DtoReprobacionAsignatura> {
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull PeriodosEscolares periodoEscolar;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
    @Getter @Setter @NonNull Integer reprobadosOrdinario;
    @Getter @Setter @NonNull Integer aprobadosNivelacion;
    @Getter @Setter @NonNull Integer reprobadosNivelacion;
    @Getter @Setter @NonNull Integer matriculaInicial;
    @Getter @Setter @NonNull Double porcentajeRepOrdinario;
    @Getter @Setter @NonNull Double porcentajeRepNivelacion;
    
    @Override
    public int compareTo(DtoReprobacionAsignatura o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoReprobacionAsignatura  dtoReprobacionAsignatura){
         return dtoReprobacionAsignatura.getPrograma().getNivelEducativo().getNivel().concat(" ")
                .concat(dtoReprobacionAsignatura.getPrograma().getNombre().concat(" "))
                .concat(String.valueOf(dtoReprobacionAsignatura.getPlanEstudioMateria().getGrado()).concat(" "))
                .concat(dtoReprobacionAsignatura.getPlanEstudioMateria().getClaveMateria());
    }
}
