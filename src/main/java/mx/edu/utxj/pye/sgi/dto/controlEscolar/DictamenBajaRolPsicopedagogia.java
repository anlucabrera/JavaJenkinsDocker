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
public class DictamenBajaRolPsicopedagogia extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de psicopedagogía
     */
    @Getter @NonNull private PersonalActivo personalPsicopedagogia;
    
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<AreasUniversidad> programasEducativos;
    
    /**
     * Lista bajas registradas del periodo seleccionado
     */
    @Getter @NonNull private List<DtoTramitarBajas> bajas;
    
     /**
     * Lista bajas registradas del periodo seleccionado
     */
    @Getter @NonNull private List<DtoTramitarBajas> bajasProgramaEducativo;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private AreasUniversidad programaEducativo;
    
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
     * Valor de dictamen de la baja
     */
    @Getter @NonNull private String dictamenBaja;
    
     /**
     * Existe o no dictamen registrado
     */
    @Getter @NonNull private Boolean existeDictamen;
   
     /**
     * Valor de validación de la baja
     */
    @Getter @NonNull private DtoValidacionesBaja dtoValidacionesBaja;
    
     /**
     * Existe o no dictamenes pendientes de registrar
     */
    @Getter @NonNull private Boolean pendientes;
    
      /**
     * Mensaje de dictamenes pendientes por registrar
     */
    @Getter @NonNull private String mensajePendientes;
    
     /**
     * Verifica si se muestran los trámites de baja institucional o por programa educativo
     */
    @Getter @NonNull private Boolean mostrarRegDicInstitucional;
    
     /**
     * Verifica si se muestran todos los trámites de baja o solamente los que están pendientes de registrar dictamen
     */
    @Getter @NonNull private Boolean mostrarRegDicPendientes;
    
    public DictamenBajaRolPsicopedagogia(Filter<PersonalActivo> filtro, PersonalActivo personalPsicopedagogia) {
        super(filtro);
        this.personalPsicopedagogia = personalPsicopedagogia;
    }

    public void setPersonalPsicopedagogia(PersonalActivo personalPsicopedagogia) {
        this.personalPsicopedagogia = personalPsicopedagogia;
    }
    
    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setProgramasEducativos(List<AreasUniversidad> programasEducativos) {
        this.programasEducativos = programasEducativos;
    }
    
    public void setBajas(List<DtoTramitarBajas> bajas) {
        this.bajas = bajas;
    }

    public void setBajasProgramaEducativo(List<DtoTramitarBajas> bajasProgramaEducativo) {
        this.bajasProgramaEducativo = bajasProgramaEducativo;
    }
    
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setProgramaEducativo(AreasUniversidad programaEducativo) {
        this.programaEducativo = programaEducativo;
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

    public void setDictamenBaja(String dictamenBaja) {
        this.dictamenBaja = dictamenBaja;
    }

    public void setExisteDictamen(Boolean existeDictamen) {
        this.existeDictamen = existeDictamen;
    }

    public void setDtoValidacionesBaja(DtoValidacionesBaja dtoValidacionesBaja) {
        this.dtoValidacionesBaja = dtoValidacionesBaja;
    }   

    public void setPendientes(Boolean pendientes) {
        this.pendientes = pendientes;
    }

    public void setMensajePendientes(String mensajePendientes) {
        this.mensajePendientes = mensajePendientes;
    }

    public void setMostrarRegDicInstitucional(Boolean mostrarRegDicInstitucional) {
        this.mostrarRegDicInstitucional = mostrarRegDicInstitucional;
    }

    public void setMostrarRegDicPendientes(Boolean mostrarRegDicPendientes) {
        this.mostrarRegDicPendientes = mostrarRegDicPendientes;
    }
}
