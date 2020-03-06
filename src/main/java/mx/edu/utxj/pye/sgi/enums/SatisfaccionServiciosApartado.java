package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum SatisfaccionServiciosApartado {
    APOYO_PSICOPEDAGOGICO_1       (1D, "Apoyo Psicopedagógico"),
    ACTIVIDADES_ARTISTICAS_2      (2D, "Actividades Artísticas"),
    SERVICIO_MEDICO_INTERNO_3     (3D, "Servicio Médico Interno"),
    SERVICIO_MEDICO_EXTERNO_4     (4D, "Servicio Médico Externo"),
    ACTIVIDADES_DEPORTIVAS_5      (5D, "Actividades Deportivas"),
    TUTORIAS_6                    (6D, "Tutorías"),
    ASESORIAS_7                   (7D, "Asesorías"),
    CAFETERIA_8                   (8D, "Cafetería"),
    DESARROLLO_HUMANO_9           (9D, "Actividades para el Desarrollo Humano"),
    BIBLIOTECA_10                 (10D, "Biblioteca"),
    INSTALACIONES_11              (11D, "Instalaciones"),
    TRANSPORTE_12                 (12D, "Transporte"),
    MEDIOS_DE_EXPRESION_13        (13D, "Medios de Expresión"),
    BECAS_Y_ESTIMULOS_14          (14D, "Becas y Estimulos"),
    BOLSA_DE_TRABAJO_15           (15D, "Bolsa de Trabajo");

    @NonNull @Getter @Setter private Double apartado;
    @NonNull @Getter @Setter private String tema_corto;
}
