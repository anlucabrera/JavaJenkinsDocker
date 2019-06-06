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
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
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
@Named(value = "controladorArchExp")
@ManagedBean
@ViewScoped
public class ControladorArchivosExpediente implements Serializable{

    private static final long serialVersionUID = -3541916697043304041L;
    
     
    // Variable para documentos  
    @Getter private Boolean estOnceavo;
    @Getter private String matricula;
    @Getter @Setter private Alumnos estudiante;
    
    @Getter @Setter private Integer claveDoc;
    @Getter @Setter private Part fileC, fileAN, fileCB, fileAEE, fileCSS, fileCEIL, fileAE, fileFotoIL, fileCTTSU, fileLibSCLTF; 
    
    @EJB private EjbEstudianteRegistro ejbEstudianteRegistro;
    
    @Inject LogonMB logonMB;
    @Inject UtilidadesCH utilidadesCH;
    @Inject ControladorEstudianteRegistro controladorEstudianteRegistro;
    @Inject ControladorArchivosExpediente controladorArchivosExpediente;
    
    @Getter @Setter private DocumentosExpediente nuevoOBJdocExp;
    @Getter @Setter private Egresados nuevoOBJegresado;
    
    @Getter @Setter private List<DocumentosExpediente> listaDocsExp;
    
    @Getter @Setter private ExpedientesTitulacion expediente;
    
    @Getter @Setter private Boolean existeAN;
    
    @PostConstruct
    public void init() {
        try {

            matricula = controladorEstudianteRegistro.getMatricula();
            estudiante = controladorEstudianteRegistro.getEstudiante();
            
            cargarDocumentosPorExpediente();
           
        } catch (Exception e) {
           
        }
        
    }
    
    public void subirDocumentoCURP() {
        try {
            claveDoc = 1;
            nuevoOBJdocExp = new DocumentosExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileC, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente(); 
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileAN, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente(); 
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCB, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente(); 
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileAE, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente(); 
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileFotoIL, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente(); 
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileAEE, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente();
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCSS, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente();
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCEIL, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente();
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);  
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileCTTSU, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente();
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);  
            
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

            ExpedientesTitulacion exp = new ExpedientesTitulacion();
            exp = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
            Short gen = exp.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (exp.getNivel() == 2) {
                nivel = "ING";
            } else if (exp.getNivel() == 1) {
                nivel = "TSU";
            }
            if (exp.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJdocExp.setExpediente(exp);
            nuevoOBJdocExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJdocExp.setRuta(utilidadesCH.agregarDocExpTit(fileLibSCLTF, generacion , nivel, exp.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura()));
            nuevoOBJdocExp.setFechaCarga(new Date());
            nuevoOBJdocExp.setObservaciones("Sin revisar");
            nuevoOBJdocExp.setFechaValidacion(null);
            nuevoOBJdocExp.setValidadoTitulacion(false);
            nuevoOBJdocExp = ejbEstudianteRegistro.guardarDocumentoExpediente(nuevoOBJdocExp);
            
            cargarDocumentosPorExpediente();
            Ajax.update("frmDocsExp");
            controladorEstudianteRegistro.setPestaniaActiva(3);  
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivosExpediente.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void cargarDocumentosPorExpediente(){
        listaDocsExp = ejbEstudianteRegistro.getListaDocumentosPorRegistro(ejbEstudianteRegistro.obtenerClaveExpediente(estudiante));
        expediente = ejbEstudianteRegistro.obtenerClaveExpediente(estudiante);
        if(controladorEstudianteRegistro.getValDatPer() == true){
        controladorEstudianteRegistro.progressBarDocumentos(listaDocsExp, expediente);
        }else{
         controladorEstudianteRegistro.setProgresoExpediente(0);
        }
        Ajax.update("frmDocsExp");
//        Ajax.update("frmDocExp");
        Ajax.update("frmFotoExp");
    }
    
    public void eliminarDocumento(DocumentosExpediente docsExp){
        Boolean eliminado = ejbEstudianteRegistro.eliminarDocumentosEnExpediente(docsExp);
        if(eliminado){ 
            cargarDocumentosPorExpediente();
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
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
 
}
