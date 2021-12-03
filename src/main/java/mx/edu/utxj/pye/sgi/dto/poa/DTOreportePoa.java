/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.poa;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;

/**
 *
 * @author Desarrollo
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DTOreportePoa {

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class General {
        @Getter @Setter @NonNull AreasUniversidad areasUniversidad;
        @Getter @Setter @NonNull Personal personal;
        @Getter @Setter @NonNull EjesRegistro ejesRegistro;
        @Getter @Setter @NonNull Estrategias estrategias;
        @Getter @Setter @NonNull LineasAccion lineasAccion;
        @Getter @Setter @NonNull ActividadesPoa actividadesPoa;
        @Getter @Setter @NonNull UnidadMedidas medidas;
        @Getter @Setter @NonNull RecursosActividad recursosActividad;
        @Getter @Setter @NonNull ProductosAreas productosAreas;
        @Getter @Setter @NonNull Productos productos;
        @Getter @Setter @NonNull Partidas partidas;
        @Getter @Setter @NonNull CapitulosTipos capitulosTipos;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ProgramacionActividades {
        @Getter @Setter @NonNull EjesRegistro ejesRegistro;
        @Getter @Setter @NonNull List<EstrategiaLineas> estrategiaLineas;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class EstrategiaLineas {
        @Getter @Setter @NonNull EjesRegistro ejesRegistro;
        @Getter @Setter @NonNull List<LineasActividades> lineaActividades;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class LineasActividades {
        @Getter @Setter @NonNull LineasAccion lineasAccion;
        @Getter @Setter @NonNull List<ActividadRecurso> actividadeRecurso;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ActividadRecurso {
        @Getter @Setter @NonNull ActividadesPoa actividadesPoa;
        @Getter @Setter @NonNull UnidadMedidas medidas;
        @Getter @Setter List<RecursoActividad> recursosActividad;    
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class RecursoActividad {
        @Getter @Setter @NonNull RecursosActividad recursosActividad;
        @Getter @Setter @NonNull ProductosAreas productosAreas;
        @Getter @Setter @NonNull Productos productos;
        @Getter @Setter @NonNull Partidas partidas;
        @Getter @Setter @NonNull CapitulosTipos capitulosTipos;
    }
}
