package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Messages;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasPersonal implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Integer usuario, numeroIN = 0, numeroCU = 0;
    @Getter    @Setter    private String tipo;
    @Getter    @Setter    private List<String> tiposIncidencias = new ArrayList<>(), tiposCuidados = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidades = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidados = new ArrayList<>();
    @Getter    @Setter    private Incidencias nuevOBJIncidencias = new Incidencias(), nuevOBJIncidenciasEditada = new Incidencias();
    @Getter    @Setter    private Incapacidad nuevOBJIncapacidad = new Incapacidad();
    @Getter    @Setter    private Cuidados nuevOBJCuidados = new Cuidados();
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    @Getter    @Setter    private Boolean registro = true, activo = false, archivoSC = false;
    @Getter    @Setter    private Date tiempo = new Date();
    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now();
    @Getter    @Setter    private LocalDate fechaI = LocalDate.now();
    @Getter    @Setter    private LocalDate fechaF = LocalDate.now();
    @Getter    @Setter    private Part file;

//@EJB
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {
        fechaActual = LocalDate.now();
        tiempo = new Date(0, 0, 0, 0, 0);
        registro = true;
        activo = false;
        nuevOBJIncidencias = new Incidencias();
        nuevOBJIncapacidad = new Incapacidad();
        nuevOBJCuidados = new Cuidados();
        tiposIncidencias.clear();
        tiposIncidencias.add("No registro entrada");
        tiposIncidencias.add("No registro salida");
        if (controladorEmpleado.getNuevoOBJListaPersonal().getActividad() == 3) {
            tiposIncidencias.add("Retardo");
        } else {
            tiposIncidencias.add("Retardo menor");
            tiposIncidencias.add("Retardo mayor");
        }
        tiposIncidencias.add("Salida anticipada");
        tiposIncidencias.add("Inasistencia");

        tiposCuidados.clear();
        tiposCuidados.add("Familiares");
        tiposCuidados.add("Paternos");
        tiposCuidados.add("Maternos");

        usuario = controladorEmpleado.getEmpleadoLogeado();
        mostrarLista();
    }
// ------------------------------------------------------------- Generales -------------------------------------------------------------

    public void mostrarLista() {
        try {
            fechaActual =LocalDate.now();
            listaIncidencias = new ArrayList<>();            listaIncidencias.clear();
            listaIncapacidades = new ArrayList<>();            listaIncapacidades.clear();

            listaIncidencias = ejbNotificacionesIncidencias.mostrarIncidencias(usuario);
            listaIncapacidades = ejbNotificacionesIncidencias.mostrarIncapacidad(usuario);
            listaCuidados = ejbNotificacionesIncidencias.mostrarCuidados(usuario);

            List<Incidencias> incidenciases = new ArrayList<>();            incidenciases.clear();
            
            if (fechaActual.getDayOfMonth() <= 15) {
                fechaI = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01);
                fechaF = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 15);
            } else {
                fechaI = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 16);
                fechaF = LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), LocalDate.of(fechaActual.getYear(), fechaActual.getMonthValue(), 01).lengthOfMonth());
            }         
            
            if (!listaIncidencias.isEmpty()) {
                listaIncidencias.forEach((i) -> {
                    if ((utilidadesCH.castearDaLD(i.getFecha()).isAfter(fechaI) || utilidadesCH.castearDaLD(i.getFecha()).equals(fechaI)) && (utilidadesCH.castearDaLD(i.getFecha()).isBefore(fechaF) || utilidadesCH.castearDaLD(i.getFecha()).equals(fechaF))) {
                        incidenciases.add(i);
                    }
                });
            }
            if (!incidenciases.isEmpty()) {
                if (incidenciases.size() >= 2) {
                    registro = false;
                }
            }
            List<Incidencias> list1 = new ArrayList<>();
            List<Incidencias> list2 = new ArrayList<>();
            numeroCU = ejbNotificacionesIncidencias.mostrarCuidadosTotales().size() + 1;
            list1 = ejbNotificacionesIncidencias.mostrarIncidenciasArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
            if (!list1.isEmpty()) {
                list1.forEach((t) -> {
                    if ((utilidadesCH.castearDaLD(t.getFecha()).getYear() - 100) == (fechaActual.getYear() - 100)) {
                        list2.add(t);
                    }
                });
            }
            if (!list2.isEmpty()) {
                Collections.sort(list2, (x, y) -> Integer.compare(x.getNumeroOficio(), y.getNumeroOficio()));
                list2.forEach((t) -> {
                    numeroIN = t.getNumeroOficio();
                });
            }
            nuevOBJIncidencias.setNumeroOficio((numeroIN + 1));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

