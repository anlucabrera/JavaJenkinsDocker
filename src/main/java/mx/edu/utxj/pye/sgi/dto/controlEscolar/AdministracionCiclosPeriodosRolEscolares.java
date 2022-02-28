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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;

/**
 *
 * @author UTXJ
 */
public class AdministracionCiclosPeriodosRolEscolares extends AbstractRol{
    
    /**
     * Representa la referencia hacia el personal de servicios escolares
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
     /**
     * Habilitar o deshabilitar opciones para agregar ciclo escolar
     */
    @Getter @NonNull private Boolean agregarCicloEscolar;
    
     /**
     * Habilitar o deshabilitar opciones para agregar periodo escolar
     */
    @Getter @NonNull private Boolean agregarPeriodoEscolar;
    
    /**
     * Lista de ciclo escolares
     */
    @Getter @NonNull private List<CiclosEscolares> ciclosEscolares;
    
    /**
     * Lista de periodos escolares
     */
    @Getter @NonNull private List<DtoPeriodoEscolarFechas> periodosEscolares; 
    
    /**
     * Lista años de inicio
     */
    @Getter private List<Date> anios; 
    
    /**
     * Año inicio ciclo escolar
     */
    @Getter private Date anioInicio; 
    
    /**
     * Año fin ciclo escolar
     */
    @Getter private Date anioFin; 
    
     /**
     * Ciclo escolar seleccionado para agregar periodo escolar
     */
    @Getter private CiclosEscolares cicloEscolar;
    
     /**
     * Lista de meses de inicio para agregar periodo escolar
     */
    @Getter @NonNull private List<Meses> mesesInicio; 
    
     /**
     * Mes de inicio seleccionado para agregar periodo escolar
     */
    @Getter @NonNull private Meses mesInicio; 
    
     /**
     * Mes de fin para agregar periodo escolar
     */
    @Getter @NonNull private Meses mesFin; 
    
    /**
     * Mes de fin para agregar periodo escolar
     */
    @Getter @NonNull private Integer anioPeriodoEscolar; 
    
     /**
     * Fecha de inicio del periodo escolar agregado
     */
    @Getter private Date fechaInicioPeriodoEscolar; 
    
     /**
     * Fecha de fin del periodo escolar agregado
     */
    @Getter private Date fechaFinPeriodoEscolar; 
    
     /**
     * Número de pestaña activa del tab
     */
    @Getter @NonNull private Integer pestaniaActiva; 
    
    public AdministracionCiclosPeriodosRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setAgregarCicloEscolar(Boolean agregarCicloEscolar) {
        this.agregarCicloEscolar = agregarCicloEscolar;
    }

    public void setAgregarPeriodoEscolar(Boolean agregarPeriodoEscolar) {
        this.agregarPeriodoEscolar = agregarPeriodoEscolar;
    }

    public void setCiclosEscolares(List<CiclosEscolares> ciclosEscolares) {
        this.ciclosEscolares = ciclosEscolares;
    }

    public void setPeriodosEscolares(List<DtoPeriodoEscolarFechas> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    public void setAnios(List<Date> anios) {
        this.anios = anios;
    }
    
    public void setAnioInicio(Date anioInicio) {
        this.anioInicio = anioInicio;
    }

    public void setAnioFin(Date anioFin) {
        this.anioFin = anioFin;
    }

    public void setCicloEscolar(CiclosEscolares cicloEscolar) {
        this.cicloEscolar = cicloEscolar;
    }

    public void setMesesInicio(List<Meses> mesesInicio) {
        this.mesesInicio = mesesInicio;
    }

    public void setMesInicio(Meses mesInicio) {
        this.mesInicio = mesInicio;
    }

    public void setMesFin(Meses mesFin) {
        this.mesFin = mesFin;
    }

    public void setAnioPeriodoEscolar(Integer anioPeriodoEscolar) {
        this.anioPeriodoEscolar = anioPeriodoEscolar;
    }

    public void setFechaInicioPeriodoEscolar(Date fechaInicioPeriodoEscolar) {
        this.fechaInicioPeriodoEscolar = fechaInicioPeriodoEscolar;
    }

    public void setFechaFinPeriodoEscolar(Date fechaFinPeriodoEscolar) {
        this.fechaFinPeriodoEscolar = fechaFinPeriodoEscolar;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
    
}
