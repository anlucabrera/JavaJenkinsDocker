package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum AsignacionTarifaEtapa {
    CALCULAR_DISTANCIA(1,"Calcular_distancia"),
    TARIFA_VIATICOS(2,"Tarifa_de_viáticos"),
    CONFIRMAR(3,"Confirmar");
    @Getter @Setter @NonNull private Integer id;
    @Getter @Setter @NonNull private String label;
}
