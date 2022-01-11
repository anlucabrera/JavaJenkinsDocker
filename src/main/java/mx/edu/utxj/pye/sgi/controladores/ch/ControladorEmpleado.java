package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
import mx.edu.utxj.pye.generico.UtilidadesAcceso;
import mx.edu.utxj.pye.sgi.dto.ch.MenuDinamicoBD;
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosadminstracion;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
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
    @Getter    @Setter    private Calendarioevaluacionpoa calendarioevaluacionpoa=new Calendarioevaluacionpoa();

////////////////////////////////////////////////////////////////////////////////Listas complementarias
    @Getter    @Setter    private List<Procesopoa> procesopoas=new ArrayList<>();
    @Getter    @Setter    private List<Calendarioevaluacionpoa> calendarioevaluacionpoas=new ArrayList<>();
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();
    @Getter    @Setter    private List<Notificaciones> listaNotificaciones = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> incidenciases = new ArrayList<>();


    @Getter    @Setter    private Integer empleadoLogeado;
    @Getter    @Setter    private EjerciciosFiscales ef;
    @Getter    @Setter    private List<Integer> administradores = new ArrayList();
    @Getter    @Setter    private List<Boolean> poaProces = new ArrayList();
    @Getter    @Setter    private String clavePersonalLogeado, mandos = "", fechaCVBencimiento, fechaFuncionesBencimiento, fechaLimiteCurriculumVitae = "", fechaLimiteRegistroFunciones = "",
            mensajeIndex1 = "", mensajeIndex2 = "",mensajeIndexPoa = "";

    @Getter    @Setter    private Boolean fechaLimiteCV, fechaLimiteFunciones, tienePOA = false,mensajeVisiblePOA = false, estiloInfo = false, mensajeGeneral = false,evaluable=false;
    @Getter    @Setter    private List<Modulosregistro> nuevaListaModulosregistro = new ArrayList<>();

    @Getter    @Setter    private EventosAreas nuevaEventosAreas = new EventosAreas();
    
    @Getter    @Setter    private AreasUniversidad nuevaAreasUniversidad = new AreasUniversidad();
    @Getter    @Setter    private AreasUniversidad nuevaAreasUniversidadPOA = new AreasUniversidad();
    @Getter    @Setter    private AreasUniversidad nuevaAreasUniversidadLogeado = new AreasUniversidad();
    @Getter    @Setter    private List<MenuDinamico> moduloses = new ArrayList();
    
    @Getter    @Setter    private List<MenuDinamicoBD.EncabezadosMenu> menus = new ArrayList<>();
    List<MenuDinamicoBD.Nivel1> nivel1s = new ArrayList<>();
    List<MenuDinamicoBD.Nivel2> nivel2s = new ArrayList<>();
    List<MenuDinamicoBD.Nivel3> nivel3s = new ArrayList<>();
    List<MenuDinamicoBD.Nivel4> nivel4s = new ArrayList<>();
    List<MenuDinamicoBD.Nivel5> nivel5s = new ArrayList<>();
    
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
    
    @Inject    UtilidadesCH uch;
    @Inject    UtilidadesAcceso acceso;
    @Inject    UtilidadesPOA utilidadesPOA;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
   
    @PostConstruct
    public void init() {
        try {
            if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
                return;
            }
            cargado = true;
// Comentar la siguiente asignación cuando saiiut falle//
                if (logonMB.getPersonal().getStatus().equals('B')) {
                    nuevoOBJListaPersonal = null;
                    Messages.addGlobalFatal("El usuario para la clave " + empleadoLogeado + " no Existe o ha sido dado de baja, si cree que es un error favor de Comunicarse con el área de personal");
                    return;
                }
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
            administradores.add(714);
            mostrarPerfilLogeado();
            informacionComplementariaAEmpleadoLogeado();
            areaPoa();
            crearMenuAdministrador();
        } catch (NullPointerException npe) {
            Messages.addGlobalFatal("No se tiene acceso al Logon");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
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
            nuevaAreasUniversidadLogeado=ejbAreasLogeo.mostrarAreasUniversidad(nuevoOBJListaPersonal.getAreaOperativa());
            fechasModulos();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            procesopoas = new ArrayList<>();
            
            calendarioevaluacionpoas= new ArrayList<>();
            calendarioevaluacionpoas=ejbUtilidadesCH.mostrarCalendariosEvaluacionActivos(new Date());            
                
            ef=new EjerciciosFiscales();
            Integer i=LocalDate.now().getYear();
            ef=utilidadesPOA.obtenerAnioRegistroActivo(Short.parseShort(i.toString()));
            procesopoas = ejbUtilidadesCH.mostrarEtapaPOAPersona(nuevoOBJListaPersonal.getClave());
            if (procesopoas.isEmpty()) {
                if (administradores.contains(nuevoOBJListaPersonal.getClave())) {
                    procesopoa = ejbUtilidadesCH.mostrarEtapaPOAArea(nuevoOBJListaPersonal.getAreaOperativa());
                }else{
                    Procesopoa p = ejbUtilidadesCH.mostrarEtapaPOAArea(Short.parseShort("9"));
                    procesopoa=p;
                    procesopoa.setActivaEtapa1(Boolean.FALSE);
                    procesopoa.setActivaEtapa2(Boolean.FALSE);
                }
                procesopoas.add(procesopoa);
            }
            if (!procesopoas.isEmpty()) {
                procesopoa=procesopoas.get(0);
                if (Objects.equals(procesopoa.getActivaEtapa1(), Boolean.FALSE) && Objects.equals(procesopoa.getActivaEtapa2(), Boolean.FALSE)) {
                    tienePOA = false;
                    evaluable = false;
                } else {
                    nuevaAreasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(procesopoa.getArea());
                    nuevaAreasUniversidadPOA = ejbAreasLogeo.mostrarAreasUniversidad(procesopoa.getArea());
                    calendarioevaluacionpoa = new Calendarioevaluacionpoa();
                    calendarioevaluacionpoa = utilidadesPOA.buscarCalendarioPOA(procesopoa, 0,ef.getEjercicioFiscal());
                    if (Objects.equals(procesopoa.getActivaEtapa2(), Boolean.TRUE)) {
                        if (!"No hay mes activo".equals(calendarioevaluacionpoa.getMesEvaluacion())) {
                            evaluable = true;
                            eventosRegistro(calendarioevaluacionpoa);
                        }                                          
//                        if(nuevoOBJListaPersonal.getAreaOperativa()==9 || nuevoOBJListaPersonal.getAreaOperativa()==6 || nuevoOBJListaPersonal.getAreaOperativa()==7){
//                            procesopoa=new Procesopoa();  
//                            Calendarioevaluacionpoa periodoEvaluacion = new Calendarioevaluacionpoa();
//                            Short ejeFE=0;
//                            Integer anio=0;
//                            DateFormat df = new SimpleDateFormat("yy");
//                            if(fechaActual.getMonth()==Month.JANUARY){
//                                anio=Integer.parseInt(df.format(new Date()))-2;
//                            }else{
//                                anio=Integer.parseInt(df.format(new Date()))-1;
//                            }
//                            ejeFE=Short.parseShort(anio.toString());
//                            periodoEvaluacion=new Calendarioevaluacionpoa(13, new Date(), new Date(), "Diciembre", Boolean.FALSE);
//                            procesopoa=new Procesopoa(0, nuevoOBJListaPersonal.getAreaOperativa(),nuevoOBJListaPersonal.getClave(), Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, Boolean.TRUE, ejeFE, ejeFE, Boolean.TRUE, Boolean.TRUE, periodoEvaluacion);
//                        }
                    }
                    if (Objects.equals(procesopoa.getActivaEtapa1(), Boolean.TRUE)) {
                        tienePOA = true;
                        if(!procesopoa.getValidacionRegistroA() && utilidadesPOA.buscarCalendarioPOA("Registro",procesopoa)){poaProces.add(true);} else {poaProces.add(false);}
                        if(procesopoa.getValidacionRegistroA() && (procesopoa.getValidacionRFFinalizado() == false) && utilidadesPOA.buscarCalendarioPOA("Recurso",procesopoa)){poaProces.add(true);} else {poaProces.add(false);}
                    } else {
                        poaProces.add(false);
                        poaProces.add(false);
                    }
                }
            } else {
                tienePOA = false;
                evaluable = false;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eventosRegistro(Calendarioevaluacionpoa c) {
        try {
            fechaActual = LocalDate.now();                              
            mensajeGeneral = false;
            estiloInfo = false;
            Integer diasR = (int) ((c.getFechaFin().getTime() - uch.castearLDaD(fechaActual).getTime()) / 86400000);
            Integer diasI = (int) ((uch.castearLDaD(fechaActual).getTime() - c.getFechaInicio().getTime()) / 86400000);
            if (diasI <= 2) {
                mensajeIndex1 = "Inicio del periodo para la Evaluación de actividades, Carga de Evidencia, y Registro en Sistema del mes de " + c.getMesEvaluacion();
                estiloInfo = true;
                mensajeGeneral = true;
            }
            if (diasR <= 5) {
                mensajeIndex1 = "La fecha límite para la Evaluación de actividades, Carga de Evidencia, y Registro en Sistema del mes de " + c.getMesEvaluacion() + " ¡Está por vencer!";
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
        if(!procesopoa.getActivaEtapa1()){
            return Boolean.FALSE;
        }
        nuevaEventosAreas = ejbUtilidadesCH.mostrarEventoAreas(new EventosAreasPK(t.getEvento(), procesopoa.getArea()));
        LocalDateTime fechaf = uch.castearDaLDT(t.getFechaFin());
        LocalDateTime fechai = uch.castearDaLDT(t.getFechaInicio());
        if (nuevaEventosAreas != null) {
            fechaf = uch.castearDaLDT(t.getFechaFin()).plusDays(nuevaEventosAreas.getDiasExtra()).plusHours(23).plusMinutes(59).plusSeconds(59);
        } else {
            fechaf = uch.castearDaLDT(t.getFechaFin()).plusHours(23).plusMinutes(59).plusSeconds(59);
        }
        Integer minutos = (int) ((uch.castearLDTaD(fechaf).getTime() - uch.castearLDTaD(fechaActualHora).getTime()) / 60000);
        Integer diasR = minutos / 1440;
        Integer horasR = (minutos % 1440);
        Integer res = (minutos % 60);
        if ((fechaActualHora.isAfter(fechai) || fechaActualHora.equals(fechai)) && (fechaActualHora.isBefore(fechaf) || fechaActualHora.equals(fechaf))) {
            mensajeIndexPoa = "Resta " + diasR + " día(s) " + (horasR / 60) + " hora(s) y " + (res) + " minutos para que finalice el periodo para el Proceso de " + t.getNombre();
            mensajeVisiblePOA = Boolean.TRUE;
            return true;
        } else {
            return false;
        }
    }

//////////////////////////////////////////////////////////////////////////////// Menú dinamico
    public void crearMenuAdministrador() {
        try {
            List<MenuDinamico> dinamicos = new ArrayList<>();
            dinamicos = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 0, "", "Trabajador");
            menus = new ArrayList<>();
            if (!dinamicos.isEmpty()) {
                dinamicos.forEach((enc) -> {
                    List<MenuDinamico> dinamicosN1 = new ArrayList<>();
//                    dinamicosN1 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 1, "Administrador","Trabajador");
                    dinamicosN1 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 1, enc.getEncabezado(), "Trabajador");
                    nivel1s = new ArrayList<>();
                    if (!dinamicosN1.isEmpty()) {
                        dinamicosN1.forEach((n1) -> {
                            List<MenuDinamico> dinamicosN2 = new ArrayList<>();
                            dinamicosN2 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 2, n1.getTituloNivel1(), "Trabajador");
                            nivel2s = new ArrayList<>();
                            if (!dinamicosN2.isEmpty()) {
                                dinamicosN2.forEach((n2) -> {
                                    List<MenuDinamico> dinamicosN3 = new ArrayList<>();
                                    dinamicosN3 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 3, n2.getTituloNivel2(), "Trabajador");
                                    nivel3s = new ArrayList<>();
                                    if (!dinamicosN3.isEmpty()) {
                                        dinamicosN3.forEach((n3) -> {
                                            List<MenuDinamico> dinamicosN4 = new ArrayList<>();
                                            dinamicosN4 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 4, n3.getTitulonivel3(), "Trabajador");
                                            nivel4s = new ArrayList<>();
                                            if (!dinamicosN4.isEmpty()) {
                                                dinamicosN4.forEach((n4) -> {
                                                    List<MenuDinamico> dinamicosN5 = new ArrayList<>();
                                                    dinamicosN5 = ejbUtilidadesCH.mostrarListaMenu(nuevoOBJListaPersonal, 5, n4.getTitulonivel4(), "Trabajador");
                                                    nivel5s = new ArrayList<>();
                                                    if (!dinamicosN5.isEmpty()) {
                                                        dinamicosN5.forEach((n5) -> {
                                                            MenuDinamicoBD.Nivel5 nivel5 = new MenuDinamicoBD.Nivel5();
                                                            nivel5.setTitulo(saltoLinea(n5.getTitulonivel5()));
                                                            nivel5.setIcono(n5.getIconoNivel5());
                                                            if (acceso.getProcesoElectoralActivo()) {
                                                                nivel5.setEnlace(n5.getEnlaceVedaElectoral());
                                                            } else {
                                                                nivel5.setEnlace(n5.getEnlace());
                                                            }
                                                            nivel5.setEstaus(n5.getEstatus());
                                                            nivel5.setTipoenlace(n5.getTipoenlace());
                                                            nivel5s.add(nivel5);
                                                        });
                                                    }
                                                    MenuDinamicoBD.Nivel4 nivel4 = new MenuDinamicoBD.Nivel4();
                                                    nivel4.setTitulo(saltoLinea(n4.getTitulonivel4()));
                                                    nivel4.setIcono(n4.getIconoNivel4());
                                                    if (acceso.getProcesoElectoralActivo()) {
                                                        nivel4.setEnlace(n4.getEnlaceVedaElectoral());
                                                    } else {
                                                        nivel4.setEnlace(n4.getEnlace());
                                                    }
                                                    nivel4.setEstaus(n4.getEstatus());
                                                    nivel4.setTipoenlace(n4.getTipoenlace());
                                                    nivel4.setQuintoNivel(nivel5s);
                                                    nivel4s.add(nivel4);
                                                });
                                            }
                                            MenuDinamicoBD.Nivel3 nivel3 = new MenuDinamicoBD.Nivel3();
                                            nivel3.setTitulo(saltoLinea(n3.getTitulonivel3()));
                                            nivel3.setIcono(n3.getIconoNivel3());
                                            if (acceso.getProcesoElectoralActivo()) {
                                                nivel3.setEnlace(n3.getEnlaceVedaElectoral());
                                            } else {
                                                nivel3.setEnlace(n3.getEnlace());
                                            }
                                            nivel3.setEstaus(n3.getEstatus());
                                            nivel3.setTipoenlace(n3.getTipoenlace());
                                            nivel3.setCuartoNivel(nivel4s);
                                            nivel3s.add(nivel3);
                                        });
                                    }
                                    MenuDinamicoBD.Nivel2 nivel2 = new MenuDinamicoBD.Nivel2();
                                    nivel2.setTitulo(saltoLinea(n2.getTituloNivel2()));
                                    nivel2.setIcono(n2.getIconoNivel2());
                                    if (acceso.getProcesoElectoralActivo()) {
                                        nivel2.setEnlace(n2.getEnlaceVedaElectoral());
                                    } else {
                                        nivel2.setEnlace(n2.getEnlace());
                                    }
                                    nivel2.setEstaus(n2.getEstatus());
                                    nivel2.setTipoenlace(n2.getTipoenlace());
                                    nivel2.setTercerNivel(nivel3s);
                                    nivel2s.add(nivel2);
                                });
                            }
                            MenuDinamicoBD.Nivel1 nivel1 = new MenuDinamicoBD.Nivel1();
                            nivel1.setTitulo(saltoLinea(n1.getTituloNivel1()));
                            nivel1.setIcono(n1.getIconoNivel1());
                            if (acceso.getProcesoElectoralActivo()) {
                                nivel1.setEnlace(n1.getEnlaceVedaElectoral());
                            } else {
                                nivel1.setEnlace(n1.getEnlace());
                            }
                            nivel1.setEstaus(n1.getEstatus());
                            nivel1.setTipoenlace(n1.getTipoenlace());
                            nivel1.setSegundoNivel(nivel2s);
                            nivel1s.add(nivel1);
                        });
                    }
                    MenuDinamicoBD.EncabezadosMenu nivel0 = new MenuDinamicoBD.EncabezadosMenu();
                    nivel0.setEncabezado(saltoLinea(enc.getEncabezado()));
//                    nivel0.setEncabezado("Administrador");
                    nivel0.setPrimerNivel(nivel1s);
                    menus.add(nivel0);
                });
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    public List<String> saltoLinea(String titulo) {
        String[] parts = titulo.split(" ");
        List<String> textoSeparado = Arrays.asList(parts);
        List<String> tituloMostrado = new ArrayList<>();
        String texto = "";
        for (int i = 0; i < textoSeparado.size(); i++) {
            String letra = textoSeparado.get(i);
            Integer tmt = texto.length() + letra.length();
            if (tmt > 20) {
                tituloMostrado.add(texto);
                texto = "";
                texto = texto + letra;
            } else {
                texto = texto + " " + letra;
            }
        }
        tituloMostrado.add(texto);
        return tituloMostrado;
    }
}
