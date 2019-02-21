package mx.edu.utxj.pye.sgi.controladores.ch;

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
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class CvDatosPersonales implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Personal nuevoOBJPersonal;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private String evidencia = "";
    @Getter    @Setter    private Part file;
//@EJB    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

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
            nuevoOBJInformacionAdicionalPersonal.setEdad(utilidadesCH.obtenerEdad(nuevoOBJPersonal.getFechaNacimiento()));
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
        switch (evidencia) {
            case "ComprobanteDomicilio":
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio() != null) {
                    CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio());
                }
                nuevoOBJInformacionAdicionalPersonal.setEvidenciaDomicilio(utilidadesCH.agregarEvidencias(file, nuevoOBJPersonal.getClave().toString(), "datosPersonales", evidencia));
                break;
            case "CURP":
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp() != null) {
                    CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp());
                }
                nuevoOBJInformacionAdicionalPersonal.setEvidenciaCurp(utilidadesCH.agregarEvidencias(file, nuevoOBJPersonal.getClave().toString(), "datosPersonales", evidencia));
                break;
            case "INE":
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne() != null) {
                    CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
                }
                nuevoOBJInformacionAdicionalPersonal.setEvidenciaIne(utilidadesCH.agregarEvidencias(file, nuevoOBJPersonal.getClave().toString(), "datosPersonales", evidencia));
                break;
            case "ActaDeNacimiento":
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa() != null) {
                    CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
                }
                nuevoOBJInformacionAdicionalPersonal.setEvidenciaActa(utilidadesCH.agregarEvidencias(file, nuevoOBJPersonal.getClave().toString(), "datosPersonales", evidencia));
                break;
            case "RFC":
                if (nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc() != null) {
                    CargaArchivosCH.eliminarArchivo(nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc());
                }
                nuevoOBJInformacionAdicionalPersonal.setEvidenciaRfc(utilidadesCH.agregarEvidencias(file, nuevoOBJPersonal.getClave().toString(), "datosPersonales", evidencia));
                break;
            default:
                Messages.addGlobalWarn("Es necesario el tipo de evidencia a cargar !!");
                break;
        }
        file = null;
        evidencia = "";
        actualizarInformacionAdicional();
    }
}
