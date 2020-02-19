/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;



import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstudianteRegistro;
import mx.edu.utxj.pye.titulacion.interfaces.EjbTitulacionSeguimiento;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
/**
 *
 * @author UTXJ
 */
@Named(value = "controladorFotoExp")
@ManagedBean
@SessionScoped
public class ControladorFotoExpediente implements Serializable{
    
    
    @Getter @Setter private String rutaFotografia;
    @Getter @Setter private StreamedContent graphicImage;
    @EJB private EjbTitulacionSeguimiento ejbTitulacionSeguimiento;
    @EJB private EjbEstudianteRegistro ejbEstudianteRegistro;
    @Inject ControladorTitSegGeneracion controladorTitSegGeneracion;
    
     public void mostrarFotografiaING(Integer expediente) {
        try {
            rutaFotografia = ejbTitulacionSeguimiento.buscarFotografiaING(expediente);
            if (rutaFotografia == null) {
                rutaFotografia = "C:\\archivos\\formatosTitulacion\\sinFotografia.png";
            }
            graphicImage = new DefaultStreamedContent(new FileInputStream(rutaFotografia), "image/jpg");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFotoExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     public void mostrarFotografiaTSU(Integer expediente) {
        try {
            rutaFotografia = ejbTitulacionSeguimiento.buscarFotografiaTSU(expediente);
            if (rutaFotografia == null) {
                rutaFotografia = "C:\\archivos\\formatosTitulacion\\sinFotografia.png";
            }
            graphicImage = new DefaultStreamedContent(new FileInputStream(rutaFotografia), "image/jpg");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorFotoExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void eliminarFotografiaSegGen(DocumentosExpediente docsExp){
        Boolean eliminado = ejbEstudianteRegistro.eliminarDocumentosEnExpediente(docsExp);
        if(eliminado){ 
            controladorTitSegGeneracion.consultarExpediente(docsExp.getExpediente().getExpediente());
            Ajax.update("frmDocsExp");
            Ajax.update("frmDocExp");
            Ajax.update("frmFotoExp");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
}
