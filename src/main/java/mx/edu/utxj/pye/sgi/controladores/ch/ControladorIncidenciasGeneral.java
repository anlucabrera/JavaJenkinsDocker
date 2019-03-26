package mx.edu.utxj.pye.sgi.controladores.ch;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Bitacoraacceso;
import mx.edu.utxj.pye.sgi.entity.ch.Cuidados;
import mx.edu.utxj.pye.sgi.entity.ch.Incapacidad;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

@Named
@ManagedBean
@ViewScoped
public class ControladorIncidenciasGeneral implements Serializable {

    private static final long serialVersionUID = -8842055922698338073L;

    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Incapacidad> listaIncapacidads = new ArrayList<>();
    @Getter    @Setter    private List<Cuidados> listaCuidadoses = new ArrayList<>();
    @Getter    @Setter    private List<Bitacoraacceso> listaBitacoraaccesos = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> listaAreasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<ListaBitacoraIncidencias> bitacoraIncidenciases = new ArrayList<>();
    
    @Getter    @Setter    private AreasUniversidad au = new AreasUniversidad();
    @Getter    @Setter    private String areaNombre = "",mes="";
    @Getter    @Setter    private Short area = 0;

    @Getter    @Setter    private LocalDate fechaActual = LocalDate.now(), fechaI = LocalDate.now(), fechaF = LocalDate.now();
    @Getter    @Setter    private Integer anioNumero = 0;
    @Getter    @Setter    private String numeroQuincena = "1";
    @Getter    @Setter    private Boolean vistaMensual = true;

//@EJB    
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    private EjbAreasLogeo areasLogeo;
//@Inject
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;

    @PostConstruct
    public void init() {
        fechaI = LocalDate.now();
        fechaF = LocalDate.now();
        fechaActual = LocalDate.now();
        if (fechaActual.getDayOfMonth() <= 15) {
            numeroQuincena = "1";
        } else {
            numeroQuincena = "2";
        }
        anioNumero = fechaActual.getYear();
        area = 0;
        areaNombre = "Todas las áreas";
        mostrarIncidencias(String.valueOf(fechaActual.getMonthValue()));
        mostrarareas();
    }

