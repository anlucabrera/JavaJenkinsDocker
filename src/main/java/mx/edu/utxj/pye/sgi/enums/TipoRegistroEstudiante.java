package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TipoRegistroEstudiante {
    INSCRIPCION("Inscripción"),
    REINSCRIPCION("Reinscripción"),
    REINSCRIPCION_AUTONOMA("Reinscripcion Autónoma"),
    REINCORPORACION_OTRA_UT("Reincorporación otra UT"),
    REGULARIZACION_CALIFICACION_REINCORPORACION("Regularización de calificaciones por reincoporación"),
    REINCORPORACION_OTRA_GENERACION("Reincorporación otra generación"),
    REINCORPORACION_MISMA_UT("Reincorporación misma UT"),
    INSCRIPCION_ING_LIC("Inscripción ingeniería/licenciatura"),
    INSCRIPCION_ING_lIC_OTRA_GENERACION("Inscripción ingeniería/licenciatura otra generación"),
    INSCRIPCION_ING_LIC_OTRA_UT("Inscripción ingeniería/licenciatura otra UT")
    ;
    @Getter @NonNull private final String label;
}
