package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum ValorVacio {
    PENDIENTE("Pendiente");
    @Getter @Setter @NonNull private String label;
}
