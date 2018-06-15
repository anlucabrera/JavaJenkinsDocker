package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

@Named
@ManagedBean
@ViewScoped
public class DatosPersonales implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

//List<Personal> 
    @Getter    @Setter    private List<Personal> nuevaListaPersonal = new ArrayList<>();
    @Getter    @Setter    private Personal nuevoOBJPersonal;
//List<Personal> 
    @Getter    @Setter    private List<InformacionAdicionalPersonal> nuevaListaInformacionAdicionalPersonal = new ArrayList<>();
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
//List<Integer> 
    @Getter    @Setter    private Integer usuario;
//List<String> 
    @Getter    @Setter    private String procedencia, fechaN, claveTraba,evidencia="";
//List<Date>
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    @Getter    @Setter    private Date date = new Date();
    
    @Getter    @Setter    private Integer diaH,mesH,anioH,diaN,mesN,anioN,restaA;
//@EJB    
    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;
    
    @EJB    EjbCarga carga;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec; 
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUpdate ejbUpdate; 
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbCreate ejbCreate;
//@Inject
    @Inject
    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("DatosPersonales Inicio: " + System.currentTimeMillis());
        usuario = controladorEmpleado.getEmpleadoLogeado();
        claveTraba = controladorEmpleado.getClavePersonalLogeado();
        procedencia = "";
        evidencia = "";
        consultarPerfil();
        System.out.println("DatosPersonales Fin: " + System.currentTimeMillis());
    }

    public void consultarPerfil() {
        try {
            nuevaListaPersonal.clear();
            nuevaListaInformacionAdicionalPersonal.clear();

            nuevaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosPorClave(usuario);
            nuevaListaInformacionAdicionalPersonal = ejbSelectec.mostrarListaDeInformacionAdicionalPersonal(usuario);

            if (nuevaListaPersonal.isEmpty()) {

            } else {
                nuevoOBJPersonal = nuevaListaPersonal.get(0);
                procedencia = nuevoOBJPersonal.getLocalidad() + ", " + nuevoOBJPersonal.getMunicipio() + ", " + nuevoOBJPersonal.getEstado() + ", " + nuevoOBJPersonal.getPais() + ", ";
                fechaN = dateFormat.format(nuevoOBJPersonal.getFechaNacimiento());
            }

            if (nuevaListaInformacionAdicionalPersonal.isEmpty()) {
                nuevoOBJInformacionAdicionalPersonal = new InformacionAdicionalPersonal();
                obtenerEdad();
            } else {
                nuevoOBJInformacionAdicionalPersonal = nuevaListaInformacionAdicionalPersonal.get(0);
                obtenerEdad();
//                System.out.println("nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa() "+nuevoOBJInformacionAdicionalPersonal.getEvidenciaActa());
//                System.out.println("nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp() "+nuevoOBJInformacionAdicionalPersonal.getEvidenciaCurp());
//                System.out.println("nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio() "+nuevoOBJInformacionAdicionalPersonal.getEvidenciaDomicilio());
//                System.out.println("nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne() "+nuevoOBJInformacionAdicionalPersonal.getEvidenciaIne());
//                System.out.println("nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc() "+nuevoOBJInformacionAdicionalPersonal.getEvidenciaRfc());
//                System.out.println("nuevoOBJInformacionAdicionalPersonal.getEdad() 2 " + nuevoOBJInformacionAdicionalPersonal.getEdad());
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(DatosPersonales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarInformacionAdicional() {
        try {
            System.out.println("nuevoOBJInformacionAdicionalPersonal.getClave() " + nuevoOBJInformacionAdicionalPersonal.getClave());
            nuevoOBJInformacionAdicionalPersonal.setEstatus("Aceptado");
            if (nuevoOBJInformacionAdicionalPersonal.getClave() == null) {
                nuevoOBJInformacionAdicionalPersonal.setClave(usuario);
                nuevoOBJInformacionAdicionalPersonal.setEstatus("Aceptado");
                nuevoOBJInformacionAdicionalPersonal = ejbCreate.agregarInformacionAdicionalPersonal(nuevoOBJInformacionAdicionalPersonal);
            } else {
                nuevoOBJInformacionAdicionalPersonal.setEstatus("Aceptado");
                nuevoOBJInformacionAdicionalPersonal = ejbUpdate.actualizarInformacionAdicionalPersonal(nuevoOBJInformacionAdicionalPersonal);
            }
            Messages.addGlobalInfo("¡Operación exitosa!!");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(DatosPersonales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidencias() {
        System.out.println("inicio " + evidencia);
        System.out.println("file " + file);
        if (file != null) {
            ruta = carga.subir(file, new File(claveTraba.concat(File.separator).concat("datosPersonales").concat(File.separator).concat(evidencia).concat(File.separator)));
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
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intentelo nuevamente !!");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !!");
        }
    }

    public void obtenerEdad() {
        Date fechaActual = new Date();

        diaH = fechaActual.getDay();
        diaN = nuevoOBJPersonal.getFechaNacimiento().getDay();
        mesH = fechaActual.getMonth();
        mesN = nuevoOBJPersonal.getFechaNacimiento().getMonth();
        anioH = fechaActual.getYear();
        anioN = nuevoOBJPersonal.getFechaNacimiento().getYear();

        if (Objects.equals(diaH, diaN)) {
            if (Objects.equals(mesH, mesN)) {
                restaA = anioH - anioN;
            } else {
                if (mesH < mesN) {
                    restaA = (anioH - anioN) - 1;
                } else {
                    restaA = anioH - anioN;
                }
            }
        } else {
            if (diaH < diaN) {
                if (Objects.equals(mesH, mesN)) {
                    restaA = (anioH - anioN) - 1;
                } else {
                    if (mesH < mesN) {
                        restaA = (anioH - anioN) - 1;
                    } else {
                        restaA = anioH - anioN;
                    }
                }
            } else {
                if (Objects.equals(mesH, mesN)) {
                    restaA = (anioH - anioN);
                } else {
                    if (mesH < mesN) {
                        restaA = (anioH - anioN) - 1;
                    } else {
                        restaA = anioH - anioN;
                    }
                }
            }
        }
        nuevoOBJInformacionAdicionalPersonal.setEdad(restaA);
//        System.out.println("nuevoOBJInformacionAdicionalPersonal.getEdad() 1 " + nuevoOBJInformacionAdicionalPersonal.getEdad());
    }
}
