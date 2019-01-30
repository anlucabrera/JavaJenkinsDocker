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
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class CvHabilidades implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;
    // variables basicas
    @Getter    @Setter    private String conocimiento, programa, nivel;
    @Getter    @Setter    private String lengua, dominio, conversacion,escritura,lecctura;
    @Getter    @Setter    private Integer usuario, direccionInt = 0, pestaniaActiva;
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Date fechaC = new Date();
    @Getter    @Setter    private Date fechaO = new Date();
    @Getter    @Setter    private Date fechaF = new Date();
    //Variables de objetos Entitys
    @Getter    @Setter    private Lenguas nuevOBJLenguas= new Lenguas();
    @Getter    @Setter    private Idiomas nuevOBJIdiomas=new Idiomas();
    @Getter    @Setter    private HabilidadesInformaticas nuevOBJHabilidadesInformaticas=new HabilidadesInformaticas();
    // listas de variables basicas
    @Getter    @Setter    private List<String> listaConocimiento = new ArrayList<>();
    // listas de entitys
    @Getter    @Setter    private List<Lenguas> nuevaListaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> nuevaListaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> nuevaListaHabilidadesInformaticas = new ArrayList<>();
    // variable archivos  
    @Getter    @Setter    private Part file;
    // EJbs  
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades habilidades;
    //Injects 
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;
    
    @PostConstruct
    public void init() {
        System.out.println("ControladorHabilidadesIIL Inicio: " + System.currentTimeMillis());
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.init(a)"+nuevOBJHabilidadesInformaticas);
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.init(b)"+nuevOBJLenguas);
        usuario = controladorEmpleado.getEmpleadoLogeado();

        nuevOBJIdiomas = new Idiomas();
        nuevOBJLenguas = new Lenguas();
        nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();

        nuevaListaIdiomas.clear();
        nuevaListaLenguas.clear();
        nuevaListaHabilidadesInformaticas.clear();

        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.init(a)"+nuevOBJHabilidadesInformaticas);
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.init(b)"+nuevOBJLenguas);
        mostrarListas();
        
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.init(a)"+nuevOBJHabilidadesInformaticas);
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.init(b)"+nuevOBJLenguas);
        System.out.println(" ControladorHabilidadesIIL Fin: " + System.currentTimeMillis());
    }
    
    public void reiniciarValores() {
        nuevOBJLenguas = new Lenguas();
        nuevOBJIdiomas = new Idiomas();
        nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();
        direccionInt = 0;
        listaConocimiento.clear();
        conocimiento = null;
        programa = null;
        nivel = "Nivel Usuario";
        lengua = null;
        dominio = null;
        conversacion = null;
        escritura = null;
        lecctura = null;
    }
