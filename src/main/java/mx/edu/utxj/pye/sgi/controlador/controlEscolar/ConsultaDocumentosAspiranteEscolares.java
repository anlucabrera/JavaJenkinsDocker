/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbCargaDocumentosAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;



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
    @Getter @Setter private List<DtoDocumentoAspirante> listaDocumentoAspirantes;
    @EJB private EjbCargaDocumentosAspirante ejbCargaDocumentosAspirante;
    
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
    
}
