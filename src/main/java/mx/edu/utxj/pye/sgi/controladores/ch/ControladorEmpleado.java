package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@SessionScoped
public class ControladorEmpleado implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

////////////////////////////////////////////////////////////////////////////////Datos Perosnales 
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal;
    @Getter    @Setter    private Procesopoa procesopoa=new Procesopoa();

////////////////////////////////////////////////////////////////////////////////Listas complementarias
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();
    @Getter    @Setter    private List<Notificaciones> listaNotificaciones = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> incidenciases = new ArrayList<>();


    @Getter    @Setter    private Integer empleadoLogeado;
    @Getter    @Setter    private String clavePersonalLogeado, mandos = "", fechaCVBencimiento, fechaFuncionesBencimiento, fechaLimiteCurriculumVitae = "", fechaLimiteRegistroFunciones = "",
            mensajeIndex1 = "", mensajeIndex2 = "";

    @Getter    @Setter    private Boolean fechaLimiteCV, fechaLimiteFunciones, tienePOA = false, estiloInfo = false, mensajeGeneral = false;
    @Getter    @Setter    private List<Modulosregistro> nuevaListaModulosregistro = new ArrayList<>();

    @Getter    @Setter    private EventosAreas nuevaEventosAreas = new EventosAreas();

    @Getter    @Setter    private AreasUniversidad nuevaAreasUniversidad = new AreasUniversidad();

    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now();
    @Getter    @Setter    private LocalDateTime fechaActualHora = LocalDateTime.now();
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("EEEE d MMMM yyyy");
    @Getter    @Setter    private DateFormat dateFormatHora = new SimpleDateFormat("h:mm a");
    @Getter    @Setter    private LocalDate fechaI = LocalDate.now();
    @Getter    @Setter    private LocalDate fechaF = LocalDate.now();

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;
    
    @Inject    LogonMB logonMB;
    @Inject    UtilidadesCH uch;

    @PostConstruct
    public void init() {
        // Comentar la siguiente asignación cuando saiiut falle//
        empleadoLogeado = Integer.parseInt(logonMB.getListaUsuarioClaveNomina().getNumeroNomina());
//      empleadoLogeado = Integer.parseInt(logonMB.getListaUsuarioClaveNominaShiro().getClaveNomina());
        // fin de asignación
        clavePersonalLogeado = empleadoLogeado.toString();
        mostrarPerfilLogeado();
        informacionComplementariaAEmpleadoLogeado();
        areaPoa();
    }

    public void informacionComplementariaAEmpleadoLogeado() {
        try {
            incidenciases.clear();
            listaDocencias.clear();
            listaNotificaciones.clear();

            fechaI = LocalDate.now();
            fechaF = LocalDate.now();

            fechaI = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01);
            fechaF = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01).lengthOfMonth());


            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReportePendientes(uch.castearLDaD(fechaI), uch.castearLDaD(fechaF), nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getClave());
            listaDocencias = ejbPersonal.mostrarListaDocencias(empleadoLogeado);
            listaNotificaciones = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuarios(empleadoLogeado, 0);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPerfilLogeado() {
        try {

            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(empleadoLogeado);
            nuevoOBJListaPersonal = ejbPersonal.mostrarListaPersonal(empleadoLogeado);

            if (nuevoOBJInformacionAdicionalPersonal == null) {
                nuevoOBJInformacionAdicionalPersonal = new InformacionAdicionalPersonal();
                nuevoOBJInformacionAdicionalPersonal.setClave(empleadoLogeado);
                nuevoOBJInformacionAdicionalPersonal.setAutorizacion(false);
                nuevoOBJInformacionAdicionalPersonal.setEdad(uch.obtenerEdad(nuevoOBJListaPersonal.getFechaNacimiento()));
                nuevoOBJInformacionAdicionalPersonal = ejbPersonal.crearNuevoInformacionAdicionalPersonal(nuevoOBJInformacionAdicionalPersonal);
            }

            if (nuevoOBJListaPersonal == null) {
                Messages.addGlobalFatal("Sin datos para la clave " + empleadoLogeado);
            }
            
            switch (nuevoOBJListaPersonal.getClave()) {
                case 97:
                    nuevoOBJListaPersonal.setAreaOperativa(Short.parseShort("21"));
                    break;
                case 343:
                    nuevoOBJListaPersonal.setAreaOperativa(Short.parseShort("19"));
                    break;
            }

            fechasModulos();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fechasModulos() {
        try {
            nuevaListaModulosregistro.clear();
            nuevaListaModulosregistro = ejbUtilidadesCH.mostrarModulosregistro(nuevoOBJListaPersonal.getActividadNombre());
            nuevaListaModulosregistro.forEach((t) -> {
                switch (t.getNombre()) {
                    case "CV":
                        fechaActualHora = LocalDateTime.now();
                        fechaLimiteCV = fechaActualHora.isBefore(uch.castearDaLDT(t.getFechaFin()));
                        fechaCVBencimiento = dateFormat.format(t.getFechaFin());
                        fechaLimiteCurriculumVitae = "La fecha límite para la actualización de currículum vitae es el día " + dateFormat.format(t.getFechaFin()) + " a las " + dateFormatHora.format(t.getFechaFin());
                        break;
                    case "Funciones":
                        fechaLimiteFunciones = fechaActual.isBefore(uch.castearDaLD(t.getFechaFin()));
                        fechaFuncionesBencimiento = dateFormat.format(t.getFechaFin());
                        fechaLimiteRegistroFunciones = "La fecha límite para la revisión y actualización de funciones del personal administrativo y docente es el día " + dateFormat.format(t.getFechaFin()) + " a las " + dateFormatHora.format(t.getFechaFin());
                        break;
                }
            });
            switch (nuevoOBJListaPersonal.getActividadNombre()) {
                case "Directivo":
                case "Coordinador":
                    mandos = "Superiores";
                    break;
                case "Docente":
                    mandos = "Docente";
                    break;
                case "Administrativo":
                    mandos = "Administrativo";
                    break;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void areaPoa() {
        try {
            procesopoa = new Procesopoa();
            nuevaAreasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(nuevoOBJListaPersonal.getAreaOperativa());
            if (nuevaAreasUniversidad != null) {
                if (nuevaAreasUniversidad.getTienePoa()) {
                    if (Objects.equals(nuevaAreasUniversidad.getResponsable(), empleadoLogeado)) {
                        tienePOA = true;
                        procesopoa = ejbUtilidadesCH.mostrarEtapaPOA(nuevoOBJListaPersonal.getAreaOperativa());
                        eventosRegistro();
                    } else {
                        if (nuevaAreasUniversidad.getArea() == 6) {
                            procesopoa = ejbUtilidadesCH.mostrarEtapaPOA(nuevoOBJListaPersonal.getAreaOperativa());
                            eventosRegistro();
                        }
                        tienePOA = false;
                    }
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eventosRegistro() {
        try {
            fechaActual = LocalDate.now();                              
            mensajeGeneral = false;
            estiloInfo = false;
            Integer diasR = (int) ((procesopoa.getEvaluacion().getFechaFin().getTime() - uch.castearLDaD(fechaActual).getTime()) / 86400000);
            Integer diasI = (int) ((uch.castearLDaD(fechaActual).getTime() - procesopoa.getEvaluacion().getFechaInicio().getTime()) / 86400000);
            if (diasI <= 2) {
                mensajeIndex1 = "Inicio del periodo para la Evaluación de actividades, Carga de Evidencia, y Registro en Sistema del mes de " + procesopoa.getEvaluacion().getMesEvaluacion();
                estiloInfo = true;
                mensajeGeneral = true;
            }
            if (diasR <= 5) {
                mensajeIndex1 = "La fecha límite para la Evaluación de actividades, Carga de Evidencia, y Registro en Sistema del mes de " + procesopoa.getEvaluacion().getMesEvaluacion() + " ¡Está por vencer!";
                mensajeIndex2 = "Restan " + diasR + " días";
                estiloInfo = false;
                mensajeGeneral = true;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Boolean periodoActivoPOA(Eventos t) {
        if ((fechaActual.isBefore(uch.castearDaLD(t.getFechaFin())) || (fechaActual.getDayOfMonth() == uch.castearDaLD(t.getFechaFin()).getDayOfMonth() && fechaActual.getMonthValue() == uch.castearDaLD(t.getFechaFin()).getMonthValue() && fechaActual.getYear() == uch.castearDaLD(t.getFechaFin()).getYear())) && (fechaActual.isAfter(uch.castearDaLD(t.getFechaInicio())) || fechaActual.equals(uch.castearDaLD(t.getFechaInicio())))) {
            return true;
        } else {
            nuevaEventosAreas = new EventosAreas();
             nuevaEventosAreas = ejbUtilidadesCH.mostrarEventoAreas(new EventosAreasPK(t.getEvento(), nuevoOBJListaPersonal.getAreaOperativa()));
             if(nuevaEventosAreas != null) {
                 return true;
             }else{
                 return false;
             }
         }
     }

   }
