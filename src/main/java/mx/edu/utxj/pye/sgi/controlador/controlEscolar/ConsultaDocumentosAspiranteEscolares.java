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
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaDocumentosAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;



/**
 *
 * @author UTXJ
 */
@Named(value = "consultaDocumentosAspiranteEscolares")
@ManagedBean
@SessionScoped
public class ConsultaDocumentosAspiranteEscolares implements Serializable{

    private static final long serialVersionUID = 8523173138860959204L;
    
    @Getter @Setter private Aspirante aspiranteB;
    @Getter private DtoDocumentoAspirante dtoDocumentoAspirante;
    @Getter @Setter private List<DtoDocumentoAspirante> listaDocumentoAspirantes;
    @EJB private EjbCargaDocumentosAspirante ejbCargaDocumentosAspirante;
    @Getter @Setter private Part file;
    @Inject UtilidadesCH utilidadesCH;
    
    public void mostrarDocumentos(Aspirante aspirante){
       aspiranteB = aspirante;
       listaDocumentoAspirantes = ejbCargaDocumentosAspirante.getDocumentoAspirante(aspirante).getValor();
       Ajax.update("frmDocsAsp");
        
    }
    
     public void descargarDocumento(DtoDocumentoAspirante docsExp) throws IOException{
        File f = new File(docsExp.getDocumentoAspiranteProceso().getRuta());
        Faces.sendFile(f, false);
    }
    
    public Boolean consultarExisteDocumento(Documento documento){
        return ejbCargaDocumentosAspirante.consultarDocumento(documento, aspiranteB).getValor();
    }
    
    public void editarDocumento(DtoDocumentoAspirante registro) {
        dtoDocumentoAspirante = registro;
    }
    
    public void subirDocumento() {
        try {
            DocumentoAspiranteProceso nuevoDocumento = new DocumentoAspiranteProceso();

            nuevoDocumento.setAspirante(aspiranteB);
            nuevoDocumento.setDocumento(dtoDocumentoAspirante.getDocumentoProceso().getDocumento());
            nuevoDocumento.setRuta(utilidadesCH.agregarDocumentoAspirante(file, aspiranteB, dtoDocumentoAspirante));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin observaciones");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejbCargaDocumentosAspirante.guardarDocumentoAspirante(nuevoDocumento).getValor();
            mostrarDocumentos(aspiranteB);
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ConsultaDocumentosAspiranteEscolares.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarDocumento(DtoDocumentoAspirante docsExp){
        ResultadoEJB<Integer> resEliminar =  ejbCargaDocumentosAspirante.eliminarDocumentoAspirante(docsExp.getDocumentoAspiranteProceso());
        if(resEliminar.getCorrecto()){
        if(resEliminar.getValor() == 1){ 
            mostrarDocumentos(aspiranteB);
            Ajax.update("frmDocsAsp");
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
 
    public void onCellEdit(CellEditEvent event) {
        DataTable dataTable = (DataTable) event.getSource();
        DtoDocumentoAspirante registroNew = (DtoDocumentoAspirante) dataTable.getRowData();
        ejbCargaDocumentosAspirante.guardarObservacionesDocumento(registroNew);
        Messages.addGlobalInfo("Las observaciones se han guardado correctamente.");
    }
    
    public void validarDocumento(DtoDocumentoAspirante docsExp) {
        ResultadoEJB<DocumentoAspiranteProceso> res =   ejbCargaDocumentosAspirante.validarDocumento(docsExp.getDocumentoAspiranteProceso());
        if(res.getCorrecto()){
            mostrarDocumentos(aspiranteB);
            Ajax.update("frmDocsAsp");
            Messages.addGlobalInfo("El documento se ha validado o invalidado correctamente.");
        }else Messages.addGlobalError("El documento no se pudo validar o invalidar.");
    }
    
}
