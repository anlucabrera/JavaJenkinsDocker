package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.RegistroNotificacionRolGeneral;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbRegistroNotificaciones;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbValidacionRol;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCe;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesCeImagenes;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesEnlaces;
import mx.edu.utxj.pye.sgi.enums.ControlEscolarVistaControlador;
import mx.edu.utxj.pye.sgi.enums.NotificacionesTipo;
import mx.edu.utxj.pye.sgi.enums.RolNotificacion;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.funcional.Desarrollable;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

@Named
@ViewScoped
public class RegistroNotificacionesGeneral extends ViewScopedRol implements Desarrollable {

    private static final long serialVersionUID = -3552096699095522780L;
    @Getter @Setter private NotificacionesCe  notificacionCe = new NotificacionesCe();
    @Getter @Setter private RegistroNotificacionRolGeneral  rol;
    @Getter @Setter private Integer    horasRestantes = 23;
    @Getter protected   List<String>    instrucciones = new ArrayList<>();
    @Getter private Boolean cargado = false;
    @Getter private Boolean puedeAgregar = false;
    @Getter private Boolean puedeEliminar = false;
    @Getter Boolean tieneAcceso = false;

    @EJB    EjbRegistroNotificaciones ejb;
    @EJB    EjbValidacionRol ejbValidacionRol;
    @EJB    EjbPropiedades ep;

    @Inject LogonMB logonMB;
    @Inject UtilidadesCH utilidadesCH;

    @Override
    public Boolean mostrarEnDesarrollo(HttpServletRequest request) {
        String valor = "registro de notificaciones";
        Map<Integer, String> map = ep.leerPropiedadMapa(getClave(), valor);
        return mostrar(request, map.containsValue(valor));
    }

    @PostConstruct
    public void init() {
        try {

            if (logonMB.getUsuarioTipo().equals(UsuarioTipo.ESTUDIANTE)) {
                setVistaControlador(ControlEscolarVistaControlador.REGISTRO_NOTIFICACIONES);
                ResultadoEJB<Estudiante> notifAcceso = ejbValidacionRol.validarEstudiante(logonMB.getCurrentUser());

                rol = new RegistroNotificacionRolGeneral(notifAcceso.getValor());
                obtenerListaNotificacionesAlumnos();
            }

            ResultadoEJB<Filter<PersonalActivo>> regAcceso = ejbValidacionRol.validarTrabajador(logonMB.getPersonal().getClave());
            if (regAcceso.getCorrecto()) {
                tieneAcceso = true;
                cargado = true;
                if (logonMB.getPersonal().getClave() == 639) {
                    puedeAgregar = true;
                    puedeEliminar = true;
                }else if(logonMB.getPersonal().getClave()==714){
                    puedeAgregar = true;
                    puedeEliminar = false;
                    
                }
            }

            setVistaControlador(ControlEscolarVistaControlador.REGISTRO_NOTIFICACIONES);

            ResultadoEJB<Filter<PersonalActivo>> notifAcceso = ejbValidacionRol.validarPersonalActivo(logonMB.getPersonal().getClave());
            if (!notifAcceso.getCorrecto()) {
                mostrarMensajeResultadoEJB(notifAcceso);
                return;
            }

            rol = new RegistroNotificacionRolGeneral(notifAcceso.getValor());
//            obtenerListaNotificacionesActivas(logonMB.getPersonal().getClave());
            obtenerListaNotificacionesTotal();
            instrucciones.add("Si el evento dura todo el dia, marcar como duracion 00:00");
            instrucciones.add("Si el evento dura 1 solo dia, puedes omitir la fecha de fin");
            instrucciones.add("Si falta algun dato por llenar no se podra registrar la notificacion");
            obtenerListaNotificacionesTrabajador();

        } catch (Exception e) {
            mostrarExcepcion(e);
        }
    }

    /**
     * ********************************************* Inicializadores
     * ********************************************************
     */
    public void actualizar() {
        repetirUltimoMensaje();
    }

    public List<NotificacionesTipo> getListaNotificacionesTipos() {
        return NotificacionesTipo.ListaGenericos();
    }

    public List<RolNotificacion> getListaNotificacionRol() {
        return RolNotificacion.ListaNotificacion();
    }

    public void inicializarNotificacionCe() {
        rol.setNotificacionCe(new NotificacionesCe());
        rol.setFechaInicio(new Date());
        rol.setFechaFin(new Date());
        rol.getNotificacionCe().setPersonaRegistro(rol.getPersonal().getPersonal().getClave());
        if (rol.getAlcance() != null) {
            rol.getAlcance().clear();
        }
    }

    public void inicializarNotificacionCeEnlace() {
        rol.setNotificacionEnlace(new NotificacionesEnlaces());
        if (rol.getNotificacionCe().getNotificacion() != null) {
            rol.getNotificacionEnlace().setNotificacion(rol.getNotificacionCe());
        }
    }

