package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
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
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class ControladorHabilidadesIIL implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;
    
    @Getter    @Setter    private List<Lenguas> nuevaListaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> nuevaListaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> nuevaListaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<String> listaConocimiento = new ArrayList<>();
    @Getter    @Setter    private Lenguas nuevOBJLenguas, nuevOBJLenguaSelectec;
    @Getter    @Setter    private Idiomas nuevOBJIdiomas, nuevOBJIdiomaSelectec;
    @Getter    @Setter    private HabilidadesInformaticas nuevOBJHabilidadesInformaticas, nuevOBJHabilidadesInformaticaSelectec;
    @Getter    @Setter    private Integer personal, direccionInt = 0, pestaniaActiva;
    @Getter    @Setter    private String claveTraba;
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Date fechaC = new Date();
    @Getter    @Setter    private Date fechaO = new Date();
    @Getter    @Setter    private Date fechaF = new Date();

    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla, numeroRegistro, accion;
    
    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    @EJB    EjbCarga carga;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades habilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado datosUsuarioLogeado;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorHabilidadesIIL Inicio: " + System.currentTimeMillis());
        personal = controladorEmpleado.getEmpleadoLogeado();
        claveTraba = controladorEmpleado.getClavePersonalLogeado();

        nuevOBJIdiomas = new Idiomas();
        nuevOBJLenguas = new Lenguas();
        nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();
        nuevOBJHabilidadesInformaticas.setNivelConocimiento("Nivel Usuario");

        nuevaListaIdiomas.clear();
        nuevaListaLenguas.clear();
        nuevaListaHabilidadesInformaticas.clear();

        mostrarListas();
        System.out.println(" ControladorHabilidadesIIL Fin: " + System.currentTimeMillis());
    }

