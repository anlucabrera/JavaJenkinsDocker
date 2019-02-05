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
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import org.omnifaces.util.Messages;
import org.primefaces.model.StreamedContent;

import java.io.File;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import org.omnifaces.util.Ajax;
import org.primefaces.event.RowEditEvent;


@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasPersonal implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private Integer usuario,numeroIN=0,numeroCU=0;
    @Getter    @Setter    private String tipo;
    @Getter    @Setter    private List<String> tiposIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<String> tiposCuidados = new ArrayList<>();
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidades = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidados = new ArrayList<>();
    @Getter    @Setter    private Incidencias nuevOBJIncidencias=new Incidencias(),nuevOBJIncidenciasEditada=new Incidencias();
    @Getter    @Setter    private Incapacidad nuevOBJIncapacidad=new Incapacidad();
    @Getter    @Setter    private Cuidados nuevOBJCuidados=new Cuidados();
    @Getter    @Setter    private DateFormat dateFormat = new SimpleDateFormat("HH:mm");
    @Getter    @Setter    private Boolean registro = true,activo=false,archivoSC=false;
    @Getter    @Setter    private Date tiempo=new Date();
    @Getter    @Setter    private Date fechaActual= new Date();
    @Getter    @Setter    private Date fechaI =new Date();
    @Getter    @Setter    private Date fechaF =new Date();

    @Getter    @Setter    private Part file;
    @Getter    private String ruta;
    @Getter    StreamedContent content;

    @EJB    EjbCarga carga;
//@EJB
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("ControladorIncidenciasPersonal Inicio: " + System.currentTimeMillis());
        fechaActual = new Date();
        tiempo = new Date(0, 0, 0, 0, 0);
        registro = true;
        activo = false;
        nuevOBJIncidencias = new Incidencias();
        nuevOBJIncapacidad = new Incapacidad();
        nuevOBJCuidados=new Cuidados();
        tiposIncidencias.clear();
        tiposIncidencias.add("No registro entrada");
        tiposIncidencias.add("No registro salida");
        if (controladorEmpleado.getNuevoOBJListaPersonal().getActividad()== 3){
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
        System.out.println("ControladorIncidenciasPersonal Fin: " + System.currentTimeMillis());
    }
