package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
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
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class ControladorTecnologia implements Serializable{

    private static final long serialVersionUID = 5357883394691887772L;
        
    @Getter    @Setter    private String claveTrabajador;
    @Getter    @Setter    private Integer usuario, direccionInt = 0, pestaniaActiva;
    @Getter    @Setter    private DesarrollosTecnologicos nuevoOBJDesarrolloTecnologico;
    @Getter    @Setter    private DesarrolloSoftware nuevoOBJDesarrolloSoftware;
    @Getter    @Setter    private Innovaciones nuevoOBJInnovaciones;
    @Getter    @Setter    private List<DesarrollosTecnologicos> nuevaListaDesarrolloTecnologico = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> nuevaListaDesarrolloSoftware = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> nuevaListaInnovaciones = new ArrayList<>();

    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla, numeroRegistro, accion;
    
    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    @EJB    EjbCarga carga;
    
    @EJB    private EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private EjbTecnologia ejbTecnologia;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorTecnologia Inicio: " + System.currentTimeMillis());
        usuario = controladorEmpleado.getEmpleadoLogeado();
        claveTrabajador = controladorEmpleado.getClavePersonalLogeado();

        nuevoOBJDesarrolloSoftware = new DesarrolloSoftware();
        nuevoOBJDesarrolloTecnologico = new DesarrollosTecnologicos();
        nuevoOBJInnovaciones = new Innovaciones();

        nuevaListaDesarrolloSoftware.clear();
        nuevaListaDesarrolloTecnologico.clear();
        nuevaListaInnovaciones.clear();

        listaConsultas();
        System.out.println("ControladorTecnologia Fin: " + System.currentTimeMillis());
    }

    //////////////////////////////Desarrollos Tecnológicos///////////////////////////////
    public void guardarDesarrolloTec() {
        try {
            nuevoOBJDesarrolloTecnologico.setClavePersonal(new Personal());
            nuevoOBJDesarrolloTecnologico.getClavePersonal().setClave(usuario);
            nuevoOBJDesarrolloTecnologico.setEstatus("Denegado");

            nuevoOBJDesarrolloTecnologico = ejbTecnologia.crearNuevoDesarrollosTecnologicos(nuevoOBJDesarrolloTecnologico);
            nombreTabla = "Desarrollo Tecnológico";
            numeroRegistro = nuevoOBJDesarrolloTecnologico.getDesarrollo().toString();
            accion = "Insert";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJDesarrolloTecnologico = new DesarrollosTecnologicos();

            listaConsultas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarDesTec() {
        try {
            if (nuevoOBJDesarrolloTecnologico.getDocumentoRespaldo() != null) {
                CargaArchivosCH.eliminarArchivo(nuevoOBJDesarrolloTecnologico.getDocumentoRespaldo());
            }
            nombreTabla = "Desarrollo Tecnológico";
            numeroRegistro = nuevoOBJDesarrolloTecnologico.getDesarrollo().toString();
            accion = "Delate";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJDesarrolloTecnologico = ejbTecnologia.eliminarDesarrollosTecnologicos(nuevoOBJDesarrolloTecnologico);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJDesarrolloTecnologico = new DesarrollosTecnologicos();
            listaConsultas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDesarrolloTecnologico() {
        try {
            nombreTabla = "Desarrollo Tecnológico";
            numeroRegistro = nuevoOBJDesarrolloTecnologico.getDesarrollo().toString();
            accion = "Update";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJDesarrolloTecnologico = ejbTecnologia.actualizarDesarrollosTecnologicos(nuevoOBJDesarrolloTecnologico);
            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////Innovaciones///////////////////////////////
    public void guardarInnovacion() {
        try {
            nuevoOBJInnovaciones.setClavePersonal(new Personal());
            nuevoOBJInnovaciones.getClavePersonal().setClave(usuario);
            nuevoOBJInnovaciones.setEstatus("Aceptado");

            nuevoOBJInnovaciones = ejbTecnologia.crearNuevoInnovaciones(nuevoOBJInnovaciones);
            nombreTabla = "Innovaciones";
            numeroRegistro = nuevoOBJInnovaciones.getInnovacion().toString();
            accion = "Insert";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJInnovaciones = new Innovaciones();
            listaConsultas();
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarInnovacion() {
        try {
            nombreTabla = "Innovaciones";
            numeroRegistro = nuevoOBJInnovaciones.getInnovacion().toString();
            accion = "Delate";
            agregaBitacora();
            nuevoOBJInnovaciones = ejbTecnologia.eliminarInnovaciones(nuevoOBJInnovaciones);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJInnovaciones = new Innovaciones();
            listaConsultas();
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateInnovaciones() {
        try {
            nombreTabla = "Innovaciones";
            numeroRegistro = nuevoOBJInnovaciones.getInnovacion().toString();
            accion = "Update";
            agregaBitacora();
            nuevoOBJInnovaciones = ejbTecnologia.actualizarInnovaciones(nuevoOBJInnovaciones);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////Desarrollo de Software///////////////////////////////
    public void guardarDesaSoft() {
        try {
            nuevoOBJDesarrolloSoftware.setClavePersonal(new Personal());
            nuevoOBJDesarrolloSoftware.getClavePersonal().setClave(usuario);
            nuevoOBJDesarrolloSoftware.setEstatus("Aceptado");

            nuevoOBJDesarrolloSoftware = ejbTecnologia.crearNuevoDesarrolloSoftware(nuevoOBJDesarrolloSoftware);
            nombreTabla = "Desarrollo de Software";
            numeroRegistro = nuevoOBJDesarrolloSoftware.getDesarrollo().toString();
            accion = "Insert";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJDesarrolloSoftware = new DesarrolloSoftware();
            listaConsultas();
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarDesSoft() {
        try {
            nombreTabla = "Desarrollo de Software";
            numeroRegistro = nuevoOBJDesarrolloSoftware.getDesarrollo().toString();
            accion = "Delate";
            agregaBitacora();
            nuevoOBJDesarrolloSoftware = ejbTecnologia.eliminarDesarrolloSoftware(nuevoOBJDesarrolloSoftware);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJDesarrolloSoftware = new DesarrolloSoftware();
            listaConsultas();
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDesarrolloSoftware() {
        try {
            nombreTabla = "Desarrollo de Software";
            numeroRegistro = nuevoOBJDesarrolloSoftware.getDesarrollo().toString();
            accion = "Update";
            agregaBitacora();
            nuevoOBJDesarrolloSoftware = ejbTecnologia.actualizarDesarrolloSoftware(nuevoOBJDesarrolloSoftware);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////////////Consultas de mostrado de información
    public void listaConsultas() {
        try {
            nuevaListaDesarrolloTecnologico = ejbTecnologia.mostrarDesarrollosTecnologicos(usuario);
            nuevaListaDesarrolloSoftware = ejbTecnologia.mostrarDesarrolloSoftware(usuario);
            nuevaListaInnovaciones = ejbTecnologia.mostrarInnovaciones(usuario);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaDesTec() {
        if (file != null) {
            ruta = carga.subir(file, new File(claveTrabajador.concat(File.separator).concat("desarrolloTecnologico").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevoOBJDesarrolloTecnologico.setDocumentoRespaldo(ruta);
//                System.out.println("mx.edu.utxj.pye.subir.controlador.Subir.upload() res:" + nuevoOBJDesarrolloTecnologico.getDocumentoRespaldo());
                direccionInt = 1;
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }

    public String convertirRuta(String ruta) {
        if (nuevoOBJDesarrolloTecnologico.getDocumentoRespaldo() != null) {
            File file = new File(nuevoOBJDesarrolloTecnologico.getDocumentoRespaldo());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public void agregaBitacora() {
        try {
            Date fechaActual = new Date();
            nuevaBitacoraacceso = new Bitacoraacceso();
            nuevaBitacoraacceso.setClavePersonal(usuario);
            nuevaBitacoraacceso.setNumeroRegistro(numeroRegistro);
            nuevaBitacoraacceso.setTabla(nombreTabla);
            nuevaBitacoraacceso.setAccion(accion);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            nuevaBitacoraacceso = ejbDatosUsuarioLogeado.crearBitacoraacceso(nuevaBitacoraacceso);

            nombreTabla = "";
            numeroRegistro = "";
            accion = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