/////////////////////////////////////////////////////////////////////////////Lenguas\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createLengua() {
        try {
            nuevOBJLenguas.setClavePersonal(new Personal());
            nuevOBJLenguas.getClavePersonal().setClave(personal);
            nuevOBJLenguas.setEstatus("Aceptado");

            if (nuevOBJLenguas.getLengua() != null) {
                actualizaLengua();
            } else {
                nuevOBJLenguas = habilidades.crearNuevoLenguas(nuevOBJLenguas);
                nombreTabla = "Lenguas";
                numeroRegistro = nuevOBJLenguas.getLengua().toString();
                accion = "Insert";
                agregaBitacora();
                Messages.addGlobalInfo("¡Operación exitosa!!");
                nuevOBJLenguas = new Lenguas();
                mostrarListas();
                direccionInt = 0;
                pestaniaActiva = 2;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaLengua() {
        try {
            nombreTabla = "Lenguas";
            numeroRegistro = nuevOBJLenguaSelectec.getLengua().toString();
            accion = "Delate";
            agregaBitacora();
            nuevOBJLenguaSelectec = habilidades.eliminarLenguas(nuevOBJLenguaSelectec);
            nuevOBJLenguaSelectec = null;
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizaLengua() {
        try {
            nombreTabla = "Lenguas";
            numeroRegistro = nuevOBJLenguaSelectec.getLengua().toString();
            accion = "Update";
            agregaBitacora();
            nuevOBJLenguaSelectec = nuevOBJLenguas;
            nuevOBJLenguaSelectec = habilidades.actualizarLenguas(nuevOBJLenguaSelectec);
            nuevOBJLenguaSelectec = null;
            nuevOBJLenguas = new Lenguas();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/////////////////////////////////////////////////////////////////////////////Idiomas\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public void createIdioma() {
        try {
            nuevOBJIdiomas.setClavePersonal(new Personal());
            nuevOBJIdiomas.setFechaEvaluacion(fechaC);
            nuevOBJIdiomas.setFechaVigenciaA(fechaO);
            nuevOBJIdiomas.setFechaVigenciaDe(fechaF);
            nuevOBJIdiomas.getClavePersonal().setClave(personal);
            nuevOBJIdiomas.setEstatus("Denegado");

            if (nuevOBJIdiomas.getIdioma() != null) {
                actualizaIdioma();
            } else {
                nuevOBJIdiomas = habilidades.crearNuevoIdiomas(nuevOBJIdiomas);
                nombreTabla = "Idiomas";
                numeroRegistro = nuevOBJIdiomas.getIdioma().toString();
                accion = "Insert";
                agregaBitacora();
                Messages.addGlobalInfo("¡Operación exitosa!!");
                nuevOBJIdiomas = new Idiomas();
                mostrarListas();
                direccionInt = 0;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaIdioma() {
        try {
            if (nuevOBJIdiomaSelectec.getEvidenciaDoc() != null) {
                CargaArchivosCH.eliminarArchivo(nuevOBJIdiomaSelectec.getEvidenciaDoc());
            }
            nombreTabla = "Idiomas";
            numeroRegistro = nuevOBJIdiomaSelectec.getIdioma().toString();
            accion = "Delate";
            agregaBitacora();
            nuevOBJIdiomaSelectec = habilidades.eliminarIdiomas(nuevOBJIdiomaSelectec);
            nuevOBJIdiomaSelectec = null;
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizaIdioma() {
        try {
            nuevOBJIdiomaSelectec = nuevOBJIdiomas;
            nombreTabla = "Idiomas";
            numeroRegistro = nuevOBJIdiomaSelectec.getIdioma().toString();
            accion = "Update";
            agregaBitacora();
            nuevOBJIdiomaSelectec = habilidades.actualizarIdiomas(nuevOBJIdiomaSelectec);
            nuevOBJIdiomaSelectec = null;
            nuevOBJIdiomas = new Idiomas();
            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//////////////////////////////////////////////////////////////////////////Informaticas\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void createrInformaticas() {
        try {
            nuevOBJHabilidadesInformaticas.setClavePersonal(new Personal());
            nuevOBJHabilidadesInformaticas.getClavePersonal().setClave(personal);
            nuevOBJHabilidadesInformaticas.setEstatus("Aceptado");
            if (nuevOBJHabilidadesInformaticas.getConocimiento() != null) {
                actualizaInformaticas();
            } else {
                nuevOBJHabilidadesInformaticas = habilidades.crearNuevoHabilidadesInformaticas(nuevOBJHabilidadesInformaticas);
                nombreTabla = "Habilidades Informáticas";
                numeroRegistro = nuevOBJHabilidadesInformaticas.getConocimiento().toString();
                accion = "Insert";
                agregaBitacora();
                Messages.addGlobalInfo("¡Operación exitosa!!");
                mostrarListas();
                listaConocimiento.clear();
                nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();
                nuevOBJHabilidadesInformaticas.setNivelConocimiento("Nivel Usuario");
            }
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaInformaticas() {
        try {
            nombreTabla = "Habilidades Informáticas";
            numeroRegistro = nuevOBJHabilidadesInformaticaSelectec.getConocimiento().toString();
            accion = "Delate";
            agregaBitacora();
            nuevOBJHabilidadesInformaticaSelectec = habilidades.eliminarHabilidadesInformaticas(nuevOBJHabilidadesInformaticaSelectec);
            nuevOBJHabilidadesInformaticaSelectec = null;
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizaInformaticas() {
        try {
            nuevOBJHabilidadesInformaticaSelectec = nuevOBJHabilidadesInformaticas;
            nombreTabla = "Habilidades Informáticas";
            numeroRegistro = nuevOBJHabilidadesInformaticaSelectec.getConocimiento().toString();
            accion = "Update";
            agregaBitacora();
            nuevOBJHabilidadesInformaticaSelectec = habilidades.actualizarHabilidadesInformaticas(nuevOBJHabilidadesInformaticaSelectec);
            nuevOBJHabilidadesInformaticaSelectec = null;
            nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();
            nuevOBJHabilidadesInformaticas.setNivelConocimiento("Nivel Usuario");
            Messages.addGlobalInfo("¡Operación exitosa!!");
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
////////////////////////////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void subirEvidenciaIdioma() {
        if (file != null) {
            ruta = carga.subir(file, new File(claveTraba.concat(File.separator).concat("idiomas").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJIdiomas.setEvidenciaDoc(ruta);
//                System.out.println("mx.edu.utxj.pye.subir.controlador.Subir.upload() res:" + nuevOBJIdiomas.getEvidenciaDoc());
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

    public void mostrarListas() {
        try {
            nuevaListaHabilidadesInformaticas.clear();
            nuevaListaIdiomas.clear();
            nuevaListaLenguas.clear();
            nuevaListaHabilidadesInformaticas = habilidades.mostrarHabilidadesInformaticas(personal);
            nuevaListaIdiomas = habilidades.mostrarIdiomas(personal);
            nuevaListaLenguas = habilidades.mostrarLenguas(personal);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorHabilidadesIIL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/////////////////////////  filtrado de elementos

    public void filtarConocimientos() {
        listaConocimiento.clear();
        switch (nuevOBJHabilidadesInformaticas.getTipoConocimiento()) {
            case "Ofimática":
                listaConocimiento.add("Autoedición");
                listaConocimiento.add("Hojas de cálculo");
                listaConocimiento.add("Internet");
                listaConocimiento.add("Mail");
                listaConocimiento.add("Procesadores de texto");
                break;
            case "Base de datos":
                listaConocimiento.add("Access");
                listaConocimiento.add("Datawarehouse");
                listaConocimiento.add("DB2");
                listaConocimiento.add("Oracle");
                listaConocimiento.add("SQL Server");
                break;
            case "Diseño":
                listaConocimiento.add("3D Studio");
                listaConocimiento.add("Adobe After Effects");
                listaConocimiento.add("Adobe Premiere");
                listaConocimiento.add("CAD");
                listaConocimiento.add("Cinema 4D");
                listaConocimiento.add("CorelDRAW");
                listaConocimiento.add("Dreamweaver");
                listaConocimiento.add("Flash");
                listaConocimiento.add("Freehand");
                listaConocimiento.add("Photoshop");
                listaConocimiento.add("Quar kpress");
                break;
            case "Herramientas de Gestión":
                listaConocimiento.add("CRM");
                listaConocimiento.add("e-comerce");
                listaConocimiento.add("GIS");
                listaConocimiento.add("Meta4");
                listaConocimiento.add("People  Soft");
                listaConocimiento.add("SAP");
                break;
            case "Lenguajes de Programación":
                listaConocimiento.add("ASP/.NET");
                listaConocimiento.add("C/C++");
                listaConocimiento.add("HTML");
                listaConocimiento.add("Java");
                listaConocimiento.add("JavaScript");
                listaConocimiento.add("SQL");
                listaConocimiento.add("XML");
                break;
            case "Paquetes Integrados":
                listaConocimiento.add("Lotus SmartSuite");
                listaConocimiento.add("Microsoft Office");
                listaConocimiento.add("MS Project");
                listaConocimiento.add("Viso");
                break;
            case "Sistemas Operativos":
                listaConocimiento.add("DOS");
                listaConocimiento.add("Linux");
                listaConocimiento.add("Macintosh");
                listaConocimiento.add("Os/xx");
                listaConocimiento.add("UNIX");
                listaConocimiento.add("Windows");
                break;
            case "Comunicación":
                listaConocimiento.add("Internet");
                listaConocimiento.add("Redes");
                listaConocimiento.add("Routers");
                listaConocimiento.add("TCP/IP");
                listaConocimiento.add("VoIP");
                listaConocimiento.add("WAP");
                listaConocimiento.add("Wireless");
                break;
            case "Internet":
                listaConocimiento.add("Adwords");
                listaConocimiento.add("Analytics");
                listaConocimiento.add("Blogger");
                listaConocimiento.add("Google APIs");
                listaConocimiento.add("SEM");
                listaConocimiento.add("SEO");
                listaConocimiento.add("SMO");
                listaConocimiento.add("Wordpress");
                break;
        }
    }

    public String convertirRuta(String ruta) {
        if (nuevOBJIdiomaSelectec.getEvidenciaDoc() != null) {
            File file = new File(nuevOBJIdiomaSelectec.getEvidenciaDoc());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public void actualizarObjetosIdioma() {
        if (nuevOBJIdiomaSelectec.getIdioma() != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ControladorHabilidadesIIL.actualizarObjetosIdioma()");
            nuevOBJIdiomas = nuevOBJIdiomaSelectec;
        } else {
            Messages.addGlobalWarn("No es posible actualizar el registro!!");
        }
    }

    public void actualizarObjetosHabilidadesInformaticas() {
        if (nuevOBJHabilidadesInformaticaSelectec.getConocimiento() != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ControladorHabilidadesIIL.actualizarObjetosHabilidadesInformaticas()");
            nuevOBJHabilidadesInformaticas = nuevOBJHabilidadesInformaticaSelectec;
            filtarConocimientos();
        } else {
            Messages.addGlobalWarn("No es posible actualizar el registro!!");
        }
    }

    public void actualizarObjetosLengua() {
        if (nuevOBJLenguaSelectec.getLengua() != null) {
//            System.out.println("mx.edu.utxj.pye.sgi.ch.controladores.ControladorHabilidadesIIL.actualizarObjetosLengua()");
            nuevOBJLenguas = nuevOBJLenguaSelectec;
        } else {
            Messages.addGlobalWarn("No es posible actualizar el registro!!");
        }
    }

    public void agregaBitacora() {
        try {
            Date fechaActual = new Date();
            nuevaBitacoraacceso = new Bitacoraacceso();
            nuevaBitacoraacceso.setClavePersonal(personal);
            nuevaBitacoraacceso.setNumeroRegistro(numeroRegistro);
            nuevaBitacoraacceso.setTabla(nombreTabla);
            nuevaBitacoraacceso.setAccion(accion);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            nuevaBitacoraacceso = datosUsuarioLogeado.crearBitacoraacceso(nuevaBitacoraacceso);

            nombreTabla = "";
            numeroRegistro = "";
            accion = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
