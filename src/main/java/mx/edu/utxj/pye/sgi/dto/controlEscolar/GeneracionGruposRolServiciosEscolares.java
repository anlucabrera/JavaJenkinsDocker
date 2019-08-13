package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Turno;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import javax.faces.model.SelectItem;
import java.util.List;
import java.util.Objects;

public class GeneracionGruposRolServiciosEscolares extends AbstractRol{
    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo serviciosEscolares;

    /**
     * Representa la referencia al evento activo de reincorporación
     */
    @Getter @NonNull private EventoEscolar eventoActivo;

    /**
     * Periodo escolar en el que se hara la reincorporación
     */
    @Getter @NonNull private PeriodosEscolares periodo;

    /**
     * Representa la clave del periodo activo
     */
    @Getter @NonNull private Integer periodoActivo, periodoAct, noGrupos;

    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;

    @Getter @NonNull private Grupo grupo;

    @Getter @NonNull private PlanEstudio planEstudio;

    @Getter @NonNull private List<Grupo> grupos;

    @Getter @NonNull private List<Turno> turnos;

    @Getter @NonNull private List<PlanEstudio> planEstudios;

    @Getter @NonNull private List<SelectItem> periodos;

    @Getter @Setter private List<AreasUniversidad> areasUniversidades;

    @Getter @Setter private Generaciones generaciones;

    @Getter @Setter private List<DtoConteoGrupos> listaSugerencia;

    @Getter @Setter private Boolean botonActivo;

    public GeneracionGruposRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad programa) {
        super(filtro);
        this.serviciosEscolares = serviciosEscolares;
        this.programa = programa;
    }

    public void setServiciosEscolares(PersonalActivo serviciosEscolares) {
        this.serviciosEscolares = serviciosEscolares;
    }

    public void setEventoActivo(EventoEscolar eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public void setPlanEstudio(PlanEstudio planEstudio) {
        this.planEstudio = planEstudio;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
    }

    public void setPlanEstudios(List<PlanEstudio> planEstudios) {
        this.planEstudios = planEstudios;
    }

    public void setPeriodos(List<SelectItem> periodos) {
        this.periodos = periodos;
    }

    public void setPeriodoAct(Integer periodoAct) {
        this.periodoAct = periodoAct;
    }

    public void setNoGrupos(Integer noGrupos) {
        this.noGrupos = noGrupos;
    }
}
