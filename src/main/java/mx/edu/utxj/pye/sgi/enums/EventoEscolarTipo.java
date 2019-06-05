package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventoEscolarTipo {
    CARGA_ACADEMICA("Carga_académica"),
    CAPTURA_CALIFICACIONES("Captura_de_calificaciones"),
    CAPTURA_CALIFICACIONES_EXTEMPORANEA("Captura_de_calificaciones_extemporánea");
    @Getter @NonNull private final String label;
}
