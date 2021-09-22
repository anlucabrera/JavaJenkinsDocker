package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import javax.faces.model.SelectItem;
import java.math.BigDecimal;
import java.util.List;

public class CedulaIdentificacionRolPsicopedagogia extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personalPsicopedagogia;
    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;
    @Getter @Setter String matricula;
    //TODO: Representa la carrera del estudiante
    @Getter @Setter AreasUniversidad carrera;
    //TODO: Representa al estudiante que esta buscando
    @Getter @Setter Estudiante estudiante;
    //TODO: Lista general de estudiantes
    @Getter @Setter List<Estudiante> estudiantes;
    @Getter @Setter DtoCedulaIdentificacion cedulaIdentificacion;
    //TODO: Respresenta cuestionario psicopedagogico
    @Getter @Setter Evaluaciones cuestionario;
    @Getter @Setter CuestionarioPsicopedagogicoResultados resultados;
    //TODO: Cuestionario finalizado Estudiante /Personal Examinador
    @Getter @Setter boolean finalizadoPersonal ;
    @Getter @Setter List<Apartado> apartados;
    @Getter @Setter List<Apartado> apartadoCuestionario;
    @Getter @Setter private  List<SelectItem>  sino,gruposVunerabilidad, estadoCivilPadres, tecnicasEstudio,famFinado,tipoProblemaFam;
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
    @Getter @NonNull Integer periodo;

    public CedulaIdentificacionRolPsicopedagogia(Filter<PersonalActivo> filtro, PersonalActivo personalPsicopedagogia, AreasUniversidad programa) {
        super(filtro);
        this.personalPsicopedagogia = personalPsicopedagogia;
        this.programa = programa;
    }
    public void setPersonalPsicopedagogia(PersonalActivo personalPsicopedagogia) {
        this.personalPsicopedagogia = personalPsicopedagogia;
    }
    public void setMateriasPorEstudiante(List<DtoCalificacionEstudiante.MateriasPorEstudiante> materiasPorEstudiante) {
        this.materiasPorEstudiante = materiasPorEstudiante;
    }
    public void setPeriodoEstudiante(PeriodosEscolares periodoEstudiante) {
        this.periodoEstudiante = periodoEstudiante;
    }
    public void setPeriodoE(Integer periodoE) {
        this.periodoE = periodoE;
    }
    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
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
    public void setPromedio(BigDecimal promedio) {
        this.promedio = promedio;
    }
    public void setPromedioAcumluado(BigDecimal promedioAcumluado) {
        this.promedioAcumluado = promedioAcumluado;
    }
    public void setTareaIntegradoraPresentacion(List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion) {
        this.tareaIntegradoraPresentacion = tareaIntegradoraPresentacion;
    }
    public void setCalificacionesNivelacionPorMateria(List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria) {
        this.calificacionesNivelacionPorMateria = calificacionesNivelacionPorMateria;
    }
}
