package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorSubordinados implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonal = new ArrayList<>(), nuevaListaListaPersonalJefes = new ArrayList<>();
    @Getter    @Setter    private List<Funciones> listaFuncioneSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigaciones = new ArrayList<>();
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>(), nuevaListaFuncionesGenerales = new ArrayList<>(), estatus = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();

    @Getter    @Setter    private Date fechaActual = new Date();
    @Getter    @Setter    private Integer empleadoLogeado, contactoDestino;
    @Getter    @Setter    private String mensajeDNotificacion = "";

    @Getter    @Setter    private Funciones nuevoOBJFunciones;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaPersonal, nuevoOBJListaPersonalFiltro, nuevoOBJListaPersonalLogeado;
    @Getter    @Setter    private Personal nuevoOBJPersonal;

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbDatosUsuarioLogeado ejbDatosUsuarioLogeado;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorSubordinados Inicio: " + System.currentTimeMillis());
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        estatus.add("Pendiente");

        empleadoLogeado = controladorEmpleado.getEmpleadoLogeado();
        nuevaListaFuncionesEspecificas.clear();
        nuevaListaFuncionesGenerales.clear();
        nuevoOBJFunciones = new Funciones();
        mostrarContactosParaNotificacion();
        System.out.println("ControladorSubordinados Fin: " + System.currentTimeMillis());
    }

    public void mostrarPerfilSubordinado() {
        try {
            nuevoOBJPersonal = new Personal();

            nuevoOBJInformacionAdicionalPersonal = ejbDatosUsuarioLogeado.mostrarInformacionAdicionalPersonalLogeado(contactoDestino);
            nuevoOBJListaPersonal = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(contactoDestino);
            nuevoOBJPersonal = ejbDatosUsuarioLogeado.mostrarPersonalLogeado(contactoDestino);
            informacionCV();
            mostrarFuncioneSubordinado();
            mostrarIncidencias();
            
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
            listaInvestigaciones.clear();
            listaDocencias.clear();

            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(contactoDestino);
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(contactoDestino);
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(contactoDestino);
            listaIdiomas = ejbHabilidades.mostrarIdiomas(contactoDestino);
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(contactoDestino);
            listaInvestigaciones = ejbProduccionProfecional.mostrarInvestigacion(contactoDestino);
            listaDocencias = ejbDatosUsuarioLogeado.mostrarListaDocencias(contactoDestino);
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
            nuevoOBJListaPersonalLogeado = ejbDatosUsuarioLogeado.mostrarVistaListaPersonalLogeado(empleadoLogeado);

            nuevaListaListaPersonal = ejbDatosUsuarioLogeado.mostrarListaSubordinados(nuevoOBJListaPersonalLogeado);
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

    public void mostrarIncidencias() {
        try {
            ejbNotificacionesIncidencias.mostrarIncidencias(contactoDestino).forEach((t) -> {
                if (t.getEstatus().equals("Pendiente")) {
                    listaIncidencias.add(t);
                }
            });
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ejbNotificacionesIncidencias.actualizarIncidencias((Incidencias) event.getObject());
            Messages.addGlobalInfo("¡Operación exitosa!!");
            informacionCV();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("¡Operación cancelada!!");
    }
}
