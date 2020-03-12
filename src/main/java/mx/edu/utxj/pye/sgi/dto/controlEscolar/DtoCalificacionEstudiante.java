package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class DtoCalificacionEstudiante {

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class MateriasPorEstudiante{
        @Getter @Setter @NonNull Integer periodo;
        @Getter @Setter @NonNull Grupo grupo;
        @Getter @Setter @NonNull CargaAcademica cargaAcademica;
        @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull Personal docente;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class UnidadesPorEstudiante{
        @Getter @Setter @NonNull Estudiante estudiante;
        @Getter @Setter @NonNull Grupo grupo;
        @Getter @Setter @NonNull CargaAcademica cargaAcademica;
        @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull UnidadMateria unidadMateria;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "materia")
    public static class UnidadesPorMateria{
        @Getter @Setter @NonNull CargaAcademica materia;
        @Getter @Setter @NonNull List<UnidadMateria> unidadMaterias;
    }

    public static class MapUnidadesTematicas{
        @Getter @Setter @NonNull Integer cveGrupo;
        @Getter @Setter @NonNull Integer noUnidad;

        public MapUnidadesTematicas(@NonNull Integer cveGrupo, @NonNull Integer noUnidad) {
            this.cveGrupo = cveGrupo;
            this.noUnidad = noUnidad;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MapUnidadesTematicas)) return false;
            MapUnidadesTematicas that = (MapUnidadesTematicas) o;
            return getCveGrupo().equals(that.getCveGrupo()) &&
                    getNoUnidad().equals(that.getNoUnidad());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getCveGrupo(), getNoUnidad());
        }

        @Override
        public String toString() {
            return "MapUnidadesTematicas{" +
                    "cveGrupo=" + cveGrupo +
                    ", noUnidad=" + noUnidad +
                    '}';
        }
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CalificacionesPorUnidadyMateria1{
        @Getter @Setter @NonNull Estudiante estudiante;
        @Getter @Setter @NonNull Grupo grupo;
        @Getter @Setter @NonNull Integer idGrupo;
        @Getter @Setter @NonNull CargaAcademica cargaAcademica;
        @Getter @Setter @NonNull Integer idCargaAcademica;
        @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
        @Getter @Setter @NonNull Integer idPlanEstudioMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull Integer idMateria;
        @Getter @Setter @NonNull UnidadMateria unidadMateria;
        @Getter @Setter @NonNull Integer idUnidadMateria;
        @Getter @Setter @NonNull UnidadMateriaConfiguracion unidadMateriaConfiguracion;
        @Getter @Setter @NonNull Integer idUnidadMateriaConfiguracion;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionDetalle unidadMateriaConfiguracionDetalle;
        @Getter @Setter @NonNull Long idUnidadMateriaConfiguracionDetalle;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionCriterio unidadMateriaConfiguracionCriterio;
        @Getter @Setter @NonNull Integer idUnidadMateriaConfiguracionCriterio;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CalificacionesPorUnidadyMateria{
        @Getter @Setter @NonNull Grupo grupo;
        @Getter @Setter @NonNull Integer idGrupo;
        @Getter @Setter @NonNull CargaAcademica cargaAcademica;
        @Getter @Setter @NonNull Integer idCargaAcademica;
        @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
        @Getter @Setter @NonNull Integer idPlanEstudioMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull Integer idMateria;
        @Getter @Setter @NonNull UnidadMateria unidadMateria;
        @Getter @Setter @NonNull Integer idUnidadMateria;
        @Getter @Setter @NonNull UnidadMateriaConfiguracion unidadMateriaConfiguracion;
        @Getter @Setter @NonNull Integer idUnidadMateriaConfiguracion;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionDetalle unidadMateriaConfiguracionDetalle;
        @Getter @Setter @NonNull Long idUnidadMateriaConfiguracionDetalle;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionCriterio unidadMateriaConfiguracionCriterio;
        @Getter @Setter @NonNull Integer idUnidadMateriaConfiguracionCriterio;
        @Getter @Setter @NonNull Calificacion calificacion;
        @Getter @Setter @NonNull Long idCalificacion;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CalificacionesPorDetalle{
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull Integer idMateria;
        @Getter @Setter @NonNull UnidadMateria unidadMateria;
        @Getter @Setter @NonNull Integer idUnidadMateria;
        @Getter @Setter @NonNull UnidadMateriaConfiguracion unidadMateriaConfiguracion;
        @Getter @Setter @NonNull Integer idUnidadMateriaConfiguracion;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionDetalle unidadMateriaConfiguracionDetalle;
        @Getter @Setter @NonNull Long idUnidadMateriaConfiguracionDetalle;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionCriterio unidadMateriaConfiguracionCriterio;
        @Getter @Setter @NonNull Integer idUnidadMateriaConfiguracionCriterio;
        @Getter @Setter @NonNull Calificacion calificacion;
        @Getter @Setter @NonNull Long idCalificacion;
        @Getter @Setter @NonNull BigDecimal porcentajeDetalle;
        @Getter @Setter @NonNull BigDecimal valor;
        @Getter @Setter @NonNull BigDecimal promedio_detalle;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CalificacionePorCriterio{
        @Getter @Setter @NonNull Integer idMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull Integer idUnidad;
        @Getter @Setter @NonNull UnidadMateria unidadMateria;
        @Getter @Setter @NonNull UnidadMateriaConfiguracion configuracion;
        @Getter @Setter @NonNull Integer idCriterio;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionCriterio criterio;
        @Getter @Setter @NonNull Long idDetalle;
        @Getter @Setter @NonNull UnidadMateriaConfiguracionDetalle detalle;
        @Getter @Setter @NonNull Estudiante estudiante;
        @Getter @Setter @NonNull BigDecimal porcentajeRecomendado;
        @Getter @Setter @NonNull BigDecimal promedioCriterio;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CalificacionePorUnidad{
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull UnidadMateria unidadMateria;
        @Getter @Setter @NonNull UnidadMateriaConfiguracion configuracion;
        @Getter @Setter @NonNull Estudiante estudiante;
        @Getter @Setter @NonNull String escala;
        @Getter @Setter @NonNull BigDecimal porcentaje;
        @Getter @Setter @NonNull BigDecimal promedioUnidad;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CalificacionePorMateria{
        @Getter @Setter @NonNull Grupo grupo;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull BigDecimal promedio;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class TareaIntegradoraPorMateria{
        @Getter @Setter @NonNull Estudiante estudiante;
        @Getter @Setter @NonNull Grupo grupo;
        @Getter @Setter @NonNull CargaAcademica cargaAcademica;
        @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull TareaIntegradora tareaIntegradora;
        @Getter @Setter @NonNull TareaIntegradoraPromedio tareaIntegradoraPromedio;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class TareaIntegradoraPresentacion{
        @Getter @Setter @NonNull Grupo grupo;
        @Getter @Setter @NonNull CargaAcademica cargaAcademica;
        @Getter @Setter @NonNull TareaIntegradoraPromedio tareaIntegradora;
        @Getter @Setter @NonNull String promedio;
        @Getter @Setter @NonNull String porcentaje;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class PromediosNivelacionesPorMateria{
        @Getter @Setter @NonNull Integer idMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull UnidadMateriaComentario unidadMateriaComentario;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class CalificacionesNivelacionPorMateria{
        @Getter @Setter @NonNull Integer idMateria;
        @Getter @Setter @NonNull Materia materia;
        @Getter @Setter @NonNull BigDecimal promedio;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoCargaAcademica{
        @Getter @Setter @NonNull CargaAcademica carga;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoPeriodosEscolares{
        @Getter @Setter @NonNull PeriodosEscolares periodosEscolares;
    }


    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "dtoInscripcion")
    public static class DtoHistorialCalificaciones{
        @Getter @Setter @NonNull DtoInscripcion dtoInscripcion;
        @Getter @Setter @NonNull List<mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica> dtoCargaAcademicas;
        @Getter @Setter @NonNull List<MapUnidadesTematicas> mapUnidadesTematicas;
        @Getter @Setter @NonNull List<DtoUnidadesCalificacionEstudiante> dtoUnidadesCalificacionEstudiantes;
    }
}

