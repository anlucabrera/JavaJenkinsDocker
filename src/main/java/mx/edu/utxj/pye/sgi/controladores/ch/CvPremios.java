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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class CvPremios implements Serializable {

    private static final long serialVersionUID = -6775211516127859011L;

    // variables basicas
    @Getter    @Setter    private Integer usuario, direccionInt = 0;
    //Variables de objetos Entitys
    @Getter    @Setter    private Distinciones nuevoOBJDistinciones;
    // listas de entitys
    @Getter    @Setter    private List<Distinciones> nuevaListaDistinciones = new ArrayList<>();
    // variable archivos  
    @Getter    @Setter    private Part file;
    // EJbs  
    @EJB    private EjbPremios ejbPremios;
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
        nuevoOBJDistinciones = new Distinciones();
        nuevaListaDistinciones.clear();
        distincionesRegistradas();
    }

    public void distincionesRegistradas() {
        try {
            nuevaListaDistinciones = ejbPremios.mostrarDistinciones(usuario);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvPremios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createDistincion() {
        try {
            //Inicialización de las relaciones entre las tablas “Capacitacionespersonal” con “Personal”, "CursosModalidad" y con "CursosTipo"
            nuevoOBJDistinciones.setClaveEmpleado(new Personal());
            //Una vez realizada la inicialización se procede a realizar la asignación de los valores correspondientes mediante los métodos Get().Set(Valor_A_Asignar)
            nuevoOBJDistinciones.getClaveEmpleado().setClave(usuario);
            //Posteriormente se procese a realizar la asignación de valores que no se obtienen mediante la interfaz grafica
            nuevoOBJDistinciones.setEstatus("Denegado");
            //En caso de que sea información nueva se procede a enviar la información al “EJB” el cual la agregara a la BD.
            nuevoOBJDistinciones = ejbPremios.crearNuevoDistinciones(nuevoOBJDistinciones);
            //Después de agregar la información a la BD, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, nuevoOBJDistinciones.getDistincion().toString(), "Distinciones", "Insert");
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();”
            distincionesRegistradas();
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método
            nuevoOBJDistinciones = new Distinciones();
            direccionInt = 0;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalWarn("Error al intentar guardar información");
            Logger.getLogger(CvPremios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaDistincion() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevoOBJDistinciones.setEvidenciaDistincion(utilidadesCH.agregarEvidencias(file, usuario.toString(), "premiosDistinciones", ""));
        //Se bacía el valor de la variable file
        direccionInt = 1;
        file = null;
    }

    public void delDistincion(Distinciones distinciones) {
        try {
            //Primero se evalúa si su propiedad evidenciaCapacitacion cuenta con información, de ser así se procede a invocar el método eliminarArchivo del controlador CargaArchivosCH el cual removerá el archivo permanentemente del servidor.
            if (distinciones.getEvidenciaDistincion() != null) {
                CargaArchivosCH.eliminarArchivo(distinciones.getEvidenciaDistincion());
            }
            //Después de comprobar la existencia de archivos relacionados al registro, se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, distinciones.getDistincion().toString(), "Distinciones", "Delate");
            //Posteriormente de realizar el registro en la bitácora se procede a eliminar el registro, esto invocado al “EJB”
            ejbPremios.eliminarDistinciones(distinciones);
            //Al finalizar los dos registros de información se procede a realizar la actualización de las listas, para esto se invoca al método “mostrarListas();” 
            distincionesRegistradas();
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvPremios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateDistinciones(RowEditEvent event) {
        try {
            Distinciones actualizarD = (Distinciones) event.getObject();
            //Primero se procede a realizar el registro de la “Bitácora”, para esto se requiere de enviar ciertos parámetros, los cuales se describen dentro el método en el controlador de utilidadesCH
            utilidadesCH.agregaBitacora(usuario, actualizarD.getDistincion().toString(), "Distinciones", "Update");
            //Se procede a invocar el “EJB” el cual mediante la recepción del objeto se encargará de procesar y actualizar la información en la BD. 
            ejbPremios.actualizarDistinciones(actualizarD);
            //Posteriormente de actualizar las listas se procede a reiniciar las variables utilizadas en el método
            actualizarD = new Distinciones();
            direccionInt = 0;
            //Finalmente se le informa al usuario cual es el resultado obtenido
            utilidadesCH.mensajes("", "I", "C");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(CvPremios.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }

    public void metodoBase() {

    }
}
