package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum TarifaTipo {
    POR_KILOMETRO("Por kilómetro"),
    POR_ZONA("Por zona"),
    VIAJE("Viaje");
    @Getter @NonNull private final String label;
}
