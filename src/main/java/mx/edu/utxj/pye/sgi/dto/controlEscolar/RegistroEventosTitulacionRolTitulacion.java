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
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
/**
 *
 * @author UTXJ
 */
public class RegistroEventosTitulacionRolTitulacion extends AbstractRol{
     /**
     * Representa la referencia hacia al usuario
     */
    @Getter @NonNull private PersonalActivo usuario;
    
     /**
     * Representa el periodo escolar activo
     */
    @Getter @NonNull private Integer periodoActivo;
    
     /**
     * Lista de eventos de titulación registrados de generaciones de control escolar
     */
    @Getter @NonNull private List<DtoEventosTitulacion.GeneracionesControlEscolar> listEventosTitulacionCE;
    
     /**
     * Lista de eventos de titulación registrados de generaciones de control escolar
     */
    @Getter @NonNull private List<DtoEventosTitulacion.GeneracionesSAIIUT> listEventosTitulacionSAIIUT;
    
     /**
     * Representa el valor si se habilitan o deshabilitan componentes para agregar nuevo evento
     */
    @Getter @NonNull private Boolean agregarEvento;
    
     /**
     * Lista de generaciones activas en control escolar
     */
    @Getter @NonNull private List<Generaciones> generaciones;
    
     /**
     * Generación seleccionada
     */
    @Getter @NonNull private Generaciones generacion;
    
     /**
     * Lista de niveles educativos
     */
    @Getter @NonNull private List<ProgramasEducativosNiveles> niveles;
    
     /**
     * Nivel educativo seleccionado
     */
    @Getter @NonNull private ProgramasEducativosNiveles nivel;
    
    /**
     * Fecha de inicio del evento
     */
    @Getter @NonNull private Date fechaInicio;
    
    /**
     * Fecha fin del evento
     */
    @Getter @NonNull private Date fechaFin;
    
    public RegistroEventosTitulacionRolTitulacion(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setListEventosTitulacionCE(List<DtoEventosTitulacion.GeneracionesControlEscolar> listEventosTitulacionCE) {
        this.listEventosTitulacionCE = listEventosTitulacionCE;
    }

    public void setListEventosTitulacionSAIIUT(List<DtoEventosTitulacion.GeneracionesSAIIUT> listEventosTitulacionSAIIUT) {
        this.listEventosTitulacionSAIIUT = listEventosTitulacionSAIIUT;
    }

    public void setAgregarEvento(Boolean agregarEvento) {
        this.agregarEvento = agregarEvento;
    }

    public void setGeneraciones(List<Generaciones> generaciones) {
        this.generaciones = generaciones;
    }

    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }

    public void setNiveles(List<ProgramasEducativosNiveles> niveles) {
        this.niveles = niveles;
    }

    public void setNivel(ProgramasEducativosNiveles nivel) {
        this.nivel = nivel;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
    
}
