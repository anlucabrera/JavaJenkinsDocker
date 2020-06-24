/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

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
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import org.omnifaces.cdi.ViewScoped;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaDocumentosAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "cargaArchivosControlador")
@ManagedBean
@ViewScoped
public class CargaArchivosControlador implements Serializable{

    private static final long serialVersionUID = 6789654131911900835L;
    
    // Variable para documentos  
    @Getter private Aspirante aspirante;
    @Getter private DtoDocumentoAspirante dtoDocumentoAspirante;
    @Getter private List<DtoDocumentoAspirante> lista;
    @Getter @Setter private Part file;
    @Inject UtilidadesCH utilidadesCH;
    @Inject CargaDocumentosAspirante cargaDocumentosAspirante;
    @Getter private Documento documento;
    @Getter private DocumentoProceso documentoProceso;
    
    @EJB private EjbCargaDocumentosAspirante ejbCargaDocumentosAspirante;
    
    @PostConstruct
    public void init() {
        aspirante = cargaDocumentosAspirante.getRol().getAspirante();
    }
    
    public void cambiarDocumentoSeleccionado (){
        documento = cargaDocumentosAspirante.getRol().getDocumentoSeleccionado();
    }
    
    public void buscarInformacionProceso (){
        documentoProceso = ejbCargaDocumentosAspirante.buscarInformacionProceso(documento).getValor();
    }
    
    public void editarDocumento(DtoDocumentoAspirante registro) {
        dtoDocumentoAspirante = registro;
    }
    
    public void subirDocumento() {
        try {
            cambiarDocumentoSeleccionado();
            
            DocumentoAspiranteProceso nuevoDocumento = new DocumentoAspiranteProceso();

            nuevoDocumento.setAspirante(aspirante);
            nuevoDocumento.setDocumento(dtoDocumentoAspirante.getDocumentoProceso().getDocumento());
            nuevoDocumento.setRuta(utilidadesCH.agregarDocumentoAspirante(file, aspirante, dtoDocumentoAspirante));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin observaciones");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejbCargaDocumentosAspirante.guardarDocumentoAspirante(nuevoDocumento).getValor();
            cargaDocumentosAspirante.mostrarDocumentos(aspirante);
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaArchivosControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarDocumento(DtoDocumentoAspirante docsExp){
        ResultadoEJB<Integer> resEliminar =  ejbCargaDocumentosAspirante.eliminarDocumentoAspirante(docsExp.getDocumentoAspiranteProceso());
        if(resEliminar.getCorrecto()){
        if(resEliminar.getValor() == 1){ 
            cargaDocumentosAspirante.mostrarDocumentos(aspirante);
            Ajax.update("frmDocsAsp");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
    
    public void descargarDocumento(DtoDocumentoAspirante docsExp) throws IOException{
        File f = new File(docsExp.getDocumentoAspiranteProceso().getRuta());
        Faces.sendFile(f, false);
    }
    
    public Boolean consultarExisteDocumento(Documento documento){
        return ejbCargaDocumentosAspirante.consultarDocumento(documento, aspirante).getValor();
    }
    
    public void subirDocumentoPendiente() {
        try {
            cambiarDocumentoSeleccionado();
            buscarInformacionProceso();
            
            DocumentoAspiranteProceso nuevoDocumento = new DocumentoAspiranteProceso();

            nuevoDocumento.setAspirante(aspirante);
            nuevoDocumento.setDocumento(documento);
            nuevoDocumento.setRuta(utilidadesCH.agregarDocumentoPendienteAspirante(file, aspirante, documento, documentoProceso));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin observaciones");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejbCargaDocumentosAspirante.guardarDocumentoAspirante(nuevoDocumento).getValor();
            cargaDocumentosAspirante.mostrarDocumentos(aspirante);
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaArchivosControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
}
