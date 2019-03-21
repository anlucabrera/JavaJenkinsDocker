package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbFichaAdmision;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbProcesoInscripcion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSelectItemCE;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import org.omnifaces.util.Faces;

@Named(value = "procesoInscripcion")
@ViewScoped
public class ProcesoInscripcion implements Serializable{
    
    @Getter @Setter private ProcesosInscripcion procesosInscripcion;
    @Getter @Setter private List<Aspirante> listaAspirantesTSU;
    @Getter @Setter private List<SelectItem> listaPEAll;
    @Getter @Setter private String pe;
    
    @EJB EjbFichaAdmision ejbFichaAdmision;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbSelectItemCE ejbItemCE;
    
    @PostConstruct
    public void init(){
        listaPEAll = ejbItemCE.itemProgramaEducativoAll();
        procesosInscripcion = ejbFichaAdmision.getProcesoIncripcionTSU();
    }

    public void validarFichaAdmision(Aspirante aspirante){
        ejbFichaAdmision.actualizaAspirante(aspirante);
    }

    public void selectAspirantesByPE(){
        listaAspirantesTSU = ejbProcesoInscripcion.lisAspirantesByPE(pe,procesosInscripcion.getIdProcesosInscripcion());
    }

    public void descargarEvidenciaCURP(DocumentoAspirante documentoAspirante) throws IOException {
        File f = new File(documentoAspirante.getEvidenciaCurp());
        Faces.sendFile(f, false);
    }

    public void descargarEvidenciaActaN(DocumentoAspirante documentoAspirante) throws IOException {
        File f = new File(documentoAspirante.getEvidenciaActaNacimiento());
        Faces.sendFile(f, false);
    }

    public void descargarEvidenciaHistorial(DocumentoAspirante documentoAspirante) throws IOException {
        File f = new File(documentoAspirante.getEvidenciaHistorialAcademico());
        Faces.sendFile(f, false);
    }
    
    public void enviarComentario(Aspirante aspirante){
        ejbFichaAdmision.actualizaDocumentosAspirante(aspirante.getDocumentoAspirante());
    }
   
}