    public void inicializarNotificacionCeImagen() {
        rol.setNotificacionCeImagen(new NotificacionesCeImagenes());
        if (rol.getNotificacionCe().getNotificacion() != null) {
            rol.getNotificacionCeImagen().setNotificacion(rol.getNotificacionCe());
        }
    }

    public void inicializarListaNotificacionesCe() {
        rol.setListaNotificacionesCe(new ArrayList<>());
        rol.setListaNotificacionesCe(Collections.EMPTY_LIST);
    }

    public void inicializarListaNotificacionesEnlaces() {
        rol.setListaNotificacionesEnlaces(new ArrayList<>());
        rol.setListaNotificacionesEnlaces(Collections.EMPTY_LIST);
    }

    public void inicializarListaNotificacionesImagenes() {
        rol.setListaNotificacionesCeImagenes(new ArrayList<>());
        rol.setListaNotificacionesCeImagenes(Collections.EMPTY_LIST);
    }

    public void inicializarGeneral() {
//        inicializarNotificacionCe();
        inicializarNotificacionCeEnlace();
        inicializarNotificacionCeImagen();

        inicializarListaNotificacionesCe();
        inicializarListaNotificacionesEnlaces();
        inicializarListaNotificacionesImagenes();
    }

    /**
     * *********************************************** Validaciones
     * *************************************************
     */
    public void validaFechaInicio(ValueChangeEvent event) {
        System.out.println("Fecha de inicio" + (Date) event.getNewValue());
        if ((Date) event.getNewValue() != null && rol.getFechaFin() != null || rol.getFechaFin() != null) {

            rol.setFechaInicio((Date) event.getNewValue());
            if (rol.getFechaInicio().before(rol.getFechaFin())) {
            } else {
                if (rol.getFechaFin().before(rol.getFechaInicio())) {
                    rol.setFechaFin(null);
                    rol.setFechaFin(rol.getFechaInicio());
                } else {
                }
            }
        }
    }

