package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConsultaCalificacionesRolCoordinadorAD extends AbstractRol{

    @Getter @NonNull private PersonalActivo coordinador;
    @Getter @NonNull private AreasUniversidad programa;
    @Getter @NonNull private PeriodosEscolares periodosEscolar;
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    @Getter @NonNull private List<Estudiante> estudiantes;
    @Getter @NonNull private Estudiante estudiante;
    @Getter @NonNull private Integer periodoSelect;
    @Getter @NonNull private List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante, promedioMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria, calificacionesFinalesPorMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria;
    @Getter @NonNull private List<Calificacion> calificacion;
    @Getter @NonNull private List<BigDecimal> promediosAcumulados;
    @Getter @NonNull private Integer periodo, idMateria, matricula;
    @Getter @NonNull private BigDecimal promedio = BigDecimal.ZERO;
    @Getter @NonNull private BigDecimal promedioAcumluado = BigDecimal.ZERO;

    public ConsultaCalificacionesRolCoordinadorAD(Filter<PersonalActivo> filtro, PersonalActivo coordinador, AreasUniversidad programa) {
        super(filtro);
        this.coordinador = coordinador;
        this.programa = programa;
    }

    public void setCoordinador(PersonalActivo coordinador) {
        this.coordinador = coordinador;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setPeriodosEscolar(PeriodosEscolares periodosEscolar) {
        this.periodosEscolar = periodosEscolar;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    public void setPeriodoSelect(Integer periodoSelect) {
        this.periodoSelect = periodoSelect;
    }

    public void setMateriasPorEstudiante(List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante) {
        this.materiasPorEstudiante = materiasPorEstudiante;
    }

    public void setPromedioMateria(List<DtoCalificacionEstudiante.MateriasPorEstudiante> promedioMateria) {
        this.promedioMateria = promedioMateria;
    }

    public void setUnidadesPorMateria(List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria) {
        this.unidadesPorMateria = unidadesPorMateria;
    }

    public void setMapUnidadesTematicas(List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas) {
        this.mapUnidadesTematicas = mapUnidadesTematicas;
    }

    public void setCalificacionePorUnidad(List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad) {
        this.calificacionePorUnidad = calificacionePorUnidad;
    }

    public void setCalificacionePorMateria(List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria) {
        this.calificacionePorMateria = calificacionePorMateria;
    }

    public void setCalificacionesFinalesPorMateria(List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionesFinalesPorMateria) {
        this.calificacionesFinalesPorMateria = calificacionesFinalesPorMateria;
    }

    public void setTareaIntegradoraPresentacion(List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion) {
        this.tareaIntegradoraPresentacion = tareaIntegradoraPresentacion;
    }

    public void setCalificacionesNivelacionPorMateria(List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria) {
        this.calificacionesNivelacionPorMateria = calificacionesNivelacionPorMateria;
    }

    public void setCalificacion(List<Calificacion> calificacion) {
        this.calificacion = calificacion;
    }

    public void setPromediosAcumulados(List<BigDecimal> promediosAcumulados) {
        this.promediosAcumulados = promediosAcumulados;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    public void setPromedioAcumluado(BigDecimal promedioAcumluado) {
        this.promedioAcumluado = promedioAcumluado;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }
}
