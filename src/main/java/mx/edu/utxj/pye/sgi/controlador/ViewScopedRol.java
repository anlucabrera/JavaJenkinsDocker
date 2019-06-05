package mx.edu.utxj.pye.sgi.controlador;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import java.io.Serializable;

@Dependent
public class ViewScopedRol implements Serializable {
    protected  FacesMessage ultimoMensaje;
    public void mostrarMensajeNoAcceso(){
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        String mensaje = "No tiene acceso a esta página.";
        ultimoMensaje = construirMensaje(severity, mensaje);
        Messages.addGlobal(FacesMessage.SEVERITY_ERROR, mensaje);
    }

    public void mostrarMensajeResultadoEJB(ResultadoEJB res){
        FacesMessage.Severity severity = res.getCorrecto() ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR;
        ultimoMensaje = construirMensaje(severity, res.getMensaje());
        Messages.addGlobal(severity, res.getMensaje());
    }

    public void mostrarMensaje(String mensaje){
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        ultimoMensaje = construirMensaje(severity, mensaje);
        Messages.addGlobal(severity, mensaje);
    }

    public void mostrarExcepcion(Throwable e){
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        if(e instanceof  NullPointerException){
            String mensaje = "Ocurrió un error: Objeto nulo.";
            ultimoMensaje = construirMensaje(severity, mensaje);
            Messages.addGlobal(severity, mensaje);
        }else {
            if (e != null) {
                String mensaje = "Ocurrió un error: " + e.getCause() != null ? e.getCause().getMessage() : e.getMessage() == null ? " Objeto nulo. " : e.getMessage();
                ultimoMensaje = construirMensaje(severity, mensaje);
                Messages.addGlobal(severity, mensaje);
            }
        }
    }

    private FacesMessage construirMensaje(FacesMessage.Severity severity, String mensaje){
        return new FacesMessage(severity, mensaje, mensaje);
    }

    public void repetirUltimoMensaje(){
        if(ultimoMensaje != null){
            Messages.addGlobal(ultimoMensaje.getSeverity(), ultimoMensaje.getSummary());
            ultimoMensaje = null;
        }
    }

    public Boolean verificarInvocacionMenu(){
        return Faces.getRequestServletPath().equals("/index.xhtml");
    }
}