/////////////////////////////////////////////////////////////////////////////Lenguas\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void createLengua() {
        try {
            //Inicialización del objeto
            nuevOBJLenguas = new Lenguas();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(1)" + nuevOBJLenguas);
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevOBJLenguas.setClavePersonal(new Personal());
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(2)");
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJLenguas.getClavePersonal().setClave(usuario);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(3)"+lengua);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevOBJLenguas.setTipoLengua(lengua);
            nuevOBJLenguas.setGradoDominio(dominio);
            nuevOBJLenguas.setNivelConversacion(conversacion);
            nuevOBJLenguas.setNivelEscritura(escritura);
            nuevOBJLenguas.setNivelLectura(lecctura);
            nuevOBJLenguas.setEstatus("Aceptado");            

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(4)");
            //En este apartado se procede a comprobar si la información a registrar ya existe en la BD, en caso de ya existir se envía la información al método “actualizaLengua” el cual sirve para actualizar la información, posteriormente se cierra el flujo del proceso
            if (nuevOBJLenguas.getLengua() != null) {
                actualizaLengua();
                return;
            }

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(5)");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJLenguas = habilidades.crearNuevoLenguas(nuevOBJLenguas);

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(6)");
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJLenguas.getLengua().toString(), "Lenguas", "Insert");

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(7)");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(8)");
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(9)");
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.     
            pestaniaActiva = 2;

            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createLengua(10)");
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaLengua(Lenguas lenguas) {
        try {
            //Primero se realiza el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, lenguas.getLengua().toString(), "Lenguas", "Delate");

            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            habilidades.eliminarLenguas(lenguas);

            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();

            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;

            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizaLengua() {
        try {
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJLenguas.getLengua().toString(), "Lenguas", "Update");

            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            nuevOBJLenguas = habilidades.actualizarLenguas(nuevOBJLenguas);

            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;

            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();

            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/////////////////////////////////////////////////////////////////////////////Idiomas\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\    

    public void createIdioma() {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(1)");
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevOBJIdiomas.setClavePersonal(new Personal());
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(2)");
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJIdiomas.getClavePersonal().setClave(usuario);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(3)");
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevOBJIdiomas.setFechaEvaluacion(fechaC);
            nuevOBJIdiomas.setFechaVigenciaA(fechaO);
            nuevOBJIdiomas.setFechaVigenciaDe(fechaF);
            nuevOBJIdiomas.setEstatus("Denegado");
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(4)");

            //En este apartado se procede a comprobar si la información a registrar ya existe en la BD, en caso de ya existir se envía la información al método “updateFormacion” el cual sirve para actualizar la información, posteriormente se cierra el flujo del proceso.
            if (nuevOBJIdiomas.getIdioma() != null) {
                actualizaIdioma();
                return;
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(5)");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJIdiomas = habilidades.crearNuevoIdiomas(nuevOBJIdiomas);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(6)");
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJIdiomas.getIdioma().toString(), "Idiomas", "Insert");
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(7)");

            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(8)");

            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createIdioma(9)");

            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaIdioma(Idiomas idiomas) {
        try {
            //Primero se evalúa si su propiedad EvidenciaDoc cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (idiomas.getEvidenciaDoc() != null) {
                CargaArchivosCH.eliminarArchivo(idiomas.getEvidenciaDoc());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, idiomas.getIdioma().toString(), "Idiomas", "Delate");

            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            habilidades.eliminarIdiomas(idiomas);

            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();

            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizaIdioma() {
        try {
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJIdiomas.getIdioma().toString(), "Idiomas", "Update");

            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            nuevOBJIdiomas = habilidades.actualizarIdiomas(nuevOBJIdiomas);

            //Al finalizar la actualización de la información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();

            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();

            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//////////////////////////////////////////////////////////////////////////Informaticas\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void createrInformaticas() {
        try {            
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(1)");
            //Inicialización del objeto
            nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas()"+nuevOBJHabilidadesInformaticas);
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevOBJHabilidadesInformaticas.setClavePersonal(new Personal());
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(2)");
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJHabilidadesInformaticas.getClavePersonal().setClave(usuario);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(3)");
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevOBJHabilidadesInformaticas.setTipoConocimiento(conocimiento);
            nuevOBJHabilidadesInformaticas.setProgramaDominante(programa);
            nuevOBJHabilidadesInformaticas.setNivelConocimiento(nivel);
            nuevOBJHabilidadesInformaticas.setEstatus("Aceptado");
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(4)");
            //En este apartado se procede a comprobar si la información a registrar ya existe en la BD, en caso de ya existir se envía la información al método “updateFormacion” el cual sirve para actualizar la información, posteriormente se cierra el flujo del proceso.
            if (nuevOBJHabilidadesInformaticas.getConocimiento() != null) {
                actualizaInformaticas();
                return;
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(5)");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJHabilidadesInformaticas = habilidades.crearNuevoHabilidadesInformaticas(nuevOBJHabilidadesInformaticas);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(6)");
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJHabilidadesInformaticas.getConocimiento().toString(), "Habilidades Informáticas", "Insert");
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(7)");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(8)");
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(9)");
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.CvHabilidades.createrInformaticas(10)");
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminaInformaticas(HabilidadesInformaticas habilidadesInformaticas) {
        try {
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, habilidadesInformaticas.getConocimiento().toString(), "Habilidades Informáticas", "Delate");

            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”          
            habilidades.eliminarHabilidadesInformaticas(habilidadesInformaticas);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            mostrarListas();

            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;

            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizaInformaticas() {
        try {
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJHabilidadesInformaticas.getConocimiento().toString(), "Habilidades Informáticas", "Update");

            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            nuevOBJHabilidadesInformaticas = habilidades.actualizarHabilidadesInformaticas(nuevOBJHabilidadesInformaticas);

            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;

            //Al finalizar la actualización de la información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();

            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();

            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
////////////////////////////////////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void subirEvidenciaIdioma() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJIdiomas.setEvidenciaDoc(utilidadesCH.agregarEvidencias(file, usuario.toString(), "idiomas", ""));
        //Finalmente se actualiza el valor de la variable para permitir guardar la información.
        direccionInt = 1;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }

    public void mostrarListas() {
        try {
            //Las listas son llenadas con los registros existentes del usuario logeado en la BD, esto mediante la recepción de su clave.
            nuevaListaHabilidadesInformaticas.clear();
            nuevaListaIdiomas.clear();
            nuevaListaLenguas.clear();
            nuevaListaHabilidadesInformaticas = habilidades.mostrarHabilidadesInformaticas(usuario);
            nuevaListaIdiomas = habilidades.mostrarIdiomas(usuario);
            nuevaListaLenguas = habilidades.mostrarLenguas(usuario);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvHabilidades.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
/////////////////////////  filtrado de elementos

    public void filtarConocimientos() {
        //Al momento que el usuario selecciona una habilidad informática automáticamente el sistema le generara una lista de los programas relacionados con la habilidad
        listaConocimiento.clear();
        switch (conocimiento) {
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

    public void metodoBase() {

    }
}
