package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CedulaIdetificacionRolDirector extends AbstractRol {



    @Getter @NonNull private PersonalActivo director;
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    @Getter @NonNull private PeriodosEscolares periodo;
    @Getter @NonNull private Integer periodoActivo;
    @Getter    @NonNull    private AreasUniversidad programa;
    // Representa el listado de programas educativos vigentes
    @Getter    @NonNull    private List<AreasUniversidad> programas;
    // Representa el listado de las áreas de conocimiento
    @Getter    @NonNull    private List<PlanEstudio> planesEstudios;
    // Representa la referencia al plan de estudios seleccionado
    @Getter    @NonNull    private PlanEstudio planEstudio;
    @Getter    @Setter private List<Listaalumnosca> listaEstudiantes;
    @Getter    @Setter    private List<Estudiante> estudiantes;
    @Getter    @Setter    private Grupo grupoSelec;
    @Getter    @Setter    private List<Grupo> grupos;
    // Mapeo de materias con sus unidades de acuerdo al plan de estudios seleccionado
    @Getter    @NonNull    private Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap;
    //TODO: Apartados de la Cedula de Identificación
    @Getter @Setter List<Apartado> apartados;
    @Getter @Setter DtoCedulaIdentificacion cedulaIdentificacion;
    //TODO: Estudiante seleccionado
    @Getter @Setter Estudiante estudiante;
    @Getter @Setter String matricula;
    //Calificaciones del estudiante
    @Getter @NonNull private List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad;
    @Getter @NonNull private List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas;
    @Getter @NonNull private List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria, calificacionesFinalesPorMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria;
    @Getter @NonNull private BigDecimal promedio = BigDecimal.ZERO;
    @Getter @NonNull private BigDecimal promedioAcumluado = BigDecimal.ZERO;
    @Getter @NonNull PeriodosEscolares periodoEstudiante;
    @Getter @NonNull Integer periodoE;
    @Getter @NonNull Integer periodoX;

    public CedulaIdetificacionRolDirector(Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }
    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setListaEstudiantes(List<Listaalumnosca> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void setGrupoSelec(Grupo grupoSelec) {
        this.grupoSelec = grupoSelec;
    }
    public void setProgramas(List<AreasUniversidad> programas) { this.programas = programas; }

    public void setPlanesEstudios(List<PlanEstudio> planesEstudios) { this.planesEstudios = planesEstudios; }
    public void setPrograma(AreasUniversidad programa) { this.programa = programa; }
    public void setPlanEstudio(PlanEstudio planEstudio) { this.planEstudio = planEstudio; }

    public void setMateriasPorEstudiante(List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante) {
        this.materiasPorEstudiante = materiasPorEstudiante;
    }

    public void setCalificacionePorUnidad(List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad) {
        this.calificacionePorUnidad = calificacionePorUnidad;
    }

    public void setMapUnidadesTematicas(List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas) {
        this.mapUnidadesTematicas = mapUnidadesTematicas;
    }

    public void setUnidadesPorMateria(List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria) {
        this.unidadesPorMateria = unidadesPorMateria;
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

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    public void setPromedioAcumluado(BigDecimal promedioAcumluado) {
        this.promedioAcumluado = promedioAcumluado;
    }

    public void setPeriodoEstudiante(PeriodosEscolares periodoEstudiante) {
        this.periodoEstudiante = periodoEstudiante;
    }

    public void setPeriodoE(Integer periodoE) {
        this.periodoE = periodoE;
    }

    public void setPeriodoX(Integer periodoX){
        this.periodoX = periodoX;
    }

    public void setAreaPlanEstudioMap(Map<AreasUniversidad, List<PlanEstudio>> areaPlanEstudioMap) {
        this.areaPlanEstudioMap = areaPlanEstudioMap;
        this.planesEstudios = new ArrayList<>();
        if (areaPlanEstudioMap != null) {
            this.programas = areaPlanEstudioMap.keySet().stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList());
            areaPlanEstudioMap.forEach((t, u) -> {
                this.planesEstudios.addAll(u);
            });
        }
        if (areaPlanEstudioMap != null && programa != null && areaPlanEstudioMap.containsKey(programa)) {
            this.planesEstudios = areaPlanEstudioMap.get(programa);
            if (planesEstudios != null) {
                planesEstudios.get(0);
            }
        }
    }
}
