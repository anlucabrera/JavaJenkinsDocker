package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;
import mx.edu.utxj.pye.sgi.util.POAUtilidades;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;

@Named
@ManagedBean
@ViewScoped
public class ControladorPOARegistro implements Serializable {
// Listas de entities 
    @Getter    @Setter    private List<ActividadesPoa> listaActividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> listaActividadesPoasPadres = new ArrayList<>(), listaActividadesPoasHijas = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> listaEjesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> listaEstrategias = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> listaLineasAccion = new ArrayList<>();
    @Getter    @Setter    private List<CuadroMandoIntegral> listaCuadroMandoIntegrals = new ArrayList<>();
    @Getter    @Setter    private List<UnidadMedidas> listaUnidadMedidases = new ArrayList<>();
// variables de datos Primitivos
    @Getter    @Setter    private Boolean unidadMedidaNueva = false, esActividadPrincipal = false, permitirRegistro = false,ejeActivo = false;
    @Getter    @Setter    private Short unidadDMedida = 0, ejercicioFiscal = 0;    
    @Getter    @Setter    private Date fechaActual=new Date();   
    @Getter    @Setter    private Short numPm1=0,numPm2=0,numPm3=0,numPm4=0,numPm5=0,numPm6=0,numPm7=0,numPm8=0,numPm9=0,numPm10=0,numPm11=0,numPm12=0;
    @Getter    @Setter    private Short numPEm1=0,numPEm2=0,numPEm3=0,numPEm4=0,numPEm5=0,numPEm6=0,numPEm7=0,numPEm8=0,numPEm9=0,numPEm10=0,numPEm11=0,numPEm12=0;
    @Getter    @Setter    private String tipo = "Actividad", nombreUnidad = "", mensajeValidacion = "";
    @Getter    @Setter    private Integer totalProgramado = 0, numeroActividadPrincipal = 1, numeroActividadSecuendaria = 1, numeroActividadPrincipalAnterior = 0, unidadExistente = 0;
    @Getter    @Setter    private Integer mes1 = 0, mes2 = 0, mes3 = 0, mes4 = 0, mes5 = 0, mes6 = 0, mes7 = 0, mes8 = 0, mes9 = 0, mes10 = 0, mes11 = 0, mes12 = 0;
// variables de entities
    @Getter    @Setter    private ActividadesPoa actividadesPoa = new ActividadesPoa(), actividadPoaPrincipal = new ActividadesPoa(), actividadPoaPrincipalEditada = new ActividadesPoa(), actividadPoaPrincipalEditadaAnterior = new ActividadesPoa(), actividadPoaEliminada = new ActividadesPoa(), actividadPoaEliminado = new ActividadesPoa(), actividadPoaEditando = new ActividadesPoa();
    @Getter    @Setter    private EjesRegistro ejesRegistro = new EjesRegistro();
    @Getter    @Setter    private Estrategias estrategias = new Estrategias();
    @Getter    @Setter    private LineasAccion lineasAccion = new LineasAccion();
    @Getter    @Setter    private CuadroMandoIntegral cuadroMandoIntegral = new CuadroMandoIntegral();
    @Getter    @Setter    private UnidadMedidas unidadMedidas = new UnidadMedidas();
// Listas de DTO's
    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp = new ArrayList<>();
    @Getter    @Setter    private List<listaEjeEstrategia> listaListaEjeEstrategia = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
   

    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    POAUtilidades pOAUtilidades;

    @PostConstruct
    public void init() {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.init()");
        ejercicioFiscal =  pOAUtilidades.obtenerejercicioFiscal("Registro",100);
        unidadDMedida = null;
        numPm1 = null;        numPm2 = null;        numPm3 = null;        numPm4 = null;        numPm5 = null;        numPm6 = null;
        numPm7 = null;        numPm8 = null;        numPm9 = null;        numPm10 = null;        numPm11 = null;        numPm12 = null;
        ejeActivo = false;
        consultarListasInit();
    }

