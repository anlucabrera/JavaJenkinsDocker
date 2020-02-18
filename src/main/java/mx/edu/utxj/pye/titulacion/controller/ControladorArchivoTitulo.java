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
import mx.edu.utxj.pye.sgi.entity.titulacion.TituloExpediente;
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
@Named(value = "controladorArchTitulo")
@ManagedBean
@ViewScoped
public class ControladorArchivoTitulo implements Serializable{

    private static final long serialVersionUID = 1L;

    // Variable para documentos  
    @Getter private String matricula;
    
    @Getter @Setter private Integer claveDoc;
    @Getter @Setter private Part fileTitTSU, fileTitIL; 
   
    @Inject LogonMB logonMB;
    @Inject UtilidadesCH utilidadesCH;
    @Inject ControladorTitSegMatricula controladorTitSegMatricula;
    @Inject ControladorFotoExpediente controladorFotoExpediente;
    
    @EJB private EjbEstudianteRegistro ejbEstudianteRegistro;
    
    @Getter @Setter private DocumentosExpediente nuevoOBJdocExp;
    @Getter @Setter private TituloExpediente nuevoOBJtitExp;
    @Getter @Setter private Egresados nuevoOBJegresado;
    
    @Getter @Setter private List<DocumentosExpediente> listaDocsExp;
    
    @Getter @Setter private ExpedientesTitulacion expediente;
    
    
    @PostConstruct
    public void init() {
        matricula = controladorTitSegMatricula.getNuevoDtoExpMat().getExpedientesTitulacion().getMatricula().getMatricula();
        expediente = controladorTitSegMatricula.getNuevoDtoExpMat().getExpedientesTitulacion();
    }
   
    public void subirDocumentoTitTSU(){
            try {
            claveDoc = 10;
            nuevoOBJtitExp = new TituloExpediente();
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

            nuevoOBJtitExp.setExpediente(expediente);
            nuevoOBJtitExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJtitExp.setRuta(utilidadesCH.agregarTitExpTit(fileTitTSU, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJtitExp.setFechaCarga(new Date());
            nuevoOBJtitExp.setFechaEmision(new Date());
            nuevoOBJtitExp = ejbEstudianteRegistro.guardarTituloExpediente(nuevoOBJtitExp);
            
            controladorTitSegMatricula.mostrarExpedienteValidado();
            Ajax.update("frmTitExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivoTitulo.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
   
    public void subirDocumentoTitIL(){
            try {
            claveDoc = 17;
            nuevoOBJtitExp = new TituloExpediente();
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

            nuevoOBJtitExp.setExpediente(expediente);
            nuevoOBJtitExp.setDocumento(doc);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJtitExp.setRuta(utilidadesCH.agregarTitExpTit(fileTitIL, generacion , nivel, expediente.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expediente.getMatricula().getMatricula()));
            nuevoOBJtitExp.setFechaCarga(new Date());
            nuevoOBJtitExp.setFechaEmision(new Date());
            nuevoOBJtitExp = ejbEstudianteRegistro.guardarTituloExpediente(nuevoOBJtitExp);
            
            controladorTitSegMatricula.mostrarExpedienteValidado();
            Ajax.update("frmTitExp");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorArchivoTitulo.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void descargarTitulo(TituloExpediente titExp) throws IOException{
        File f = new File(titExp.getRuta());
        Faces.sendFile(f, false);
    }
    
    public void eliminarTitulo(TituloExpediente titExp){
        Boolean eliminado = ejbEstudianteRegistro.eliminarTituloExpediente(titExp);
        if (eliminado) {
            controladorTitSegMatricula.mostrarExpedienteValidado();
            Ajax.update("frmTitExp");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        } else {
            Messages.addGlobalError("El documento no ha podido eliminarse.");
        }
    }
    
}
