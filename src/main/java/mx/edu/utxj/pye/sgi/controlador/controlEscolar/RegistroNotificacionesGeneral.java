/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import java.util.stream.Collectors;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoNotificacionesAreas;
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
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ViewScoped
public class RegistroNotificacionesGeneral extends ViewScopedRol implements Desarrollable {

    private static final long serialVersionUID = -3552096699095522780L;
    @Getter
    @Setter
    private RegistroNotificacionRolGeneral rol;
    @Getter
    Boolean tieneAcceso = false;

    @EJB
    EjbRegistroNotificaciones ejb;
    @EJB
    EjbValidacionRol ejbValidacionRol;
    @EJB
    EjbPropiedades ep;

    @Inject
    LogonMB logonMB;
    @Inject
    UtilidadesCH utilidadesCH;
    @Getter
    private Boolean cargado = false;
    @Getter
    private Boolean admin = false;
    @Getter
    protected List<String> instrucciones = new ArrayList<>();

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
                    admin = true;
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
            instrucciones.add("Prueba 1");
            instrucciones.add("Prueba 2");
            instrucciones.add("Prueba 3");
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

    public void cargaAreasParaAsignarNotificacion() {
        ResultadoEJB<List<DtoNotificacionesAreas>> resDtoAreasParaNotificacion = ejb.consultarListadoAreasParaNotificacion();
        if (resDtoAreasParaNotificacion.getCorrecto()) {
            rol.setListaDtoNotificacionesAreas(resDtoAreasParaNotificacion.getValor());
        } else {
            mostrarMensajeResultadoEJB(resDtoAreasParaNotificacion);
        }
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
    public void valifaFechaInicio(ValueChangeEvent event) {
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

    public void valifaFechaInicioFiltro(ValueChangeEvent event) {
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

    public void valifaFechaFinFiltro(ValueChangeEvent event) {
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
     * ********************************************* Filtros
     * ********************************************************
     */
    public void abrirModalConsultaEnlaces(NotificacionesCe notificacion) {
        rol.setNotificacionCe(notificacion);
        inicializarNotificacionCeEnlace();
        actualizarEnlacesNotificacion();
        Ajax.update("frmEnlacesNotificaciones");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalConsultaEnlacesNotificaciones').show();");
    }

    public void abrirModalConsultaImagenes(NotificacionesCe notificacion) {
        rol.setNotificacionCe(notificacion);
//        Considerar la actualización de la base de datos
        Ajax.update("frmImagenesNotificaciones");
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalConsultaImagenesNotificaciones').show();");
    }

    public void abrirModalConsultaAreasAsignadas() {
        Ajax.oncomplete("skin();");
        Ajax.oncomplete("PF('modalConsultaAreasNotificaciones').show();");
    }

    /**
     * ********************************************* Administración de datos
     * ********************************************************
     */
    public void guardarNotificacion() {
        if (rol.getAlcance().isEmpty() || rol.getAlcance() == null) {
            mostrarMensajeError("El alcance de la notificación debe contener al menos un dato");
        } else {
            List<String> listaAlcanceOrdenada = RolNotificacion.ListaValoresLabel();
            rol.getNotificacionCe().setHoraInicio(rol.getFechaInicio());
            rol.getNotificacionCe().setHoraFin(rol.getFechaFin());
            rol.getNotificacionCe().setGeneral(Boolean.FALSE);
            listaAlcanceOrdenada.retainAll(rol.getAlcance());
            rol.getNotificacionCe().setAlcance(String.join(",", listaAlcanceOrdenada));
            ResultadoEJB<NotificacionesCe> resNotificacion = ejb.guardaNotificacion(rol.getNotificacionCe());
            if (resNotificacion.getCorrecto()) {
                mostrarMensajeResultadoEJB(resNotificacion);
                inicializarGeneral();
                obtenerListaNotificacionesUltimosDiez();
            } else {
                mostrarMensajeResultadoEJB(resNotificacion);
            }
        }
    }

    public void actualizarNotificacion(NotificacionesCe notificacion) {
        ResultadoEJB<NotificacionesCe> resNotificacion = ejb.editarNotificacion(notificacion);
        if (resNotificacion.getCorrecto()) {
            inicializarNotificacionCe();
            inicializarListaNotificacionesCe();
            obtenerListaNotificacionesActivas(logonMB.getPersonal().getClave());
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
//                obtenerListaNotificacionesActivas(logonMB.getPersonal().getClave());
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

//    public void editaFechaInicioDuracion(ValueChangeEvent event){
//        
//    }
//    
//    public void editaNotificacion(){
//        
//    }
    public void guardarEnlaceNotificacion() {
        ResultadoEJB<NotificacionesEnlaces> resNotificacionEnlace = ejb.guardaNotificacionEnlace(rol.getNotificacionEnlace());
        if (resNotificacionEnlace.getCorrecto()) {
            inicializarNotificacionCeEnlace();
            inicializarListaNotificacionesEnlaces();
            actualizarEnlacesNotificacion();
            mostrarMensajeResultadoEJB(resNotificacionEnlace);
        } else {
            mostrarMensajeResultadoEJB(resNotificacionEnlace);
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
            if (horaActual.isBefore(horaTermino.plusDays(2)) == true) {
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
    public void obtenerListaNotificacionesUltimosDiez() {
        ResultadoEJB<List<NotificacionesCe>> resNotificaciones = ejb.consultarNotificacionesUltimosDiez();
        if (resNotificaciones.getCorrecto()) {
            rol.setListaNotificacionesCe(resNotificaciones.getValor());
            mostrarMensajeResultadoEJB(resNotificaciones);
        } else {
            inicializarListaNotificacionesCe();
        }
    }

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

    public void obtenerListaFechasNotificaciones() {
        ResultadoEJB<List<NotificacionesCe>> resNotificaciones = ejb.consultarFechasNotificaciones();
        if (resNotificaciones.getCorrecto()) {
            rol.setListaNotificacionesCe(resNotificaciones.getValor());
            mostrarMensajeResultadoEJB(resNotificaciones);
        } else {
            inicializarListaNotificacionesCe();
        }
    }

    public void obtenerListaNotificacionesPorFechaRegistro() {
        ResultadoEJB<List<NotificacionesCe>> resNotificaciones = ejb.consultaNotificacionesPorFechaRegistro(rol.getFechaInicioFiltro(), rol.getFechaFinFiltro());
        if (resNotificaciones.getCorrecto()) {
            rol.setListaNotificacionesCe(resNotificaciones.getValor());
            mostrarMensajeResultadoEJB(resNotificaciones);
        } else {
            inicializarListaNotificacionesCe();
        }
    }

    public void mostrarEnlacesPorNotificacion() {
        rol.setListaNotificacionesEnlaces(rol.getNotificacionCe().getNotificacionesEnlacesList());
    }

    public void actualizarEnlacesNotificacion() {
        ResultadoEJB<List<NotificacionesEnlaces>> resListaEnlaces = ejb.consultarEnlacesPorNotificacion(rol.getNotificacionCe());
        if (resListaEnlaces.getCorrecto()) {
            rol.setListaNotificacionesEnlaces(resListaEnlaces.getValor());
            if (!rol.getListaNotificacionesEnlaces().isEmpty()) {
                rol.setNotificacionCe(rol.getListaNotificacionesEnlaces().get(0).getNotificacion());
                rol.getListaNotificacionesCe().removeIf(nce -> nce.getNotificacion().equals(rol.getNotificacionCe().getNotificacion()));
                rol.getListaNotificacionesCe().stream().sorted((s1, s2) -> s2.getNotificacion().compareTo(s1.getNotificacion())).collect(Collectors.toList());
                rol.getListaNotificacionesCe().add(rol.getNotificacionCe());
            }
        } else {
            mostrarMensajeResultadoEJB(resListaEnlaces);
        }
    }

    public void mostrarImagenesPorNotificacion() {
        rol.setListaNotificacionesCeImagenes(rol.getNotificacionCe().getNotificacionesCeImagenesList());
    }

    public void metodoBase() {
    }
}
