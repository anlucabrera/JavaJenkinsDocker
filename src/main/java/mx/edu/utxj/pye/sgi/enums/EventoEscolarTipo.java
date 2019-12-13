package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EventoEscolarTipo {
    CARGA_ACADEMICA("Carga_académica"),
    CAPTURA_CALIFICACIONES("Captura_de_calificaciones"),
    CAPTURA_CALIFICACIONES_EXTEMPORANEA("Captura_de_calificaciones_extemporánea"),
    REINSCRIPCION_AUTONOMA("Reinscripción_autónoma"),
    REINCORPORACIONES("Reincorporaciones"),
    GENEREACION_GRUPOS("Generacion_de_grupos"),
    CONFIGURACION_DE_MATERIA("Configuración_de_materia"),
    ASIGNACION_TUTORES("Asignación_de_tutores"),
    INSCRIPCIONES("Inscripciones"),
    ASIGNACION_INDICADORES_CRITERIOS("Asignación_indicadores_criterios"),
    VALIDACION_ASIGNACION_INDICADORES_CRITERIOS("Validacion_Asignación_indicadores_criterios"),
    FUSION_GRUPOS("Fusión_de_grupos"),
    CAPTURA_TAREA_INTEGRADORA("Captura_tarea_integradora"),
    CAPTURA_TAREA_INTEGRADORA_EXTERMPORANEA("Captura_tarea_integradora_extemporánea");
    @Getter @NonNull private final String label;
}
