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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbIntegracionExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class CargaDocumentosExpedienteEstudiante implements Serializable{

    private static final long serialVersionUID = 1984739133970121492L;
    
    // Variable para documentos  
    @Getter @Setter private Integer claveDoc;
    @Getter @Setter private Part fileFotoTSU, fileFotoIL; 
    @Getter @Setter private DocumentoExpedienteTitulacion documentoExpedienteTitulacion;
    @Getter @Setter private DtoExpedienteTitulacion dtoExpedienteTitulacion;
    
    @EJB private EjbIntegracionExpedienteTitulacion ejbIntegracionExpedienteTitulacion;
    
    @Inject LogonMB logonMB;
    @Inject UtilidadesCH utilidadesCH;
    @Inject IntegracionExpedienteTitulacionEstudiante integracionExpedienteTitulacionEstudiante;
    
    @PostConstruct
    public void init() {
       
            dtoExpedienteTitulacion = integracionExpedienteTitulacionEstudiante.getRol().getDtoExpedienteTitulacion();
    }
    
    public void subirDocumentoFotoIL(){
            try {
            claveDoc = 25;
            
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

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDoc);
           
            documentoExpedienteTitulacion.setExpediente(dtoExpedienteTitulacion.getExpediente());
            documentoExpedienteTitulacion.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            documentoExpedienteTitulacion.setRuta(utilidadesCH.agregarDocExpTit(fileFotoIL, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            documentoExpedienteTitulacion.setFechaCarga(new Date());
            documentoExpedienteTitulacion.setObservaciones("Sin revisar");
            documentoExpedienteTitulacion.setValidado(false);
            documentoExpedienteTitulacion.setFechaValidacion(null);
            documentoExpedienteTitulacion = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(documentoExpedienteTitulacion);
            Ajax.update("frmDocsExp");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpedienteEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
    public void subirDocumentoFotoTSU(){
             try {
            claveDoc = 18;
            
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

            Documento documentoTitulacion = ejbIntegracionExpedienteTitulacion.obtenerInformacionDocumento(claveDoc);
            
            DocumentoExpedienteTitulacion nuevoDocExp = new DocumentoExpedienteTitulacion();

            nuevoDocExp.setExpediente(dtoExpedienteTitulacion.getExpediente());
            nuevoDocExp.setDocumento(documentoTitulacion);

            String nombreEstMat = dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoPaterno()+ "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getApellidoMaterno() + "_" + dtoExpedienteTitulacion.getExpediente().getMatricula().getAspirante().getIdPersona().getNombre() + "_" + matricula;
            nuevoDocExp.setRuta(utilidadesCH.agregarDocExpTit(fileFotoTSU, generacion , nivel, dtoExpedienteTitulacion.getProgramaEducativo().getSiglas(), nombreEstMat, documentoTitulacion.getNomenclatura(), matricula));
            nuevoDocExp.setFechaCarga(new Date());
            nuevoDocExp.setObservaciones("Sin revisar");
            nuevoDocExp.setValidado(false);
            nuevoDocExp.setFechaValidacion(null);
            nuevoDocExp = ejbIntegracionExpedienteTitulacion.guardarDocumentoExpediente(nuevoDocExp);
            integracionExpedienteTitulacionEstudiante.existeExpedienteTitulacion();
            Ajax.update("frmDocsExp");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaDocumentosExpedienteEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }
                 
    }
    
     public void eliminarDocumento(DocumentoExpedienteTitulacion docsExp){
        Boolean eliminado = ejbIntegracionExpedienteTitulacion.eliminarDocumentosEnExpediente(docsExp);
        if(eliminado){
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
            Ajax.update("todo");
            integracionExpedienteTitulacionEstudiante.existeExpedienteTitulacion();
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
    
    public void descargarDocumento(DocumentoExpedienteTitulacion docsExp) throws IOException{
        File f = new File(docsExp.getRuta());
        Faces.sendFile(f, false);
    }
    
}
