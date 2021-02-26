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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import org.omnifaces.cdi.ViewScoped;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "cargaArchivosSeguimientoEstadia")
@ManagedBean
@ViewScoped
public class CargaArchivosSeguimientoEstadia implements Serializable{

    private static final long serialVersionUID = 370815015509105840L;
    
    // Variable para documentos  
    @Getter private SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante;
    @Getter private DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante;
    @Getter private List<DtoDocumentoEstadiaEstudiante> lista;
    @Getter @Setter private Part file;
    @Inject UtilidadesCH utilidadesCH;
    @Inject SeguimientoEstadiaPorEstudiante seguimientoEstadiaPorEstudiante;
    @Getter private Documento documento;
    @Getter private DocumentoProceso documentoProceso;
    @Getter private EventoEstadia eventoEstadia;
    
    @EJB private EjbSeguimientoEstadia ejbSeguimientoEstadia;
    
    @PostConstruct
    public void init() {
        seguimientoEstadiaEstudiante = seguimientoEstadiaPorEstudiante.getRol().getDtoSeguimientoEstadiaEstudiante().getSeguimientoEstadiaEstudiante();
    }
   
    public void editarDocumento(DtoDocumentoEstadiaEstudiante registro, EventoEstadia evento) {
        dtoDocumentoEstadiaEstudiante = registro;
        eventoEstadia = evento;
    }
    
    public void subirDocumento() {
        try {
            
            DocumentoSeguimientoEstadia nuevoDocumento = new DocumentoSeguimientoEstadia();

            nuevoDocumento.setSeguimientoEstadia(seguimientoEstadiaEstudiante);
            nuevoDocumento.setEvento(eventoEstadia);
            nuevoDocumento.setDocumento(dtoDocumentoEstadiaEstudiante.getDocumentoProceso().getDocumento());
            nuevoDocumento.setRuta(utilidadesCH.agregarDocumentoEstadia(file, seguimientoEstadiaEstudiante, dtoDocumentoEstadiaEstudiante));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin observaciones");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejbSeguimientoEstadia.guardarDocumentoSeguimientoEstadia(nuevoDocumento).getValor();
            seguimientoEstadiaPorEstudiante.seguimientoEstudiante();
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaArchivosSeguimientoEstadia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarDocumento(DtoDocumentoEstadiaEstudiante docsSeg){
        ResultadoEJB<Integer> resEliminar =  ejbSeguimientoEstadia.eliminarDocumentoSeguimiento(docsSeg.getDocumentoSeguimientoEstadia());
        if(resEliminar.getCorrecto()){
        if(resEliminar.getValor() == 1){ 
            seguimientoEstadiaPorEstudiante.seguimientoEstudiante();
            Ajax.update("frmDocsSegEst");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
    
    public void descargarDocumento(DtoDocumentoEstadiaEstudiante docsSeg) throws IOException{
        File f = new File(docsSeg.getDocumentoSeguimientoEstadia().getRuta());
        Faces.sendFile(f, false);
    }
    
    public Boolean consultarExisteDocumento(Documento documento){
        return ejbSeguimientoEstadia.consultarDocumento(documento, seguimientoEstadiaEstudiante).getValor();
    }
    
//    public void subirDocumentoPendiente() {
//        try {
//            cambiarDocumentoSeleccionado();
//            buscarInformacionProceso();
//            
//            DocumentoAspiranteProceso nuevoDocumento = new DocumentoAspiranteProceso();
//
//            nuevoDocumento.setAspirante(aspirante);
//            nuevoDocumento.setDocumento(documento);
//            nuevoDocumento.setRuta(utilidadesCH.agregarDocumentoPendienteAspirante(file, aspirante, documento, documentoProceso));
//            nuevoDocumento.setFechaCarga(new Date());
//            nuevoDocumento.setObservaciones("Sin observaciones");
//            nuevoDocumento.setValidado(false);
//            nuevoDocumento.setFechaValidacion(null);
//            nuevoDocumento = ejbCargaDocumentosAspirante.guardarDocumentoAspirante(nuevoDocumento).getValor();
//            cargaDocumentosAspirante.mostrarDocumentos(aspirante);
//            
//        } catch (Throwable ex) {
//            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
//            Logger.getLogger(CargaArchivosControlador.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
    
}
