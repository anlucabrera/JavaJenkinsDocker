
package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Representa las etapas del controlador de fiscalización para controlar la conversación.
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum FiscalizacionEtapa {
    CONSULTA("Consulta"),
    OFICIO_COMISION("Oficio de comisión"),
    FACTURAS("Facturas"),
    FIN("Fin");
    @Getter @NonNull private final String label;
}
