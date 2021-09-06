/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoTitulacionEstudiante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoExpedienteGeneracion;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@SessionScoped
public class SeguimientoExpedienteIndividualTitulacion implements Serializable{

    private static final long serialVersionUID = 2501719871589905473L;
   
    @Getter @Setter DtoExpedienteTitulacion dtoExpedienteTitulacion;
    @Getter @Setter Personal personalTitulacion;
    @Getter @Setter private StreamedContent graphicImage;
    @Getter @Setter private String rutaFotografia;
    
    @Getter @Setter private List<DtoDocumentoTitulacionEstudiante> listaDocsExp;
    
    @Inject LogonMB logonMB;
    @Inject CargaDocumentosExpSegGeneracionTitulacion cargaDocumentosExpedienteTitulacion;
    
    @EJB EjbSeguimientoExpedienteGeneracion ejbSeguimientoExpedienteGeneracion;
    @EJB EjbIntegracionExpedienteTitulacion  ejbIntegracionExpedienteTitulacion;
    
    @PostConstruct
    public void init(){
        
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        try{
            ResultadoEJB<Filter<PersonalActivo>> resAcceso = ejbSeguimientoExpedienteGeneracion.validarTitulacion(logonMB.getPersonal().getClave());//validar si es personal de servicios escolares
            if(!resAcceso.getCorrecto()){;return;}//cortar el flujo si no se pudo verificar el acceso

            Filter<PersonalActivo> filtro = resAcceso.getValor();//se obtiene el filtro resultado de la validación
            PersonalActivo personal = filtro.getEntity();//ejbPersonalBean.pack(logon.getPersonal());
            
            personalTitulacion = personal.getPersonal();
           
        }catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(SeguimientoExpedienteIndividualTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    
     public void consultarExpediente(DtoExpedienteTitulacion dtoExpedienteTitulacion) {
        ResultadoEJB<DtoExpedienteTitulacion> res = ejbIntegracionExpedienteTitulacion.getDtoExpedienteTitulacion(dtoExpedienteTitulacion.getExpediente());
        if(res.getCorrecto()){
            setDtoExpedienteTitulacion(res.getValor());
            mostrarFotografia(dtoExpedienteTitulacion);
            obtenerListaDocumentosExpediente();
            Ajax.update("formMuestraExpediente");
            Ajax.update("tblDocsExp");
            Ajax.update("frmDocsExp");
            Ajax.update("frmDocExp");
            Ajax.update("frmFotoExp");
             
        } 
    }
     
     public void mostrarFotografia(DtoExpedienteTitulacion dtoExpedienteTitulacion) {
        try {
            rutaFotografia = ejbSeguimientoExpedienteGeneracion.buscarFotografia(dtoExpedienteTitulacion).getValor();
            graphicImage = new DefaultStreamedContent(new FileInputStream(rutaFotografia), "image/jpg");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(SeguimientoExpedienteIndividualTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    
    public void validarExpediente(){
        ResultadoEJB<ExpedienteTitulacion> res = ejbSeguimientoExpedienteGeneracion.validarExpediente(dtoExpedienteTitulacion.getExpediente(), personalTitulacion);
        if(res.getCorrecto()){
            consultarExpediente(dtoExpedienteTitulacion);
        }
    }
    
    public void actualizarFechaTerminacion() {
        ResultadoEJB<DatosAcademicos> res = ejbIntegracionExpedienteTitulacion.actualizarDatosAcademicos(dtoExpedienteTitulacion.getDatosAcademicos());
        if(res.getCorrecto()){
            Messages.addGlobalInfo("Se actualizó la fecha de terminación correctamente.");
            consultarExpediente(dtoExpedienteTitulacion);
        }
    }
    
    public void guardarDatosTitulacion() {
        ResultadoEJB<ExpedienteTitulacion> res = ejbIntegracionExpedienteTitulacion.actualizarDatosTitulacion(dtoExpedienteTitulacion.getExpediente());
        if(res.getCorrecto()){
            Messages.addGlobalInfo("Se actualizó el promedio y el dato de servicio social correctamente.");
            consultarExpediente(dtoExpedienteTitulacion);
        }
    }
    
     public void obtenerListaDocumentosExpediente() {
        ResultadoEJB<List<DtoDocumentoTitulacionEstudiante>> res = ejbSeguimientoExpedienteGeneracion.obtenerListaDocumentosExpediente(dtoExpedienteTitulacion.getExpediente());
        if(res.getCorrecto()){
            setListaDocsExp(res.getValor());
            Ajax.update("tblDocsExp");
            Ajax.update("frmDocsExp");
            Ajax.update("frmDocExp");
        } 
    }
 
     
}
