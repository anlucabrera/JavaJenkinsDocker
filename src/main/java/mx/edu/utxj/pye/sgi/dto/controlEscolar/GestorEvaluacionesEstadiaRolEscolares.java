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
     * Indica si se deshabilita o habilita el botón de registro
     */
    @Getter @NonNull private Boolean deshabilitarRegistro;
    
    /**
     * Evaluaciones por evento de estadía
     */
    @Getter @NonNull private List<DtoEvaluacionEventoEstadia> evaluacionesEvento;
    
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

    public void setDeshabilitarRegistro(Boolean deshabilitarRegistro) {
        this.deshabilitarRegistro = deshabilitarRegistro;
    }

    public void setEvaluacionEstadia(EvaluacionEstadia evaluacionEstadia) {
        this.evaluacionEstadia = evaluacionEstadia;
    }
    public void setEvaluacionesEvento(List<DtoEvaluacionEventoEstadia> evaluacionesEvento) {
        this.evaluacionesEvento = evaluacionesEvento;
    }

}

