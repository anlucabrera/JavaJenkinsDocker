/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.titulacion.Documentos;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstudianteRegistro;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "controladorArchTit")
@ManagedBean
@ViewScoped
public class ControladorArchivosTitulacion implements Serializable{

    private static final long serialVersionUID = -3541916697043304041L;
    
     
    // Variable para documentos  
    @Getter private String matricula;
    
    @Getter @Setter private Integer claveDoc;
    @Getter @Setter private Part fileC, fileAN, fileCB, fileAEE, fileCSS, fileCEIL, fileAE, fileFotoIL, fileCTTSU, fileLibSCLTF, fileFotoTSU, fileAEETSU, fileCSSTSU, fileCETSU; 
   
    @Inject LogonMB logonMB;
    @Inject UtilidadesCH utilidadesCH;
    @Inject ControladorTitSegGeneracion controladorTitSegGeneracion;
    @Inject ControladorFotoExpediente controladorFotoExpediente;
    
    @EJB private EjbEstudianteRegistro ejbEstudianteRegistro;
    
    @Getter @Setter private DocumentosExpediente nuevoOBJdocExp;
    @Getter @Setter private Egresados nuevoOBJegresado;
    
    @Getter @Setter private List<DocumentosExpediente> listaDocsExp;
    
    @Getter @Setter private ExpedientesTitulacion expediente;
    
    
    @PostConstruct
    public void init() {
        matricula = controladorTitSegGeneracion.getNuevoDtoExpMat().getExpedientesTitulacion().getMatricula().getMatricula();
        expediente = controladorTitSegGeneracion.getNuevoDtoExpMat().getExpedientesTitulacion();
        controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
    }
   
    public void eliminarDocumento(DocumentosExpediente docsExp){
        if (docsExp.getDocumento().getDocumento() != 12 && docsExp.getDocumento().getDocumento() != 5) {
            Boolean eliminado = ejbEstudianteRegistro.eliminarDocumentosEnExpediente(docsExp);
            if (eliminado) {
                controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
                Ajax.update("frmDocsExp");
                Ajax.update("frmDocExp");
                Ajax.update("frmFotoExp");
                Messages.addGlobalInfo("El documento se eliminó correctamente.");
            } else {
                Messages.addGlobalError("El documento no ha podido eliminarse.");
            }
        } else {
            controladorFotoExpediente.eliminarFotografiaSegGen(docsExp);
            Ajax.update("frmDocsExp");
            Ajax.update("frmDocExp");
            Ajax.update("frmFotoExp");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }
    }
    
    public void descargarDocumento(DocumentosExpediente docsExp) throws IOException{
        File f = new File(docsExp.getRuta());
        Faces.sendFile(f, false);
    }
    
    public Boolean docExiste(Integer tipoDoc, Integer expediente) {
        if(ejbEstudianteRegistro.docExisteEnExpediente(tipoDoc, expediente) == null){
            return false;
        }else{
            return true;  
        }
    }
    
    public void subirDocumentoCURP() {
        try {
            claveDoc = 1;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileC, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void subirDocumentoActNac(){
            try {
            claveDoc = 2;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileAN, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }
    
    public void subirDocumentoCertBach(){
            try {
            claveDoc = 3;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCB, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void subirDocumentoAcredEstIL(){
            try {
            claveDoc = 11;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
 
            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileAE, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente()); 
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoFotoIL(){
            try {
            claveDoc = 12;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileFotoIL, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoFotoTSU(){
            try {
            claveDoc = 5;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "TSU";
            
            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileFotoTSU, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoActExenIL(){
            try {
            claveDoc = 13;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileAEE, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoActExenTSU(){
            try {
            claveDoc = 6;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileAEETSU, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoConsServIL(){
            try {
            claveDoc = 14;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCSS, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoConsServTSU(){
            try {
            claveDoc = 7;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCSSTSU, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoCertEstIL(){
            try {
            claveDoc = 15;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCEIL, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoCertEstTSU(){
            try {
            claveDoc = 8;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCETSU, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoCopiaTituloTSU(){
            try {
            claveDoc = 18;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
         
            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCTTSU, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoLibSCLTF(){
            try {
            claveDoc = 19;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);
            
            Short gen = expediente.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expediente.getNivel() == 2) {
                nivel = "ING";
            } else if (expediente.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expediente.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(expediente);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileLibSCLTF, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            controladorTitSegGeneracion.consultarExpediente(expediente.getExpediente());
            Ajax.update("frmDocsExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
   
}
