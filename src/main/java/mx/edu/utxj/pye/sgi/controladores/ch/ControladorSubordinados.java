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
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorSubordinados implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonal = new ArrayList<>(), nuevaListaListaPersonalJefes = new ArrayList<>();
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> listaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> listaDesarrolloSoftwar = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> listaInnovaciones = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> listaDistinciones = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> listaLibrosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> listaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> listaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigacion = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> listaCongresos = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>(), nuevaListaFuncionesGenerales = new ArrayList<>(), estatus = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidenciasReporteImpresion = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidenciasIndividuales = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidads = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidadsReporteImpresion = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidados = new ArrayList<>();
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();
    @Getter    @Setter    private Modulosregistro modulosRegistro = new Modulosregistro();
    @Getter    @Setter    private String[] nombreAr;

    @Getter    @Setter    private Date fechaActual = new Date(), fechaI = new Date(), fechaF = new Date(),fechaIR = new Date(), fechaFR = new Date();;
    @Getter    @Setter    private Integer empleadoLogeado, contactoDestino, anioNumero = 0;
    @Getter    @Setter    private String mensajeDNotificacion = "", mes = "", numeroQuincena = "1", anio = "";
    @Getter    @Setter    private Boolean visible = false, vistaMensual = true,fechaReportesActiva=false;

    @Getter    @Setter    private Funciones nuevoOBJFunciones;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal, nuevoOBJListaPersonalFiltro, nuevoOBJListaPersonalLogeado;
    @Getter    @Setter    private Personal nuevoOBJPersonal;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;

    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {        
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
        modulosEventos();
        mostrarIncidencias(mes);        
    }

    public void modulosEventos() {
        try {
            fechaReportesActiva=false;
             List<Incidencias> incidenciases = new ArrayList<>();
            incidenciases.clear();
            List<Incapacidad> incapacidads = new ArrayList<>();
            incapacidads.clear();
            
            listaIncidenciasReporteImpresion=new ArrayList<>();
            
            modulosRegistro = ejbDatosUsuarioLogeado.mostrarModuloregistro("Incidencia");
            fechaIR = new Date();
            fechaFR = new Date(); 
            String m="",a="",d="";
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (modulosRegistro != null) {                                                                
                if ((fechaActual.compareTo(modulosRegistro.getFechaInicio()) >= 0) && (fechaActual.compareTo(modulosRegistro.getFechaFin()) <= 0)) {
                    fechaReportesActiva=true;
                    if (modulosRegistro.getFechaFin().getDate() > 15) {
                        fechaIR = dateFormat.parse("01" + "/" + mes + "/" + anio);
                        fechaFR = dateFormat.parse("15" + "/" + mes + "/" + anio);
                    } else {
                        a = "20" + (fechaActual.getYear() - 100);
                        switch (modulosRegistro.getFechaFin().getMonth()) {
                            case 0:
                                d = "31";                                m = "12";                                a = "20" + (fechaActual.getYear() - 101);                                break;
                            case 1:                                d = "31";                                m = "01";                                break;
                            case 2:                                d = "28";                                m = "02";                                break;
                            case 3:                                d = "31";                                m = "03";                                break;
                            case 4:                                d = "30";                                m = "04";                                break;
                            case 5:                                d = "31";                                m = "05";                                break;
                            case 6:                                d = "30";                                m = "06";                                break;
                            case 7:                                d = "31";                                m = "07";                                break;
                            case 8:                                d = "31";                                m = "08";                                break;
                            case 9:                                d = "30";                                m = "09";                                break;
                            case 10:                                d = "31";                                m = "10";                                break;
                            case 11:                                d = "30";                                m = "11";                                break;
                        }
                        fechaIR = dateFormat.parse("16" + "/" + m + "/" + a);
                        fechaFR = dateFormat.parse(d + "/" + m + "/" + a);
                    }

                    incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReporte(fechaIR, fechaFR);
                    if (!incidenciases.isEmpty()) {
                        incidenciases.forEach((Incidencias t) -> {
                            if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                                listaIncidenciasReporteImpresion.add(t);
                            }
                        });
                    }

                    listaIncapacidadsReporteImpresion = new ArrayList<>();
                    listaIncapacidadsReporteImpresion.clear();
                    incapacidads = ejbNotificacionesIncidencias.mostrarIncapacidadReporte(fechaIR, fechaFR);                    
                    if (!incapacidads.isEmpty()) {
                        incapacidads.forEach((t) -> {
                            if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                                listaIncapacidadsReporteImpresion.add(t);
                            }
                        });
                    }
                }

            }
            if (!listaIncidenciasReporteImpresion.isEmpty()) {
                listaIncidenciasReporteImpresion.forEach((t) -> {
                    if (t.getEstatus().equals("Aceptado")) {
                        t.setEstatus("JUSTIFICADO");
                    } else if (t.getEstatus().equals("Denegado")) {
                        t.setEstatus("NO JUSTIFICADO");
                    } else if (t.getEstatus().equals("Pendiente")) {
                        t.setEstatus("");
                    }
                });
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPerfilSubordinado() {
        try {
            nuevoOBJPersonal = new Personal();
            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(contactoDestino);
            nuevoOBJPersonal = ejbPersonal.mostrarPersonalLogeado(contactoDestino);
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
            listaLenguas.clear();
            listaDesarrolloSoftwar.clear();
            listaDesarrollosTecnologicos.clear();
            listaInnovaciones.clear();
            listaDistinciones.clear();
            listaLibrosPubs.clear();
            listaArticulosp.clear();
            listaMemoriaspub.clear();
            listaInvestigacion.clear();
            listaCongresos.clear();
            
            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(contactoDestino);
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(contactoDestino);
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(contactoDestino);
            listaIdiomas = ejbHabilidades.mostrarIdiomas(contactoDestino);
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(contactoDestino);
            listaLenguas = ejbHabilidades.mostrarLenguas(contactoDestino);
            listaDesarrolloSoftwar = ejbTecnologia.mostrarDesarrolloSoftware(contactoDestino);
            listaDesarrollosTecnologicos = ejbTecnologia.mostrarDesarrollosTecnologicos(contactoDestino);
            listaInnovaciones = ejbTecnologia.mostrarInnovaciones(contactoDestino);
            listaDistinciones = ejbPremios.mostrarDistinciones(contactoDestino);
            listaLibrosPubs = ejbProduccionProfecional.mostrarLibrosPub(contactoDestino);
            listaArticulosp = ejbProduccionProfecional.mostrarArticulosp(contactoDestino);
            listaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(contactoDestino);
            listaInvestigacion = ejbProduccionProfecional.mostrarInvestigacion(contactoDestino);
            listaCongresos = ejbProduccionProfecional.mostrarCongresos(contactoDestino);
            
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
            nuevoOBJListaPersonalLogeado = ejbPersonal.mostrarListaPersonal(empleadoLogeado);
            nuevaListaListaPersonal = ejbPersonal.mostrarListaPersonalListSubordinados(nuevoOBJListaPersonalLogeado);
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
            List<Incidencias> incidenciases = new ArrayList<>();
            incidenciases.clear();
            List<Incapacidad> incapacidads = new ArrayList<>();
            incapacidads.clear();
            List<Cuidados> cuidadoses = new ArrayList<>();
            cuidadoses.clear();
            
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
            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReporte(fechaI, fechaF);
            if (!incidenciases.isEmpty()) {
                incidenciases.forEach((t) -> {
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
            }
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
            incapacidads = ejbNotificacionesIncidencias.mostrarIncapacidadReporte(fechaI, fechaF);
            if (!incapacidads.isEmpty()) {
                incapacidads.forEach((t) -> {
                    if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaIncapacidads.add(t);
                    }
                });
            }
            
            listaCuidados = new ArrayList<>();
            listaCuidados.clear();
            cuidadoses = ejbNotificacionesIncidencias.mostrarCuidadosReporte(fechaI, fechaF);
            if (!cuidadoses.isEmpty()) {
                cuidadoses.forEach((t) -> {
                    if ((t.getPersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getPersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getPersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaCuidados.add(t);
                    }
                });
            }                        
            Ajax.update("@(form)");
            Ajax.update("@(form)");
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
            switch (incidencias.getFecha().getDay()){
                case 1:  maximo = 3; break;
                case 2:  maximo = 3; break;
                case 3:  maximo = 5; break;
                case 4:  maximo = 5; break;
                case 5:  maximo = 5; break;
                case 6:  maximo = 4; break;
            }
            
            if (dias <= maximo) {
                utilidadesCH.agregaBitacora(empleadoLogeado, incidencias.getIncidenciaID().toString(), "Incidencia", "Justificación "+incidencias.getEstatus());
                ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
                Messages.addGlobalInfo("¡Operación exitosa!");
            } else {
                Messages.addGlobalInfo("¡El timepo asiganado para validar incidencia a expirado!");
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
        Messages.addGlobalWarn("¡Operación cancelada!");
    }

     public String convertirRutaVistaEvidencia(String ruta) {
        if (!"".equals(ruta)) {
            File file = new File(ruta);
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!");
            return null;
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
      
    public void imprimirValores() {
    }
}
