package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.Date;
import java.util.List;

public class InscripcionIngenieriaRolServiciosEscolares extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personalActivoSe;
    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad areaSe;

    @Getter @NonNull private EventoEscolar eventoEscolar; //Evento de inscripcion
    @Getter @NonNull ProcesosInscripcion procesosInscripcion; //Proceso de inscripción Ingeniería
    @Getter @NonNull private TramitesEscolares tramitesEscolares ;// Tramite de inscripcion para el registro de citas

    @Getter @NonNull private String tipoAspiranteSelect, tipoEstudiante; //Tipo aspirante seleccionado
    @Getter @NonNull private List<TipoSangre> tipoSangreList;
    @Getter @NonNull private  List<Sistema> sistemas;
    @Getter @NonNull private Sistema sistemaSeleccionado;
    @Getter @NonNull private Date fechaSeleccionada;
    @Getter @NonNull private Login login;

    @Getter @NonNull private List<DtoAspiranteIng> aspiranteIng, estudiantesIncritos, vacia;
    @Getter @NonNull private DtoAspiranteIng aspiranteSelect, estudianteInscrito; // Aspirante seleccionado

    @Getter @NonNull private  List<DtoGrupo> grupos;
    @Getter @NonNull private  List<DocumentoProceso> documentos, documentosSelect;
    @Getter @NonNull private  List<DocumentoEstudianteProceso> documentosSelectIns;

    public InscripcionIngenieriaRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad area) {
        super(filtro);
        this.personalActivoSe = serviciosEscolares;
        this.areaSe = area;
    }

    public void setPersonalActivoSe(PersonalActivo personalActivoSe) { this.personalActivoSe = personalActivoSe; }
    public void setEventoEscolar(EventoEscolar eventoEscolar) { this.eventoEscolar = eventoEscolar; }
    public void setProcesosInscripcion(ProcesosInscripcion procesosInscripcion) { this.procesosInscripcion = procesosInscripcion; }

    public void setAspiranteIng(List<DtoAspiranteIng> aspiranteIng) { this.aspiranteIng = aspiranteIng; }

    public void setTramitesEscolares(TramitesEscolares tramitesEscolares) { this.tramitesEscolares = tramitesEscolares; }

    public void setAspiranteSelect(DtoAspiranteIng aspiranteSelect) { this.aspiranteSelect = aspiranteSelect; }

    public void setTipoAspiranteSelect(String tipoAspiranteSelect) { this.tipoAspiranteSelect = tipoAspiranteSelect; }

    public void setGrupos(List<DtoGrupo> grupos) { this.grupos = grupos; }

    public void setTipoSangreList(List<TipoSangre> tipoSangreList) { this.tipoSangreList = tipoSangreList; }

    public void setTipoEstudiante(String tipoEstudiante) { this.tipoEstudiante = tipoEstudiante; }

    public void setDocumentos(List<DocumentoProceso> documentos) { this.documentos = documentos; }

    public void setSistemas(List<Sistema> sistemas) { this.sistemas = sistemas; }

    public void setSistemaSeleccionado(Sistema sistemaSeleccionado) { this.sistemaSeleccionado = sistemaSeleccionado; }

    public void setFechaSeleccionada(Date fechaSeleccionada) { this.fechaSeleccionada = fechaSeleccionada; }

    public void setDocumentosSelect(List<DocumentoProceso> documentosSelect) { this.documentosSelect = documentosSelect; }

    public void setLogin(Login login) { this.login = login; }

    public void setDocumentosSelectIns(List<DocumentoEstudianteProceso> documentosSelectIns) { this.documentosSelectIns = documentosSelectIns; }

    public void setEstudiantesIncritos(List<DtoAspiranteIng> estudiantesIncritos) { this.estudiantesIncritos = estudiantesIncritos; }

    public void setVacia(List<DtoAspiranteIng> vacia) { this.vacia = vacia; }

    public void setEstudianteInscrito(DtoAspiranteIng estudianteInscrito) { this.estudianteInscrito = estudianteInscrito; }
}
