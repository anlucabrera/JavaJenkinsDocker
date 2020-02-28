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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
/**
 *
 * @author UTXJ
 */
public class ValidacionAperturaExtRolAdministrador extends AbstractRol{
    /**
     * Representa la referencia hacia el personal docente
     */
    @Getter @NonNull private PersonalActivo administrador;
    
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
   
    /**
     * Lista de programas educativos
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;
    
    /**
     * Programa educativo seleccionado
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;
    
    /**
     * Programa educativo seleccionado
     */
    @Getter @NonNull private List<DtoValidarPermisoCapExt> permisosProgramaEducativo;
    
     /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
  
     /**
     * Valor del status de la baja y área que valida
     */
    @Getter @NonNull private Boolean validacion;
   
     public ValidacionAperturaExtRolAdministrador(Filter<PersonalActivo> filtro, PersonalActivo administrador) {
        super(filtro);
        this.administrador = administrador;
    }

    public void setAdministrador(PersonalActivo administrador) {
        this.administrador = administrador;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public void setPermisosProgramaEducativo(List<DtoValidarPermisoCapExt> permisosProgramaEducativo) {
        this.permisosProgramaEducativo = permisosProgramaEducativo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setValidacion(Boolean validacion) {
        this.validacion = validacion;
    }

    
}
