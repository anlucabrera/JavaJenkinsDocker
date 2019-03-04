package mx.edu.utxj.pye.sgi.controlador;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import java.io.Serializable;

@Dependent
public class ScopedRol implements Serializable {
    public void mostrarMensajeNoAcceso(){
        Messages.addGlobal(FacesMessage.SEVERITY_ERROR, "No tiene acceso a esta página.");
    }

    public void mostrarMensajeResultadoEJB(ResultadoEJB res){
        Messages.addGlobal(res.getCorrecto() ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, res.getMensaje());
    }
}
