package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPresupuestacion;
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
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.primefaces.event.RowEditEvent;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.util.Faces;

@Named
@ManagedBean
@ViewScoped
public class AreaPoaPresupuestacion implements Serializable {
// Listas de entitys desde BD 
    @Getter    @Setter    private List<Estrategias> consultaListaEstrategias = new ArrayList<>();
    @Getter    @Setter    private List<Partidas> consultaListaPartidas = new ArrayList<>();
    @Getter    @Setter    private List<Productos> consltaListaProductos = new ArrayList<>();
    @Getter    @Setter    private List<PretechoFinanciero> consltaListaPretechoFinancieros = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> consultaListaEjesRegistros = new ArrayList<>();   
    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads=new ArrayList<>();    
// Listas de entitys
    @Getter    @Setter    private List<ActividadListaRecursoActividades> listaRecursoActividadeses = new ArrayList<>(); 
    
// Listas de DTO's
    @Getter    @Setter    private List<EjeListaEstrategias> listaEstrategiases = new ArrayList<>();    
    @Getter    @Setter    private List<EstrategiasListaLineasAccion> listaLineasAccions = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccionListaActividad> accionListaActividads = new ArrayList<>();
    
// Objetos Entitys    
    @Getter    @Setter    private VistaRecurso capitulo2m,capitulo3m,capitulo4m,capitulo5m,capituloCD,capituloTT;
    @Getter    @Setter    private ActividadesPoa actividadesPoa = new ActividadesPoa();
    @Getter    @Setter    private RecursosActividad recursosActividad = new RecursosActividad();
    @Getter    @Setter    private EjesRegistro ejesRegistro = new EjesRegistro();
    @Getter    @Setter    private Estrategias estrategias = new Estrategias();
    @Getter    @Setter    private ProductosAreas productosAreas = new ProductosAreas();
    @Getter    @Setter    private Partidas partidas = new Partidas();
    @Getter    @Setter    private Productos productos = new Productos();
    
// Objetos primitivos    
    @Getter    @Setter    private Short ejercicioFiscal = 0; 
    @Getter    @Setter    private Short claveArea = 0;
    @Getter    @Setter    private Double numPm1=0D,numPm2=0D,numPm3=0D,numPm4=0D,numPm5=0D,numPm6=0D,numPm7=0D,numPm8=0D,numPm9=0D,numPm10=0D,numPm11=0D,numPm12=0D;
    @Getter    @Setter    private Double totalRecursoActividad = 0D, total=0D;
    @Getter    @Setter    private Boolean contenido = false, alineacionSeleccionada = false,productoSeleccionado=false;

