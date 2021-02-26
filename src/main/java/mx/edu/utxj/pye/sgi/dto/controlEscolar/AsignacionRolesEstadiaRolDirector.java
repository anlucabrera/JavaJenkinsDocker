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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CoordinadorAreaEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
/**
 *
 * @author UTXJ
 */
public class AsignacionRolesEstadiaRolDirector extends AbstractRol{
    /**
     * Representa la referencia hacia el director de carrera
     */
    @Getter @NonNull private PersonalActivo directorCarrera;
    
    /**
     * Área superior de la que es responsable el director de carrera
     */
    @Getter @NonNull private AreasUniversidad areaSuperior;
    
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
     * Roles asignados de la generacion y nivel seleccionados
     */
    @Getter @NonNull private List<DtoRolEstadia> rolesEstadia;
    
    /**
     * Rol de estadia seleccionado
     */
    @Getter @NonNull private DtoRolEstadia rolEstadia;
    
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
     * Coordinador de estadía registrado
     */
    @Getter @NonNull private CoordinadorAreaEstadia coordinadorAreaEstadia;
    
     /**
     * Asesor académico registrado
     */
    @Getter @NonNull private AsesorAcademicoEstadia asesorAcademicoEstadia;
    
     /**
     * Representa si está habilitada o deshabilitada la asignación de roles
     */
    @Getter @NonNull private Boolean deshabilitarAsignacion;
    
    
    public AsignacionRolesEstadiaRolDirector(Filter<PersonalActivo> filtro, PersonalActivo directorCarrera) {
        super(filtro);
        this.directorCarrera = directorCarrera;
    }

    public void setDirectorCarrera(PersonalActivo directorCarrera) {
        this.directorCarrera = directorCarrera;
    }

    public void setAreaSuperior(AreasUniversidad areaSuperior) {
        this.areaSuperior = areaSuperior;
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

    public void setRolesEstadia(List<DtoRolEstadia> rolesEstadia) {
        this.rolesEstadia = rolesEstadia;
    }

    public void setRolEstadia(DtoRolEstadia rolEstadia) {
        this.rolEstadia = rolEstadia;
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
    
    public void setCoordinadorAreaEstadia(CoordinadorAreaEstadia coordinadorAreaEstadia) {
        this.coordinadorAreaEstadia = coordinadorAreaEstadia;
    }

    public void setAsesorAcademicoEstadia(AsesorAcademicoEstadia asesorAcademicoEstadia) {
        this.asesorAcademicoEstadia = asesorAcademicoEstadia;
    }

    public void setDeshabilitarAsignacion(Boolean deshabilitarAsignacion) {
        this.deshabilitarAsignacion = deshabilitarAsignacion;
    }

}
