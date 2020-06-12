package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum TramiteEscolar {
    INSCRIPCION("Incripción");
    @Getter @Setter @NonNull private String label;
}
