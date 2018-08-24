package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum TramiteTipo {
    COMISION("Comisión"),
    PRODUCTO_SERVICIO("Producto / Servicio");
    @Getter @NonNull private final String label;
}
