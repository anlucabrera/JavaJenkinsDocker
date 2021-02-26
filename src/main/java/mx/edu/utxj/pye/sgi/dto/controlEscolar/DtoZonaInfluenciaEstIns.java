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
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor @ToString
public class DtoZonaInfluenciaEstIns implements Serializable, Comparable<DtoZonaInfluenciaEstIns>{
    @Getter @Setter @NonNull Municipio municipio;
    @Getter @Setter @NonNull Integer estudiantesColocados;
    @Getter @Setter @NonNull Double porcentajeEstudiantes;

    @Override
    public int compareTo(DtoZonaInfluenciaEstIns o) {
        return toLabel(this).compareTo(toLabel(o));
    }

    public static String toLabel(DtoZonaInfluenciaEstIns dtoZonaInfluenciaEstIns){
         return dtoZonaInfluenciaEstIns.getMunicipio().getEstado().getNombre().concat(" ")
                 .concat(dtoZonaInfluenciaEstIns.getMunicipio().getNombre());
    }
}
