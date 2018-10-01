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
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class ControladorProduccionPersonal implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    @Getter    @Setter    private List<LibrosPub> nuevaListaLibrosPub = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> nuevaListaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> nuevaListaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> nuevaListatInvestigaciones = new ArrayList<>(),nuevaListatTrabajos=new ArrayList<>(),nuevaListatProyectos=new ArrayList<>();
    @Getter    @Setter    private List<Congresos> nuevaListaCongresos = new ArrayList<>();
    @Getter    @Setter    private List<String> listaSubClase = new ArrayList<>();

    @Getter    @Setter    private LibrosPub nuevOBJLibrosPub, selcNuevOBJLibrosPub;
    @Getter    @Setter    private Memoriaspub nuevOBJMemoriaspub, selcNuevOBJMemoriaspub;
    @Getter    @Setter    private Articulosp nuevOBJArticulosp, selcNuevOBJArticulosp;
    @Getter    @Setter    private Investigaciones nuevOBJInvestigaciones = new Investigaciones(), nuevOBJInvestigacionesSelectec;
    @Getter    @Setter    private Congresos nuevoOBJCongresos = new Congresos(), nuevoOBJCongresoSelectec = new Congresos();

    @Getter    @Setter    private Integer personal, direccionInt1 = 0, direccionInt2 = 0, direccionInt3 = 0, traduccion, pestaniaActiva;
    @Getter    @Setter    private String anioPub, anioEdi, clase, tipoDr = "", nombre = "", istitucion = "", descripcion = "";
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Date anio1 = new Date();
    @Getter    @Setter    private Date anio2 = new Date();
    @Getter    @Setter    private Date fechaI = new Date();
    @Getter    @Setter    private Date fechaF = null;
    @Getter    @Setter    private Date fechaO = new Date();

    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla, numeroRegistro, accion;
    
    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    @EJB    EjbCarga carga;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorProduccionPersonal Inicio: " + System.currentTimeMillis());
        personal = controladorEmpleado.getEmpleadoLogeado();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorProduccionPersonal.init()"+personal);
        nuevaListaArticulosp.clear();
        nuevaListaLibrosPub.clear();
        nuevaListaMemoriaspub.clear();
        nuevaListaCongresos.clear();
        nuevaListatInvestigaciones.clear();

        nuevOBJArticulosp = new Articulosp();
        nuevOBJLibrosPub = new LibrosPub();
        nuevOBJMemoriaspub = new Memoriaspub();
        nuevoOBJCongresos = new Congresos();
        nuevOBJInvestigaciones = new Investigaciones();

        mostrarListas();
        System.out.println("ControladorProduccionPersonal Fin: " + System.currentTimeMillis());
    }

