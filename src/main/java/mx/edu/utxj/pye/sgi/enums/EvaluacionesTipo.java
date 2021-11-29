package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum EvaluacionesTipo {//Tutor, Control interno, Docente, Satisfacción de egresados de ingenieria, Clima laboral, Estudio egresados, Premios, Docente materia, Estudio socioeconómico, Evaluación de Conocimiento y Cumplimiento
    EVALUACION_DESEMPENIO(1, "Desempeño"),
    EVALUACION_360(2, "360°"),
    SERVICIOS(3, "Servicios"),
    SATISFACCION_EGRESADOS_TSU(4, "Satisfacción de egresados de TSU"),
    EVALUACION_ESTADIA(5, "Evaluación Estadía"),
    CEDULA_IDENTIFICACION(6, "Cédula de identificación"),
    CUESTIONARIO_PSICOPEDAGOGICO(7, "Cuestionario Psicopedagógico"),
    EVALUACION_ETICA(8, "Evaluación ética"),
    EVALUACION_AMBIENTAL(9, "Evaluación ambiental"),
    CONDICIONES_ESTUDIO(10, "Condiciones estudio"),
    TUTOR(11,"Tutor (Cuestionario 1)"),
    TUTOR_2(12,"Tutor (Cuestionario 2)"),
    TUTOR_1(11,"Tutor"),
    DOCENTE(13,"Docente materia"),
    DOCENTE_1(20, "Docente materia (Cuestionario 1)"),
    DOCENTE_2(14,"Docente materia (Cuestionario 2 por contingencia)"),
    DOCENTE_3(15,"Docente materia (Cuestionario 3 por contingencia)"),
    DOCENTE_4(16,"Docente materia (Cuestionario 4 por contingencia)"),
    CUESTIONARIO_COMPLEMENTARIO_INFORMACION_PERSONAL(16,"Cuestionario complementario de información personal"),
    EVALUACION_PARES_ACADEMICOS(17,"Evaluación entre pares académicos"),
    TEST_DIAGNOSTICO_APRENDIZAJE(18, "Test de Diagnóstico de Estilos de Aprendizaje"),
    EVALUACION_CODIGO_ETICA_CONDUCTA(19,"Evaluacion conocimientos de los código ética y conducta")
    ;
    @Getter @NonNull private final Integer numero;
    @Getter @NonNull private final String label;
}
