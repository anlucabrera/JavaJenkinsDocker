package mx.edu.utxj.pye.sgi.enums.rol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NivelRol {
    OPERATIVO(1, "Operativo"),
    SUPERIOR(1, "Superior"),
    SUPERVISOR(1, "Supervisor"),
    CONSULTA(1, "Consulta");

    @Getter @NonNull private Integer id;
    @Getter @NonNull private String label;
}
