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

    @Getter    @Setter    private List<InformacionAdicionalPersonal> nuevaListaInformacionAdicionalPersonalSubordinado = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonaSubordinado = new ArrayList<>(), nuevaListaListaPersonalLogeado = new ArrayList<>(), nuevaListaListaPersonal = new ArrayList<>(), nuevaListaListaPersonalJefes = new ArrayList<>();
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

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUpdate ejbUpdate;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorSubordinados Inicio: " + System.currentTimeMillis());
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        estatus.add("Pendiente");

        empleadoLogeado = controladorEmpleado.getEmpleadoLogeado();
//        System.out.println("empleadoLogeado " + empleadoLogeado);
        nuevaListaFuncionesEspecificas.clear();
        nuevaListaFuncionesGenerales.clear();
        nuevoOBJFunciones = new Funciones();
        mostrarContactosParaNotificacion();
        System.out.println("ControladorSubordinados Fin: " + System.currentTimeMillis());
    }

    public void mostrarPerfilSubordinado() {
        try {
            nuevaListaInformacionAdicionalPersonalSubordinado.clear();
            nuevaListaListaPersonaSubordinado.clear();
            nuevoOBJPersonal = new Personal();

            nuevaListaInformacionAdicionalPersonalSubordinado = ejbSelectec.mostrarListaDeInformacionAdicionalPersonal(contactoDestino);
            nuevaListaListaPersonaSubordinado = ejbSelectec.mostrarListaDeEmpleadosXClave(contactoDestino);

            if (nuevaListaInformacionAdicionalPersonalSubordinado.isEmpty() || nuevaListaListaPersonaSubordinado.isEmpty()) {

                nuevoOBJListaPersonal = nuevaListaListaPersonaSubordinado.get(0);
                nuevoOBJInformacionAdicionalPersonal = new InformacionAdicionalPersonal();

                listaFormacionAcademica.clear();
                listaExperienciasLaborales.clear();
                listaCapacitacionespersonal.clear();
                listaIdiomas.clear();
                listaHabilidadesInformaticas.clear();
                listaInvestigaciones.clear();

            } else {
                nuevoOBJInformacionAdicionalPersonal = nuevaListaInformacionAdicionalPersonalSubordinado.get(0);
                nuevoOBJListaPersonal = nuevaListaListaPersonaSubordinado.get(0);
                nuevoOBJPersonal = ejbSelectec.mostrarEmpleadosPorClave(contactoDestino);
                informacionCV();
                mostrarFuncioneSubordinado();
                mostrarIncidencias();
            }
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

            listaFormacionAcademica = ejbSelectec.mostrarFormacionAcademica(contactoDestino);
            listaExperienciasLaborales = ejbSelectec.mostrarExperienciasLaborales(contactoDestino);
            listaCapacitacionespersonal = ejbSelectec.mostrarCapacitacionespersonal(contactoDestino);
            listaIdiomas = ejbSelectec.mostrarHabilidadesIdiomasPorClaveTrabajador(contactoDestino);
            listaHabilidadesInformaticas = ejbSelectec.mostrarHabilidadesInformaticasPorClaveTrabajador(contactoDestino);
            listaInvestigaciones = ejbSelectec.mostrarInvestigacionPorClaveTrabajador(contactoDestino);
            listaDocencias = ejbSelectec.mostrarDocencias(contactoDestino);
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
                    listaFuncioneSubordinado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 32:
                    listaFuncioneSubordinado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 34:
                    listaFuncioneSubordinado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 41:
                    listaFuncioneSubordinado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                default:
                    listaFuncioneSubordinado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(nuevoOBJListaPersonal.getAreaOperativa(), nuevoOBJListaPersonal.getCategoriaOperativa(), nuevoOBJPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
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
            nuevaListaListaPersonalLogeado.clear();
            nuevoOBJListaPersonalLogeado = new ListaPersonal();
            nuevaListaListaPersonalLogeado = ejbSelectec.mostrarListaDeEmpleadosXClave(empleadoLogeado);
            nuevoOBJListaPersonalLogeado = nuevaListaListaPersonalLogeado.get(0);

            nuevaListaListaPersonal = ejbSelectec.mostrarListaDeEmpleadosParaJefes(nuevoOBJListaPersonalLogeado.getAreaOperativa());
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
            listaIncidencias = ejbSelectec.mostrarIncidenciasPorEstatus(contactoDestino, "Pendiente");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            ejbUpdate.actualizarIncidencias((Incidencias) event.getObject());
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
