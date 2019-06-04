package mx.edu.utxj.pye.sgi.controladores.poa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;

@Named
@ManagedBean
@ViewScoped
public class AreaPoaProgramacion implements Serializable {
// Listas de entities optimizadas
    @Getter    @Setter    private List<EjesRegistro> consultaListaEjesRegistro = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> consultaListaEstrategias = new ArrayList<>();
    @Getter    @Setter    private List<LineasAccion> consultaListaLineasAccion = new ArrayList<>();
    @Getter    @Setter    private List<UnidadMedidas> consultaListaUnidadMedidas = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> consultaListaActividadesPoa = new ArrayList<>();
    @Getter    @Setter    private List<CuadroMandoIntegral> consultaListaCuadroMandoIntegrals = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> listaActividadesPoasPadres = new ArrayList<>();

// Listas de entities 
// variables de datos Primitivos
    @Getter    @Setter    private Boolean unidadMedidaNueva = false, esActividadPrincipal = false, permitirRegistro = false, ejeActivo = false;
    @Getter    @Setter    private Short unidadDMedida = 0, ejercicioFiscal = 0, aumentoSubactividades = 1, aumentoActividades = 1;
    @Getter    @Setter    private Short numPm1 = 0, numPm2 = 0, numPm3 = 0, numPm4 = 0, numPm5 = 0, numPm6 = 0, numPm7 = 0, numPm8 = 0, numPm9 = 0, numPm10 = 0, numPm11 = 0, numPm12 = 0;
    @Getter    @Setter    private Short numPEm1 = 0, numPEm2 = 0, numPEm3 = 0, numPEm4 = 0, numPEm5 = 0, numPEm6 = 0, numPEm7 = 0, numPEm8 = 0, numPEm9 = 0, numPEm10 = 0, numPEm11 = 0, numPEm12 = 0;
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
    @Getter    @Setter    private List<ListaEjesEsLaAp> ejesEsLaAp = new ArrayList<>();
    @Getter    @Setter    private List<ListaEjeEstrategia> listaListaEjeEstrategia = new ArrayList<>();
    @Getter    @Setter    private List<ListaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();

    @EJB    EjbRegistroActividades registroActividades;
    @EJB    EjbCatalogosPoa catalogos;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA pOAUtilidades;

    @PostConstruct
    public void init() {
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa1();
        unidadDMedida = null;
        numPm1 = null;        numPm2 = null;        numPm3 = null;        numPm4 = null;        numPm5 = null;        numPm6 = null;
        numPm7 = null;        numPm8 = null;        numPm9 = null;        numPm10 = null;        numPm11 = null;        numPm12 = null;
        ejeActivo = false;
        consultarListasProcesoRegistro();
    }
//////////////////////////////////////////////////////////////////////////////// consultar listas
    public void consultarListasProcesoRegistro() {
        consultaListaEjesRegistro = new ArrayList<>();
        consultaListaEstrategias = new ArrayList<>();
        consultaListaLineasAccion = new ArrayList<>();

        consultaListaEjesRegistro.clear();
        consultaListaEstrategias.clear();
        consultaListaLineasAccion.clear();

        consultaListaEjesRegistro.add(new EjesRegistro(0, "Selecciones Uno", "Selecciones Uno", "Selecciones Uno", "Selecciones Uno"));
        consultaListaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
        consultaListaLineasAccion.add(new LineasAccion(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));

        consultaListaEjesRegistro.addAll(catalogos.mostrarEjesRegistros());

        consultarPOA(null, 1);
    }

