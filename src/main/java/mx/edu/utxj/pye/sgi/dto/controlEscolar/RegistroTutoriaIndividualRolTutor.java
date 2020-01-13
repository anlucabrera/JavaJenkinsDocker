/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasIndividuales;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
public class RegistroTutoriaIndividualRolTutor extends AbstractRol{
    @Getter     private     PersonalActivo                          docenteLogueado;
    
    @Getter     private     Integer                                 periodoActivo;
    @Getter     private     PeriodosEscolares                       periodoSeleccionado;
    @Getter     private     List<PeriodosEscolares>                 periodosConCargaGrupo;
    
    @Getter     private     DtoListadoTutores                       grupoTutorSeleccionado;
    @Getter     private     List<DtoListadoTutores>                 listadoGruposTutor;
    
    @Getter     private     SesionesGrupalesTutorias                sesionGrupalSeleccionada;
    @Getter     private     List<SesionesGrupalesTutorias>          listaSesionesGrupalesTutorias;
    
    @Getter     private     DtoTutoriaIndividualCE                  dtoTutoriaIndividualCE;
    @Getter     @Setter     private                                 DtoTutoriaIndividualCE                        tutoriaIndividualSeleccionada;
    @Getter     private     List<DtoTutoriaIndividualCE>            listaDtoTutoriasIndividuales;
    
    @Getter     private     EventosRegistros                        eventoRegistroActivo, eventoSeleccionado;
    @Getter     private     List<EventosRegistros>                  eventosPorPeriodo;
    
    @Getter     private     String                                  pistaEstudiante;
    @Getter     private     DtoEstudianteComplete                   estudianteSeguimientoSeleccionado;
    @Getter     private     Estudiante                              estudianteSeleccionado;
    
    @Getter     @Setter     String                                  mensajeVerificacionAsesorias;
    @Getter     @Setter     Boolean                                 verificacionAsesorias;
    
    @Getter     @Setter     private                                 DtoCasoCritico                              dtoCasoCritico;
    @Getter     private     List<DtoCasoCritico>                    listaCasosCriticos;
    
    @Getter     @Setter     private                                 Date                                        fecha;
    @Getter     @Setter     private                                 Date                                        horaInicio;
    
    @Getter     @Setter     private                                 String                                      fechaCasoCritico;

    @Getter     @NonNull    private                                 String                                      pistaDocente;
    @Getter     @NonNull    private                                 PersonalActivo                              docente;
    @Getter     @NonNull    private                                 List<PersonalActivo>                        docentes;
    
    @Getter     private     Part                                    archivo;
    
    @Getter     private     List<DtoCasosCriticosPendientes>        listaCasosCriticosPendientes;
    
    @Getter     private     List<DtoCasoCritico>                    listaCasosCriticosPorGrupo;
    @Getter     @Setter     private                                 List<String>                                listaSecundaria;     
    
    public RegistroTutoriaIndividualRolTutor(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
        this.docenteLogueado = filtro.getEntity();
    }
    
    public void setDocenteLogueado(PersonalActivo docenteLogueado) {
        this.docenteLogueado = docenteLogueado;
    }

    public void setPeriodoSeleccionado(PeriodosEscolares periodoSeleccionado) {
        this.periodoSeleccionado = periodoSeleccionado;
        this.setListadoGruposTutor(Collections.EMPTY_LIST);
        if (this.periodoSeleccionado == null) {
            setEventosPorPeriodo(Collections.EMPTY_LIST);
        }
    }

