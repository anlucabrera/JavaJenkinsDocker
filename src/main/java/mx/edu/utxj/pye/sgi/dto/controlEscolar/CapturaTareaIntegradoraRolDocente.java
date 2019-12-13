package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;

public class CapturaTareaIntegradoraRolDocente  extends AbstractRol {
    @Getter private PersonalActivo docenteLogueado;

    @Getter private EventoEscolar eventoActivo;
    @Getter private Integer periodoActivo;
    @Getter private PeriodosEscolares periodoSeleccionado;
    @Getter private List<PeriodosEscolares> periodosConCarga;

    @Getter private DtoCargaAcademica cargaAcademicaSeleccionada;
    @Getter private List<DtoCargaAcademica> cargasDocente;

    @Getter private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;
    @Getter private DtoUnidadesCalificacion dtoUnidadesCalificacion;
    @Getter @Setter private Boolean tieneIntegradora = false;
    @Getter @Setter private TareaIntegradora tareaIntegradora;

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

    public void setDtoUnidadConfiguraciones(List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
    }

    public void setDtoUnidadesCalificacion(DtoUnidadesCalificacion dtoUnidadesCalificacion) {
        this.dtoUnidadesCalificacion = dtoUnidadesCalificacion;
    }
}
