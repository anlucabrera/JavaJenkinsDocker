package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
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
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasGeneral implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidads = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> listaAreasUniversidads = new ArrayList<>();
    @Getter    @Setter    private String[] nombreAr;
    @Getter    @Setter    private String areaNombre = "";
    @Getter    @Setter    private Short area=0;

    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    @Getter    @Setter    private Date fechaActual = new Date(), fechaI = new Date(), fechaF = new Date();
    @Getter    @Setter    private Integer empleadoLogeado, contactoDestino, anioNumero = 0;
    @Getter    @Setter    private String mensajeDNotificacion = "", mes = "", numeroQuincena = "1", anio = "";
    @Getter    @Setter    private Boolean vistaMensual = true;

//@EJB    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private EjbAreasLogeo areasLogeo;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorIncidenciasPersonal Inicio: " + System.currentTimeMillis());
        fechaI = new Date();
        fechaF = new Date();
        fechaActual = new Date();
        if (fechaActual.getDate() <= 15) {
            numeroQuincena = "1";
        } else {
            numeroQuincena = "2";
        }
        if (fechaActual.getMonth() <= 8) {
            mes = "0" + (fechaActual.getMonth() + 1);
        } else {
            mes = String.valueOf(fechaActual.getMonth() + 1);
        }
        anioNumero = fechaActual.getYear() - 100;
        anio = "20" + anioNumero;
        area = 0;
        areaNombre = "Todas las áreas";
        mostrarIncidencias(mes);
        mostrarareas();
        System.out.println("ControladorIncidenciasPersonal Fin: " + System.currentTimeMillis());
    }

    public void mostrarareas() {
        try {
            listaAreasUniversidads = new ArrayList<>();
            listaAreasUniversidads.clear();
            listaAreasUniversidads = areasLogeo.mostrarAreasUniversidad();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarIncidencias(String mActual) {
        try {
            mes = mActual;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            fechaI = new Date();
            fechaF = new Date();
            String diai = "";
            String diaf = "";
            if (vistaMensual) {
                diai = "01";
                diaf = "31";
            } else {
                if (numeroQuincena.equals("1")) {
                    diai = "01";
                    diaf = "15";
                } else {
                    switch (mes) {
                        case "01": diaf = "31"; break;
                        case "02": diaf = "28"; break;
                        case "03": diaf = "31"; break;
                        case "04": diaf = "30"; break;
                        case "05": diaf = "31"; break;
                        case "06": diaf = "30"; break;
                        case "07": diaf = "31"; break;
                        case "08": diaf = "31"; break;
                        case "09": diaf = "30"; break;
                        case "10": diaf = "31"; break;
                        case "11": diaf = "30"; break;
                        case "12": diaf = "31"; break;
                    }
                    diai = "16";
                }
            }
            fechaI = dateFormat.parse(diai + "/" + mes + "/" + anio);
            fechaF = dateFormat.parse(diaf + "/" + mes + "/" + anio);
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();
            ejbNotificacionesIncidencias.mostrarIncidenciasReporte(fechaI, fechaF).forEach((t) -> {
                if (area != 0) {
                    if (t.getClavePersonal().getAreaOperativa() == area) {
                        listaIncidencias.add(t);
                    }
                } else {
                    listaIncidencias.add(t);
                }
            });
            if (!listaIncidencias.isEmpty()) {
                listaIncidencias.forEach((t) -> {
                    if (t.getEstatus().equals("Aceptado")) {
                        t.setEstatus("JUSTIFICADO");
                    } else if (t.getEstatus().equals("Denegado")) {
                        t.setEstatus("NO JUSTIFICADO");
                    } else if (t.getEstatus().equals("Pendiente")) {
                        t.setEstatus("");
                    }
                });
            }
            
            listaIncapacidads = new ArrayList<>();
            listaIncapacidads.clear();
            ejbNotificacionesIncidencias.mostrarIncapacidadReporte(fechaI, fechaF).forEach((t) -> {
                if (area != 0) {
                    if (t.getClavePersonal().getAreaOperativa() == area) {
                        listaIncapacidads.add(t);
                    }
                } else {
                    listaIncapacidads.add(t);
                }
            });         
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
    public void vistaMensualAsiganado(ValueChangeEvent event) {
        vistaMensual = true;
        vistaMensual = (Boolean) event.getNewValue();
        mostrarIncidencias(mes);
    }

    public void numeroQuincenaAsiganado(ValueChangeEvent event) {
        numeroQuincena = "";
        numeroQuincena = event.getNewValue().toString();
        mostrarIncidencias(mes);
    }

    public void numeroAreaAsiganado(ValueChangeEvent event) {
        area = 0;
        areaNombre = "";
        area = Short.parseShort(event.getNewValue().toString());
        if (area == 0) {
            areaNombre = "Todas las áreas";
        } else {
            areaNombre = buscarArea(area);
        }
        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasGeneral.numeroAreaAsiganado()" + areaNombre);
        mostrarIncidencias(mes);
    }
   
   
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        anio = "";
        anioNumero = 0;
        anioNumero = Integer.parseInt(event.getNewValue().toString());
        anio = "20" + anioNumero;
        mostrarIncidencias(mes);
    }

    public String buscarArea(Short area) {
        try {
            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(area);
            return areaU.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String calculaM(String area) {
        nombreAr = area.split(":");
        String hora = nombreAr[0];
        String minu = nombreAr[1];
        Integer h = 0;
        Integer m = 0;
        Integer t = 0;
        h = Integer.parseInt(hora) * 60;
        m = Integer.parseInt(minu);
        t = h + m;
        return t.toString();
    }

    public String convertirRutaVistaEvidencia(String ruta) {
        if (!"".equals(ruta)) {
            File file = new File(ruta);
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!!");
            return null;
        }
    }

    public void novisible() {
        mostrarIncidencias(mes);
    }

    public void imprimirValores() {
    }
}
