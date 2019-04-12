package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Partidas;
import mx.edu.utxj.pye.sgi.entity.pye2.PretechoFinanciero;
import mx.edu.utxj.pye.sgi.entity.pye2.Productos;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAreas;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosPK;
import mx.edu.utxj.pye.sgi.entity.pye2.RecursosActividad;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;

@Named
@ManagedBean
@ViewScoped
public class ControladorPOARecurso implements Serializable {
// Listas de entities 
    @Getter    @Setter    private List<ActividadesPoa> listaActividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> listaEjesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> listaEstrategias = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> recursosActividads = new ArrayList<>();
    @Getter    @Setter    private List<Partidas> partidases = new ArrayList<>();
    @Getter    @Setter    private List<Productos> productoses = new ArrayList<>();
    @Getter    @Setter    private List<ProductosAreas> productosAreases = new ArrayList<>();
    @Getter    @Setter    private List<PretechoFinanciero> pretechoFinancieros = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> recursosActividads2 = new ArrayList<>(),recursosActividads3 = new ArrayList<>(),recursosActividads4 = new ArrayList<>(),recursosActividads5 = new ArrayList<>(),recursosActividadscdh = new ArrayList<>();
// variables de datos Primitivos
    @Getter    @Setter    private String claseP1="",claseP2="",claseP3="",claseP4="",clasePC="",clasePT="";
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Date fechaActual=new Date();   
    @Getter    @Setter    private Double numPm1=0D,numPm2=0D,numPm3=0D,numPm4=0D,numPm5=0D,numPm6=0D,numPm7=0D,numPm8=0D,numPm9=0D,numPm10=0D,numPm11=0D,numPm12=0D;
    @Getter    @Setter    private Double pretecho2000=0D,pretecho3000=0D,pretecho4000=0D,pretecho5000=0D,pretechoCPDD=0D,totalPretecho=0D;
    @Getter    @Setter    private Double totalRecursoActividad = 0D,totalCaptitulos=0D,totalCaptituloPartida = 0D,totalCaptitulo2000 = 0D,totalCaptitulo3000 = 0D,totalCaptitulo4000 = 0D,totalCaptitulo5000 = 0D,totalCaptituloCPDD = 0D;
    @Getter    @Setter    private Boolean contenido = false, alineacionSeleccionada = false,productoSeleccionado=false;
// variables de entities
    @Getter    @Setter    private ActividadesPoa actividadesPoa = new ActividadesPoa();
    @Getter    @Setter    private RecursosActividad recursosActividad = new RecursosActividad();
    @Getter    @Setter    private EjesRegistro ejesRegistro = new EjesRegistro();
    @Getter    @Setter    private Estrategias estrategias = new Estrategias();
    @Getter    @Setter    private Partidas partidas = new Partidas();
    @Getter    @Setter    private ProductosAreas productosAreas = new ProductosAreas();
    @Getter    @Setter    private Productos productos = new Productos();
// Listas de DTO's
    @Getter    @Setter    private List<ActividadesPoa> actividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<RecursosActividad> recursosActividadsP = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> listaEjesRegistrosP = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> listaEstrategiasP = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> listaLineasAccionsP = new ArrayList<>();
    @Getter    @Setter    private List<ejeListaEstrategias> listaEstrategiases = new ArrayList<>();
    @Getter    @Setter    private List<estrategiasListaLineasAccion> listaLineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<lineasAccionListaActividad> accionListaActividads = new ArrayList<>();
    @Getter    @Setter    private List<actividadListaRecursoActividades> listaRecursoActividadeses = new ArrayList<>();
    

    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA pOAUtilidades;

    @PostConstruct
    public void init() {
        System.out.println("inicio"+System.currentTimeMillis());
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        numPm1 = null;        numPm2 = null;        numPm3 = null;        numPm4 = null;        numPm5 = null;        numPm6 = null;
        numPm7 = null;        numPm8 = null;        numPm9 = null;        numPm10 = null;        numPm11 = null;        numPm12 = null;
        alineacionSeleccionada = false;
        consultarListasInit();
        System.out.println("inicio"+System.currentTimeMillis());
    }

