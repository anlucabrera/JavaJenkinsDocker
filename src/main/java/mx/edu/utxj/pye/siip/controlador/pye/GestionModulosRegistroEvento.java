/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controlador.pye;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPermisoAperturaExtemporanea;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.siip.dto.pye.DtoRegistroEvento;
import mx.edu.utxj.pye.siip.dto.pye.GestionModulosRegistroEventoRolPYE;
import mx.edu.utxj.pye.siip.ejb.pye.EjbGestionModulosEvento;
import mx.edu.utxj.pye.siip.ejb.pye.EjbGestionModulosRegistro;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class GestionModulosRegistroEvento extends ViewScopedRol{
    @Getter @Setter GestionModulosRegistroEventoRolPYE rol;
    
    @EJB EjbGestionModulosEvento ejb;
    @EJB EjbGestionModulosRegistro ejbGestionModulosRegistro;
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logon;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol personal del departamento de planeación y evaluación, administradores de sistema con clave 349 y 511<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
    if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.GESTION_EVENTO_MODULOS_REGISTRO);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbGestionModulosRegistro.validarAdministrador(logon.getPersonal().getClave());//validar si es administrador
            
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return; }//cortar el flujo si no se pudo validar

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo usuario = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new GestionModulosRegistroEventoRolPYE(filtro, usuario);
            tieneAcceso = rol.tieneAcceso(usuario);

            if(!tieneAcceso){return;} //cortar el flujo si no tiene acceso

            rol.setUsuario(usuario);
            
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setPeriodoActivo(ejb.getEventoActivo()());
//            rol.setSoloLectura(true);
            
            rol.getInstrucciones().add("Seleccione eje de registro.");
            rol.getInstrucciones().add("Seleccione tipo de módulo.");
            rol.getInstrucciones().add("Dependiendo del tipo de módulo se actualizará la lista de módulos de registro.");
            rol.getInstrucciones().add("Seleccione el módulo de registro.");
            rol.getInstrucciones().add("Se actualizará la tabla y podrá consultar los usuarios que están asignados al módulo de registro.");
            rol.getInstrucciones().add("ELIMINAR ASIGNACIÓN.");
            rol.getInstrucciones().add("Deberá dar clic en el icono ubicado en la columna ELIMINAR de la fila del registro que desea eliminar.");
            rol.getInstrucciones().add("AGREGAR ASIGNACIÓN.");
            rol.getInstrucciones().add("Seleccione que desea agregar usuario.");
            rol.getInstrucciones().add("Ingrese nombre o clave del personal al que se le asignará el módulo, le aparecerá una lista de coincidencias, dará clic en el nombre que corresponda");
            rol.getInstrucciones().add("Deberá dar clic en el botón guardar para registrar la asignación.");
            
            rol.setHabilitarCambiarMes(false);
            rol.setExisteRegSeleccionados(false);
            
            listadoEventosRegistro();
        }catch (Exception e){mostrarExcepcion(e); }
    }

     /**
     * Permite obtener la lista de eventos registro
     */
    public void listadoEventosRegistro(){
        ResultadoEJB<List<EventosRegistros>> res =  ejb.getEventosRegistro();
        if(res.getCorrecto()){
            rol.setEventosRegistro(res.getValor());
            rol.setEventoRegistro(rol.getEventosRegistro().get(0));
            listadoAreas();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite obtener la lista de áreas que tienen registro en el evento seleccionado
     */
    public void listadoAreas(){
        ResultadoEJB<List<AreasUniversidad>> res = ejb.getAreasEventoRegistro(rol.getEventoRegistro());
        if(res.getCorrecto()){
            if(!res.getValor().isEmpty()){
               rol.setAreas(res.getValor());
               rol.setArea(rol.getAreas().get(0));
               listadoEjesRegistro();
            }else{
               rol.setAreas(Collections.EMPTY_LIST);    
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
    
     /**
     * Permite obtener la lista de ejes de registro del evento y área selecccionada
     */
    public void listadoEjesRegistro(){
        ResultadoEJB<List<EjesRegistro>> res =  ejb.getEjesRegistroArea(rol.getEventoRegistro(), rol.getArea());
        if(res.getCorrecto()){
            rol.setEjesRegistro(res.getValor());
            rol.setEjeRegistro(rol.getEjesRegistro().get(0));
            listadoTiposRegistro();
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de tipos de registro del evento, área y eje seleccionado
     */
    public void listadoTiposRegistro(){
        ResultadoEJB<List<RegistrosTipo>> res =  ejb.getTiposRegistroAreaEje(rol.getEventoRegistro(), rol.getArea(), rol.getEjeRegistro());
        if(res.getCorrecto()){
            rol.setTiposRegistro(res.getValor());
            rol.setTipoRegistro(rol.getTiposRegistro().get(0));
            listadoRegistrosEvento();
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de registros del evento, área, eje y tipo seleccionado
     */
    public void listadoRegistrosEvento(){
        ResultadoEJB<List<DtoRegistroEvento>> res =  ejb.getRegistrosEvento(rol.getEventoRegistro(), rol.getArea(), rol.getEjeRegistro(), rol.getTipoRegistro());
        if(res.getCorrecto()){
            rol.setListaRegistrosEvento(res.getValor());
            Ajax.update("tbRegEvento");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    /**
     * Permite que al cambiar o seleccionar el eje de registro se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarEvento(ValueChangeEvent e){
        if(e.getNewValue() instanceof EventosRegistros){
            EventosRegistros evento = (EventosRegistros)e.getNewValue();
            rol.setEventoRegistro(evento);
            listadoAreas();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
   
    /**
     * Permite que al cambiar o seleccionar el eje de registro se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarArea(ValueChangeEvent e){
        if(e.getNewValue() instanceof AreasUniversidad){
            AreasUniversidad area = (AreasUniversidad)e.getNewValue();
            rol.setArea(area);
            listadoEjesRegistro();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite que al cambiar o seleccionar el eje de registro se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarEje(ValueChangeEvent e){
        if(e.getNewValue() instanceof EjesRegistro){
            EjesRegistro eje = (EjesRegistro)e.getNewValue();
            rol.setEjeRegistro(eje);
            listadoTiposRegistro();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que al cambiar o seleccionar el eje de registro se actualice la información en la tabla que corresponda
     * @param e Evento del cambio de valor
     */
    public void cambiarTipo(ValueChangeEvent e){
        if(e.getNewValue() instanceof RegistrosTipo){
            RegistrosTipo tipo = (RegistrosTipo)e.getNewValue();
            rol.setTipoRegistro(tipo);
            listadoRegistrosEvento();
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
     /**
     * Permite que al cambiar o seleccionar el registro se actualice el valor de selección que corresponda
     * @param e Evento del cambio de valor
     */
    public void seleccionarRegistro(ValueChangeEvent e) {
        try {
            String id = e.getComponent().getClientId();
            DtoRegistroEvento dto = rol.getListaRegistrosEvento().get(Integer.parseInt(id.split("tbRegEvento:")[1].split(":validar")[0]));
            dto.setSeleccionado((Boolean) e.getNewValue());
            verificarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(GestionModulosRegistroEvento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      /**
     * Permite obtener la lista de eventos de registro disponibles para cambiar de mes el registro seleccionado
     */
    public void verificarLista(){
       Integer registrosSeleccionados = rol.getListaRegistrosEvento().stream().filter(p->p.getSeleccionado()).collect(Collectors.toList()).size();
       if(registrosSeleccionados > 0){
           rol.setExisteRegSeleccionados(true);
       }else{
           rol.setExisteRegSeleccionados(false);
       }
       Ajax.update("frm");
    }
    
     /**
     * Permite habilitar o deshabilitar componentes para cambiar de mes el registro
     * @param e Evento del cambio de valor
     */
    public void cambiarHabilitarCambiarMes(ValueChangeEvent e){
        if(e.getNewValue() instanceof Boolean){
            Boolean valor = (Boolean)e.getNewValue();
            rol.setHabilitarCambiarMes(valor);
            if(rol.getHabilitarCambiarMes()){
                listaMesesCambiarRegistro();
            }
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
      /**
     * Permite obtener la lista de eventos de registro disponibles para cambiar de mes el registro seleccionado
     */
    public void listaMesesCambiarRegistro(){
       rol.setEventosCambiarMes(rol.getEventosRegistro().stream().filter(p->p != rol.getEventoRegistro()).collect(Collectors.toList()));
       rol.setEventoCambiarMes(rol.getEventosCambiarMes().get(0));
       Ajax.update("frm");
    }
    
    /**
     * Permite que al cambiar o seleccionar el valor del mes al que se cambiará el registro
     * @param e Evento del cambio de valor
     */
    public void cambiarMesRegistro(ValueChangeEvent e){
        if(e.getNewValue() instanceof EventosRegistros){
            EventosRegistros mesCambiar = (EventosRegistros)e.getNewValue();
            rol.setEventoCambiarMes(mesCambiar);
            Ajax.update("frm");
        }else mostrarMensaje("");
    }
    
    /**
     * Permite guardar una asignación
     */
    public void cambiarEventoRegistros(){
        ResultadoEJB<List<DtoRegistroEvento>> cambiar = ejb.cambiarEventoRegistros(rol.getEventoCambiarMes(), rol.getListaRegistrosEvento().stream().filter(p->p.getSeleccionado()).collect(Collectors.toList()));
        if (cambiar.getCorrecto()) {
            Messages.addGlobalInfo(cambiar.getMensaje());
            listadoRegistrosEvento();
            inicializarValores();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(cambiar);
    }
    
      /**
     * Permite inicializar valores una vez que se realiza el cambio de mes correctamente
     */
    public void inicializarValores(){
       rol.setHabilitarCambiarMes(false);
       rol.setExisteRegSeleccionados(false);
       Ajax.update("frm");
    }
    
    
}
