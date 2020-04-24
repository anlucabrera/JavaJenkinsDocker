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
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class ConcentradoBajasRolVarios extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal
     */
    @Getter @NonNull private PersonalActivo personal;
    
    /**
     * Lista ciclos escolares
     */
    @Getter @NonNull private List<CiclosEscolares> ciclos;
    
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodos;
    
     /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
    
    /**
     * Ciclo escolar seleccionado
     */
    @Getter @NonNull private CiclosEscolares ciclo;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodo;
    
    /**
     * Lista de concentrado de bajas por categoría
     */
    @Getter @NonNull private List<DtoConcentradoBajas> listaConcentradoPorCategoria;
    
    /**
     * Lista de concentrado de bajas por tipo
     */
    @Getter @NonNull private List<DtoConcentradoBajas> listaConcentradoPorTipo;
    
    /**
     * Lista de concentrado de bajas por causa
     */
    @Getter @NonNull private List<DtoConcentradoBajas> listaConcentradoPorCausa;
    
    @Getter @NonNull private Integer bajasCatAcademicos;
    @Getter @NonNull private Integer bajasCatEconomicos;
    @Getter @NonNull private Integer bajasCatPersonales;
    
    @Getter @NonNull private Integer bajasTipDefinitiva;
    @Getter @NonNull private Integer bajasTipTemporal;
    
    
    public ConcentradoBajasRolVarios(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setCiclos(List<CiclosEscolares> ciclos) {
        this.ciclos = ciclos;
    }

    public void setPeriodos(List<PeriodosEscolares> periodos) {
        this.periodos = periodos;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setCiclo(CiclosEscolares ciclo) {
        this.ciclo = ciclo;
    }

    public void setPeriodo(PeriodosEscolares periodo) {
        this.periodo = periodo;
    }

    public void setListaConcentradoPorCategoria(List<DtoConcentradoBajas> listaConcentradoPorCategoria) {
        this.listaConcentradoPorCategoria = listaConcentradoPorCategoria;
    }

    public void setListaConcentradoPorTipo(List<DtoConcentradoBajas> listaConcentradoPorTipo) {
        this.listaConcentradoPorTipo = listaConcentradoPorTipo;
    }

    public void setListaConcentradoPorCausa(List<DtoConcentradoBajas> listaConcentradoPorCausa) {
        this.listaConcentradoPorCausa = listaConcentradoPorCausa;
    }

    public void setBajasCatAcademicos(Integer bajasCatAcademicos) {
        this.bajasCatAcademicos = bajasCatAcademicos;
    }

    public void setBajasCatEconomicos(Integer bajasCatEconomicos) {
        this.bajasCatEconomicos = bajasCatEconomicos;
    }

    public void setBajasCatPersonales(Integer bajasCatPersonales) {
        this.bajasCatPersonales = bajasCatPersonales;
    }

    public void setBajasTipDefinitiva(Integer bajasTipDefinitiva) {
        this.bajasTipDefinitiva = bajasTipDefinitiva;
    }

    public void setBajasTipTemporal(Integer bajasTipTemporal) {
        this.bajasTipTemporal = bajasTipTemporal;
    }

    
    
}
