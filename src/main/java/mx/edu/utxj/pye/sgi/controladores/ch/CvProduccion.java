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
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class CvProduccion implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;

    // variables basicas
    @Getter    @Setter    private Integer usuario, direccionInt1 = 0, direccionInt2 = 0, direccionInt3 = 0, traduccion, pestaniaActiva;
    @Getter    @Setter    private String anioPub, anioEdi, clase, tipoDr = "", nombre = "", istitucion = "", descripcion = "";
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Date anio1 = new Date();
    @Getter    @Setter    private Date anio2 = new Date();
    @Getter    @Setter    private Date fechaI = new Date();
    @Getter    @Setter    private Date fechaF = null;
    @Getter    @Setter    private Date fechaO = new Date();
    //Variables de objetos Entitys
    @Getter    @Setter    private LibrosPub nuevOBJLibrosPub, selcNuevOBJLibrosPub;
    @Getter    @Setter    private Memoriaspub nuevOBJMemoriaspub, selcNuevOBJMemoriaspub;
    @Getter    @Setter    private Articulosp nuevOBJArticulosp, selcNuevOBJArticulosp;
    @Getter    @Setter    private Investigaciones nuevOBJInvestigaciones = new Investigaciones(), nuevOBJInvestigacionesSelectec;
    @Getter    @Setter    private Congresos nuevoOBJCongresos = new Congresos(), nuevoOBJCongresoSelectec = new Congresos();
    // listas de variables basicas
    @Getter    @Setter    private List<String> listaSubClase = new ArrayList<>();
    // listas de entitys
    @Getter    @Setter    private List<LibrosPub> nuevaListaLibrosPub = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> nuevaListaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> nuevaListaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> nuevaListatInvestigaciones = new ArrayList<>(),nuevaListatTrabajos=new ArrayList<>(),nuevaListatProyectos=new ArrayList<>();
    @Getter    @Setter    private List<Congresos> nuevaListaCongresos = new ArrayList<>();
    // variable archivos  
    @Getter    @Setter    private Part file;
    // EJbs  
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;
    //Injects
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {
        System.out.println("ControladorProduccionPersonal Inicio: " + System.currentTimeMillis());
        usuario = controladorEmpleado.getEmpleadoLogeado();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorProduccionPersonal.init()" + usuario);
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

    public void reiniciarValores() {
         file = null;
        anioEdi = "";
        anioPub = "";
        nuevOBJLibrosPub = new LibrosPub();
        nuevoOBJCongresos = new Congresos();
        nuevOBJArticulosp = new Articulosp();
        nuevOBJMemoriaspub = new Memoriaspub();
        nuevOBJInvestigaciones = new Investigaciones();
    }
/////////////////////////////////////////////////////////////////////////////Libros\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createLibros() {
        try {
            //Inicialización de las relaciones entre las tablas “Capacitacionespersonal” con “Personal”, "CursosModalidad" y con "CursosTipo"
            nuevOBJLibrosPub.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJLibrosPub.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            anio1 = dateFormat.parse("01/01/" + anioPub);
            anio2 = dateFormat.parse("01/01/" + anioEdi);
            nuevOBJLibrosPub.setAnioEdicion(anio2);
            nuevOBJLibrosPub.setAnioPub(anio1);
            nuevOBJLibrosPub.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJLibrosPub = ejbProduccionProfecional.crearNuevoLibrosPub(nuevOBJLibrosPub);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJLibrosPub.getLibrosp().toString(), "Libros Publicados", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaLibros(LibrosPub librosPub) {
        try {
            //Primero se evalúa si su propiedad evidenciaNombremiento cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (librosPub.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(librosPub.getEvidencia());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, librosPub.getLibrosp().toString(), "Libros Publicados", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbProduccionProfecional.eliminarLibrosPub(librosPub);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/////////////////////////////////////////////////////////////////////////////Articulos\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    
    public void createArticulos() {
        try {
            //Inicialización de las relaciones entre las tablas “Capacitacionespersonal” con “Personal”, "CursosModalidad" y con "CursosTipo"
            nuevOBJArticulosp.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJArticulosp.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            anio1 = dateFormat.parse("01/01/" + anioPub);
            anio2 = dateFormat.parse("01/01/" + anioEdi);
            nuevOBJArticulosp.setEnioEdicion(anio2);
            nuevOBJArticulosp.setAnioPublicacion(anio1);
            nuevOBJArticulosp.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJArticulosp = ejbProduccionProfecional.crearNuevoArticulosp(nuevOBJArticulosp);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJArticulosp.getArticuloId().toString(), "Artículos Publicados", "Insert");
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
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaArticulos(Articulosp articulosp) {
        try {
            if (articulosp.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(articulosp.getEvidencia());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, articulosp.getArticuloId().toString(), "Artículos Publicados", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbProduccionProfecional.eliminarArticulosp(articulosp);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//////////////////////////////////////////////////////////////////////////Memorias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createrMemorias() {
        try {
            //Inicialización de las relaciones entre las tablas “Capacitacionespersonal” con “Personal”, "CursosModalidad" y con "CursosTipo"
            nuevOBJArticulosp.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJMemoriaspub.setClavePersonal(new Personal(usuario));
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            anio1 = dateFormat.parse("01/01/" + anioPub);
            nuevOBJMemoriaspub.setAnioDePublicacion(anio1);
            nuevOBJMemoriaspub.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJMemoriaspub = ejbProduccionProfecional.crearNuevoMemoriaspub(nuevOBJMemoriaspub);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJMemoriaspub.getMemoriaID().toString(), "Memorias Publicadas", "Insert");
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
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaMemorias(Memoriaspub memoriaspub) {
        try {
            if (memoriaspub.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(memoriaspub.getEvidencia());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, memoriaspub.getMemoriaID().toString(), "Memorias Publicadas", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbProduccionProfecional.eliminarMemoriaspub(memoriaspub);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    ///////////////////////////////////////////////////////////////////Proyectos y trabajos\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createrProyecto() {
        try {
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevOBJInvestigaciones.setClavePerosnal(usuario);
            nuevOBJInvestigaciones.setNombreInvestigacion(nombre);
            nuevOBJInvestigaciones.setFechaInicio(fechaI);
            nuevOBJInvestigaciones.setFechaFin(fechaF);
            nuevOBJInvestigaciones.setInstitucion(istitucion);
            nuevOBJInvestigaciones.setDecripcion(descripcion);
            nuevOBJInvestigaciones.setTipo(tipoDr);
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJInvestigaciones = ejbProduccionProfecional.crearNuevoInvestigacion(nuevOBJInvestigaciones);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJInvestigaciones.getInvestigacion().toString(), "Investigaciones", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 3;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaInvestigacion(Investigaciones investigaciones) {
        try {
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, investigaciones.getInvestigacion().toString(), "Investigaciones", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbProduccionProfecional.eliminarInvestigacion(investigaciones);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 3;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

////////////////////////////////////////////////////////////////////////////Congreso\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void createrCongreso() {
        try {
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevoOBJCongresos.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJCongresos.setClavePersonal(new Personal(usuario));
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJCongresos.setFechaCongreso(dateFormat.format(fechaI));
            nuevoOBJCongresos.setEstatus("Aceptado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJCongresos = ejbProduccionProfecional.crearNuevoCongresos(nuevoOBJCongresos);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJCongresos.getCongreso().toString(), "Congresos", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 4;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaCongreso(Congresos congresos) {
        try {
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, congresos.getCongreso().toString(), "Congresos", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbProduccionProfecional.eliminarCongresos(congresos);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 4;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
///////////////////////////////////////////////////////////////////////////evidencias\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void agregarEvidenciaLibro() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJLibrosPub.setEvidencia(utilidadesCH.agregarEvidencias(file, usuario.toString(), "ProduccionPersonal", "Libros"));
        //Antes de culminar se actualiza el valor de la variable para permitir guardar la información.
        direccionInt1 = 1;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }

    public void agregarEvidenciaArticulo() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJArticulosp.setEvidencia(utilidadesCH.agregarEvidencias(file, usuario.toString(), "ProduccionPersonal", "Articulos"));
        //Antes de culminar se actualiza el valor de la variable para permitir guardar la información.
        direccionInt2 = 1;
        //Finalmente se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
        pestaniaActiva = 1;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }

    public void agregarEvidenciaMemoria() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJMemoriaspub.setEvidencia(utilidadesCH.agregarEvidencias(file, usuario.toString(), "ProduccionPersonal", "Memorias"));
        //Antes de culminar se actualiza el valor de la variable para permitir guardar la información.
        direccionInt3 = 1;
        //Finalmente se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
        pestaniaActiva = 2;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }
////////////////////////////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void mostrarListas() {
        try {
            //Las listas son llenadas con los registros existentes del usuario logeado en la BD, esto mediante la recepción de su clave.
            nuevaListaArticulosp.clear();
            nuevaListaLibrosPub.clear();
            nuevaListaMemoriaspub.clear();
            nuevaListaCongresos.clear();
            nuevaListatInvestigaciones.clear();
            nuevaListatProyectos.clear();
            nuevaListatTrabajos.clear();

            nuevaListaArticulosp = ejbProduccionProfecional.mostrarArticulosp(usuario);
            nuevaListaLibrosPub = ejbProduccionProfecional.mostrarLibrosPub(usuario);
            nuevaListaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(usuario);
            nuevaListaCongresos = ejbProduccionProfecional.mostrarCongresos(usuario);
            nuevaListatInvestigaciones = ejbProduccionProfecional.mostrarInvestigacion(usuario);
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
            Logger.getLogger(CvProduccion.class.getName()).log(Level.SEVERE, null, ex);
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

    public void metodoBase() {

    }
}
