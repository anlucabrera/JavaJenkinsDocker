package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsultaCalificacionesRolEstudiante{

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.CONSULTA;
    @Getter protected List<String> instrucciones = new ArrayList<>();
    @Getter @NonNull private Estudiante estudiante;
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    @Getter @NonNull private List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas;
    @Getter @NonNull private List<BigDecimal> promediosAcumulados;
    @Getter @NonNull private Integer periodo, periodoSeleccionado, idMateria;
    @Getter @NonNull private BigDecimal promedio = BigDecimal.ZERO;
    @Getter @NonNull private BigDecimal promedioAcumluado = BigDecimal.ZERO;
    @Getter @NonNull private DtoCapturaCalificacion captura;

    @Getter @NonNull private List<DtoCargaAcademica> cargasEstudiante;
    @Getter private Map<DtoCargaAcademica, List<DtoUnidadConfiguracion>> dtoUnidadConfiguracionesMap = new HashMap<>();
    @Getter @NonNull private DtoUnidadesCalificacionEstudiante dtoUnidadesCalificacionEstudiante;
    @Getter private Map<DtoCargaAcademica, DtoUnidadesCalificacionEstudiante> dtoUnidadesCalificacionMap = new HashMap<>();
    @Getter @NonNull private List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria;
    @Getter @Setter private Boolean tieneIntegradora = false;
    @Getter @Setter private Map<DtoCargaAcademica, Boolean> tieneIntegradoraMap = new HashMap<>();
    @Getter @Setter private Map<DtoCargaAcademica, TareaIntegradora> tareaIntegradoraMap = new HashMap<>();


    public Boolean tieneAcceso(Estudiante estudiante, UsuarioTipo usuarioTipo){
        if(estudiante == null) return false;
        if(!usuarioTipo.equals(UsuarioTipo.ESTUDIANTE19)) return false;
        return true;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    public void setMapUnidadesTematicas(List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas) {
        this.mapUnidadesTematicas = mapUnidadesTematicas;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public void setCaptura(DtoCapturaCalificacion captura) {
        this.captura = captura;
    }

    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }

    public void setPeriodoSeleccionado(Integer periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public void setPromediosAcumulados(List<BigDecimal> promediosAcumulados) {
        this.promediosAcumulados = promediosAcumulados;
    }

    public void setPromedioAcumluado(BigDecimal promedioAcumluado) {
        this.promedioAcumluado = promedioAcumluado;
    }

    public void setInstrucciones(List<String> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void setCargasEstudiante(List<DtoCargaAcademica> cargasEstudiante) {
        this.cargasEstudiante = cargasEstudiante;
    }

    public void setDtoUnidadConfiguracionesMap(Map<DtoCargaAcademica, List<DtoUnidadConfiguracion>> dtoUnidadConfiguracionesMap) {
        this.dtoUnidadConfiguracionesMap = dtoUnidadConfiguracionesMap;
    }

    public void setDtoUnidadesCalificacionEstudiante(DtoUnidadesCalificacionEstudiante dtoUnidadesCalificacionEstudiante) {
        this.dtoUnidadesCalificacionEstudiante = dtoUnidadesCalificacionEstudiante;
    }

    public void setUnidadesPorMateria(List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria) {
        this.unidadesPorMateria = unidadesPorMateria;
    }
}
