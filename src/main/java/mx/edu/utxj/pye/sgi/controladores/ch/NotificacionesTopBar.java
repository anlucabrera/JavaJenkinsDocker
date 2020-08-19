package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class NotificacionesTopBar implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

////////////////////////////////////////////////////////////////////////////////Listas complementarias
    @Getter    @Setter    private List<Notificaciones> listaNotificaciones = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> incidenciases = new ArrayList<>();
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal = new ListaPersonal();
    @Getter    @Setter    private Procesopoa procesopoa = new Procesopoa();
    @Getter    @Setter    private Boolean tienePOA, incidenciasPendientes = false, procesoPOARa = false, procesoPOAAr = false, procesoPOAJu = false, procesoPOAVra = false, procesoPOAVar = false, procesoPOAVjus = false;
    @Getter    @Setter    private Integer incrementoTotalPasado = 0,mensajesTotalPasado=0, incrementoTotal = 0, incrementoRA = 0, incrementoAR = 0, incrementoJU = 0;
    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now();
    @Getter    @Setter    private AreasUniversidad nuevaAreasUniversidad = new AreasUniversidad();
    @Getter    @Setter    private List<Modulosregistro> nuevaListaModulosregistro = new ArrayList<>();

    @EJB
    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB
    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB
    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo ejbAreasLogeo;

    @Inject
    ControladorEmpleado controladorEmpleado;
    @Inject
    UtilidadesCH uch;

    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        if (controladorEmpleado.getNuevoOBJListaPersonal() == null) {
            Messages.addGlobalFatal("El usuario para no Existe o ha sido dado de baja, si cree que es un error favor de Comunicarse con el área de personal");
            return;
        }
        nuevoOBJListaPersonal = controladorEmpleado.getNuevoOBJListaPersonal();
        buscarMensajesPendientes();
        listaNotifiaciones();
    }

    public void listaNotifiaciones() {
        if (nuevoOBJListaPersonal.getActividad() == 2 || nuevoOBJListaPersonal.getActividad() == 4) {
            buscarIncidenciasPendientes();
            buscarProcesoPOAArea();
        }
        if (nuevoOBJListaPersonal.getAreaOperativa() == 6 || nuevoOBJListaPersonal.getAreaOperativa() == 7 || nuevoOBJListaPersonal.getClave() == 148) {
            buscarProcesosPOAAdministrador();
        }
        total();
    }

    public void buscarMensajesPendientes() {
        try {
            listaNotificaciones.clear();
            listaNotificaciones = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuarios(nuevoOBJListaPersonal.getClave(), 0);

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesTopBar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscarIncidenciasPendientes() {
        try {
            incidenciases.clear();

            LocalDate fechaI = LocalDate.now();
            LocalDate fechaF = LocalDate.now();

            fechaI = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01);
            fechaF = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01).lengthOfMonth());

            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReportePendientes(uch.castearLDaD(fechaI), uch.castearLDaD(fechaF), nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getClave());
            if (!incidenciases.isEmpty()) {
                incidenciasPendientes = true;
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesTopBar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscarProcesoPOAArea() {
        try {
            procesopoa = new Procesopoa();
            nuevaAreasUniversidad = ejbAreasLogeo.mostrarAreasUniversidad(nuevoOBJListaPersonal.getAreaOperativa());
            if (nuevaAreasUniversidad != null) {
                if (nuevaAreasUniversidad.getTienePoa()) {
                    if (Objects.equals(nuevaAreasUniversidad.getResponsable(), nuevoOBJListaPersonal.getClave())) {
                        tienePOA = true;
                        procesopoa = ejbUtilidadesCH.mostrarEtapaPOAArea(nuevoOBJListaPersonal.getAreaOperativa());
                    } else {
                        if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 284 ||
                                controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 613 ||
                                controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564) {
                            procesopoa = ejbUtilidadesCH.mostrarEtapaPOAArea(nuevoOBJListaPersonal.getAreaOperativa());
                        }
                        tienePOA = false;
                    }
                }
            }
            if (tienePOA) {
                procesoPOARa = !procesopoa.getRegistroAFinalizado();
                procesoPOAAr = (procesopoa.getValidacionRegistroA() && !procesopoa.getAsiganacionRFinalizado());
                procesoPOAJu = (procesopoa.getValidacionRFFinalizado() && !procesopoa.getRegistroJustificacionFinalizado());
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesTopBar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void buscarProcesosPOAAdministrador() {
        try {
            if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 148
                    || controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 284
                    || controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564
                    || controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 613
                    || controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 619) {
                incrementoRA = 0;
                incrementoAR = 0;
                incrementoJU = 0;
                List<Procesopoa> ps = new ArrayList<>();
                ps.clear();
                ps = ejbUtilidadesCH.mostrarProcesopoa();
                if (!ps.isEmpty()) {
                    ps.forEach((t) -> {
                        if (t.getRegistroAFinalizado() == true && t.getValidacionRegistroA() == false) {
                            incrementoRA = incrementoRA + 1;
                        }
                        if (t.getAsiganacionRFinalizado() == true && t.getValidacionRFFinalizado() == false) {
                            incrementoAR = incrementoAR + 1;
                        }
                        if (t.getRegistroJustificacionFinalizado() == true && t.getValidacionJustificacion() == false) {
                            incrementoJU = incrementoJU + 1;
                        }
                    });
                }
                if (incrementoRA != 0) {
                    procesoPOAVra = true;
                }
                if (incrementoAR != 0) {
                    procesoPOAVar = true;
                }
                if (incrementoJU != 0) {
                    procesoPOAVjus = true;
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesTopBar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void total() {
        try {
            incrementoTotal = 0;
            if (incidenciasPendientes) {
                incrementoTotal = incrementoTotal + 1;
            }
            if (procesoPOARa || procesoPOAAr || procesoPOAJu) {
                incrementoTotal = incrementoTotal + 1;
            }
            if (procesoPOAVra || procesoPOAVar || procesoPOAVjus) {
                incrementoTotal = incrementoTotal + 1;
            }

            if (!Objects.equals(incrementoTotalPasado, incrementoTotal)) {
                String mensaje = "";
                FacesContext context = FacesContext.getCurrentInstance();
                mensaje = "Tiene " + (incrementoTotal - incrementoTotalPasado) + "notificaciones por atender, ";
                if (mensajesTotalPasado != listaNotificaciones.size()) {
                    mensaje = mensaje + "y " + (listaNotificaciones.size() - mensajesTotalPasado) + " nuevos mensajes sin leer";
                }
                context.addMessage(null, new FacesMessage("Notificaciones por atender", mensaje));
                
            }
            incrementoTotalPasado = incrementoTotal;
            mensajesTotalPasado = listaNotificaciones.size();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(NotificacionesTopBar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
