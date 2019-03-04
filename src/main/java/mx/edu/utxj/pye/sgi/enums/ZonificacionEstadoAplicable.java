package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum ZonificacionEstadoAplicable {
    TOTAL("Total"), PARCIAL("Parcial");
    @Getter @Setter @NonNull private String label;
}
