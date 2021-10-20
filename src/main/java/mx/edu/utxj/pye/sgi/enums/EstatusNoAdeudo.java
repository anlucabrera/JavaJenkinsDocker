package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EstatusNoAdeudo {
    LIBERADO("Liberado"),
    NO_LIBERADO("No liberado"),
    EN_REVISION("En revisión"),
    SIN_REVISION("No ha iniciado revisión")
    ;
    @Getter @NonNull private final String label;
}
