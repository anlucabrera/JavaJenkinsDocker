/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;

/**
 *
 * @author UTXJ
 */
public class GestionModulosRegistroEventoRolPYE extends AbstractRol {
     /**
     * Representa la referencia hacia el personal de planeación y evaluación
     */
    @Getter @NonNull private PersonalActivo usuario;
    
    /**
     * Representa la clave
     */
    @Getter @NonNull private Integer eventoActivo;
    
     /**
     * Lista de eventos de registro
     */
    @Getter @NonNull private List<EventosRegistros> eventosRegistro;
    
     /**
     * Evento de registro seleccionado
     */
    @Getter @NonNull private EventosRegistros eventoRegistro;
    
     /**
     * Lista de areas con registros del evento seleccionado
     */
    @Getter @NonNull private List<AreasUniversidad> areas;
    
     /**
     * Área seleccionada
     */
    @Getter @NonNull private AreasUniversidad area;
    
     /**
     * Lista de ejes de registro
     */
    @Getter @NonNull private List<EjesRegistro> ejesRegistro;
    
     /**
     * Eje de registro seleccionado
     */
    @Getter @NonNull private EjesRegistro ejeRegistro;
     
    /**
     * Lista de tipos de registro del eje seleccionado
     */
    @Getter @NonNull private List<RegistrosTipo> tiposRegistro;
    
    /**
     * Tipo de registro seleccionado
     */
    @Getter @NonNull private RegistrosTipo tipoRegistro;
    
     /**
     * Lista de registros del evento, área, eje y tipo seleccionado
     */
    @Getter @NonNull private List<DtoRegistroEvento> listaRegistrosEvento;
    
    /**
     * Representa valor para saber si se muestran o no las opciones para cambiar mes
     */
    @Getter @NonNull private Boolean habilitarCambiarMes;
    
    /**
     * Lista de eventos de registro disponibles para cambiar de mes el registro seleccionado
     */
    @Getter @NonNull private List<EventosRegistros> eventosCambiarMes;
    
     /**
     * Evento de registro seleccionado al que se cambiará el registro seleccionado
     */
    @Getter @NonNull private EventosRegistros eventoCambiarMes;
    
    /**
     * Representa valor para saber si hya registros seleccionados para cambiar mes
     */
    @Getter @NonNull private Boolean existeRegSeleccionados;
    
    
    public GestionModulosRegistroEventoRolPYE(Filter<PersonalActivo> filtro, PersonalActivo usuario) {
        super(filtro);
        this.usuario = usuario;
    }

    public void setUsuario(PersonalActivo usuario) {
        this.usuario = usuario;
    }

    public void setEventoActivo(Integer eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setEventosRegistro(List<EventosRegistros> eventosRegistro) {
        this.eventosRegistro = eventosRegistro;
    }

    public void setEventoRegistro(EventosRegistros eventoRegistro) {
        this.eventoRegistro = eventoRegistro;
    }

    public void setAreas(List<AreasUniversidad> areas) {
        this.areas = areas;
    }

    public void setArea(AreasUniversidad area) {
        this.area = area;
    }

    public void setEjesRegistro(List<EjesRegistro> ejesRegistro) {
        this.ejesRegistro = ejesRegistro;
    }

    public void setEjeRegistro(EjesRegistro ejeRegistro) {
        this.ejeRegistro = ejeRegistro;
    }

    public void setTiposRegistro(List<RegistrosTipo> tiposRegistro) {
        this.tiposRegistro = tiposRegistro;
    }

    public void setTipoRegistro(RegistrosTipo tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public void setListaRegistrosEvento(List<DtoRegistroEvento> listaRegistrosEvento) {
        this.listaRegistrosEvento = listaRegistrosEvento;
    }

    public void setHabilitarCambiarMes(Boolean habilitarCambiarMes) {
        this.habilitarCambiarMes = habilitarCambiarMes;
    }

    public void setEventosCambiarMes(List<EventosRegistros> eventosCambiarMes) {
        this.eventosCambiarMes = eventosCambiarMes;
    }

    public void setEventoCambiarMes(EventosRegistros eventoCambiarMes) {
        this.eventoCambiarMes = eventoCambiarMes;
    }

    public void setExisteRegSeleccionados(Boolean existeRegSeleccionados) {
        this.existeRegSeleccionados = existeRegSeleccionados;
    }
    
}
