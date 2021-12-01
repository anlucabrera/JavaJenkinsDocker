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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class RegistroEventosEscolaresRolEscolares extends AbstractRol{
    /**
     * Representa la referencia hacia al usuario
     */
    @Getter @NonNull private PersonalActivo usuario;
    
     /**
     * Representa el periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
  
    /**
     * Lista periodos escolares
     */
    @Getter @NonNull private List<PeriodosEscolares> periodosEscolares;
    
    /**
     * Periodo escolar seleccionado
     */
    @Getter @NonNull private PeriodosEscolares periodoEscolar;
   
    /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private Boolean existeRegistro;
    
    /**
     * Lista de eventos escolares registrados
     */
    @Getter @NonNull private List<DtoCalendarioEventosEscolares> listaEventosRegistrados;
    
    /**
     * Lista de eventos escolares para registrar
     */
    @Getter @NonNull private List<DtoEventosEscolares> listaEventos;
    
    /**
     * Agregar evento
     */
    @Getter @NonNull private Boolean agregarEvento;
    
    /**
     * Lista de opciones de eventos escolares para agregar
     */
    @Getter @NonNull private List<String> listaOpcionesEventos;
    
    /**
     * Evento escolar que se agregará
     */
    @Getter @NonNull private String opcionEvento;
    
    /**
     * Fecha de inicio del evento escolar
     */
    @Getter @NonNull private Date fechaInicio;
    
    /**
     * Fecha de fin del evento escolar
     */
    @Getter @NonNull private Date fechaFin;
    
    /**
     * Representa si se deshabilita o no la acción de eliminar
     */
    @Getter @NonNull private Boolean deshabilitarEliminar;
    
    
    /**
     * Lista de procesos de inscripción registrados en el periodo escolar seleccionado
     */
    @Getter @NonNull private List<ProcesosInscripcion> listaProcesosInscripcion;
    
    /**
     * Número de pestaña activa
     */
    @Getter @NonNull private Integer pestaniaActiva;
    
    public RegistroEventosEscolaresRolEscolares(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
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

    public void setExisteRegistro(Boolean existeRegistro) {
        this.existeRegistro = existeRegistro;
    }

    public void setListaEventosRegistrados(List<DtoCalendarioEventosEscolares> listaEventosRegistrados) {
        this.listaEventosRegistrados = listaEventosRegistrados;
    }

    public void setListaEventos(List<DtoEventosEscolares> listaEventos) {
        this.listaEventos = listaEventos;
    }

    public void setAgregarEvento(Boolean agregarEvento) {
        this.agregarEvento = agregarEvento;
    }

    public void setListaOpcionesEventos(List<String> listaOpcionesEventos) {
        this.listaOpcionesEventos = listaOpcionesEventos;
    }

    public void setOpcionEvento(String opcionEvento) {
        this.opcionEvento = opcionEvento;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setDeshabilitarEliminar(Boolean deshabilitarEliminar) {
        this.deshabilitarEliminar = deshabilitarEliminar;
    }

    public void setListaProcesosInscripcion(List<ProcesosInscripcion> listaProcesosInscripcion) {
        this.listaProcesosInscripcion = listaProcesosInscripcion;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }
}
