package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorSoporteTecnico implements Serializable {

    private static final long serialVersionUID = -515564016042081711L;

    @Getter    @Setter    private Integer usuario;
    @Getter    @Setter    private String menajeNot, tipo, nombreO;
    @Getter    @Setter    private List<Notificaciones> listaNotificaciones = new ArrayList<>();
    @Getter    @Setter    private Notificaciones nuevOBJNotificaciones;

    @Getter    @Setter    private Date fechaActual = new Date();

//@EJB 
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbCreate;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {        
        usuario = controladorEmpleado.getEmpleadoLogeado();        
    }

    public void agregarNotificacionMultiple() {
        try {
            if (!"".equals(menajeNot) || !"".equals(nombreO) || !"".equals(tipo)) {
                nuevOBJNotificaciones.setClaveTDestino(new Personal());
                nuevOBJNotificaciones.setClaveTRemitente(new Personal());
                nuevOBJNotificaciones = new Notificaciones();
                nuevOBJNotificaciones.setMensaje(menajeNot);
                nuevOBJNotificaciones.getClaveTRemitente().setClave(usuario);
                nuevOBJNotificaciones.setStatus(0);
                nuevOBJNotificaciones.setFecha(fechaActual);
                nuevOBJNotificaciones.getClaveTDestino().setClave(564);
                nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
            }
            menajeNot = "";
            tipo = "";
            nombreO = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSoporteTecnico.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
