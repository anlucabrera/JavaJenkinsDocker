package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum GastoTipo {
    ANTICIPADO("Anticipado"), 
    DEVENGADO("Devengado");
    @Getter @NonNull private final String label;
}
