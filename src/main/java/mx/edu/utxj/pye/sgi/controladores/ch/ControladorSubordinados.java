package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Actividadesremotas;
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
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
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
    @Getter    @Setter    private List<Incidencias> listaIncidenciasIndividuales = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidads = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidados = new ArrayList<>();
    @Getter    @Setter    private List<Actividadesremotas> actividadesremotas = new ArrayList<>();
     
    @Getter    @Setter    private List<Incidencias> listaIncidenciasReporteImpresion = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidadesReporteImpresion = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidadosReporteImpresion = new ArrayList<>();
    @Getter    @Setter    private List<Actividadesremotas> listaActividadesremotasReporteImpresion = new ArrayList<>();

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
    @Getter    @Setter    private LocalDateTime fechaActual;
    @Getter    @Setter    private LocalDate fechaI ,fechaF,fechaIR, fechaFR;
    @Getter    @Setter    private Integer contactoDestino, anioNumero = 0;
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

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        estatus.add("Pendiente");
        fechaNow = LocalDate.now();
        anioNumero = fechaNow.getYear();
        visible = false;
        mostrarSubordinados();
        reportesJustificacionAsistencias();
        mostrarIncidencias(String.valueOf(fechaNow.getMonthValue()));
    }

    public void mostrarSubordinados() {
        try {
            nuevaListaListaPersonalJefes = new ArrayList<>();
            nuevaListaListaPersonalJefes.clear();
            nuevaListaListaPersonalJefes = ejbPersonal.mostrarListaPersonalListSubordinados(controladorEmpleado.getNuevoOBJListaPersonal());
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void reportesJustificacionAsistencias() {
        try {
            listaIncidenciasReporteImpresion = new ArrayList<>();
            listaIncapacidadesReporteImpresion = new ArrayList<>();
            listaCuidadosReporteImpresion = new ArrayList<>();
            listaActividadesremotasReporteImpresion = new ArrayList<>();
                        
            fechaActual=LocalDateTime.now();
            fechaReportesActiva = false;
            
            List<Incidencias> incidenciases = new ArrayList<>(); incidenciases.clear();
            List<Incapacidad> incapacidads = new ArrayList<>(); incapacidads.clear();
            List<Cuidados> cuidadoses = new ArrayList<>(); cuidadoses.clear();
            List<Actividadesremotas> remotas = new ArrayList<>(); remotas.clear();
            
            modulosRegistro = ejbDatosUsuarioLogeado.mostrarModuloregistro("Incidencia");
            if (modulosRegistro == null) {
                return;
            }
            
            LocalDateTime fechaMi = utilidadesCH.castearDaLDT(modulosRegistro.getFechaInicio());
            LocalDateTime fechaMF = utilidadesCH.castearDaLDT(modulosRegistro.getFechaFin());

            if (fechaMF.getDayOfMonth() > 15) {
                fechaIR = LocalDate.of(fechaMF.getYear(), fechaMF.getMonthValue(), 01);
                fechaFR = LocalDate.of(fechaMF.getYear(), fechaMF.getMonthValue(), 15);
            } else {
                if (fechaMF.getMonthValue() == 1) {
                    fechaIR = LocalDate.of(fechaMF.getYear() - 1, 12, 01);
                    fechaFR = LocalDate.of(fechaMF.getYear() - 1, 12, 31);
                } else {
                    fechaIR = LocalDate.of(fechaMF.getYear(), (fechaMF.getMonthValue() - 1), 16);
                    fechaFR = LocalDate.of(fechaMF.getYear(), (fechaMF.getMonthValue() - 1), LocalDate.of(anioNumero, (fechaMF.getMonthValue() - 1), 01).lengthOfMonth());
                }
            }
            fechaReportesActiva = true;

            listaIncidenciasReporteImpresion = new ArrayList<>();
            listaIncidenciasReporteImpresion.clear();
            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReporte(utilidadesCH.castearLDaD(fechaIR), utilidadesCH.castearLDaD(fechaFR));
            if (!incidenciases.isEmpty()) {
                incidenciases.forEach((Incidencias t) -> {
                    if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaIncidenciasReporteImpresion.add(t);
                    }
                });
            }

            listaIncapacidadesReporteImpresion = new ArrayList<>();
            listaIncapacidadesReporteImpresion.clear();
            incapacidads = ejbNotificacionesIncidencias.mostrarIncapacidadReporte(utilidadesCH.castearLDaD(fechaIR), utilidadesCH.castearLDaD(fechaFR));
            if (!incapacidads.isEmpty()) {
                incapacidads.forEach((t) -> {
                    if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaIncapacidadesReporteImpresion.add(t);
                    }
                });
            }

            listaCuidadosReporteImpresion = new ArrayList<>();
            listaCuidadosReporteImpresion.clear();
            cuidadoses = ejbNotificacionesIncidencias.mostrarCuidadosReporte(utilidadesCH.castearLDaD(fechaIR), utilidadesCH.castearLDaD(fechaFR));
            if (!cuidadoses.isEmpty()) {
                cuidadoses.forEach((t) -> {
                    if ((t.getPersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getPersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getPersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaCuidadosReporteImpresion.add(t);
                    }
                });
            }

            listaActividadesremotasReporteImpresion = new ArrayList<>();
            listaActividadesremotasReporteImpresion.clear();
            remotas = ejbNotificacionesIncidencias.mostrarActividadesremotasReporte(utilidadesCH.castearLDaD(fechaIR), utilidadesCH.castearLDaD(fechaFR));
            if (!remotas.isEmpty()) {
                remotas.forEach((t) -> {
                    if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaActividadesremotasReporteImpresion.add(t);
                    }
                });
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
            List<Actividadesremotas> remotas = new ArrayList<>();
            remotas.clear();
            fechaI = LocalDate.of(anioNumero, Integer.parseInt(mActual), 01);
            fechaF = LocalDate.of(anioNumero, Integer.parseInt(mActual), LocalDate.of(anioNumero, Integer.parseInt(mActual), 01).lengthOfMonth());
            
            
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();
            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReporte(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
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
            incapacidads = ejbNotificacionesIncidencias.mostrarIncapacidadReporte(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
            if (!incapacidads.isEmpty()) {
                incapacidads.forEach((t) -> {
                    if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaIncapacidads.add(t);
                    }
                });
            }

            listaCuidados = new ArrayList<>();
            listaCuidados.clear();
            cuidadoses = ejbNotificacionesIncidencias.mostrarCuidadosReporte(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
            if (!cuidadoses.isEmpty()) {
                cuidadoses.forEach((t) -> {
                    if ((t.getPersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getPersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getPersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        listaCuidados.add(t);
                    }
                });
            }
            
            actividadesremotas = new ArrayList<>();
            actividadesremotas.clear();
            remotas = ejbNotificacionesIncidencias.mostrarActividadesremotasReporte(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
            if (!remotas.isEmpty()) {
                remotas.forEach((t) -> {
                    if ((t.getClavePersonal().getAreaOperativa() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa() || t.getClavePersonal().getAreaSuperior() == controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()) && !Objects.equals(t.getClavePersonal().getClave(), controladorEmpleado.getNuevoOBJListaPersonal().getClave())) {
                        actividadesremotas.add(t);
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
            switch (utilidadesCH.castearDaLD(incidencias.getFecha()).getDayOfWeek()) {
                case MONDAY:                    maximo = 3;                    break;
                case TUESDAY:                    maximo = 3;                    break;
                case WEDNESDAY:                    maximo = 5;                    break;
                case THURSDAY:                    maximo = 5;                    break;
                case FRIDAY:                    maximo = 5;                    break;
                case SATURDAY:                    maximo = 4;                    break;
            }
            if (dias <= maximo) {
                utilidadesCH.agregaBitacora(controladorEmpleado.getEmpleadoLogeado(), incidencias.getIncidenciaID().toString(), "Incidencias", "Update");                
                ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
                Messages.addGlobalInfo("¡Operación exitosa!");
            } else {
                Messages.addGlobalInfo("¡El tiempo asignado para validar incidencia ha expirado!");
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

    public void imprimirValores() {
    }
    
    public void actualizarAP(ValueChangeEvent e) {
        try {
            String id = e.getComponent().getClientId();
            Actividadesremotas ag = actividadesremotas.get(Integer.parseInt(id.split("tblActividades:")[1].split(":validar")[0]));
            ag.setValidado((Boolean) e.getNewValue());
            ejbNotificacionesIncidencias.actualizarActividadesremotas(ag);
            mostrarIncidencias(String.valueOf(fechaNow.getMonthValue()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void validarTrabajador(Actividadesremotas actividadesr) {
        try {
            List<Actividadesremotas> as = new ArrayList<>();
            as = actividadesremotas.stream().filter(t -> Objects.equals(t.getClavePersonal().getClave(), actividadesr.getClavePersonal().getClave())).collect(Collectors.toList());

            if (!as.isEmpty()) {
                as.forEach((t) -> {
                    try {
                        t.setValidado(Boolean.TRUE);
                        ejbNotificacionesIncidencias.actualizarActividadesremotas(t);
                    } catch (Throwable ex) {
                        Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            mostrarIncidencias(String.valueOf(fechaNow.getMonthValue()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
