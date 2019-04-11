package mx.edu.utxj.pye.sgi.enums.rol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ControlEscolarRol {
    ASPIRANTE("Aspirante"),
    ESTUDIANTE(""),
    DOCENTE(""),
    TUTOR(""),
    ASESOR(""),
    DIRECTOR(""),
    SECRETARIA_ACADEMICA(""),
    SERVICIOS_ESTUDIANTILES(""),
    PSICOPEDAGOGIA(""),
    TITULACION(""),
    ESCOLARES_OPERATIVO(""),
    ESCOLARES_DIRECTIVO("");
    @Getter @NonNull private final String label;
}