    public void validaFechaFin(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && rol.getFechaInicio() != null || rol.getFechaInicio() != null) {
            rol.setFechaFin((Date) event.getNewValue());
            System.out.println("Fecha de fin == " + rol.getFechaFin());
            if (rol.getFechaFin().after(rol.getFechaInicio())) {
            } else {
                if (rol.getFechaInicio().after(rol.getFechaFin())) {
                    rol.setFechaInicio(null);
                    rol.setFechaInicio(rol.getFechaFin());
                } else {
                }
            }
        }
    }

    public void verDuracion(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && rol.getHoraDuracion() != null || rol.getHoraDuracion() != null) {
            rol.setHoraDuracion((Date) event.getNewValue());
            System.out.println("Duracion del evento == " + rol.getHoraDuracion());
        } else {
            rol.setHoraDuracion(null);
        }

    }

    public void verHoraInicio(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && rol.getHoraInicio() != null || rol.getHoraInicio() != null) {
            rol.setHoraInicio((Date) event.getNewValue());
            System.out.println("Hora de inicio del evento == " + rol.getHoraInicio());
        } else {
            rol.setHoraInicio(null);
        }
    }
    
        public void obtenerHorasRestantes(ValueChangeEvent event){
            if((Date) event.getNewValue() != null){
                setHorasRestantes(24-obtenerHora((Date) event.getNewValue()));
            }
    }

    public void validaFechaInicioFiltro(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && rol.getFechaFinFiltro() != null || rol.getFechaFinFiltro() != null) {
            rol.setFechaInicioFiltro((Date) event.getNewValue());
            if (rol.getFechaInicioFiltro().before(rol.getFechaFinFiltro())) {
            } else {
                if (rol.getFechaFinFiltro().before(rol.getFechaInicioFiltro())) {
                    rol.setFechaFinFiltro(null);
                    rol.setFechaFinFiltro(rol.getFechaInicioFiltro());
                } else {
                }
            }
        }
    }

    public void validaFechaFinFiltro(ValueChangeEvent event) {
        if ((Date) event.getNewValue() != null && rol.getFechaInicioFiltro() != null || rol.getFechaInicioFiltro() != null) {
            rol.setFechaFinFiltro((Date) event.getNewValue());
            if (rol.getFechaFinFiltro().after(rol.getFechaInicioFiltro())) {
            } else {
                if (rol.getFechaInicioFiltro().after(rol.getFechaFinFiltro())) {
                    rol.setFechaInicioFiltro(null);
                    rol.setFechaInicioFiltro(rol.getFechaFinFiltro());
                } else {
                }
            }
        }
    }



    /**
     * ********************************************* Administración de datos
     * ********************************************************
     */
    
    public void guardarNotificacion() {
        try {
            Date fechaInicio, fechaFin,duracionEvento;
            LocalDateTime inicio, fin;
            Long duracionHoras, duracionMinutos, tiempoInicioHoras, tiempoInicioMinutos, tiempoFinHoras, tiempoFinMinutos;
            inicio = utilidadesCH.castearDaLDT(rol.getFechaInicio());
            fin = utilidadesCH.castearDaLDT(rol.getFechaFin());

            duracionHoras = Long.parseLong(obtenerHora(rol.getHoraDuracion()).toString());
            tiempoInicioHoras = Long.parseLong(obtenerHora(rol.getHoraInicio()).toString());
            tiempoFinHoras = tiempoInicioHoras + duracionHoras;

            duracionMinutos = Long.parseLong(obtenerMinuto(rol.getHoraDuracion()).toString());
            tiempoInicioMinutos = Long.parseLong(obtenerMinuto(rol.getHoraInicio()).toString());
            tiempoFinMinutos = tiempoInicioMinutos + duracionMinutos;
            
                fechaInicio = utilidadesCH.castearLDTaD(inicio.plusHours(tiempoInicioHoras).plusMinutes(tiempoInicioMinutos));
            if (rol.getFechaFin() != null) {
                System.out.println(rol.getFechaFin());
                fechaFin = utilidadesCH.castearLDTaD(fin.plusHours(tiempoFinHoras).plusMinutes(tiempoFinMinutos));
            } else {
                System.out.println(rol.getFechaFin());
                fechaFin = utilidadesCH.castearLDTaD(inicio.plusHours(tiempoFinHoras).plusMinutes(tiempoFinMinutos));
            }
            duracionEvento = rol.getHoraDuracion();
            notificacionCe.setHoraInicio(fechaInicio);
            notificacionCe.setHoraFin(fechaFin);
            notificacionCe.setDuracionEvento(duracionEvento);
            notificacionCe.setPersonaRegistro(logonMB.getPersonal().getClave());
            System.out.println(notificacionCe);

            ResultadoEJB<NotificacionesCe> resNotificacion = ejb.guardaNotificacion(notificacionCe);
            if (resNotificacion.getCorrecto()) {
                inicializarNotificacionCe();
                inicializarListaNotificacionesCe();
                obtenerListaNotificacionesTotal();
                mostrarMensajeResultadoEJB(resNotificacion);
            } else {
                mostrarMensajeResultadoEJB(resNotificacion);
            }
        } catch (Exception e) {
            System.out.println("mx.edu.utxj.pye.sgi.controlador.controlEscolar.RegistroNotificacionesGeneral.guardarNotificacion() == " + e);
        }
    }

    public void actualizarNotificacion(NotificacionesCe notificacion) {
        ResultadoEJB<NotificacionesCe> resNotificacion = ejb.editarNotificacion(notificacion);
        if (resNotificacion.getCorrecto()) {
            inicializarNotificacionCe();
            inicializarListaNotificacionesCe();
            obtenerListaNotificacionesTotal();
            mostrarMensajeResultadoEJB(resNotificacion);
        } else {
            mostrarMensajeResultadoEJB(resNotificacion);
        }
    }

    public void eliminarNotificacion(NotificacionesCe notificacion) {
        try {
            ResultadoEJB<Boolean> resNotificacion = ejb.eliminarNotificacion(notificacion);
            if (resNotificacion.getCorrecto()) {
                inicializarNotificacionCe();
                inicializarListaNotificacionesCe();
                obtenerListaNotificacionesTotal();
                mostrarMensajeResultadoEJB(resNotificacion);
            } else {
                mostrarMensajeResultadoEJB(resNotificacion);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(RegistroNotificacionesGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * ********************************************* Manejo de datos
     * ********************************************************
     */
    public int obtenerPorcentajeFechas(Date horaInicio) {
        LocalDateTime fechaI = utilidadesCH.castearDaLDT(horaInicio);
        Date fechaInicio = utilidadesCH.castearLDTaD(fechaI.minusDays(7));
        long now = System.currentTimeMillis();
        long s = fechaInicio.getTime();
        long e = horaInicio.getTime();
        if (s >= e || now >= e) {
            return 0;
        }
        if (now <= s) {
            return 100;
        }
        return (int) ((e - now) * 100 / (e - s));
    }

    public int obtenerDiferenciaDias(Date horaInicio) {
        LocalDateTime fechaI = utilidadesCH.castearDaLDT(horaInicio);
        Date fechaFin = utilidadesCH.castearLDTaD(fechaI.with(LocalTime.MIN).plusDays(1));
        long fechaInicio = System.currentTimeMillis();
        return (int) ((fechaFin.getTime() - fechaInicio) / (1000 * 60 * 60 * 24));
    }

    public boolean obtenerInicioEvento(Date horaInicio) {
        LocalDateTime horaActual = LocalDateTime.now();
        LocalDateTime horaComienzo = utilidadesCH.castearDaLDT(horaInicio);
        boolean empezo = horaActual.isAfter(horaComienzo);
        return empezo;
    }

    public boolean obtenerFinEvento(Date horaFin) {
        LocalDateTime horaActual = LocalDateTime.now();
        LocalDateTime horaTermino = utilidadesCH.castearDaLDT(horaFin);
        boolean termino = horaActual.isAfter(horaTermino);
        return termino;
    }

    public boolean tieneVariosDias(Date horaInicio, Date horaFin) {
        Date fechaInicio, fechaFin;
        fechaInicio = utilidadesCH.castearLDTaD(utilidadesCH.castearDaLDT(horaInicio).with(LocalTime.MIDNIGHT));
        fechaFin = utilidadesCH.castearLDTaD(utilidadesCH.castearDaLDT(horaFin).with(LocalTime.MIDNIGHT));
        if (fechaInicio.equals(fechaFin)) {
            return false;
        } else {
            return true;
        }
    }
    
    public boolean obtenerMes(Date horaInicio, Date horaFin) {
        LocalDate fechaI = utilidadesCH.castearDaLD(horaInicio);
        LocalDate fechaF = utilidadesCH.castearDaLD(horaFin);
        Month mesInicio = fechaI.getMonth();
        Month mesFin = fechaF.getMonth();
        if (mesInicio == mesFin) {
            return true;
        } else {
            return false;
        }
    }

    public Integer obtenerHora(Date fecha) {
        int hora;
        LocalDateTime horasFecha = utilidadesCH.castearDaLDT(fecha);
        hora = horasFecha.getHour();
        return hora;
    }

    public Integer obtenerMinuto(Date fecha) {
        int minuto;
        LocalDateTime horasFecha = utilidadesCH.castearDaLDT(fecha);
        minuto = horasFecha.getMinute();
        return minuto;
    }
    
    public boolean mostrarNotificacion(int index, Date horaInicio, Date horaFin) {
        LocalDateTime horaActual = LocalDateTime.now();
        LocalDateTime horaComienzo = utilidadesCH.castearDaLDT(horaInicio);
        LocalDateTime horaTermino = utilidadesCH.castearDaLDT(horaFin);
        if (horaActual.isAfter(horaComienzo.minusDays(7)) == true) {
            if (horaActual.isBefore(horaTermino.plusDays(1)) == true) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * ********************************************* Llenado de listas
     * ********************************************************
     */

    public void obtenerListaNotificacionesTotal() {
        ResultadoEJB<List<NotificacionesCe>> resNotificaciones = ejb.consultarNotificacionesTotal();
        if (resNotificaciones.getCorrecto()) {
            rol.setListaNotificacionesCe(resNotificaciones.getValor());
            rol.setListaNotificacionesCeRegistradas(resNotificaciones.getValor());
            mostrarMensajeResultadoEJB(resNotificaciones);
        } else {
            inicializarListaNotificacionesCe();
        }
    }

    public void obtenerListaNotificacionesActivas(int clave) {
        ResultadoEJB<List<NotificacionesCe>> resNotificaciones = ejb.consultarNotificacionesActivas(clave);
        if (resNotificaciones.getCorrecto()) {
            rol.setListaNotificacionesCeRegistradas(resNotificaciones.getValor());
            mostrarMensajeResultadoEJB(resNotificaciones);
        } else {
            inicializarListaNotificacionesCe();
        }
    }

    public void obtenerListaNotificacionesAlumnos() {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaI = fechaActual.minusMonths(1);
        LocalDate fechaF = fechaActual.plusMonths(1);
        ResultadoEJB<List<NotificacionesCe>> resNotificaciones = ejb.consultarNotificacionesAlumnos(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
        if (resNotificaciones.getCorrecto()) {
            rol.setListaNotificacionesCe(resNotificaciones.getValor());
            mostrarMensajeResultadoEJB(resNotificaciones);
        } else {
            inicializarListaNotificacionesCe();
        }
    }

    public void obtenerListaNotificacionesTrabajador() {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaI = fechaActual.minusMonths(1);
        LocalDate fechaF = fechaActual.plusMonths(1);
        ResultadoEJB<List<NotificacionesCe>> resNotificaciones = ejb.consultarNotificacionesTrabajador(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
        if (resNotificaciones.getCorrecto()) {
            rol.setListaNotificacionesCe(resNotificaciones.getValor());
            mostrarMensajeResultadoEJB(resNotificaciones);
        } else {
            inicializarListaNotificacionesCe();
        }
    }

    public void metodoBase() {
    }
}
