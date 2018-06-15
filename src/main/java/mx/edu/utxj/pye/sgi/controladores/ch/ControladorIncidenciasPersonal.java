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
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasPersonal implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Integer usuario;
    @Getter    @Setter    private String tipo;
    @Getter    @Setter    private List<String> tiposIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private Incidencias nuevOBJIncidencias;
    @Getter    @Setter    private Date tiempo;
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
//@EJB    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbCreate ejbCreate;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        nuevOBJIncidencias = new Incidencias();
        tiposIncidencias.clear();
        tiposIncidencias.add("No registro entrada");
        tiposIncidencias.add("No registro salida");
        tiposIncidencias.add("Retardo menor");
        tiposIncidencias.add("Retardo mayor");
        tiposIncidencias.add("Salida anticipada");
        tiposIncidencias.add("Inasistencia");
        usuario = controladorEmpleado.getEmpleadoLogeado();
//        System.out.println("usuario " + usuario);
        mostrarLista();
    }

    public void mostrarLista() {
        try {
            listaIncidencias.clear();
            listaIncidencias = ejbSelectec.mostrarIncidencias(usuario);
//            System.out.println("listaIncidencias.siza() " + listaIncidencias.size());
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
            nuevOBJIncidencias = ejbCreate.agregarIncidencias(nuevOBJIncidencias);
            nuevOBJIncidencias = new Incidencias();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
