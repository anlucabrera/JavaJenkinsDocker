/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum RolNotificacion {
    RECTOR(1D,"Rector"),
    SECRETARIO_ACADEMICO(2D,"Secretario Academico"),
    DIRECTOR(3D,"Director"),
    JEFE_DEPARTAMENTO(4D,"Jefe Departamento"),
    COORDINADOR(5D,"Coordinador"),
    ADMINISTRATIVO(6D,"Administrativo"),
    DOCENTE(7D,"Docente"),
    LABORATORISTA(8D,"Laboratorista"),
    COORDINADOR_TUTORES(9D,"Coordinador Tutores"),
    TUTOR(10D,"Tutor"),
    ESTUDIANTE(11D,"Estudiante"),
    EGRESADO(12D,"Egresado"),
    ASPIRANTE(13D,"Aspirante"),
    NUEVO_INGRESO(14D,"Nuevo Ingreso"),
    INVITADO(15D,"Invitado");
    @Getter @NonNull    private Double nivel;
    @Getter @NonNull    private String label;
}
