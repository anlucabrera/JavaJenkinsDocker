package mx.edu.utxj.pye.sgi.enums.rol;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ControlEscolarRol {
    ASPIRANTE("Aspirante"),
    ESTUDIANTE("Estudiante"),
    DOCENTE("Docente"),
    TUTOR("Tutor"),
    ASESOR("Asesor"),
    DIRECTOR("Director"),
    SECRETARIA_ACADEMICA("Secretaría académica"),
    SERVICIOS_ESTUDIANTILES("Servicios_estudiantiles"),
    PSICOPEDAGOGIA("Psicopedagogía"),
    TITULACION("Titulación"),
    ESCOLARES_OPERATIVO("Escolares operativo"),
    ESCOLARES_DIRECTIVO("Escolares directivo");
    @Getter @NonNull private final String label;
}
