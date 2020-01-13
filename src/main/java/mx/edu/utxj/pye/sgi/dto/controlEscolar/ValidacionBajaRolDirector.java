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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
/**
 *
 * @author UTXJ
 */
public class ValidacionBajaRolDirector extends AbstractRol{
    /**
     * Representa la referencia hacia el director de carrera
     */
    @Getter @NonNull private PersonalActivo directorCarrera;
    
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    
    /**
     * Lista bajas registradas del periodo seleccionado
     */
    @Getter @NonNull private List<DtoTramitarBajas> bajasPeriodo;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    
    /**
     * Área académcia superior
     */
    @Getter @NonNull private AreasUniversidad areaSuperior;
    
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
    @Getter @NonNull private List<DtoTramitarBajas> bajasProgramaEducativo;
    
    /**
     * Baja seleccionado
     */
    @Getter @NonNull private DtoTramitarBajas baja;
    
     /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
  
    /**
     * Baja actualizada
     */
    @Getter @NonNull private Baja bajaRegistrada;
    
     /**
     * Lista de materias reprobadas registradas en caso de ser baja por reprobación
     */
    @Getter @NonNull private List<DtoMateriaReprobada> listaMateriasReprobadas;
    
     /**
     * Valor modal con materias reprobadas
     */
    @Getter @NonNull private Boolean forzarAperturaDialogo;
    
     /**
     * Valor del status de la baja y área que valida
     */
    @Getter @NonNull private DtoValidacionesBaja dtoValidacionesBaja;
   
     public ValidacionBajaRolDirector(Filter<PersonalActivo> filtro, PersonalActivo directorCarrera) {
        super(filtro);
        this.directorCarrera = directorCarrera;
    }

    public void setDirectorCarrera(PersonalActivo directorCarrera) {
        this.directorCarrera = directorCarrera;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setBajasPeriodo(List<DtoTramitarBajas> bajasPeriodo) {
        this.bajasPeriodo = bajasPeriodo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setAreaSuperior(AreasUniversidad areaSuperior) {
        this.areaSuperior = areaSuperior;
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
    }

    public void setBajasProgramaEducativo(List<DtoTramitarBajas> bajasProgramaEducativo) {
        this.bajasProgramaEducativo = bajasProgramaEducativo;
    }

    public void setBaja(DtoTramitarBajas baja) {
        this.baja = baja;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setBajaRegistrada(Baja bajaRegistrada) {
        this.bajaRegistrada = bajaRegistrada;
    }

    public void setListaMateriasReprobadas(List<DtoMateriaReprobada> listaMateriasReprobadas) {
        this.listaMateriasReprobadas = listaMateriasReprobadas;
    }

    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo) {
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }

    public void setDtoValidacionesBaja(DtoValidacionesBaja dtoValidacionesBaja) {
        this.dtoValidacionesBaja = dtoValidacionesBaja;
    }
}
