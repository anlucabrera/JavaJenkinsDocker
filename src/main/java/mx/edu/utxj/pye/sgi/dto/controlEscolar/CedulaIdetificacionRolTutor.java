package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;

public class CedulaIdetificacionRolTutor extends AbstractRol {

    @Getter @NonNull private PersonalActivo tutor;
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    @Getter @NonNull private PeriodosEscolares periodo;
    @Getter @NonNull private Integer periodoActivo;

    @Getter    @Setter private List<Listaalumnosca> listaEstudiantes;
    @Getter    @Setter    private List<Estudiante> estudiantes;
    @Getter    @Setter    private Grupo grupoSelec;
    @Getter    @Setter    private List<Grupo> grupos;
    //TODO: Apartados de la Cedula de Identificación
    @Getter @Setter List<Apartado> apartados;
    @Getter @Setter DtoCedulaIdentificacion cedulaIdentificacion;
    //TODO: Estudiante seleccionado
    @Getter @Setter Estudiante estudiante;

    @Getter @Setter String matricula;

    public CedulaIdetificacionRolTutor(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        tutor = filtro.getEntity();
    }

    public void setTutor(PersonalActivo tutor) {
        this.tutor = tutor;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setListaEstudiantes(List<Listaalumnosca> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void setGrupoSelec(Grupo grupoSelec) {
        this.grupoSelec = grupoSelec;
    }
}
