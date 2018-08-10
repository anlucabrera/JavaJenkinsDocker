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

    @Getter    @Setter    private List<InformacionAdicionalPersonal> nuevaListaInformacionAdicionalPersonalLogeado = new ArrayList<>();
    @Getter    @Setter    private List<ListaPersonal> nuevaListaListaPersonalLogeado = new ArrayList<>();
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
    @Getter    @Setter    private contactosChat nuevOBJcontactosChat,nuevOBJcontactosChat2,nuevOBJcontactosChat3;
    @Getter    @Setter    private contactosChat nuevoOBJcontactosChatSelec = new contactosChat(0, "", 0);  
    @Getter    @Setter    private List<Integer> clavesContactosCChat = new ArrayList<>();
    @Getter    @Setter    private Personal nuevoOBJListaPersonalContacto,nuevoOBJListaPersonal;
    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbSelectec ejbSelectec;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbCreate ejbCreate;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUpdate ejbUpdate;

    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorEmpleadoLogeado Inicio: " + System.currentTimeMillis());
        empleadoLogeado = controladorEmpleado.getEmpleadoLogeado();
        nuevaListaFuncionesEspecificas.clear();
        nuevaListaFuncionesGenerales.clear();
        nuevoOBJFunciones = new Funciones();
        mostrarPerfilLogeado();
        listaDoc();
        System.out.println("ControladorEmpleadoLogeado Fin: " + System.currentTimeMillis());
    }

    public void listaDoc() {
        try {
            listaDocencias.clear();
            listaDocencias = ejbSelectec.mostrarDocencias(empleadoLogeado);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEmpleadoLogeado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPerfilLogeado() {
        try {
            nuevaListaInformacionAdicionalPersonalLogeado.clear();
            nuevaListaListaPersonalLogeado.clear();
            nuevoOBJListaPersonal = new Personal();

            nuevaListaInformacionAdicionalPersonalLogeado = ejbSelectec.mostrarListaDeInformacionAdicionalPersonal(empleadoLogeado);
            nuevaListaListaPersonalLogeado = ejbSelectec.mostrarListaDeEmpleadosXClave(empleadoLogeado);
            nuevoOBJListaPersonal = ejbSelectec.mostrarEmpleadosPorClave(empleadoLogeado);

            if (nuevaListaInformacionAdicionalPersonalLogeado.isEmpty()) {
                Messages.addGlobalFatal("Sin información complementaria para la clave " + empleadoLogeado);
            } else {
                nuevoOBJInformacionAdicionalPersonal = nuevaListaInformacionAdicionalPersonalLogeado.get(0);
            }

            if (nuevaListaListaPersonalLogeado.isEmpty()) {
                Messages.addGlobalFatal("Sin datos para la clave " + empleadoLogeado);
            } else {
                nuevoOBJListaListaPersonal = nuevaListaListaPersonalLogeado.get(0);
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
            listaFormacionAcademica = ejbSelectec.mostrarFormacionAcademica(empleadoLogeado);
            listaExperienciasLaborales = ejbSelectec.mostrarExperienciasLaborales(empleadoLogeado);
            listaCapacitacionespersonal = ejbSelectec.mostrarCapacitacionespersonal(empleadoLogeado);
            listaIdiomas = ejbSelectec.mostrarHabilidadesIdiomasPorClaveTrabajador(empleadoLogeado);
            listaHabilidadesInformaticas = ejbSelectec.mostrarHabilidadesInformaticasPorClaveTrabajador(empleadoLogeado);
            listaLenguas = ejbSelectec.mostrarHabilidadesLengiasPorClaveTrabajador(empleadoLogeado);
            listaDesarrolloSoftwar = ejbSelectec.mostrarDesarrollosSoftware(empleadoLogeado);
            listaDesarrollosTecnologicos = ejbSelectec.mostrarDesarrollosTecnologicos(empleadoLogeado);
            listaInnovaciones = ejbSelectec.mostrarInnovaciones(empleadoLogeado);
            listaDistinciones = ejbSelectec.mostrarDistinciones(empleadoLogeado);
            listaLibrosPubs = ejbSelectec.mostrarLibrosPublicados(empleadoLogeado);
            listaArticulosp = ejbSelectec.mostrarArticulospublicados(empleadoLogeado);
            listaMemoriaspub = ejbSelectec.mostrarMemoriaspubicados(empleadoLogeado);
            listaInvestigacion = ejbSelectec.mostrarInvestigacionPorClaveTrabajador(empleadoLogeado);
            listaCongresos = ejbSelectec.mostrarCongresos(empleadoLogeado);
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
                    listaFuncionesLogeado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 32:
                    listaFuncionesLogeado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                case 34:
                    if (nuevoOBJListaListaPersonal.getAreaOperativa() >= 24 && nuevoOBJListaListaPersonal.getAreaOperativa() <= 56) {
                        listaFuncionesLogeado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    } else {
                        listaFuncionesLogeado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(nuevoOBJListaListaPersonal.getAreaOperativa(), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    }
                    break;
                case 41:
                    listaFuncionesLogeado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(Short.parseShort("61"), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
                    break;
                default:
                    listaFuncionesLogeado = ejbSelectec.mostrarListaDeFuncionesXAreaYPuestoOperativo(nuevoOBJListaListaPersonal.getAreaOperativa(), nuevoOBJListaListaPersonal.getCategoriaOperativa(), nuevoOBJListaPersonal.getCategoriaEspecifica().getCategoriaEspecifica());
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

            listaNotificacionesLogeado = ejbSelectec.mostrarListaDenotificacionesPorUsuario(empleadoLogeado);
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
                listaNotificacionesChat = ejbSelectec.mostrarListaDenotificacionesPorUsuariosyEstatus(empleadoLogeado, contactoDestino, 0);
                if (!listaNotificacionesChat.isEmpty()) {
                    for (int i = 0; i <= listaNotificacionesChat.size() - 1; i++) {
                        nuevoOBJNotificaciones = new Notificaciones();
                        nuevoOBJNotificaciones = listaNotificacionesChat.get(i);
                        nuevoOBJNotificaciones.setStatus(1);
                        nuevoOBJNotificaciones = ejbUpdate.actualizarNotificaciones(nuevoOBJNotificaciones);
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

            nuevaListaPersonalContacto = ejbSelectec.mostrarListaDeEmpleadosTotalActivos();
            for (int i = 0; i <= nuevaListaPersonalContacto.size() - 1; i++) {
                nuevoOBJListaPersonalContacto = new Personal();
                nuevoOBJListaPersonalContacto = nuevaListaPersonalContacto.get(i);
                if (!nuevoOBJListaPersonalContacto.getClave().equals(empleadoLogeado)) {
                    nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 2);
                    listaNotificacionesContacto = ejbSelectec.mostrarListaDenotificacionesPorUsuariosyEstatus(empleadoLogeado, nuevoOBJListaPersonalContacto.getClave(), 0);
                    if (!listaNotificacionesContacto.isEmpty()) {
                        listacontactosChat.add(nuevOBJcontactosChat);
                        clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                    }
                    listaNotificacionesContacto.clear();
                }
            }
//            System.out.println("listacontactosChat.size() 1: " + listacontactosChat.size());
//            System.out.println("_________________________________________________________________");
            for (int i = 0; i <= nuevaListaPersonalContacto.size() - 1; i++) {
                nuevoOBJListaPersonalContacto = new Personal();
                nuevoOBJListaPersonalContacto = nuevaListaPersonalContacto.get(i);
                if (!nuevoOBJListaPersonalContacto.getClave().equals(empleadoLogeado)) {
                    if (!clavesContactosCChat.contains(nuevoOBJListaPersonalContacto.getClave())) {
                        listaNotificacionesContacto = ejbSelectec.mostrarListaDenotificacionesPorUsuariosyEstatus(empleadoLogeado, nuevoOBJListaPersonalContacto.getClave(), 1);
                        if (!listaNotificacionesContacto.isEmpty()) {
                            nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 1);
                            listacontactosChat.add(nuevOBJcontactosChat);
                            clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                        } else {
                            listaNotificacionesContacto.clear();
                            listaNotificacionesContacto = ejbSelectec.mostrarListaDenotificacionesPorUsuariosyEstatus(nuevoOBJListaPersonalContacto.getClave(), empleadoLogeado, 1);
                            if (!listaNotificacionesContacto.isEmpty()) {
                                nuevOBJcontactosChat = new contactosChat(nuevoOBJListaPersonalContacto.getClave(), nuevoOBJListaPersonalContacto.getNombre(), 1);
                                listacontactosChat.add(nuevOBJcontactosChat);
                                clavesContactosCChat.add(nuevoOBJListaPersonalContacto.getClave());
                            } else {
                                listaNotificacionesContacto.clear();
                                listaNotificacionesContacto = ejbSelectec.mostrarListaDenotificacionesPorUsuariosyEstatus(nuevoOBJListaPersonalContacto.getClave(), empleadoLogeado, 0);
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
//            System.out.println("listacontactosChat.size() 2: " + listacontactosChat.size());
//            System.out.println("_________________________________________________________________");
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

//            System.out.println("listacontactosChat.size() 3: " + listacontactosChat.size());
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
                nuevoOBJNotificaciones = ejbCreate.agregarNotificacion(nuevoOBJNotificaciones);
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
