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
public enum RolesConsultaSeguroFacultativo {
    GENERAL(0D, "Rol - General de Consulta"),
    PROGRAMA_EDUCATIVO(1.1D,"Rol - Programa Educativo Específico ó Asignado"),
    ESPECIFICO_COORDINADOR_AREA_ACADEMICA_ESTADIA(1.2D, "Rol - Coordinador de Área Académica de Estadías"),
    ESPECIFICO_TUTOR(1.3D, "Rol - Tutor"),
    ESPECIFICO_ASESOR_ACADEMICO(1.4D, "Rol - Asesor Académico de Estadías");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
    
    public static List<RolesConsultaSeguroFacultativo> Lista(){
        return Arrays.asList(RolesConsultaSeguroFacultativo.values())
                .stream()
                .collect(Collectors.toList());
    }
}