// ------------------------------------------------------------- Generales -------------------------------------------------------------
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }

    public String convertirRutaVistaEvidencia(String ruta) {
        if (!"".equals(ruta)) {
            File file = new File(ruta);
            return "evidencias2".concat(file.toURI().toString().split("archivos")[1]);
        } else {
            Messages.addGlobalWarn("No fue posible cargar el archivo!");
            return null;
        }
    }

    public void metodoBase() {

    }

    public void mostrarLista() {
        try {
            DateFormat dateFormatF = new SimpleDateFormat("dd/MM/yyyy");
            fechaActual = new Date();
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();
            listaIncapacidades = new ArrayList<>();
            listaIncapacidades.clear();

            listaIncidencias = ejbNotificacionesIncidencias.mostrarIncidencias(usuario);
            listaIncapacidades = ejbNotificacionesIncidencias.mostrarIncapacidad(usuario);
            listaCuidados = ejbNotificacionesIncidencias.mostrarCuidados(usuario);

            List<Incidencias> incidenciases = new ArrayList<>();
            incidenciases.clear();
            fechaI = new Date();
            fechaF = new Date();
            String mes = "";
            if (fechaActual.getMonth() <= 8) {
                mes = "0" + (fechaActual.getMonth() + 1);
            } else {
                mes = String.valueOf(fechaActual.getMonth() + 1);
            }
            System.out.println("dia " + fechaActual.getDate() + " mes" + fechaActual.getMonth());
            if (fechaActual.getDate() <= 15) {
                fechaI = dateFormatF.parse("01/" + mes + "/20" + (fechaActual.getYear() - 100));
                fechaF = dateFormatF.parse("15/" + mes + "/20" + (fechaActual.getYear() - 100));
            } else {
                fechaI = dateFormatF.parse("16/" + mes + "/20" + (fechaActual.getYear() - 100));
                fechaF = dateFormatF.parse("31/" + mes + "/20" + (fechaActual.getYear() - 100));
            }
            if (!listaIncidencias.isEmpty()) {
                listaIncidencias.forEach((i) -> {
                    if (i.getFecha().after(fechaI) && i.getFecha().before(fechaF)) {
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

            numeroCU = ejbNotificacionesIncidencias.mostrarCuidadosTotales().size()+1;
            list1 = ejbNotificacionesIncidencias.mostrarIncidenciasArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());

            if (!list1.isEmpty()) {
                list1.forEach((t) -> {
                    if ((t.getFecha().getYear() - 100) == (fechaActual.getYear() - 100)) {
                        list2.add(t);
                    }
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.mostrarLista(1)"+numeroIN);
            if (!list2.isEmpty()) {
                Collections.sort(list2, (x, y) -> Integer.compare(x.getNumeroOficio(),y.getNumeroOficio()));
                list2.forEach((t) -> {
                    numeroIN=t.getNumeroOficio();
                });
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.mostrarLista(2)"+numeroIN);
            nuevOBJIncidencias.setNumeroOficio((numeroIN+1));

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
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.crearIncidencia()"+nuevOBJIncidencias.getTipo());
            if ((tiempo.getHours() == 0 && tiempo.getMinutes() == 0) && (nuevOBJIncidencias.getTipo().equals("Retardo menor") || nuevOBJIncidencias.getTipo().equals("Retardo mayor") || nuevOBJIncidencias.getTipo().equals("Salida anticipada"))) {
                Messages.addGlobalWarn("¡No puede registrar una incidencia con el tiempo 00:00!");
            } else {
                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.crearIncidencia()"+nuevOBJIncidencias.getTipo());
                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.crearIncidencia()"+tiempo.getHours()+" - "+tiempo.getMinutes());
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
                            }else{
                                if (tiempo.getMinutes() < 11 || tiempo.getMinutes() > 20) {
                                    Messages.addGlobalWarn("¡El Retardo menor es desde los 11 minutos hasta los 20 minutos!");
                                    return;
                                }
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
                    Integer dias = (int) ((fechaActual.getTime() - nuevOBJIncidencias.getFecha().getTime()) / 86400000);
                    Integer maximo = 0;
                    switch (nuevOBJIncidencias.getFecha().getDay()) {
                        case 1:
                            maximo = 1;
                            break;
                        case 2:
                            maximo = 1;
                            break;
                        case 3:
                            maximo = 1;
                            break;
                        case 4:
                            maximo = 1;
                            break;
                        case 5:
                            maximo = 3;
                            break;
                        case 6:
                            maximo = 2;
                            break;
                    }
                    if (dias <= maximo) {
                        nuevOBJIncidencias = ejbNotificacionesIncidencias.agregarIncidencias(nuevOBJIncidencias);
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
        if (file != null) {
            ruta = carga.subir(file, new File(usuario.toString().concat(File.separator).concat("Incidencia").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJIncidencias.setEvidencia(ruta);
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !");
        }
        file = null;
    }

    public void agregarEvidenciaIncidenciaDesfasada() {
        archivoSC = true;
        if (file != null) {
            ruta = carga.subir(file, new File(usuario.toString().concat(File.separator).concat("Incidencia").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJIncidenciasEditada.setEvidencia(ruta);
                editarIncideciaDesfazada();
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !");
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !");
        }
        file = null;
    }

    public void eliminarIncidencia(Incidencias incidencias) {
        try {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.eliminarIncidencia()");
            if (incidencias.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(incidencias.getEvidencia());
            }
            ejbNotificacionesIncidencias.eliminarIncidencias(incidencias);
            mostrarLista();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.eliminarIncidencia()");
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
            Integer dias = (int) ((fechaActual.getTime() - incidencias.getFecha().getTime()) / 86400000);
            Integer maximo = 0;
            switch (incidencias.getFecha().getDay()) {
                case 1:
                    maximo = 1;
                    break;
                case 2:
                    maximo = 1;
                    break;
                case 3:
                    maximo = 1;
                    break;
                case 4:
                    maximo = 1;
                    break;
                case 5:
                    maximo = 3;
                    break;
                case 6:
                    maximo = 2;
                    break;
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
            nuevOBJIncapacidad = new Incapacidad();
            Messages.addGlobalInfo("¡Operación exitosa!");

            mostrarLista();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaIncapacidad() {
        if (file != null) {
            ruta = carga.subir(file, new File(usuario.toString().concat(File.separator).concat("incapacidades").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJIncapacidad.setEvidencia(ruta);
                activo = true;
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !");
                activo = false;
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !");
            activo = false;
        }
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
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.crearCuidado()");
            nuevOBJCuidados.setPersonal(new Personal());
            nuevOBJCuidados.getPersonal().setClave(usuario);
            nuevOBJCuidados.setNumero(numeroCU);
            Integer dias = (int) ((nuevOBJCuidados.getFechaFin().getTime() - nuevOBJCuidados.getFechaInicio().getTime()) / 86400000);
            dias = dias + 1;
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.crearCuidado()" + dias);
            nuevOBJCuidados.setNumeroDias(dias);

            nuevOBJCuidados = ejbNotificacionesIncidencias.agregarCuidados(nuevOBJCuidados);
            nuevOBJCuidados = new Cuidados();
            Messages.addGlobalInfo("¡Operación exitosa!");

            mostrarLista();
            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.ControladorIncidenciasPersonal.crearCuidado()");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void agregarEvidenciaCuidado() {
        if (file != null) {
            ruta = carga.subir(file, new File(usuario.toString().concat(File.separator).concat("cuidados").concat(File.separator)));
            if (!"Error: No se pudo leer el archivo".equals(ruta)) {
                nuevOBJCuidados.setEvidencia(ruta);
                activo = true;
                ruta = null;
            } else {
                ruta = null;
                Messages.addGlobalWarn("No fue posible cargar el archivo, Intente nuevamente !");
                activo = false;
            }
        } else {
            Messages.addGlobalWarn("Es necesario seleccionar un archivo !");
            activo = false;
        }
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

}
