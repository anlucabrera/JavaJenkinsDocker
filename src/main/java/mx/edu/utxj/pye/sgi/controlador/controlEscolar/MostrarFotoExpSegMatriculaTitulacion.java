/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import java.io.FileInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbSeguimientoExpedienteGeneracion;
import org.omnifaces.util.Messages;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@SessionScoped
public class MostrarFotoExpSegMatriculaTitulacion implements Serializable{

    @Getter @Setter private StreamedContent graphicImage;
    @Getter @Setter private String rutaFotografia;
    
    @EJB EjbSeguimientoExpedienteGeneracion ejbSeguimientoExpedienteGeneracion;
    
    public void mostrarFotografia(DtoExpedienteTitulacion dtoExpedienteTitulacion) {
        try {
            rutaFotografia = ejbSeguimientoExpedienteGeneracion.buscarFotografia(dtoExpedienteTitulacion).getValor();
            graphicImage = new DefaultStreamedContent(new FileInputStream(rutaFotografia), "image/jpg");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(MostrarFotoExpSegMatriculaTitulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