    public void consultarListasInit() {
        listaEstrategias.clear();
        listaEjesRegistros.clear();
        listaActividadesPoas.clear();
        partidases.clear();

        partidases.add(new Partidas(Short.parseShort("0"), "Selecciones Uno"));
        if(!poaSelectec.mostrarPartidases(ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()).isEmpty()){
            partidases.addAll(poaSelectec.mostrarPartidases(ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        }
        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
        listaEjesRegistros.add(new EjesRegistro(0, "Selecciones Uno", "Selecciones Uno", "Selecciones Uno", "Selecciones Uno"));
        poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).forEach((t) -> {
            listaEjesRegistros.add(t);
        });

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasReporteArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);

        listaRecursoActividadeses = new ArrayList<>();
        accionListaActividads = new ArrayList<>();
        listaLineasAccions = new ArrayList<>();
        listaEstrategiases = new ArrayList<>();
        
        listaRecursoActividadeses.clear();
        accionListaActividads.clear();
        listaLineasAccions.clear();
        listaEstrategiases.clear();
        
        listaEjesRegistrosP = new ArrayList<>();
        listaEjesRegistrosP.clear();
        listaEjesRegistrosP = poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);
        if (!listaEjesRegistrosP.isEmpty()) {
            listaEjesRegistrosP.forEach((ej) -> {
                listaEstrategiasP = new ArrayList<>();
                listaEstrategiasP.clear();
                listaEstrategiasP = poaSelectec.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                if (!listaEstrategiasP.isEmpty()) {
                    listaEstrategiasP.forEach((es) -> {
                        listaLineasAccionsP = new ArrayList<>();
                        listaLineasAccionsP.clear();
                        listaLineasAccionsP = poaSelectec.mostrarLineasAccionRegistroParametros(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, ej, es);
                        if (!listaLineasAccionsP.isEmpty()) {
                            listaLineasAccionsP.forEach((li) -> {
                                List<CuadroMandoIntegral> cuadro = new ArrayList<>();
                                cuadro = poaSelectec.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ej, es, li);
                                actividadesPoas = new ArrayList<>();
                                actividadesPoas.clear();
                                actividadesPoas = poaSelectec.mostrarActividadesPoaCuadroDeMandoRecurso(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, cuadro.get(0));
                                if (!actividadesPoas.isEmpty()) {
                                    actividadesPoas.forEach((ap) -> {
                                        recursosActividadsP = new ArrayList<>();
                                        recursosActividadsP.clear();
                                        recursosActividadsP = poaSelectec.mostrarRecursosActividadReporte(ap);
                                        listaRecursoActividadeses.add(new actividadListaRecursoActividades(ap, recursosActividadsP));
                                        recursosActividadsP = new ArrayList<>();
                                        recursosActividadsP.clear();
                                    });
                                }
                                accionListaActividads.add(new lineasAccionListaActividad(li, listaRecursoActividadeses));
                                listaRecursoActividadeses = new ArrayList<>();
                                listaRecursoActividadeses.clear();
                            });
                        }
                        listaLineasAccions.add(new estrategiasListaLineasAccion(es, accionListaActividads));
                        accionListaActividads = new ArrayList<>();
                        accionListaActividads.clear();
                    });
                }
                listaEstrategiases.add(new ejeListaEstrategias(ej, listaLineasAccions));
                listaLineasAccions = new ArrayList<>();
                listaLineasAccions.clear();
            });
        }
        
        obtenerPretechos();
        obteneroTotalesCapitulos();
        obteneroTotalesFinales();
    }

    public void resetearValores() {
        System.out.println("resetear"+System.currentTimeMillis());
        actividadesPoa = new ActividadesPoa();
        listaActividadesPoas.clear();
        actividadesPoa = new ActividadesPoa();
        numPm1 = null;        numPm2 = null;        numPm3 = null;        numPm4 = null;        numPm5 = null;        numPm6 = null;        
        numPm7 = null;        numPm8 = null;        numPm9 = null;        numPm10 = null;        numPm11 = null;        numPm12 = null;
        System.out.println("resetear"+System.currentTimeMillis());
    }

    public void consultarListas() {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARecurso.consultarListas(1)");
        listaActividadesPoas = new ArrayList();
        listaActividadesPoas.clear();

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasAreaEjeyEjercicioFiscal(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, ejesRegistro);

        listaRecursoActividadeses = new ArrayList<>();
        accionListaActividads = new ArrayList<>();
        listaLineasAccions = new ArrayList<>();
        listaEstrategiases = new ArrayList<>();
        
        listaRecursoActividadeses.clear();
        accionListaActividads.clear();
        listaLineasAccions.clear();
        listaEstrategiases.clear();

        listaLineasAccionsP = poaSelectec.mostrarLineasAccionRegistroParametros(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, ejesRegistro, estrategias);
        if (!listaLineasAccionsP.isEmpty()) {
            listaLineasAccionsP.forEach((li) -> {
                List<CuadroMandoIntegral> cuadro = new ArrayList<>();
                cuadro = poaSelectec.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ejesRegistro, estrategias, li);
                actividadesPoas = new ArrayList<>();
                actividadesPoas.clear();
                actividadesPoas = poaSelectec.mostrarActividadesPoaCuadroDeMandoRecurso(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, cuadro.get(0));
                if (!actividadesPoas.isEmpty()) {
                    actividadesPoas.forEach((ap) -> {
                        recursosActividadsP = new ArrayList<>();
                        recursosActividadsP.clear();
                        recursosActividadsP = poaSelectec.mostrarRecursosActividadReporte(ap);
                        listaRecursoActividadeses.add(new actividadListaRecursoActividades(ap, recursosActividadsP));
                        recursosActividadsP = new ArrayList<>();
                        recursosActividadsP.clear();
                    });
                }
                accionListaActividads.add(new lineasAccionListaActividad(li, listaRecursoActividadeses));
                listaRecursoActividadeses = new ArrayList<>();
                listaRecursoActividadeses.clear();
            });
        }
        listaLineasAccions.add(new estrategiasListaLineasAccion(estrategias, accionListaActividads));
        accionListaActividads = new ArrayList<>();
        accionListaActividads.clear();

        listaEstrategiases.add(new ejeListaEstrategias(ejesRegistro, listaLineasAccions));
        listaLineasAccions = new ArrayList<>();
        listaLineasAccions.clear();

        obtenerPretechos();
        obteneroTotalesCapitulos();
        obteneroTotalesFinales();
    }

    public void asignarParametrosRegistro(ValueChangeEvent event) {
        if (Short.parseShort(event.getNewValue().toString()) != Short.parseShort("0")) {
            switch (event.getComponent().getId()) {
                case "eje":
                    ejesRegistro = new EjesRegistro();
                    estrategias = new Estrategias();
                    ejesRegistro = poaSelectec.mostrarEjeRegistro(Integer.parseInt(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        listaEstrategias.clear();
                        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        poaSelectec.getEstarategiasPorEje(ejesRegistro,ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()).forEach((t) -> {
                            listaEstrategias.add(t);
                        });
                    }
                    resetearValores();
                    break;
                case "estrategia":
                    estrategias = new Estrategias();
                    estrategias = poaSelectec.mostrarEstrategia(Short.parseShort(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        consultarListas();
                        alineacionSeleccionada = true;
                    }
                    resetearValores();
                    break;
            }
        } else {
            ejesRegistro = new EjesRegistro();
            estrategias = new Estrategias();
            listaEstrategias.clear();
            consultarListasInit();
        }
    }   
   
    public void asignarRecursoProgramados(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "mes1":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPEnero((numPm1 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm1 = null;                    recursosActividad.setRPEnero(Double.parseDouble("0"));                }
                } else {                    numPm1 = null;                    recursosActividad.setRPEnero(Double.parseDouble("0"));                }
                break;
            case "mes2":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPFebero((numPm2 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm2 = null;                    recursosActividad.setRPFebero(Double.parseDouble("0"));                }
                } else {                    numPm2 = null;                    recursosActividad.setRPFebero(Double.parseDouble("0"));                }
                break;
            case "mes3":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPMarzo((numPm3 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm3 = null;                    recursosActividad.setRPMarzo(Double.parseDouble("0"));                }
                } else {                    numPm3 = null;                    recursosActividad.setRPMarzo(Double.parseDouble("0"));                }
                break;
            case "mes4":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPAbril((numPm4 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm4 = null;                    recursosActividad.setRPAbril(Double.parseDouble("0"));                }
                } else {                    numPm4 = null;                    recursosActividad.setRPAbril(Double.parseDouble("0"));                }
                break;
            case "mes5":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPMayo((numPm5 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm5 = null;                    recursosActividad.setRPMayo(Double.parseDouble("0"));                }
                } else {                    numPm5 = null;                    recursosActividad.setRPMayo(Double.parseDouble("0"));                }
                break;
            case "mes6":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPJunio((numPm6 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm6 = null;                    recursosActividad.setRPJunio(Double.parseDouble("0"));                }
                } else {                    numPm6 = null;                    recursosActividad.setRPJunio(Double.parseDouble("0"));                }
                break;
            case "mes7":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPJulio((numPm7 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm7 = null;                    recursosActividad.setRPJulio(Double.parseDouble("0"));                }
                } else {                    numPm7 = null;                    recursosActividad.setRPJulio(Double.parseDouble("0"));                }
                break;
            case "mes8":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPAgosto((numPm8 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm8 = null;                    recursosActividad.setRPAgosto(Double.parseDouble("0"));                }
                } else {                    numPm8 = null;                    recursosActividad.setRPAgosto(Double.parseDouble("0"));                }
                break;
            case "mes9":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPSeptiembre((numPm9 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm9 = null;                    recursosActividad.setRPSeptiembre(Double.parseDouble("0"));                }
                } else {                    numPm9 = null;                    recursosActividad.setRPSeptiembre(Double.parseDouble("0"));                }
                break;
            case "mes10":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPOctubre((numPm10 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm10 = null;                    recursosActividad.setRPOctubre(Double.parseDouble("0"));                }
                } else {                    numPm10 = null;                    recursosActividad.setRPOctubre(Double.parseDouble("0"));                }
                break;
            case "mes11":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPNoviembre((numPm11 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm11 = null;                    recursosActividad.setRPNoviembre(Double.parseDouble("0"));                }
                } else {                    numPm11 = null;                    recursosActividad.setRPNoviembre(Double.parseDouble("0"));                }
                break;
            case "mes12":
                if (event.getNewValue() != null) {
                    if (Double.parseDouble(event.getNewValue().toString()) != 0D) {
                        recursosActividad.setRPDiciembre((numPm12 = Double.parseDouble(event.getNewValue().toString())));
                    } else {                    numPm12 = null;                    recursosActividad.setRPDiciembre(Double.parseDouble("0"));                }
                } else {                    numPm12 = null;                    recursosActividad.setRPDiciembre(Double.parseDouble("0"));                }
                break;
        }
        totalRecursoActividad = recursosActividad.getRPEnero() + recursosActividad.getRPFebero() + recursosActividad.getRPMarzo() + recursosActividad.getRPAbril() + recursosActividad.getRPMayo() + recursosActividad.getRPJunio() + recursosActividad.getRPJulio() + recursosActividad.getRPAgosto() + recursosActividad.getRPSeptiembre() + recursosActividad.getRPOctubre() + recursosActividad.getRPNoviembre() + recursosActividad.getRPDiciembre();
        recursosActividad.setTotal(Double.parseDouble(totalRecursoActividad.toString()));
    }
    
    public void asignarRecursoActividad(ActividadesPoa actividadesPoaRecurso) {
        System.out.println("asignacion"+actividadesPoaRecurso);
        actividadesPoa = new ActividadesPoa();
        actividadesPoa = actividadesPoaRecurso;
        System.out.println("asignacion"+System.currentTimeMillis());
    }

    public void asignaProductos(ValueChangeEvent event2) {
        partidas = new Partidas();
        partidas = poaSelectec.mostrarPartidas(Short.parseShort(String.valueOf(event2.getNewValue().toString())));
        productoses = new ArrayList<>();
        productoses.clear();
        productoses.add(new Productos(new ProductosPK("0", ejercicioFiscal), "Seleccione uno"));
        if(!poaSelectec.mostrarProductoses(ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), partidas).isEmpty()){
            productoses.addAll(poaSelectec.mostrarProductoses(ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), partidas));
        }
    }

    public void asignaRecursoProductos(ValueChangeEvent event2) {
        contenido = false;
        productoSeleccionado = false;
        if (!event2.getNewValue().toString().equals("0")) {

            ProductosPK seleccionado = new ProductosPK();
            seleccionado.setEjercicioFiscal(ejercicioFiscal);
            seleccionado.setProducto(event2.getNewValue().toString());

            productos = new Productos();
            productos = poaSelectec.mostrarProductos(seleccionado);

            productosAreas = new ProductosAreas();
            productosAreas = poaSelectec.mostrarProductosAreas(productos, partidas, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());

            if (!actividadesPoa.getRecursosActividadList().isEmpty()) {
                actividadesPoa.getRecursosActividadList().forEach((r) -> {
                    if (r.getProductoArea().equals(productosAreas)) {
                        contenido = true;
                    }
                });
            }

            if (contenido == true) {
                productos = new Productos();
                productosAreas = new ProductosAreas();
                Messages.addGlobalWarn("El producto ya está asignado a la actividad, favor de seleccionar otro");
            } else {
                productoSeleccionado = true;
            }
        } else {
            productos = new Productos();
            productosAreas = new ProductosAreas();
            productoSeleccionado = false;
        }
    }

    public void agregarRecursoActividad() {
        System.out.println("agregarcion"+System.currentTimeMillis());
        recursosActividad.setActividadPoa(new ActividadesPoa());
        recursosActividad.setProductoArea(new ProductosAreas());

        recursosActividad.setActividadPoa(actividadesPoa);
        recursosActividad.setProductoArea(productosAreas);

        poaSelectec.agregarRecursosActividad(recursosActividad);
        recursosActividad = new RecursosActividad();
       
        resetearValores();
        if (alineacionSeleccionada == false) {
            consultarListasInit();
        } else {
            consultarListas();
        }
        System.out.println("agregacion"+System.currentTimeMillis());
    }

    public void onCellEditProductos(RowEditEvent event) {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARecurso.onCellEditProductos(1)"+event.getObject());
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARecurso.onCellEditProductos(2)"+event.getObject().getClass());
        System.out.println("edicion"+System.currentTimeMillis());
        RecursosActividad modificada = (RecursosActividad) event.getObject();
        totalRecursoActividad = modificada.getRPEnero() + modificada.getRPFebero() + modificada.getRPMarzo() + modificada.getRPAbril() + modificada.getRPMayo() + modificada.getRPJunio() + modificada.getRPJulio() + modificada.getRPAgosto() + modificada.getRPSeptiembre() + modificada.getRPOctubre() + modificada.getRPNoviembre() + modificada.getRPDiciembre();
        modificada.setTotal(totalRecursoActividad);
        poaSelectec.actualizaRecursosActividad(modificada);
        if (alineacionSeleccionada == false) {
            consultarListasInit();
        } else {
            consultarListas();
        }
        System.out.println("edicion"+System.currentTimeMillis());
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("!!Operación cancelada!!");
    }
    
    public void eliminarRecursoActividad(RecursosActividad recursosActividad) {
        System.out.println("eliminacion"+System.currentTimeMillis());
        poaSelectec.eliminarRecursosActividad(recursosActividad);
        if (alineacionSeleccionada == false) {
            consultarListasInit();
        } else {
            consultarListas();
        }
        System.out.println("eliminiacion"+System.currentTimeMillis());
    }
    
    public void obtenerPretechos() {
        pretechoFinancieros.clear();
        pretechoFinancieros = poaSelectec.mostrarPretechoFinancieros(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);
        pretechoFinancieros.forEach((t) -> {
            switch (t.getCapituloTipo().getCapituloTipo()) {
                case 2:
                    pretecho2000 = t.getMonto();
                    break;
                case 3:
                    pretecho3000 = t.getMonto();
                    break;
                case 4:
                    pretecho4000 = t.getMonto();
                    break;
                case 5:
                    pretecho5000 = t.getMonto();
                    break;
                case 6:
                    pretechoCPDD = t.getMonto();
                    break;
            }
        });
        totalPretecho = pretecho2000 + pretecho3000 + pretecho4000 + pretecho5000 + pretechoCPDD;
    }
    
    public void obteneroTotalesCapitulos() {
        recursosActividads2 = new ArrayList<>();        recursosActividads3 = new ArrayList<>();
        recursosActividads4 = new ArrayList<>();        recursosActividads5 = new ArrayList<>();
        recursosActividadscdh = new ArrayList<>();

        recursosActividads2.clear();        recursosActividads3.clear();
        recursosActividads4.clear();        recursosActividads5.clear();
        recursosActividadscdh.clear();

        recursosActividads2=poaSelectec.mostrarRecursosActividad(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, Short.parseShort("2"));
        recursosActividads3=poaSelectec.mostrarRecursosActividad(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, Short.parseShort("3"));
        recursosActividads4=poaSelectec.mostrarRecursosActividad(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, Short.parseShort("4"));
        recursosActividads5=poaSelectec.mostrarRecursosActividad(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, Short.parseShort("5"));
        recursosActividadscdh=poaSelectec.mostrarRecursosActividad(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, Short.parseShort("6"));
        
        totalCaptitulo2000 = 0D;
        totalCaptitulo3000 = 0D;
        totalCaptitulo4000 = 0D;
        totalCaptitulo5000 = 0D;
        totalCaptituloCPDD = 0D;

        if (!recursosActividads2.isEmpty()) {            recursosActividads2.forEach((t) -> {                totalCaptitulo2000 = totalCaptitulo2000 + t.getTotal();            });        }
        if (!recursosActividads3.isEmpty()) {            recursosActividads3.forEach((t) -> {                totalCaptitulo3000 = totalCaptitulo3000 + t.getTotal();            });        }
        if (!recursosActividads4.isEmpty()) {            recursosActividads4.forEach((t) -> {                totalCaptitulo4000 = totalCaptitulo4000 + t.getTotal();            });        }
        if (!recursosActividads5.isEmpty()) {            recursosActividads5.forEach((t) -> {                totalCaptitulo5000 = totalCaptitulo5000 + t.getTotal();            });        }
        if (!recursosActividadscdh.isEmpty()) {          recursosActividadscdh.forEach((t) -> {               totalCaptituloCPDD = totalCaptituloCPDD + t.getTotal();            });        }
    }

    public void obteneroTotalesFinales() {
        totalCaptitulos = totalCaptitulo2000 + totalCaptitulo3000 + totalCaptitulo4000 + totalCaptitulo5000 + totalCaptituloCPDD;
        if (totalCaptitulo2000 < pretecho2000) {            claseP1 = "mayor";        }
        if (totalCaptitulo3000 < pretecho3000) {            claseP2 = "mayor";        }
        if (totalCaptitulo4000 < pretecho4000) {            claseP3 = "mayor";        }
        if (totalCaptitulo5000 < pretecho5000) {            claseP4 = "mayor";        }
        if (totalCaptituloCPDD < pretechoCPDD) {            clasePC = "mayor";        }
        if (totalCaptitulos < totalPretecho) {            clasePT = "mayor";        }
    }
    
    // Presentacion de Recuerso de actividades

    public static class ejeListaEstrategias {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<estrategiasListaLineasAccion> listaLineasAccions;

        public ejeListaEstrategias(EjesRegistro ejeA, List<estrategiasListaLineasAccion> listaLineasAccions) {
            this.ejeA = ejeA;
            this.listaLineasAccions = listaLineasAccions;
        }

    }

    public static class estrategiasListaLineasAccion {

        @Getter        @Setter        private Estrategias etra;
        @Getter        @Setter        private List<lineasAccionListaActividad> listalistaEstrategiaLaAp;

        public estrategiasListaLineasAccion(Estrategias etra, List<lineasAccionListaActividad> listalistaEstrategiaLaAp) {
            this.etra = etra;
            this.listalistaEstrategiaLaAp = listalistaEstrategiaLaAp;
        }

    }

    public static class lineasAccionListaActividad {

        @Getter        @Setter        private LineasAccion lineasAccion;
        @Getter        @Setter        private List<actividadListaRecursoActividades> listaRecursoActividadeses;

        public lineasAccionListaActividad(LineasAccion lineasAccion, List<actividadListaRecursoActividades> listaRecursoActividadeses) {
            this.lineasAccion = lineasAccion;
            this.listaRecursoActividadeses = listaRecursoActividadeses;
        }

    }

    public static class actividadListaRecursoActividades {

        @Getter        @Setter        private ActividadesPoa actividadesPoa1;
        @Getter        @Setter        private List<RecursosActividad> recacts;

        public actividadListaRecursoActividades(ActividadesPoa actividadesPoa1, List<RecursosActividad> recacts) {
            this.actividadesPoa1 = actividadesPoa1;
            this.recacts = recacts;
        }

    }

    public void imprimirValores() {
    }

}