/////////////////////////////////////////////////////////////////////////////Libros\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createLibros() {
        try {
            nuevOBJLibrosPub.setClavePersonal(new Personal());
            anio1 = dateFormat.parse("01/01/" + anioPub);
            anio2 = dateFormat.parse("01/01/" + anioEdi);
            nuevOBJLibrosPub.setAnioEdicion(anio2);
            nuevOBJLibrosPub.setAnioPub(anio1);
            nuevOBJLibrosPub.setEstatus("Denegado");
            nuevOBJLibrosPub.getClavePersonal().setClave(personal);
            nuevOBJLibrosPub = ejbProduccionProfecional.crearNuevoLibrosPub(nuevOBJLibrosPub);
            nombreTabla = "Libros Publicados";
            numeroRegistro = nuevOBJLibrosPub.getLibrosp().toString();
            accion = "Insert";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            nuevOBJLibrosPub = new LibrosPub();
            anioEdi = "";
            anioPub = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaLibros(LibrosPub librosPub) {
        try {
            if (librosPub.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(librosPub.getEvidencia());
            }
            nombreTabla = "Libros Publicados";
            numeroRegistro = librosPub.getLibrosp().toString();
            accion = "Delate";
            agregaBitacora();
            ejbProduccionProfecional.eliminarLibrosPub(librosPub);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/////////////////////////////////////////////////////////////////////////////Articulos\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    
    public void createArticulos() {
        try {
            nuevOBJArticulosp.setClavePersonal(new Personal());
            anio1 = dateFormat.parse("01/01/" + anioPub);
            anio2 = dateFormat.parse("01/01/" + anioEdi);
            nuevOBJArticulosp.setEnioEdicion(anio2);
            nuevOBJArticulosp.setAnioPublicacion(anio1);
            nuevOBJArticulosp.setEstatus("Denegado");
            nuevOBJArticulosp.getClavePersonal().setClave(personal);

            nuevOBJArticulosp = ejbProduccionProfecional.crearNuevoArticulosp(nuevOBJArticulosp);
            nombreTabla = "Artículos Publicados";
            numeroRegistro = nuevOBJArticulosp.getArticuloId().toString();
            accion = "Insert";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            nuevOBJArticulosp = new Articulosp();
            anioEdi = "";
            anioPub = "";
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaArticulos(Articulosp articulosp) {
        try {
            if (articulosp.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(articulosp.getEvidencia());
            }
            nombreTabla = "Artículos Publicados";
            numeroRegistro = articulosp.getArticuloId().toString();
            accion = "Delate";
            agregaBitacora();
            ejbProduccionProfecional.eliminarArticulosp(articulosp);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//////////////////////////////////////////////////////////////////////////Memorias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createrMemorias() {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorProduccionPersonal.createrMemorias()"+personal);
            nuevOBJArticulosp.setClavePersonal(new Personal());
            anio1 = dateFormat.parse("01/01/" + anioPub);
            nuevOBJMemoriaspub.setAnioDePublicacion(anio1);
            nuevOBJMemoriaspub.setEstatus("Denegado");
            nuevOBJMemoriaspub.setClavePersonal(new Personal(personal));
            nuevOBJMemoriaspub = ejbProduccionProfecional.crearNuevoMemoriaspub(nuevOBJMemoriaspub);
            nombreTabla = "Memorias Publicadas";
            numeroRegistro = nuevOBJMemoriaspub.getMemoriaID().toString();
            accion = "Insert";
            agregaBitacora();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            nuevOBJMemoriaspub = new Memoriaspub();
            anioEdi = "";
            anioPub = "";
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaMemorias(Memoriaspub memoriaspub) {
        try {
            if (memoriaspub.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(memoriaspub.getEvidencia());
            }
            nombreTabla = "Memorias Publicadas";
            numeroRegistro = memoriaspub.getMemoriaID().toString();
            accion = "Delate";
            agregaBitacora();
            ejbProduccionProfecional.eliminarMemoriaspub(memoriaspub);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ///////////////////////////////////////////////////////////////////Proyectos y trabajos\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createrProyecto() {
        try {
            nuevOBJInvestigaciones.setClavePerosnal(personal);
            nuevOBJInvestigaciones.setNombreInvestigacion(nombre);
            nuevOBJInvestigaciones.setFechaInicio(fechaI);
            nuevOBJInvestigaciones.setFechaFin(fechaF);
            nuevOBJInvestigaciones.setInstitucion(istitucion);
            nuevOBJInvestigaciones.setDecripcion(descripcion);
            nuevOBJInvestigaciones.setTipo(tipoDr);

            nuevOBJInvestigaciones = ejbProduccionProfecional.crearNuevoInvestigacion(nuevOBJInvestigaciones);
            nombreTabla = "Investigaciones";
            numeroRegistro = nuevOBJInvestigaciones.getInvestigacion().toString();
            accion = "Insert";
            agregaBitacora();

            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            nuevOBJInvestigaciones = new Investigaciones();
            pestaniaActiva = 3;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaInvestigacion(Investigaciones investigaciones) {
        try {
            nombreTabla = "Investigaciones";
            numeroRegistro = investigaciones.getInvestigacion().toString();
            accion = "Delate";
            agregaBitacora();
            ejbProduccionProfecional.actualizarInvestigacion(investigaciones);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            pestaniaActiva = 3;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

////////////////////////////////////////////////////////////////////////////Congreso\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createrCongreso() {
        try {
            nuevoOBJCongresos.setClavePersonal(new Personal());
            nuevoOBJCongresos.setFechaCongreso(dateFormat.format(fechaI));
            nuevoOBJCongresos.setClavePersonal(new Personal(personal));
            nuevoOBJCongresos.setEstatus("Aceptado");

            nuevoOBJCongresos = ejbProduccionProfecional.crearNuevoCongresos(nuevoOBJCongresos);
            nombreTabla = "Congresos";
            numeroRegistro = nuevoOBJCongresos.getCongreso().toString();
            accion = "Insert";
            agregaBitacora();
            nuevoOBJCongresos = new Congresos();
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            pestaniaActiva = 4;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaCongreso(Congresos congresos) {
        try {
            nombreTabla = "Congresos";
            numeroRegistro = congresos.getCongreso().toString();
            accion = "Delate";
            agregaBitacora();
            ejbProduccionProfecional.eliminarCongresos(congresos);
            Messages.addGlobalInfo("¡Operación exitosa!!");
            mostrarListas();
            pestaniaActiva = 4;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
///////////////////////////////////////////////////////////////////////////evidencias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void agregarEvidenciaLibro() {
        if (file != null) {
            ruta = carga.subir(file, new File(personal.toString().concat(File.separator).concat("ProduccionPersonal").concat(File.separator).concat("Libros").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJLibrosPub.setEvidencia(ruta);
                direccionInt1 = 1;
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }

    public void agregarEvidenciaArticulo() {
        if (file != null) {
            ruta = carga.subir(file, new File(personal.toString().concat(File.separator).concat("ProduccionPersonal").concat(File.separator).concat("Articulos").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJArticulosp.setEvidencia(ruta);
                direccionInt2 = 1;
                ruta = null;
                pestaniaActiva = 1;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }

    public void agregarEvidenciaMemoria() {
        if (file != null) {
            ruta = carga.subir(file, new File(personal.toString().concat(File.separator).concat("ProduccionPersonal").concat(File.separator).concat("Memorias").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJMemoriaspub.setEvidencia(ruta);
                direccionInt3 = 1;
                ruta = null;
                pestaniaActiva = 2;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }
////////////////////////////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void mostrarListas() {
        try {
            nuevaListaArticulosp.clear();
            nuevaListaLibrosPub.clear();
            nuevaListaMemoriaspub.clear();
            nuevaListaCongresos.clear();
            nuevaListatInvestigaciones.clear();
            nuevaListatProyectos.clear();
            nuevaListatTrabajos.clear();

            nuevaListaArticulosp = ejbProduccionProfecional.mostrarArticulosp(personal);
            nuevaListaLibrosPub = ejbProduccionProfecional.mostrarLibrosPub(personal);
            nuevaListaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(personal);
            nuevaListaCongresos = ejbProduccionProfecional.mostrarCongresos(personal);
            nuevaListatInvestigaciones = ejbProduccionProfecional.mostrarInvestigacion(personal);
            if (!nuevaListatInvestigaciones.isEmpty()) {
                nuevaListatInvestigaciones.forEach((t) -> {
                    if (t.getTipo().equals("Proyecto")) {
                        nuevaListatProyectos.add(t);
                    } else {
                        nuevaListatTrabajos.add(t);
                    }
                });
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void filtarSubClases() {
        listaSubClase.clear();
        switch (clase) {
            case "Según el grado de dificultad que entraña su consecución":
                listaSubClase.add("Simples");
                listaSubClase.add("Complejos");
                break;
            case "Según la procedencia del capital":
                listaSubClase.add("Públicos");
                listaSubClase.add("Privados");
                listaSubClase.add("Mixtos");
                break;
            case "Según el grado de experimentación del proyecto y sus objetivos":
                listaSubClase.add("Experimentales");
                listaSubClase.add("Normalizados");
                break;
            case "Según el sector":
                listaSubClase.add("Construcción");
                listaSubClase.add("Energía");
                listaSubClase.add("Minería");
                listaSubClase.add("Transformación");
                listaSubClase.add("Medio Ambiente");
                listaSubClase.add("Industriales");
                listaSubClase.add("Servicios");
                break;
            case "Según el ámbito":
                listaSubClase.add("Ingeniería");
                listaSubClase.add("Económicos");
                listaSubClase.add("Fiscales");
                listaSubClase.add("Legales");
                listaSubClase.add("Médicos");
                listaSubClase.add("Matemáticos");
                listaSubClase.add("Artísticos");
                listaSubClase.add("Literarios");
                listaSubClase.add("Tecnológicos");
                listaSubClase.add("Informáticos");
                break;
            case "Según su orientación":
                listaSubClase.add("Productivos");
                listaSubClase.add("Educativos");
                listaSubClase.add("Sociales");
                listaSubClase.add("Comunitarios");
                listaSubClase.add("Investigación");
                break;
            case "Según su área de influencia":
                listaSubClase.add("Supranacionales");
                listaSubClase.add("Internacionales");
                listaSubClase.add("Nacionales");
                listaSubClase.add("Regionales");
                listaSubClase.add("Locales");
                break;
        }
    }

    public String convertirRutaLibros(String ruta) {
        if (selcNuevOBJLibrosPub.getEvidencia() != null) {
            File file = new File(selcNuevOBJLibrosPub.getEvidencia());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaArticulos(String ruta) {
        if (selcNuevOBJArticulosp.getEvidencia() != null) {
            File file = new File(selcNuevOBJArticulosp.getEvidencia());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaMemorias(String ruta) {
        if (selcNuevOBJMemoriaspub.getEvidencia() != null) {
            File file = new File(selcNuevOBJMemoriaspub.getEvidencia());
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
            nuevaBitacoraacceso.setClavePersonal(personal);
            nuevaBitacoraacceso.setNumeroRegistro(numeroRegistro);
            nuevaBitacoraacceso.setTabla(nombreTabla);
            nuevaBitacoraacceso.setAccion(accion);
            nuevaBitacoraacceso.setFechaHora(fechaActual);
            nuevaBitacoraacceso = ejbDatosUsuarioLogeado.crearBitacoraacceso(nuevaBitacoraacceso);

            nombreTabla = "";
            numeroRegistro = "";
            accion = "";
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorProduccionPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void metodoBase() {

    }
}
