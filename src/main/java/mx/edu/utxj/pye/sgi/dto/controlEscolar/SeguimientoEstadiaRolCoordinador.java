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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;

/**
 *
 * @author UTXJ
 */
public class SeguimientoEstadiaRolCoordinador extends AbstractRol{
    /**
     * Representa la referencia hacia al coordinador por área
     */
    @Getter @NonNull private PersonalActivo docente;
  
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
     * Indica si se visualiza todas las columnas
     */
    @Getter @NonNull private Boolean ocultarColumnas;
    
    public SeguimientoEstadiaRolCoordinador(Filter<PersonalActivo> filtro, PersonalActivo docente) {
        super(filtro);
        this.docente = docente;
    }

    public void setDocente(PersonalActivo docente) {
        this.docente = docente;
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
    
    public void setOcultarColumnas(Boolean ocultarColumnas) {
        this.ocultarColumnas = ocultarColumnas;
    }
    
}
