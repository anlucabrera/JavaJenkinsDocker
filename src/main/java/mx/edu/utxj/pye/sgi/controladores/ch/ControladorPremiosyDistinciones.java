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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;  
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class ControladorPremiosyDistinciones implements Serializable{
    
    private static final long serialVersionUID = -6775211516127859011L;
    
    @Getter    @Setter    private Integer usuario, direccionInt = 0;
    @Getter    @Setter    private String claveTrabajador;
    @Getter    @Setter    private Distinciones nuevoOBJDistinciones;
    @Getter    @Setter    private List<Distinciones> nuevaListaDistinciones = new ArrayList<>();

    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla, numeroRegistro, accion;
        
    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    
    @EJB    EjbCarga carga;
    @EJB    private EjbPremios ejbPremios;
    @EJB    private EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;

    @Inject    ControladorEmpleado controladorEmpleado;
    
    @PostConstruct
    public void init() {
        System.out.println("ControladorPremiosyDistinciones Inicio: " + System.currentTimeMillis());
        usuario = controladorEmpleado.getEmpleadoLogeado();
        claveTrabajador = controladorEmpleado.getClavePersonalLogeado();
        nuevoOBJDistinciones = new Distinciones();
        nuevaListaDistinciones.clear();
        distincionesRegistradas();
        System.out.println("ControladorPremiosyDistinciones Fin: " + System.currentTimeMillis());
    }

    public void createDistincion() {
        try {
            nuevoOBJDistinciones.setClaveEmpleado(new Personal());
            nuevoOBJDistinciones.getClaveEmpleado().setClave(usuario);
            nuevoOBJDistinciones.setEstatus("Denegado");
            nuevoOBJDistinciones = ejbPremios.crearNuevoDistinciones(nuevoOBJDistinciones);
            nombreTabla = "Distinciones";
            numeroRegistro = nuevoOBJDistinciones.getDistincion().toString();
            accion = "Insert";
            agregaBitacora();
            nuevoOBJDistinciones = new Distinciones();

            Messages.addGlobalInfo("¡Operación exitosa!!");
            distincionesRegistradas();
            direccionInt = 0;
        } catch (Throwable ex) {
            Messages.addGlobalWarn("Error al intentar guardar información");
            Logger.getLogger(ControladorPremiosyDistinciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Eventos
    public void agregarEvidenciaDistincion() {
        if (file != null) {
            ruta = carga.subir(file, new File(claveTrabajador.concat(File.separator).concat("premiosDistinciones").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevoOBJDistinciones.setEvidenciaDistincion(ruta);
//                System.out.println("mx.edu.utxj.pye.subir.controlador.Subir.upload() res:" + nuevoOBJDistinciones.getEvidenciaDistincion());
                direccionInt = 1;
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

    public void distincionesRegistradas() {
        try {
            nuevaListaDistinciones = ejbPremios.mostrarDistinciones(usuario);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPremiosyDistinciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delDistincion(Distinciones distinciones) {
        try {
            if (distinciones.getEvidenciaDistincion() != null) {
                CargaArchivosCH.eliminarArchivo(distinciones.getEvidenciaDistincion());
            }
            nombreTabla = "Distinciones";
            numeroRegistro = distinciones.getDistincion().toString();
            accion = "Delate";
            agregaBitacora();
            ejbPremios.eliminarDistinciones(distinciones);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            distincionesRegistradas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorPremiosyDistinciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDistinciones() {
        try {
            nombreTabla = "Distinciones";
            numeroRegistro = nuevoOBJDistinciones.getDistincion().toString();
            accion = "Update";
            agregaBitacora();
            nuevoOBJDistinciones = ejbPremios.actualizarDistinciones(nuevoOBJDistinciones);
            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorPremiosyDistinciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String convertirRuta(String ruta) {
        if (nuevoOBJDistinciones.getEvidenciaDistincion() != null) {
            File file = new File(nuevoOBJDistinciones.getEvidenciaDistincion());
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

public void metodoBase() {
        
    }
}
