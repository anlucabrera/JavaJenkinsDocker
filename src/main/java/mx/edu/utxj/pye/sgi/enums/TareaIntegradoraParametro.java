package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public enum TareaIntegradoraParametro {
    //No Acreditado(5), Satisfactorio(8), Destacado (9) y Autónomo (10)
    SIN_EVALUACION("Sin evaluación", BigDecimal.ZERO),
    NO_ACREDITADO("No acreditado", new BigDecimal(5)),
    SATISFACTORIO("Satisfactorio", new BigDecimal(8)),
    DESTACADO("Destacado", new BigDecimal(9)),
    AUTONOMO("Autónomo", new BigDecimal(10));
    @Getter @NonNull private final String label;
    @Getter @NonNull private final BigDecimal valor;
}
