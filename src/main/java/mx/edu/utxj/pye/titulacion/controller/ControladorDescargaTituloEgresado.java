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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.titulacion.Documentos;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.TituloExpediente;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import mx.edu.utxj.pye.titulacion.interfaces.EjbEstudianteRegistro;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author UTXJ
 */
@Named(value = "controladorTitEgresado")
@ManagedBean
@ViewScoped
public class ControladorDescargaTituloEgresado implements Serializable{

    private static final long serialVersionUID = 5938343366127533334L;
    
    @Getter @Setter private String matricula;
    @Getter @Setter private ExpedientesTitulacion expedienteTitulacion;
    @Getter @Setter private TituloExpediente tituloExpediente;
    @Getter @Setter private Boolean existeTitulo = false, expedienteValidado = false;
    @Getter @Setter private Date fechaEmisionTit;
    
    @EJB private EjbEstudianteRegistro ejbEstudianteRegistro;
    
    @Inject LogonMB logonMB;
    
    @Inject UtilidadesCH utilidadesCH;
    
    @Getter @Setter private Integer claveDoc;
    @Getter @Setter private Part fileTitTSU, fileTitIL;
    @Getter @Setter private TituloExpediente nuevoOBJtitExp;
    @Getter @Setter private Egresados nuevoOBJegresado;
    
    @PostConstruct
    public void init() {
        try {
            matricula = logonMB.getCurrentUser();
            expedienteTitulacion = ejbEstudianteRegistro.consultarStatusExpediente(matricula);
            
            if (expedienteTitulacion.getValidado()) {
                expedienteValidado = true;
            } else {
                expedienteValidado = false;
            }
            
            tituloExpediente = ejbEstudianteRegistro.buscarTituloRegistrado(expedienteTitulacion);
            if (tituloExpediente == null) {
                fechaEmisionTit = new Date();
                existeTitulo = false;
            } else {
                existeTitulo = true;
            }
            
        } catch (Exception e) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + e.getMessage());
            Logger.getLogger(ControladorDescargaTituloEgresado.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
     /**
     * Permite guardar el registro y el archivo correspondente a título del egresado de nivel de T.S.U.
     */
    
    public void subirDocumentoTitTSU(){
            try {
            claveDoc = 10;
            nuevoOBJtitExp = new TituloExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expedienteTitulacion.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expedienteTitulacion.getNivel() == 2) {
                nivel = "ING";
            } else if (expedienteTitulacion.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expedienteTitulacion.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJtitExp.setExpediente(expedienteTitulacion);
            nuevoOBJtitExp.setDocumento(doc);
            nuevoOBJtitExp.setFechaEmision(fechaEmisionTit);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJtitExp.setRuta(utilidadesCH.agregarTitExpTit(fileTitTSU, generacion , nivel, expedienteTitulacion.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expedienteTitulacion.getMatricula().getMatricula()));
            nuevoOBJtitExp.setFechaCarga(new Date());
            nuevoOBJtitExp = ejbEstudianteRegistro.guardarTituloExpediente(nuevoOBJtitExp);
            
            Faces.redirect("expedienteTitulacion/egresadoTitulado/descargarTitulo.xhtml");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorDescargaTituloEgresado.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    /**
     * Permite guardar el registro y el archivo correspondente a título del egresado de nivel de Ingeniería o Licenciatura
     */
   
    public void subirDocumentoTitIL(){
            try {
            claveDoc = 17;
            nuevoOBJtitExp = new TituloExpediente();
            nuevoOBJegresado = ejbEstudianteRegistro.mostrarDatosPersonales(matricula);

            Short gen = expedienteTitulacion.getGeneracion();
            String generacion = ejbEstudianteRegistro.obtenerGeneracionProntuario(gen);
            
            String nivel = "";
            if (expedienteTitulacion.getNivel() == 2) {
                nivel = "ING";
            } else if (expedienteTitulacion.getNivel() == 1) {
                nivel = "TSU";
            }
            if (expedienteTitulacion.getNivel() == 4) {
                nivel = "LIC";
            }

            Documentos doc = new Documentos();
            doc = ejbEstudianteRegistro.obtenerInformacionDocumento(claveDoc);

            nuevoOBJtitExp.setExpediente(expedienteTitulacion);
            nuevoOBJtitExp.setDocumento(doc);
            nuevoOBJtitExp.setFechaEmision(fechaEmisionTit);

            String nombreEstMat = nuevoOBJegresado.getApellidoPaterno() + "_" + nuevoOBJegresado.getApellidoMaterno() + "_" + nuevoOBJegresado.getNombre() + "_" + nuevoOBJegresado.getMatricula();

            nuevoOBJtitExp.setRuta(utilidadesCH.agregarTitExpTit(fileTitIL, generacion , nivel, expedienteTitulacion.getProgramaEducativo(), nombreEstMat, doc.getNomenclatura(), expedienteTitulacion.getMatricula().getMatricula()));
            nuevoOBJtitExp.setFechaCarga(new Date());
            nuevoOBJtitExp = ejbEstudianteRegistro.guardarTituloExpediente(nuevoOBJtitExp);
            
            Faces.redirect("expedienteTitulacion/egresadoTitulado/descargarTitulo.xhtml");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorDescargaTituloEgresado.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    /**
     * Permite descargar el archivo del título guardado
     * @param titExp
     */
    
    public void descargarTitulo(TituloExpediente titExp) throws IOException{
        File f = new File(titExp.getRuta());
        Faces.sendFile(f, false);
    }
    
    /**
     * Permite eliminar el registro y archivo guardado del título
     * @param titExp
     */
       
    public void eliminarTitulo(TituloExpediente titExp){
        Boolean eliminado = ejbEstudianteRegistro.eliminarTituloExpediente(titExp);
        if (eliminado) {
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
            Faces.redirect("expedienteTitulacion/egresadoTitulado/cargarTitulo.xhtml");
        } else {
            Messages.addGlobalError("El documento no ha podido eliminarse.");
        }
    }
    
    /**
     * Permite que al cambiar o seleccionar generación de listado de titulos se actualice el valor de la variable
     * @param event Evento del cambio de valor
     */
    
    public void cambiarFechaEmision(ValueChangeEvent event){
        setFechaEmisionTit((Date)event.getNewValue());
    }
    
    /**
     * Permite que se cambié el valor de la variable y se ejecute el método que actualizará el registro
     * @param event SelectEvent del cambio de fecha
     */
    
    public void cambiarFechaEmision2(SelectEvent event){
        tituloExpediente.setFechaEmision((Date) event.getObject());
        actualizarFechaEmision();
    }
    
    /**
     * Permite actualizar fecha de emisión del título en el registro guardado previamente
     */
    
    public void actualizarFechaEmision(){
        try{
            tituloExpediente = ejbEstudianteRegistro.actualizarFechaEmision(tituloExpediente);
            Ajax.update("frmTitExp");
         } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorDescargaTituloEgresado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
