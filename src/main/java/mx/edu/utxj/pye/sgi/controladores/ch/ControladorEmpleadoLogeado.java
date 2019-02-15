package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.entity.ch.Articulosp;
import mx.edu.utxj.pye.sgi.entity.ch.ExperienciasLaborales;
import mx.edu.utxj.pye.sgi.entity.ch.Capacitacionespersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Congresos;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrolloSoftware;
import mx.edu.utxj.pye.sgi.entity.ch.DesarrollosTecnologicos;
import mx.edu.utxj.pye.sgi.entity.ch.Distinciones;
import mx.edu.utxj.pye.sgi.entity.ch.Docencias;
import mx.edu.utxj.pye.sgi.entity.ch.FormacionAcademica;
import mx.edu.utxj.pye.sgi.entity.ch.Funciones;
import mx.edu.utxj.pye.sgi.entity.ch.HabilidadesInformaticas;
import mx.edu.utxj.pye.sgi.entity.ch.Idiomas;
import mx.edu.utxj.pye.sgi.entity.ch.InformacionAdicionalPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Innovaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Lenguas;
import mx.edu.utxj.pye.sgi.entity.ch.LibrosPub;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Memoriaspub;
import mx.edu.utxj.pye.sgi.entity.ch.Notificaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Investigaciones;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorEmpleadoLogeado implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<Personal> nuevaListaPersonalContacto = new ArrayList<>();
    @Getter    @Setter    private List<Funciones> listaFuncionesLogeado = new ArrayList<>();
    @Getter    @Setter    private List<Notificaciones> listaNotificacionesLogeado = new ArrayList<>(),listaNotificacionesContacto = new ArrayList<>(),listaNotificacionesChat = new ArrayList<>();
    ////////////////////////////C V//////////////////////////////////
    @Getter    @Setter    private List<FormacionAcademica> listaFormacionAcademica = new ArrayList<>();
    @Getter    @Setter    private List<ExperienciasLaborales> listaExperienciasLaborales = new ArrayList<>();
    @Getter    @Setter    private List<Capacitacionespersonal> listaCapacitacionespersonal = new ArrayList<>();
    @Getter    @Setter    private List<Idiomas> listaIdiomas = new ArrayList<>();
    @Getter    @Setter    private List<HabilidadesInformaticas> listaHabilidadesInformaticas = new ArrayList<>();
    @Getter    @Setter    private List<Lenguas> listaLenguas = new ArrayList<>();
    @Getter    @Setter    private List<DesarrolloSoftware> listaDesarrolloSoftwar = new ArrayList<>();
    @Getter    @Setter    private List<DesarrollosTecnologicos> listaDesarrollosTecnologicos = new ArrayList<>();
    @Getter    @Setter    private List<Innovaciones> listaInnovaciones = new ArrayList<>();
    @Getter    @Setter    private List<Distinciones> listaDistinciones = new ArrayList<>();
    @Getter    @Setter    private List<LibrosPub> listaLibrosPubs = new ArrayList<>();
    @Getter    @Setter    private List<Articulosp> listaArticulosp = new ArrayList<>();
    @Getter    @Setter    private List<Memoriaspub> listaMemoriaspub = new ArrayList<>();
    @Getter    @Setter    private List<Investigaciones> listaInvestigacion = new ArrayList<>();
    @Getter    @Setter    private List<Congresos> listaCongresos = new ArrayList<>();
    
    @Getter    @Setter    private List<String> nuevaListaFuncionesEspecificas = new ArrayList<>(), nuevaListaFuncionesGenerales = new ArrayList<>(), mensajeN = new ArrayList<>();
    @Getter    @Setter    private List<Integer> cTD = new ArrayList<>(), cTR = new ArrayList<>();
    @Getter    @Setter    private List<Date> fechaNot = new ArrayList<>();
    @Getter    @Setter    private List<Docencias> listaDocencias = new ArrayList<>();
    
    @Getter    @Setter    private Date fechaActual = new Date();
    @Getter    @Setter    private Integer empleadoLogeado, contactoDestino;
    @Getter    @Setter    private String mensajeDNotificacion = "",nombreContacto="";

    @Getter    @Setter    private Notificaciones nuevoOBJNotificaciones;
    @Getter    @Setter    private Funciones nuevoOBJFunciones;
    @Getter    @Setter    private InformacionAdicionalPersonal nuevoOBJInformacionAdicionalPersonal;
    @Getter    @Setter    private ListaPersonal nuevoOBJListaListaPersonal;

    @Getter    @Setter    private List<contactosChat> listacontactosChat = new ArrayList<>();
    @Getter    @Setter    private contactosChat nuevOBJcontactosChat;
    @Getter    @Setter    private contactosChat nuevoOBJcontactosChatSelec = new contactosChat(0, "", 0);  
    @Getter    @Setter    private List<Integer> clavesContactosCChat = new ArrayList<>();
    @Getter    @Setter    private Personal nuevoOBJListaPersonalContacto,nuevoOBJListaPersonal;
    
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbFunciones ejbFunciones;    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbEducacion ejbEducacion;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbHabilidades ejbHabilidades;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbTecnologia ejbTecnologia;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPremios ejbPremios;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbProduccionProfecional ejbProduccionProfecional;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {        
        empleadoLogeado = controladorEmpleado.getEmpleadoLogeado();
        nuevaListaFuncionesEspecificas.clear();
        nuevaListaFuncionesGenerales.clear();
        nuevoOBJFunciones = new Funciones();
        mostrarPerfilLogeado();
        listaDoc();        
    }

    public void listaDoc() {
        try {
            listaDocencias.clear();
            listaDocencias = ejbPersonal.mostrarListaDocencias(empleadoLogeado);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPerfilLogeado() {
        try {
            nuevoOBJListaPersonal = new Personal();

            nuevoOBJInformacionAdicionalPersonal = ejbPersonal.mostrarInformacionAdicionalPersonalLogeado(empleadoLogeado);
            nuevoOBJListaListaPersonal = ejbPersonal.mostrarListaPersonal(empleadoLogeado);
            nuevoOBJListaPersonal = ejbPersonal.mostrarPersonalLogeado(empleadoLogeado);

            if (nuevoOBJInformacionAdicionalPersonal==null) {
                Messages.addGlobalFatal("Sin información complementaria para la clave " + empleadoLogeado);
            } 

            if (nuevoOBJListaListaPersonal==null) {
                Messages.addGlobalFatal("Sin datos para la clave " + empleadoLogeado);
            } 

            informacionCV();
            mostrarFuncionesLogeado();
//            mostrarNotificacionesLogeado();
//            mostrarContactosParaNotificacion();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void informacionCV() {
        try {
            listaFormacionAcademica = ejbEducacion.mostrarFormacionAcademica(empleadoLogeado);
            listaExperienciasLaborales = ejbEducacion.mostrarExperienciasLaborales(empleadoLogeado);
            listaCapacitacionespersonal = ejbEducacion.mostrarCapacitacionespersonal(empleadoLogeado);
            
            listaIdiomas = ejbHabilidades.mostrarIdiomas(empleadoLogeado);
            listaHabilidadesInformaticas = ejbHabilidades.mostrarHabilidadesInformaticas(empleadoLogeado);
            listaLenguas = ejbHabilidades.mostrarLenguas(empleadoLogeado);
            
            listaDesarrolloSoftwar = ejbTecnologia.mostrarDesarrolloSoftware(empleadoLogeado);
            listaDesarrollosTecnologicos = ejbTecnologia.mostrarDesarrollosTecnologicos(empleadoLogeado);
            listaInnovaciones = ejbTecnologia.mostrarInnovaciones(empleadoLogeado);
            
            listaDistinciones = ejbPremios.mostrarDistinciones(empleadoLogeado);
            
            listaLibrosPubs = ejbProduccionProfecional.mostrarLibrosPub(empleadoLogeado);
            listaArticulosp = ejbProduccionProfecional.mostrarArticulosp(empleadoLogeado);
            listaMemoriaspub = ejbProduccionProfecional.mostrarMemoriaspub(empleadoLogeado);
            listaInvestigacion = ejbProduccionProfecional.mostrarInvestigacion(empleadoLogeado);
            listaCongresos = ejbProduccionProfecional.mostrarCongresos(empleadoLogeado);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarFuncionesLogeado() {
        try {
            nuevaListaFuncionesGenerales.clear();
            nuevaListaFuncionesEspecificas.clear();
            switch (nuevoOBJListaListaPersonal.getCategoriaOperativa()) {
                case 30:
                    listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 32:
                    listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 34:
                    if (nuevoOBJListaListaPersonal.getAreaOperativa() >= 24 && nuevoOBJListaListaPersonal.getAreaOperativa() <= 56) {
                        listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaListaPersonal.getAreaOperativa(), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 41:
                    listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                default:
                    listaFuncionesLogeado = ejbFunciones.mostrarListaFuncionesPersonalLogeado(nuevoOBJListaListaPersonal.getAreaOperativa(), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
            }
            if (!listaFuncionesLogeado.isEmpty()) {
                for (int i = 0; i <= listaFuncionesLogeado.size() - 1; i++) {
                    nuevoOBJFunciones = new Funciones();
                    nuevoOBJFunciones = listaFuncionesLogeado.get(i);
                    if ("GENERAL".equals(nuevoOBJFunciones.getTipo())) {
                        nuevaListaFuncionesGenerales.add(nuevoOBJFunciones.getNombre());
                    } else {
                        nuevaListaFuncionesEspecificas.add(nuevoOBJFunciones.getNombre());
                    }
                }
            }
            listaFuncionesLogeado.clear();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarNotificacionesLogeado() {
        try {
            if (nuevoOBJcontactosChatSelec.getClave() == 0) {
                contactoDestino = 0;
                nombreContacto = "";
            } else {
                contactoDestino = nuevoOBJcontactosChatSelec.getClave();
                nombreContacto = nuevoOBJcontactosChatSelec.getNombre();
            }
            cTD.clear();
            cTR.clear();
            mensajeN.clear();
            fechaNot.clear();
            listaNotificacionesLogeado.clear();

            listaNotificacionesLogeado = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuario(empleadoLogeado);
            if (listaNotificacionesLogeado.isEmpty()) {
                listaNotificacionesLogeado.clear();
            } else {
                for (int j = listaNotificacionesLogeado.size() - 1; j >= 0; j--) {
                    nuevoOBJNotificaciones = listaNotificacionesLogeado.get(j);
                    cTD.add(nuevoOBJNotificaciones.getClaveTDestino().getClave());
                    cTR.add(nuevoOBJNotificaciones.getClaveTRemitente().getClave());
                    mensajeN.add(nuevoOBJNotificaciones.getMensaje());
                    fechaNot.add(nuevoOBJNotificaciones.getFecha());
                }
                listaNotificacionesChat.clear();
                listaNotificacionesChat = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(empleadoLogeado, contactoDestino, 0);
                if (!listaNotificacionesChat.isEmpty()) {
                    for (int i = 0; i <= listaNotificacionesChat.size() - 1; i++) {
                        nuevoOBJNotificaciones = new Notificaciones();
                        nuevoOBJNotificaciones = listaNotificacionesChat.get(i);
                        nuevoOBJNotificaciones.setStatus(1);
                        nuevoOBJNotificaciones = ejbNotificacionesIncidencias.actualizarNotificaciones(nuevoOBJNotificaciones);
                    }
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarContactosParaNotificacion() {
        try {
            nuevaListaPersonalContacto.clear();
            listaNotificacionesContacto.clear();
            listacontactosChat.clear();
            clavesContactosCChat.clear();

            nuevaListaPersonalContacto = ejbPersonal.mostrarListaPersonalsPorEstatus(1);
            for (int i = 0; i <= nuevaListaPersonalContacto.size() - 1; i++) {
                nuevoOBJListaPersonalContacto = new Personal();
                nuevoOBJListaPersonalContacto = nuevaListaPersonalContacto.get(i);
                if (!nuevoOBJListaPersonalContacto.getClave().equals(empleadoLogeado)) {
                    nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 2);
                    listaNotificacionesContacto = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(empleadoLogeado, nuevoOBJListaPersonalContacto.getClave(), 0);
                    if (!listaNotificacionesContacto.isEmpty()) {
                        listacontactosChat.add(nuevOBJcontactosChat);
                        clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                    }
                    listaNotificacionesContacto.clear();
                }
            }
            for (int i = 0; i <= nuevaListaPersonalContacto.size() - 1; i++) {
                nuevoOBJListaPersonalContacto = new Personal();
                nuevoOBJListaPersonalContacto = nuevaListaPersonalContacto.get(i);
                if (!nuevoOBJListaPersonalContacto.getClave().equals(empleadoLogeado)) {
                    if (!clavesContactosCChat.contains(nuevoOBJListaPersonalContacto.getClave())) {
                        listaNotificacionesContacto = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(empleadoLogeado, nuevoOBJListaPersonalContacto.getClave(), 1);
                        if (!listaNotificacionesContacto.isEmpty()) {
                            nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 1);
                            listacontactosChat.add(nuevOBJcontactosChat);
                            clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                        } else {
                            listaNotificacionesContacto.clear();
                            listaNotificacionesContacto = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(nuevoOBJListaPersonalContacto.getClave(), empleadoLogeado, 1);
                            if (!listaNotificacionesContacto.isEmpty()) {
                                nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 1);
                                listacontactosChat.add(nuevOBJcontactosChat);
                                clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                            } else {
                                listaNotificacionesContacto.clear();
                                listaNotificacionesContacto = ejbNotificacionesIncidencias.mostrarListaDenotificacionesPorUsuariosyEstatus(nuevoOBJListaPersonalContacto.getClave(), empleadoLogeado, 0);
                                if (!listaNotificacionesContacto.isEmpty()) {
                                    nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 1);
                                    listacontactosChat.add(nuevOBJcontactosChat);
                                    clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                                }
                            }
                        }
                    }
                    listaNotificacionesContacto.clear();
                }
            }
            for (int i = 0; i <= nuevaListaPersonalContacto.size() - 1; i++) {
                nuevoOBJListaPersonalContacto = new Personal();
                nuevoOBJListaPersonalContacto = nuevaListaPersonalContacto.get(i);
                if (!nuevoOBJListaPersonalContacto.getClave().equals(empleadoLogeado)) {
                    if (!clavesContactosCChat.contains(nuevoOBJListaPersonalContacto.getClave())) {
                        nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 0);
                        listacontactosChat.add(nuevOBJcontactosChat);
                        clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                    }
                    listaNotificacionesContacto.clear();
                }
            }
} catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarNotificacion() {
        try {
            if (mensajeDNotificacion.equals("")) {
            } else {
                nuevoOBJNotificaciones.setClaveTDestino(new Personal());
                nuevoOBJNotificaciones.setClaveTRemitente(new Personal());
                nuevoOBJNotificaciones = new Notificaciones();
                nuevoOBJNotificaciones.setFecha(fechaActual);
                nuevoOBJNotificaciones.setMensaje(mensajeDNotificacion);
                nuevoOBJNotificaciones.setStatus(0);
                nuevoOBJNotificaciones.getClaveTDestino().setClave(contactoDestino);
                nuevoOBJNotificaciones.getClaveTRemitente().setClave(empleadoLogeado);
                nuevoOBJNotificaciones = ejbNotificacionesIncidencias.agregarNotificacion(nuevoOBJNotificaciones);
                mensajeDNotificacion = "";
            }
            mostrarNotificacionesLogeado();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class contactosChat {

        @Getter        @Setter        private int clave;
        @Getter        @Setter        private String nombre;
        @Getter        @Setter        private int estatus;

        private contactosChat(int _clave, String _nombre, int _estatus) {
            clave = _clave;
            nombre = _nombre;
            estatus = _estatus;
        }
    }
}
