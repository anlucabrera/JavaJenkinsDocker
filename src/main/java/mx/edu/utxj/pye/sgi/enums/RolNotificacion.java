/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum RolNotificacion {
//    Se puede determinar mediante la base de datos - tabla personal
    RECTOR(1D, "Rector"),     
//    Se puede determinar mediante la base de datos
    SECRETARIO_ACADEMICO(2D, "Secretario Académico"),
//    Se puede determinar mediante la base de datos
    DIRECTOR(3D, "Director"),
//    Se puede determinar mediante la base de datos
    JEFE_DEPARTAMENTO(4D, "Jefe de Departamento"),
//    Se puede determinar mediante la base de datos
    COORDINADOR(5D, "Coordinador"),
//    Se puede determinar mediante la base de datos
    ADMINISTRATIVO(6D, "Administrativo"),
//    Se puede determinar mediante la base de datos
    DOCENTE(7D, "Docente"),
//    Se puede determinar mediante la base de datos
    LABORATORISTA(8D, "Laboratorista"),
//    Aún no se puede determinar de la base de datos
    COORDINADOR_ESTADIAS(9D, "Coordinador de Estadías"),
//    Se puede determinar mediante la base de datos
    COORDINADOR_TUTORES(10D, "Coordinador de Tutores"),
//    Se puede determinar mediante la base de datos
    TUTOR(11D, "Tutor"),
//    Aún no se puede determinar de la base de datos
    ASESOR_DE_ESTADIA(12D, "Asesor de estadía"),
//    Se puede determinar mediante la base de datos
    ESTUDIANTE(13D, "Estudiante"),
//    Aún no se puede determinar de la base de datos - o bien tomar como referencia que esté en 6to cuatrimestre
    ESTADIA(14D, "Estadía"),
//    Aún no se puede determinar de la base de datos - ó bien tomar como referencia el tipo de estudiante
    EGRESADO(15D, "Egresado"),
//    Se puede determinar mediante el sistema directamente
    ASPIRANTE(16D, "Aspirante"),
//    Aún no se puede determinar de la base de datos
    NUEVO_INGRESO(17D, "Nuevo Ingreso"),
//    Se puede determinar mediante el sistema directamente
    INVITADO(18D, "Invitado");
    @Getter @NonNull    private Double nivel;
    @Getter @NonNull    private String label;
    
    public static List<RolNotificacion> ListaNotificacion(){
        return Arrays.asList(RolNotificacion.values())
                .stream()
                .collect(Collectors.toList());
    }
}