    public void consultarListasInit() {
        
        ejesRegistro = new EjesRegistro();
        estrategias = new Estrategias();
        lineasAccion = new LineasAccion();

        listaLineasAccion.clear();
        listaEstrategias.clear();
        listaEjesRegistros.clear();
        listaUnidadMedidases.clear();
        listaActividadesPoas.clear();
        listaActividadesPoasPadres.clear();
        listaActividadesPoasHijas.clear();

        listaUnidadMedidases = poaSelectec.mostrarUnidadMedidases();
        listaUnidadMedidases.add(new UnidadMedidas(Short.parseShort("0"), "Nueva unidad de medida"));
        listaLineasAccion.add(new LineasAccion(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
        listaEjesRegistros.add(new EjesRegistro(0, "Selecciones Uno", "Selecciones Uno", "Selecciones Uno", "Selecciones Uno"));
        poaSelectec.mostrarEjesRegistros().forEach((t) -> {
            listaEjesRegistros.add(t);
        });

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasReporteArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);
        if (!listaActividadesPoas.isEmpty()) {
            Collections.sort(listaActividadesPoas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
            listaActividadesPoas.forEach((t) -> {
                if (t.getNumeroS() == 0) {
                    listaActividadesPoasPadres.add(t);
                } else {
                    listaActividadesPoasHijas.add(t);
                }
            });
        }

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();

        if (!poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).isEmpty()) {
            poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).forEach((ej) -> {
                listaListaEjeEstrategia = new ArrayList<>();
                listaListaEjeEstrategia.clear();
                listaListaEjeEstrategia.add(new listaEjeEstrategia(ej, poaSelectec.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa())));
                if (!listaListaEjeEstrategia.isEmpty()) {
                    listaListaEjeEstrategia.forEach((e) -> {
                        e.getListaEstrategiases1().forEach((t) -> {
                            List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                            listaActividadesPoasFiltradas.clear();
                            listaActividadesPoasFiltradas = poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                            listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                            Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                        });
                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje));
                        listaEstrategiaActividadesesEje = new ArrayList<>();
                        listaEstrategiaActividadesesEje.clear();
                    });
                }
            });
        }
        
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
    }

    public void recargarPag(){        
            Faces.refresh();
    }
    
    public void resetearValores() {
        actividadesPoa = new ActividadesPoa();
        listaActividadesPoas.clear();
        unidadDMedida = null;
        actividadPoaPrincipal = new ActividadesPoa();
        tipo = "Actividad";
        nombreUnidad = "";
        totalProgramado = 0;
        actividadPoaEliminado = new ActividadesPoa();
        numPm1 = null;        numPm2 = null;        numPm3 = null;        numPm4 = null;        numPm5 = null;        numPm6 = null;
        numPm7 = null;        numPm8 = null;        numPm9 = null;        numPm10 = null;        numPm11 = null;        numPm12 = null;
        numPEm1 = null;        numPEm2 = null;        numPEm3 = null;        numPEm4 = null;        numPEm5 = null;        numPEm6 = null;
        numPEm7 = null;        numPEm8 = null;        numPEm9 = null;        numPEm10 = null;        numPEm11 = null;        numPEm12 = null;
    }

    public void resetearProgramados(ValueChangeEvent event) {
        esActividadPrincipal = Boolean.parseBoolean(event.getNewValue().toString());
        if (esActividadPrincipal) {
            numPm1 = null;            numPm2 = null;            numPm3 = null;            numPm4 = null;            numPm5 = null;            numPm6 = null;
            numPm7 = null;            numPm8 = null;            numPm9 = null;            numPm10 = null;            numPm11 = null;            numPm12 = null;
            numPEm1 = null;            numPEm2 = null;            numPEm3 = null;            numPEm4 = null;            numPEm5 = null;            numPEm6 = null;
            numPEm7 = null;            numPEm8 = null;            numPEm9 = null;            numPEm10 = null;            numPEm11 = null;            numPEm12 = null;
            actividadesPoa.setNPEnero(Short.parseShort("0"));            actividadesPoa.setNPFebrero(Short.parseShort("0"));
            actividadesPoa.setNPMarzo(Short.parseShort("0"));            actividadesPoa.setNPAbril(Short.parseShort("0"));
            actividadesPoa.setNPMayo(Short.parseShort("0"));            actividadesPoa.setNPJunio(Short.parseShort("0"));
            actividadesPoa.setNPJulio(Short.parseShort("0"));            actividadesPoa.setNPAgosto(Short.parseShort("0"));
            actividadesPoa.setNPSeptiembre(Short.parseShort("0"));            actividadesPoa.setNPOctubre(Short.parseShort("0"));
            actividadesPoa.setNPNoviembre(Short.parseShort("0"));            actividadesPoa.setNPDiciembre(Short.parseShort("0"));
            totalProgramado = actividadesPoa.getNPEnero() + actividadesPoa.getNPFebrero() + actividadesPoa.getNPMarzo() + actividadesPoa.getNPAbril() + actividadesPoa.getNPMayo() + actividadesPoa.getNPJunio() + actividadesPoa.getNPJulio() + actividadesPoa.getNPAgosto() + actividadesPoa.getNPSeptiembre() + actividadesPoa.getNPOctubre() + actividadesPoa.getNPNoviembre() + actividadesPoa.getNPDiciembre();
            actividadesPoa.setTotal(Short.parseShort(totalProgramado.toString()));
        }
    }

    public void consultarListas() {
        listaActividadesPoasPadres = new ArrayList<>();
        listaActividadesPoasHijas = new ArrayList();
        listaActividadesPoas = new ArrayList();

        listaActividadesPoasPadres.clear();
        listaActividadesPoasHijas.clear();
        listaActividadesPoas.clear();
        listaUnidadMedidases.clear();

        listaUnidadMedidases = poaSelectec.mostrarUnidadMedidases();
        listaUnidadMedidases.add(new UnidadMedidas(Short.parseShort("0"), "Nueva unidad de medida"));

        listaActividadesPoas = poaSelectec.mostrarActividadesPoasAreaEjeyEjercicioFiscal(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, ejesRegistro);
        if (!listaActividadesPoas.isEmpty()) {
            Collections.sort(listaActividadesPoas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
            listaActividadesPoas.forEach((t) -> {
                if (t.getNumeroS() == 0) {
                    listaActividadesPoasPadres.add(t);
                } else {
                    listaActividadesPoasHijas.add(t);
                }
            });
        }

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        ejesEsLaAp = new ArrayList<>();
        ejesEsLaAp.clear();

        listaListaEjeEstrategia = new ArrayList<>();
        listaListaEjeEstrategia.clear();
        listaListaEjeEstrategia.add(new listaEjeEstrategia(ejesRegistro, poaSelectec.getEstarategiasPorEje(ejesRegistro, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa())));
        if (!listaListaEjeEstrategia.isEmpty()) {
            listaListaEjeEstrategia.forEach((e) -> {
                e.getListaEstrategiases1().forEach((t) -> {
                    List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                    listaActividadesPoasFiltradas.clear();
                    listaActividadesPoasFiltradas = poaSelectec.getActividadesPoasporEstarategias(t, e.getEjess(), ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                    Collections.sort(listaActividadesPoasFiltradas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
                    listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                });
            });
            Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
            ejesEsLaAp.add(new listaEjesEsLaAp(ejesRegistro, listaEstrategiaActividadesesEje));
            Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
        }
    }

    public void asignarParametrosRegistro(ValueChangeEvent event) {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.asignarParametrosRegistro()" + Short.parseShort(event.getNewValue().toString()));
        if (Short.parseShort(event.getNewValue().toString()) != Short.parseShort("0")) {
            switch (event.getComponent().getId()) {
                case "eje":
                    ejesRegistro = new EjesRegistro();
                    estrategias = new Estrategias();
                    lineasAccion = new LineasAccion();
                    ejesRegistro = poaSelectec.mostrarEjeRegistro(Integer.parseInt(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        listaEstrategias.clear();
                        listaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        poaSelectec.mostrarEstrategiasRegistro(ejercicioFiscal, ejesRegistro).forEach((t) -> {
                            listaEstrategias.add(t);
                        });
                        consultarListas();
                        permitirRegistro = false;
                    }
                    resetearValores();
                    break;
                case "estrategia":
                    estrategias = new Estrategias();
                    lineasAccion = new LineasAccion();
                    estrategias = poaSelectec.mostrarEstrategia(Short.parseShort(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        listaLineasAccion.clear();
                        listaLineasAccion.add(new LineasAccion(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        poaSelectec.mostrarLineasAccionRegistro(ejercicioFiscal, ejesRegistro, estrategias).forEach((t) -> {
                            listaLineasAccion.add(t);
                        });
                        permitirRegistro = false;
                    }
                    resetearValores();
                    break;
                case "lineaAccion":
                    lineasAccion = new LineasAccion();
                    lineasAccion = poaSelectec.mostrarLineaAccion(Short.parseShort(event.getNewValue().toString()));
                    if (lineasAccion != null) {
                        cuadroMandoIntegral = new CuadroMandoIntegral();
                        listaCuadroMandoIntegrals.clear();
                        listaCuadroMandoIntegrals = poaSelectec.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ejesRegistro, estrategias, lineasAccion);
                        cuadroMandoIntegral = listaCuadroMandoIntegrals.get(0);
                        permitirRegistro = true;
                    }
                    resetearValores();
                    ejeActivo = true;
                    break;
            }
        } else {
            ejesRegistro = new EjesRegistro();
            estrategias = new Estrategias();
            lineasAccion = new LineasAccion();
            listaEstrategias.clear();
            listaLineasAccion.clear();
            listaCuadroMandoIntegrals.clear();
            consultarListasInit();
        }
    }

    public void asignarUnidadMedida(ValueChangeEvent event) {
        if ("medida".equals(event.getComponent().getId()) || "medida1".equals(event.getComponent().getId())) {
            if (Integer.parseInt(event.getNewValue().toString()) == 0) {
                unidadMedidaNueva = true;
                unidadDMedida = 0;
            } else {
                unidadMedidaNueva = false;
                unidadDMedida = Short.parseShort(event.getNewValue().toString());
            }
        } else {
            nombreUnidad = event.getNewValue().toString();
        }

    }

    public void actividadTipo(ValueChangeEvent event) {
        tipo = "";
        tipo = event.getNewValue().toString();
        esActividadPrincipal = false;

    }

    public void asignarNumerosProgramados(ValueChangeEvent event) {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.asignarNumerosProgramados(1)"+event.getNewValue());
        switch (event.getComponent().getId()) {
            case "mes1":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPEnero((numPm1 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm1 = null;                    actividadesPoa.setNPEnero(Short.parseShort("0"));                }
                } else {                    numPm1 = null;                    actividadesPoa.setNPEnero(Short.parseShort("0"));                }
                break;
            case "mes2":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPFebrero((numPm2 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm2 = null;                    actividadesPoa.setNPFebrero(Short.parseShort("0"));                }
                } else {                    numPm2 = null;                    actividadesPoa.setNPFebrero(Short.parseShort("0"));                }
                break;
            case "mes3":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPMarzo((numPm3 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm3 = null;                    actividadesPoa.setNPMarzo(Short.parseShort("0"));                }
                } else {                    numPm3 = null;                    actividadesPoa.setNPMarzo(Short.parseShort("0"));                }
                break;
            case "mes4":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPAbril((numPm4 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm4 = null;                    actividadesPoa.setNPAbril(Short.parseShort("0"));                }
                } else {                    numPm4 = null;                    actividadesPoa.setNPAbril(Short.parseShort("0"));                }
                break;
            case "mes5":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPMayo((numPm5 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm5 = null;                    actividadesPoa.setNPMayo(Short.parseShort("0"));                }
                } else {                    numPm5 = null;                    actividadesPoa.setNPMayo(Short.parseShort("0"));                }
                break;
            case "mes6":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPJunio((numPm6 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm6 = null;                    actividadesPoa.setNPJunio(Short.parseShort("0"));                }
                } else {                    numPm6 = null;                    actividadesPoa.setNPJunio(Short.parseShort("0"));                }
                break;
            case "mes7":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPJulio((numPm7 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm7 = null;                    actividadesPoa.setNPJulio(Short.parseShort("0"));                }
                } else {                    numPm7 = null;                    actividadesPoa.setNPJulio(Short.parseShort("0"));                }
                break;
            case "mes8":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPAgosto((numPm8 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm8 = null;                    actividadesPoa.setNPAgosto(Short.parseShort("0"));                }
                } else {                    numPm8 = null;                    actividadesPoa.setNPAgosto(Short.parseShort("0"));                }
                break;
            case "mes9":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPSeptiembre((numPm9 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm9 = null;                    actividadesPoa.setNPSeptiembre(Short.parseShort("0"));                }
                } else {                    numPm9 = null;                    actividadesPoa.setNPSeptiembre(Short.parseShort("0"));                }
                break;
            case "mes10":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPOctubre((numPm10 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm10 = null;                    actividadesPoa.setNPOctubre(Short.parseShort("0"));                }
                } else {                    numPm10 = null;                    actividadesPoa.setNPOctubre(Short.parseShort("0"));                }
                break;
            case "mes11":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPNoviembre((numPm11 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm11 = null;                    actividadesPoa.setNPNoviembre(Short.parseShort("0"));                }
                } else {                    numPm11 = null;                    actividadesPoa.setNPNoviembre(Short.parseShort("0"));                }
                break;
            case "mes12":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadesPoa.setNPDiciembre((numPm12 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPm12 = null;                    actividadesPoa.setNPDiciembre(Short.parseShort("0"));                }
                } else {                    numPm12 = null;                    actividadesPoa.setNPDiciembre(Short.parseShort("0"));                }
                break;
        }
        
        totalProgramado = actividadesPoa.getNPEnero() + actividadesPoa.getNPFebrero() + actividadesPoa.getNPMarzo() + actividadesPoa.getNPAbril() + actividadesPoa.getNPMayo() + actividadesPoa.getNPJunio() + actividadesPoa.getNPJulio() + actividadesPoa.getNPAgosto() + actividadesPoa.getNPSeptiembre() + actividadesPoa.getNPOctubre() + actividadesPoa.getNPNoviembre() + actividadesPoa.getNPDiciembre();
        actividadesPoa.setTotal(Short.parseShort(totalProgramado.toString()));
    }

    public void asignarNumerosProgramadosEdicion(ValueChangeEvent event) {
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.asignarNumerosProgramadosEdicion(1)"+event.getNewValue());
        switch (event.getComponent().getId()) {
            case "mes1":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPEnero((numPEm1 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm1 = null;                    actividadPoaEditando.setNPEnero(Short.parseShort("0"));                }
                } else {                    numPEm1 = null;                    actividadPoaEditando.setNPEnero(Short.parseShort("0"));                }
                break;
            case "mes2":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPFebrero((numPEm2 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm2 = null;                    actividadPoaEditando.setNPFebrero(Short.parseShort("0"));                }
                } else {                    numPEm2 = null;                    actividadPoaEditando.setNPFebrero(Short.parseShort("0"));                }
                break;
            case "mes3":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPMarzo((numPEm3 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm3 = null;                    actividadPoaEditando.setNPMarzo(Short.parseShort("0"));                }
                } else {                    numPEm3 = null;                    actividadPoaEditando.setNPMarzo(Short.parseShort("0"));                }
                break;
            case "mes4":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPAbril((numPEm4 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm4 = null;                    actividadPoaEditando.setNPAbril(Short.parseShort("0"));                }
                } else {                    numPEm4 = null;                    actividadPoaEditando.setNPAbril(Short.parseShort("0"));                }
                break;
            case "mes5":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPMayo((numPEm5 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm5 = null;                    actividadPoaEditando.setNPMayo(Short.parseShort("0"));                }
                } else {                    numPEm5 = null;                    actividadPoaEditando.setNPMayo(Short.parseShort("0"));                }
                break;
            case "mes6":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPJunio((numPEm6 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm6 = null;                    actividadPoaEditando.setNPJunio(Short.parseShort("0"));                }
                } else {                    numPEm6 = null;                    actividadPoaEditando.setNPJunio(Short.parseShort("0"));                }
                break;
            case "mes7":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPJulio((numPEm7 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm7 = null;                    actividadPoaEditando.setNPJulio(Short.parseShort("0"));                }
                } else {                    numPEm7 = null;                    actividadPoaEditando.setNPJulio(Short.parseShort("0"));                }
                break;
            case "mes8":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPAgosto((numPEm8 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm8 = null;                    actividadPoaEditando.setNPAgosto(Short.parseShort("0"));                }
                } else {                    numPEm8 = null;                    actividadPoaEditando.setNPAgosto(Short.parseShort("0"));                }
                break;
            case "mes9":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPSeptiembre((numPEm9 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm9 = null;                    actividadPoaEditando.setNPSeptiembre(Short.parseShort("0"));                }
                } else {                    numPEm9 = null;                    actividadPoaEditando.setNPSeptiembre(Short.parseShort("0"));                }
                break;
            case "mes10":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPOctubre((numPEm10 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm10 = null;                    actividadPoaEditando.setNPOctubre(Short.parseShort("0"));                }
                } else {                    numPEm10 = null;                    actividadPoaEditando.setNPOctubre(Short.parseShort("0"));                }
                break;
            case "mes11":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPNoviembre((numPEm11 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm11 = null;                    actividadPoaEditando.setNPNoviembre(Short.parseShort("0"));                }
                } else {                    numPEm11 = null;                    actividadPoaEditando.setNPNoviembre(Short.parseShort("0"));                }
                break;
            case "mes12":
                if (event.getNewValue() != null) {
                    if (Integer.parseInt(event.getNewValue().toString()) != 0) {
                        actividadPoaEditando.setNPDiciembre((numPEm12 = Short.parseShort(event.getNewValue().toString())));
                    } else {                    numPEm12 = null;                    actividadPoaEditando.setNPDiciembre(Short.parseShort("0"));                }
                } else {                    numPEm12 = null;                    actividadPoaEditando.setNPDiciembre(Short.parseShort("0"));                }
                break;
        }
        totalProgramado = actividadPoaEditando.getNPEnero() + actividadPoaEditando.getNPFebrero() + actividadPoaEditando.getNPMarzo() + actividadPoaEditando.getNPAbril() + actividadPoaEditando.getNPMayo() + actividadPoaEditando.getNPJunio() + actividadPoaEditando.getNPJulio() + actividadPoaEditando.getNPAgosto() + actividadPoaEditando.getNPSeptiembre() + actividadPoaEditando.getNPOctubre() + actividadPoaEditando.getNPNoviembre() + actividadPoaEditando.getNPDiciembre();
        actividadPoaEditando.setTotal(Short.parseShort(totalProgramado.toString()));
    }

    public void anadirNuavUnidadDeMedida() {
        if ("Subactividad".equals(tipo)) {
            if (numeroActividadPrincipal == null || numeroActividadPrincipal == 0) {
                mensajeValidacion = "La actividad principal no puede quedar vacia";
                        Ajax.oncomplete("PF('validacion').show();");
                return;
            }
        }
        if (permitirRegistro == true) {
            if (unidadDMedida != null) {
                if (unidadDMedida == 0) {
                    unidadExistente = 0;
                    listaUnidadMedidases.forEach((u) -> {
                        if (u.getNombre().equalsIgnoreCase(nombreUnidad)) {
                            unidadExistente = unidadExistente + 1;
                        }
                    });
                    if (unidadExistente == 0) {
                        unidadMedidas.setNombre(nombreUnidad);
                        unidadMedidas = poaSelectec.agregarUnidadMedidas(unidadMedidas);
                        unidadDMedida = unidadMedidas.getUnidadMedida();
                        anadirNuavActividad();
                    } else {
                        mensajeValidacion = "La unidad de medida ya existe, favor de seleccionarla del menú";
                        Ajax.oncomplete("PF('validacion').show();");
                    }
                } else {
                    anadirNuavActividad();
                }
            } else {
                mensajeValidacion = "Seleccione una unidad de medida";
                Ajax.oncomplete("PF('validacion').show();");
            }
        } else {
            mensajeValidacion = "Seleccione una alineación estratégica";
            Ajax.oncomplete("PF('validacion').show();");
        }
    }

    public void anadirNuavActividad() {
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;        mes7 = 0;        mes8 = 0;        mes9 = 0;
        mes10 = 0;        mes11 = 0;        mes12 = 0;        totalProgramado = 0;        numeroActividadPrincipal = 0;        numeroActividadSecuendaria = 0;
        if ("Actividad".equals(tipo)) {
            numeroActividadPrincipal = poaSelectec.mostrarActividadesPoaCuadroDeMando(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, cuadroMandoIntegral).size() + 1;
            actividadesPoa.setNumeroP(Short.parseShort(numeroActividadPrincipal.toString()));
            if (esActividadPrincipal) {
                actividadesPoa.setBandera("X");
            } else {
                actividadesPoa.setBandera("y");
            }
            actividadesPoa.setCuadroMandoInt(new CuadroMandoIntegral(cuadroMandoIntegral.getCuadroMandoInt()));
        } else {
            numeroActividadSecuendaria = poaSelectec.mostrarSubActividadesPoa(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, actividadPoaPrincipal.getNumeroP(), actividadPoaPrincipal.getCuadroMandoInt()).size();
            actividadesPoa.setNumeroP(actividadPoaPrincipal.getNumeroP());
            actividadesPoa.setNumeroS(Short.parseShort(numeroActividadSecuendaria.toString()));
            actividadesPoa.setBandera("y");
            actividadesPoa.setActividadPadre(actividadPoaPrincipal.getActividadPoa());
            actividadesPoa.setCuadroMandoInt(new CuadroMandoIntegral(actividadPoaPrincipal.getCuadroMandoInt().getCuadroMandoInt()));
        }
        actividadesPoa.setEsPIDE("NO PIDE");
        actividadesPoa.setActividadPasada(false);
        actividadesPoa.setUnidadMedida(new UnidadMedidas(unidadDMedida));
        actividadesPoa.setArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        actividadesPoa = poaSelectec.agregarActividadesPoa(actividadesPoa);
        if ("Subactividad".equals(tipo)) {
            actividadPoaPrincipal.setBandera("x");
            listaActividadesPoas.clear();
            listaActividadesPoasPadres.clear();
            listaActividadesPoasHijas.clear();
            listaActividadesPoas = poaSelectec.mostrarSubActividadesPoa(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, actividadPoaPrincipal.getNumeroP(), actividadPoaPrincipal.getCuadroMandoInt());
            if (!listaActividadesPoas.isEmpty()) {
                Collections.sort(listaActividadesPoas, (x, y) -> Short.compare(x.getNumeroP(), y.getNumeroP()));
                listaActividadesPoas.forEach((t) -> {
                    if (t.getNumeroS() == 0) {
                        listaActividadesPoasPadres.add(t);
                    } else {
                        listaActividadesPoasHijas.add(t);
                    }
                });
            }
            listaActividadesPoasHijas.forEach((t) -> {
                mes1 = mes1 + t.getNPEnero();                mes2 = mes2 + t.getNPFebrero();
                mes3 = mes3 + t.getNPMarzo();                mes4 = mes4 + t.getNPAbril();
                mes5 = mes5 + t.getNPMayo();                mes6 = mes6 + t.getNPJunio();
                mes7 = mes7 + t.getNPJulio();                mes8 = mes8 + t.getNPAgosto();
                mes9 = mes9 + t.getNPSeptiembre();                mes10 = mes10 + t.getNPOctubre();
                mes11 = mes11 + t.getNPNoviembre();                mes12 = mes12 + t.getNPDiciembre();
            });
            totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
            actividadPoaPrincipal.setNPEnero(Short.parseShort(mes1.toString()));            actividadPoaPrincipal.setNPFebrero(Short.parseShort(mes2.toString()));
            actividadPoaPrincipal.setNPMarzo(Short.parseShort(mes3.toString()));            actividadPoaPrincipal.setNPAbril(Short.parseShort(mes4.toString()));
            actividadPoaPrincipal.setNPMayo(Short.parseShort(mes5.toString()));            actividadPoaPrincipal.setNPJunio(Short.parseShort(mes6.toString()));
            actividadPoaPrincipal.setNPJulio(Short.parseShort(mes7.toString()));            actividadPoaPrincipal.setNPAgosto(Short.parseShort(mes8.toString()));
            actividadPoaPrincipal.setNPSeptiembre(Short.parseShort(mes9.toString()));            actividadPoaPrincipal.setNPOctubre(Short.parseShort(mes10.toString()));
            actividadPoaPrincipal.setNPNoviembre(Short.parseShort(mes11.toString()));            actividadPoaPrincipal.setNPDiciembre(Short.parseShort(mes12.toString()));
            actividadPoaPrincipal.setTotal(Short.parseShort(totalProgramado.toString()));            poaSelectec.actualizaActividadesPoa(actividadPoaPrincipal);
        }
        resetearValores();
        consultarListas();
    }

    public void buscarActividadeaEliminar(ActividadesPoa actividadesPoa) {
        actividadPoaEliminado=new ActividadesPoa();
        actividadPoaEliminado=actividadesPoa;
    }
    
    public void eliminarActividad() {
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;        mes7 = 0;        mes8 = 0;
        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;        numeroActividadPrincipal = 1;        numeroActividadSecuendaria = 1;
        listaActividadesPoasPadres.clear();
        listaActividadesPoasHijas.clear();
        actividadPoaEliminada = new ActividadesPoa();
        totalProgramado = 0;
        actividadPoaEliminada = actividadPoaEliminado;
        poaSelectec.eliminarActividadesPoa(actividadPoaEliminado);        
        listaActividadesPoas.clear();
        listaActividadesPoas = poaSelectec.mostrarSubActividadesPoa(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, actividadPoaEliminada.getNumeroP(), actividadPoaEliminada.getCuadroMandoInt());
        listaActividadesPoas.forEach((t) -> {
            if (t.getCuadroMandoInt().equals(actividadPoaEliminada.getCuadroMandoInt())) {
                if (t.getNumeroS() == 0) {
                    listaActividadesPoasPadres.add(t);
                } else {
                    listaActividadesPoasHijas.add(t);
                }
            }
        });
        
        if (actividadPoaEliminada.getNumeroS() == 0) {
            listaActividadesPoasHijas.forEach((s) -> {
                if (s.getNumeroP() == actividadPoaEliminado.getNumeroP()) {
                    poaSelectec.eliminarActividadesPoa(s);
                }
            });
            
            listaActividadesPoas.clear();
            listaActividadesPoasPadres.clear();
            listaActividadesPoasHijas.clear();
            
            listaActividadesPoas = poaSelectec.mostrarActividadesPoasAreaEjeyEjercicioFiscal(actividadPoaEliminada.getArea(), actividadPoaEliminada.getCuadroMandoInt().getEjercicioFiscal().getEjercicioFiscal(), actividadPoaEliminada.getCuadroMandoInt().getEje());
            listaActividadesPoas.forEach((t) -> {
                if (t.getCuadroMandoInt().equals(actividadPoaEliminada.getCuadroMandoInt())) {
                    if (t.getNumeroS() == 0) {
                        listaActividadesPoasPadres.add(t);
                    } else {
                        listaActividadesPoasHijas.add(t);
                    }
                }
            });
            
            listaActividadesPoasPadres.forEach((t) -> {
                listaActividadesPoasHijas.forEach((s) -> {
                    if (t.getNumeroP() == s.getNumeroP()) {
                        s.setNumeroP(Short.valueOf(numeroActividadPrincipal.toString()));
                        poaSelectec.actualizaActividadesPoa(s);
                    }
                });
                t.setNumeroP(Short.valueOf(numeroActividadPrincipal.toString()));
                poaSelectec.actualizaActividadesPoa(t);
                numeroActividadPrincipal = numeroActividadPrincipal + 1;
            });
        } else {
            if(listaActividadesPoasHijas.isEmpty()){
                listaActividadesPoasPadres.forEach((t) -> {
                    t.setBandera("y");
                    t.setNPEnero(Short.parseShort("0"));                    t.setNPFebrero(Short.parseShort("0"));
                    t.setNPMarzo(Short.parseShort("0"));                    t.setNPAbril(Short.parseShort("0"));
                    t.setNPMayo(Short.parseShort("0"));                    t.setNPJunio(Short.parseShort("0"));
                    t.setNPJulio(Short.parseShort("0"));                    t.setNPAgosto(Short.parseShort("0"));
                    t.setNPSeptiembre(Short.parseShort("0"));                    t.setNPOctubre(Short.parseShort("0"));
                    t.setNPNoviembre(Short.parseShort("0"));                    t.setNPDiciembre(Short.parseShort("0"));
                    t.setTotal(Short.parseShort("0"));                    poaSelectec.actualizaActividadesPoa(t);
                });
            }else{
                listaActividadesPoasHijas.forEach((s) -> {
                if (actividadPoaEliminada.getNumeroP() == s.getNumeroP()) {
                    s.setNumeroS(Short.valueOf(numeroActividadSecuendaria.toString()));
                    poaSelectec.actualizaActividadesPoa(s);
                    numeroActividadSecuendaria = numeroActividadSecuendaria + 1;
                }
            });
            listaActividadesPoasPadres.forEach((t) -> {
                if (t.getNumeroP() == actividadPoaEliminada.getNumeroP()) {
                    listaActividadesPoasHijas.forEach((s) -> {
                        if (s.getNumeroP() == t.getNumeroP() && s.getNumeroS() != 0) {
                            mes1 = mes1 + s.getNPEnero();                            mes2 = mes2 + s.getNPFebrero();
                            mes3 = mes3 + s.getNPMarzo();                            mes4 = mes4 + s.getNPAbril();
                            mes5 = mes5 + s.getNPMayo();                            mes6 = mes6 + s.getNPJunio();
                            mes7 = mes7 + s.getNPJulio();                            mes8 = mes8 + s.getNPAgosto();
                            mes9 = mes9 + s.getNPSeptiembre();                            mes10 = mes10 + s.getNPOctubre();
                            mes11 = mes11 + s.getNPNoviembre();                            mes12 = mes12 + s.getNPDiciembre();
                            t.setBandera("x");
                        }else{
                            t.setBandera("y");
                        }
                        totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                        t.setNPEnero(Short.parseShort(mes1.toString()));                        t.setNPFebrero(Short.parseShort(mes2.toString()));
                        t.setNPMarzo(Short.parseShort(mes3.toString()));                        t.setNPAbril(Short.parseShort(mes4.toString()));
                        t.setNPMayo(Short.parseShort(mes5.toString()));                        t.setNPJunio(Short.parseShort(mes6.toString()));
                        t.setNPJulio(Short.parseShort(mes7.toString()));                        t.setNPAgosto(Short.parseShort(mes8.toString()));
                        t.setNPSeptiembre(Short.parseShort(mes9.toString()));                        t.setNPOctubre(Short.parseShort(mes10.toString()));
                        t.setNPNoviembre(Short.parseShort(mes11.toString()));                        t.setNPDiciembre(Short.parseShort(mes12.toString()));
                        t.setTotal(Short.parseShort(totalProgramado.toString()));                        poaSelectec.actualizaActividadesPoa(t);
                    });
                }
            });
            }
            
        }
        resetearValores();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.eliminarActividad()"+ejeActivo);
        if(ejeActivo == false){
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.eliminarActividad(False)");
            Faces.refresh();
        }else{
            consultarListas();
        }
    }

    public void asignarNumeroP(ValueChangeEvent event) {
        numeroActividadPrincipal = 0;
        numeroActividadPrincipalAnterior = 0;
        actividadPoaPrincipalEditada = new ActividadesPoa();
        actividadPoaPrincipalEditadaAnterior = new ActividadesPoa();
        numeroActividadPrincipal = Integer.parseInt(event.getNewValue().toString());
        numeroActividadPrincipalAnterior = Integer.parseInt(event.getOldValue().toString());
        actividadPoaPrincipalEditada = poaSelectec.mostrarActividadPoaPrincipal(numeroActividadPrincipal);
        actividadPoaPrincipalEditadaAnterior = poaSelectec.mostrarActividadPoaPrincipal(numeroActividadPrincipalAnterior);
    }

    public void asignarActividadPrincipal(ValueChangeEvent event) {
        numeroActividadPrincipal = Integer.parseInt(event.getNewValue().toString());
        actividadPoaPrincipal = poaSelectec.mostrarActividadPoaPrincipal(numeroActividadPrincipal);
    }
    
    public void buscarActividadesVariasPlaneacionCuatrimestral(ActividadesPoa actividadesPoa) {
        numPEm1 = null;        numPEm2 = null;        numPEm3 = null;        numPEm4 = null;        numPEm5 = null;        numPEm6 = null;
        numPEm7 = null;        numPEm8 = null;        numPEm9 = null;        numPEm10 = null;        numPEm11 = null;        numPEm12 = null;
        actividadPoaPrincipalEditada = new ActividadesPoa();
        actividadPoaPrincipalEditadaAnterior = new ActividadesPoa();
        cuadroMandoIntegral = new CuadroMandoIntegral();
        Integer tamaño = 0;
        unidadDMedida = 0;
        tipo = "";
        esActividadPrincipal = false;
        numeroActividadPrincipal = 0;
        numeroActividadPrincipalAnterior = 0;
        tamaño = poaSelectec.mostrarSubActividadesPoa(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, actividadesPoa.getNumeroP(), actividadesPoa.getCuadroMandoInt()).size();
        actividadPoaPrincipalEditada = actividadesPoa;
        actividadPoaPrincipalEditadaAnterior = actividadesPoa;
        unidadDMedida = actividadesPoa.getUnidadMedida().getUnidadMedida();
        numeroActividadPrincipal = actividadesPoa.getActividadPadre();
        numeroActividadPrincipalAnterior = actividadesPoa.getActividadPadre();
        cuadroMandoIntegral = actividadesPoa.getCuadroMandoInt();
        if (actividadesPoa.getNumeroS() == 0) {
            tipo = "Actividad";
        } else {
            tipo = "Subactividad";
        }
        if ("Actividad".equals(tipo)) {
            if (tamaño == 1) {
                esActividadPrincipal = false;
            } else {
                esActividadPrincipal = true;
            }
        } else {
            esActividadPrincipal = false;
        }
        if (actividadesPoa.getNPEnero() != 0) {            numPEm1 = actividadesPoa.getNPEnero();        }
        if (actividadesPoa.getNPFebrero() != 0) {            numPEm2 = actividadesPoa.getNPFebrero();      }  
        if (actividadesPoa.getNPMarzo() != 0) {            numPEm3 = actividadesPoa.getNPMarzo();        }
        if (actividadesPoa.getNPAbril() != 0) {            numPEm4 = actividadesPoa.getNPAbril();        }
        if (actividadesPoa.getNPMayo() != 0) {            numPEm5 = actividadesPoa.getNPMayo();         }
        if (actividadesPoa.getNPJunio() != 0) {            numPEm6 = actividadesPoa.getNPJunio();        }
        if (actividadesPoa.getNPJulio() != 0) {            numPEm7 = actividadesPoa.getNPJulio();        }
        if (actividadesPoa.getNPAgosto() != 0) {            numPEm8 = actividadesPoa.getNPAgosto();       }
        if (actividadesPoa.getNPSeptiembre() != 0) {            numPEm9 = actividadesPoa.getNPSeptiembre();   }    
        if (actividadesPoa.getNPOctubre() != 0) {            numPEm10 = actividadesPoa.getNPOctubre();     }  
        if (actividadesPoa.getNPNoviembre() != 0) {            numPEm11 = actividadesPoa.getNPNoviembre();   }    
        if (actividadesPoa.getNPDiciembre() != 0) {            numPEm12 = actividadesPoa.getNPDiciembre();   }
        
    }

    public void onRowEdit() {
        listaActividadesPoasPadres.clear();
        listaActividadesPoasHijas.clear();
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;        mes7 = 0;        mes8 = 0;
        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;        numeroActividadSecuendaria = 0;        totalProgramado = 0;
        totalProgramado = actividadPoaEditando.getNPEnero() + actividadPoaEditando.getNPFebrero() + actividadPoaEditando.getNPMarzo() + actividadPoaEditando.getNPAbril() + actividadPoaEditando.getNPMayo() + actividadPoaEditando.getNPJunio() + actividadPoaEditando.getNPJulio() + actividadPoaEditando.getNPAgosto() + actividadPoaEditando.getNPSeptiembre() + actividadPoaEditando.getNPOctubre() + actividadPoaEditando.getNPNoviembre() + actividadPoaEditando.getNPDiciembre();
        actividadPoaEditando.setTotal(Short.parseShort(totalProgramado.toString()));
        actividadPoaEditando.setUnidadMedida(new UnidadMedidas(unidadDMedida));
        actividadPoaEditando.setNumeroP(actividadPoaPrincipalEditada.getNumeroP());
        actividadPoaEditando.setCuadroMandoInt(new CuadroMandoIntegral(actividadPoaPrincipalEditada.getCuadroMandoInt().getCuadroMandoInt()));
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.onRowEdit(1)"+actividadPoaEditando.getActividadPoa());
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.onRowEdit(2)"+actividadPoaEditando.getActividadPadre());
        if (tipo.equals("Subactividad")) {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.poa.ControladorPOARegistro.onRowEdit(3)"+numeroActividadPrincipal);
            actividadPoaEditando.setActividadPadre(numeroActividadPrincipal);
        }
        poaSelectec.actualizaActividadesPoa(actividadPoaEditando);
        totalProgramado = 0;
        if (actividadPoaPrincipalEditadaAnterior.getNumeroP() != actividadPoaEditando.getNumeroP() || actividadPoaPrincipalEditadaAnterior.getCuadroMandoInt() != actividadPoaEditando.getCuadroMandoInt()) {
            poaSelectec.mostrarSubActividadesPoa(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, actividadPoaPrincipalEditadaAnterior.getNumeroP(), actividadPoaEditando.getCuadroMandoInt()).forEach((t) -> {
                if (t.getNumeroS() == 0) {
                    listaActividadesPoasPadres.add(t);
                } else {
                    listaActividadesPoasHijas.add(t);
                }
            });
            listaActividadesPoasHijas.forEach((s) -> {
                if (s.getNumeroP() == actividadPoaPrincipalEditadaAnterior.getNumeroP()) {
                    numeroActividadSecuendaria = numeroActividadSecuendaria + 1;
                    s.setNumeroS(Short.parseShort(numeroActividadSecuendaria.toString()));
                    poaSelectec.actualizaActividadesPoa(s);                    mes1 = mes1 + s.getNPEnero();
                    mes2 = mes2 + s.getNPFebrero();                    mes3 = mes3 + s.getNPMarzo();
                    mes4 = mes4 + s.getNPAbril();                    mes5 = mes5 + s.getNPMayo();
                    mes6 = mes6 + s.getNPJunio();                    mes7 = mes7 + s.getNPJulio();
                    mes8 = mes8 + s.getNPAgosto();                    mes9 = mes9 + s.getNPSeptiembre();
                    mes10 = mes10 + s.getNPOctubre();                    mes11 = mes11 + s.getNPNoviembre();
                    mes12 = mes12 + s.getNPDiciembre();
                }
            });
            listaActividadesPoasPadres.forEach((t) -> {
                if (t.getNumeroP() == actividadPoaPrincipalEditadaAnterior.getNumeroP()) {
                    if (listaActividadesPoasHijas.isEmpty()) {
                        t.setBandera("y");
                    }
                    totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                    t.setNPEnero(Short.parseShort(mes1.toString()));                    t.setNPFebrero(Short.parseShort(mes2.toString()));
                    t.setNPMarzo(Short.parseShort(mes3.toString()));                    t.setNPAbril(Short.parseShort(mes4.toString()));
                    t.setNPMayo(Short.parseShort(mes5.toString()));                    t.setNPJunio(Short.parseShort(mes6.toString()));
                    t.setNPJulio(Short.parseShort(mes7.toString()));                    t.setNPAgosto(Short.parseShort(mes8.toString()));
                    t.setNPSeptiembre(Short.parseShort(mes9.toString()));                    t.setNPOctubre(Short.parseShort(mes10.toString()));
                    t.setNPNoviembre(Short.parseShort(mes11.toString()));                    t.setNPDiciembre(Short.parseShort(mes12.toString()));
                    t.setTotal(Short.parseShort(totalProgramado.toString()));                    poaSelectec.actualizaActividadesPoa(t);
                }
            });
        }
        if (actividadPoaEditando.getNumeroS() == 0 && "y".equals(actividadPoaEditando.getBandera())) {
        } else {
            totalProgramado = 0;            numeroActividadSecuendaria = 0;            listaActividadesPoasPadres.clear();            listaActividadesPoasHijas.clear();
            mes1 = 0;            mes2 = 0;            mes3 = 0;            mes4 = 0;            mes5 = 0;            mes6 = 0;            mes7 = 0;            mes8 = 0;
            mes9 = 0;            mes10 = 0;            mes11 = 0;            mes12 = 0;            totalProgramado = 0;
            poaSelectec.mostrarSubActividadesPoa(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, actividadPoaEditando.getNumeroP(), actividadPoaEditando.getCuadroMandoInt()).forEach((t) -> {
                if (t.getNumeroS() == 0) {
                    listaActividadesPoasPadres.add(t);
                } else {
                    listaActividadesPoasHijas.add(t);
                }
            });
            listaActividadesPoasHijas.forEach((s) -> {
                if (s.getNumeroP() == actividadPoaEditando.getNumeroP()) {
                    if (actividadPoaPrincipalEditadaAnterior.getNumeroP() != actividadPoaEditando.getNumeroP()) {
                        numeroActividadSecuendaria = numeroActividadSecuendaria + 1;
                        s.setNumeroS(Short.parseShort(numeroActividadSecuendaria.toString()));
                        poaSelectec.actualizaActividadesPoa(s);
                    }
                    mes1 = mes1 + s.getNPEnero();                    mes2 = mes2 + s.getNPFebrero();
                    mes3 = mes3 + s.getNPMarzo();                    mes4 = mes4 + s.getNPAbril();
                    mes5 = mes5 + s.getNPMayo();                    mes6 = mes6 + s.getNPJunio();
                    mes7 = mes7 + s.getNPJulio();                    mes8 = mes8 + s.getNPAgosto();
                    mes9 = mes9 + s.getNPSeptiembre();                    mes10 = mes10 + s.getNPOctubre();
                    mes11 = mes11 + s.getNPNoviembre();                    mes12 = mes12 + s.getNPDiciembre();
                }
            });
            listaActividadesPoasPadres.forEach((t) -> {
                if (t.getNumeroP() == actividadPoaEditando.getNumeroP()) {
                    if (listaActividadesPoasHijas.isEmpty()) {
                        t.setBandera("y");
                    }
                    totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                    t.setNPEnero(Short.parseShort(mes1.toString()));                    t.setNPFebrero(Short.parseShort(mes2.toString()));
                    t.setNPMarzo(Short.parseShort(mes3.toString()));                    t.setNPAbril(Short.parseShort(mes4.toString()));
                    t.setNPMayo(Short.parseShort(mes5.toString()));                    t.setNPJunio(Short.parseShort(mes6.toString()));
                    t.setNPJulio(Short.parseShort(mes7.toString()));                    t.setNPAgosto(Short.parseShort(mes8.toString()));
                    t.setNPSeptiembre(Short.parseShort(mes9.toString()));                    t.setNPOctubre(Short.parseShort(mes10.toString()));
                    t.setNPNoviembre(Short.parseShort(mes11.toString()));                    t.setNPDiciembre(Short.parseShort(mes12.toString()));
                    t.setTotal(Short.parseShort(totalProgramado.toString()));                    poaSelectec.actualizaActividadesPoa(t);
                }
            });
        }
        resetearValores();
        if(ejeActivo == false){
            Faces.refresh();
        }else{
            consultarListas();
        }
    }

    public static class listaEjesEsLaAp {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<listaEstrategiaActividades> listalistaEstrategiaLaAp;

        public listaEjesEsLaAp(EjesRegistro ejeA, List<listaEstrategiaActividades> listalistaEstrategiaLaAp) {
            this.ejeA = ejeA;
            this.listalistaEstrategiaLaAp = listalistaEstrategiaLaAp;
        }
    }

    public static class listaEjeEstrategia {

        @Getter        @Setter        private EjesRegistro ejess;
        @Getter        @Setter        private List<Estrategias> listaEstrategiases1;

        public listaEjeEstrategia(EjesRegistro ejess, List<Estrategias> listaEstrategiases1) {
            this.ejess = ejess;
            this.listaEstrategiases1 = listaEstrategiases1;
        }
    }

    public static class listaEstrategiaActividades {

        @Getter        @Setter        private Estrategias estrategias;
        @Getter        @Setter        private List<ActividadesPoa> actividadesPoas;

        public listaEstrategiaActividades(Estrategias estrategias, List<ActividadesPoa> actividadesPoas) {
            this.estrategias = estrategias;
            this.actividadesPoas = actividadesPoas;
        }
    }

    public void imprimirValores() {
    }

}
