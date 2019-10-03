package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosadminstracion;
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
    @Getter    @Setter    private List<Integer> administradores = new ArrayList();
    @Getter    @Setter    private String clavePersonalLogeado, mandos = "", fechaCVBencimiento, fechaFuncionesBencimiento, fechaLimiteCurriculumVitae = "", fechaLimiteRegistroFunciones = "",
            mensajeIndex1 = "", mensajeIndex2 = "";

    @Getter    @Setter    private Boolean fechaLimiteCV, fechaLimiteFunciones, tienePOA = false, estiloInfo = false, mensajeGeneral = false,evaluable=false;
    @Getter    @Setter    private List<Modulosregistro> nuevaListaModulosregistro = new ArrayList<>();

    @Getter    @Setter    private EventosAreas nuevaEventosAreas = new EventosAreas();

    @Getter    @Setter    private AreasUniversidad nuevaAreasUniversidad = new AreasUniversidad();
    @Getter    @Setter    private List<MenuDinamico> moduloses = new ArrayList();
    
    @Getter    @Setter    private List<Menu> menus = new ArrayList();
    @Getter    @Setter    private Nivel3 menu;

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
        administradores = new ArrayList();
        administradores.clear();
        administradores.add(302);
        administradores.add(564);
        administradores.add(300);
        administradores.add(148);
        administradores.add(284);
        administradores.add(613);
        mostrarPerfilLogeado();
        informacionComplementariaAEmpleadoLogeado();
        areaPoa();
        crearMenuAdministrador();
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
            if (nuevoOBJListaPersonal == null) {
                Messages.addGlobalFatal("El usuario para la clave "+empleadoLogeado+" no Existe o ha sido dado de baja, si cree que es un error favor de Comunicarse con el área de personal");
                return;
            }

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
            procesopoa = ejbUtilidadesCH.mostrarEtapaPOAPersona(nuevoOBJListaPersonal.getClave());
            if (procesopoa == null) {
                if (administradores.contains(nuevoOBJListaPersonal.getClave())) {
                    procesopoa = ejbUtilidadesCH.mostrarEtapaPOAArea(nuevoOBJListaPersonal.getAreaOperativa());
                }
            }
            if (procesopoa != null) {
                if (procesopoa.getEvaluacion() == null) {
                    procesopoa.setEvaluacion(new Calendarioevaluacionpoa());
                    Calendarioevaluacionpoa periodoEvaluacion = new Calendarioevaluacionpoa();
                    periodoEvaluacion = ejbUtilidadesCH.mostrarCalendarioEvaluacion(uch.castearLDaD(fechaActual));
                    if (periodoEvaluacion != null) {
                        evaluable = true;
                    } else {
                        periodoEvaluacion = ejbUtilidadesCH.mostrarCalendarioEvaluacion(uch.castearLDaD(LocalDate.of(fechaActual.getYear(), fechaActual.getMonth(), 1)));
                        if (periodoEvaluacion != null) {
                            periodoEvaluacion = ejbUtilidadesCH.mostrarCalendarioEvaluacion(uch.castearLDaD(LocalDate.of(fechaActual.getYear(), fechaActual.getMonth(), 25)));
                        }
                    }
                    procesopoa.setEvaluacion(periodoEvaluacion);
                }
                tienePOA = true;
                eventosRegistro();
            } else {
                tienePOA = false;
                evaluable = false;
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

//////////////////////////////////////////////////////////////////////////////// Menú dinamico
    public void crearMenuAdministrador() {
        try {
//            List<MenuDinamico> msN1 = new ArrayList<>();
//            msN1 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 1, "Administrador","Trabajador");
//            if (!msN1.isEmpty()) {
//                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(msN1)" + msN1.size());
//                msN1.forEach((n1) -> {
                    List<MenuDinamico> msN2 = new ArrayList<>();
//                    msN2 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 2, n1.getTituloNivel1(),"Trabajador");
                    msN2 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 2, "Administrador","Trabajador");
                    List<Nivel2> nivel2s = new ArrayList<>();
                    if (!msN2.isEmpty()) {
//                        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(n1)" + n1.getTituloNivel1() + "-msN2-" + msN2.size());
                        msN2.forEach((n2) -> {
                            List<MenuDinamico> msN3 = new ArrayList<>();
                            msN3 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 3, n2.getTituloNivel2(),"Trabajador");
                            List<Nivel3> nivel3s = new ArrayList<>();
                            if (!msN3.isEmpty()) {
//                                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(n2)" + n2.getTituloNivel2() + "-msN3-" + msN3.size());
                                msN3.forEach((n3) -> {
//                                    System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(n3)" + n3.getTitulonivel3());
                                    List<MenuDinamico> msN4 = new ArrayList<>();
                                    msN4 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 4, n3.getTitulonivel3(),"Trabajador");
                                    List<Nivel4> nivel4s = new ArrayList<>();
                                    if (!msN4.isEmpty()) {
//                                        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(n3)" + n3.getTitulonivel3() + "-msN4-" + msN4.size());
                                        msN4.forEach((n4) -> {
                                            nivel4s.add(new Nivel4(n4.getTitulonivel4(), n4.getIconoNivel4(), n4.getEnlacenivel4(),n4.getEstatus()));
                                        });
                                    }
                                    nivel3s.add(new Nivel3(n3.getTitulonivel3(), n3.getIconoNivel3(), n3.getEnlacenivel3(),n3.getEstatus(), nivel4s));
                                });
//                                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(nivel3s)" + nivel3s.size());
                            }
                            nivel2s.add(new Nivel2(n2.getTituloNivel2(), n2.getIconoNivel2(), n2.getEnlaceNivel2(),n2.getEstatus(), nivel3s));
                        });
//                        System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(nivel2s)" + nivel2s.size());
                    }
                    menus.add(new Menu("Administrador", nivel2s));
