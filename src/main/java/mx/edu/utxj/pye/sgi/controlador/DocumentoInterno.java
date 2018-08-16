package mx.edu.utxj.pye.sgi.controlador;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbDocumentosInternos;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tramites;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@Named(value = "documentoInterno")
@ViewScoped
public class DocumentoInterno implements Serializable{
    @EJB EjbDocumentosInternos ejb;
    public void descargarComisionOficio(Tramites tramite) throws IOException{
        File f = new File(ejb.construirOficio(tramite.getComisionOficios()));
        Faces.sendFile(f, true);
    }
}
