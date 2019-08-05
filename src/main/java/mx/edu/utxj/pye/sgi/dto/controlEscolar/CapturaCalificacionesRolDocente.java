package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.Collections;
import java.util.List;

public class CapturaCalificacionesRolDocente extends AbstractRol {
    @Getter private PersonalActivo docenteLogueado;

    @Getter private EventoEscolar eventoActivo;
    @Getter private Integer periodoActivo;
    @Getter private PeriodosEscolares periodoSeleccionado;
    @Getter private List<PeriodosEscolares> periodosConCarga;

    @Getter private DtoCargaAcademica cargaAcademicaSeleccionada;
    @Getter private List<DtoCargaAcademica> cargasDocente;

    @Getter private DtoUnidadConfiguracion dtoUnidadConfiguracionSeleccionada;
    @Getter private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;

    @Getter private DtoGrupoEstudiante estudiantesPorGrupo;

    @Getter private DtoCapturaCalificacion capturaEstudianteSeleccionado;

    public CapturaCalificacionesRolDocente(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.docenteLogueado = filtro.getEntity();
    }

    public void setDocenteLogueado(PersonalActivo docenteLogueado) {
        this.docenteLogueado = docenteLogueado;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setCargasDocente(Collections.EMPTY_LIST);
//        this.setCargaAcademicaSeleccionada(null);
//        this.setDtoUnidadConfiguracionSeleccionada(null);
    }

    public void setPeriodosConCarga(List<PeriodosEscolares> periodosConCarga) {
        this.periodosConCarga = periodosConCarga;
        if(!periodosConCarga.isEmpty()){
            periodoSeleccionado = periodosConCarga.get(0);
        }
    }

    public void setCargaAcademicaSeleccionada(DtoCargaAcademica cargaAcademicaSeleccionada) {
//        System.out.println("cargaAcademicaSeleccionada = [" + cargaAcademicaSeleccionada + "]");
        this.cargaAcademicaSeleccionada = cargaAcademicaSeleccionada;
        this.setDtoUnidadConfiguraciones(Collections.emptyList());
//        this.setDtoUnidadConfiguracionSeleccionada(null);
    }

    public void setCargasDocente(List<DtoCargaAcademica> cargasDocente) {
        this.cargasDocente = cargasDocente;
        if(!cargasDocente.isEmpty()) cargaAcademicaSeleccionada = cargasDocente.get(0);
        else cargaAcademicaSeleccionada = null;

    }

    public void setDtoUnidadConfiguracionSeleccionada(DtoUnidadConfiguracion dtoUnidadConfiguracionSeleccionada) {
        this.dtoUnidadConfiguracionSeleccionada = dtoUnidadConfiguracionSeleccionada;
    }

    public void setDtoUnidadConfiguraciones(List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
        if(!dtoUnidadConfiguraciones.isEmpty()) dtoUnidadConfiguracionSeleccionada = dtoUnidadConfiguraciones.get(0);
        else dtoUnidadConfiguracionSeleccionada = null;
    }

    public void setEstudiantesPorGrupo(DtoGrupoEstudiante estudiantesPorGrupo) {
        this.estudiantesPorGrupo = estudiantesPorGrupo;
    }

    public void setCapturaEstudianteSeleccionado(DtoCapturaCalificacion capturaEstudianteSeleccionado) {
        this.capturaEstudianteSeleccionado = capturaEstudianteSeleccionado;
    }

    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
}
