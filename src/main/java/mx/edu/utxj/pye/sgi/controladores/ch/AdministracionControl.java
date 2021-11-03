package mx.edu.utxj.pye.sgi.controladores.ch;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.annotation.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.PaseListaDoc;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPresupuestacion;
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.Eventos;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreas;
import mx.edu.utxj.pye.sgi.entity.ch.EventosAreasPK;
import mx.edu.utxj.pye.sgi.entity.ch.Incidencias;
import mx.edu.utxj.pye.sgi.entity.ch.MenuDinamico;
import mx.edu.utxj.pye.sgi.entity.ch.Modulosregistro;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.entity.pye2.CapitulosTipos;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosPK;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.sgi.util.UtilidadesCH;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class AdministracionControl implements Serializable {

    private static final long serialVersionUID = 1736039029781733869L;

    @Getter    @Setter    private List<String> estatus = new ArrayList<>();
    
    @Getter    @Setter    private List<String> titulosn1 = new ArrayList<>();
    @Getter    @Setter    private List<String> titulosn2 = new ArrayList<>();
    @Getter    @Setter    private List<String> titulosn3 = new ArrayList<>();
    @Getter    @Setter    private List<String> titulosn4 = new ArrayList<>();
    @Getter    @Setter    private List<String> titulosn5 = new ArrayList<>();
    @Getter    @Setter    private List<String> titulos = new ArrayList<>();
    
    @Getter    @Setter    private List<Incidencias> listaIncidencias = new ArrayList<>();
    @Getter    @Setter    private List<Modulosregistro> modulosregistros = new ArrayList<>();
    @Getter    @Setter    private List<Eventos> eventoses = new ArrayList<>();
    @Getter    @Setter    private List<EventosAreas> eventosesAreases = new ArrayList<>();
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<Procesopoa> procesopoas = new ArrayList<>();
    @Getter    @Setter    private List<Calendarioevaluacionpoa> calendarioevaluacionpoas = new ArrayList<>();
    @Getter    @Setter    private List<ConfiguracionPropiedades> cps = new ArrayList<>();
    @Getter    @Setter    private List<EventosAreaPOA> areaPOAs = new ArrayList<>();
    @Getter    @Setter    private List<PresupuestoPOA> presupuestoPOAs = new ArrayList<>();
    @Getter    @Setter    private List<PretechoFinanciero> p=new ArrayList<>();
    @Getter    @Setter    private List<PretechoFinanciero> pfs = new ArrayList<>();
    @Getter    @Setter    private List<ProductosAreas> productoses = new ArrayList<>();
    @Getter    @Setter    private List<Partidas> partidases = new ArrayList<>();
    @Getter    @Setter    private List<Partidas> partidasesTotales = new ArrayList<>();
    @Getter    @Setter    private List<ProductosAreasAsigados> asigadoses = new ArrayList<>();
    @Getter    @Setter    private List<CapitulosTipos> capitulosTiposes = new ArrayList<>();
    @Getter    @Setter    private List<MenuDinamico> menuDinamicos = new ArrayList<>();
    
    
    @Getter    @Setter    private String titulon1 ="No";
    @Getter    @Setter    private String titulon2 ="No";
    @Getter    @Setter    private String titulon3 ="No";
    @Getter    @Setter    private String titulon4 ="No";
    @Getter    @Setter    private String titulon5 ="No";
    
    @Getter    @Setter    private Calendarioevaluacionpoa c = new Calendarioevaluacionpoa();
    @Getter    @Setter    private MenuDinamico dinamico = new MenuDinamico();
    @Getter    @Setter    private MenuDinamico menuDinamico = new MenuDinamico();
    @Getter    @Setter    private CapitulosTipos capitulosTipos = new CapitulosTipos();
    @Getter    @Setter    private Eventos eventos = new Eventos();
    @Getter    @Setter    private Productos productos = new Productos();
    @Getter    @Setter    private Partidas partidas = new Partidas();
    @Getter    @Setter    private ProductosAreas productosAreas = new ProductosAreas();
    @Getter    @Setter    private PretechoFinanciero financiero= new PretechoFinanciero();
    @Getter    @Setter    private Integer eventoC = 0;
    @Getter    @Setter    private Integer totalProductosR = 0;
    @Getter    @Setter    private Integer i = 0;
    @Getter    @Setter    private Double cp2 = 0D, cp3 = 0D, cp4 = 0D, cpd = 0D, cp2T = 0D, cp3T = 0D, cp4T = 0D, cpdT = 0D;
    @Getter    @Setter    private Short areaC = 0, ejeFiscal = 0, ejeFiscalReferencia = 0,partida = 0;
    @Getter    @Setter    private Date fehDate = new Date();
    @Getter    @Setter    private Boolean nuevoPeriodo = Boolean.FALSE;
    @Getter    @Setter    private Boolean nuevoproducto = Boolean.FALSE;
    @Getter    @Setter    private Part file;
    @Getter    @Setter    private Part fileRegistro;
    @Getter    private String ruta;

    @Getter    @Setter    private EventosAreas editEventosAreas = new EventosAreas();

    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbNotificacionesIncidencias ejbNotificacionesIncidencias;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo areasLogeo;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbUtilidadesCH ejbUtilidadesCH;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador administrador;
    @EJB    private mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal ejbPersonal;
    @EJB    EjbPresupuestacion ejbPresupuestacion;
    @EJB    EjbCarga carga;

    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesCH utilidadesCH;
    @Inject    UtilidadesPOA utilidadesPOA;

    @Inject LogonMB logonMB;
    @Inject ServicioArchivos archivos;
    @Inject    UtilidadesCH uch;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
        cargado = true;
        estatus.clear();
        estatus.add("Aceptado");
        estatus.add("Denegado");
        estatus.add("Pendiente");
        ejeFiscal=controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        mostrarProcesos();    
        mostrarPresupuestos();
        mostrarIncidencias();
        mostrarModulos();
        mostrarConfiguracionProp();
        mostrarPartidas();
        mostrarProductos(Short.parseShort("0"), 1);
        menuDocumentacion();
    }


