/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum DocumentosEstudiante {
    ACTA_NACIMIENTO(1.1D,"Acta de Nacimiento"),
    CERTIFICADO_IEMS(1.2D,"Certificado de IEMS");
    @Getter @NonNull    private Double  numero;
    @Getter @NonNull    private String  label;
}