    @EJB    EjbCatalogosPoa catalogos;
    @EJB    EjbPresupuestacion presupuestacion;
    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo ejbAreasLogeo;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA pOAUtilidades;

    

@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;


@PostConstruct
    public void init() {
 if(!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) return;
 cargado = true;
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        numPm1 = null;        numPm2 = null;        numPm3 = null;        numPm4 = null;        numPm5 = null;        numPm6 = null;
        numPm7 = null;        numPm8 = null;        numPm9 = null;        numPm10 = null;        numPm11 = null;        numPm12 = null;
        alineacionSeleccionada = false;
        consultarCatalogos();
        consultrAreasEvaluacion();
    }
    
//////////////////////////////////////////////////////////////////////////////// consulta de listas
    public void consultrAreasEvaluacion() {
        ejercicioFiscal=controladorEmpleado.getEf().getEjercicioFiscal();
        areasUniversidads = new ArrayList<>();
        controladorEmpleado.getProcesopoas().forEach((t) -> {
            try {
                areasUniversidads.add(ejbAreasLogeo.mostrarAreasUniversidad(t.getArea()));
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
    
    public void consultarCatalogos() {    
        consultaListaPartidas.clear();
        consultaListaEstrategias.clear();
        consultaListaEjesRegistros.clear();        
        consltaListaProductos.clear();
        
        claveArea = controladorEmpleado.getProcesopoa().getArea();
        
        consltaListaProductos.add(new Productos(new ProductosPK("0", ejercicioFiscal), "Seleccione uno"));
        
        consultaListaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
        consultaListaPartidas.add(new Partidas(Short.parseShort("0"), "Selecciones Uno"));
        if(!presupuestacion.mostrarPartidases(ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea()).isEmpty()){
            consultaListaPartidas.addAll(presupuestacion.mostrarPartidases(ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea()));
        }
        consultaListaEjesRegistros.add(new EjesRegistro(0, "Selecciones Uno", "Selecciones Uno", "Selecciones Uno", "Selecciones Uno"));
        consultaListaEjesRegistros.addAll(catalogos.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal));
        consultarPOARecurso();
    }
    
    public void consultarPOARecurso() {

        listaEstrategiases = new ArrayList<>();
        listaEstrategiases.clear();

        List<EjesRegistro> ers = new ArrayList<>();
        ers.clear();
        ers = catalogos.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        if (!ers.isEmpty()) {
            ers.forEach((ej) -> {
                List<Estrategias> estrategiases = new ArrayList<>();
                estrategiases.clear();
                estrategiases = catalogos.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea());
                if (!estrategiases.isEmpty()) {
                    estrategiases.forEach((es) -> {
                        List<LineasAccion> lineasAccions = new ArrayList<>();
                        lineasAccions.clear();
                        lineasAccions = catalogos.mostrarLineasAccionRegistroParametros(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, ej, es);
                        if (!lineasAccions.isEmpty()) {
                            lineasAccions.forEach((li) -> {
                                List<CuadroMandoIntegral> cuadro = new ArrayList<>();
                                cuadro = catalogos.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ej, es, li);
                                List<ActividadesPoa> actividadesPoas = new ArrayList<>();
                                actividadesPoas.clear();
                                actividadesPoas = presupuestacion.mostrarActividadesPoaCuadroDeMandoRecurso(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, cuadro.get(0));
                                if (!actividadesPoas.isEmpty()) {
                                    actividadesPoas.forEach((ap) -> {
                                        List<RecursosActividad> recursosActividadsP = new ArrayList<>();
                                        recursosActividadsP.clear();
                                        recursosActividadsP = presupuestacion.mostrarRecursosActividadReporte(ap);
                                        listaRecursoActividadeses.add(new ActividadListaRecursoActividades(ap, recursosActividadsP));
                                    });
                                }
                                accionListaActividads.add(new LineasAccionListaActividad(li, listaRecursoActividadeses));
                                listaRecursoActividadeses = new ArrayList<>();
                                listaRecursoActividadeses.clear();
                            });
                        }
                        listaLineasAccions.add(new EstrategiasListaLineasAccion(es, accionListaActividads));
                        accionListaActividads = new ArrayList<>();
                        accionListaActividads.clear();
                    });
                }
                listaEstrategiases.add(new EjeListaEstrategias(ej, listaLineasAccions));
                listaLineasAccions = new ArrayList<>();
                listaLineasAccions.clear();
            });
        }
        obtenerPretechos();
    }

    public void consultarListas() {
        listaRecursoActividadeses = new ArrayList<>();
        accionListaActividads = new ArrayList<>();
        listaLineasAccions = new ArrayList<>();
        listaEstrategiases = new ArrayList<>();

        listaRecursoActividadeses.clear();
        accionListaActividads.clear();
        listaLineasAccions.clear();
        listaEstrategiases.clear();

        List<LineasAccion> listaLineasAccionsP = catalogos.mostrarLineasAccionRegistroParametros(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, ejesRegistro, estrategias);
        if (!listaLineasAccionsP.isEmpty()) {
            listaLineasAccionsP.forEach((li) -> {
                List<CuadroMandoIntegral> cuadro = new ArrayList<>();
                cuadro = catalogos.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ejesRegistro, estrategias, li);
                List<ActividadesPoa> actividadesPoas = new ArrayList<>();
                actividadesPoas.clear();
                actividadesPoas = presupuestacion.mostrarActividadesPoaCuadroDeMandoRecurso(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, cuadro.get(0));
                if (!actividadesPoas.isEmpty()) {
                    actividadesPoas.forEach((ap) -> {
                        List<RecursosActividad> recursosActividadsP = new ArrayList<>();
                        recursosActividadsP.clear();
                        recursosActividadsP = presupuestacion.mostrarRecursosActividadReporte(ap);
                        listaRecursoActividadeses.add(new ActividadListaRecursoActividades(ap, recursosActividadsP));
                        recursosActividadsP = new ArrayList<>();
                        recursosActividadsP.clear();
                    });
                }
                accionListaActividads.add(new LineasAccionListaActividad(li, listaRecursoActividadeses));
                listaRecursoActividadeses = new ArrayList<>();
                listaRecursoActividadeses.clear();
            });
        }
        listaLineasAccions.add(new EstrategiasListaLineasAccion(estrategias, accionListaActividads));
        accionListaActividads = new ArrayList<>();
        accionListaActividads.clear();

        listaEstrategiases.add(new EjeListaEstrategias(ejesRegistro, listaLineasAccions));
        listaLineasAccions = new ArrayList<>();
        listaLineasAccions.clear();

        obtenerPretechos();
    }
    
