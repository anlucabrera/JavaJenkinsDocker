package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;

public class ValidaComentariosRolTutor extends AbstractRol {
    @Getter private PersonalActivo tutorLogueado;

    @Getter private PeriodosEscolares periodoSeleccionado;
    @Getter private List<PeriodosEscolares> periodosConCarga;

    @Getter private DtoCargaAcademica cargaAcademicaSeleccionada;
    @Getter private List<DtoCargaAcademica> cargasDocente;

    @Getter private DtoUnidadConfiguracion dtoUnidadConfiguracionSeleccionada;
    @Getter private List<DtoUnidadConfiguracion> dtoUnidadConfiguraciones;

    @Getter private DtoGrupoEstudiante estudiantesPorGrupo;

    public ValidaComentariosRolTutor(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        tutorLogueado = filtro.getEntity();
    }
}
