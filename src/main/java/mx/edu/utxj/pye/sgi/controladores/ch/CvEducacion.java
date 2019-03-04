package mx.edu.utxj.pye.sgi.controladores.ch;

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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.CursosModalidad;
import mx.edu.utxj.pye.sgi.entity.ch.CursosTipo;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Grados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class CvEducacion implements Serializable {

    private static final long serialVersionUID = 2917873066237326818L;
// variables basicas
    @Getter    @Setter    private String evidencia;
    @Getter    @Setter    private Short grado = 0, modalida = 0, tipoCur = 0;
    @Getter    @Setter    private Integer usuario, direccionInt = 0, direccionInt3 = 0, direccionInt4 = 0, claveR = 0, claveREX = 0, claveRAP = 0, pestaniaActiva;
    @Getter    @Setter    private Boolean nomCarrera, formaTyC;
//Variables de objetos Entitys
    @Getter    @Setter    private FormacionAcademica nuevoOBJFormacionAcademica;
    @Getter    @Setter    private ExperienciasLaborales nuevoOBJExpeienciasLaborales;
    @Getter    @Setter    private Capacitacionespersonal nuevoOBJCapacitacionespersonal;
// listas de entitys
    @Getter    @Setter    private List<FormacionAcademica> nuevaListaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> nuevaListaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> nuevaListaCapacitaciones = new ArrayList<>();
    @Getter    @Setter    private List<CursosTipo> listaCursos = new ArrayList<>();
    @Getter    @Setter    private List<Grados> listaGrados = new ArrayList<>();
    @Getter    @Setter    private List<CursosModalidad> listaModalidades = new ArrayList<>();
// variable archivos    
    @Getter    @Setter    private Part file;
// EJbs    
    @EJB    EjbCarga carga;
    @EJB    private EjbEducacion ejbEducacion;
//Injects
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {
        // se realiza la inicializacion de las variabes, objetos y listas
        formaTyC = false;
        nomCarrera = false;

        usuario = controladorEmpleado.getEmpleadoLogeado();

        nuevoOBJFormacionAcademica = new FormacionAcademica();
        nuevoOBJExpeienciasLaborales = new ExperienciasLaborales();
        nuevoOBJCapacitacionespersonal = new Capacitacionespersonal();

        listaCursos.clear();
        listaGrados.clear();
        listaModalidades.clear();
        nuevaListaFormacionAcademica.clear();
        nuevaListaExperienciasLaborales.clear();
        nuevaListaCapacitaciones.clear();

        mostrarListas();
    }

    public void reiniciarValores() {
        grado = 0;
        claveR = 0;
        tipoCur = 0;
        claveREX = 0;
        claveRAP = 0;
        modalida = 0;
        evidencia = "";
        direccionInt = 0;
        direccionInt3 = 0;
        direccionInt4 = 0;
        nuevoOBJFormacionAcademica = new FormacionAcademica();
        nuevoOBJExpeienciasLaborales = new ExperienciasLaborales();
        nuevoOBJCapacitacionespersonal = new Capacitacionespersonal();
    }
    ////////////////////////////////////////////Formación Académica\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void createFormacion() {
        try {
            //Inicialización de las relaciones entre las tablas “Formación Académica” con “Personal” y “Grados Académicos”
            nuevoOBJFormacionAcademica.setClavePersonal(new Personal());
            nuevoOBJFormacionAcademica.setNivelEscolaridad(new Grados());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJFormacionAcademica.getNivelEscolaridad().setGrado(grado);
            nuevoOBJFormacionAcademica.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJFormacionAcademica.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJFormacionAcademica = ejbEducacion.crearNuevoFormacionAcademica(nuevoOBJFormacionAcademica);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJFormacionAcademica.getFormacion().toString(), "Formación Académica", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteFormacion(FormacionAcademica formacionAcademica) {
        try {
            //Primero se evalúa si su propiedad evidenciaCedula cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (formacionAcademica.getEvidenciaCedula() != null) {
                CargaArchivosCH.eliminarArchivo(formacionAcademica.getEvidenciaCedula());
            }
            //También se evalúa si su propiedad evidenciaTitulo cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (formacionAcademica.getEvidenciaTitulo() != null) {
                CargaArchivosCH.eliminarArchivo(formacionAcademica.getEvidenciaTitulo());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, formacionAcademica.getFormacion().toString(), "Formación Académica", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbEducacion.eliminarFormacionAcademica(formacionAcademica);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateFormacion(RowEditEvent event) {
        try {
            FormacionAcademica actualizarForAc = (FormacionAcademica) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarForAc.getFormacion().toString(), "Formación Académica", "Update");
            //Se realiza la asignación de los valores a actualizar
            actualizarForAc.getNivelEscolaridad().setGrado(grado);
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            ejbEducacion.actualizarFormacionAcademica(actualizarForAc);
            //Al finalizar la actualización de la información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////////////////////////Experiencias Laborales\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\ 
    public void guardarEmpleo() {
        try {
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevoOBJExpeienciasLaborales.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJExpeienciasLaborales.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJExpeienciasLaborales.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJExpeienciasLaborales = ejbEducacion.crearNuevoExperienciasLaborales(nuevoOBJExpeienciasLaborales);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJExpeienciasLaborales.getEmpleo().toString(), "Experiencia Laboral", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void eliminarEmpleos(ExperienciasLaborales experienciasLaborales) {
        try {
            //Primero se evalúa si su propiedad evidenciaNombremiento cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (experienciasLaborales.getEvidenciaNombremiento() != null) {
                CargaArchivosCH.eliminarArchivo(experienciasLaborales.getEvidenciaNombremiento());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, experienciasLaborales.getEmpleo().toString(), "Experiencia Laboral", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbEducacion.eliminarExperienciasLaborales(experienciasLaborales);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateExperienciaLaboral(RowEditEvent event) {
        try {
            ExperienciasLaborales actualizarExpe = (ExperienciasLaborales) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarExpe.getEmpleo().toString(), "Experiencia Laboral", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            ejbEducacion.actualizarExperienciasLaborales(actualizarExpe);
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////////////////////////////////Capacitaciones personales\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void guardarCapacitacion() {
        try {
            //Inicialización de las relaciones entre las tablas “Capacitacionespersonal” con “Personal”, "CursosModalidad" y con "CursosTipo"
            nuevoOBJCapacitacionespersonal.setClavePersonal(new Personal());
            nuevoOBJCapacitacionespersonal.setModalidad(new CursosModalidad());
            nuevoOBJCapacitacionespersonal.setTipo(new CursosTipo());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJCapacitacionespersonal.getClavePersonal().setClave(usuario);
            nuevoOBJCapacitacionespersonal.getModalidad().setModalidad(modalida);
            nuevoOBJCapacitacionespersonal.getTipo().setTipo(tipoCur);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJCapacitacionespersonal.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJCapacitacionespersonal = ejbEducacion.crearNuevoCapacitacionespersonal(nuevoOBJCapacitacionespersonal);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJCapacitacionespersonal.getCursoClave().toString(), "Capacitación Personal", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarCapacitacion(Capacitacionespersonal capacitacionespersonal) {
        try {
            //Primero se evalúa si su propiedad evidenciaCapacitacion cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (capacitacionespersonal.getEvidenciaCapacitacion() != null) {
                CargaArchivosCH.eliminarArchivo(capacitacionespersonal.getEvidenciaCapacitacion());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, capacitacionespersonal.getCursoClave().toString(), "Capacitación Personal", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbEducacion.eliminarCapacitacionespersonal(capacitacionespersonal);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateCapacitacion(RowEditEvent event) {
        try {
            Capacitacionespersonal actualizarCapa = (Capacitacionespersonal) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarCapa.getCursoClave().toString(), "Capacitación Personal", "Update");
            //Se realiza la asignación de los valores a actualizar
            actualizarCapa.getModalidad().setModalidad(modalida);
            actualizarCapa.getTipo().setTipo(tipoCur);
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            ejbEducacion.actualizarCapacitacionespersonal(actualizarCapa);
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////Lista \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void mostrarListas() {
        try {
            //Las listas son llenadas con los catálogos existentes en la BD.
            listaGrados = ejbEducacion.mostrarListaGrados();
            listaCursos = ejbEducacion.mostrarListaCursosTipo();
            listaModalidades = ejbEducacion.mostrarListaCursosModalidad();
            //Las listas son llenadas con los registros existentes del usuario logeado en la BD, esto mediante la recepción de su clave.
            nuevaListaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(usuario);
            nuevaListaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(usuario);
            nuevaListaCapacitaciones = ejbEducacion.mostrarCapacitacionespersonal(usuario);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvEducacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciasEducacion() {
        //Este método comprueba que tipo de evidencia es el que se carga, para así poder asignar la ruta de almacenamiento a la propiedad correcta del objeto
        switch (evidencia) {
            case "Cedula":
                //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
                nuevoOBJFormacionAcademica.setEvidenciaCedula(utilidadesCH.agregarEvidencias(file, usuario.toString(), "experienciaLaboral", evidencia));
                break;
            case "EvidenciaTitulacion":
                //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
                nuevoOBJFormacionAcademica.setEvidenciaTitulo(utilidadesCH.agregarEvidencias(file, usuario.toString(), "experienciaLaboral", evidencia));
                break;
            default:
                //En caso de no seleccionar el tipo de evidencia a cargar se le informa el usuario un mensaje de error
                utilidadesCH.mensajes(" es necesario el tipo de evidencia a cargar", "W", "C");
                break;
        }
        //Antes de culminar se actualiza el valor de la variable para permitir guardar la información.
        direccionInt = 1;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }

    public void agregarEvidenciaExperienciaLab() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevoOBJExpeienciasLaborales.setEvidenciaNombremiento(utilidadesCH.agregarEvidencias(file, usuario.toString(), "experienciaLaboral", ""));
        //Antes de culminar se actualiza el valor de la variable para permitir guardar la información.
        direccionInt3 = 1;
        //Finalmente se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
        pestaniaActiva = 1;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }

    public void agregarEvidenciaCapacitacion() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevoOBJCapacitacionespersonal.setEvidenciaCapacitacion(utilidadesCH.agregarEvidencias(file, usuario.toString(), "capacitacion", ""));
        //Antes de culminar se actualiza el valor de la variable para permitir guardar la información.
        direccionInt4 = 1;
        //Finalmente se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
        pestaniaActiva = 2;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }

    public void asignaGrado() {
        if (nuevoOBJFormacionAcademica == null) {
            reiniciarValores();
            return;
        }
        claveR = nuevoOBJFormacionAcademica.getFormacion();
        grado = nuevoOBJFormacionAcademica.getNivelEscolaridad().getGrado();
    }

    public void asignaclaveREx() {
        if (nuevoOBJExpeienciasLaborales == null) {
            reiniciarValores();
            return;
        }
        claveREX = nuevoOBJExpeienciasLaborales.getEmpleo();
    }

    public void asignaclaveRAP() {
        if (nuevoOBJCapacitacionespersonal == null) {
            reiniciarValores();
            return;
        }
        tipoCur = nuevoOBJCapacitacionespersonal.getTipo().getTipo();
        modalida = nuevoOBJCapacitacionespersonal.getModalidad().getModalidad();
        claveRAP = nuevoOBJCapacitacionespersonal.getCursoClave();
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

    public void metodoBase() {

    }
}