    public void asignarParametrosRegistro(ValueChangeEvent event) {
        if (Short.parseShort(event.getNewValue().toString()) != Short.parseShort("0")) {
            switch (event.getComponent().getId()) {
                case "eje":
                    ejesRegistro = new EjesRegistro();
                    estrategias = new Estrategias();
                    ejesRegistro = catalogos.mostrarEjeRegistro(Integer.parseInt(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        consultaListaEstrategias.clear();
                        consultaListaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        consultaListaEstrategias.addAll(catalogos.getEstarategiasPorEje(ejesRegistro,ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea()));
                    }
                    resetearValores();
                    break;
                case "estrategia":
                    estrategias = new Estrategias();
                    estrategias = catalogos.mostrarEstrategia(Short.parseShort(event.getNewValue().toString()));
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
            consultaListaEstrategias.clear();
            consultarCatalogos();
        }
    }   
    
    public void asignarAreaEvaluada(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "area":
                controladorEmpleado.setProcesopoa(new Procesopoa());
                controladorEmpleado.getProcesopoas().forEach((t) -> {
                    if (t.getArea() == Short.parseShort(event.getNewValue().toString())) {
                        controladorEmpleado.setProcesopoa(t);
                        claveArea=Short.parseShort(event.getNewValue().toString());
                    }
                });
                break;
        }
        Faces.refresh();
    }
    
//////////////////////////////////////////////////////////////////////////////// Agregar Justificacion    
    public void asignarJustificaionActividad(ActividadesPoa actividadesPoaRecurso) {
        actividadesPoa = new ActividadesPoa();
        actividadesPoa = actividadesPoaRecurso;
        Ajax.oncomplete("PF('delRegistroJustificacion').show()");
    }
    public void actualizarNuavActividad() {
        actividadesPoa = ejbRegistroActividades.actualizaActividadesPoa(actividadesPoa);
        if (alineacionSeleccionada == false) {
            consultarCatalogos();
        } else {
            consultarListas();
        }
    }
//////////////////////////////////////////////////////////////////////////////// Agregar recurso
    public void asignarRecursoActividad(ActividadesPoa actividadesPoaRecurso) {
        actividadesPoa = new ActividadesPoa();
        actividadesPoa = actividadesPoaRecurso;
        partidas= new Partidas();
        Ajax.oncomplete("PF('delRegistroProdustos').show()");
    }

    public void agregarRecursoActividad() {
        recursosActividad.setActividadPoa(new ActividadesPoa());
        recursosActividad.setProductoArea(new ProductosAreas());

        recursosActividad.setActividadPoa(actividadesPoa);
        recursosActividad.setProductoArea(productosAreas);

        presupuestacion.agregarRecursosActividad(recursosActividad);
        recursosActividad = new RecursosActividad();

        resetearValores();
        if (alineacionSeleccionada == false) {
            consultarCatalogos();
        } else {
            consultarListas();
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
    
    public void asignaProductos(ValueChangeEvent event2) {
        partidas = new Partidas();
        partidas = presupuestacion.mostrarPartidas(Short.parseShort(String.valueOf(event2.getNewValue().toString())));
        consltaListaProductos = new ArrayList<>();
        consltaListaProductos.clear();
        consltaListaProductos.add(new Productos(new ProductosPK("0", ejercicioFiscal), "Seleccione uno"));
        if(!presupuestacion.mostrarProductoses(ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea(), partidas).isEmpty()){
            consltaListaProductos.addAll(presupuestacion.mostrarProductoses(ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea(), partidas));
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
            productos = presupuestacion.mostrarProductos(seleccionado);

            productosAreas = new ProductosAreas();
            productosAreas = presupuestacion.mostrarProductosAreas(productos, partidas, controladorEmpleado.getProcesopoa().getArea());

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
//////////////////////////////////////////////////////////////////////////////// Actualizar recurso
    public void onCellEditProductos(RowEditEvent event) {
        RecursosActividad modificada = (RecursosActividad) event.getObject();
        totalRecursoActividad = modificada.getRPEnero() + modificada.getRPFebero() + modificada.getRPMarzo() + modificada.getRPAbril() + modificada.getRPMayo() + modificada.getRPJunio() + modificada.getRPJulio() + modificada.getRPAgosto() + modificada.getRPSeptiembre() + modificada.getRPOctubre() + modificada.getRPNoviembre() + modificada.getRPDiciembre();
        modificada.setTotal(totalRecursoActividad);
        presupuestacion.actualizaRecursosActividad(modificada);
        if (alineacionSeleccionada == false) {
            consultarCatalogos();
        } else {
            consultarListas();
        }
    }
    
    public void onRowCancel(RowEditEvent event) {
        Messages.addGlobalWarn("!!Operación cancelada!!");
    }
//////////////////////////////////////////////////////////////////////////////// Eliminar recurso
    public void eliminarRecursoActividad(RecursosActividad recursosActividad) {
        presupuestacion.eliminarRecursosActividad(recursosActividad);
        if (alineacionSeleccionada == false) {
            consultarCatalogos();
        } else {
            consultarListas();
        }
    }

//////////////////////////////////////////////////////////////////////////////// Utilidades    
    public void obtenerPretechos() {        
        capitulo2m = new VistaRecurso(0D, 0D, 0D, "#000000",false);
        capitulo3m = new VistaRecurso(0D, 0D, 0D, "#000000",false);        
        capitulo4m = new VistaRecurso(0D, 0D, 0D, "#000000",false);
        capitulo5m = new VistaRecurso(0D, 0D, 0D, "#000000",false);        
        capituloCD = new VistaRecurso(0D, 0D, 0D, "#000000",false);        
        capituloTT = new VistaRecurso(0D, 0D, 0D, "#000000",false);
        
        consltaListaPretechoFinancieros.clear();
        consltaListaPretechoFinancieros = presupuestacion.mostrarPretechoFinancieros(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        consltaListaPretechoFinancieros.forEach((t) -> {
            switch (t.getCapituloTipo().getCapituloTipo()) {
                case 2:
                    capitulo2m.setTotalPretecho(t.getMonto());
                    break;
                case 3:
                    capitulo3m.setTotalPretecho(t.getMonto());
                    break;
                case 4:
                    capitulo4m.setTotalPretecho(t.getMonto());
                    break;
                case 5:
                    capitulo5m.setTotalPretecho(t.getMonto());
                    break;
                case 6:
                    capituloCD.setTotalPretecho(t.getMonto());
                    break;
            }
        });
        capitulo2m.setTotalProgramado(totalProgrado(presupuestacion.mostrarRecursosActividad(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, Short.parseShort("2"))));
        capitulo3m.setTotalProgramado(totalProgrado(presupuestacion.mostrarRecursosActividad(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, Short.parseShort("3"))));
        capitulo4m.setTotalProgramado(totalProgrado(presupuestacion.mostrarRecursosActividad(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, Short.parseShort("4"))));
        capitulo5m.setTotalProgramado(totalProgrado(presupuestacion.mostrarRecursosActividad(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, Short.parseShort("5"))));
        capituloCD.setTotalProgramado(totalProgrado(presupuestacion.mostrarRecursosActividad(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, Short.parseShort("6"))));
        
        capituloTT.setTotalPretecho(capitulo2m.getTotalPretecho() + capitulo3m.getTotalPretecho() + capitulo4m.getTotalPretecho() + capitulo5m.getTotalPretecho() + capituloCD.getTotalPretecho());
        capituloTT.setTotalProgramado(capitulo2m.getTotalProgramado() + capitulo3m.getTotalProgramado() + capitulo4m.getTotalProgramado() + capitulo5m.getTotalProgramado() + capituloCD.getTotalProgramado());
        
        capitulo2m.setTotalDisponible(capitulo2m.getTotalPretecho()-capitulo2m.getTotalProgramado());
        capitulo3m.setTotalDisponible(capitulo3m.getTotalPretecho()-capitulo3m.getTotalProgramado());
        capitulo4m.setTotalDisponible(capitulo4m.getTotalPretecho()-capitulo4m.getTotalProgramado());
        capitulo5m.setTotalDisponible(capitulo5m.getTotalPretecho()-capitulo5m.getTotalProgramado());
        capituloCD.setTotalDisponible(capituloCD.getTotalPretecho()-capituloCD.getTotalProgramado());
        capituloTT.setTotalDisponible(capituloTT.getTotalPretecho()-capituloTT.getTotalProgramado());
        
        capitulo2m.setRendered(capitulo2m.getTotalPretecho().equals(0D));
        capitulo3m.setRendered(capitulo3m.getTotalPretecho().equals(0D));
        capitulo4m.setRendered(capitulo4m.getTotalPretecho().equals(0D));
        capitulo5m.setRendered(capitulo5m.getTotalPretecho().equals(0D));
        capituloCD.setRendered(capituloCD.getTotalPretecho().equals(0D));
        capituloTT.setRendered(capituloTT.getTotalPretecho().equals(0D));
        
        
        
        capitulo2m.setColorClase(claseVista(capitulo2m.getTotalPretecho(),capitulo2m.getTotalProgramado()));
        capitulo3m.setColorClase(claseVista(capitulo3m.getTotalPretecho(),capitulo3m.getTotalProgramado()));
        capitulo4m.setColorClase(claseVista(capitulo4m.getTotalPretecho(),capitulo4m.getTotalProgramado()));
        capitulo5m.setColorClase(claseVista(capitulo5m.getTotalPretecho(),capitulo5m.getTotalProgramado()));
        capituloCD.setColorClase(claseVista(capituloCD.getTotalPretecho(),capituloCD.getTotalProgramado()));
        capituloTT.setColorClase(claseVista(capituloTT.getTotalPretecho(),capituloTT.getTotalProgramado()));
        
    }
    
    public Double totalProgrado(List<RecursosActividad> listaRecAc) {
        total=0D;
        if (!listaRecAc.isEmpty()) {
            listaRecAc.forEach((t) -> {
                total = total + t.getTotal();
            });
        }
        return total;
    }

    public String claseVista(Double asigando,Double disponible) {
        if (asigando >= disponible) {
            return "#000000";
        }else{            
            return "#FF0000";
        }    
    }
    
    public void resetearValores() {
        actividadesPoa = new ActividadesPoa();
        actividadesPoa = new ActividadesPoa();
        numPm1 = null;        numPm2 = null;        numPm3 = null;        numPm4 = null;        numPm5 = null;        numPm6 = null;        
        numPm7 = null;        numPm8 = null;        numPm9 = null;        numPm10 = null;        numPm11 = null;        numPm12 = null;
    }
    
    public void imprimirValores() {
    }
//////////////////////////////////////////////////////////////////////////////// DTO's
    public static class VistaRecurso {

        @Getter        @Setter        private Double totalPretecho;
        @Getter        @Setter        private Double totalProgramado;
        @Getter        @Setter        private Double totalDisponible;
        @Getter        @Setter        private String colorClase;
        @Getter        @Setter        private Boolean rendered;

        public VistaRecurso(Double totalPretecho, Double totalProgramado, Double totalDisponible, String colorClase, Boolean rendered) {
            this.totalPretecho = totalPretecho;
            this.totalProgramado = totalProgramado;
            this.totalDisponible = totalDisponible;
            this.colorClase = colorClase;
            this.rendered = rendered;
        }
    }

    public static class EjeListaEstrategias {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<EstrategiasListaLineasAccion> listaLineasAccions;

        public EjeListaEstrategias(EjesRegistro ejeA, List<EstrategiasListaLineasAccion> listaLineasAccions) {
            this.ejeA = ejeA;
            this.listaLineasAccions = listaLineasAccions;
        }

    }

    public static class EstrategiasListaLineasAccion {

        @Getter        @Setter        private Estrategias etra;
        @Getter        @Setter        private List<LineasAccionListaActividad> listalistaEstrategiaLaAp;

        public EstrategiasListaLineasAccion(Estrategias etra, List<LineasAccionListaActividad> listalistaEstrategiaLaAp) {
            this.etra = etra;
            this.listalistaEstrategiaLaAp = listalistaEstrategiaLaAp;
        }

    }

    public static class LineasAccionListaActividad {

        @Getter        @Setter        private LineasAccion lineasAccion;
        @Getter        @Setter        private List<ActividadListaRecursoActividades> listaRecursoActividadeses;

        public LineasAccionListaActividad(LineasAccion lineasAccion, List<ActividadListaRecursoActividades> listaRecursoActividadeses) {
            this.lineasAccion = lineasAccion;
            this.listaRecursoActividadeses = listaRecursoActividadeses;
        }

    }

    public static class ActividadListaRecursoActividades {

        @Getter        @Setter        private ActividadesPoa actividadesPoa1;
        @Getter        @Setter        private List<RecursosActividad> recacts;

        public ActividadListaRecursoActividades(ActividadesPoa actividadesPoa1, List<RecursosActividad> recacts) {
            this.actividadesPoa1 = actividadesPoa1;
            this.recacts = recacts;
        }

    }
    
}