    public void setPeriodosConCargaGrupo(List<PeriodosEscolares> periodosConCargaGrupo) {
        this.periodosConCargaGrupo = periodosConCargaGrupo;
        if (!periodosConCargaGrupo.isEmpty()) {
            periodoSeleccionado = periodosConCargaGrupo.get(0);
        }
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setGrupoTutorSeleccionado(DtoListadoTutores grupoTutorSeleccionado) {
        this.grupoTutorSeleccionado = grupoTutorSeleccionado;
        this.setListaSesionesGrupalesTutorias(Collections.EMPTY_LIST);
    }

    public void setListadoGruposTutor(List<DtoListadoTutores> listadoGruposTutor) {
        this.listadoGruposTutor = listadoGruposTutor;
        if (!listadoGruposTutor.isEmpty()) {
            grupoTutorSeleccionado = listadoGruposTutor.get(0);
        } else {
            grupoTutorSeleccionado = null;
        }
    }

    public void setSesionGrupalSeleccionada(SesionesGrupalesTutorias sesionGrupalSeleccionada) {
        this.sesionGrupalSeleccionada = sesionGrupalSeleccionada;
    }

    public void setListaSesionesGrupalesTutorias(List<SesionesGrupalesTutorias> listaSesionesGrupalesTutorias) {
        this.listaSesionesGrupalesTutorias = listaSesionesGrupalesTutorias;
        if(!listaSesionesGrupalesTutorias.isEmpty()){
            sesionGrupalSeleccionada = listaSesionesGrupalesTutorias.get(0);
        }else{
            sesionGrupalSeleccionada = null;
        }
    }
    
    public void setEventoRegistroActivo(EventosRegistros eventoRegistroActivo) {
        this.eventoRegistroActivo = eventoRegistroActivo;
    }

    public void setEventoSeleccionado(EventosRegistros eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }

    public void setEventosPorPeriodo(List<EventosRegistros> eventosPorPeriodo) {
        this.eventosPorPeriodo = eventosPorPeriodo;
        if (eventosPorPeriodo != null && !eventosPorPeriodo.isEmpty()) {//si la lista de eventos existe
            setEventoSeleccionado(eventosPorPeriodo.get(0));//se selecciona el primer evento de la lista
            if (getEventosPorPeriodo().contains(getEventoRegistroActivo())) {//si la lista de eventos contiene al evento actual se declara a este como seleccionado
                setEventoSeleccionado(getEventoRegistroActivo());
            }
        }
    }

    public void setDtoTutoriaIndividualCE(DtoTutoriaIndividualCE dtoTutoriaIndividualCE) {
        this.dtoTutoriaIndividualCE = dtoTutoriaIndividualCE;
    }

    public void setListaDtoTutoriasIndividuales(List<DtoTutoriaIndividualCE> listaDtoTutoriasIndividuales) {
        this.listaDtoTutoriasIndividuales = listaDtoTutoriasIndividuales;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeguimientoSeleccionado(DtoEstudianteComplete estudianteSeguimientoSeleccionado) {
        this.estudianteSeguimientoSeleccionado = estudianteSeguimientoSeleccionado;
    }

    public void setListaCasosCriticos(List<DtoCasoCritico> listaCasosCriticos) {
        this.listaCasosCriticos = listaCasosCriticos;
        if (listaCasosCriticos != null && !listaCasosCriticos.isEmpty()) {//si la lista de eventos existe
            setDtoCasoCritico(null);//se selecciona el primer evento de la lista
        }
    }

    public void setEstudianteSeleccionado(Estudiante estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setArchivo(Part archivo) {
        this.archivo = archivo;
    }

    public void setPistaDocente(String pistaDocente) {
        this.pistaDocente = pistaDocente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setDocentes(List<PersonalActivo> docentes) {
        this.docentes = docentes;
    }

    public void setListaCasosCriticosPendientes(List<DtoCasosCriticosPendientes> listaCasosCriticosPendientes) {
        this.listaCasosCriticosPendientes = listaCasosCriticosPendientes;
    }

    public void setListaCasosCriticosPorGrupo(List<DtoCasoCritico> listaCasosCriticosPorGrupo) {
        this.listaCasosCriticosPorGrupo = listaCasosCriticosPorGrupo;
    }
    
}
