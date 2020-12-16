package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaCalificacionesRolMultiple extends AbstractRol {

    @Getter @NonNull private PersonalActivo coordinador;
    @Getter @NonNull private AreasUniversidad programa;
    @Getter @NonNull private PeriodosEscolares periodosEscolar;
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    @Getter @NonNull private List<DtoEstudiante> estudiantes, estudianteRegistro = new ArrayList<>();
    @Getter @Setter @NonNull private List<DtoInscripcion> dtoInscripciones = new ArrayList<>();
    @Getter @NonNull private DtoEstudiante estudiante, estudianteK;
    @Getter @NonNull private Integer periodoSelect;
    @Getter @NonNull private List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas;
    @Getter @NonNull private List<BigDecimal> promediosAcumulados;
    @Getter @NonNull private Integer periodo, idMateria, matricula;
    @Getter @Setter private Short carrera;
    @Getter @NonNull private BigDecimal promedio = BigDecimal.ZERO;
    @Getter @NonNull private BigDecimal promedioAcumluado = BigDecimal.ZERO;

    @Getter @NonNull private List<DtoCargaAcademica> cargasEstudiante = new ArrayList<>();
    @Getter @Setter @NonNull List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones = new ArrayList<>();
    @Getter private Map<DtoCargaAcademica, List<DtoUnidadConfiguracion>> dtoUnidadConfiguracionesMap = new HashMap<>();
    @Getter @NonNull private DtoUnidadesCalificacionEstudiante dtoUnidadesCalificacionEstudiante;
    @Getter private Map<DtoCargaAcademica, DtoUnidadesCalificacionEstudiante> dtoUnidadesCalificacionMap = new HashMap<>();
    @Getter @NonNull private List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria;
    @Getter @Setter private Boolean tieneIntegradora = false;
    @Getter @Setter private Map<DtoCargaAcademica, Boolean> tieneIntegradoraMap = new HashMap<>();
    @Getter @Setter private Map<DtoCargaAcademica, TareaIntegradora> tareaIntegradoraMap = new HashMap<>();
    @Getter @Setter private List<DtoCalificacionEstudiante.DtoHistorialCalificaciones> dtoHistorialCalificaciones;
    @Getter @Setter private List<AreasUniversidad> areasUniversidad = new ArrayList<>();

    @Getter @Setter private Map<DtoInscripcion, BigDecimal> mapTopMejoresEstudiantes = new HashMap<>(), mapTopMejoresEstudiantePE = new HashMap<>(), mapTopMejoresEstudiantesCuatri = new HashMap<>();

    public ConsultaCalificacionesRolMultiple(Filter<PersonalActivo> filtro, PersonalActivo coordinador, AreasUniversidad programa) {
        super(filtro);
        this.coordinador = coordinador;
        this.programa = programa;
    }

    public void setEstudianteK(DtoEstudiante estudianteK) {
        this.estudianteK = estudianteK;
    }

    public void setCargasEstudiante(List<DtoCargaAcademica> cargasEstudiante) {
        this.cargasEstudiante = cargasEstudiante;
    }

    public void setDtoUnidadesCalificacionEstudiante(DtoUnidadesCalificacionEstudiante dtoUnidadesCalificacionEstudiante) {
        this.dtoUnidadesCalificacionEstudiante = dtoUnidadesCalificacionEstudiante;
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

    public void setEstudiantes(List<DtoEstudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void setEstudiante(DtoEstudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    public void setPeriodoSelect(Integer periodoSelect) {
        this.periodoSelect = periodoSelect;
    }

    public void setUnidadesPorMateria(List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria) {
        this.unidadesPorMateria = unidadesPorMateria;
    }

    public void setMapUnidadesTematicas(List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas) {
        this.mapUnidadesTematicas = mapUnidadesTematicas;
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

    public void setEstudianteRegistro(List<DtoEstudiante> estudianteRegistro) {
        this.estudianteRegistro = estudianteRegistro;
    }
}
