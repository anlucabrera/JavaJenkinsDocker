package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
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
    
////////////////////////////////////////////////////////////////////////////////Listas CV
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> listaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> listaCongresos = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> listaLibrosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> listaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> listaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> listaInnovaciones = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> listaDistinciones = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigacion = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> listaDesarrolloSoftwar = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
  
////////////////////////////////////////////////////////////////////////////////Funciones
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////Justificacion de Asistemcias
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidenciasReporteImpresion = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidenciasIndividuales = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidads = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidadsReporteImpresion = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidados = new ArrayList<>();

////////////////////////////////////////////////////////////////////////////////Perfil Subordinados
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private Personal nuevoOBJPersonal;
    
    
////////////////////////////////////////////////////////////////////////////////Variables extra    
    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonalJefes = new ArrayList<>();
    @Getter    @Setter    private List<String> estatus = new ArrayList<>();
        
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();
    @Getter    @Setter    private Modulosregistro modulosRegistro = new Modulosregistro();
    @Getter    @Setter    private String[] nombreAr;

    @Getter    @Setter    private LocalDate fechaNow;
    @Getter    @Setter    private Date fechaI = new Date(), fechaF = new Date(),fechaIR = new Date(), fechaFR = new Date();
    @Getter    @Setter    private Integer empleadoLogeado, contactoDestino, anioNumero = 0;
    @Getter    @Setter    private String mensajeDNotificacion = "";
    @Getter    @Setter    private Boolean visible = false, fechaReportesActiva=false;


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
        fechaNow=LocalDate.now();
        anioNumero =  fechaNow.getYear();
        visible = false;
        mostrarSubordinados();
        reportesJustificacionAsistencias();
        mostrarIncidencias(String.valueOf(fechaNow.getMonthValue()));        
    }
    
    public void mostrarSubordinados() {
        try {
            nuevaListaListaPersonalJefes=new ArrayList<>();
            nuevaListaListaPersonalJefes.clear();
            nuevaListaListaPersonalJefes = ejbPersonal.mostrarListaPersonalListSubordinados(controladorEmpleado.getNuevoOBJListaPersonal());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void reportesJustificacionAsistencias() {
        try {
            fechaReportesActiva = false;
            List<Incidencias> incidenciases = new ArrayList<>();
            incidenciases.clear();
            List<Incapacidad> incapacidads = new ArrayList<>();
            incapacidads.clear();
            listaIncidenciasReporteImpresion = new ArrayList<>();
            modulosRegistro = ejbDatosUsuarioLogeado.mostrarModuloregistro("Incidencia");
            if (modulosRegistro == null) {
                return;
            }
            LocalDate fechaMi = utilidadesCH.castearDaLD(modulosRegistro.getFechaInicio());
            LocalDate fechaMF = utilidadesCH.castearDaLD(modulosRegistro.getFechaFin());
            if (fechaNow.isAfter(fechaMi) && fechaNow.isBefore(fechaMF)) {
                fechaReportesActiva = true;
                if (fechaMF.getDayOfMonth() > 15) {
                    fechaIR = utilidadesCH.castearLDaD(LocalDate.of(fechaMF.getYear(), fechaMF.getMonthValue(), 01));
                    fechaFR = utilidadesCH.castearLDaD(LocalDate.of(fechaMF.getYear(), fechaMF.getMonthValue(), 15));
                } else {
                    if (fechaMF.getMonthValue() == 1) {
                        fechaIR = utilidadesCH.castearLDaD(LocalDate.of((fechaMF.getYear()-1), 12, 16));
                        fechaFR = utilidadesCH.castearLDaD(LocalDate.of((fechaMF.getYear()-1), 12, 31));
                    } else {
                        fechaIR = utilidadesCH.castearLDaD(LocalDate.of(fechaMF.getYear(), (fechaMF.getMonthValue() - 1), 16));
                        fechaFR = utilidadesCH.castearLDaD(LocalDate.of(fechaMF.getYear(), (fechaMF.getMonthValue() - 1), LocalDate.of(anioNumero, (fechaMF.getMonthValue() - 1), 01).lengthOfMonth()));
                    }                    
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
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public void mostrarIncidencias(String mActual) {
        try {
            listaIncidencias = new ArrayList<>();            listaIncidencias.clear();
            List<Incidencias> incidenciases = new ArrayList<>();            incidenciases.clear();
            List<Incapacidad> incapacidads = new ArrayList<>();            incapacidads.clear();
            List<Cuidados> cuidadoses = new ArrayList<>();            cuidadoses.clear();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorSubordinados.mostrarIncidencias(1)");
            fechaI = utilidadesCH.castearLDaD(LocalDate.of(anioNumero, Integer.parseInt(mActual), 01));
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorSubordinados.mostrarIncidencias(2)");
            fechaF = utilidadesCH.castearLDaD(LocalDate.of(anioNumero, Integer.parseInt(mActual), LocalDate.of(anioNumero, Integer.parseInt(mActual), 01).lengthOfMonth()));
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorSubordinados.mostrarIncidencias(3)");
            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReporte(fechaI, fechaF);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorSubordinados.mostrarIncidencias(4)");
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


    public void mostrarPerfilSubordinado() {
        try {
            nuevoOBJPersonal = new Personal();
            nuevoOBJPersonal = ejbPersonal.mostrarPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(contactoDestino);
            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            listaIncidenciasIndividuales = ejbNotificacionesIncidencias.mostrarIncidencias(contactoDestino);
            listaDocencias = ejbPersonal.mostrarListaDocencias(contactoDestino);            
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
            if (nuevoOBJListaPersonal.getCategoriaOperativa() == 30
                    || nuevoOBJListaPersonal.getCategoriaOperativa() == 32
                    || nuevoOBJListaPersonal.getCategoriaOperativa() == 41
                    || (nuevoOBJListaPersonal.getCategoriaOperativa() == 34 && (nuevoOBJListaPersonal.getAreaSuperior() >= 23 && nuevoOBJListaPersonal.getAreaSuperior() <= 29))) {
                listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
            } else {
                listaFuncioneSubordinado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Incidencias incidencias = (Incidencias) event.getObject();
            Integer dias = (int) ((utilidadesCH.castearLDaD(fechaNow).getTime() - incidencias.getFecha().getTime()) / 86400000);
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
            mostrarIncidencias(String.valueOf(fechaNow.getMonthValue()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void numeroAnioAsiganado(ValueChangeEvent event) {
        anioNumero = 0;
        anioNumero = Integer.parseInt(event.getNewValue().toString());
        mostrarIncidencias(String.valueOf(fechaNow.getMonthValue()));
    }

    public void novisible() {
        mostrarIncidencias(String.valueOf(fechaNow.getMonthValue()));
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
