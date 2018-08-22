package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class ControladorEducacion implements Serializable {

    private static final long serialVersionUID = 2917873066237326818L;

    @Getter    @Setter    private String claveTrabajador;
    @Getter    @Setter    private Short grado = 0, modalida = 0, tipoCur = 0;
    @Getter    @Setter    private Integer usuario, direccionInt = 0, direccionInt3 = 0, direccionInt4 = 0, claveR = 0, claveREX = 0, claveRAP = 0, pestaniaActiva;
    @Getter    @Setter    private FormacionAcademica nuevoOBJFormacionAcademica;
    @Getter    @Setter    private ExperienciasLaborales nuevoOBJExpeienciasLaborales;
    @Getter    @Setter    private Capacitacionespersonal nuevoOBJCapacitacionespersonal;
    @Getter    @Setter    private List<FormacionAcademica> nuevaListaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> nuevaListaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> nuevaListaCapacitacionesInternas = new ArrayList<>();
    @Getter    @Setter    private List<CursosTipo> listaCursos = new ArrayList<>();
    @Getter    @Setter    private List<Grados> listaGrados = new ArrayList<>();
    @Getter    @Setter    private List<CursosModalidad> listaModalidades = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> nuevaListaCapacitacionesExternas = new ArrayList<>();
    @Getter    @Setter    private Boolean nomCarrera, formaTyC;
    
   
    @Getter    @Setter    private Bitacoraacceso nuevaBitacoraacceso;
    @Getter    @Setter    private String nombreTabla, numeroRegistro, accion,evidencia;
    
    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    @EJB    EjbCarga carga;
    
    @EJB    private EjbEducacion ejbEducacion;
    @EJB    private EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorEducacion Inicio: " + System.currentTimeMillis());
        nomCarrera = false;
        formaTyC = false;
        usuario = controladorEmpleado.getEmpleadoLogeado();
        claveTrabajador = controladorEmpleado.getClavePersonalLogeado();

        nuevoOBJCapacitacionespersonal = new Capacitacionespersonal();
        nuevoOBJExpeienciasLaborales = new ExperienciasLaborales();
        nuevoOBJFormacionAcademica = new FormacionAcademica();

        nuevaListaCapacitacionesInternas.clear();
        nuevaListaExperienciasLaborales.clear();
        nuevaListaFormacionAcademica.clear();
        listaCursos.clear();
        listaGrados.clear();
        listaModalidades.clear();
        nuevaListaCapacitacionesExternas.clear();

        mostrarListas();
        System.out.println("ControladorEducacion Fin: " + System.currentTimeMillis());
    }

    ////////////////////////////////////////////Formación Académica\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createFormacion() {
        try {
            nuevoOBJFormacionAcademica.setClavePersonal(new Personal());
            nuevoOBJFormacionAcademica.setNivelEscolaridad(new Grados());
            nuevoOBJFormacionAcademica.getNivelEscolaridad().setGrado(grado);
            nuevoOBJFormacionAcademica.getClavePersonal().setClave(usuario);
            nuevoOBJFormacionAcademica.setEstatus("Denegado");
            if (nuevoOBJFormacionAcademica.getFormacion() == null) {
                nuevoOBJFormacionAcademica = ejbEducacion.crearNuevoFormacionAcademica(nuevoOBJFormacionAcademica);
                nombreTabla = "Formación Académica";
                numeroRegistro = nuevoOBJFormacionAcademica.getFormacion().toString();
                accion = "Insert";
                agregaBitacora();
            } else {
                updateFormacion();
            }

            Messages.addGlobalInfo("¡Operación exitosa!!");
            nuevoOBJFormacionAcademica = null;
            grado = 0;
            mostrarListas();
            direccionInt = 0;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteFormacion(FormacionAcademica formacionAcademica) {
        try {
            if (formacionAcademica.getEvidenciaCedula() != null) {
                CargaArchivosCH.eliminarArchivo(formacionAcademica.getEvidenciaCedula());
            }
            if (formacionAcademica.getEvidenciaTitulo() != null) {
                CargaArchivosCH.eliminarArchivo(formacionAcademica.getEvidenciaTitulo());
            }
            nombreTabla = "Formación Académica";
            numeroRegistro = formacionAcademica.getFormacion().toString();
            accion = "Delate";
            agregaBitacora();            
            ejbEducacion.eliminarFormacionAcademica(formacionAcademica);
            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
            mostrarListas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateFormacion() {
        try {
            nombreTabla = "Formación Académica";
            numeroRegistro = nuevoOBJFormacionAcademica.getFormacion().toString();
            accion = "Update";
            agregaBitacora();
            nuevoOBJFormacionAcademica.getNivelEscolaridad().setGrado(grado);
            nuevoOBJFormacionAcademica = ejbEducacion.actualizarFormacionAcademica(nuevoOBJFormacionAcademica);
            nuevoOBJFormacionAcademica = null;
            claveR = 0;
            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////////////////////////Experiencias Laborales\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ 
    public void guardarEmpleo() {
        try {
            nuevoOBJExpeienciasLaborales.setClavePersonal(new Personal());
            nuevoOBJExpeienciasLaborales.getClavePersonal().setClave(usuario);
            nuevoOBJExpeienciasLaborales.setEstatus("Denegado");
            if (nuevoOBJFormacionAcademica.getFormacion() == null) {
                nuevoOBJExpeienciasLaborales = ejbEducacion.crearNuevoExperienciasLaborales(nuevoOBJExpeienciasLaborales);
                nombreTabla = "Experiencia Laboral";
                numeroRegistro = nuevoOBJExpeienciasLaborales.getEmpleo().toString();
                accion = "Insert";
                agregaBitacora();
            } else {
                updateExperienciaLaboral();
            }
            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
            nuevoOBJExpeienciasLaborales = null;
            mostrarListas();
            direccionInt3 = 0;
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eliminarEmpleos(ExperienciasLaborales experienciasLaborales) {
        try {
            if (experienciasLaborales.getEvidenciaNombremiento() != null) {
                CargaArchivosCH.eliminarArchivo(experienciasLaborales.getEvidenciaNombremiento());
            }
            nombreTabla = "Experiencia Laboral";
            numeroRegistro = experienciasLaborales.getEmpleo().toString();
            accion = "Delate";
            agregaBitacora();
            ejbEducacion.eliminarExperienciasLaborales(experienciasLaborales);
            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
            mostrarListas();
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateExperienciaLaboral() {
        try {
            nombreTabla = "Experiencia Laboral";
            numeroRegistro = nuevoOBJExpeienciasLaborales.getEmpleo().toString();
            accion = "Update";
            agregaBitacora();
            nuevoOBJExpeienciasLaborales = ejbEducacion.actualizarExperienciasLaborales(nuevoOBJExpeienciasLaborales);
            nuevoOBJExpeienciasLaborales = null;
            claveREX = 0;
            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
            pestaniaActiva = 1;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////////////////////////////////Capacitaciones personales\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void guardarCapacitacion() {
        try {
            nuevoOBJCapacitacionespersonal.setClavePersonal(new Personal());
            nuevoOBJCapacitacionespersonal.setModalidad(new CursosModalidad());
            nuevoOBJCapacitacionespersonal.setTipo(new CursosTipo());
            nuevoOBJCapacitacionespersonal.getClavePersonal().setClave(usuario);
            nuevoOBJCapacitacionespersonal.getModalidad().setModalidad(modalida);
            nuevoOBJCapacitacionespersonal.getTipo().setTipo(tipoCur);
            nuevoOBJCapacitacionespersonal.setEstatus("Denegado");
            if (nuevoOBJFormacionAcademica.getFormacion() == null) {
                nuevoOBJCapacitacionespersonal = ejbEducacion.crearNuevoCapacitacionespersonal(nuevoOBJCapacitacionespersonal);
                nombreTabla = "Capacitación Personal";
                numeroRegistro = nuevoOBJCapacitacionespersonal.getCursoClave().toString();
                accion = "Insert";
                agregaBitacora();
            } else {
                updateCapacitacion();
            }

            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
            nuevoOBJCapacitacionespersonal = new Capacitacionespersonal();
            tipoCur = 0;
            modalida = 0;
            mostrarListas();
            direccionInt4 = 0;
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarCapacitacion(Capacitacionespersonal capacitacionespersonal) {
        try {
            if (capacitacionespersonal.getEvidenciaCapacitacion() != null) {
                CargaArchivosCH.eliminarArchivo(capacitacionespersonal.getEvidenciaCapacitacion());
            }
            nombreTabla = "Capacitación Personal";
            numeroRegistro = capacitacionespersonal.getCursoClave().toString();
            accion = "Delate";
            agregaBitacora();
            ejbEducacion.eliminarCapacitacionespersonal(capacitacionespersonal);
            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
            mostrarListas();
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateCapacitacion() {
        try {
            nombreTabla = "Capacitación Personal";
            numeroRegistro = nuevoOBJCapacitacionespersonal.getCursoClave().toString();
            accion = "Update";
            agregaBitacora();
            nuevoOBJCapacitacionespersonal.getModalidad().setModalidad(modalida);
            nuevoOBJCapacitacionespersonal.getTipo().setTipo(tipoCur);
            nuevoOBJCapacitacionespersonal = ejbEducacion.actualizarCapacitacionespersonal(nuevoOBJCapacitacionespersonal);
            nuevoOBJCapacitacionespersonal = null;
            claveRAP = 0;
            Messages.addGlobalInfo("Información Actualizada con Éxito!!");
            pestaniaActiva = 2;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////Lista \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void mostrarListas() {
        try {
            nuevaListaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(usuario);
            nuevaListaCapacitacionesInternas = ejbEducacion.mostrarCapacitacionespersonalTipo(usuario, "Interna");
            nuevaListaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(usuario);
            listaCursos = ejbDatosUsuarioLogeado.mostrarListaCursosTipo();
            listaGrados = ejbDatosUsuarioLogeado.mostrarListaGrados();
            listaModalidades = ejbDatosUsuarioLogeado.mostrarListaCursosModalidad();
            nuevaListaCapacitacionesExternas = ejbEducacion.mostrarCapacitacionespersonalTipo(usuario, "Externa");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciasEducacion() {
        if (file != null) {
            ruta = carga.subir(file, new File(claveTrabajador.concat(File.separator).concat("formacionAcademica").concat(File.separator).concat(evidencia).concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                switch (evidencia) {
                    case "Cedula":
                        nuevoOBJFormacionAcademica.setEvidenciaCedula(ruta);
                        break;
                    case "EvidenciaTitulacion":
                        nuevoOBJFormacionAcademica.setEvidenciaTitulo(ruta);
                        break;
                    default:
                        Messages.addGlobalWarn("Es necesario el tipo de evidencia a cargar !!");
                        break;
                }
                ruta = null;
                file = null;
                evidencia = "";
                direccionInt = 1;
            } else {
                ruta = null;
                file = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }

    public void agregarEvidenciaExperienciaLab() {
        if (file != null) {
            ruta = carga.subir(file, new File(claveTrabajador.concat(File.separator).concat("experienciaLaboral").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevoOBJExpeienciasLaborales.setEvidenciaNombremiento(ruta);
                direccionInt3 = 1;
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
        pestaniaActiva = 1;
    }

    public void agregarEvidenciaCapacitacion() {
        if (file != null) {
            ruta = carga.subir(file, new File(claveTrabajador.concat(File.separator).concat("capacitacion").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevoOBJCapacitacionespersonal.setEvidenciaCapacitacion(ruta);
                direccionInt4 = 1;
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
        pestaniaActiva = 2;
    }

    public String convertirRutaFormaAT(String ruta) {
        if (nuevoOBJFormacionAcademica.getEvidenciaTitulo() != null) {
            File file = new File(nuevoOBJFormacionAcademica.getEvidenciaTitulo());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaFormaAC(String ruta) {
        if (nuevoOBJFormacionAcademica.getEvidenciaCedula() != null) {
            File file = new File(nuevoOBJFormacionAcademica.getEvidenciaCedula());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaExperienciaL(String ruta) {
        if (nuevoOBJExpeienciasLaborales.getEvidenciaNombremiento() != null) {
            File file = new File(nuevoOBJExpeienciasLaborales.getEvidenciaNombremiento());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public String convertirRutaActualizacionP(String ruta) {
        if (nuevoOBJCapacitacionespersonal.getEvidenciaCapacitacion() != null && nuevoOBJCapacitacionespersonal.getEvidenciaCapacitacion() != "") {
            File file = new File(nuevoOBJCapacitacionespersonal.getEvidenciaCapacitacion());
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public void asignaGrado() {
        if (nuevoOBJFormacionAcademica != null) {
            claveR = nuevoOBJFormacionAcademica.getFormacion();
            grado = nuevoOBJFormacionAcademica.getNivelEscolaridad().getGrado();
        } else {
            claveR = 0;
            grado = 0;
        }
    }

    public void asignaclaveREx() {
        if (nuevoOBJExpeienciasLaborales != null) {
            claveREX = nuevoOBJExpeienciasLaborales.getEmpleo();
        } else {
            claveREX = 0;
        }
    }

    public void asignaclaveRAP() {
        if (nuevoOBJCapacitacionespersonal != null) {
            tipoCur = nuevoOBJCapacitacionespersonal.getTipo().getTipo();
            modalida = nuevoOBJCapacitacionespersonal.getModalidad().getModalidad();
            claveRAP = nuevoOBJCapacitacionespersonal.getCursoClave();
        } else {
            tipoCur = 0;
            modalida = 0;
            claveRAP = 0;
        }
    }

    public void filtrarInfoEducacion() {
        switch (grado) {
            case 1:                nomCarrera = false;                formaTyC = false;                break;
            case 2:                nomCarrera = false;                formaTyC = false;                break;
            case 3:                nomCarrera = false;                formaTyC = false;                break;
            case 4:                nomCarrera = false;                formaTyC = false;                break;
            case 5:                nomCarrera = false;                formaTyC = false;                break;
            case 6:                nomCarrera = false;                formaTyC = false;                break;
            case 7:                nomCarrera = true;                formaTyC = false;                break;
            case 8:                nomCarrera = true;                formaTyC = true;                break;
            case 9:                nomCarrera = true;                formaTyC = false;                break;
            case 10:                nomCarrera = true;                formaTyC = false;                break;
            case 11:                nomCarrera = true;                formaTyC = true;                break;
            case 12:                nomCarrera = true;                formaTyC = false;                break;
            case 13:                nomCarrera = true;                formaTyC = false;                break;
            case 14:                nomCarrera = true;                formaTyC = true;                break;
            case 15:                nomCarrera = true;                formaTyC = true;                break;
            case 16:                nomCarrera = true;                formaTyC = false;                break;
            case 17:                nomCarrera = true;                formaTyC = true;                break;
            default:                nomCarrera = false;                formaTyC = false;                break;
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
            Logger.getLogger(ControladorEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void metodoBase() {
        
    }
}
