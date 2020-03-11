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
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;

/**
 *
 * @author UTXJ
 */
public class RegistroTutoriaGrupalRolTutor extends AbstractRol{
    private static final long serialVersionUID = -8654749366730093073L;
    @Getter     private     PersonalActivo                          docenteLogueado;
    
    @Getter     private     Integer                                 periodoActivo;
    @Getter     private     PeriodosEscolares                       periodoSeleccionado;
    @Getter     private     List<PeriodosEscolares>                 periodosConCargaGrupo;
    
    @Getter     private     DtoListadoTutores                       grupoTutorSeleccionado;
    @Getter     private     List<DtoListadoTutores>                 listadoGruposTutor;
    
    @Getter     private     SesionesGrupalesTutorias                sesionGrupalSeleccionada;
    @Getter     private     List<SesionesGrupalesTutorias>          listaSesionesGrupalesTutorias;
    
    @Getter     private     EventosRegistros                        eventoRegistroActivo, eventoSeleccionado;
    @Getter     private     List<EventosRegistros>                  eventosPorPeriodo;

    @Getter     private     TutoriasGrupales                        tutoriaGrupal;
    @Getter     private     DtoTutoriaGrupalCE                      tutoriaGrupalSeleccionada;
    @Getter     private     List<DtoTutoriaGrupalCE>                listaDtoTuriasGrupales;
    
    @Getter     private     List<DtoParticipantesTutoriaGrupalCE>   listaEstudiantes;
    @Getter     private     List<DtoParticipantesTutoriaGrupalCE>   listaEstudiantesFiltro;
    
    @Getter     private     String                                  pistaEstudiante;
    @Getter     private     DtoEstudianteComplete                   estudianteJefeGrupoSeleccionado;
    
    @Getter     @Setter     private                                 List<DtoCasoCritico>                        listaCasosCriticos;
    @Getter     @Setter     private                                 DtoCasoCritico                              dtoCasoCritico;
    @Getter     @Setter     private                                 List<String>                                listaSecundaria;
    @Getter     @Setter     private                                 DtoEstudiante                               dtoEstudiante;
    @Getter     @Setter     private                                 Estudiante                                  estudiante;
    
    @Getter     @Setter     private                                 Date                                        fecha;
    @Getter     @Setter     private                                 Date                                        horaInicio;
    @Getter     @Setter     private                                 Date                                        horaCierre;
    @Getter     @Setter     private                                 String                                      formatoFechaAsesoria;
    
    public RegistroTutoriaGrupalRolTutor(@NonNull Filter<PersonalActivo> filtro) {
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

    public void setTutoriaGrupal(TutoriasGrupales tutoriaGrupal) {
        this.tutoriaGrupal = tutoriaGrupal;
    }

    public void setTutoriaGrupalSeleccionada(DtoTutoriaGrupalCE tutoriaGrupalSeleccionada) {
        this.tutoriaGrupalSeleccionada = tutoriaGrupalSeleccionada;
        if(this.tutoriaGrupalSeleccionada != null){
            this.setFormatoFechaAsesoria(Caster.convertirFormatoFecha(this.tutoriaGrupalSeleccionada.getTutoriaGrupal().getFecha()));
        }else{
            this.setFormatoFechaAsesoria("");
        }
    }
    
    public void setListaDtoTuriasGrupales(List<DtoTutoriaGrupalCE> listaDtoTuriasGrupales) {
        this.listaDtoTuriasGrupales = listaDtoTuriasGrupales;
    }

    public void setEstudianteJefeGrupoSeleccionado(DtoEstudianteComplete estudianteJefeGrupoSeleccionado) {
        this.estudianteJefeGrupoSeleccionado = estudianteJefeGrupoSeleccionado;
    }

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setListaEstudiantes(List<DtoParticipantesTutoriaGrupalCE> listaEstudiantes) {
        this.listaEstudiantes = listaEstudiantes;
    }

    public void setListaEstudiantesFiltro(List<DtoParticipantesTutoriaGrupalCE> listaEstudiantesFiltro) {
        this.listaEstudiantesFiltro = listaEstudiantesFiltro;
    }
    
}
