/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;

import java.util.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadiaDescripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;

/**
 *
 * @author UTXJ
 */
public class GestorEvaluacionesEstadiaRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo usuario;
    
    /**
     * Lista generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Generacion seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
    /**
     * Lista de niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> nivelesEducativos;
    
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivelEducativo;
    
    /**
     * Lista de evaluaciones de estadía registradas
     */
    @Getter @NonNull private List<EvaluacionEstadiaDescripcion> evaluaciones;
    
    /**
     * Evaluación de estadía seleccionada
     */
    @Getter @NonNull private EvaluacionEstadiaDescripcion evaluacion;
    
    /**
     * Evento escolar de la evaluación
     */
    @Getter @NonNull private EventoEstadia eventoEstadia;
    
    /**
     * Evento escolar de la evaluación
     */
    @Getter @NonNull private EvaluacionEstadia  evaluacionEstadia;
  
    /**
     * Evaluaciones por evento de estadía
     */
    @Getter @NonNull private List<DtoEvaluacionEventoEstadia> evaluacionesEvento;
    
    /**
     * Lista de calificaciones registradas para la evaluación seleccionada
     */
    @Getter @NonNull private List<CalificacionCriterioEstadia> listaCalificacionesEvaluacion;
    
    
     /**
     * Número de pestaña activa
     */
    @Getter @NonNull private Integer  pestaniaActiva;
    
     /**
     * Número de evaluación que se registrará
     */
    @Getter @NonNull private Integer  numeroEvaluacion;
    
     /**
     * Descripción de la evaluación que se registrará
     */
    @Getter @NonNull private String  descripcionEvaluacion;
    
     /**
     * Anio de inicio de la evaluación que se registrará
     */
    @Getter @NonNull private Integer  anioInicioEvaluacion;
    
     /**
     * Representa si se registró o no la evaluación
     */
    @Getter @NonNull private Boolean  registroEvaluacion;
    
    /**
     * Evaluación de estadía registrada
     */
    @Getter @NonNull private EvaluacionEstadiaDescripcion evaluacionRegistrada;
    
    /**
     * Lista de preguntas que se registrarán a la evaluación
     */
    @Getter @NonNull private List<String> preguntasRegistrarEvaluacion;
    
    
    /**
     * Lista de preguntas registradas de la evaluación
     */
    @Getter @NonNull private List<CriterioEvaluacionEstadia> preguntasRegistradas;
    
    public GestorEvaluacionesEstadiaRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNivelesEducativos(List<ProgramasEducativosNiveles> nivelesEducativos) {
        this.nivelesEducativos = nivelesEducativos;
    }

    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setEvaluaciones(List<EvaluacionEstadiaDescripcion> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }

    public void setEvaluacion(EvaluacionEstadiaDescripcion evaluacion) {
        this.evaluacion = evaluacion;
    }

    public void setEventoEstadia(EventoEstadia eventoEstadia) {
        this.eventoEstadia = eventoEstadia;
    }

    public void setEvaluacionEstadia(EvaluacionEstadia evaluacionEstadia) {
        this.evaluacionEstadia = evaluacionEstadia;
    }
    public void setEvaluacionesEvento(List<DtoEvaluacionEventoEstadia> evaluacionesEvento) {
        this.evaluacionesEvento = evaluacionesEvento;
    }

    public void setListaCalificacionesEvaluacion(List<CalificacionCriterioEstadia> listaCalificacionesEvaluacion) {
        this.listaCalificacionesEvaluacion = listaCalificacionesEvaluacion;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
    
    public void setNumeroEvaluacion(Integer numeroEvaluacion) {
        this.numeroEvaluacion = numeroEvaluacion;
    }

    public void setDescripcionEvaluacion(String descripcionEvaluacion) {
        this.descripcionEvaluacion = descripcionEvaluacion;
    }

    public void setAnioInicioEvaluacion(Integer anioInicioEvaluacion) {
        this.anioInicioEvaluacion = anioInicioEvaluacion;
    }

    public void setRegistroEvaluacion(Boolean registroEvaluacion) {
        this.registroEvaluacion = registroEvaluacion;
    }

    public void setEvaluacionRegistrada(EvaluacionEstadiaDescripcion evaluacionRegistrada) {
        this.evaluacionRegistrada = evaluacionRegistrada;
    }

    public void setPreguntasRegistrarEvaluacion(List<String> preguntasRegistrarEvaluacion) {
        this.preguntasRegistrarEvaluacion = preguntasRegistrarEvaluacion;
    }

    public void setPreguntasRegistradas(List<CriterioEvaluacionEstadia> preguntasRegistradas) {
        this.preguntasRegistradas = preguntasRegistradas;
    }
    
}

