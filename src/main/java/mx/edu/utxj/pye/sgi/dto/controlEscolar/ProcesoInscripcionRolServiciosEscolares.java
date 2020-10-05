package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;
import java.util.Objects;

public class ProcesoInscripcionRolServiciosEscolares extends AbstractRol {

    @Getter
    @NonNull
    private PersonalActivo serviciosEscolares;

    /**
     * TODO:Representa la referencia del evento de inscripciones
     */
    @Getter @NonNull private EventoEscolar eventoIncripcion;
    @Getter @NonNull private EventoEscolar eventoRegistroFichas;
    @Getter @NonNull private PeriodosEscolares periodo;
    @Getter @NonNull private Integer periodoActivo, periodoAct;
    @Getter @NonNull private AreasUniversidad programa;
    @Getter @NonNull private TramitesEscolares tramiteCita;

    @Getter @NonNull private List<TipoSangre> tipoSangreList; //Lista de tipo de sangre
    @Getter @NonNull private List<DtoGrupo> posiblesGrupos;// Grupos posibles
    @Getter @NonNull private DtoGrupo grupoSeleccionado; //Grupo seleccionado
    @Getter @NonNull private AreasUniversidad pePo, peSo; //Programas educativos del aspirante validado
    @Getter @NonNull private Boolean cartaCom = true;

    //////////// Para modificacion de estudiante inscrito
    @Getter @NonNull private Estudiante estudianteSeleccionado; //Representa a un estudiante seleccionado en la tabla de estudiantes inscritos
    @Getter @NonNull private List<AreasUniversidad> areasAcademicasE, peE;
    @Getter @NonNull private List<DtoGrupo> gruposPosiblesE;
    @Getter @NonNull private DtoGrupo grupoSelecEs;
    @Getter @NonNull private AreasUniversidad areaAEstudiante, peEstudiante;
    @Getter @NonNull private Sistema sistemaSeleccionado;
    @Getter @NonNull private List<Sistema> sistemas;
    @Getter @NonNull private CitasAspirantes cita;
    // Modificación de aspirante seleccionado
    @Getter @NonNull private Aspirante aspiranteSeleccionado; //Representa a un aspirante seleccionado de la lista de aspirantes registrados
    @Getter @NonNull private AreasUniversidad areaPoA, areaSoA, pePoA,peSoA;
    @Getter @NonNull private  List<AreasUniversidad> areasPoA, areasSoA, pesPoA,pesSoA;
    @Getter @NonNull private  Sistema sPoA, sSoA;
    @Getter @NonNull private  List<Sistema> sistemasAspirante;
    @Getter @NonNull private DatosAcademicos datosAcademicosAspiranteSelect;

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
        if (periodoActivo != null) soloLectura = !Objects.equals(periodo.getPeriodo(), periodoActivo);
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

    public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setAreasAcademicasE(List<AreasUniversidad> areasAcademicasE) {
        this.areasAcademicasE = areasAcademicasE;
    }

    public void setPeE(List<AreasUniversidad> peE) {
        this.peE = peE;
    }

    public void setGruposPosiblesE(List<DtoGrupo> gruposPosiblesE) {
        this.gruposPosiblesE = gruposPosiblesE;
    }

    public void setGrupoSelecEs(DtoGrupo grupoSelecEs) {
        this.grupoSelecEs = grupoSelecEs;
    }

    public void setAreaAEstudiante(AreasUniversidad areaAEstudiante) {
        this.areaAEstudiante = areaAEstudiante;
    }

    public void setPeEstudiante(AreasUniversidad peEstudiante) {
        this.peEstudiante = peEstudiante;
    }

    public void setSistemaSeleccionado(Sistema sistemaSeleccionado) {
        this.sistemaSeleccionado = sistemaSeleccionado;
    }

    public void setSistemas(List<Sistema> sistemas) {
        this.sistemas = sistemas;
    }

    public void setTramiteCita(TramitesEscolares tramiteCita) { this.tramiteCita = tramiteCita; }

    public void setCita(CitasAspirantes cita) { this.cita = cita; }

    public void setAspiranteSeleccionado(Aspirante aspiranteSeleccionado) { this.aspiranteSeleccionado = aspiranteSeleccionado; }

    public void setAreaPoA(AreasUniversidad areaPoA) { this.areaPoA = areaPoA; }

    public void setAreaSoA(AreasUniversidad areaSoA) { this.areaSoA = areaSoA; }

    public void setPePoA(AreasUniversidad pePoA) { this.pePoA = pePoA; }

    public void setPeSoA(AreasUniversidad peSoA) {
        this.peSoA = peSoA;
    }

    public void setAreasPoA(List<AreasUniversidad> areasPoA) { this.areasPoA = areasPoA; }

    public void setAreasSoA(List<AreasUniversidad> areasSoA) { this.areasSoA = areasSoA; }

    public void setPesPoA(List<AreasUniversidad> pesPoA) { this.pesPoA = pesPoA; }

    public void setPesSoA(List<AreasUniversidad> pesSoA) { this.pesSoA = pesSoA; }

    public void setsPoA(Sistema sPoA) { this.sPoA = sPoA; }

    public void setsSoA(Sistema sSoA) { this.sSoA = sSoA; }

    public void setSistemasAspirante(List<Sistema> sistemasAspirante) { this.sistemasAspirante = sistemasAspirante; }

    public void setDatosAcademicosAspiranteSelect(DatosAcademicos datosAcademicosAspiranteSelect) { this.datosAcademicosAspiranteSelect = datosAcademicosAspiranteSelect; }

}
