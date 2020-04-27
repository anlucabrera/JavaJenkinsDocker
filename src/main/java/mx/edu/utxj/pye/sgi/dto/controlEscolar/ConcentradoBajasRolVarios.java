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
    
    @Getter @NonNull private Integer bajasAbandonoEst;
    @Getter @NonNull private Integer bajasFalRegEsc;
    @Getter @NonNull private Integer bajasCompCarrera;
    @Getter @NonNull private Integer bajasProbAcad;
    @Getter @NonNull private Integer bajasReprobacion;
    
    @Getter @NonNull private Integer bajasDistanciaUTXJ;
    @Getter @NonNull private Integer bajasProbEcon;
    
    @Getter @NonNull private Integer bajasCambCarrera;
    @Getter @NonNull private Integer bajasCambCiudad;
    @Getter @NonNull private Integer bajasCambDom;
    @Getter @NonNull private Integer bajasCambUni;
    @Getter @NonNull private Integer bajasCambUT;
    @Getter @NonNull private Integer bajasDefuncion;
    @Getter @NonNull private Integer bajasSinCausaCon;
    @Getter @NonNull private Integer bajasEmbarazo;
    @Getter @NonNull private Integer bajasInasistencia;
    @Getter @NonNull private Integer bajasIncompHorario;
    @Getter @NonNull private Integer bajasIncumExp;
    @Getter @NonNull private Integer bajasIntDist;
    @Getter @NonNull private Integer bajasMotivosPer;
    @Getter @NonNull private Integer bajasOrientacionVoc;
    @Getter @NonNull private Integer bajasProbSalud;
    @Getter @NonNull private Integer bajasProbTrab;
    @Getter @NonNull private Integer bajasProbFam;
    
    
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

    public void setBajasAbandonoEst(Integer bajasAbandonoEst) {
        this.bajasAbandonoEst = bajasAbandonoEst;
    }

    public void setBajasFalRegEsc(Integer bajasFalRegEsc) {
        this.bajasFalRegEsc = bajasFalRegEsc;
    }

    public void setBajasCompCarrera(Integer bajasCompCarrera) {
        this.bajasCompCarrera = bajasCompCarrera;
    }

    public void setBajasProbAcad(Integer bajasProbAcad) {
        this.bajasProbAcad = bajasProbAcad;
    }

    public void setBajasReprobacion(Integer bajasReprobacion) {
        this.bajasReprobacion = bajasReprobacion;
    }

    public void setBajasDistanciaUTXJ(Integer bajasDistanciaUTXJ) {
        this.bajasDistanciaUTXJ = bajasDistanciaUTXJ;
    }

    public void setBajasProbEcon(Integer bajasProbEcon) {
        this.bajasProbEcon = bajasProbEcon;
    }

    public void setBajasCambCarrera(Integer bajasCambCarrera) {
        this.bajasCambCarrera = bajasCambCarrera;
    }

    public void setBajasCambCiudad(Integer bajasCambCiudad) {
        this.bajasCambCiudad = bajasCambCiudad;
    }

    public void setBajasCambDom(Integer bajasCambDom) {
        this.bajasCambDom = bajasCambDom;
    }

    public void setBajasCambUni(Integer bajasCambUni) {
        this.bajasCambUni = bajasCambUni;
    }

    public void setBajasCambUT(Integer bajasCambUT) {
        this.bajasCambUT = bajasCambUT;
    }

    public void setBajasDefuncion(Integer bajasDefuncion) {
        this.bajasDefuncion = bajasDefuncion;
    }

    public void setBajasSinCausaCon(Integer bajasSinCausaCon) {
        this.bajasSinCausaCon = bajasSinCausaCon;
    }

    public void setBajasEmbarazo(Integer bajasEmbarazo) {
        this.bajasEmbarazo = bajasEmbarazo;
    }

    public void setBajasInasistencia(Integer bajasInasistencia) {
        this.bajasInasistencia = bajasInasistencia;
    }

    public void setBajasIncompHorario(Integer bajasIncompHorario) {
        this.bajasIncompHorario = bajasIncompHorario;
    }

    public void setBajasIncumExp(Integer bajasIncumExp) {
        this.bajasIncumExp = bajasIncumExp;
    }

    public void setBajasIntDist(Integer bajasIntDist) {
        this.bajasIntDist = bajasIntDist;
    }

    public void setBajasMotivosPer(Integer bajasMotivosPer) {
        this.bajasMotivosPer = bajasMotivosPer;
    }

    public void setBajasOrientacionVoc(Integer bajasOrientacionVoc) {
        this.bajasOrientacionVoc = bajasOrientacionVoc;
    }

    public void setBajasProbSalud(Integer bajasProbSalud) {
        this.bajasProbSalud = bajasProbSalud;
    }

    public void setBajasProbTrab(Integer bajasProbTrab) {
        this.bajasProbTrab = bajasProbTrab;
    }

    public void setBajasProbFam(Integer bajasProbFam) {
        this.bajasProbFam = bajasProbFam;
    }

}