// ------------------------------------------------------------- incidencias -------------------------------------------------------------
    public void crearIncidencia() {
        try {
            nuevOBJIncidencias.setClavePersonal(new Personal());
            nuevOBJIncidencias.getClavePersonal().setClave(usuario);
            nuevOBJIncidencias.setEstatus("Pendiente");
            if ((tiempo.getHours() == 0 && tiempo.getMinutes() == 0) && (nuevOBJIncidencias.getTipo().equals("Retardo menor") || nuevOBJIncidencias.getTipo().equals("Retardo mayor") || nuevOBJIncidencias.getTipo().equals("Salida anticipada"))) {
                Messages.addGlobalWarn("¡No puede registrar una incidencia con el tiempo 00:00!");
            } else {
                if (nuevOBJIncidencias.getNumeroOficio() != 0) {
                    switch (nuevOBJIncidencias.getTipo()) {
                        case "Inasistencia":
                            tiempo = new Date(0, 0, 0, 8, 0);
                            break;
                        case "Retardo menor":
                            if (tiempo.getHours() != 0) {
                                if (tiempo.getMinutes() < 11 || tiempo.getMinutes() > 20) {
                                    Messages.addGlobalWarn("¡El Retardo menor es desde los 11 minutos hasta los 20 minutos!");
                                    return;
                                }
                            } else {
                                Messages.addGlobalWarn("¡El Retardo menor es desde los 11 minutos hasta los 20 minutos!");
                                return;
                            }
                            break;
                        case "Retardo mayor":
                            if (tiempo.getHours() == 0) {
                                if (tiempo.getMinutes() < 21) {
                                    Messages.addGlobalWarn("¡El Retardo mayor es desde los 21 minutos!");
                                    return;
                                }
                            }
                            break;
                        case "Retardo":
                            if (tiempo.getMinutes() < 6) {
                                Messages.addGlobalWarn("¡El Retardo es desde de los 6 minutos!");
                                return;
                            }
                            break;
                    }
                    nuevOBJIncidencias.setTiempo(dateFormat.format(tiempo));
                    Integer dias = (int) ((utilidadesCH.castearLDaD(fechaActual).getTime() - nuevOBJIncidencias.getFecha().getTime()) / 86400000);
                    Integer maximo = 0;
                    switch (utilidadesCH.castearDaLD(nuevOBJIncidencias.getFecha()).getDayOfWeek()) {
                        case MONDAY:                    maximo = 1;                    break;
                        case TUESDAY:                    maximo = 1;                    break;
                        case WEDNESDAY:                    maximo = 1;                    break;
                        case THURSDAY:                    maximo = 1;                    break;
                        case FRIDAY:                    maximo = 3;                    break;
                        case SATURDAY:                    maximo = 2;                    break;
            }
                    if (dias <= maximo) {
                        nuevOBJIncidencias = ejbNotificacionesIncidencias.agregarIncidencias(nuevOBJIncidencias);
                        utilidadesCH.agregaBitacora(usuario, nuevOBJIncidencias.getIncidenciaID().toString(), "Incidencias", "Insert");
                        nuevOBJIncidencias = new Incidencias();
                        Messages.addGlobalInfo("¡Operación exitosa!");
                    } else {
                        Messages.addGlobalWarn("¡El tiempo maximo para el registro de incidencia ya expiro!");
                    }
                } else {
                    Messages.addGlobalWarn("¡El número de oficio no puede ser 0!");
                }
            }
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaIncidencia() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJIncidencias.setEvidencia(utilidadesCH.agregarEvidencias(file, usuario.toString(), "Incidencia", ""));
        //Se bacía el valor de la variable file
        file = null;
    }

    public void agregarEvidenciaIncidenciaDesfasada() {
        archivoSC = true;
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJIncidenciasEditada.setEvidencia(utilidadesCH.agregarEvidencias(file, usuario.toString(), "Incidencia", ""));
        //Se bacía el valor de la variable file
        file = null;
    }

    public void eliminarIncidencia(Incidencias incidencias) {
        try {
            if (incidencias.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(incidencias.getEvidencia());
            }
            utilidadesCH.agregaBitacora(usuario, incidencias.getIncidenciaID().toString(), "Incidencias", "Delate");
            ejbNotificacionesIncidencias.eliminarIncidencias(incidencias);
            mostrarLista();
            Ajax.update("@form");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void editarIncideciaDesfazada() {
        try {
            archivoSC = false;
            ejbNotificacionesIncidencias.actualizarIncidencias(nuevOBJIncidenciasEditada);
            Messages.addGlobalInfo("Evidencia actualizada exitosamente !");
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Incidencias incidencias = (Incidencias) event.getObject();
            incidencias.setTiempo(dateFormat.format(dateFormat.parse(incidencias.getTiempo())));
            Integer dias = (int) ((utilidadesCH.castearLDaD(fechaActual).getTime() - incidencias.getFecha().getTime()) / 86400000);
            Integer maximo = 0;
            switch (utilidadesCH.castearDaLD(incidencias.getFecha()).getDayOfWeek()) {
                case MONDAY:                    maximo = 1;                    break;
                case TUESDAY:                    maximo = 1;                    break;
                case WEDNESDAY:                    maximo = 1;                    break;
                case THURSDAY:                    maximo = 1;                    break;
                case FRIDAY:                    maximo = 3;                    break;
                case SATURDAY:                    maximo = 2;                    break;
            }
            if (dias <= maximo) {
                ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
                Messages.addGlobalInfo("¡Operación exitosa!");
            } else {
                Messages.addGlobalWarn("¡El tiempo maximo para el registro de incidencia ya expiro!");
            }
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// ------------------------------------------------------------- Incacpacidades -------------------------------------------------------------

    public void crearIncapacidad() {
        try {
            nuevOBJIncapacidad.setClavePersonal(new Personal());
            nuevOBJIncapacidad.getClavePersonal().setClave(usuario);
            nuevOBJIncapacidad = ejbNotificacionesIncidencias.agregarIncapacidad(nuevOBJIncapacidad);
            utilidadesCH.agregaBitacora(usuario, nuevOBJIncapacidad.getIncapacidad().toString(), "Incapacidad", "Insert");
            nuevOBJIncapacidad = new Incapacidad();
            Messages.addGlobalInfo("¡Operación exitosa!");

            mostrarLista();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaIncapacidad() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJIncapacidad.setEvidencia(utilidadesCH.agregarEvidencias(file, usuario.toString(), "incapacidades", ""));
        //Se bacía el valor de la variable file
        activo = true;
        file = null;
    }

    public void onRowEditInca(RowEditEvent event) {
        try {
            Incapacidad incapacidad = (Incapacidad) event.getObject();
            ejbNotificacionesIncidencias.actualizarIncapacidad(incapacidad);
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
// ------------------------------------------------------------- Cuidados -------------------------------------------------------------

    public void crearCuidado() {
        try {
            nuevOBJCuidados.setPersonal(new Personal());
            nuevOBJCuidados.getPersonal().setClave(usuario);
            nuevOBJCuidados.setNumero(numeroCU);
            Integer dias = (int) ((nuevOBJCuidados.getFechaFin().getTime() - nuevOBJCuidados.getFechaInicio().getTime()) / 86400000);
            dias = dias + 1;
            nuevOBJCuidados.setNumeroDias(dias);

            nuevOBJCuidados = ejbNotificacionesIncidencias.agregarCuidados(nuevOBJCuidados);
            utilidadesCH.agregaBitacora(usuario, nuevOBJCuidados.getCuidados().toString(), "Cuidados", "Insert");
            nuevOBJCuidados = new Cuidados();
            Messages.addGlobalInfo("¡Operación exitosa!");

            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaCuidado() {
        //Se invoca el método agregarEvidencias en el cual se envía ciertos parámetros (descritos dentro del método) el cual regresara la ruta del archivo ya almacenado en el servidor.
        nuevOBJCuidados.setEvidencia(utilidadesCH.agregarEvidencias(file, usuario.toString(), "cuidados", ""));
        //Se bacía el valor de la variable file
        activo = true;
        file = null;
    }

    public void onRowEditCuidado(RowEditEvent event) {
        try {
            Cuidados cuidados = (Cuidados) event.getObject();
            ejbNotificacionesIncidencias.actualizarCuidados(cuidados);
            mostrarLista();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void metodoBase() {

    }
}