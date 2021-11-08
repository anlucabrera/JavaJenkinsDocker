/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Map;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.IntegracionExpedienteTitulacionRolEstudiante;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class IntegracionExpedienteTitulacionEstudiante extends ViewScopedRol implements Desarrollable{
    
    @EJB EjbIntegracionExpedienteTitulacion ejb;
    @EJB EJBSelectItems eJBSelectItems;
    @EJB EjbSelectItemCE ejbItemCE;
    @EJB EjbPropiedades ep;
    @Inject LogonMB logonMB;
    @Getter Boolean tieneAcceso = false;
    
    @Getter @Setter IntegracionExpedienteTitulacionRolEstudiante rol = new IntegracionExpedienteTitulacionRolEstudiante();
   
    /**
     * Inicializa:<br/>
     *      El filtro por estudiante de la generacion 2019 - 2021<br/>
     *      El DTO del rol<br/>
     */
 
    @Getter private Boolean cargado = false;

    @PostConstruct
    public void init(){
        try{
            
            if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE19)) return;
            cargado = true;
            setVistaControlador(ControlEscolarVistaControlador.INTEGRACION_EXPEDIENTE_TITULACION);
            ResultadoEJB<Estudiante> resAcceso = ejb.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));//validar si es director
            if(!resAcceso.getCorrecto()){ mostrarMensajeResultadoEJB(resAcceso);return;}//cortar el flujo si no se pudo verificar el acceso

            ResultadoEJB<Estudiante> resValidacion = ejb.validaEstudiante(Integer.parseInt(logonMB.getCurrentUser()));
            if(!resValidacion.getCorrecto()){ mostrarMensajeResultadoEJB(resValidacion);return; }//cortar el flujo si no se pudo validar

            Estudiante estudiante = resValidacion.getValor();//se obtiene el filtro resultado de la validación
            if (estudiante != null) {
                tieneAcceso = true;
            }
            if(!tieneAcceso){mostrarMensajeNoAcceso(); return;} //cortar el flujo si no tiene acceso
            
            rol.setEstudiante(estudiante);
            rol.setPeriodoActivo(ejb.getPeriodoActual().getPeriodo());
            ResultadoEJB<EventoTitulacion> resEvento = ejb.verificarEvento(estudiante);
            if(!resEvento.getCorrecto()) tieneAcceso = false;//debe negarle el acceso si no hay un periodo activo para que no se cargue en menú
            // ----------------------------------------------------------------------------------------------------------------------------------------------------------
            if(verificarInvocacionMenu()) return;//detener el flujo si la invocación es desde el menu para impedir que se ejecute todo el proceso y eficientar la  ejecución
            if(!resEvento.getCorrecto()) mostrarMensajeResultadoEJB(resEvento);
            rol.setNivelRol(NivelRol.OPERATIVO);
            rol.setEventoActivo(resEvento.getValor());
            existeExpedienteTitulacion();

            rol.getInstrucciones().add("Es importante que sigas con las instrucciones que te irá mostrando el sistema.");
            rol.getInstrucciones().add("Verifica que tu información sea correcta.");
            rol.getInstrucciones().add("Ten a la tu certificado de bachillerato (copia, imagen, etc) para consultar la fecha de emisión.");
            
            
        }catch (Exception e){ }
    }

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "integracion expediente titulacion";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }
    
    public void existeExpedienteTitulacion() {
        ResultadoEJB<ExpedienteTitulacion> res = ejb.buscarExpedienteRegistrado(rol.getEstudiante());
        if(res.getCorrecto()){
            if (res.getValor() != null) {
                rol.setExisteExpediente(true);
                rol.setExpedienteRegistrado(res.getValor());
                rol.setPasoRegistro(rol.getExpedienteRegistrado().getPasoRegistro());
                progresoRegistro();
                pestaniaRegistro();
                crearDtoExpedienteTitulacion();
                Ajax.update("contenedorExp");
            } else {
                rol.setExisteExpediente(false);
                rol.setProgresoExpediente(0);
                rol.setPestaniaActiva(0);
            }
        }
        rol.setListaEstadosDomicilioRadica(eJBSelectItems.itemEstados());
        selectMunicipio();
        selectAsentamiento();
        
        rol.setListaEstadosIEMS(eJBSelectItems.itemEstados());
        selectMunicipioIems();
        selectLocalidadIems();
        selectIems();
    }
    
    public void iniciarIntegracionExpediente() {
       ResultadoEJB<ExpedienteTitulacion> res = ejb.guardarExpedienteTitulacion(rol.getEventoActivo(), rol.getEstudiante());
        if(res.getCorrecto()){
            existeExpedienteTitulacion();
            mostrarMensajeResultadoEJB(res);
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void crearDtoExpedienteTitulacion() {
        ResultadoEJB<DtoExpedienteTitulacion> res = ejb.getDtoExpedienteTitulacion(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setDtoExpedienteTitulacion(res.getValor());
            buscarDocumentosExpediente();
//            mostrarMensajeResultadoEJB(res);
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void buscarDocumentosExpediente(){
        ResultadoEJB<List<DocumentoExpedienteTitulacion>> res = ejb.buscarDocumentosExpediente(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setListaDocExpTit(res.getValor());
            if(rol.getListaDocExpTit().size() > 0)
            {
                rol.setProgresoExpediente(100);
                rol.setExisteDocs(true);
            } else {
                progresoRegistro();
                pestaniaRegistro();
                rol.setExisteDocs(false);
            }
//            mostrarMensajeResultadoEJB(res);
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void validarDatosPersonales() {
       ResultadoEJB<ExpedienteTitulacion> res = ejb.validarDatosPersonales(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setExpedienteRegistrado(res.getValor());
            existeExpedienteTitulacion();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frmExpTitDP");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void validarDomCom() {
       actualizarDomCom();
       ResultadoEJB<ExpedienteTitulacion> res = ejb.validarDomCom(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setExpedienteRegistrado(res.getValor());
            existeExpedienteTitulacion();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frmExpTitCD");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void validarAntAcademicos() {
       actualizarAntAcademicos();
       ResultadoEJB<ExpedienteTitulacion> res = ejb.validarAntAcademicos(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setExpedienteRegistrado(res.getValor());
            existeExpedienteTitulacion();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frmExpTitAA");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void finalizarIntegracion() {
       ResultadoEJB<ExpedienteTitulacion> res = ejb.finalizarIntegracion(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setExpedienteRegistrado(res.getValor());
            existeExpedienteTitulacion();
            mostrarMensajeResultadoEJB(res);
            Ajax.update("frmExpTitFin");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void pestaniaRegistro() {
       ResultadoEJB<Integer> res = ejb.getPestaniaRegistro(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setPestaniaActiva(res.getValor());
//            mostrarMensajeResultadoEJB(res);
            Ajax.update("frmExpTitDP");
            Ajax.update("frmExpTitCD");
            Ajax.update("frmExpTitAA");
            Ajax.update("frmFotoExpTSU");
            Ajax.update("frmFotoExpIL");
            Ajax.update("frmDocsExp");
            Ajax.update("frmExpTitFin");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void progresoRegistro() {
       ResultadoEJB<Integer> res = ejb.getProgresoRegistro(rol.getExpedienteRegistrado());
        if(res.getCorrecto()){
            rol.setProgresoExpediente(res.getValor());
//            mostrarMensajeResultadoEJB(res);
            Ajax.update("frmExpTitDP");
            Ajax.update("frmExpTitCD");
            Ajax.update("frmExpTitAA");
            Ajax.update("frmFotoExpTSU");
            Ajax.update("frmFotoExpIL");
            Ajax.update("frmDocsExp");
        }else mostrarMensajeResultadoEJB(res);
    }
    
    public void actualizarDomCom() {
        ResultadoEJB<MedioComunicacion> resCom = ejb.actualizarMediosComunicacion(rol.getDtoExpedienteTitulacion().getMedioComunicacion());
        ResultadoEJB<Domicilio> resDom = ejb.actualizarDomicilio(rol.getDtoExpedienteTitulacion().getDomicilio());
        if(resCom.getCorrecto() && resDom.getCorrecto()){
            rol.setMedioComunicacion(resCom.getValor());
            rol.setDomicilio(resDom.getValor());
            mostrarMensajeResultadoEJB(resCom);
            mostrarMensajeResultadoEJB(resDom);
            existeExpedienteTitulacion();
            rol.setPestaniaActiva(1);
            Ajax.update("frmExpTitCD");
        }else mostrarMensajeResultadoEJB(resCom); mostrarMensajeResultadoEJB(resDom); 
    }
  
    /* Combos para domicilio */
    public void selectMunicipio(){
        rol.setListaMunicipiosRadica(eJBSelectItems.itemMunicipiosByClave(this.rol.getDtoExpedienteTitulacion().getDomicilio().getIdEstado()));
    }
    
    public void selectAsentamiento(){
        rol.setListaAsentamientos(eJBSelectItems.itemAsentamientoByClave(rol.getDtoExpedienteTitulacion().getDomicilio().getIdEstado(), rol.getDtoExpedienteTitulacion().getDomicilio().getIdMunicipio()));
    }
  
    /* Combos para IEMS */
    public void selectMunicipioIems(){
        rol.setListaMunicipiosIEMS(eJBSelectItems.itemMunicipiosByClave(this.rol.getDtoExpedienteTitulacion().getIems().getLocalidad().getMunicipio().getEstado().getIdestado()));
    }
    
    public void selectLocalidadIems(){
        rol.setListaLocalidadesIEMS(eJBSelectItems.itemLocalidadesIEMSByClave(this.rol.getDtoExpedienteTitulacion().getIems().getLocalidad().getMunicipio().getEstado().getIdestado(),this.rol.getDtoExpedienteTitulacion().getIems().getLocalidad().getMunicipio().getMunicipioPK().getClaveMunicipio()));
        selectIems();
    }
    
    public void selectIems(){
        rol.setListaIEMS(ejbItemCE.itemIems(this.rol.getDtoExpedienteTitulacion().getIems().getLocalidad().getMunicipio().getEstado().getIdestado(),this.rol.getDtoExpedienteTitulacion().getIems().getLocalidad().getMunicipio().getMunicipioPK().getClaveMunicipio(), this.rol.getDtoExpedienteTitulacion().getIems().getLocalidad().getLocalidadPK().getClaveLocalidad()));  
    }
    
    public void actualizarAntAcademicos() {
        ResultadoEJB<DatosAcademicos> res = ejb.actualizarDatosAcademicos(rol.getDtoExpedienteTitulacion().getDatosAcademicos());
        if(res.getCorrecto()){
            rol.setDatosAcademicos(res.getValor());
            mostrarMensajeResultadoEJB(res);
            existeExpedienteTitulacion();
            rol.setPestaniaActiva(2);
            Ajax.update("frmExpTitAA");
        }else mostrarMensajeResultadoEJB(res); 
    }
    
    public void actualizarIEMS(){
        selectIems();
        Ajax.update("frmExpTitAA");
        rol.setPestaniaActiva(2);
    }
    
    public void consultarStatusDocumento(Integer claveDoc){
        ResultadoEJB<Boolean> res = ejb.consultarStatusDocumento(claveDoc);
        if(res.getCorrecto()){
            rol.setValDoc(res.getValor());
//            mostrarMensajeResultadoEJB(res);
        }else mostrarMensajeResultadoEJB(res);
    }
}
