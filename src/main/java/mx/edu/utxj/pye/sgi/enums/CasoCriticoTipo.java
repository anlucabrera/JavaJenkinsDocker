package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public enum CasoCriticoTipo {
    ASISTENCIA_IRREGURLAR("Asistencia irregular", 1d),
    INDISCIPLINA("Indisciplina", 1d),
    FALTA_ADMINISTRATIVA("Falta administrativa", 1d),
    BAJO_APROVECHAMIENTO("Bajo aprovechamiento escolar", 1d),
    INCUMPLIMIENTO("Incumplimiento de tareas, trabajos, prácticas", 1d),
    DIFERENCIA_ASIMILACION("Diferencia en la detección de asimilación de conocimientos y habilidades", 1d),
    DIFERENCIA_VALORES_ACTITUDES("Deficiencia en la información de valores y actitudes", 1d),
    PERSONALES("Personales (Autoestima, emocionales, familiares, adicciones, etc.)", 1d),
    PEDAGOGICOS("Pedagógicos", 1d),
    SISTEMA_UNIDAD_REPROBADA("Sistema - unidad reprobada", 0d),
    SISTEMA_TUTORIA_GRUPAL("Sistema - Detectado en Tutoria Grupal",0d),
    SISTEMA_ASISTENCIA_IRREGURLAR("Sistema - Asistencia irregular", 0d);
    @Getter @NonNull private String label;
    @Getter @NonNull private Double nivel;

    /**
     * Lista de tipos de casos críticos que no son generados automáticamente por sistema.
     * @return Regresa la lista de tipos
     */
    public static List<CasoCriticoTipo> Lista(){
        return Arrays.asList(CasoCriticoTipo.values())
                .stream()
                .filter(casoCriticoTipo -> casoCriticoTipo.getNivel() > 0d)
                .collect(Collectors.toList());
    }

    /**
     * Lista de tipos de casos críticos que son generados automáticamente por sistema.
     * @return Regresa la lista de tipos
     */
    public static List<CasoCriticoTipo> ListaSistema(){
        return Arrays.stream(CasoCriticoTipo.values())
                .filter(casoCriticoTipo -> casoCriticoTipo.getNivel() < 1d)
                .collect(Collectors.toList());
    }
}
