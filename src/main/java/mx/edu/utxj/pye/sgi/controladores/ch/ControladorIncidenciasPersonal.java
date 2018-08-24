package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasPersonal implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Integer usuario,incidenciasRegistradas=0;
    @Getter    @Setter    private String tipo;
    @Getter    @Setter    private List<String> tiposIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private Incidencias nuevOBJIncidencias;
    @Getter    @Setter    private Date tiempo;
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    @Getter    @Setter    private Date fechaActual = new Date();
//@EJB    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorIncidenciasPersonal Inicio: " + System.currentTimeMillis());
        fechaActual = new Date();
        incidenciasRegistradas=0;
        nuevOBJIncidencias = new Incidencias();
        tiposIncidencias.clear();
        tiposIncidencias.add("No registro entrada");
        tiposIncidencias.add("No registro salida");
        tiposIncidencias.add("Retardo menor");
        tiposIncidencias.add("Retardo mayor");
        tiposIncidencias.add("Salida anticipada");
        tiposIncidencias.add("Inasistencia");
        usuario = controladorEmpleado.getEmpleadoLogeado();
        mostrarLista();
        System.out.println("ControladorIncidenciasPersonal Fin: " + System.currentTimeMillis());
    }

    public void mostrarLista() {
        try {
            fechaActual = new Date();
            incidenciasRegistradas = 0;
            listaIncidencias.clear();
            listaIncidencias = ejbNotificacionesIncidencias.mostrarIncidencias(usuario);
            if (!listaIncidencias.isEmpty()) {

                listaIncidencias.forEach((f) -> {
                    System.out.println("mes " + f.getFecha().getMonth() + " año " + f.getFecha().getYear());
                    System.out.println("mes " + fechaActual.getMonth() + " año " + fechaActual.getYear());
                    System.out.println("-------------------------------------------------------------------");
                    if (f.getFecha().getMonth() == fechaActual.getMonth() && f.getFecha().getYear() == fechaActual.getYear()) {
                        if (fechaActual.getDay() <= 15) {
                            incidenciasRegistradas = incidenciasRegistradas + 1;
                        }
                    }
                });
                if (incidenciasRegistradas == 2) {
                    Ajax.oncomplete("PF('bloqueoIncidencias').show();");
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearIncidencia() {
        try {
            nuevOBJIncidencias.setClavePersonal(new Personal());
            nuevOBJIncidencias.getClavePersonal().setClave(usuario);
            nuevOBJIncidencias.setEstatus("Pendiente");
            nuevOBJIncidencias.setTiempo(dateFormat.format(tiempo));
            nuevOBJIncidencias = ejbNotificacionesIncidencias.agregarIncidencias(nuevOBJIncidencias);
            nuevOBJIncidencias = new Incidencias();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
