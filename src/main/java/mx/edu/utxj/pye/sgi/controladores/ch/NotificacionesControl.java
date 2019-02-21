package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
public class NotificacionesControl implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<Personal> nuevaListaPersonalContacto = new ArrayList<>();
    @Getter    @Setter    private List<Notificaciones> notificacioneses = new ArrayList<>();

    @Getter    @Setter    private Iterator<Personal> pIterator;
    @Getter    @Setter    private Iterator<Notificaciones> nIterator;
////////////////////////////C V//////////////////////////////////

    @Getter    @Setter    private Integer empleadoLogeado = 0, contactoDestino = 0;
    @Getter    @Setter    private String mensajeDNotificacion = "", nombreContacto = "";

    @Getter    @Setter    private Notificaciones nuevoOBJNotificaciones;

    @Getter    @Setter    private List<ContactosChat> listacontactosChat = new ArrayList<>(), listaContactosChatFiltrados;
    @Getter    @Setter    private ContactosChat nuevOBJcontactosChat, nuevOBJcontactosChat2, nuevOBJcontactosChat3;
    @Getter    @Setter    private ContactosChat nuevoOBJcontactosChatSelec = new ContactosChat(0, "", 0);
    @Getter    @Setter    private List<Integer> clavesContactosCChat = new ArrayList<>();

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbSelectec;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        empleadoLogeado = controladorEmpleado.getEmpleadoLogeado();
        mostrarContactosParaNotificacion();
        mostrarNotificacionesLogeado();
    }

    public void mostrarContactosParaNotificacion() {
        try {
            nuevaListaPersonalContacto = new ArrayList<>();
            nuevaListaPersonalContacto.clear();
            nuevaListaPersonalContacto = ejbSelectec.mostrarListaPersonalsPorEstatus(1);

            pIterator = nuevaListaPersonalContacto.iterator();
            while (pIterator.hasNext()) {
                Personal p = pIterator.next();
                if (!p.getClave().equals(controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                    List<Notificaciones> notificacioneses = new ArrayList<>();
                    notificacioneses.clear();
                    notificacioneses = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(controladorEmpleado.getNuevoOBJListaPersonal().getClave(), p.getClave(), 0);
                    if (!notificacioneses.isEmpty()) {
                        listacontactosChat.add(new ContactosChat(p.getClave(), p.getNombre(), 2));
                        clavesContactosCChat.add(p.getClave());
                        pIterator.remove();
                    }
                }
            }

            pIterator = nuevaListaPersonalContacto.iterator();
            while (pIterator.hasNext()) {
                Personal p = pIterator.next();
                if (!p.getClave().equals(controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                    List<Notificaciones> notificacioneses1 = new ArrayList<>();
                    notificacioneses1.clear();
                    notificacioneses1 = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(controladorEmpleado.getNuevoOBJListaPersonal().getClave(), p.getClave(), 1);
                    List<Notificaciones> notificacioneses2 = new ArrayList<>();
                    notificacioneses2.clear();
                    notificacioneses2 = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(p.getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave(), 1);
                    if (!notificacioneses1.isEmpty() || !notificacioneses2.isEmpty()) {
                        listacontactosChat.add(new ContactosChat(p.getClave(), p.getNombre(), 1));
                        clavesContactosCChat.add(p.getClave());
                        pIterator.remove();
                    } else {
                        List<Notificaciones> notificacioneses3 = new ArrayList<>();
                        notificacioneses3.clear();
                        notificacioneses3 = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(p.getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave(), 0);
                        if (!notificacioneses3.isEmpty()) {
                            listacontactosChat.add(new ContactosChat(p.getClave(), p.getNombre(), 2));
                            clavesContactosCChat.add(p.getClave());
                            pIterator.remove();
                        }
                    }
                }
            }

            pIterator = nuevaListaPersonalContacto.iterator();
            while (pIterator.hasNext()) {
                Personal p = pIterator.next();
                if (!p.getClave().equals(controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                    listacontactosChat.add(new ContactosChat(p.getClave(), p.getNombre(), 0));
                    clavesContactosCChat.add(p.getClave());
                    pIterator.remove();
                }
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String actualizaF(Date fechaMensaje) {
        DateFormat fMensaje = new SimpleDateFormat("EEE d MMM yyyy HH:mm");
        return fMensaje.format(fechaMensaje);
    }

    public void mostrarNotificacionesLogeado() {
        try {
            if (nuevoOBJcontactosChatSelec.getClave() == 0) {
                contactoDestino = 0;
                nombreContacto = "";
            } else {
                contactoDestino = nuevoOBJcontactosChatSelec.getClave();
                nombreContacto = nuevoOBJcontactosChatSelec.getNombre();
            }

            notificacioneses = new ArrayList<>();
            notificacioneses.clear();
            notificacioneses = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuario(controladorEmpleado.getNuevoOBJListaPersonal().getClave());
            if (!notificacioneses.isEmpty()) {
                nIterator = notificacioneses.iterator();
                while (nIterator.hasNext()) {
                    Notificaciones n = nIterator.next();
                    if (Objects.equals(n.getClaveTDestino().getClave(), contactoDestino)) {

                    } else if (Objects.equals(n.getClaveTRemitente().getClave(), contactoDestino)) {
                        if (n.getStatus() == 0) {
                            n.setStatus(1);
                            ejbNotificacionesIncidencias.actualizarNotificaciones(n);
                        }
                    } else {
                        nIterator.remove();
                    }
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarNotificacion() {
        try {
            if (!mensajeDNotificacion.equals("")) {
                nuevoOBJNotificaciones = new Notificaciones();
                nuevoOBJNotificaciones.setClaveTDestino(new Personal());
                nuevoOBJNotificaciones.setClaveTRemitente(new Personal());
                nuevoOBJNotificaciones.setFecha(new Date());
                nuevoOBJNotificaciones.setMensaje(mensajeDNotificacion);
                nuevoOBJNotificaciones.setStatus(0);
                nuevoOBJNotificaciones.setTipo(4);
                nuevoOBJNotificaciones.setClaveTDestino(new Personal(contactoDestino));
                nuevoOBJNotificaciones.setClaveTRemitente(new Personal(empleadoLogeado));
                nuevoOBJNotificaciones = ejbNotificacionesIncidencias.agregarNotificacion(nuevoOBJNotificaciones);
                mensajeDNotificacion = "";
            }
            mostrarNotificacionesLogeado();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class ContactosChat {

        @Getter        @Setter        private int clave;
        @Getter        @Setter        private String nombre;
        @Getter        @Setter        private int estatus;

        @PostConstruct
        public void init() {

        }

        private ContactosChat(int _clave, String _nombre, int _estatus) {
            clave = _clave;
            nombre = _nombre;
            estatus = _estatus;
        }
    }
}
