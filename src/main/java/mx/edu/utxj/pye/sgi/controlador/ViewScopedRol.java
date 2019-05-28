package mx.edu.utxj.pye.sgi.controlador;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import java.io.Serializable;

@Dependent
public class ViewScopedRol implements Serializable {
    public void mostrarMensajeNoAcceso(){
        Messages.addGlobal(FacesMessage.SEVERITY_ERROR, "No tiene acceso a esta página.");
    }

    public void mostrarMensajeResultadoEJB(ResultadoEJB res){
        Messages.addGlobal(res.getCorrecto() ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR, res.getMensaje());
    }

    public void mostrarMensaje(String mensaje){
        Messages.addGlobal(FacesMessage.SEVERITY_INFO, mensaje);
    }

    public void mostrarExcepcion(Throwable e){
        //e.printStackTrace();
//        System.out.println("e = " + e);
//        System.out.println("e.getMessage() = " + e.getMessage());
//        System.out.println("e.getCause() = " + e.getCause());
        if(e instanceof  NullPointerException){
            Messages.addGlobal(FacesMessage.SEVERITY_ERROR, "Ocurrió un error: Objeto nulo.");
        }else {
            if (e != null)
                Messages.addGlobal(FacesMessage.SEVERITY_ERROR, "Ocurrió un error: " + e.getCause() != null ? e.getCause().getMessage() : e.getMessage() == null ? " Objeto nulo. " : e.getMessage());
        }
    }
}
