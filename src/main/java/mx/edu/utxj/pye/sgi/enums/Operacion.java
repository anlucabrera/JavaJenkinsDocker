package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum Operacion {
    PERSISTIR("Persistir"),
    ACTUALIZAR("Actualizar"),
    ELIMINAR("Eliminar"),
    REFRESCAR("Refrescar"),
    DETACHAR("Detachar");
    @Getter @Setter @NonNull private String label;
}
