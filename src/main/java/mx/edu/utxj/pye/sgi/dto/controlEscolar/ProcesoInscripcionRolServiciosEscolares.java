package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;
import java.util.Objects;

public class ProcesoInscripcionRolServiciosEscolares extends AbstractRol {

    @Getter @NonNull private PersonalActivo serviciosEscolares;

    /**
     * TODO:Representa la referencia del evento de inscripciones
     */
    @Getter @NonNull private EventoEscolar eventoIncripcion;
    @Getter @NonNull private EventoEscolar eventoRegistroFichas;
    @Getter @NonNull private PeriodosEscolares periodo;
    @Getter @NonNull private Integer periodoActivo, periodoAct;
    @Getter @NonNull private AreasUniversidad programa;

    @Getter @NonNull private List<TipoSangre> tipoSangreList; //Lista de tipo de sangre
    @Getter @NonNull private  List<DtoGrupo> posiblesGrupos;// Grupos posibles
    @Getter @NonNull private DtoGrupo grupoSeleccionado; //Grupo seleccionado
    @Getter @NonNull private  AreasUniversidad pePo,peSo; //Programas educativos del aspirante validado
    @Getter @NonNull private Boolean cartaCom=true;

    public ProcesoInscripcionRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad programa) {
        super(filtro);
        this.serviciosEscolares = serviciosEscolares;
        this.programa = programa;
    }
    public void setServiciosEscolares(PersonalActivo serviciosEscolares) {
        this.serviciosEscolares = serviciosEscolares;
    }

    public void setEventoIncripcion(EventoEscolar eventoIncripcion) {
        this.eventoIncripcion = eventoIncripcion;
    }

    public void setEventoRegistroFichas(EventoEscolar eventoRegistroFichas) {
        this.eventoRegistroFichas = eventoRegistroFichas;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
        if(periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPeriodoAct(Integer periodoAct) {
        this.periodoAct = periodoAct;
    }

    public void setTipoSangreList(List<TipoSangre> tipoSangreList) {
        this.tipoSangreList = tipoSangreList;
    }
    public void setGrupoSeleccionado(DtoGrupo grupoSeleccionado) {
        this.grupoSeleccionado = grupoSeleccionado;
    }

    public void setPePo(AreasUniversidad pePo) {
        this.pePo = pePo;
    }

    public void setPeSo(AreasUniversidad peSo) {
        this.peSo = peSo;
    }

    public void setPosiblesGrupos(List<DtoGrupo> posiblesGrupos) {
        this.posiblesGrupos = posiblesGrupos;
    }

    public void setCartaCom(Boolean cartaCom) {
        this.cartaCom = cartaCom;
    }
}
