/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum TipoSeguroFacultativo {
    ESTUDIANTE_ACTIVO(0D, "Estudiante"),
    PADRES(1.1D,"Padres"),
    ESPOSA(1.2D, "Esposa"),
    ESPOSO(1.3D, "Esposo"),
    HIJOS(1.4D, "Hijos"),
    LABORAL(1.5D,"Laboral"),
    CONYUGUE(1.6D,"Conyugue");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
    
    public static List<TipoSeguroFacultativo> Lista(){
        return Arrays.asList(TipoSeguroFacultativo.values())
                .stream()
                .collect(Collectors.toList());
    }
}
