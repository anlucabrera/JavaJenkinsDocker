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
import mx.edu.utxj.pye.sgi.entity.ch.Actividades;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorNotificacionesMultiples implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Integer usuario;
    @Getter    @Setter    private String menajeNot, tipo, nombreO;
    @Getter    @Setter    private List<String> listaO = new ArrayList<>();
    @Getter    @Setter    private List<Notificaciones> listaNotificaciones = new ArrayList<>();
    @Getter    @Setter    private Notificaciones nuevOBJNotificaciones;
    @Getter    @Setter    private List<ListaPersonal> listaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private PersonalCategorias nuevoOBJPersonalCategorias;
    @Getter    @Setter    private Actividades nuevoOBJActividades;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;

    @Getter    @Setter    private Date fechaActual = new Date();

//@EJB 
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbCreate ejbCreate;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        usuario = controladorEmpleado.getEmpleadoLogeado();
    }

    public void agregarNotificacionMultiple() {
        try {
            if ("".equals(menajeNot) || "".equals(nombreO) || "".equals(tipo)) {

            } else {
                nuevOBJNotificaciones.setClaveTDestino(new Personal());
                nuevOBJNotificaciones.setClaveTRemitente(new Personal());
                listaListaPersonal.clear();
                nuevOBJNotificaciones = new Notificaciones();
                nuevOBJNotificaciones.setMensaje(menajeNot);
                nuevOBJNotificaciones.getClaveTRemitente().setClave(usuario);
                nuevOBJNotificaciones.setStatus(0);
                nuevOBJNotificaciones.setFecha(fechaActual);
                switch (tipo) {
                    case "Categoria":
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaPersonalPorCategoria(nombreO);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.getClaveTDestino().setClave(nuevoOBJListaPersonal.getClave());
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a los empleados con la categoría/puesto: " + nombreO);
                        break;
                    case "Actividad":
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaPersonalPorActividad(nombreO);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.getClaveTDestino().setClave(nuevoOBJListaPersonal.getClave());
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a los empleados con la actividad: " + nombreO);
                        break;
                    case "Area":
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaPersonalPorAreaOpySu(nombreO);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.getClaveTDestino().setClave(nuevoOBJListaPersonal.getClave());
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a los empleados del área: " + nombreO);
                        break;
                    case "Personal":
                        listaListaPersonal.clear();
                        listaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosN(nombreO);
                        for (int i = 0; i <= listaListaPersonal.size() - 1; i++) {
                            nuevoOBJListaPersonal = new ListaPersonal();
                            nuevoOBJListaPersonal = listaListaPersonal.get(i);
                            nuevOBJNotificaciones.getClaveTDestino().setClave(nuevoOBJListaPersonal.getClave());
                            nuevOBJNotificaciones = ejbCreate.agregarNotificacion(nuevOBJNotificaciones);
                        }
                        Messages.addGlobalInfo("Mensaje enviado a:" + nombreO);
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
