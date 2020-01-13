package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum TramitesSupervisorFiltro {
    SIN_VALIDAR_COMISION(1D, "Sin_validar_comisión"),
    SIN_ASIGNAR_TARIFA(2D,"Sin_asignar_tarifa"),
    SIN_VALIDAR_FACTURAS(3D,"Sin_validar_facturas"),
    SIN_FORMATOS_AUTORIZADOS(4D,"Sin_formatos_autorizados"),
    SIN_LIQUIDAR(5D, "Sin_liquidar");
    @Getter @NonNull private final Double id;
    @Getter @NonNull private final String label;
}
