/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoTitulacionEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoExpedienteGeneracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class CargaDocumentosExpSegMatriculaTitulacion implements Serializable{

    private static final long serialVersionUID = 4360567392654145683L;
    
    // Variable para documentos  
    @Getter @Setter private Integer claveDocumento;
    @Getter @Setter private Part file, fileCURP, fileAN, fileCB, fileAEET, fileCSST, fileCET, fileAET, fileFotoT, fileAEEIL, fileCSSIL, fileCEIL, fileAEIL, fileFotoIL, fileCTT;  
    @Getter @Setter private DtoExpedienteTitulacion dtoExpedienteTitulacion;
    @Getter @Setter private DtoDocumentoTitulacionEstudiante dtoDocumentoTitulacionEstudiante;
    @Getter @Setter private Boolean aperturaDialogo = false, valorValidacion = false;
    
    @EJB private EjbIntegracionExpedienteTitulacion ejbIntegracionExpedienteTitulacion;
    @EJB private EjbSeguimientoExpedienteGeneracion ejbSeguimientoExpedienteGeneracion;
    
    @Inject LogonMB logonMB;
    @Inject UtilidadesCH utilidadesCH;
    @Inject SeguimientoExpedienteMatriculaTitulacion seguimientoExpedienteMatriculaTitulacion;
    
    public void seleccionarDocumento(DtoDocumentoTitulacionEstudiante registro) {
        dtoDocumentoTitulacionEstudiante = registro;
    }

    public Boolean docExiste(Integer claveDoc, DtoExpedienteTitulacion dto) {
        setDtoExpedienteTitulacion(dto);
        DocumentoExpedienteTitulacion docExp = ejbSeguimientoExpedienteGeneracion.buscarExtDocExpediente(claveDoc, dtoExpedienteTitulacion.getExpediente()).getValor();
        
        if(docExp == null){
            return false;
        }else{
            return true;  
        }
    }
    
     public void subirDocumentoCURP() {
        try {
            claveDocumento = 14;
            
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileCURP, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirDocumentoActNac(){
            try {
            claveDocumento = 15;
            
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileAN, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void subirDocumentoCertBach(){
            try {
            claveDocumento = 16;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileCB, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void subirDocumentoAcredEstIL(){
            try {
            claveDocumento = 24;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileAEIL, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoFotoIL(){
            try {
            claveDocumento = 25; 
            
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileFotoIL, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoFotoTSU(){
            try {
            claveDocumento = 18;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileFotoT, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoActExenIL(){
            try {
            claveDocumento = 26;
            
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileAEEIL, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoActExenTSU(){
            try {
            claveDocumento = 19;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileAEET, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoConsServIL(){
            try {
            claveDocumento = 27;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileCSSIL, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoConsServTSU(){
            try {
            claveDocumento = 20;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileCSST, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoCertEstIL(){
            try {
            claveDocumento = 28;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileCEIL, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoCertEstTSU(){
            try {
            claveDocumento = 21;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileCET, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoCopiaTituloTSU(){
            try {
            claveDocumento = 31;    
                
            String matricula = Integer.toString(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula()); 

            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDocumento);
           
            DocumentoExpedienteTitulacion documentoExpedienteTit = new DocumentoExpedienteTitulacion();
            documentoExpedienteTit.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTit.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTit.setRuta(utilidadesCH.agregarDocExpTit(fileCTT, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTit.setFechaCarga(new Date());
            documentoExpedienteTit.setObservaciones("Sin revisar");
            documentoExpedienteTit.setValidado(false);
            documentoExpedienteTit.setFechaValidacion(null);
            documentoExpedienteTit = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTit);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void eliminarDocumento(DocumentoExpedienteTitulacion docsExp){
         try {
            Boolean eliminado = ejbIntegracionExpedienteTitulacion.eliminarDocumentosEnExpediente(docsExp);
            if(!eliminado){ 
                Ajax.update("frmDocsExp");
                 seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
                Messages.addGlobalInfo("El documento se eliminó correctamente.");
            }else Messages.addGlobalError("El documento no ha podido eliminarse.");
            
         } catch (Throwable ex) {
             Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
             Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    public void descargarDocumento(DocumentoExpedienteTitulacion docsExp) throws IOException{
        File f = new File(docsExp.getRuta());
        Faces.sendFile(f, false);
    }
    
     public void subirDocumento() {
        try {
            String nivel = "";
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5A") ) {
                nivel = "ING";
            } else if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals( "TSU")) {
                nivel = "TSU";
            }
            if (dtoExpedienteTitulacion.getExpediente().getEvento().getNivel().equals("5B")) {
                nivel = "LIC";
            }
            
            String generacion = dtoExpedienteTitulacion.getGeneracion().getInicio() + "-" + dtoExpedienteTitulacion.getGeneracion().getFin();
            
            DocumentoExpedienteTitulacion nuevoDocumento = new DocumentoExpedienteTitulacion();

            nuevoDocumento.setExpediente(dtoExpedienteTitulacion.getExpediente());
            nuevoDocumento.setDocumento(dtoDocumentoTitulacionEstudiante.getDocumentoProceso().getDocumento());
            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + String.valueOf(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula());
            nuevoDocumento.setRuta(utilidadesCH.agregarDocExpTit(file, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, dtoDocumentoTitulacionEstudiante.getDocumentoProceso().getDocumento().getNomenclatura(), String.valueOf(dtoExpedienteTitulacion.getExpediente().getMatricula().getMatricula())));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin revisar");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(nuevoDocumento);
            Ajax.update("frmDocsExp");
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editarRegistro(DtoDocumentoTitulacionEstudiante registro){
        dtoDocumentoTitulacionEstudiante = registro;
        valorValidacion = dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().getValidado();
        Ajax.update("frmModalEdicion");
        Ajax.oncomplete("skin();");
        aperturaDialogo = Boolean.TRUE;
        forzarAperturaEdicionDialogo();
    }
    
     public void forzarAperturaEdicionDialogo(){
        if(getAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicion').show();");
            aperturaDialogo = Boolean.FALSE;
        }
    }
     
     public void guardarEdicion() {
        try {
            if(dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().getValidado()== true && "Sin revisar".equals(dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().getObservaciones())){
                dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().setObservaciones("Listo");
            }
            if(dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().getValidado() == false && "Sin revisar".equals(dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().getObservaciones())){
                dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().setObservaciones("No válido");
            }
            dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion().setFechaValidacion(new Date());
            dtoDocumentoTitulacionEstudiante.setDocumentoExpedienteTitulacion(ejbIntegracionExpedienteTitulacion.actualizarDocumentoExpediente(dtoDocumentoTitulacionEstudiante.getDocumentoExpedienteTitulacion()));
            seguimientoExpedienteMatriculaTitulacion.mostrarExpediente();
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
