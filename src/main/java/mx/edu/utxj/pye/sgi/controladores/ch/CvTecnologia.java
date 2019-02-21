package mx.edu.utxj.pye.sgi.controladores.ch;

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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class CvTecnologia implements Serializable {

    private static final long serialVersionUID = 5357883394691887772L;

    // variables basicas
    @Getter    @Setter    private Integer usuario, direccionInt = 0, pestaniaActiva;
    //Variables de objetos Entitys
    @Getter    @Setter    private DesarrollosTecnologicos nuevoOBJDesarrolloTecnologico;
    @Getter    @Setter    private DesarrolloSoftware nuevoOBJDesarrolloSoftware;
    @Getter    @Setter    private Innovaciones nuevoOBJInnovaciones;
    // listas de variables basicas
    // listas de entitys
    @Getter    @Setter    private List<DesarrollosTecnologicos> nuevaListaDesarrolloTecnologico = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> nuevaListaDesarrolloSoftware = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> nuevaListaInnovaciones = new ArrayList<>();
    // variable archivos  
    @Getter    @Setter    private Part file;
    // EJbs   
    @EJB    private EjbTecnologia ejbTecnologia;
    //Injects
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {
        usuario = controladorEmpleado.getEmpleadoLogeado();

        nuevoOBJDesarrolloSoftware = new DesarrolloSoftware();
        nuevoOBJDesarrolloTecnologico = new DesarrollosTecnologicos();
        nuevoOBJInnovaciones = new Innovaciones();

        nuevaListaDesarrolloSoftware.clear();
        nuevaListaDesarrolloTecnologico.clear();
        nuevaListaInnovaciones.clear();

        listaConsultas();
    }

    public void reiniciarValores() {
        nuevoOBJDesarrolloTecnologico = new DesarrollosTecnologicos();
        nuevoOBJInnovaciones = new Innovaciones();
        nuevoOBJDesarrolloSoftware = new DesarrolloSoftware();
    }

    //////////////////////////////Desarrollos Tecnológicos///////////////////////////////
    public void guardarDesarrolloTec() {
        try {
            //Inicialización de las relaciones entre las tablas “Desarrollo Tecnologico” con “Personal”
            nuevoOBJDesarrolloTecnologico.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJDesarrolloTecnologico.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJDesarrolloTecnologico.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJDesarrolloTecnologico = ejbTecnologia.crearNuevoDesarrollosTecnologicos(nuevoOBJDesarrolloTecnologico);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJDesarrolloTecnologico.getDesarrollo().toString(), "Desarrollo Tecnológico", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            listaConsultas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarDesTec(DesarrollosTecnologicos desarrollosTecnologicos) {
        try {
            //Primero se evalúa si su propiedad evidenciaNombremiento cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (desarrollosTecnologicos.getDocumentoRespaldo() != null) {
                CargaArchivosCH.eliminarArchivo(desarrollosTecnologicos.getDocumentoRespaldo());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, desarrollosTecnologicos.getDesarrollo().toString(), "Desarrollo Tecnológico", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbTecnologia.eliminarDesarrollosTecnologicos(desarrollosTecnologicos);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            listaConsultas();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDesarrolloTecnologico(RowEditEvent event) {
        try {
            DesarrollosTecnologicos actualizarDesTec = (DesarrollosTecnologicos) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarDesTec.getDesarrollo().toString(), "Desarrollo Tecnológico", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            actualizarDesTec = ejbTecnologia.actualizarDesarrollosTecnologicos(actualizarDesTec);
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////Innovaciones///////////////////////////////
    public void guardarInnovacion() {
        try {
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevoOBJInnovaciones.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJInnovaciones.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJInnovaciones.setEstatus("Aceptado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJInnovaciones = ejbTecnologia.crearNuevoInnovaciones(nuevoOBJInnovaciones);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJInnovaciones.getInnovacion().toString(), "Innovaciones", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            listaConsultas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarInnovacion(Innovaciones innovaciones) {
        try {
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, innovaciones.getInnovacion().toString(), "Innovaciones", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbTecnologia.eliminarInnovaciones(innovaciones);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            listaConsultas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateInnovaciones(RowEditEvent event) {
        try {
            Innovaciones actualizarInno = (Innovaciones) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarInno.getInnovacion().toString(), "Innovaciones", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            actualizarInno = ejbTecnologia.actualizarInnovaciones(actualizarInno);
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 1;
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //////////////////////////////////////Desarrollo de Software///////////////////////////////
    public void guardarDesaSoft() {
        try {
            //Inicialización de las relaciones entre las tablas “ExperienciasLaborales” con “Personal”
            nuevoOBJDesarrolloSoftware.setClavePersonal(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJDesarrolloSoftware.getClavePersonal().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJDesarrolloSoftware.setEstatus("Aceptado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJDesarrolloSoftware = ejbTecnologia.crearNuevoDesarrolloSoftware(nuevoOBJDesarrolloSoftware);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJDesarrolloSoftware.getDesarrollo().toString(), "Desarrollo de Software", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            listaConsultas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarDesSoft(DesarrolloSoftware desarrolloSoftware) {
        try {
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, desarrolloSoftware.getDesarrollo().toString(), "Desarrollo de Software", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbTecnologia.eliminarDesarrolloSoftware(desarrolloSoftware);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            listaConsultas();
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDesarrolloSoftware(RowEditEvent event) {
        try {
            DesarrolloSoftware actualizarDesSof = (DesarrolloSoftware) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarDesSof.getDesarrollo().toString(), "Desarrollo de Software", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            actualizarDesSof = ejbTecnologia.actualizarDesarrolloSoftware(actualizarDesSof);
            //Antes de culminar se actualiza el valor de la pestaña del TabView en la interfaz gráfica.
            pestaniaActiva = 2;
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método, esto a través de la invocación del método “reiniciarValores();”
            reiniciarValores();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////////////Consultas de mostrado de información
    public void listaConsultas() {
        try {
            //Las listas son llenadas con los registros existentes del usuario logeado en la BD, esto mediante la recepción de su clave.
            nuevaListaDesarrolloTecnologico = ejbTecnologia.mostrarDesarrollosTecnologicos(usuario);
            nuevaListaDesarrolloSoftware = ejbTecnologia.mostrarDesarrolloSoftware(usuario);
            nuevaListaInnovaciones = ejbTecnologia.mostrarInnovaciones(usuario);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvTecnologia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaDesTec() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevoOBJDesarrolloTecnologico.setDocumentoRespaldo(utilidadesCH.agregarEvidencias(file, usuario.toString(), "desarrolloTecnologico", ""));
        //Antes de culminar se actualiza el valor de la variable para permitir guardar la información.
        direccionInt = 1;
        //Finalmente se procede a reiniciar las variables utilizadas en el método
        file = null;
    }

    public void metodoBase() {

    }
}
