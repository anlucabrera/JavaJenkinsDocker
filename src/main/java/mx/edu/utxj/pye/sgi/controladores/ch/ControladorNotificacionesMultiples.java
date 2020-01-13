package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.time.LocalDate;
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
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorNotificacionesMultiples implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private String menajeNot, tipo, nombreO;
    @Getter    @Setter    private List<String> listaO = new ArrayList<>();
    @Getter    @Setter    private Notificaciones nuevOBJNotificaciones;
    @Getter    @Setter    private List<ListaPersonal> listaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;

    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now();

//@EJB 
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbCreate;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbSelectec;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH uch;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
    }

    public void agregarNotificacionMultiple() {
        try {
            if ("".equals(menajeNot) || "".equals(nombreO) || "".equals(tipo)) {

            } else {
                nuevOBJNotificaciones = new Notificaciones();
                nuevOBJNotificaciones.setClaveTDestino(new Personal());
                nuevOBJNotificaciones.setClaveTRemitente(new Personal());
                listaListaPersonal.clear();
                nuevOBJNotificaciones.setMensaje(menajeNot);
                nuevOBJNotificaciones.setClaveTRemitente(new Personal(controladorEmpleado.getEmpleadoLogeado()));
                nuevOBJNotificaciones.setStatus(0);
                nuevOBJNotificaciones.setFecha(uch.castearLDaD(fechaActual));
                switch (tipo) {
                    case "Personal":
                        nuevOBJNotificaciones.setTipo(4);
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaPersonalsPorParametros(nombreO, 1);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.setClaveTDestino(new Personal(nuevoOBJListaPersonal.getClave()));
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a:" + nombreO);
                        break;
                    case "Categoria":
                        nuevOBJNotificaciones.setTipo(1);
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaPersonalsPorParametros(nombreO, 2);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.getClaveTDestino().setClave(nuevoOBJListaPersonal.getClave());
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a los empleados con la categoría/puesto: " + nombreO);
                        break;
                    case "Actividad":
                        nuevOBJNotificaciones.setTipo(2);
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaPersonalsPorParametros(nombreO, 3);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.getClaveTDestino().setClave(nuevoOBJListaPersonal.getClave());
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a los empleados con la actividad: " + nombreO);
                        break;
                    case "Area":
                        nuevOBJNotificaciones.setTipo(3);
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaPersonalsPorParametros(nombreO, 4);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.getClaveTDestino().setClave(nuevoOBJListaPersonal.getClave());
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a los empleados del área: " + nombreO);
                        break;
                }
            }
            nuevoOBJListaPersonal = new ListaPersonal();
            listaListaPersonal.clear();
            menajeNot = "";
            tipo = "";
            nombreO = "";

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorNotificacionesMultiples.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarListaO() {
        try {
            listaO.clear();
            listaListaPersonal = ejbSelectec.mostrarListaDeEmpleados();
            switch (tipo) {
                case "Actividad":
                    for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                        nuevoOBJListaPersonal = listaListaPersonal.get(i);
                        if (listaO.contains(nuevoOBJListaPersonal.getActividadNombre())) {
                        } else {
                            listaO.add(nuevoOBJListaPersonal.getActividadNombre());
                        }
                    }
                    break;
                case "Categoria":
                    for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                        nuevoOBJListaPersonal = listaListaPersonal.get(i);
                        if (listaO.contains(nuevoOBJListaPersonal.getCategoriaOperativaNombre())) {
                        } else {
                            listaO.add(nuevoOBJListaPersonal.getCategoriaOperativaNombre());
                        }
                    }
                    break;
                case "Area":
                    for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                        nuevoOBJListaPersonal = listaListaPersonal.get(i);
                        if (listaO.contains(nuevoOBJListaPersonal.getAreaOperativaNombre())) {
                        } else {
                            listaO.add(nuevoOBJListaPersonal.getAreaOperativaNombre());
                        }
                    }
                    break;
                case "Personal":
                    for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                        nuevoOBJListaPersonal = listaListaPersonal.get(i);
                        if (listaO.contains(nuevoOBJListaPersonal.getNombre())) {
                        } else {
                            listaO.add(nuevoOBJListaPersonal.getNombre());
                        }
                    }
                    break;
            }
            listaListaPersonal.clear();
            nuevoOBJListaPersonal = new ListaPersonal();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorNotificacionesMultiples.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