    public void consultarPOA(EjesRegistro er, Integer tipo) {
        consultaListaActividadesPoa = new ArrayList<>();
        consultaListaUnidadMedidas = new ArrayList<>();
        listaActividadesPoasPadres = new ArrayList<>();
        ejesEsLaAp = new ArrayList<>();
        consultaListaActividadesPoa.clear();
        consultaListaUnidadMedidas.clear();
        listaActividadesPoasPadres.clear();
        ejesEsLaAp = new ArrayList<>();

        consultaListaUnidadMedidas.add(new UnidadMedidas(Short.parseShort("0"), "Nueva unidad de medida"));
        consultaListaUnidadMedidas.addAll(catalogos.mostrarUnidadMedidases());

        if (tipo == 1) {
            consultaListaActividadesPoa = registroActividades.mostrarActividadesPoasTotalArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);
        } else {
            consultaListaActividadesPoa = registroActividades.mostrarActividadesPoasEje(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal, ejesRegistro);
        }
        if (!consultaListaActividadesPoa.isEmpty()) {
            Collections.sort(consultaListaActividadesPoa, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
            consultaListaActividadesPoa.forEach((t) -> {
                if (t.getNumeroS() == 0) {
                    listaActividadesPoasPadres.add(t);
                }
            });
        }

        listaEstrategiaActividadesesEje = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        List<EjesRegistro> ers = new ArrayList<>();
        ers.clear();
        if (tipo == 1) {
            ers = catalogos.mostrarEjesRegistrosAreas(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa(), ejercicioFiscal);
        } else {
            ers.add(er);
        }
        if (!ers.isEmpty()) {
            ers.forEach((ej) -> {
                List<ListaEjeEstrategia> ess = new ArrayList<>();
                ess.clear();
                ess.add(new ListaEjeEstrategia(ej, catalogos.getEstarategiasPorEje(ej, ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa())));
                if (!ess.isEmpty()) {
                    ess.forEach((e) -> {
                        e.getListaEstrategiases1().forEach((t) -> {
                            List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                            listaActividadesPoasFiltradas.clear();
                            listaActividadesPoasFiltradas = registroActividades.getActividadesPoasEstarategias(t, e.getEjess(), ejercicioFiscal, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
                            listaEstrategiaActividadesesEje.add(new ListaEstrategiaActividades(t, listaActividadesPoasFiltradas));
                            Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                        });
                        ejesEsLaAp.add(new ListaEjesEsLaAp(ej, listaEstrategiaActividadesesEje));
                        listaEstrategiaActividadesesEje = new ArrayList<>();
                        listaEstrategiaActividadesesEje.clear();
                    });
                }
            });
        }
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
    }

//////////////////////////////////////////////////////////////////////////////// Registrar actividades
    public void anadirNuavUnidadDeMedida() {
        if ("Subactividad".equals(tipo)) {
            if (numeroActividadPrincipal == null || numeroActividadPrincipal == 0) {
                mensajeValidacion = "La actividad principal no puede quedar vacia";
                Ajax.oncomplete("PF('validacion').show();");
                return;
            }
        }
        if (permitirRegistro == false) {
            mensajeValidacion = "Seleccione una alineación estratégica";
            Ajax.oncomplete("PF('validacion').show();");
            return;
        }
        if (unidadDMedida == null) {
            mensajeValidacion = "Seleccione una unidad de medida";
            Ajax.oncomplete("PF('validacion').show();");
            return;
        }
        if (unidadDMedida == 0) {
            unidadExistente = 0;
            consultaListaUnidadMedidas.forEach((u) -> {
                if (u.getNombre().equalsIgnoreCase(nombreUnidad)) {
                    unidadExistente = unidadExistente + 1;
                }
            });
            if (unidadExistente != 0) {
                mensajeValidacion = "La unidad de medida ya existe, favor de seleccionarla del menú";
                Ajax.oncomplete("PF('validacion').show();");
                return;
            }
            unidadMedidas.setNombre(nombreUnidad);
            unidadMedidas = catalogos.agregarUnidadMedidas(unidadMedidas);
            unidadDMedida = unidadMedidas.getUnidadMedida();
        }
        anadirNuavActividad();
    }

    public void anadirNuavActividad() {
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;
        mes7 = 0;        mes8 = 0;        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;
        totalProgramado = 0;        numeroActividadPrincipal = 0;        numeroActividadSecuendaria = 0;

        if ("Actividad".equals(tipo)) {
            if (esActividadPrincipal) {
                actividadesPoa.setBandera("X");
            } else {
                actividadesPoa.setBandera("y");
            }
            actividadesPoa.setCuadroMandoInt(new CuadroMandoIntegral(cuadroMandoIntegral.getCuadroMandoInt()));
            Integer numeroP = registroActividades.mostrarActividadesPoaPrincipalesCuadroMando(cuadroMandoIntegral.getCuadroMandoInt(), controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()).size() + 1;
            actividadesPoa.setNumeroP(Short.parseShort(numeroP.toString()));
        } else {
            Integer numeroS = registroActividades.mostrarSubActividadesPoa(actividadPoaPrincipal.getActividadPoa()).size();
            actividadesPoa.setNumeroP(actividadPoaPrincipal.getNumeroP());
            actividadesPoa.setNumeroS(Short.parseShort(numeroS.toString()));
            actividadesPoa.setBandera("y");
            actividadesPoa.setActividadPadre(actividadPoaPrincipal.getActividadPoa());
            actividadesPoa.setCuadroMandoInt(new CuadroMandoIntegral(actividadPoaPrincipal.getCuadroMandoInt().getCuadroMandoInt()));
        }
        actividadesPoa.setEsPIDE("NO PIDE");
        actividadesPoa.setActividadPasada(false);
        actividadesPoa.setUnidadMedida(new UnidadMedidas(unidadDMedida));
        actividadesPoa.setArea(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        actividadesPoa = registroActividades.agregarActividadesPoa(actividadesPoa);

        if ("Subactividad".equals(tipo)) {
            actualizarNumeracionYTotales(actividadesPoa.getActividadPadre());
        } else {
            actualizarNumeracionActividadesPOA(actividadesPoa.getCuadroMandoInt().getCuadroMandoInt());
        }
        resetearValores();
        consultarPOA(ejesRegistro, 2);
    }

    public void asignarActividadPrincipal(ValueChangeEvent event) {
        numeroActividadPrincipal = Integer.parseInt(event.getNewValue().toString());
        actividadPoaPrincipal = registroActividades.mostrarActividadPoaPrincipal(numeroActividadPrincipal);
    }

    public void asignarNumerosProgramados(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "mes1":
                actividadesPoa.setNPEnero(comparacionValores(event.getNewValue(), 1));
                numPm1 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes2":
                actividadesPoa.setNPFebrero(comparacionValores(event.getNewValue(), 1));
                numPm2 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes3":
                actividadesPoa.setNPMarzo(comparacionValores(event.getNewValue(), 1));
                numPm3 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes4":
                actividadesPoa.setNPAbril(comparacionValores(event.getNewValue(), 1));
                numPm4 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes5":
                actividadesPoa.setNPMayo(comparacionValores(event.getNewValue(), 1));
                numPm5 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes6":
                actividadesPoa.setNPJunio(comparacionValores(event.getNewValue(), 1));
                numPm6 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes7":
                actividadesPoa.setNPJulio(comparacionValores(event.getNewValue(), 1));
                numPm7 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes8":
                actividadesPoa.setNPAgosto(comparacionValores(event.getNewValue(), 1));
                numPm8 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes9":
                actividadesPoa.setNPSeptiembre(comparacionValores(event.getNewValue(), 1));
                numPm9 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes10":
                actividadesPoa.setNPOctubre(comparacionValores(event.getNewValue(), 1));
                numPm10 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes11":
                actividadesPoa.setNPNoviembre(comparacionValores(event.getNewValue(), 1));
                numPm11 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes12":
                actividadesPoa.setNPDiciembre(comparacionValores(event.getNewValue(), 1));
                numPm12 = comparacionValores(event.getNewValue(), 2);
                break;
        }

        totalProgramado = actividadesPoa.getNPEnero() + actividadesPoa.getNPFebrero() + actividadesPoa.getNPMarzo() + actividadesPoa.getNPAbril() + actividadesPoa.getNPMayo() + actividadesPoa.getNPJunio() + actividadesPoa.getNPJulio() + actividadesPoa.getNPAgosto() + actividadesPoa.getNPSeptiembre() + actividadesPoa.getNPOctubre() + actividadesPoa.getNPNoviembre() + actividadesPoa.getNPDiciembre();
        actividadesPoa.setTotal(Short.parseShort(totalProgramado.toString()));
    }

//////////////////////////////////////////////////////////////////////////////// Ediatar Actividades
    public void buscarActividadParaEdicion(ActividadesPoa actividadesPoa) {
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
        tamaño = registroActividades.mostrarSubActividadesPoa(actividadesPoa.getActividadPoa()).size();
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
        if (actividadesPoa.getNPFebrero() != 0) {            numPEm2 = actividadesPoa.getNPFebrero();        }
        if (actividadesPoa.getNPMarzo() != 0) {            numPEm3 = actividadesPoa.getNPMarzo();        }
        if (actividadesPoa.getNPAbril() != 0) {            numPEm4 = actividadesPoa.getNPAbril();        }
        if (actividadesPoa.getNPMayo() != 0) {            numPEm5 = actividadesPoa.getNPMayo();        }
        if (actividadesPoa.getNPJunio() != 0) {            numPEm6 = actividadesPoa.getNPJunio();        }
        if (actividadesPoa.getNPJulio() != 0) {            numPEm7 = actividadesPoa.getNPJulio();        }
        if (actividadesPoa.getNPAgosto() != 0) {            numPEm8 = actividadesPoa.getNPAgosto();        }
        if (actividadesPoa.getNPSeptiembre() != 0) {            numPEm9 = actividadesPoa.getNPSeptiembre();        }
        if (actividadesPoa.getNPOctubre() != 0) {            numPEm10 = actividadesPoa.getNPOctubre();        }
        if (actividadesPoa.getNPNoviembre() != 0) {            numPEm11 = actividadesPoa.getNPNoviembre();        }
        if (actividadesPoa.getNPDiciembre() != 0) {            numPEm12 = actividadesPoa.getNPDiciembre();        }
    }

    public void actualizarActividadPoa() {
        totalProgramado = 0;
        totalProgramado = actividadPoaEditando.getNPEnero() + actividadPoaEditando.getNPFebrero() + actividadPoaEditando.getNPMarzo() + actividadPoaEditando.getNPAbril() + actividadPoaEditando.getNPMayo() + actividadPoaEditando.getNPJunio() + actividadPoaEditando.getNPJulio() + actividadPoaEditando.getNPAgosto() + actividadPoaEditando.getNPSeptiembre() + actividadPoaEditando.getNPOctubre() + actividadPoaEditando.getNPNoviembre() + actividadPoaEditando.getNPDiciembre();
        actividadPoaEditando.setTotal(Short.parseShort(totalProgramado.toString()));
        actividadPoaEditando.setUnidadMedida(new UnidadMedidas(unidadDMedida));
        actividadPoaEditando.setNumeroP(actividadPoaPrincipalEditada.getNumeroP());
        actividadPoaEditando.setCuadroMandoInt(new CuadroMandoIntegral(actividadPoaPrincipalEditada.getCuadroMandoInt().getCuadroMandoInt()));
        if (tipo.equals("Subactividad")) {
            actividadPoaEditando.setActividadPadre(numeroActividadPrincipal);
        }
        registroActividades.actualizaActividadesPoa(actividadPoaEditando);
        if (actividadPoaEditando.getNumeroS() != 0) {
            actualizarNumeracionYTotales(numeroActividadPrincipal);
        }
        if (!Objects.equals(numeroActividadPrincipal, numeroActividadPrincipalAnterior)) {
            actualizarNumeracionYTotales(numeroActividadPrincipalAnterior);
        }
        resetearValores();
        if (ejeActivo == false) {
            consultarPOA(null, 1);
            Faces.refresh();
        } else {
            consultarPOA(ejesRegistro, 2);
        }
    }

    public void asignarNumerosProgramadosEdicion(ValueChangeEvent event) {
        switch (event.getComponent().getId()) {
            case "mes1":
                actividadPoaEditando.setNPEnero(comparacionValores(event.getNewValue(), 1));
                numPEm1 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes2":
                actividadPoaEditando.setNPFebrero(comparacionValores(event.getNewValue(), 1));
                numPEm2 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes3":
                actividadPoaEditando.setNPMarzo(comparacionValores(event.getNewValue(), 1));
                numPEm3 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes4":
                actividadPoaEditando.setNPAbril(comparacionValores(event.getNewValue(), 1));
                numPEm4 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes5":
                actividadPoaEditando.setNPMayo(comparacionValores(event.getNewValue(), 1));
                numPEm5 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes6":
                actividadPoaEditando.setNPJunio(comparacionValores(event.getNewValue(), 1));
                numPEm6 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes7":
                actividadPoaEditando.setNPJulio(comparacionValores(event.getNewValue(), 1));
                numPEm7 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes8":
                actividadPoaEditando.setNPAgosto(comparacionValores(event.getNewValue(), 1));
                numPEm8 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes9":
                actividadPoaEditando.setNPSeptiembre(comparacionValores(event.getNewValue(), 1));
                numPEm9 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes10":
                actividadPoaEditando.setNPOctubre(comparacionValores(event.getNewValue(), 1));
                numPEm10 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes11":
                actividadPoaEditando.setNPNoviembre(comparacionValores(event.getNewValue(), 1));
                numPEm11 = comparacionValores(event.getNewValue(), 2);
                break;
            case "mes12":
                actividadPoaEditando.setNPDiciembre(comparacionValores(event.getNewValue(), 1));
                numPEm12 = comparacionValores(event.getNewValue(), 2);
                break;
        }
        totalProgramado = actividadPoaEditando.getNPEnero() + actividadPoaEditando.getNPFebrero() + actividadPoaEditando.getNPMarzo() + actividadPoaEditando.getNPAbril() + actividadPoaEditando.getNPMayo() + actividadPoaEditando.getNPJunio() + actividadPoaEditando.getNPJulio() + actividadPoaEditando.getNPAgosto() + actividadPoaEditando.getNPSeptiembre() + actividadPoaEditando.getNPOctubre() + actividadPoaEditando.getNPNoviembre() + actividadPoaEditando.getNPDiciembre();
        actividadPoaEditando.setTotal(Short.parseShort(totalProgramado.toString()));
    }

    public void asignarNumeroP(ValueChangeEvent event) {
        numeroActividadPrincipal = 0;
        numeroActividadPrincipalAnterior = 0;
        actividadPoaPrincipalEditada = new ActividadesPoa();
        actividadPoaPrincipalEditadaAnterior = new ActividadesPoa();
        numeroActividadPrincipal = Integer.parseInt(event.getNewValue().toString());
        numeroActividadPrincipalAnterior = Integer.parseInt(event.getOldValue().toString());
        actividadPoaPrincipalEditada = registroActividades.mostrarActividadPoaPrincipal(numeroActividadPrincipal);
        actividadPoaPrincipalEditadaAnterior = registroActividades.mostrarActividadPoaPrincipal(numeroActividadPrincipalAnterior);
    }

//////////////////////////////////////////////////////////////////////////////// Elinimar actividades
    public void buscarActividadeaEliminar(ActividadesPoa actividadesPoa) {
        actividadPoaEliminado = new ActividadesPoa();
        actividadPoaEliminado = actividadesPoa;
    }

    public void eliminarActividad() {
        /////////// reseteo valores
        numeroActividadPrincipal = 1;
        numeroActividadSecuendaria = 1;
        listaActividadesPoasPadres.clear();
        ///////////////
        actividadPoaEliminada = new ActividadesPoa();
        actividadPoaEliminada = actividadPoaEliminado;
        registroActividades.eliminarActividadesPoa(actividadPoaEliminado);

        if (actividadPoaEliminada.getNumeroS() == 0) {
            List<ActividadesPoa> subActividadesPoas = new ArrayList<>();
            subActividadesPoas = registroActividades.mostrarSubActividadesPoa(actividadPoaEliminada.getActividadPoa());
            if (!subActividadesPoas.isEmpty()) {
                subActividadesPoas.forEach((t) -> {
                    registroActividades.eliminarActividadesPoa(t);
                });
            }
            actualizarNumeracionActividadesPOA(actividadPoaEliminada.getCuadroMandoInt().getCuadroMandoInt());
        } else {
            actualizarNumeracionYTotales(actividadPoaEliminada.getActividadPadre());
        }

        resetearValores();
        if (ejeActivo == false) {
            Faces.refresh();
        } else {
            consultarPOA(ejesRegistro, 2);
        }
    }

//////////////////////////////////////////////////////////////////////////////// Utilidades
    public void actualizarNumeracionActividadesPOA(Integer cuadroDeMando) {
        aumentoActividades = 1;
        List<ActividadesPoa> actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        actividadesPoas = registroActividades.mostrarActividadesPoaPrincipalesCuadroMando(cuadroDeMando, controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa());
        if (!actividadesPoas.isEmpty()) {
            actividadesPoas.forEach((t) -> {
                t.setNumeroP(aumentoActividades);
                registroActividades.actualizaActividadesPoa(t);
                actualizarNumeracionYTotales(t.getActividadPoa());
                aumentoActividades++;
            });
        }
    }

    public void actualizarNumeracionYTotales(Integer claveActividadPOa) {
        mes1 = 0;        mes2 = 0;        mes3 = 0;        mes4 = 0;        mes5 = 0;        mes6 = 0;
        mes7 = 0;        mes8 = 0;        mes9 = 0;        mes10 = 0;        mes11 = 0;        mes12 = 0;
        totalProgramado = 0;
        ActividadesPoa actividadesPoas = new ActividadesPoa();
        List<ActividadesPoa> subActividadesPoas = new ArrayList<>();
        aumentoSubactividades = 1;
        actividadesPoas = registroActividades.mostrarActividadPoaPrincipal(claveActividadPOa);
        subActividadesPoas = registroActividades.mostrarSubActividadesPoa(claveActividadPOa);

        final Short numeroP = actividadesPoas.getNumeroP();

        if (!subActividadesPoas.isEmpty()) {
            subActividadesPoas.forEach((t) -> {
                t.setNumeroP(numeroP);
                t.setNumeroS(aumentoSubactividades);
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

                registroActividades.actualizaActividadesPoa(t);
                aumentoSubactividades++;
            });
            actividadesPoas.setBandera("x");
        } else {
            if (actividadesPoas.getBandera().equals("y")) {
                mes1 = mes1 + actividadesPoas.getNPEnero();
                mes2 = mes2 + actividadesPoas.getNPFebrero();
                mes3 = mes3 + actividadesPoas.getNPMarzo();
                mes4 = mes4 + actividadesPoas.getNPAbril();
                mes5 = mes5 + actividadesPoas.getNPMayo();
                mes6 = mes6 + actividadesPoas.getNPJunio();
                mes7 = mes7 + actividadesPoas.getNPJulio();
                mes8 = mes8 + actividadesPoas.getNPAgosto();
                mes9 = mes9 + actividadesPoas.getNPSeptiembre();
                mes10 = mes10 + actividadesPoas.getNPOctubre();
                mes11 = mes11 + actividadesPoas.getNPNoviembre();
                mes12 = mes12 + actividadesPoas.getNPDiciembre();
            }
            actividadesPoas.setBandera("y");
        }
        totalProgramado = mes1 + mes2 + mes3 + mes4 + mes5 + mes6 + mes7 + mes8 + mes9 + mes10 + mes11 + mes12;
        actividadesPoas.setNPEnero(Short.parseShort(mes1.toString()));
        actividadesPoas.setNPFebrero(Short.parseShort(mes2.toString()));
        actividadesPoas.setNPMarzo(Short.parseShort(mes3.toString()));
        actividadesPoas.setNPAbril(Short.parseShort(mes4.toString()));
        actividadesPoas.setNPMayo(Short.parseShort(mes5.toString()));
        actividadesPoas.setNPJunio(Short.parseShort(mes6.toString()));
        actividadesPoas.setNPJulio(Short.parseShort(mes7.toString()));
        actividadesPoas.setNPAgosto(Short.parseShort(mes8.toString()));
        actividadesPoas.setNPSeptiembre(Short.parseShort(mes9.toString()));
        actividadesPoas.setNPOctubre(Short.parseShort(mes10.toString()));
        actividadesPoas.setNPNoviembre(Short.parseShort(mes11.toString()));
        actividadesPoas.setNPDiciembre(Short.parseShort(mes12.toString()));
        actividadesPoas.setTotal(Short.parseShort(totalProgramado.toString()));
        registroActividades.actualizaActividadesPoa(actividadesPoas);
    }

    public Short comparacionValores(Object valor, Integer tipoValor) {
        Short valor1 = 0;
        Short valor2 = 0;
        if (valor == null) {
            valor1 = 0;
            valor2 = null;
        } else {
            if (Integer.parseInt(valor.toString()) != 0) {
                valor2 = valor1 = Short.parseShort(valor.toString());
            } else {
                valor1 = 0;
                valor2 = null;
            }
        }
        if (tipoValor == 1) {
            return valor1;
        } else {
            return valor2;
        }
    }

    public void asignarParametrosRegistro(ValueChangeEvent event) {
        if (Short.parseShort(event.getNewValue().toString()) != Short.parseShort("0")) {
            switch (event.getComponent().getId()) {
                case "eje":
                    ejesRegistro = new EjesRegistro();
                    estrategias = new Estrategias();
                    lineasAccion = new LineasAccion();
                    ejesRegistro = catalogos.mostrarEjeRegistro(Integer.parseInt(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        consultaListaEstrategias.clear();
                        consultaListaEstrategias.add(new Estrategias(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        catalogos.mostrarEstrategiasRegistro(ejercicioFiscal, ejesRegistro).forEach((t) -> {
                            consultaListaEstrategias.add(t);
                        });
                        consultarPOA(ejesRegistro, 2);
                        permitirRegistro = false;
                    }
                    resetearValores();
                    break;
                case "estrategia":
                    estrategias = new Estrategias();
                    lineasAccion = new LineasAccion();
                    estrategias = catalogos.mostrarEstrategia(Short.parseShort(event.getNewValue().toString()));
                    if (ejesRegistro != null) {
                        consultaListaLineasAccion.clear();
                        consultaListaLineasAccion.add(new LineasAccion(Short.parseShort("0"), Short.parseShort("0"), "Selecciones Uno"));
                        catalogos.mostrarLineasAccionRegistro(ejercicioFiscal, ejesRegistro, estrategias).forEach((t) -> {
                            consultaListaLineasAccion.add(t);
                        });
                        permitirRegistro = false;
                    }
                    resetearValores();
                    break;
                case "lineaAccion":
                    lineasAccion = new LineasAccion();
                    lineasAccion = catalogos.mostrarLineaAccion(Short.parseShort(event.getNewValue().toString()));
                    if (lineasAccion != null) {
                        cuadroMandoIntegral = new CuadroMandoIntegral();
                        consultaListaCuadroMandoIntegrals.clear();
                        consultaListaCuadroMandoIntegrals = catalogos.mostrarCuadroMandoIntegralRegistrpo(ejercicioFiscal, ejesRegistro, estrategias, lineasAccion);
                        cuadroMandoIntegral = consultaListaCuadroMandoIntegrals.get(0);
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
            consultaListaEstrategias.clear();
            consultaListaLineasAccion.clear();
            consultaListaCuadroMandoIntegrals.clear();
            consultarListasProcesoRegistro();
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

    public void resetearValores() {
        actividadesPoa = new ActividadesPoa();
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
            actividadesPoa.setNPEnero(Short.parseShort("0"));
            actividadesPoa.setNPFebrero(Short.parseShort("0"));
            actividadesPoa.setNPMarzo(Short.parseShort("0"));
            actividadesPoa.setNPAbril(Short.parseShort("0"));
            actividadesPoa.setNPMayo(Short.parseShort("0"));
            actividadesPoa.setNPJunio(Short.parseShort("0"));
            actividadesPoa.setNPJulio(Short.parseShort("0"));
            actividadesPoa.setNPAgosto(Short.parseShort("0"));
            actividadesPoa.setNPSeptiembre(Short.parseShort("0"));
            actividadesPoa.setNPOctubre(Short.parseShort("0"));
            actividadesPoa.setNPNoviembre(Short.parseShort("0"));
            actividadesPoa.setNPDiciembre(Short.parseShort("0"));
            totalProgramado = actividadesPoa.getNPEnero() + actividadesPoa.getNPFebrero() + actividadesPoa.getNPMarzo() + actividadesPoa.getNPAbril() + actividadesPoa.getNPMayo() + actividadesPoa.getNPJunio() + actividadesPoa.getNPJulio() + actividadesPoa.getNPAgosto() + actividadesPoa.getNPSeptiembre() + actividadesPoa.getNPOctubre() + actividadesPoa.getNPNoviembre() + actividadesPoa.getNPDiciembre();
            actividadesPoa.setTotal(Short.parseShort(totalProgramado.toString()));
        }
    }

    public void actividadTipo(ValueChangeEvent event) {
        tipo = "";
        tipo = event.getNewValue().toString();
        esActividadPrincipal = false;
    }

    public void recargarPag() {
        Faces.refresh();
    }

    public void imprimirValores() {}
    //////////////////////////////////////////////////////////////////////////// DTO'S

    public static class ListaEjesEsLaAp {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<ListaEstrategiaActividades> listalistaEstrategiaLaAp;

        public ListaEjesEsLaAp(EjesRegistro ejeA, List<ListaEstrategiaActividades> listalistaEstrategiaLaAp) {
            this.ejeA = ejeA;
            this.listalistaEstrategiaLaAp = listalistaEstrategiaLaAp;
        }
    }

    public static class ListaEjeEstrategia {

        @Getter        @Setter        private EjesRegistro ejess;
        @Getter        @Setter        private List<Estrategias> listaEstrategiases1;

        public ListaEjeEstrategia(EjesRegistro ejess, List<Estrategias> listaEstrategiases1) {
            this.ejess = ejess;
            this.listaEstrategiases1 = listaEstrategiases1;
        }
    }

    public static class ListaEstrategiaActividades {

        @Getter        @Setter        private Estrategias estrategias;
        @Getter        @Setter        private List<ActividadesPoa> actividadesPoas;

        public ListaEstrategiaActividades(Estrategias estrategias, List<ActividadesPoa> actividadesPoas) {
            this.estrategias = estrategias;
            this.actividadesPoas = actividadesPoas;
        }
    }
}
