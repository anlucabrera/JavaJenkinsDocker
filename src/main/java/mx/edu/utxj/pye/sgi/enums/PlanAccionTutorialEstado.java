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
public enum PlanAccionTutorialEstado {
    REGISTRADO(1D,"Registrado"),
    ENVIADO_PARA_REVISION(2.1D,"Enviado para revisión"),
    EN_OBSERVACIONES(2.2D,"En observaciones"),
    VALIDADO(-1.1D,"Validado");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
}
