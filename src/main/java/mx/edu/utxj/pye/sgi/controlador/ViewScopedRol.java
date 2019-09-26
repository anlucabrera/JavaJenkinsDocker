package mx.edu.utxj.pye.sgi.controlador;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.funcional.Controlable;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import javax.enterprise.context.Dependent;
import javax.faces.application.FacesMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Dependent
public class ViewScopedRol implements Controlable {
    @Getter @Setter protected  FacesMessage ultimoMensaje;
    @Getter @Setter protected List<DtoAlerta> alertas = new ArrayList<>();
    @Getter @Setter protected ControlEscolarVistaControlador vistaControlador;

    private boolean verificarIdentificacion(){
        if(!estaIdentificado()) {
            mostrarErrorIdentificacion();
            return false;
        }
//        System.out.println("this.getClass() = " + this.getClass());
//        if(estaIdentificado()) System.out.println("vistaControlador = " + vistaControlador.getClass());
//        if(estaIdentificado()) System.out.println("this.getClass().equals(vistaControlador.getClass()) = " + this.getClass().equals(vistaControlador.getClass()));

        return validarIdentificacion();
    }

    public Boolean validarIdentificacion(){
        if(vistaControlador == null) return false;
        return Faces.getRequestServletPath().equals(vistaControlador.getVista());
    }

    public void mostrarMensajeNoAcceso(){
        if(!verificarIdentificacion()) return;
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        String mensaje = "No tiene acceso a esta página. ".concat(this.getClass().toString());
        ultimoMensaje = construirMensaje(severity, mensaje);
        Messages.addGlobal(FacesMessage.SEVERITY_ERROR, mensaje);
    }

    public void mostrarMensajeResultadoEJB(ResultadoEJB res){
        if(!verificarIdentificacion()) return;
        FacesMessage.Severity severity = res.getCorrecto() ? FacesMessage.SEVERITY_INFO : FacesMessage.SEVERITY_ERROR;
        ultimoMensaje = construirMensaje(severity, res.getMensaje());
        Messages.addGlobal(severity, res.getMensaje());
    }

    public void mostrarMensaje(String mensaje){
        if(!verificarIdentificacion()) return;
        FacesMessage.Severity severity = FacesMessage.SEVERITY_INFO;
        ultimoMensaje = construirMensaje(severity, mensaje);
        Messages.addGlobal(severity, mensaje);
    }

    public void mostrarErrorIdentificacion(){
//        if(!validarIdentificacion()) return;
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;
        String mensaje = "No se ha identificado al controlador, para identificarlo debes sobreescribir éste método en el controlador y establer el valor de la propiedad vistaControlador.".concat(this.getClass().toString()) ;
        ultimoMensaje = construirMensaje(severity, mensaje);
        Messages.addGlobal(severity, mensaje);
    }

    public void mostrarExcepcion(Throwable e){
        if(!verificarIdentificacion()) return;
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

    @Override
    public Class identificar() throws Exception {
        throw new Exception("No se ha identificado al controlador, para identificarlo debes sobreescribir éste método en el controlador y establer el valor de la propiedad vistaControlador");
    }

    public boolean estaIdentificado(){
        return vistaControlador != null;
    }

    public boolean cortarFlujo(){
        if(verificarInvocacionMenu() || !validarIdentificacion()) return true;

        return false;
    }
}
