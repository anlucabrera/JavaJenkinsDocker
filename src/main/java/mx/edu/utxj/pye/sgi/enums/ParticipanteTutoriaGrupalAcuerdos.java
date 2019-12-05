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
public enum ParticipanteTutoriaGrupalAcuerdos {
    PENDIENTE_DE_REGISTRO(0D,"Pendiente de Registro"),
    ASISTI_ESTOY_DE_ACUERDO(1.1D,"Asistí - Estoy de acuerdo"),
    ASISTI_NO_ESTOY_DE_ACUERDO(1.2D,"Asistí - No estoy de acuerdo"),
    NO_ASISTI_ESTOY_DE_ACUERDO(-1.1D,"No asistí - Estoy de acuerdo"),
    NO_ASISTI_NO_ESTOY_DE_ACUERDO(-1.2D,"No asistí - No estoy de acuerdo");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
}
