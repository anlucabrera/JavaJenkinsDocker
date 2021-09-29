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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
/**
 *
 * @author UTXJ
 */
public class AsignacionEstudiantesEstadiaRolDocente extends AbstractRol{
    /**
     * Representa la referencia hacia al asesor academico
     */
    @Getter @NonNull private PersonalActivo docente;
    
    /**
     * Área superior a la que pertenece el asesor académico
     */
    @Getter @NonNull private AreasUniversidad areaSuperior;
    
     /**
     * Claves de programas educativos que pertenecen al área académica del asesor académico
     */
    @Getter @NonNull private List<Short> programasEducativos;
    
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
     * Pista del estudiante
     */
    @Getter @NonNull private String pistaEstudiante;
   
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoEstudianteComplete estudianteSeleccionado;
    
    /**
     * Estudiante seleccionado
     */
    @Getter @NonNull private DtoDatosEstudiante estudianteRegistrado;
    
    /**
     * Roles asignados de la generacion y nivel seleccionados
     */
    @Getter @NonNull private List<DtoDatosEstudiante> estudiantesRegistrados;
    
    /**
     * Representa evento activo de estadia 
     */
    @Getter private EventoEstadia eventoActivo;
    
    /**
     * Representa la generación del evento activo
     */
    @Getter private Generaciones generacionEventoActivo;
    
    /**
     * Representa evento de estadia seleccionado
     */
    @Getter @NonNull private EventoEstadia eventoSeleccionado;
   
     /**
     * Asesor académico registrado
     */
    @Getter @NonNull private AsesorAcademicoEstadia asesorAcademicoEstadia;
    
     /**
     * Valor que determina si se habilita o no la asignación
     */
    @Getter @NonNull private Boolean deshabilitarAsignacion;
    
     /**
     * Valor que determina si el personal tiene rol de asesor para el evento activo
     */
    @Getter @NonNull private Boolean rolAsesorActivo;
    
    
    public AsignacionEstudiantesEstadiaRolDocente(Filter<PersonalActivo> filtro, PersonalActivo docente) {
        super(filtro);
        this.docente = docente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
    }

    public void setAreaSuperior(AreasUniversidad areaSuperior) {
        this.areaSuperior = areaSuperior;
    }

    public void setProgramasEducativos(List<Short> programasEducativos) {
        this.programasEducativos = programasEducativos;
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

    public void setPistaEstudiante(String pistaEstudiante) {
        this.pistaEstudiante = pistaEstudiante;
    }

    public void setEstudianteSeleccionado(DtoEstudianteComplete estudianteSeleccionado) {
        this.estudianteSeleccionado = estudianteSeleccionado;
    }

    public void setEstudianteRegistrado(DtoDatosEstudiante estudianteRegistrado) {
        this.estudianteRegistrado = estudianteRegistrado;
    }

    public void setEstudiantesRegistrados(List<DtoDatosEstudiante> estudiantesRegistrados) {
        this.estudiantesRegistrados = estudiantesRegistrados;
    }

    public void setEventoActivo(EventoEstadia eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setGeneracionEventoActivo(Generaciones generacionEventoActivo) {
        this.generacionEventoActivo = generacionEventoActivo;
    }

    public void setEventoSeleccionado(EventoEstadia eventoSeleccionado) {
        this.eventoSeleccionado = eventoSeleccionado;
    }
    
    public void setAsesorAcademicoEstadia(AsesorAcademicoEstadia asesorAcademicoEstadia) {
        this.asesorAcademicoEstadia = asesorAcademicoEstadia;
    }

    public void setDeshabilitarAsignacion(Boolean deshabilitarAsignacion) {
        this.deshabilitarAsignacion = deshabilitarAsignacion;
    }

    public void setRolAsesorActivo(Boolean rolAsesorActivo) {
        this.rolAsesorActivo = rolAsesorActivo;
    }

}
