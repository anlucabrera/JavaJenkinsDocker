package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;

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
    @Getter    @Setter    private Boolean unidadMedidaNueva = false, esActividadPrincipal = false, permitirRegistro = false;
    @Getter    @Setter    private Short unidadDMedida = 0, ejercicioFiscal = 0;
    @Getter    @Setter    private String tipo = "Actividad", nombreUnidad = "", mensajeValidacion = "";
    @Getter    @Setter    private Integer totalProgramado = 0, numeroActividadPrincipal = 1, numeroActividadSecuendaria = 1, numeroActividadPrincipalAnterior = 0, unidadExistente = 0;
    @Getter    @Setter    private Integer mes1 = 0, mes2 = 0, mes3 = 0, mes4 = 0, mes5 = 0, mes6 = 0, mes7 = 0, mes8 = 0, mes9 = 0, mes10 = 0, mes11 = 0, mes12 = 0;
// variables de entities
    @Getter    @Setter    private ActividadesPoa actividadesPoa = new ActividadesPoa(), actividadPoaPrincipal = new ActividadesPoa(), actividadPoaPrincipalEditada = new ActividadesPoa(), actividadPoaPrincipalEditadaAnterior = new ActividadesPoa(), actividadPoaEliminada = new ActividadesPoa(), actividadPoaEditando = new ActividadesPoa();
    @Getter    @Setter    private EjesRegistro ejesRegistro = new EjesRegistro();
    @Getter    @Setter    private Estrategias estrategias = new Estrategias();
    @Getter    @Setter    private LineasAccion lineasAccion = new LineasAccion();
    @Getter    @Setter    private CuadroMandoIntegral cuadroMandoIntegral = new CuadroMandoIntegral();
    @Getter    @Setter    private UnidadMedidas unidadMedidas = new UnidadMedidas();
