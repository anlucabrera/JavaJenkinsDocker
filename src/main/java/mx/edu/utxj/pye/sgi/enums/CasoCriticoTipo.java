package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CasoCriticoTipo {
    ASISTENCIA_IRREGURLAR("Asistencia irregular"),
    INDISCIPLINA("Indisciplina"),
    FALTA_ADMINISTRATIVA("Falta administrativa"),
    BAJO_APROVECHAMIENTO("Bajo aprovechamiento escolar"),
    INCUMPLIMIENTO("Incumplimiento de tareas, trabajos, prácticas"),
    DIFERENCIA_ASIMILACION("Diferencia en la detección de asimilación de conocimientos y habilidades"),
    DIFERENCIA_VALORES_ACTITUDES("Deficiencia en la información de valores y actitudes");
    @Getter @NonNull private String label;
}
