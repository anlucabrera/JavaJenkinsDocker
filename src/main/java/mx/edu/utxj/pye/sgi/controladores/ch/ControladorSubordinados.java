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
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorSubordinados implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonal = new ArrayList<>(), nuevaListaListaPersonalJefes = new ArrayList<>();
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigaciones = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>(), nuevaListaFuncionesGenerales = new ArrayList<>(), estatus = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidads = new ArrayList<>();
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();

    @Getter    @Setter    private Date fechaActual = new Date(), fechaI = new Date(), fechaF = new Date();
    @Getter    @Setter    private Integer empleadoLogeado, contactoDestino, anioNumero = 0;
    @Getter    @Setter    private String mensajeDNotificacion = "", mes = "", numeroQuincena = "1", anio = "";
    @Getter    @Setter    private Boolean visible = false, vistaMensual = true;

    @Getter    @Setter    private Funciones nuevoOBJFunciones;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal, nuevoOBJListaPersonalFiltro, nuevoOBJListaPersonalLogeado;
    @Getter    @Setter    private Personal nuevoOBJPersonal;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorSubordinados Inicio: " + System.currentTimeMillis());
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        estatus.add("Pendiente");
        empleadoLogeado = controladorEmpleado.getEmpleadoLogeado();
        nuevaListaFuncionesEspecificas.clear();
        nuevaListaFuncionesGenerales.clear();
        nuevoOBJFunciones = new Funciones();
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
        visible = false;
        mostrarContactosParaNotificacion();
        mostrarIncidencias(mes);
        System.out.println("ControladorSubordinados Fin: " + System.currentTimeMillis());
    }

    public void mostrarPerfilSubordinado() {
        try {
            nuevoOBJPersonal = new Personal();
            nuevoOBJInformacionAdicionalPersonal = ejbDatosUsuarioLogeado.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(contactoDestino);
            nuevoOBJPersonal = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(contactoDestino);
            informacionCV();
            mostrarFuncioneSubordinado();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void informacionCV() {
        try {
            listaFormacionAcademica.clear();
            listaExperienciasLaborales.clear();
            listaCapacitacionespersonal.clear();
            listaIdiomas.clear();
            listaHabilidadesInformaticas.clear();
            listaInvestigaciones.clear();
            listaDocencias.clear();
            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(contactoDestino);
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(contactoDestino);
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(contactoDestino);
            listaIdiomas = ejbHabilidades.mostrarIdiomas(contactoDestino);
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(contactoDestino);
            listaInvestigaciones = ejbProduccionProfecional.mostrarInvestigacion(contactoDestino);
            listaDocencias = ejbDatosUsuarioLogeado.mostrarListaDocencias(contactoDestino);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarFuncioneSubordinado() {
        try {
            nuevaListaFuncionesGenerales.clear();
            nuevaListaFuncionesEspecificas.clear();
            switch (nuevoOBJListaPersonal.getCategoriaOperativa()) {
                case 30:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 32:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 34:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 41:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                default:
                    listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
            }
            if (listaFuncioneSubordinado.isEmpty()) {
            } else {
                for (int i = 0; i <= listaFuncioneSubordinado.size() - 1; i++) {
                    nuevoOBJFunciones = new Funciones();
                    nuevoOBJFunciones = listaFuncioneSubordinado.get(i);
                    if ("GENERAL".equals(nuevoOBJFunciones.getTipo())) {
                        nuevaListaFuncionesGenerales.add(nuevoOBJFunciones.getNombre());
                    } else {
                        nuevaListaFuncionesEspecificas.add(nuevoOBJFunciones.getNombre());
                    }
                }
            }
            listaFuncioneSubordinado.clear();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarContactosParaNotificacion() {
        try {
            nuevoOBJListaPersonalLogeado = new ListaPersonal();
            nuevoOBJListaPersonalLogeado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(empleadoLogeado);
            nuevaListaListaPersonal = ejbDatosUsuarioLogeado.mostrarListaSubordinados(nuevoOBJListaPersonalLogeado);
            for (int i = 0; i <= nuevaListaListaPersonal.size() - 1; i++) {
                nuevoOBJListaPersonalFiltro = nuevaListaListaPersonal.get(i);
                if (Objects.equals(nuevoOBJListaPersonalFiltro.getClave(), nuevoOBJListaPersonalLogeado.getClave())) {
                } else {
                    nuevaListaListaPersonalJefes.add(nuevoOBJListaPersonalFiltro);
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
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
                    diai = "16";
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
                }
            }
            fechaI = dateFormat.parse(diai + "/" + mes + "/" + anio);
            fechaF = dateFormat.parse(diaf + "/" + mes + "/" + anio);
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();
            listaIncapacidads = new ArrayList<>();
            listaIncapacidads.clear();

            ejbNotificacionesIncidencias.mostrarIncidenciasReporte(fechaI, fechaF).forEach((Incidencias t) -> {
                if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                    if (visible) {
                        if (t.getEstatus().equals("Pendiente")) {
                            listaIncidencias.add(t);
                        }
                    } else {
                        listaIncidencias.add(t);
                    }
                }
            });
            ejbNotificacionesIncidencias.mostrarIncapacidadReporte(fechaI, fechaF).forEach((t) -> {
                if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                    listaIncapacidads.add(t);
                }
            });
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Incidencias incidencias = (Incidencias) event.getObject();
            Integer dias = (int) ((fechaActual.getTime() - incidencias.getFecha().getTime()) / 86400000);
            Integer maximo = 0;
            if (incidencias.getFecha().getDay() == 5) {
                maximo = 5;
            } else {
                maximo = 3;
            }
            if (dias <= maximo) {
                ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
                Messages.addGlobalInfo("¡Operación exitosa!!");
            } else {
                Messages.addGlobalInfo("¡El timepo asiganado para valiadar incidencia a expirado!!");
            }
            mostrarIncidencias(mes);
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

    public void numeroAnioAsiganado(ValueChangeEvent event) {
        anio = "";
        anioNumero = 0;
        anioNumero = Integer.parseInt(event.getNewValue().toString());
        anio = "20" + anioNumero;
        mostrarIncidencias(mes);
    }

    public void novisible() {
        mostrarIncidencias(mes);
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!!");
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

     
    public void imprimirValores() {
    }
}
