package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum EvaluacionesTipo {
    EVALUACION_DESEMPENIO(1), EVALUACION_360(2), SERVICIOS(3), SATISFACCION_EGRESADOS_TSU(4);
    
    @Getter @NonNull private final Integer numero;
}
