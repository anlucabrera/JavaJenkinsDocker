package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import org.omnifaces.util.Ajax;
import org.primefaces.event.RowEditEvent;


@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasPersonal implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Integer usuario,pestaniaActiva;
    @Getter    @Setter    private String tipo;
    @Getter    @Setter    private List<String> tiposIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidades = new ArrayList<>();
    @Getter    @Setter    private Incidencias nuevOBJIncidencias=new Incidencias();
    @Getter    @Setter    private Incapacidad nuevOBJIncapacidad=new Incapacidad();
    @Getter    @Setter    private Date tiempo,tiempoEdicion;
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    @Getter    @Setter    private Date fechaActual = new Date();
    @Getter    @Setter    private Boolean registro = true,activo=false;
    @Getter    @Setter    private Date fechaI = new Date();
    @Getter    @Setter    private Date fechaF = new Date();

    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    
    @EJB    EjbCarga carga;
//@EJB    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorIncidenciasPersonal Inicio: " + System.currentTimeMillis());
        fechaActual = new Date();
        registro = true;
        activo = false;
        nuevOBJIncidencias = new Incidencias();
        nuevOBJIncapacidad = new Incapacidad();
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
            DateFormat dateFormatF = new SimpleDateFormat("dd/MM/yyyy");
            fechaActual = new Date();
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();
            listaIncapacidades = new ArrayList<>();
            listaIncapacidades.clear();

            listaIncidencias = ejbNotificacionesIncidencias.mostrarIncidencias(usuario);
            listaIncapacidades = ejbNotificacionesIncidencias.mostrarIncapacidad(usuario);

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.mostrarLista()" + listaIncidencias.size());
            List<Incidencias> incidenciases = new ArrayList<>();
            incidenciases.clear();
            fechaI = new Date();
            fechaF = new Date();
            String mes = "";
            if (fechaActual.getMonth() <= 8) {
                mes = "0" + (fechaActual.getMonth() + 1);
            } else {
                mes = String.valueOf(fechaActual.getMonth() + 1);
            }
            System.out.println("dia " + fechaActual.getDate() + " mes" + fechaActual.getMonth());
            if (fechaActual.getDate()<= 15) {
                fechaI = dateFormatF.parse("01/" + mes + "/20" + (fechaActual.getYear() - 100));
                fechaF = dateFormatF.parse("15/" + mes + "/20" + (fechaActual.getYear() - 100));
            } else {
                fechaI = dateFormatF.parse("16/" + mes + "/20" + (fechaActual.getYear() - 100));
                fechaF = dateFormatF.parse("31/" + mes + "/20" + (fechaActual.getYear() - 100));
            }
            if (!listaIncidencias.isEmpty()) {
                listaIncidencias.forEach((i) -> {
                    if (i.getFecha().after(fechaI) && i.getFecha().before(fechaF)) {
                        incidenciases.add(i);
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.mostrarLista()" + incidenciases.size());
            if (!incidenciases.isEmpty()) {
                if (incidenciases.size() >= 2) {
                    registro = false;
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearIncidencia() {
        try {
            nuevOBJIncidencias.setClavePersonal(new Personal());
            nuevOBJIncidencias.getClavePersonal().setClave(usuario);
            nuevOBJIncidencias.setEstatus("Pendiente");
            nuevOBJIncidencias.setTiempo(dateFormat.format(tiempo));
            Integer dias = (int) ((fechaActual.getTime() - nuevOBJIncidencias.getFecha().getTime()) / 86400000);
            Integer maximo = 0;
            if (nuevOBJIncidencias.getFecha().getDay() == 5) {
                maximo = 3;
            } else {
                maximo = 1;
            }
            if (dias <= maximo) {
                nuevOBJIncidencias = ejbNotificacionesIncidencias.agregarIncidencias(nuevOBJIncidencias);
                nuevOBJIncidencias = new Incidencias();
                Messages.addGlobalInfo("¡Operación exitosa!!");
            } else {
                Messages.addGlobalWarn("¡El tiempo maximo para el registro de incidencia ya expiro!!");
            }
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void crearIncapacidad() {
        try {
            nuevOBJIncapacidad.setClavePersonal(new Personal());
            nuevOBJIncapacidad.getClavePersonal().setClave(usuario);
            nuevOBJIncapacidad = ejbNotificacionesIncidencias.agregarIncapacidad(nuevOBJIncapacidad);
            nuevOBJIncapacidad = new Incapacidad();
            Messages.addGlobalInfo("¡Operación exitosa!!");

            pestaniaActiva = 1;
            mostrarLista();
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaIncidencia() {
        if (file != null) {
            ruta = carga.subir(file, new File(usuario.toString().concat(File.separator).concat("Incidencia").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJIncidencias.setEvidencia(ruta);
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
        file = null;
    }

    public void agregarEvidenciaIncapacidad() {
        if (file != null) {
            ruta = carga.subir(file, new File(usuario.toString().concat(File.separator).concat("premiosDistinciones").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJIncapacidad.setEvidencia(ruta);
                activo = true;
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
                activo = false;
            }            
            pestaniaActiva = 1;
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
            activo = false;
        }
        file = null;
    }

    public void eliminarIncidencia(Incidencias incidencias) {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.eliminarIncidencia()");
            if (incidencias.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(incidencias.getEvidencia());
            }
            ejbNotificacionesIncidencias.eliminarIncidencias(incidencias);
            mostrarLista();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.eliminarIncidencia()");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Incidencias incidencias = (Incidencias) event.getObject();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.onRowEdit(1)" + tiempoEdicion);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.onRowEdit(2)" + incidencias.getTiempo());
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.onRowEdit(3)" + dateFormat.parse(incidencias.getTiempo()));
            incidencias.setTiempo(dateFormat.format( dateFormat.parse(incidencias.getTiempo())));
            ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     public void onRowEditInca(RowEditEvent event) {
        try {
            Incapacidad incapacidad = (Incapacidad) event.getObject();
            ejbNotificacionesIncidencias.actualizarIncapacidad(incapacidad);
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!!");
    }
    
     public String convertirRutaVistaEvidencia(String ruta) {
        if (!"".equals(ruta)) {
            File file = new File(ruta);
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public void metodoBase() {
        
    }
}
