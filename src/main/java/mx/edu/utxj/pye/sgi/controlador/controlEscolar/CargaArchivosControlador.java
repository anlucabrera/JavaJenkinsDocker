/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.Serializable;
import java.util.Date;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import org.omnifaces.cdi.ViewScoped;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaDocumentosAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
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
    @Getter @Setter private Part file;
    @Inject UtilidadesCH utilidadesCH;
    @Inject CargaDocumentosAspirante cargaDocumentosAspirante;
    
    @EJB private EjbCargaDocumentosAspirante ejbCargaDocumentosAspirante;
    
    @PostConstruct
    public void init() {
        aspirante = cargaDocumentosAspirante.getRol().getAspirante();
    }
    
    public void editarDocumento(DtoDocumentoAspirante registro) {
        dtoDocumentoAspirante = registro;
//        nuevaActFormInt = dto.getRegistro().getActividadesFormacionIntegral();
//        Ajax.update("modalCargaArchivo");
//        Ajax.oncomplete("skin();");
//        setForzarAperturaDialogo(Boolean.TRUE);
//        forzarAperturaEdicionDialogo();
    }
     
    public void subirDocumento() {
        try {
            DocumentoAspiranteProceso nuevoDocumento = new DocumentoAspiranteProceso();

            nuevoDocumento.setAspirante(aspirante);
            nuevoDocumento.setDocumento(dtoDocumentoAspirante.getDocumentoProceso().getDocumento());
            nuevoDocumento.setRuta(utilidadesCH.agregarDocumentoAspirante(file, aspirante, dtoDocumentoAspirante));
            nuevoDocumento.setFechaCarga(new Date());
            nuevoDocumento.setObservaciones("Sin observaciones");
            nuevoDocumento.setValidado(false);
            nuevoDocumento.setFechaValidacion(null);
            nuevoDocumento = ejbCargaDocumentosAspirante.guardarDocumentoAspirante(nuevoDocumento).getValor();
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CargaArchivosControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
