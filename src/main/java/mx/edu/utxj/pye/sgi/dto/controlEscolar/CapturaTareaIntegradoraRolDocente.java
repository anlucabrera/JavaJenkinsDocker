package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Indicador;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapturaTareaIntegradoraRolDocente  extends AbstractRol {
    @Getter private PersonalActivo docenteLogueado;

    @Getter private EventoEscolar eventoActivo;
    @Getter private Integer periodoActivo;
    @Getter private PeriodosEscolares periodoSeleccionado;
    @Getter private List<PeriodosEscolares> periodosConCarga;

    @Getter private DtoCargaAcademica cargaAcademicaSeleccionada;
    @Getter private List<DtoCargaAcademica> cargasDocente;

//    @Getter private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;
    @Getter private Map<DtoCargaAcademica, List<DtoUnidadConfiguracion>> dtoUnidadConfiguracionesMap = new HashMap<>();

//    @Getter private DtoUnidadesCalificacion dtoUnidadesCalificacion;
    @Getter private Map<DtoCargaAcademica, DtoUnidadesCalificacion> dtoUnidadesCalificacionMap = new HashMap<>();

//    @Getter @Setter private Boolean tieneIntegradora = false;
    @Getter @Setter private Map<DtoCargaAcademica, Boolean> tieneIntegradoraMap = new HashMap<>();

//    @Getter @Setter private TareaIntegradora tareaIntegradora;
    @Getter @Setter private Map<DtoCargaAcademica, TareaIntegradora> tareaIntegradoraMap = new HashMap<>();

    @Getter @Setter private List<Indicador> indicadores;

    public CapturaTareaIntegradoraRolDocente(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.docenteLogueado = filtro.getEntity();
    }

    public void setDocenteLogueado(PersonalActivo docenteLogueado) {
        this.docenteLogueado = docenteLogueado;
    }

    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public void setPeriodosConCarga(List<PeriodosEscolares> periodosConCarga) {
        this.periodosConCarga = periodosConCarga;
        if(!periodosConCarga.isEmpty()){
            periodoSeleccionado = periodosConCarga.get(0);
        }
    }

    public void setCargaAcademicaSeleccionada(DtoCargaAcademica cargaAcademicaSeleccionada) {
        this.cargaAcademicaSeleccionada = cargaAcademicaSeleccionada;
    }

    public void setCargasDocente(List<DtoCargaAcademica> cargasDocente) {
        this.cargasDocente = cargasDocente;
        if(!cargasDocente.isEmpty()){
            cargaAcademicaSeleccionada = cargasDocente.get(0);
        }
    }

    /*public void setDtoUnidadConfiguraciones(List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
    }

    public void setDtoUnidadesCalificacion(DtoUnidadesCalificacion dtoUnidadesCalificacion) {
        this.dtoUnidadesCalificacion = dtoUnidadesCalificacion;
    }*/
}
