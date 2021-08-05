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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AperturaEventosEscolaresRolEscolares extends AbstractRol {
     /**
     * Representa la referencia hacia el personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo usuario;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer periodoActivo;
     
    /**
     * Lista de Periodos en las que el docente tiene cargas académicas
     */
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    
    /**
     * Periodo seleccionado en el que se realizará la apertura
     */
    @Getter @NonNull private PeriodosEscolares periodoEscolar;
    
     /**
     * Tipo de búsqueda por área/coordinación/departamento o personal
     */
    @Getter @NonNull private String tipoBusqueda;
    
     /**
     * Lista de áreas académicas
     */
    @Getter @NonNull private List<AreasUniversidad> areas;
    
     /**
     * Área académica
     */
    @Getter @NonNull private AreasUniversidad area;
    
     /**
     * Pista del personal
     */
    @Getter @NonNull private String pistaPersonal;
   
    /**
     * Personal seleccionado para asignar apertura
     */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Lista de Cargas Académicas del docente
     */
    @Getter @NonNull private List<DtoAperturaEventosEscolares> listaAperturasEventosEscolares;
   
     /**
     * Carga académica seleccionada
     */
    @Getter @NonNull private List<EventoEscolar> eventos;
    
     /**
     * Lista de tipos de evaluación
     */
    @Getter @NonNull private EventoEscolar evento;
    
    /**
     * Fecha de inicio de la apertura
     */
    @Getter @NonNull private Date fechaInicio;

    /**
     * Fecha de fin de la apertura
     */
    @Getter @NonNull private Date fechaFin;
    
    /**
     * Fecha miníma para apertura
     */
    @Getter @NonNull private Date rangoFechaInicial;

    public AperturaEventosEscolaresRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setPeriodosEscolares(List<PeriodosEscolares> periodosEscolares) {
        this.periodosEscolares = periodosEscolares;
    }

    public void setPeriodoEscolar(PeriodosEscolares periodoEscolar) {
        this.periodoEscolar = periodoEscolar;
    }

    public void setTipoBusqueda(String tipoBusqueda) {
        this.tipoBusqueda = tipoBusqueda;
    }

    public void setAreas(List<AreasUniversidad> areas) {
        this.areas = areas;
    }

    public void setArea(AreasUniversidad area) {
        this.area = area;
    }

    public void setPistaPersonal(String pistaPersonal) {
        this.pistaPersonal = pistaPersonal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setListaAperturasEventosEscolares(List<DtoAperturaEventosEscolares> listaAperturasEventosEscolares) {
        this.listaAperturasEventosEscolares = listaAperturasEventosEscolares;
    }

    public void setEventos(List<EventoEscolar> eventos) {
        this.eventos = eventos;
    }

    public void setEvento(EventoEscolar evento) {
        this.evento = evento;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setRangoFechaInicial(Date rangoFechaInicial) {
        this.rangoFechaInicial = rangoFechaInicial;
    }
    
}
