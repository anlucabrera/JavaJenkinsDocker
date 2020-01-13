package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
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
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class CvHabilidades implements Serializable {

    private static final long serialVersionUID = -473305993584095094L;
    // variables basicas
    @Getter    @Setter    private Integer usuario, direccionInt = 0, pestaniaActiva;
    //Variables de objetos Entitys
    @Getter    @Setter    private Lenguas nuevOBJLenguas;
    @Getter    @Setter    private Idiomas nuevOBJIdiomas;
    @Getter    @Setter    private HabilidadesInformaticas nuevOBJHabilidadesInformaticas;
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

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        usuario = controladorEmpleado.getEmpleadoLogeado();

        nuevOBJIdiomas = new Idiomas();
        nuevOBJLenguas = new Lenguas();
        nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();

        nuevaListaIdiomas.clear();
        nuevaListaLenguas.clear();
        nuevaListaHabilidadesInformaticas.clear();

        mostrarListas();
    }

    public void reiniciarValores() {
        nuevOBJLenguas = new Lenguas();
        nuevOBJIdiomas = new Idiomas();
        nuevOBJHabilidadesInformaticas = new HabilidadesInformaticas();
        direccionInt = 0;
        listaConocimiento.clear();

    }
/////////////////////////////////////////////////////////////////////////////Lenguas\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public void createLengua() {
        try {
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevOBJLenguas.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJLenguas.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevOBJLenguas.setEstatus("Aceptado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJLenguas = habilidades.crearNuevoLenguas(nuevOBJLenguas);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJLenguas.getLengua().toString(), "Lenguas", "Insert");
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

    public void actualizaLengua(RowEditEvent event) {
        try {
            Lenguas actualizarLeng = (Lenguas) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarLeng.getLengua().toString(), "Lenguas", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            habilidades.actualizarLenguas(actualizarLeng);
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
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevOBJIdiomas.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJIdiomas.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevOBJIdiomas.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJIdiomas = habilidades.crearNuevoIdiomas(nuevOBJIdiomas);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJIdiomas.getIdioma().toString(), "Idiomas", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            mostrarListas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
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

    public void actualizaIdioma(RowEditEvent event) {
        try {
            Idiomas actualizarIdio = (Idiomas) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarIdio.getIdioma().toString(), "Idiomas", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            habilidades.actualizarIdiomas(actualizarIdio);
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
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevOBJHabilidadesInformaticas.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevOBJHabilidadesInformaticas.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevOBJHabilidadesInformaticas.setEstatus("Aceptado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevOBJHabilidadesInformaticas = habilidades.crearNuevoHabilidadesInformaticas(nuevOBJHabilidadesInformaticas);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevOBJHabilidadesInformaticas.getConocimiento().toString(), "Habilidades Informáticas", "Insert");
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

    public void actualizaInformaticas(RowEditEvent event) {
        try {
            HabilidadesInformaticas actualizarHabInfo = (HabilidadesInformaticas) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarHabInfo.getConocimiento().toString(), "Habilidades Informáticas", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            habilidades.actualizarHabilidadesInformaticas(actualizarHabInfo);
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

    public void metodoBase() {

    }
}
