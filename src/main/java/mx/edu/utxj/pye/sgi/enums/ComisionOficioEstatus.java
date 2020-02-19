package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.converter.EnumConverter;

/**
 * Representa los estados del ciclo de vida de un oficio de comisión desde que el trabajador lo solicita, hast
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum ComisionOficioEstatus  {
    SOLICITADO_POR_COMISIONADO                  (1d,    "Solicitado_por_comisionado",               false,  false,  false),
    EVIDENCIADO_POR_COMISIONADO                 (2d,    "Evidenciado_por_comisionado",              false,  false,  false),
    REGRESADO_PARA_REVISION_POR_SUPERIOR        (2.1d,  "Regresado_para_revisión_por_superior",     false,  false,  false),
    CAMBIOS_REALIZADOS_PARA_SUPERIOR            (2.2d,  "Cambios_realizados_para_superior",         true,   false,  false),
    APROBADO_POR_SUPERIOR                       (3d,    "Aprobado_por_superior",                    true,   true,   false),
    REGRESADO_PARA_REVISION_POR_FIZCALIZACION   (3.1d,  "Regresado_para_revisión_por_fiscalización",false,  true,   false),
    CAMBIOS_REALIZADOS_PARA_FIZCALIZACION       (3.2d,  "Cambios_realizados_para_fiscalización",    true,   true,   false),
    VALIDADO_POR_FISCALIZACION                  (4d,    "Validado_por_fiscalización",               true,   true,   true),
    TARIAS_ASIGNADAS_POR_FISCALIZACION          (5d,    "Tarifas_asignadas_por_fiscalización",      true,   true,   true),
    IMPRESO_POR_COMISIONADO                     (6d,    "Impreso_por_comisionado",                  true,   true,   true);
    @Getter @NonNull private final Double id;
    @Getter @NonNull private final String label;
    @Getter @NonNull private final Boolean liberado;
    @Getter @NonNull private final Boolean aprobado;
    @Getter @NonNull private final Boolean validado;


}
