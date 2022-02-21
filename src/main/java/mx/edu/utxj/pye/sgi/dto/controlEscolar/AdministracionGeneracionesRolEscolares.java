/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosescolaresGeneraciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionGeneracionesRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
     /**
     * Habilitar o deshabilitar opciones para agregar una generación
     */
    @Getter @NonNull private Boolean agregarGeneracion;
    
     /**
     * Habilitar o deshabilitar opciones para un ciclo a una generación
     */
    @Getter @NonNull private Boolean agregarCicloGeneracion;
    
    /**
     * Lista de generaciones
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
    /**
     * Lista de ciclo generaciones
     */
    @Getter @NonNull private List<CiclosescolaresGeneraciones> cicloGeneraciones; 
    
    /**
     * Lista años de inicio
     */
    @Getter private List<Short> anios; 
    
    /**
     * Año inicio generación
     */
    @Getter private Short anioInicio; 
    
    /**
     * Año fin generación
     */
    @Getter private Short anioFin; 
    
     /**
     * Generación seleccionada para relacionar ciclo escolar
     */
    @Getter private Generaciones generacion;
    
     /**
     * Lista de ciclos escolares para relacionar a una generación
     */
    @Getter @NonNull private List<CiclosEscolares> ciclosEscolares; 
    
     /**
     * Ciclo escolar seleccionado para realacionar una generación
     */
    @Getter @NonNull private CiclosEscolares cicloEscolar; 
    
     /**
     * Número de pestaña activa del tab
     */
    @Getter @NonNull private Integer pestaniaActiva; 
    
    public AdministracionGeneracionesRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setAgregarGeneracion(Boolean agregarGeneracion) {
        this.agregarGeneracion = agregarGeneracion;
    }

    public void setAgregarCicloGeneracion(Boolean agregarCicloGeneracion) {
        this.agregarCicloGeneracion = agregarCicloGeneracion;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setCicloGeneraciones(List<CiclosescolaresGeneraciones> cicloGeneraciones) {
        this.cicloGeneraciones = cicloGeneraciones;
    }

    public void setAnios(List<Short> anios) {
        this.anios = anios;
    }

    public void setAnioInicio(Short anioInicio) {
        this.anioInicio = anioInicio;
    }

    public void setAnioFin(Short anioFin) {
        this.anioFin = anioFin;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setCiclosEscolares(List<CiclosEscolares> ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    public void setCicloEscolar(CiclosEscolares cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }

    
}
