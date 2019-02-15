package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
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
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class CvDatosPersonales implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Personal nuevoOBJPersonal;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private String evidencia="";
    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
//@EJB    
    @EJB    EjbCarga carga;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal; 
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH calculaEdad;

    @PostConstruct
    public void init() {
        evidencia = "";
        consultarPerfil();
        actualizarInformacionAdicional();
    }

    /*
    Este método busca la información del Personal logeado, esto mediante la obtención de su clave    
     */
    public void consultarPerfil() {
        try {
            nuevoOBJPersonal = ejbPersonal.mostrarPersonalLogeado(controladorEmpleado.getEmpleadoLogeado());
            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(controladorEmpleado.getEmpleadoLogeado());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvDatosPersonales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Este método sirve para actualizar la información adicional del personal, comprueba si existe algún registro de información adicional en caso de no existir creara el registro
     */
    public void actualizarInformacionAdicional() {
        try {
            nuevoOBJInformacionAdicionalPersonal.setEdad(calculaEdad.obtenerEdad(nuevoOBJPersonal.getFechaNacimiento()));
            if (nuevoOBJInformacionAdicionalPersonal.getClave() == null) {
                nuevoOBJInformacionAdicionalPersonal.setClave(nuevoOBJPersonal.getClave());
                nuevoOBJInformacionAdicionalPersonal = ejbPersonal.crearNuevoInformacionAdicionalPersonal(nuevoOBJInformacionAdicionalPersonal);
            } else {
                nuevoOBJInformacionAdicionalPersonal = ejbPersonal.actualizarInformacionAdicionalPersonal(nuevoOBJInformacionAdicionalPersonal);
            }
            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(CvDatosPersonales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
    Este método sirve para agregar las evidencias relacionadas con el registro de su información adicional, mediante un swich donde se encuentran cada una de las opciones posibles antes de agregar 
    evidencia comprueba si ya existe una registrada en caso de ser así eliminará la que exista y proseguirá a agregar la nueva evidencia en caso de que no exista simplemente la agregara,
     */
    public void agregarEvidencias() {
        if (file != null) {
            ruta = carga.subir(file, new File(nuevoOBJPersonal.getClave().toString().concat(File.separator).concat("datosPersonales").concat(File.separator).concat(evidencia).concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                switch (evidencia) {
                    case "ComprobanteDomicilio":
                        if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio() != null) {
                            CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio());
                        }
                        nuevoOBJInformacionAdicionalPersonal.setEvidenciaDomicilio(ruta);
                        break;
                    case "CURP":
                        if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp() != null) {
                            CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp());
                        }
                        nuevoOBJInformacionAdicionalPersonal.setEvidenciaCurp(ruta);
                        break;
                    case "INE":
                        if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne() != null) {
                            CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
                        }
                        nuevoOBJInformacionAdicionalPersonal.setEvidenciaIne(ruta);
                        break;
                    case "ActaDeNacimiento":
                        if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa() != null) {
                            CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
                        }
                        nuevoOBJInformacionAdicionalPersonal.setEvidenciaActa(ruta);
                        break;
                    case "RFC":
                        if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc() != null) {
                            CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc());
                        }
                        nuevoOBJInformacionAdicionalPersonal.setEvidenciaRfc(ruta);
                        break;
                    default:
                        Messages.addGlobalWarn("Es necesario el tipo de evidencia a cargar !!");
                        break;
                }
                ruta = null;
                file = null;
                evidencia = "";
                actualizarInformacionAdicional();
            } else {
                ruta = null;
                file = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }
}
