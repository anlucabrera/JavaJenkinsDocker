package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.PaseListaDoc;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class AdministracionControl implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<String> estatus = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Modulosregistro> modulosregistros = new ArrayList<>();
    @Getter    @Setter    private List<Eventos> eventoses = new ArrayList<>();
    @Getter    @Setter    private List<EventosAreas> eventosesAreases = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<Procesopoa> procesopoas = new ArrayList<>();
    @Getter    @Setter    private List<ConfiguracionPropiedades> cps = new ArrayList<>();
    @Getter    @Setter    private List<EventosAreaPOA> areaPOAs = new ArrayList<>();

    @Getter    @Setter    private Eventos eventos = new Eventos();
    @Getter    @Setter    private Integer eventoC = 0;
    @Getter    @Setter    private Short areaC = 0;
    @Getter    @Setter    private Date fehDate = new Date();

    @Getter    @Setter    private EventosAreas editEventosAreas = new EventosAreas();

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador administrador;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;

    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        estatus.add("Pendiente");
        mostrarProcesos();        
        mostrarIncidencias();
        mostrarModulos();
        mostrarConfiguracionProp();
    }


// -----------------------------------------------------------------------------Busqueda    
    public void mostrarProcesos() {
        try {
            procesopoas = new ArrayList<>();
            eventoses = new ArrayList<>();
            eventosesAreases = new ArrayList<>();
            areasUniversidads = new ArrayList<>();
            areaPOAs = new ArrayList<>();

            areasUniversidads.clear();
            eventoses.clear();
            eventosesAreases.clear();
            procesopoas.clear();
            areaPOAs.clear();
            
            eventoses = ejbUtilidadesCH.mostrarEventoses();
            eventosesAreases = ejbUtilidadesCH.mostrarEventosesAreases();
            procesopoas = ejbUtilidadesCH.mostrarProcesopoa();
            procesopoas.forEach((t) -> {
                if (t.getResponsable() != null) {
                    try {
                        areasUniversidads.add(areasLogeo.mostrarAreasUniversidad(t.getArea()));
                    } catch (Throwable ex) {
                        Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                        Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564) {
                if (!eventosesAreases.isEmpty()) {
                    eventosesAreases.forEach((t) -> {
                        try {
                            AreasUniversidad au = areasLogeo.mostrarAreasUniversidad(t.getEventosAreasPK().getAreaOperativa());
                            LocalDateTime datetimeLimite = utilidadesCH.castearDaLDT(t.getEventos().getFechaFin()).plusDays(t.getDiasExtra()).plusHours(23).plusMinutes(59).plusSeconds(59);
                            LocalDate dateLimite = utilidadesCH.castearDaLD(t.getEventos().getFechaFin()).plusDays(t.getDiasExtra());
                            LocalDate dateLibera = utilidadesCH.castearDaLD(t.getEventos().getFechaFin());
                            if (dateLimite.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                                dateLibera = dateLibera.plusDays((t.getDiasExtra() - 4));
                            } else {
                                dateLibera = dateLibera.plusDays((t.getDiasExtra() - 6));
                            }
                            Integer minutos = (int) ((utilidadesCH.castearLDTaD(datetimeLimite).getTime() - utilidadesCH.castearLDTaD(LocalDateTime.now()).getTime()) / 60000);
                            Integer diasR = minutos / 1440;
                            Integer horasR = (minutos % 1440);
                            Integer res = (minutos % 60);
                            String mensaje="";
                            if (diasR<0) {
                                    mensaje = "El periodo venció hace " + Math.abs(diasR) + " día(s) " + Math.abs((horasR / 60)) + " hora(s) com " + Math.abs(res) + " minutos";
                            } else {
                                mensaje = "Resta " + diasR + " día(s) " + (horasR / 60) + " hora(s) com " + (res) + " minutos";
                            }
                            areaPOAs.add(new EventosAreaPOA(t, au.getNombre(), t.getEventos().getTipo() + " -- " + t.getEventos().getNombre(), buscarPersonal(au.getResponsable()), utilidadesCH.castearLDaD(dateLibera), utilidadesCH.castearLDaD(dateLimite), mensaje));
                        } catch (Throwable ex) {
                            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String buscarPersonal(Integer clave) {
        try {
            Personal p = new Personal();
            if (clave != null) {
                p = ejbPersonal.mostrarPersonalLogeado(clave);
                if (p == null) {
                    return "Nombre del Responsable";
                } else {
                    return p.getNombre();
                }
            } else {
                return "Nombre del Responsable";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void mostrarConfiguracionProp() {
        try {
            cps = new ArrayList<>();
            cps.clear();

            cps = administrador.buscarConfiguracionPropiedadeses();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarModulos() {
        try {
            modulosregistros = new ArrayList<>();
            modulosregistros.clear();
            List<Modulosregistro> ms = new ArrayList<>();
            ms.clear();

            ms = ejbUtilidadesCH.mostrarModulosregistrosGeneral();
            if (!ms.isEmpty()) {
                if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564) {
                    modulosregistros.addAll(ms);
                } else {
                    ms.forEach((t) -> {
                        if (t.getNombre().equals("Incidencia")) {
                            modulosregistros.add(t);
                        }
                    });
                }
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarIncidencias() {
        try {
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();

            List<Incidencias> is = new ArrayList<>();
            is.clear();
            is = ejbNotificacionesIncidencias.mostrarIncidenciasTotales();
            if (!is.isEmpty()) {

                is.forEach((t) -> {
                    if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564) {
                        listaIncidencias.add(t);
                    } else {
                        if (t.getEstatus().equals("Pendiente")) {
                            listaIncidencias.add(t);
                        }
                    }
                });
            }
            Ajax.update("frmInciGeneral");
            Collections.sort(listaIncidencias, (x, y) -> Short.compare(x.getClavePersonal().getAreaOperativa(), y.getClavePersonal().getAreaOperativa()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public String buscarAreaEventosA(Integer area) {
        try {

            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(Short.parseShort(area.toString()));
            return areaU.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public String buscarCorreoArea(Integer area) {
        try {

            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(Short.parseShort(area.toString()));
            return areaU.getCorreoInstitucional();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
// -----------------------------------------------------------------------------Creacion   
    public void agreggarEventoArea() {
        try {
            editEventosAreas = new EventosAreas();
            editEventosAreas.setEventos(new Eventos());
            editEventosAreas.setEventosAreasPK(new EventosAreasPK());
            eventoses.forEach((t) -> {
                if (t.getEvento() == eventoC) {
                    eventos = t;
                }
            });
            Integer diasExtra = (int) ((fehDate.getTime() - eventos.getFechaFin().getTime()) / 86400000);

            editEventosAreas.setEventos(new Eventos(eventoC));
            editEventosAreas.setEventosAreasPK(new EventosAreasPK(eventoC, areaC));

            editEventosAreas.setDiasExtra(diasExtra);
            ejbUtilidadesCH.agregarEventosesAreases(editEventosAreas);
            Messages.addGlobalInfo("¡Evento Agregado!");
            mostrarProcesos();
            editEventosAreas = new EventosAreas();
            eventoC = 0;
            areaC = 0;
            Ajax.update("frmInci");

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
// -----------------------------------------------------------------------------Edicion    
    public void onRowEditEventos(RowEditEvent event) {
        try {
            Eventos e = (Eventos) event.getObject();
            ejbUtilidadesCH.actualizarEventoses(e);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarProcesos();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Incidencias incidencias = (Incidencias) event.getObject();
            utilidadesCH.agregaBitacora(controladorEmpleado.getNuevoOBJListaPersonal().getClave(), incidencias.getIncidenciaID().toString(), "Incidencia", "Actualización Admin");
            ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarIncidencias();
            Ajax.update("frmInciGeneral");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditModulos(RowEditEvent event) {
        try {
            Modulosregistro m = (Modulosregistro) event.getObject();
            ejbUtilidadesCH.actualizarModulosregistro(m);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarIncidencias();
            Ajax.update("frmModulos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditEventosAreas(RowEditEvent event) {
        try {
            EventosAreaPOA a = (EventosAreaPOA) event.getObject();
            EventosAreas eventosAreas = new EventosAreas();
            eventosAreas.setEventos(new Eventos());
            eventosAreas.setEventosAreasPK(new EventosAreasPK());
            
            Integer diasExtra=(int) ((a.getEvento().getEventos().getFechaFin().getTime()- a.getFechaLimiteProceso().getTime()) / 86400000);
            eventosAreas = a.getEvento();
            eventosAreas.setDiasExtra(Math.abs(diasExtra));            
            ejbUtilidadesCH.actualizarEventosesAreases(eventosAreas);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarProcesos();
            Ajax.update("frmEventosAreas");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditConfProp(RowEditEvent event) {
        try {
            ConfiguracionPropiedades cp = (ConfiguracionPropiedades) event.getObject();
            cp=administrador.actualizarConfiguracionPropiedades(cp);
            Messages.addGlobalInfo("¡Operación exitosa! "+cp.getClave());
            mostrarConfiguracionProp();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
    
// -----------------------------------------------------------------------------Eliminacion
    public void eliminarEventoArea(EventosAreaPOA ea) {
        try {
            ejbUtilidadesCH.eliminarEventosesEventosAreas(ea.getEvento());
            mostrarProcesos();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarConfiguracionProp(ConfiguracionPropiedades cp) {
        try {
            administrador.eliminarConfiguracionPropiedades(cp);
            mostrarConfiguracionProp();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


// -----------------------------------------------------------------------------Utilidades
    public void imprimirValores() {
    }
    
    public static class EventosAreaPOA {

        @Getter        @Setter        private EventosAreas evento;
        @Getter        @Setter        private String area;
        @Getter        @Setter        private String proceso;
        @Getter        @Setter        private String responsable;
        @Getter        @Setter        private Date fecaValidacion;
        @Getter        @Setter        private Date fechaLimiteProceso;
        @Getter        @Setter        private String diasrestantes;

        public EventosAreaPOA(EventosAreas evento, String area, String proceso, String responsable, Date fecaValidacion, Date fechaLimiteProceso, String diasrestantes) {
            this.evento = evento;
            this.area = area;
            this.proceso = proceso;
            this.responsable = responsable;
            this.fecaValidacion = fecaValidacion;
            this.fechaLimiteProceso = fechaLimiteProceso;
            this.diasrestantes = diasrestantes;
        }
    }
}
