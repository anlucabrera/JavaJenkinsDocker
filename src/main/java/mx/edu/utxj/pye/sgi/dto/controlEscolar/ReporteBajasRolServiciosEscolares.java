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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Baja;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class ReporteBajasRolServiciosEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia el personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personal;
    
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
     * Lista de materias reprobadas registradas en caso de ser baja por reprobación
     */
    @Getter @NonNull private List<DtoMateriaReprobada> listaMateriasReprobadas;
    
     /**
     * Valor modal con materias reprobadas
     */
    @Getter @NonNull private Boolean forzarAperturaDialogo;
    
     /**
     * Valor se encuentra validado o no la baja
     */
    @Getter @NonNull private Integer statusBaja;
    
    /**
     * Información completa de la Baja registrada
     */
    @Getter @NonNull private DtoRegistroBajaEstudiante registroBajaEstudiante;
    
    public ReporteBajasRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
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

    public void setListaMateriasReprobadas(List<DtoMateriaReprobada> listaMateriasReprobadas) {
        this.listaMateriasReprobadas = listaMateriasReprobadas;
    }

    public void setForzarAperturaDialogo(Boolean forzarAperturaDialogo) {
        this.forzarAperturaDialogo = forzarAperturaDialogo;
    }

    public void setStatusBaja(Integer statusBaja) {
        this.statusBaja = statusBaja;
    }

    public void setRegistroBajaEstudiante(DtoRegistroBajaEstudiante registroBajaEstudiante) {
        this.registroBajaEstudiante = registroBajaEstudiante;
    }
}
