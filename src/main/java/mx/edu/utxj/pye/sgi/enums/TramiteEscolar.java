package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum TramiteEscolar {
    INSCRIPCION("Inscripción"),
    INSCRIPCION_ING("Inscripción ingeniería");
    @Getter @Setter @NonNull private String label;
}
