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
     * Lista bajas registradas del periodo seleccionado
     */
    @Getter @NonNull private List<DtoTramitarBajas> bajas;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    
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

    public void setBajas(List<DtoTramitarBajas> bajas) {
        this.bajas = bajas;
    }
    
    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
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
}
