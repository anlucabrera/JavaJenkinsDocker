package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum TipoPersonaTramite {
    ASPIRANTE("Aspirante"),
    ESTUDIANTE("Estudiante"),
    EGRESADO("Egresado"),
    ASPIRANTE_ING("Aspirante ingeniería");
    @Getter @Setter @NonNull private String label;
}
