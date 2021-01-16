/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.Collections;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroExpedienteRolTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNuevoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroExpediente;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import org.omnifaces.util.Ajax;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Date;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroExpedienteTitulacion extends ViewScopedRol implements Desarrollable{
    @Getter @Setter RegistroExpedienteRolTitulacion rol;

    @EJB EjbRegistroExpediente ejb;
    @EJB EjbIntegracionExpedienteTitulacion ejbIntegracionExpedienteTitulacion;
    @EJB EjbPropiedades ep;
    @EJB EJBSelectItems eJBSelectItems;
    @EJB EjbSelectItemCE ejbItemCE;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;

    /**
     * Inicializa:<br/>
     *      El filtro de rol por personal de servicios escolares<br/>
     *      El DTO del rol<br/>
     *      El periodo activo<br/>
     *      Las instrucciones de operación de la herramienta<br/>
     */
    
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        cargado = true;
        try{
            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_EXPEDIENTE_TITULACION);
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejb.validarTitulacion(logonMB.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            rol = new RegistroExpedienteRolTitulacion(filtro, personal);
            tieneAcceso = rol.tieneAcceso(personal);
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso

            rol.setPersonal(personal);
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
           
            rol.setNivelRol(NivelRol.OPERATIVO);
//            rol.setSoloLectura(true);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            
            rol.getInstrucciones().add("Seleccione generacion.");
            rol.getInstrucciones().add("Seleccione nivel educativo.");
            rol.getInstrucciones().add("Seleccionar el proceso de integración de expediente.");
            rol.getInstrucciones().add("Seleccionar fecha de registro.");
            rol.getInstrucciones().add("Ingrese la matricula del estudiante, seleccione de la lista el registro que corresponda (cuatrimestre más actual).");
            
            rol.setExisteExpediente(Boolean.FALSE);
            rol.setHabilitarBoton(Boolean.FALSE);
            
            rol.setFechaIntExp(new Date());
            listaGeneracionesExpedientes();
        }catch (Exception e){mostrarExcepcion(e); }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro expediente titulacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
     /**
     * Permite obtener la lista de generaciones 
     */
    public void listaGeneracionesExpedientes(){
        ResultadoEJB<List<Generaciones>> res = ejb.getGeneracionesExpediente();
        if(res.getCorrecto()){
            if (res.getValor().size() != 0) {
                rol.setGeneraciones(res.getValor());
                rol.setGeneracion(rol.getGeneraciones().get(0));
                listaNivelesGeneracion();
            }
        }else mostrarMensajeResultadoEJB(res);
    }
    
     /**
     * Permite obtener la lista de niveles educativos
     */
    public void listaNivelesGeneracion(){
        if(rol.getGeneracion()== null) return;
        ResultadoEJB<List<ProgramasEducativosNiveles>> res = ejb.getNivelesExpedientes();
        if(res.getCorrecto()){
            rol.setNivelesEducativos(res.getValor());
            rol.setNivelEducativo(rol.getNivelesEducativos().get(0));
            listaProcesosIntegracion();
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite obtener la lista de niveles educativos
     */
    public void listaProcesosIntegracion(){
        if(rol.getNivelEducativo()== null) return;
        ResultadoEJB<List<EventoTitulacion>> res = ejb.getProcesosIntegracion(rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            rol.setProcesosIntegracion(res.getValor());
            rol.setProcesoIntegracion(rol.getProcesosIntegracion().get(0));
        }else mostrarMensajeResultadoEJB(res);
    
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarGeneracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof Generaciones){
            Generaciones generacion= (Generaciones)e.getNewValue();
            rol.setGeneracion(generacion);
            listaNivelesGeneracion();
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarNivelEducativo(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            ProgramasEducativosNiveles nivel= (ProgramasEducativosNiveles)e.getNewValue();
            rol.setNivelEducativo(nivel);
            Ajax.update("frm");
        }
    }
    
     /**
     * Permite que al cambiar o seleccionar programa educativo se actualice el valor de la variable
     * @param e Evento del cambio de valor
     */
    public void cambiarProcesoIntegracion(ValueChangeEvent e){
        if(e.getNewValue() instanceof String){
            EventoTitulacion proceso= (EventoTitulacion)e.getNewValue();
            rol.setProcesoIntegracion(proceso);
            Ajax.update("frm");
        }
    }
    
     /**
     * Método para proporcionar lista de docentes sugeridos en un autocomplete donde se puede ingresar el número de nómina, nombre o área del docente
     * @param pista
     * @return Lista de sugerencias
     */
    public List<DtoEstudianteComplete> completeEstudiantes(String pista){
        ResultadoEJB<List<DtoEstudianteComplete>> res = ejb.buscarEstudiante(pista, rol.getGeneracion(), rol.getNivelEducativo());
        if(res.getCorrecto()){
            return res.getValor();
        }else{
            mostrarMensajeResultadoEJB(res);
            return Collections.emptyList();
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar un estudiante se pueda actualizar la información
     * @param e Evento del cambio de valor
     */
    public void cambiarEstudiante(ValueChangeEvent e){
        if(e.getNewValue() instanceof DtoEstudianteComplete){
            DtoEstudianteComplete estudiante = (DtoEstudianteComplete)e.getNewValue();
            rol.setEstudianteSeleccionado(estudiante);
            Ajax.update("frm");
            existeExpediente();
        }else mostrarMensaje("El valor seleccionado como estudiante no es del tipo necesario.");
    }
    
     /* Combos para domicilio */
    public void selectMunicipio(){
        rol.setListaMunicipiosRadica(eJBSelectItems.itemMunicipiosByClave(this.rol.getDatosNuevoExpediente().getDomicilio().getIdEstado()));
    }
    
    public void selectAsentamiento(){
        rol.setListaAsentamientos(eJBSelectItems.itemAsentamientoByClave(rol.getDatosNuevoExpediente().getDomicilio().getIdEstado(), rol.getDatosNuevoExpediente().getDomicilio().getIdMunicipio()));
    }
  
    /* Combos para IEMS */
    public void selectMunicipioIems(){
        rol.setListaMunicipiosIEMS(eJBSelectItems.itemMunicipiosByClave(this.rol.getDatosNuevoExpediente().getIems().getLocalidad().getMunicipio().getEstado().getIdestado()));
    }
    
    public void selectLocalidadIems(){
        rol.setListaLocalidadesIEMS(eJBSelectItems.itemLocalidadesIEMSByClave(this.rol.getDatosNuevoExpediente().getIems().getLocalidad().getMunicipio().getEstado().getIdestado(),this.rol.getDatosNuevoExpediente().getIems().getLocalidad().getMunicipio().getMunicipioPK().getClaveMunicipio()));
        selectIems();
    }
    
    public void selectIems(){
        rol.setListaIEMS(ejbItemCE.itemIems(this.rol.getDatosNuevoExpediente().getIems().getLocalidad().getMunicipio().getEstado().getIdestado(),this.rol.getDatosNuevoExpediente().getIems().getLocalidad().getMunicipio().getMunicipioPK().getClaveMunicipio(), this.rol.getDatosNuevoExpediente().getIems().getLocalidad().getLocalidadPK().getClaveLocalidad()));  
    }
    public void actualizarIEMS(){
        selectIems();
        Ajax.update("frm");
    }
    
    public void existeExpediente(){
        rol.setExpedienteTitulacion(ejb.getExisteExpedienteTitulacion(rol.getEstudianteSeleccionado().getEstudiantes(), rol.getGeneracion(), rol.getNivelEducativo()).getValor());
        if (rol.getExpedienteTitulacion() != null) {
            rol.setExisteExpediente(Boolean.TRUE);
            Messages.addGlobalWarn("El estudiante ya cuenta con expediente registrado, número de expediente: " + rol.getExpedienteTitulacion().getExpediente());
        }else{
            rol.setExisteExpediente(Boolean.FALSE);
            rol.setHabilitarBoton(Boolean.TRUE);
            rol.setExpedienteTitulacion(null);
        }
        Ajax.update("frm");
    }
    
    public void guardarExpediente() {
        ResultadoEJB<ExpedienteTitulacion> res = ejb.guardarExpedienteTitulacion(rol.getProcesoIntegracion(), rol.getFechaIntExp(), rol.getEstudianteSeleccionado().getEstudiantes());
        if(res.getCorrecto()){
            rol.setExpedienteTitulacion(res.getValor());
            mostrarMensajeResultadoEJB(res);
            rol.setHabilitarBoton(Boolean.FALSE);
            obtenerInformacionEstudiante(rol.getExpedienteTitulacion());
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void obtenerInformacionEstudiante(ExpedienteTitulacion expedienteTitulacion){
        ResultadoEJB<DtoNuevoExpedienteTitulacion> res = ejb.getDtoNuevoExpedienteTitulacion(expedienteTitulacion);
        if(res.getCorrecto()){
            rol.setDatosNuevoExpediente(res.getValor());
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frm");
           
            rol.setListaEstadosDomicilioRadica(eJBSelectItems.itemEstados());
            selectMunicipio();
            selectAsentamiento();
        
            rol.setListaEstadosIEMS(eJBSelectItems.itemEstados());
            selectMunicipioIems();
            selectLocalidadIems();
            selectIems();
        }else mostrarMensajeResultadoEJB(res);
    }
    
    
     public void guardarDatosExpediente() {
        System.err.println("guardarDatosExpediente - entra");
        rol.setPasoRegistroInd("Datos Personales");
        ResultadoEJB<ExpedienteTitulacion> res = ejb.actualizarExpediente(rol.getDatosNuevoExpediente().getExpedienteTitulacion(), rol.getPasoRegistroInd());
        if(res.getCorrecto()){
            rol.setExpedienteTitulacion(res.getValor());
            mostrarMensajeResultadoEJB(res);
            actualizarDomicilio();
        }else mostrarMensajeResultadoEJB(res); 
    }
     
     public void actualizarExpediente() {
        ResultadoEJB<ExpedienteTitulacion> res = ejb.actualizarExpediente(rol.getDatosNuevoExpediente().getExpedienteTitulacion(), rol.getPasoRegistroInd());
        if(res.getCorrecto()){
            rol.setExpedienteTitulacion(res.getValor());
            mostrarMensajeResultadoEJB(res);
        }else mostrarMensajeResultadoEJB(res); 
    }
        
    public void actualizarDomicilio() {
        ResultadoEJB<Domicilio> res = ejbIntegracionExpedienteTitulacion.actualizarDomicilio(rol.getDatosNuevoExpediente().getDomicilio());
        if(res.getCorrecto()){
            rol.setDomicilio(res.getValor());
            mostrarMensajeResultadoEJB(res);
            actualizarMediosComunicacion();
        }else mostrarMensajeResultadoEJB(res);  
    }
    
    public void actualizarMediosComunicacion() {
        ResultadoEJB<MedioComunicacion> res = ejbIntegracionExpedienteTitulacion.actualizarMediosComunicacion(rol.getDatosNuevoExpediente().getMedioComunicacion());
        if(res.getCorrecto()){
            rol.setMedioComunicacion(res.getValor());
            mostrarMensajeResultadoEJB(res);
            rol.setPasoRegistroInd("Domicilio y Comunicaciones");
            actualizarExpediente();
            actualizarDatosAcademicos();
        }else mostrarMensajeResultadoEJB(res); 
    }
    
    public void actualizarDatosAcademicos() {
        ResultadoEJB<DatosAcademicos> res = ejbIntegracionExpedienteTitulacion.actualizarDatosAcademicos(rol.getDatosNuevoExpediente().getDatosAcademicos());
        if(res.getCorrecto()){
            rol.setDatosAcademicos(res.getValor());
            mostrarMensajeResultadoEJB(res);
            rol.setPasoRegistroInd("Antecedentes Académicos");
            actualizarExpediente();
            Messages.addGlobalWarn("El expediente se ha registrado corrextamente, con la clave: " + rol.getExpedienteTitulacion().getExpediente());
            rol.setExisteExpediente(Boolean.TRUE);
            init();
            Ajax.update("frm");
        }else mostrarMensajeResultadoEJB(res);  
    }
}
