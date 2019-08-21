package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CasoCriticoEstado {
    REGISTRADO(1d, "Registrado"),
    EN_SEGUMIENTO_TUTOR(2.1D, ""),
    EN_SEGUIMIENTO_ESPECIALISTA(2.2D, ""),
    CERRADO_TUTOR(-1.1D, ""),
    CERRADO_ESPECIALISTA(-1.2D, "");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
}