    public void mostrarareas() {
        try {
            listaAreasUniversidads = new ArrayList<>();
            listaAreasUniversidads.clear();
            listaAreasUniversidads = areasLogeo.mostrarAreasUniversidadActivas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarIncidencias(String mActual) {
        try {
            mes = mActual;
            List<Incidencias> incidenciases = new ArrayList<>();
            incidenciases.clear();
            List<Incapacidad> incapacidads = new ArrayList<>();
            incapacidads.clear();
            List<Cuidados> cuidadoses = new ArrayList<>();
            cuidadoses.clear();
            if (area != 0) {
                au = new AreasUniversidad();
                au = areasLogeo.mostrarAreasUniversidad(area);
            }
            if (vistaMensual) {
                fechaI = LocalDate.of(anioNumero, Integer.parseInt(mActual), 01);
                fechaF = LocalDate.of(anioNumero, Integer.parseInt(mActual), LocalDate.of(anioNumero, Integer.parseInt(mActual), 01).lengthOfMonth());
            } else {
                if (numeroQuincena.equals("1")) {
                    fechaI = LocalDate.of(anioNumero, Integer.parseInt(mActual), 01);
                    fechaF = LocalDate.of(anioNumero, Integer.parseInt(mActual), 15);
                } else {
                    fechaI = LocalDate.of(anioNumero, Integer.parseInt(mActual), 16);
                    fechaF = LocalDate.of(anioNumero, Integer.parseInt(mActual), LocalDate.of(anioNumero, Integer.parseInt(mActual), 01).lengthOfMonth());
                }
            }
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();
            incidenciases = ejbNotificacionesIncidencias.mostrarIncidenciasReporte(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
            if (!incidenciases.isEmpty()) {
                incidenciases.forEach((t) -> {
                    if (area != 0) {
                        if (t.getClavePersonal().getAreaOperativa() == area) {
                            if (t.getClavePersonal().getActividad().getActividad() == 1 || t.getClavePersonal().getActividad().getActividad() == 3) {
                                listaIncidencias.add(t);
                            }
                        } else if (t.getClavePersonal().getAreaSuperior() == area) {
                            listaIncidencias.add(t);
                        }
                    } else {
                        listaIncidencias.add(t);
                    }
                });
            }
         
            listaBitacoraaccesos = new ArrayList<>();
            listaBitacoraaccesos.clear();
            listaBitacoraaccesos = ejbUtilidadesCH.mostrarBitacoraacceso("Incidencias", utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
            bitacoraIncidenciases = new ArrayList<>();
            bitacoraIncidenciases.clear();
            listaBitacoraaccesos.forEach((t) -> {
                ListaPersonal lp = buscarPerosnal(t.getClavePersonal());
                if (lp.getClave() != null) {
                    if (area != 0) {
                        if (lp.getAreaOperativa() == area) {
                            if (lp.getActividad() == 1 || lp.getActividad() == 3) {
                                bitacoraIncidenciases.add(new ListaBitacoraIncidencias(buscarPerosnal(t.getClavePersonal()), buscarIncidencias(t.getNumeroRegistro(), t.getAccion(), t), t));
                            }
                        } else if (lp.getAreaSuperior() == area) {
                            bitacoraIncidenciases.add(new ListaBitacoraIncidencias(buscarPerosnal(t.getClavePersonal()), buscarIncidencias(t.getNumeroRegistro(), t.getAccion(), t), t));
                        }
                    } else {
                        bitacoraIncidenciases.add(new ListaBitacoraIncidencias(buscarPerosnal(t.getClavePersonal()), buscarIncidencias(t.getNumeroRegistro(), t.getAccion(), t), t));
                    }
                }
            });
            
            listaIncapacidads = new ArrayList<>();
            listaIncapacidads.clear();
            incapacidads = ejbNotificacionesIncidencias.mostrarIncapacidadReporte(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
            if (!incapacidads.isEmpty()) {
                incapacidads.forEach((t) -> {
                    if (area != 0) {
                        if (t.getClavePersonal().getAreaOperativa() == area) {
                            if (t.getClavePersonal().getActividad().getActividad() == 1 || t.getClavePersonal().getActividad().getActividad() == 3) {
                                listaIncapacidads.add(t);
                            }
                        } else if (t.getClavePersonal().getAreaSuperior() == area) {
                            listaIncapacidads.add(t);
                        }
                    } else {
                        listaIncapacidads.add(t);
                    }
                });
            }
            cuidadoses = ejbNotificacionesIncidencias.mostrarCuidadosReporte(utilidadesCH.castearLDaD(fechaI), utilidadesCH.castearLDaD(fechaF));
            if (!cuidadoses.isEmpty()) {
                cuidadoses.forEach((t) -> {
                    if (area != 0) {
                        if (t.getPersonal().getAreaOperativa() == area) {
                            if (t.getPersonal().getActividad().getActividad() == 1 || t.getPersonal().getActividad().getActividad() == 3) {
                                listaCuidadoses.add(t);
                            }
                        } else if (t.getPersonal().getAreaSuperior() == area) {
                            listaCuidadoses.add(t);
                        }
                    } else {
                        listaCuidadoses.add(t);
                    }
                });
            }            
            
            Ajax.update("frmInciGeneral");
            Ajax.update("frmIncaGeneral");
            Ajax.update("frmCuidGeneral");
            Ajax.update("frmbitacora");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorSubordinados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void vistaMensualAsiganado(ValueChangeEvent event) {
        vistaMensual = true;
        vistaMensual = (Boolean) event.getNewValue();
        mostrarIncidencias(mes);
    }

    public void numeroQuincenaAsiganado(ValueChangeEvent event) {
        numeroQuincena = "";
        numeroQuincena = event.getNewValue().toString();
        mostrarIncidencias(mes);
    }

    public void numeroAreaAsiganado(ValueChangeEvent event) {
        area = 0;
        areaNombre = "";
        area = Short.parseShort(event.getNewValue().toString());
        if (area == 0) {
            areaNombre = "Todas las áreas";
        } else {
            areaNombre = buscarArea(area);
        }
        mostrarIncidencias(mes);
    }

    public void eliminarIncidencia(Incidencias incidencias) {
        try {
            if (incidencias.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(incidencias.getEvidencia());
            }
            ejbNotificacionesIncidencias.eliminarIncidencias(incidencias);
            mostrarIncidencias(mes);
            Ajax.update("frmInciGeneral");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarIncapacidad(Incapacidad incapacidad) {
        try {
            if (incapacidad.getEvidencia() != null) {
                CargaArchivosCH.eliminarArchivo(incapacidad.getEvidencia());
            }
            ejbNotificacionesIncidencias.eliminarIncapacidad(incapacidad);
            mostrarIncidencias(String.valueOf(fechaActual.getMonthValue()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void numeroAnioAsiganado(ValueChangeEvent event) {
        anioNumero = 0;
        anioNumero = Integer.parseInt(event.getNewValue().toString());
        mostrarIncidencias(mes);
    }

    public String buscarArea(Short area) {
        try {
            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(area);
            return areaU.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public ListaPersonal buscarPerosnal(Integer clave) {
        try {
            ListaPersonal p = new ListaPersonal();
            if (clave == 0) {
                p = new ListaPersonal(0, "Sistema", new Date(), 'A', Short.parseShort("0"), 'S', "Sistema", Short.parseShort("0"), "Sistema", Short.parseShort("6"), "Sistema", Short.parseShort("0"), "Sistema", Short.parseShort("0"), "Sistema", Short.parseShort("0"), "Sistema", Short.parseShort("0"), "Sistema", Short.parseShort("0"), "", "", Short.parseShort("0"), Short.parseShort("0"), new Date(), "Sistema", "Sistema", "Sistema", "Sistema", false, false, "Sistema", "Sistema", Short.parseShort("0"), "Sistema");
            } else {
                p = ejbPersonal.mostrarListaPersonal(clave);
            }
            return p;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Incidencias buscarIncidencias(String clave, String accion,Bitacoraacceso b) {
        try {
            Incidencias inc = new Incidencias();
            if (accion.equals("Delate")) {
                inc = new Incidencias(0, 0, "Eliminada", null, null, "Eliminada", "Eliminada");
            } else {
                inc = ejbNotificacionesIncidencias.buscarIncidencias(Integer.parseInt(clave));
            }
            if (inc == null) {
                inc = new Incidencias(0, 0, "Eliminada", null, null, "Eliminada", "Eliminada");
            }
            return inc;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void novisible() {
        mostrarIncidencias(mes);
    }

    public void imprimirValores() {
    }
    
    public static class ListaBitacoraIncidencias {

        @Getter        @Setter        private ListaPersonal lp;
        @Getter        @Setter        private Incidencias i;
        @Getter        @Setter        private Bitacoraacceso b;

        public ListaBitacoraIncidencias(ListaPersonal lp, Incidencias i, Bitacoraacceso b) {
            this.lp = lp;
            this.i = i;
            this.b = b;
        }
    }
}