// -----------------------------------------------------------------------------Busqueda    
    public void mostrarProcesos() {
        try {
            c = new Calendarioevaluacionpoa();
            c = ejbUtilidadesCH.mostrarCalendarioEvaluacion(uch.castearLDaD(LocalDate.now()));

            calendarioevaluacionpoas = new ArrayList<>();
            procesopoas = new ArrayList<>();
            eventoses = new ArrayList<>();
            eventosesAreases = new ArrayList<>();
            areasUniversidads = new ArrayList<>();
            areaPOAs = new ArrayList<>();

            areasUniversidads.clear();
            eventoses.clear();
            eventosesAreases.clear();
            procesopoas.clear();
            areaPOAs.clear();

            eventoses = ejbUtilidadesCH.mostrarEventoses();
            eventosesAreases = ejbUtilidadesCH.mostrarEventosesAreases();
            calendarioevaluacionpoas = ejbUtilidadesCH.mostrarCalendarioevaluacionpoas();
            procesopoas = ejbUtilidadesCH.mostrarProcesopoa();
            procesopoas.forEach((t) -> {
                if (t.getResponsable() != null) {
                    try {
                        areasUniversidads.add(areasLogeo.mostrarAreasUniversidad(t.getArea()));
                    } catch (Throwable ex) {
                        Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                        Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (t.getEvaluacion() == null) {
                    try {                        
                        t.setEvaluacion(new Calendarioevaluacionpoa());
                        t.setEvaluacion(c);
                    } catch (Throwable ex) {
                        Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                        Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564) {
                if (!eventosesAreases.isEmpty()) {
                    eventosesAreases.forEach((t) -> {
                        try {
                            AreasUniversidad au = areasLogeo.mostrarAreasUniversidad(t.getEventosAreasPK().getAreaOperativa());
                            LocalDateTime datetimeLimite = utilidadesCH.castearDaLDT(t.getEventos().getFechaFin()).plusDays(t.getDiasExtra()).plusHours(23).plusMinutes(59).plusSeconds(59);
                            LocalDate dateLimite = utilidadesCH.castearDaLD(t.getEventos().getFechaFin()).plusDays(t.getDiasExtra());
                            LocalDate dateLibera = utilidadesCH.castearDaLD(t.getEventos().getFechaFin());
                            if (dateLimite.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                                dateLibera = dateLibera.plusDays((t.getDiasExtra() - 4));
                            } else {
                                dateLibera = dateLibera.plusDays((t.getDiasExtra() - 6));
                            }
                            Integer minutos = (int) ((utilidadesCH.castearLDTaD(datetimeLimite).getTime() - utilidadesCH.castearLDTaD(LocalDateTime.now()).getTime()) / 60000);
                            Integer diasR = minutos / 1440;
                            Integer horasR = (minutos % 1440);
                            Integer res = (minutos % 60);
                            String mensaje="";
                            if (diasR<0) {
                                    mensaje = "El periodo venció hace " + Math.abs(diasR) + " día(s) " + Math.abs((horasR / 60)) + " hora(s) com " + Math.abs(res) + " minutos";
                            } else {
                                mensaje = "Resta " + diasR + " día(s) " + (horasR / 60) + " hora(s) com " + (res) + " minutos";
                            }
                            areaPOAs.add(new EventosAreaPOA(t, au.getNombre(), t.getEventos().getTipo() + " -- " + t.getEventos().getNombre(), buscarPersonal(au.getResponsable()), utilidadesCH.castearLDaD(dateLibera), utilidadesCH.castearLDaD(dateLimite), mensaje));
                        } catch (Throwable ex) {
                            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }  
            
            capitulosTiposes=ejbPresupuestacion.mostrarCapitulosTiposes();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarPresupuestos() {
        presupuestoPOAs = new ArrayList<>();
        presupuestoPOAs.clear();
        pfs = new ArrayList<>();
        pfs = ejbPresupuestacion.mostrarPretechoFinancierosGeneral(ejeFiscal);
        if (!pfs.isEmpty()) {
            areasUniversidads.forEach((a) -> {
                p = new ArrayList<>();
                p = pfs.stream().filter(t -> t.getArea() == a.getArea()).collect(Collectors.toList());
                if (!p.isEmpty()) {
                    presupuestoPOAs.add(new PresupuestoPOA(a.getArea(),a.getNombre(), buscarPersonal(a.getResponsable()), p.get(0), p.get(1), p.get(2), p.get(3)));
                }
            });
            if (!presupuestoPOAs.isEmpty()) {
                presupuestoPOAs.forEach((t) -> {
                    cp2T = cp2T + t.cap2000.getMonto();
                    cp3T = cp3T + t.cap3000.getMonto();
                    cp4T = cp4T + t.cap4000.getMonto();
                    cpdT = cpdT + t.capdder.getMonto();
                });
                presupuestoPOAs.add(new PresupuestoPOA(Short.valueOf("0"),"Total", "", new PretechoFinanciero(0, Short.valueOf("0"), cp2T), new PretechoFinanciero(0, Short.valueOf("0"), cp3T), new PretechoFinanciero(0, Short.valueOf("0"), cp4T), new PretechoFinanciero(0, Short.valueOf("0"), cpdT)));
            }
        }
    }
    
    public void menuDocumentacion() {
        try {
            menuDinamicos = new ArrayList<>();
            menuDinamicos.clear();
            menuDinamicos = ejbUtilidadesCH.mostrarListaMenuDocumentacion();
            menuDinamicos.forEach((t) -> {
                if(!titulosn1.contains(t.getTituloNivel1())){
                    titulosn1.add(t.getTituloNivel1());
                }
            });
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String buscarPersonal(Integer clave) {
        try {
            Personal p = new Personal();
            if (clave != null) {
                p = ejbPersonal.mostrarPersonalLogeado(clave);
                if (p == null) {
                    return "Nombre del Responsable";
                } else {
                    return p.getNombre();
                }
            } else {
                return "Nombre del Responsable";
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(PaseListaDoc.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public void mostrarConfiguracionProp() {
        try {
            cps = new ArrayList<>();
            cps.clear();

            cps = administrador.buscarConfiguracionPropiedadeses();

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarPartidas() {
        try {
            partidases = new ArrayList<>();
            partidasesTotales = new ArrayList<>();
            partidasesTotales = ejbPresupuestacion.mostrarPartidasesTotales();
            partidases.add(new Partidas(Short.parseShort("0"), "Todos los productos"));
            partidasesTotales.forEach((t) -> {
                if (t.getProductosAreasList().isEmpty()) {
                    partidases.add(t);
                } else {
                    List<ProductosAreas> p = t.getProductosAreasList().stream().filter(pr -> Objects.equals(pr.getProductos().getEjerciciosFiscales().getEjercicioFiscal(), ejeFiscal)).collect(Collectors.toList());
                    if (!p.isEmpty()) {
                        partidases.add(t);
                    }
                }
            });
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mostrarProductos(Short partida, Integer tipo) {
        try {
            productoses = new ArrayList<>();
            if (tipo.equals(1)) {
                productoses = ejbPresupuestacion.mostrarProductosAreases(Short.parseShort("1"), ejeFiscal);
                totalProductosR=productoses.size();
                nuevoproducto=Boolean.FALSE;
            } else {
                productoses = ejbPresupuestacion.mostrarProductosAreasPartidas(ejeFiscal, new Partidas(partida, ""), Short.parseShort("1"));
                nuevoproducto=Boolean.TRUE;
            }
            asigadoses= new ArrayList<>();
            if(!productoses.isEmpty()){
                productoses.forEach((t) -> {
                    asigadoses.add(new ProductosAreasAsigados(t.getProductos(), t.getProductos().getEjerciciosFiscales(), t.getPartida(), t.getCapitulo().getCapituloTipo(),t.getCapitulo()));
                });
            }
            
            if (nuevoproducto) {
                ProductosPK pK = new ProductosPK();
                EjerciciosFiscales ejerciciosFiscales = new EjerciciosFiscales();
                pK.setEjercicioFiscal(ejeFiscal);
                String claveP="";
                if(totalProductosR<=8){
                    claveP=partida+"-000"+(totalProductosR+1);
                }else if(totalProductosR>=9 && totalProductosR<99){
                    claveP=partida+"-00"+(totalProductosR+1);
                }else if(totalProductosR>=99 && totalProductosR<999){
                    claveP=partida+"-0"+(totalProductosR+1);
                }else{
                    claveP=partida+"-"+(totalProductosR+1);
                }
                pK.setProducto(claveP);
                ejerciciosFiscales.setEjercicioFiscal(ejeFiscal);
                ejerciciosFiscales.setAnio(ejeFiscal);

                productos.setProductosPK(new ProductosPK());
                productos.setProductosPK(pK);
                productos.setEjerciciosFiscales(new EjerciciosFiscales());
                productos.setEjerciciosFiscales(ejerciciosFiscales);
                productos = new Productos();
                productos.setProductosPK(pK);
                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.AdministracionControl.mostrarProductos(claveP)"+claveP);

            }
            
            
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarModulos() {
        try {
            modulosregistros = new ArrayList<>();
            modulosregistros.clear();
            List<Modulosregistro> ms = new ArrayList<>();
            ms.clear();

            ms = ejbUtilidadesCH.mostrarModulosregistrosGeneral();
            if (!ms.isEmpty()) {
                if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564) {
                    modulosregistros.addAll(ms);
                } else {
                    ms.forEach((t) -> {
                        if (t.getNombre().equals("Incidencia")) {
                            modulosregistros.add(t);
                        }
                    });
                }
            }

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarIncidencias() {
        try {
            listaIncidencias = new ArrayList<>();
            listaIncidencias.clear();

            List<Incidencias> is = new ArrayList<>();
            is.clear();
            is = ejbNotificacionesIncidencias.mostrarIncidenciasTotales();
            if (!is.isEmpty()) {

                is.forEach((t) -> {
                    if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 564) {
                        listaIncidencias.add(t);
                    } else {
                        if (t.getEstatus().equals("Pendiente")) {
                            listaIncidencias.add(t);
                        }
                    }
                });
            }
            Ajax.update("frmInciGeneral");
            Collections.sort(listaIncidencias, (x, y) -> Short.compare(x.getClavePersonal().getAreaOperativa(), y.getClavePersonal().getAreaOperativa()));
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public String buscarAreaEventosA(Integer area) {
        try {

            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(Short.parseShort(area.toString()));
            return areaU.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
    
    public String buscarCorreoArea(Integer area) {
        try {

            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(Short.parseShort(area.toString()));
            return areaU.getCorreoInstitucional();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }
// -----------------------------------------------------------------------------Creacion   
    public void agreggarEventoArea() {
        try {
            editEventosAreas = new EventosAreas();
            editEventosAreas.setEventos(new Eventos());
            editEventosAreas.setEventosAreasPK(new EventosAreasPK());
            eventoses.forEach((t) -> {
                if (t.getEvento() == eventoC) {
                    eventos = t;
                }
            });
            Integer diasExtra = (int) ((fehDate.getTime() - eventos.getFechaFin().getTime()) / 86400000);

            editEventosAreas.setEventos(new Eventos(eventoC));
            editEventosAreas.setEventosAreasPK(new EventosAreasPK(eventoC, areaC));

            editEventosAreas.setDiasExtra(diasExtra);
            ejbUtilidadesCH.agregarEventosesAreases(editEventosAreas);
            Messages.addGlobalInfo("¡Evento Agregado!");
            mostrarProcesos();
            editEventosAreas = new EventosAreas();
            eventoC = 0;
            areaC = 0;
            Ajax.update("frmInci");

        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agreggarPresupuestoArea() {
        try {
            for (int i = 1; i <= 4; i++) {
                financiero = new PretechoFinanciero();
                financiero.setEjercicioFiscal(new EjerciciosFiscales());
                financiero.setArea(areaC);
                financiero.getEjercicioFiscal().setEjercicioFiscal(ejeFiscal);
                financiero.setCapituloTipo(new CapitulosTipos());
                switch (i) {
                    case 1:financiero.getCapituloTipo().setCapituloTipo(Short.parseShort("2"));financiero.setMonto(cp2);break;
                    case 2:financiero.getCapituloTipo().setCapituloTipo(Short.parseShort("3"));financiero.setMonto(cp3);break;
                    case 3:financiero.getCapituloTipo().setCapituloTipo(Short.parseShort("4"));financiero.setMonto(cp4);break;
                    case 4:financiero.getCapituloTipo().setCapituloTipo(Short.parseShort("6"));financiero.setMonto(cpd);break;
                }
                ejbPresupuestacion.agregarPretechoFinanciero(financiero);
            }
            mostrarProcesos();
            mostrarPresupuestos();
            AreasUniversidad au = areasLogeo.mostrarAreasUniversidad(areaC);
            utilidadesPOA.enviarCorreo("P", "As", Boolean.FALSE, "", au);
            Ajax.update("frmEventosAreas");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agreggarProductos() {
        try {
            ejeFiscalReferencia=ejeFiscal;
            Integer ef = ejeFiscal.intValue() - 1;
            ejeFiscal = Short.parseShort(ef.toString());
//            ejeFiscal= new Short("20");
            mostrarPartidas();
            mostrarProductos(Short.parseShort("0"), 1);
            List<Productos> p=new ArrayList<>();
            List<ProductosAreas> pa=new ArrayList<>();
            if(!asigadoses.isEmpty()){
                asigadoses.forEach((t) -> {
                    productos= new Productos();
                    ProductosPK pK= new ProductosPK();
                    EjerciciosFiscales ejerciciosFiscales =new EjerciciosFiscales();
                    
                    pK.setProducto(t.getProducto().getProductosPK().getProducto());
                    pK.setEjercicioFiscal(ejeFiscalReferencia);
                    
                    ejerciciosFiscales.setEjercicioFiscal(ejeFiscalReferencia);
                    ejerciciosFiscales.setAnio(ejeFiscalReferencia);
                    
                    productos.setProductosPK(new ProductosPK());
                    productos.setProductosPK(pK);
                    productos.setDescripcion(t.getProducto().getDescripcion());
                    productos.setEjerciciosFiscales(new EjerciciosFiscales());
                    productos.setEjerciciosFiscales(ejerciciosFiscales);
                    productos.setUnidadMedida(t.getProducto().getUnidadMedida());
                    productos=ejbPresupuestacion.agregarProductos(productos);
                    procesopoas.forEach((pros) -> {
                        productosAreas = new ProductosAreas();
                        productosAreas.setArea(pros.getArea());
                        productosAreas.setCapitulo(new CapitulosTipos());
                        productosAreas.setCapitulo(t.getTipos());
                        productosAreas.setPartida(new Partidas());
                        productosAreas.setPartida(t.getPartidas());
                        productosAreas.setProductos(new Productos());
                        productosAreas.setProductos(productos);
                        productosAreas= ejbPresupuestacion.agregarProductosAreas(productosAreas);
                    });
                });
            }
            System.out.println("Productos()" + p.size());
            System.out.println("Productos Areas()" + pa.size());
            
            
            ejeFiscal = ejeFiscalReferencia;
            nuevoPeriodo=Boolean.FALSE;
            nuevoproducto=Boolean.FALSE;
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agreggarProductoUnitario() {
        try {
            productos = ejbPresupuestacion.agregarProductos(productos);
            procesopoas.forEach((pros) -> {
                productosAreas = new ProductosAreas();
                productosAreas.setArea(pros.getArea());
                productosAreas.setCapitulo(new CapitulosTipos());
                productosAreas.setCapitulo(capitulosTipos);
                productosAreas.setPartida(new Partidas());
                productosAreas.setPartida(new Partidas(partida, ""));
                productosAreas.setProductos(new Productos());
                productosAreas.setProductos(productos);
                productosAreas = ejbPresupuestacion.agregarProductosAreas(productosAreas);
            });
            productos= new Productos();
            mostrarProductos(partida,2);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarPartidas() {
        try {
            partidas = ejbPresupuestacion.agregarPartidas(partidas);
            mostrarPartidas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasGeneral.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
// -----------------------------------------------------------------------------Edicion    
    public void onRowEditEventos(RowEditEvent event) {
        try {
            Eventos e = (Eventos) event.getObject();
            ejbUtilidadesCH.actualizarEventoses(e);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarProcesos();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onRowEditMesEvaluacion(RowEditEvent event) {
        try {
            Procesopoa p = (Procesopoa) event.getObject();
//            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.AdministracionControl.onRowEditMesEvaluacion()" + p.getEvaluacion().getEvaluacionPOA());
            if (Objects.equals(p.getEvaluacion().getEvaluacionPOA(), c.getEvaluacionPOA())) {
                p.setEvaluacion(new Calendarioevaluacionpoa());
                p.setEvaluacion(null);
            }
//            System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.AdministracionControl.onRowEditMesEvaluacion()");
            ejbUtilidadesCH.actualizarEtapaPOA(p);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarProcesos();
            Ajax.update("frmProcesoPoa");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEdit(RowEditEvent event) {
        try {
            Incidencias incidencias = (Incidencias) event.getObject();
            utilidadesCH.agregaBitacora(controladorEmpleado.getNuevoOBJListaPersonal().getClave(), incidencias.getIncidenciaID().toString(), "Incidencia", "Actualización Admin");
            ejbNotificacionesIncidencias.actualizarIncidencias(incidencias);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarIncidencias();
            Ajax.update("frmInciGeneral");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditModulos(RowEditEvent event) {
        try {
            Modulosregistro m = (Modulosregistro) event.getObject();
            ejbUtilidadesCH.actualizarModulosregistro(m);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarIncidencias();
            Ajax.update("frmModulos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditEventosAreas(RowEditEvent event) {
        try {
            EventosAreaPOA a = (EventosAreaPOA) event.getObject();
            EventosAreas eventosAreas = new EventosAreas();
            eventosAreas.setEventos(new Eventos());
            eventosAreas.setEventosAreasPK(new EventosAreasPK());
            
            Integer diasExtra=(int) ((a.getEvento().getEventos().getFechaFin().getTime()- a.getFechaLimiteProceso().getTime()) / 86400000);
            eventosAreas = a.getEvento();
            eventosAreas.setDiasExtra(Math.abs(diasExtra));            
            ejbUtilidadesCH.actualizarEventosesAreases(eventosAreas);
            Messages.addGlobalInfo("¡Operación exitosa!");
            mostrarProcesos();
            Ajax.update("frmEventosAreas");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditConfProp(RowEditEvent event) {
        try {
            ConfiguracionPropiedades cp = (ConfiguracionPropiedades) event.getObject();
            cp=administrador.actualizarConfiguracionPropiedades(cp);
            Messages.addGlobalInfo("¡Operación exitosa! "+cp.getClave());
            mostrarConfiguracionProp();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPresupuesto(RowEditEvent event) {
        try {
            PresupuestoPOA cp = (PresupuestoPOA) event.getObject();
            ejbPresupuestacion.actualizaPretechoFinanciero(cp.getCap2000());
            ejbPresupuestacion.actualizaPretechoFinanciero(cp.getCap3000());
            ejbPresupuestacion.actualizaPretechoFinanciero(cp.getCap4000());
            ejbPresupuestacion.actualizaPretechoFinanciero(cp.getCapdder());    
            Messages.addGlobalInfo("¡Operación exitosa! Presupuesto actualizado para el área: "+cp.getArea());
            mostrarPresupuestos();
            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(cp.getAreaID());
            utilidadesPOA.enviarCorreo("P", "Ac", Boolean.FALSE, "", areaU);            
            Ajax.update("frmEventosAreas");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditProductos(RowEditEvent event) {
        try {
            ProductosAreasAsigados cp = (ProductosAreasAsigados) event.getObject();
            Productos p = cp.getProducto();
            p = ejbPresupuestacion.actualizarProductos(p);
            p.getProductosAreasList().forEach((pros) -> {
                ProductosAreas pa = new ProductosAreas();
                pa = pros;
                pa.setCapitulo(new CapitulosTipos());
                pa.setCapitulo(new CapitulosTipos(cp.getCt(), "", ""));
                pa = ejbPresupuestacion.actualizarProductosAreas(pa);
            });
            productos = new Productos();
            mostrarProductos(partida, 2);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void onRowEditPartidas(RowEditEvent event) {
        try {
            Partidas cp = (Partidas) event.getObject();
            cp = ejbPresupuestacion.actualizarPartidas(cp);
            mostrarPartidas();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalInfo("¡Operación cancelada!");
    }
    
// -----------------------------------------------------------------------------Eliminacion
    public void eliminarEventoArea(EventosAreaPOA ea) {
        try {
            ejbUtilidadesCH.eliminarEventosesEventosAreas(ea.getEvento());
            mostrarProcesos();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void eliminarConfiguracionProp(ConfiguracionPropiedades cp) {
        try {
            administrador.eliminarConfiguracionPropiedades(cp);
            mostrarConfiguracionProp();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void eliminarPresuPuesto(PresupuestoPOA cp) {
        try {
            ejbPresupuestacion.eliminarPretechoFinanciero(cp.getCap2000());
            ejbPresupuestacion.eliminarPretechoFinanciero(cp.getCap3000());
            ejbPresupuestacion.eliminarPretechoFinanciero(cp.getCap4000());
            ejbPresupuestacion.eliminarPretechoFinanciero(cp.getCapdder());
            mostrarProcesos();
            mostrarPresupuestos();
            AreasUniversidad areaU = new AreasUniversidad();
            areaU = areasLogeo.mostrarAreasUniversidad(cp.getAreaID());
            utilidadesPOA.enviarCorreo("P", "El", Boolean.FALSE, "", areaU);
            Ajax.update("frmEventosAreas");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void onRowEditMenuDocumentacion(RowEditEvent event) {
        try {
            MenuDinamico e = (MenuDinamico) event.getObject();
            ejbUtilidadesCH.actualizaMenuDocumentacion(e);
            Messages.addGlobalInfo("¡Operación exitosa!");
            menuDocumentacion();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(AdministracionControl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarDocumentoconsulta() {
        try {
            String enlaceNuevo=cargaArchivo(dinamico.getEnlace());
            dinamico.setEnlace(enlaceNuevo);
            dinamico =ejbUtilidadesCH.actualizaMenuDocumentacion(dinamico);
            menuDocumentacion();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void agregarDocumentoconsultaVeda() {
        try {
            String enlaceNuevo=cargaArchivo(dinamico.getEnlaceVedaElectoral());
            dinamico.setEnlaceVedaElectoral(enlaceNuevo);
            dinamico =ejbUtilidadesCH.actualizaMenuDocumentacion(dinamico);
            menuDocumentacion();
            Ajax.update("frmEventos");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String cargaArchivo(String archivoElimindo) {
        List<String> carpetas = new ArrayList<>();
        List<String> rutaG = new ArrayList<>();
        String[] parts = archivoElimindo.split("/");
        if (archivoElimindo.length() != 0) {
            archivos.eliminarArchivo("C:" + File.separator + "archivos" + File.separator + archivoElimindo);
        }
        carpetas = Arrays.asList(parts);
        System.out.println("carpetas= " + carpetas);
        String cadenaRuta = "";
        for (int j = 0; j < (carpetas.size() - 1); j++) {
            cadenaRuta = cadenaRuta + File.separator + carpetas.get(j);
        }

        System.out.println("cadenaRuta= " + cadenaRuta);

        ruta = carga.subirEvidenciaPOA(file, new File(cadenaRuta));

        System.out.println("ruta= " + ruta);
        String[] parts2 = ruta.split("archivos");

        rutaG = Arrays.asList(parts2);
        
        System.out.println("parts2= " + rutaG);

        return rutaG.get(rutaG.size()-1);
    }

    public void eliminarProductos(ProductosAreasAsigados cp) {
        try {
            Productos p = cp.getProducto();
            List<ProductosAreas> pas = p.getProductosAreasList();
            i = 0;
            p.getProductosAreasList().forEach((pros) -> {
                System.out.println("mx.edu.utxj.pye.sgi.controladores.ch.AdministracionControl.eliminarProductos()"+pas.size());
                ProductosAreas pa = pas.get(i);
                i++;
            });
            ejbPresupuestacion.eliminarProductos(p);
            productos= new Productos();
            mostrarProductos(partida,2);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIncidenciasPersonal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarPlantillaCatalogos() throws IOException, Throwable{
        File f = new File(ejbPresupuestacion.getReportePresupuestoPOA(ejeFiscal));
        Faces.sendFile(f, true);
    }
// -----------------------------------------------------------------------------Utilidades
    public void imprimirValores() {
    }

    public void numeroAnioAsiganado(ValueChangeEvent event) {
        ejeFiscal = Short.parseShort(event.getNewValue().toString());
        mostrarProcesos();
        mostrarPresupuestos();
        Ajax.update("frmEventosAreas");
    }
    
    public void numeroProcesoAsiganado(ValueChangeEvent event) {
        ejeFiscal = Short.parseShort(event.getNewValue().toString());
        mostrarPartidas();
        mostrarProductos(Short.parseShort("0"),1);
        if(asigadoses.isEmpty()){
            nuevoPeriodo=Boolean.TRUE;
        }
    }
    public void numeroProductosPorPartida(ValueChangeEvent event) {        
        partida = Short.parseShort(event.getNewValue().toString());
        mostrarProductos(partida,2);
    }

    public static class EventosAreaPOA {

        @Getter        @Setter        private EventosAreas evento;
        @Getter        @Setter        private String area;
        @Getter        @Setter        private String proceso;
        @Getter        @Setter        private String responsable;
        @Getter        @Setter        private Date fecaValidacion;
        @Getter        @Setter        private Date fechaLimiteProceso;
        @Getter        @Setter        private String diasrestantes;

        public EventosAreaPOA(EventosAreas evento, String area, String proceso, String responsable, Date fecaValidacion, Date fechaLimiteProceso, String diasrestantes) {
            this.evento = evento;
            this.area = area;
            this.proceso = proceso;
            this.responsable = responsable;
            this.fecaValidacion = fecaValidacion;
            this.fechaLimiteProceso = fechaLimiteProceso;
            this.diasrestantes = diasrestantes;
        }
    }
    
    public static class PresupuestoPOA {
        
        @Getter        @Setter        private Short areaID;
        @Getter        @Setter        private String area;
        @Getter        @Setter        private String responsable;
        @Getter        @Setter        private PretechoFinanciero cap2000;
        @Getter        @Setter        private PretechoFinanciero cap3000;        
        @Getter        @Setter        private PretechoFinanciero cap4000;
        @Getter        @Setter        private PretechoFinanciero capdder;

        public PresupuestoPOA(Short areaID, String area, String responsable, PretechoFinanciero cap2000, PretechoFinanciero cap3000, PretechoFinanciero cap4000, PretechoFinanciero capdder) {
            this.areaID = areaID;
            this.area = area;
            this.responsable = responsable;
            this.cap2000 = cap2000;
            this.cap3000 = cap3000;
            this.cap4000 = cap4000;
            this.capdder = capdder;
        }        
    }
    
    public static class ProductosAreasAsigados {
        
        @Getter        @Setter        private Productos producto;
        @Getter        @Setter        private EjerciciosFiscales fiscales;
        @Getter        @Setter        private Partidas partidas;
        @Getter        @Setter        private Short ct;
        @Getter        @Setter        private CapitulosTipos tipos;

        public ProductosAreasAsigados(Productos producto, EjerciciosFiscales fiscales, Partidas partidas, Short ct, CapitulosTipos tipos) {
            this.producto = producto;
            this.fiscales = fiscales;
            this.partidas = partidas;
            this.ct = ct;
            this.tipos = tipos;
        }
    }
    
    public void asignaTitulo(ValueChangeEvent event) {
        String texto = "";
        System.out.println("getId "+event.getComponent().getId());
        if (null != event.getComponent().getId()) {
            if(event.getComponent().getId().contains("tn")){
                texto = event.getNewValue().toString();
                if (texto.equals("Nuevo") || texto.equals("No")) {
                    switch (event.getComponent().getId()) {
                        case "tn1":reseteo(1);break;
                        case "tn2":reseteo(2);break;
                        case "tn3":reseteo(3);break;
                        case "tn4":reseteo(4);break;
                        case "tn5":reseteo(5);break;
                    }
                }else{
                    switch (event.getComponent().getId()) {
                        case "tn1":titulosn2 = consultaTitulos(1, texto);break;
                        case "tn2":titulosn3 = consultaTitulos(2, texto);break;
                        case "tn3":titulosn4 = consultaTitulos(3, texto);break;
                        case "tn4":titulosn5 = consultaTitulos(4, texto);break;
                    }
                }
                switch (event.getComponent().getId()) {
                    case "tn1N":menuDinamico.setTituloNivel1(texto);break;
                    case "tn2N":menuDinamico.setTituloNivel2(texto);break;
                    case "tn3N":menuDinamico.setTitulonivel3(texto);break;
                    case "tn4N":menuDinamico.setTitulonivel4(texto);break;
                    case "tn5N":menuDinamico.setTitulonivel5(texto);break;
                }
            }
        }
        System.out.println("----------------------------------------------");
        System.out.println("N1 "+titulon1);
        System.out.println("N1 "+menuDinamico.getTituloNivel1());
        System.out.println("N2 "+titulon2);
        System.out.println("N2 "+menuDinamico.getTituloNivel2());
        System.out.println("N3 "+titulon3);
        System.out.println("N3 "+menuDinamico.getTitulonivel3());
        System.out.println("N4 "+titulon4);
        System.out.println("N4 "+menuDinamico.getTitulonivel4());
        System.out.println("N5 "+titulon5);
        System.out.println("N5 "+menuDinamico.getTitulonivel5());
    }
    
    public void reseteo(Integer nivel) {
        switch (nivel) {
            case 1:
                menuDinamico.setTituloNivel1("");
                titulosn2 = new ArrayList<>();
                titulon2 = "No";
            case 2:
                menuDinamico.setTituloNivel2("");
                titulosn3 = new ArrayList<>();
                titulon3 = "No";
            case 3:
                menuDinamico.setTitulonivel3("");
                titulosn4 = new ArrayList<>();
                titulon4 = "No";
            case 4:
                menuDinamico.setTitulonivel4("");
                titulosn5 = new ArrayList<>();
                titulon5 = "No";
            case 5:
                menuDinamico.setTitulonivel5("");
                break;
        }
    }
    
    public List<String> consultaTitulos(Integer nivel, String titulo) {
        titulos = new ArrayList<>();
        List<MenuDinamico> filtro = new ArrayList<>();
        switch (nivel) {
            case 1:
                filtro = ejbUtilidadesCH.mostrarListaTitulosMenu(controladorEmpleado.getNuevoOBJListaPersonal(), 2,titulo, "Trabajador");
                if(!filtro.isEmpty()){filtro.forEach((t) -> {titulos.add(t.getTituloNivel2());});}else{titulos= new ArrayList<>();}
                break;
            case 2:
                filtro = ejbUtilidadesCH.mostrarListaTitulosMenu(controladorEmpleado.getNuevoOBJListaPersonal(), 3,titulo, "Trabajador");
                if(!filtro.isEmpty()){filtro.forEach((t) -> {titulos.add(t.getTitulonivel3());});}else{titulos= new ArrayList<>();}
                break;
            case 3:
                filtro = ejbUtilidadesCH.mostrarListaTitulosMenu(controladorEmpleado.getNuevoOBJListaPersonal(), 4,titulo, "Trabajador");
                if(!filtro.isEmpty()){filtro.forEach((t) -> {titulos.add(t.getTitulonivel4());});}else{titulos= new ArrayList<>();}
                break;
            case 4:
                filtro = ejbUtilidadesCH.mostrarListaTitulosMenu(controladorEmpleado.getNuevoOBJListaPersonal(), 5,titulo, "Trabajador");
                if(!filtro.isEmpty()){filtro.forEach((t) -> {titulos.add(t.getTitulonivel5());});}else{titulos= new ArrayList<>();}
                break;
        }
        return titulos;
    }
    
    public void creaApartadoMenu() {
        menuDinamico.setEncabezado("Documentación");
        menuDinamico.setActividades("-1-2-3-4-");
        menuDinamico.setEstatus("Nuevo");
        menuDinamico.setTipoUsuario("Trabajador");
        menuDinamico.setActivo(Boolean.TRUE);
        menuDinamico.setTipoenlace("documento");
        
        System.out.println("N1 "+menuDinamico.getTituloNivel1());
        System.out.println("N2 "+menuDinamico.getTituloNivel2());
        System.out.println("N3 "+menuDinamico.getTitulonivel3());
        System.out.println("N4 "+menuDinamico.getTitulonivel4());
        System.out.println("N5 "+menuDinamico.getTitulonivel5());
        
        System.out.println("Enl "+menuDinamico.getEnlace());
    }
  
    public void agregarDocumentoConsulta() {
        String rutaG="C:" + File.separator + "archivos"+ File.separator;
        System.out.println(rutaG);
        if (menuDinamico.getTituloNivel1().length() != 0) {
            rutaG = rutaG  + menuDinamico.getTituloNivel1() + File.separator;
        }
        if (menuDinamico.getTituloNivel2().length() != 0) {
            rutaG = rutaG + menuDinamico.getTituloNivel2() + File.separator;
        }
        if (menuDinamico.getTitulonivel3().length() != 0) {
            rutaG = rutaG + menuDinamico.getTitulonivel3() + File.separator;
        }
        if (menuDinamico.getTitulonivel4().length() != 0) {
            rutaG = rutaG + menuDinamico.getTitulonivel4() + File.separator;
        }
        if (menuDinamico.getTitulonivel5().length() != 0) {
            rutaG = rutaG + menuDinamico.getTitulonivel5() + File.separator;
        }
        System.out.println(rutaG);
        String enlaceNuevo = cargaArchivo(rutaG);
        menuDinamico.setEnlace(enlaceNuevo);
    }
}
