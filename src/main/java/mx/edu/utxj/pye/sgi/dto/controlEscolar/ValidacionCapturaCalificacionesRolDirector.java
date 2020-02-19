package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;

public class ValidacionCapturaCalificacionesRolDirector extends AbstractRol {
    /**
     * Representa la referencia hacia el personal director
     */
    @Getter @NonNull private PersonalActivo director;

    /**
     * Programa educativo seleccionado para validar
     */
    @Getter @NonNull private AreasUniversidad programa;

    /**
     * Periodos escolares para seleccionar
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;

    /**
     * Lista de programas educativos vigentes y ordenados por nombre que dependen del area con POA del director identificado
     */
    @Getter @NonNull private List<AreasUniversidad> programas;

    @Getter private Integer periodoActivo;
    @Getter private PeriodosEscolares periodoSeleccionado;
    @Getter private List<PeriodosEscolares> periodosConCarga;

    @Getter private DtoCargaAcademica cargaAcademicaSeleccionada;
    @Getter private List<DtoCargaAcademica> cargasDocente;

    @Getter private DtoUnidadConfiguracion dtoUnidadConfiguracionSeleccionada;
    @Getter private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;

    @Getter private DtoGrupoEstudiante estudiantesPorGrupo;

    public ValidacionCapturaCalificacionesRolDirector(@NonNull Filter<PersonalActivo> filtro, PersonalActivo director, AreasUniversidad programa) {
        super(filtro);
        this.director = director;
        this.programa = programa;
    }

    public void setDirector(PersonalActivo director) {
        this.director = director;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setProgramas(List<AreasUniversidad> programas) {
        this.programas = programas;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
    }

    public void setPeriodosConCarga(List<PeriodosEscolares> periodosConCarga) {
        this.periodosConCarga = periodosConCarga;
    }

    public void setCargaAcademicaSeleccionada(DtoCargaAcademica cargaAcademicaSeleccionada) {
        this.cargaAcademicaSeleccionada = cargaAcademicaSeleccionada;
    }

    public void setCargasDocente(List<DtoCargaAcademica> cargasDocente) {
        this.cargasDocente = cargasDocente;
    }

    public void setDtoUnidadConfiguracionSeleccionada(DtoUnidadConfiguracion dtoUnidadConfiguracionSeleccionada) {
        this.dtoUnidadConfiguracionSeleccionada = dtoUnidadConfiguracionSeleccionada;
    }

    public void setDtoUnidadConfiguraciones(List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones) {
        this.dtoUnidadConfiguraciones = dtoUnidadConfiguraciones;
    }

    public void setEstudiantesPorGrupo(DtoGrupoEstudiante estudiantesPorGrupo) {
        this.estudiantesPorGrupo = estudiantesPorGrupo;
    }
}
