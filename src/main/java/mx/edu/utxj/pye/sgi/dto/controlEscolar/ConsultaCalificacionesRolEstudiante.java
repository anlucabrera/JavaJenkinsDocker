package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ConsultaCalificacionesRolEstudiante{

    @Getter @Setter @NonNull protected NivelRol nivelRol = NivelRol.CONSULTA;
    @Getter protected List<String> instrucciones = new ArrayList<>();
    @Getter @NonNull private Estudiante estudiante;
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    @Getter @NonNull private List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante, promedioMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria, calificacionesFinalesPorMateria;
    @Getter @NonNull private List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion;
    @Getter @NonNull private List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria;
    @Getter @NonNull private List<Calificacion> calificacion;
    @Getter @NonNull private List<BigDecimal> promediosAcumulados;
    @Getter @NonNull private Integer periodo, periodoSeleccionado, idMateria;
    @Getter @NonNull private BigDecimal promedio = BigDecimal.ZERO;
    @Getter @NonNull private BigDecimal promedioAcumluado = BigDecimal.ZERO;
    @Getter @NonNull private DtoCapturaCalificacion captura;

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

    public void setMateriasPorEstudiante(List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante) {
        this.materiasPorEstudiante = materiasPorEstudiante;
    }

    public void setUnidadesPorMateria(List<DtoCalificacionEstudiante.UnidadesPorMateria> unidadesPorMateria) {
        this.unidadesPorMateria = unidadesPorMateria;
    }

    public void setMapUnidadesTematicas(List<DtoCalificacionEstudiante.MapUnidadesTematicas> mapUnidadesTematicas) {
        this.mapUnidadesTematicas = mapUnidadesTematicas;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public void setCalificacion(List<Calificacion> calificacion) {
        this.calificacion = calificacion;
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

    public void setCalificacionePorUnidad(List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad) {
        this.calificacionePorUnidad = calificacionePorUnidad;
    }

    public void setCalificacionePorMateria(List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria) {
        this.calificacionePorMateria = calificacionePorMateria;
    }

    public void setIdMateria(Integer idMateria) {
        this.idMateria = idMateria;
    }

    public void setPromedioMateria(List<DtoCalificacionEstudiante.MateriasPorEstudiante> promedioMateria) {
        this.promedioMateria = promedioMateria;
    }

    public void setPromediosAcumulados(List<BigDecimal> promediosAcumulados) {
        this.promediosAcumulados = promediosAcumulados;
    }

    public void setPromedioAcumluado(BigDecimal promedioAcumluado) {
        this.promedioAcumluado = promedioAcumluado;
    }

    public void setCalificacionesNivelacionPorMateria(List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria) {
        this.calificacionesNivelacionPorMateria = calificacionesNivelacionPorMateria;
    }

    public void setCalificacionesFinalesPorMateria(List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionesFinalesPorMateria) {
        this.calificacionesFinalesPorMateria = calificacionesFinalesPorMateria;
    }

    public void setInstrucciones(List<String> instrucciones) {
        this.instrucciones = instrucciones;
    }

    public void setTareaIntegradoraPresentacion(List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion) {
        this.tareaIntegradoraPresentacion = tareaIntegradoraPresentacion;
    }
}
