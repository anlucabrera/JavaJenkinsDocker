package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Representa los estados del ciclo de vida de un oficio de comisión desde que el trabajador lo solicita, hast
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum OficioComisionEstatus {
    SOLICITADO_POR_COMISIONADO("Solicitado_por_comisionado"),
    AUTORIZADO_POR_SUPERIOR("Autorizador_por_superior"), 
    REVISADO_POR_FISCALIZACION("Revisado_por_fiscalización"), 
    IMPRESO_POR_FISCALIZACION("Impreso_por_comisionado"), 
    EVIDENCIADO_POR_COMISIONADO("Evidenciado_por_comisionado");
    @Getter @NonNull private final String label;
}
