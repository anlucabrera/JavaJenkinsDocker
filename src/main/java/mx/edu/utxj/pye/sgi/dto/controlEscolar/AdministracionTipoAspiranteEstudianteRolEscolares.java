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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionTipoAspiranteEstudianteRolEscolares extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
     /**
     * Habilitar o deshabilitar opciones para agregar tipo de aspirante
     */
    @Getter @NonNull private Boolean agregarTipoAspirante;
    
     /**
     * Habilitar o deshabilitar opciones para agregar tipo de estudiante
     */
    @Getter @NonNull private Boolean agregarTipoEstudiante;
    
    /**
     * Tipo de aspirante seleccionado
     */
    @Getter @NonNull private TipoAspirante tipoAspirante;

    /**
     * Lista de tipo de aspirante
     */
    @Getter @NonNull private List<TipoAspirante> tiposAspirante;
    
    /**
     * Tipo de estudiante seleccionado
     */
    @Getter @NonNull private TipoEstudiante tipoEstudiante;

    /**
     * Lista de tipo de estudiante
     */
    @Getter @NonNull private List<TipoEstudiante> tiposEstudiante;  
    
     /**
     * Nuevo aspirante
     */
    @Getter private String nuevoAspirante; 
    
    
    /**
     * Nuevo estudiante
     */
    @Getter private String nuevoEstudiante; 
    
     /**
     * Número de pestaña activa del tab
     */
    @Getter @NonNull private Integer pestaniaActiva; 
    
    public AdministracionTipoAspiranteEstudianteRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setAgregarTipoAspirante(Boolean agregarTipoAspirante) {
        this.agregarTipoAspirante = agregarTipoAspirante;
    }

    public void setAgregarTipoEstudiante(Boolean agregarTipoEstudiante) {
        this.agregarTipoEstudiante = agregarTipoEstudiante;
    }

    public void setTipoAspirante(TipoAspirante tipoAspirante) {
        this.tipoAspirante = tipoAspirante;
    }

    public void setTiposAspirante(List<TipoAspirante> tiposAspirante) {
        this.tiposAspirante = tiposAspirante;
    }

    public void setTipoEstudiante(TipoEstudiante tipoEstudiante) {
        this.tipoEstudiante = tipoEstudiante;
    }

    public void setTiposEstudiante(List<TipoEstudiante> tiposEstudiante) {
        this.tiposEstudiante = tiposEstudiante;
    }

    public void setNuevoAspirante(String nuevoAspirante) {
        this.nuevoAspirante = nuevoAspirante;
        if(nuevoAspirante == null){
            this.setNuevoAspirante("Ingresar nombre");
        }
    }

    public void setNuevoEstudiante(String nuevoEstudiante) {
        this.nuevoEstudiante = nuevoEstudiante;
        if(nuevoEstudiante == null){
            this.setNuevoEstudiante("Ingresar nombre");
        }
    }
    
    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
    
}