// Listas de DTO's
    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp = new ArrayList<>();
    @Getter    @Setter    private List<listaEjeEstrategia> listaListaEjeEstrategia = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje1 = new ArrayList<>(), listaEstrategiaActividadesesEje2 = new ArrayList<>(), listaEstrategiaActividadesesEje3 = new ArrayList<>(), listaEstrategiaActividadesesEje4 = new ArrayList<>(), listaEstrategiaActividadesesEje5 = new ArrayList<>();


    @EJB    EjbPoaSelectec poaSelectec;
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        ejercicioFiscal = 17;
        unidadDMedida = null;
        consultarListasInit();
    }

    public void consultarListasInit() {
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
                            Collections.sort(listaActividadesPoasFiltradas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
                            switch (e.getEjess().getEje()) {
                                case 1:
                                    listaEstrategiaActividadesesEje1.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                                    Collections.sort(listaEstrategiaActividadesesEje1, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                                    break;
                                case 2:
                                    listaEstrategiaActividadesesEje2.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                                    Collections.sort(listaEstrategiaActividadesesEje2, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                                    break;
                                case 3:
                                    listaEstrategiaActividadesesEje3.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                                    Collections.sort(listaEstrategiaActividadesesEje3, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                                    break;
                                case 4:
                                    listaEstrategiaActividadesesEje4.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                                    Collections.sort(listaEstrategiaActividadesesEje4, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                                    break;

                                case 5:
                                    listaEstrategiaActividadesesEje5.add(new listaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                                    Collections.sort(listaEstrategiaActividadesesEje5, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                                    break;
                            }
                        });
                    });
                }
            });
        }

        if (!poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).isEmpty()) {
            poaSelectec.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal).forEach((ej) -> {
                switch (ej.getEje()) {
                    case 1:                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje1));                        break;
                    case 2:                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje2));                        break;
                    case 3:                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje3));                        break;
                    case 4:                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje4));                        break;
                    case 5:                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, listaEstrategiaActividadesesEje5));                        break;
                }
            });
        }
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
    }

    public void resetearValores() {
        actividadesPoa = new ActividadesPoa();
        listaActividadesPoas.clear();
        unidadDMedida = null;
        actividadPoaPrincipal = new ActividadesPoa();
        tipo = "Actividad";
        nombreUnidad = "";
        totalProgramado = 0;
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
                    permitirRegistro = false;
                }
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
                break;
            case "lineaAccion":
                lineasAccion = new LineasAccion();
                lineasAccion = poaSelectec.mostrarLineaAccion(Short.parseShort(event.getNewValue().toString()));
                if (lineasAccion != null) {
                    cuadroMandoIntegral = new CuadroMandoIntegral();
                    listaCuadroMandoIntegrals.clear();
                    listaCuadroMandoIntegrals = poaSelectec.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ejesRegistro, estrategias, lineasAccion);
                    cuadroMandoIntegral = listaCuadroMandoIntegrals.get(0);
                    consultarListas();
                    permitirRegistro = true;
                }
                break;
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
        switch (event.getComponent().getId()) {
            case "mes1":                actividadesPoa.setNPEnero(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes2":                actividadesPoa.setNPFebrero(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes3":                actividadesPoa.setNPMarzo(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes4":                actividadesPoa.setNPAbril(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes5":                actividadesPoa.setNPMayo(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes6":                actividadesPoa.setNPJunio(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes7":                actividadesPoa.setNPJulio(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes8":                actividadesPoa.setNPAgosto(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes9":                actividadesPoa.setNPSeptiembre(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes10":                actividadesPoa.setNPOctubre(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes11":                actividadesPoa.setNPNoviembre(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes12":               actividadesPoa.setNPDiciembre(Short.parseShort(event.getNewValue().toString()));                break;
        }
        totalProgramado = actividadesPoa.getNPEnero() + actividadesPoa.getNPFebrero() + actividadesPoa.getNPMarzo() + actividadesPoa.getNPAbril() + actividadesPoa.getNPMayo() + actividadesPoa.getNPJunio() + actividadesPoa.getNPJulio() + actividadesPoa.getNPAgosto() + actividadesPoa.getNPSeptiembre() + actividadesPoa.getNPOctubre() + actividadesPoa.getNPNoviembre() + actividadesPoa.getNPDiciembre();
        actividadesPoa.setTotal(Short.parseShort(totalProgramado.toString()));
    }

    public void asignarNumerosProgramadosEdicion(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "mes1":                actividadPoaEditando.setNPEnero(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes2":                actividadPoaEditando.setNPFebrero(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes3":                actividadPoaEditando.setNPMarzo(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes4":                actividadPoaEditando.setNPAbril(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes5":                actividadPoaEditando.setNPMayo(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes6":                actividadPoaEditando.setNPJunio(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes7":                actividadPoaEditando.setNPJulio(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes8":                actividadPoaEditando.setNPAgosto(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes9":                actividadPoaEditando.setNPSeptiembre(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes10":                actividadPoaEditando.setNPOctubre(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes11":                actividadPoaEditando.setNPNoviembre(Short.parseShort(event.getNewValue().toString()));                break;
            case "mes12":                actividadPoaEditando.setNPDiciembre(Short.parseShort(event.getNewValue().toString()));                break;
        }
        totalProgramado = actividadPoaEditando.getNPEnero() + actividadPoaEditando.getNPFebrero() + actividadPoaEditando.getNPMarzo() + actividadPoaEditando.getNPAbril() + actividadPoaEditando.getNPMayo() + actividadPoaEditando.getNPJunio() + actividadPoaEditando.getNPJulio() + actividadPoaEditando.getNPAgosto() + actividadPoaEditando.getNPSeptiembre() + actividadPoaEditando.getNPOctubre() + actividadPoaEditando.getNPNoviembre() + actividadPoaEditando.getNPDiciembre();
        actividadPoaEditando.setTotal(Short.parseShort(totalProgramado.toString()));
    }

    public void anadirNuavUnidadDeMedida() {
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
                mes1 = mes1 + t.getNPEnero();
                mes2 = mes2 + t.getNPFebrero();
                mes3 = mes3 + t.getNPMarzo();
                mes4 = mes4 + t.getNPAbril();
                mes5 = mes5 + t.getNPMayo();
                mes6 = mes6 + t.getNPJunio();
                mes7 = mes7 + t.getNPJulio();
                mes8 = mes8 + t.getNPAgosto();
                mes9 = mes9 + t.getNPSeptiembre();
                mes10 = mes10 + t.getNPOctubre();
                mes11 = mes11 + t.getNPNoviembre();
                mes12 = mes12 + t.getNPDiciembre();
            });
            totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
            actividadPoaPrincipal.setNPEnero(Short.parseShort(mes1.toString()));
            actividadPoaPrincipal.setNPFebrero(Short.parseShort(mes2.toString()));
            actividadPoaPrincipal.setNPMarzo(Short.parseShort(mes3.toString()));
            actividadPoaPrincipal.setNPAbril(Short.parseShort(mes4.toString()));
            actividadPoaPrincipal.setNPMayo(Short.parseShort(mes5.toString()));
            actividadPoaPrincipal.setNPJunio(Short.parseShort(mes6.toString()));
            actividadPoaPrincipal.setNPJulio(Short.parseShort(mes7.toString()));
            actividadPoaPrincipal.setNPAgosto(Short.parseShort(mes8.toString()));
            actividadPoaPrincipal.setNPSeptiembre(Short.parseShort(mes9.toString()));
            actividadPoaPrincipal.setNPOctubre(Short.parseShort(mes10.toString()));
            actividadPoaPrincipal.setNPNoviembre(Short.parseShort(mes11.toString()));
            actividadPoaPrincipal.setNPDiciembre(Short.parseShort(mes12.toString()));
            actividadPoaPrincipal.setTotal(Short.parseShort(totalProgramado.toString()));
            poaSelectec.actualizaActividadesPoa(actividadPoaPrincipal);
        }
        resetearValores();
        consultarListas();
    }

    public void eliminarActividad(ActividadesPoa pOa) {
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;        mes7 = 0;        mes8 = 0;
        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;        numeroActividadPrincipal = 1;        numeroActividadSecuendaria = 1;
        listaActividadesPoasPadres.clear();
        listaActividadesPoasHijas.clear();
        actividadPoaEliminada = new ActividadesPoa();
        totalProgramado = 0;
        actividadPoaEliminada = pOa;
        poaSelectec.eliminarActividadesPoa(pOa);
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
                if (s.getNumeroP() == pOa.getNumeroP()) {
                    poaSelectec.eliminarActividadesPoa(s);
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
        resetearValores();
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
        actividadPoaPrincipalEditada = new ActividadesPoa();
        actividadPoaPrincipalEditadaAnterior = new ActividadesPoa();
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
        if (tipo.equals("Subactividad")) {
            actividadPoaEditando.setActividadPadre(actividadPoaPrincipalEditada.getActividadPoa());
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
                    poaSelectec.actualizaActividadesPoa(s);
                    mes1 = mes1 + s.getNPEnero();
                    mes2 = mes2 + s.getNPFebrero();
                    mes3 = mes3 + s.getNPMarzo();
                    mes4 = mes4 + s.getNPAbril();
                    mes5 = mes5 + s.getNPMayo();
                    mes6 = mes6 + s.getNPJunio();
                    mes7 = mes7 + s.getNPJulio();
                    mes8 = mes8 + s.getNPAgosto();
                    mes9 = mes9 + s.getNPSeptiembre();
                    mes10 = mes10 + s.getNPOctubre();
                    mes11 = mes11 + s.getNPNoviembre();
                    mes12 = mes12 + s.getNPDiciembre();
                }
            });
            listaActividadesPoasPadres.forEach((t) -> {
                if (t.getNumeroP() == actividadPoaPrincipalEditadaAnterior.getNumeroP()) {
                    if (listaActividadesPoasHijas.isEmpty()) {
                        t.setBandera("y");
                    }
                    totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                    t.setNPEnero(Short.parseShort(mes1.toString()));
                    t.setNPFebrero(Short.parseShort(mes2.toString()));
                    t.setNPMarzo(Short.parseShort(mes3.toString()));
                    t.setNPAbril(Short.parseShort(mes4.toString()));
                    t.setNPMayo(Short.parseShort(mes5.toString()));
                    t.setNPJunio(Short.parseShort(mes6.toString()));
                    t.setNPJulio(Short.parseShort(mes7.toString()));
                    t.setNPAgosto(Short.parseShort(mes8.toString()));
                    t.setNPSeptiembre(Short.parseShort(mes9.toString()));
                    t.setNPOctubre(Short.parseShort(mes10.toString()));
                    t.setNPNoviembre(Short.parseShort(mes11.toString()));
                    t.setNPDiciembre(Short.parseShort(mes12.toString()));
                    t.setTotal(Short.parseShort(totalProgramado.toString()));
                    poaSelectec.actualizaActividadesPoa(t);
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
                    mes1 = mes1 + s.getNPEnero();
                    mes2 = mes2 + s.getNPFebrero();
                    mes3 = mes3 + s.getNPMarzo();
                    mes4 = mes4 + s.getNPAbril();
                    mes5 = mes5 + s.getNPMayo();
                    mes6 = mes6 + s.getNPJunio();
                    mes7 = mes7 + s.getNPJulio();
                    mes8 = mes8 + s.getNPAgosto();
                    mes9 = mes9 + s.getNPSeptiembre();
                    mes10 = mes10 + s.getNPOctubre();
                    mes11 = mes11 + s.getNPNoviembre();
                    mes12 = mes12 + s.getNPDiciembre();
                }
            });
            listaActividadesPoasPadres.forEach((t) -> {
                if (t.getNumeroP() == actividadPoaEditando.getNumeroP()) {
                    if (listaActividadesPoasHijas.isEmpty()) {
                        t.setBandera("y");
                    }
                    totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
                    t.setNPEnero(Short.parseShort(mes1.toString()));
                    t.setNPFebrero(Short.parseShort(mes2.toString()));
                    t.setNPMarzo(Short.parseShort(mes3.toString()));
                    t.setNPAbril(Short.parseShort(mes4.toString()));
                    t.setNPMayo(Short.parseShort(mes5.toString()));
                    t.setNPJunio(Short.parseShort(mes6.toString()));
                    t.setNPJulio(Short.parseShort(mes7.toString()));
                    t.setNPAgosto(Short.parseShort(mes8.toString()));
                    t.setNPSeptiembre(Short.parseShort(mes9.toString()));
                    t.setNPOctubre(Short.parseShort(mes10.toString()));
                    t.setNPNoviembre(Short.parseShort(mes11.toString()));
                    t.setNPDiciembre(Short.parseShort(mes12.toString()));
                    t.setTotal(Short.parseShort(totalProgramado.toString()));
                    poaSelectec.actualizaActividadesPoa(t);
                }
            });
        }
        resetearValores();
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
