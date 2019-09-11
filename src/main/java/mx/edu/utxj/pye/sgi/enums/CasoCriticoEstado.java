package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CasoCriticoEstado {
    SIN_REGISTRO(0D, "Sin registro"),//no existe el valor en base de datos, solo se usa para indicar que el caso crítico no se ha registrado en la base de datos
    REGISTRADO(1d, "Registrado"),
    EN_SEGUMIENTO_TUTOR(2.1D, "En seguimiento por tutor"),
    EN_SEGUIMIENTO_ESPECIALISTA(2.2D, "En seguimiento por especialista"),
    CERRADO_TUTOR(-1.1D, "Cerrado por tutor"),
    CERRADO_ESPECIALISTA(-1.2D, "Cerrado por especialista");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
}
