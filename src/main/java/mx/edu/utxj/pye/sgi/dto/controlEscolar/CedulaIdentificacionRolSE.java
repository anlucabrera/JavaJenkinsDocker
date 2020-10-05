package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import javax.servlet.http.Part;
import java.math.BigDecimal;
import java.util.List;


public class CedulaIdentificacionRolSE  extends AbstractRol {

    @Getter @NonNull private PersonalActivo serviciosEscolares;

    @Getter @NonNull private AreasUniversidad programa;
    @Getter @NonNull private Boolean avisoPrivacidad=false;
    // Apartados de la cedula de identificación(Informacion completa del estudiante)
    @Getter @Setter private List<Apartado> apartados;
    //Lista general de Estudiantes
    @Getter @Setter private List<Estudiante> estudiantes;
    @Getter @Setter String matricula;
    // Representa al estudiante que esta buscando
    @Getter @Setter Estudiante estudiante;
    @Getter @NonNull private  Estudiante estudiantePeriodo;
    @Getter @NonNull private  List<PeriodosEscolares> periodosEstudiante;
    //Cedula de identificacion del estudiante
    @Getter @Setter DtoCedulaIdentificacion cedulaIdentificacion;
    @Getter @Setter String pwdNueva;
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
    //Fotografia y firma  del estudiante
    @Getter @NonNull private Part fileFoto,fileFirma;

    public CedulaIdentificacionRolSE(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad programa) {
        super(filtro);
        this.serviciosEscolares = serviciosEscolares;
        this.programa = programa;
    }

    public void setServiciosEscolares(PersonalActivo serviciosEscolares) {
        this.serviciosEscolares = serviciosEscolares;
    }
    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
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

    public void setPeriodosEstudiante(List<PeriodosEscolares> periodosEstudiante) {
        this.periodosEstudiante = periodosEstudiante;
    }

    public void setEstudiantePeriodo(Estudiante estudiantePeriodo) {
        this.estudiantePeriodo = estudiantePeriodo;
    }

    public void setAvisoPrivacidad(Boolean avisoPrivacidad) {
        this.avisoPrivacidad = avisoPrivacidad;
    }

    public void setFileFoto(Part fileFoto) { this.fileFoto = fileFoto; }

    public void setFileFirma(Part fileFirma) { this.fileFirma = fileFirma; }
}
