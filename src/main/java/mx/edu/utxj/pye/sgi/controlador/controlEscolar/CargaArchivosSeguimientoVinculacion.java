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
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentosCartaRespCursoIMMSEstudiante;
import org.omnifaces.cdi.ViewScoped;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaCartaResponsivaCursoIMSS;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named(value = "cargaArchivosSeguimientoVinculacion")
@ManagedBean
@ViewScoped
public class CargaArchivosSeguimientoVinculacion implements Serializable{

    private static final long serialVersionUID = -1642035799829402005L;

    // Variable para documentos  
    @Getter private DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante;
    @Getter private List<DtoDocumentosCartaRespCursoIMMSEstudiante> lista;
    @Getter @Setter private Part file;
    @Inject UtilidadesCH utilidadesCH;
    @Inject CargaCartaResponsivaCursoIMSSEstudiante cargaCartaResponsivaCursoIMSSEstudiante;
    @Getter private Documento documento;
    @Getter private DocumentoProceso documentoProceso;
    @Getter private EventoVinculacion eventoVinculacion;
    
    @EJB private EjbCargaCartaResponsivaCursoIMSS ejbCargaCartaResponsivaCursoIMSS;
   
   
    public void editarDocumento(DtoDocumentosCartaRespCursoIMMSEstudiante registro, EventoVinculacion evento) {
        dtoDocumentosCartaRespCursoIMMSEstudiante = registro;
        if(evento==null){
            eventoVinculacion = ejbCargaCartaResponsivaCursoIMSS.buscarAperturaExtemporaneaDocumento(cargaCartaResponsivaCursoIMSSEstudiante.getRol().getDtoCartaResponsivaCursoIMMSEstudiante(), dtoDocumentosCartaRespCursoIMMSEstudiante).getValor();
        }else{
            eventoVinculacion = evento;
        }
    }
    
    public void subirDocumento() {
        try {
            DocumentoSeguimientoVinculacion nuevoDocumento = new DocumentoSeguimientoVinculacion();

            nuevoDocumento.setSeguimiento(cargaCartaResponsivaCursoIMSSEstudiante.getRol().getDtoCartaResponsivaCursoIMMSEstudiante().getSeguimientoVinculacionEstudiante());
            nuevoDocumento.setEvento(eventoVinculacion);
            nuevoDocumento.setDocumento(dtoDocumentosCartaRespCursoIMMSEstudiante.getDocumentoProceso().getDocumento());
            nuevoDocumento.setRuta(utilidadesCH.agregarCartaResponsivaCursoIMSS(file, cargaCartaResponsivaCursoIMSSEstudiante.getRol().getDtoCartaResponsivaCursoIMMSEstudiante(), dtoDocumentosCartaRespCursoIMMSEstudiante));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin observaciones");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejbCargaCartaResponsivaCursoIMSS.guardarDocumentoSeguimientoVinculacion(nuevoDocumento).getValor();
            cargaCartaResponsivaCursoIMSSEstudiante.seguimientoEstudiante();
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaArchivosSeguimientoEstadia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarDocumento(DtoDocumentosCartaRespCursoIMMSEstudiante docsSeg){
        ResultadoEJB<Integer> resEliminar =  ejbCargaCartaResponsivaCursoIMSS.eliminarDocumentoSeguimiento(docsSeg.getDocumentoSeguimientoVinculacion());
        if(resEliminar.getCorrecto()){
        if(resEliminar.getValor() == 1){ 
            cargaCartaResponsivaCursoIMSSEstudiante.seguimientoEstudiante();
            Ajax.update("frmDocsSegEst");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
    
    public void descargarDocumento(DtoDocumentosCartaRespCursoIMMSEstudiante docsSeg) throws IOException{
        File f = new File(docsSeg.getDocumentoSeguimientoVinculacion().getRuta());
        Faces.sendFile(f, false);
    }
    
    public Boolean consultarExisteDocumento(Documento documento){
        return ejbCargaCartaResponsivaCursoIMSS.consultarDocumento(documento, cargaCartaResponsivaCursoIMSSEstudiante.getRol().getDtoCartaResponsivaCursoIMMSEstudiante().getSeguimientoVinculacionEstudiante()).getValor();
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