//                    menus.add(new Menu(n1.getTituloNivel1(), nivel2s));
//                });
//                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado.crearMenuAdministrador(menus)" + menus.size());
//            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
public static class listaEstrategiaActividades {

        @Getter        @Setter        private String modulo;
        @Getter        @Setter        private List<Permisosadminstracion> ps;

        public listaEstrategiaActividades(String modulo, List<Permisosadminstracion> ps) {
            this.modulo = modulo;
            this.ps = ps;
        }      
        
    }

    public static class Menu {

        @Getter        @Setter        private String titulo;
        @Getter        @Setter        private List<Nivel2> nivel2s;

        public Menu(String titulo, List<Nivel2> nivel2s) {
            this.titulo = titulo;
            this.nivel2s = nivel2s;
        }
    }

    public static class Nivel2 {

        @Getter        @Setter        private String titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;
        @Getter        @Setter        private List<Nivel3> nivel3s;

        public Nivel2(String titulo, String icono, String enlace, String estaus, List<Nivel3> nivel3s) {
            this.titulo = titulo;
            this.icono = icono;
            this.enlace = enlace;
            this.estaus = estaus;
            this.nivel3s = nivel3s;
        }
    }

    public static class Nivel3 {

        @Getter        @Setter        private String titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;
        @Getter        @Setter        private List<Nivel4> nivel4s;

        public Nivel3(String titulo, String icono, String enlace, String estaus, List<Nivel4> nivel4s) {
            this.titulo = titulo;
            this.icono = icono;
            this.enlace = enlace;
            this.estaus = estaus;
            this.nivel4s = nivel4s;
        }   
    }

    public static class Nivel4 {

        @Getter        @Setter        private String titulo;
        @Getter        @Setter        private String icono;
        @Getter        @Setter        private String enlace;
        @Getter        @Setter        private String estaus;

        public Nivel4(String titulo, String icono, String enlace, String estaus) {
            this.titulo = titulo;
            this.icono = icono;
            this.enlace = enlace;
            this.estaus = estaus;
        }   
    }

}

