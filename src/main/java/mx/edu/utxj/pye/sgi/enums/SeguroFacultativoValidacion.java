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
public enum SeguroFacultativoValidacion {
    ALTA(0D, "Alta"),
    BAJA(1.1D,"Baja"),
    EN_ESPERA_DE_VALIDACION(1.2D, "En espera de validación");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
}
