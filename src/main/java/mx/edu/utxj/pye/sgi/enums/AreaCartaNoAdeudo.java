package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AreaCartaNoAdeudo {
    DIRECCION_CARRERA("Dirección de Carrera"),
    BIBLIOTECA("Biblioteca"),
    ESTADISTICA("Departamento de Información y Estadística"),
    CORDINACION_ESTADIA("Coordinación de Estadía"),
    SEGUIMIENTO_EGRESADOS("Seguimiento a Egresados"),
    RECURSOS_MATERIALES ("Departamento de Recursos Materiales y Servicios Generales"),
    SERVICIOS_ESCOLARES("Departamento de Servicios Escolares"),
    CORDINACION_TITULACION("Coordinación de Titulación"),
    FINANZAS("Dirección de Finanzas"),
    ESTUDIANTE("Estudiante")
    ;

    @Getter @NonNull private final String label;
}
