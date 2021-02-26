/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;

/**
 *
 * @author UTXJ
 */
public class SeguimientoEstadiaRolDirector extends AbstractRol{
    /**
     * Representa la referencia hacia al director de carrera
     */
    @Getter @NonNull private PersonalActivo directorCarrera;
  
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
     * Lista de programas educativos
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;
    
    /**
     * Programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;
    
    /**
     * Lista de estudiantes que tiene asignados par dar seguimiento
     */
    @Getter @NonNull private List<DtoSeguimientoEstadia> estudiantesSeguimiento;
    
     /**
     * Seguimiento de un estudiante
     */
    @Getter @NonNull private DtoSeguimientoEstadia estudianteSeguimiento;
    
      /**
     * Represente el valor si el estudiante tiene evaluación de estadía registarda
     */
    @Getter @NonNull private Boolean evaluacionRegistrada;
    
      /**
     * Lista de criterios de evaluación de estadía registrados
     */
    @Getter @NonNull private List<CalificacionCriterioEstadia> listaEvaluacionEstadiaRegistrada;
    
    public SeguimientoEstadiaRolDirector(Filter<PersonalActivo> filtro, PersonalActivo directorCarrera) {
        super(filtro);
        this.directorCarrera = directorCarrera;
    }

    public void setDirectorCarrera(PersonalActivo directorCarrera) {
        this.directorCarrera = directorCarrera;
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

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }
    
    public void setEstudiantesSeguimiento(List<DtoSeguimientoEstadia> estudiantesSeguimiento) {
        this.estudiantesSeguimiento = estudiantesSeguimiento;
    }

    public void setEstudianteSeguimiento(DtoSeguimientoEstadia estudianteSeguimiento) {
        this.estudianteSeguimiento = estudianteSeguimiento;
    }

    public void setEvaluacionRegistrada(Boolean evaluacionRegistrada) {
        this.evaluacionRegistrada = evaluacionRegistrada;
    }

    public void setListaEvaluacionEstadiaRegistrada(List<CalificacionCriterioEstadia> listaEvaluacionEstadiaRegistrada) {
        this.listaEvaluacionEstadiaRegistrada = listaEvaluacionEstadiaRegistrada;
    }
}
