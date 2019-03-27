package mx.edu.utxj.pye.sgi.controladores.cmi;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.model.chart.MeterGaugeChartModel;

@Named
@ManagedBean
@ViewScoped
public class cuadroMandoIntegralUniversidad implements Serializable {

    @Getter    @Setter    private MeterGaugeChartModel meterGaugeModel1;

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> actividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> ejesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<resultadosCMI> cMIs = new ArrayList<>();
    @Getter    @Setter    private List<String> resultados = new ArrayList<>();

    @Getter    @Setter    private List<Proyectos> proyectoses = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> estrategiases = new ArrayList<>();

    @Getter    @Setter    private resultadosCMI cMIArea;
    @Getter    @Setter    private Integer programadas = 0, realizadas = 0, incremento = 0;
    @Getter    @Setter    private Integer alca = 0, progra = 0,numeroMes=0;
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Double avance = 0D;
    @Getter    @Setter    private String mes = "", mesNombre = "";
    @Getter    @Setter    private List<grafica> graf = new ArrayList<>();
    @Getter    @Setter    private List<grafica> graficaProyecto = new ArrayList<>();
    @Getter    @Setter    private MeterGaugeChartModel grafRA;
    @Getter    @Setter    private Date fechaActual = new Date();
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");

    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp = new ArrayList<>();
    @Getter    @Setter    private List<listaProyectosEstrategias> proyectosEstrategiases = new ArrayList<>();
    @Getter    @Setter    private List<listaEjeProyectos> ejeProyectoses = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();

    @EJB    EjbPoaSelectec poaSelectec;
    @EJB    EjbAreasLogeo areasLogeo;
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("cuadroMandoIntegralUniversidad Inicio: " + System.currentTimeMillis());
        numeroMes=fechaActual.getMonth();
//        numeroMes=0;
        if (numeroMes == 0) {
            System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralArea.init()");
            numeroMes=11;
            ejercicioFiscal = Short.parseShort(String.valueOf(fechaActual.getYear() - 102));
        } else {
            numeroMes=numeroMes-1;
            ejercicioFiscal = Short.parseShort(String.valueOf(fechaActual.getYear() - 101));
        }
        switch (numeroMes) {
            case 0:                mes = "Avance al mes de: Enero";                break;
            case 1:                mes = "Avance al mes de: Febrero";                break;
            case 2:                mes = "Avance al mes de: Marzo";                break;
            case 3:                mes = "Avance al mes de: Abril";                break;
            case 4:                mes = "Avance al mes de: Mayo";                break;
            case 5:                mes = "Avance al mes de: Junio";                break;
            case 6:                mes = "Avance al mes de: Julio";                break;
            case 7:                mes = "Avance al mes de: Agosto";                break;
            case 8:                mes = "Avance al mes de: Septiembre";                break;
            case 9:                mes = "Avance al mes de: Octubre";                break;
            case 10:                mes = "Avance al mes de: Noviembre";                break;
            case 11:                mes = "Avance al mes de: Diciembre del 20"+(fechaActual.getYear()-101);                break;
        }
        cmiPorArea();
        cmiPorEje();
        actividadesProyecto();
        System.out.println("cuadroMandoIntegralUniversidad Fin: " + System.currentTimeMillis());
    }

    public void calculosCMI(List<ActividadesPoa> actividadesPoa) {
        programadas = 0;
        realizadas = 0;
        avance = 0D;
        graf = new ArrayList<>();
        graf.clear();
        if (!actividadesPoa.isEmpty()) {
            actividadesPoa.forEach((t) -> {
                if (t.getBandera().equals("y")) {
                    Integer tP = 0;
                    Integer tR = 0;
                    switch (numeroMes) {
                        case 0:
                            tR = 0 + t.getNAEnero();
                            tP = 0 + t.getNPEnero();
                            break;
                        case 1:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero();
                            break;
                        case 2:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo();
                            break;
                        case 3:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril();
                            break;
                        case 4:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo();
                            break;
                        case 5:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio();
                            break;
                        case 6:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio();
                            break;
                        case 7:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto();
                            break;
                        case 8:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre();
                            break;
                        case 9:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre() + t.getNAOctubre();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre() + t.getNPOctubre();
                            break;
                        case 10:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre() + t.getNAOctubre() + t.getNANoviembre();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre() + t.getNPOctubre() + t.getNPNoviembre();
                            break;
                        case 11:
                            tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre() + t.getNAOctubre() + t.getNANoviembre() + t.getNADiciembre();
                            tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre() + t.getNPOctubre() + t.getNPNoviembre() + t.getNPDiciembre();
                            break;
                    }
                    if(tP!=0){                        
                    programadas=programadas+1;
                    }
                    if (tR != 0 && Objects.equals(tR, tP)) {
                        realizadas++;
                    }
                }
            });
            
            for (incremento = 0; incremento <= numeroMes; incremento++) {
                mesNombre = "";
                alca = 0;
                progra = 0;
                actividadesPoa.forEach((t) -> {
                    if (t.getBandera().equals("y")) {
                        Integer tP = 0;
                        Integer tR = 0;
                        switch (incremento) {
                            case 0:
                                mesNombre = "Enero";
                                tR = 0 + t.getNAEnero();
                                tP = 0 + t.getNPEnero();
                                break;
                            case 1:
                                mesNombre = "Febrero";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero();
                                break;
                            case 2:
                                mesNombre = "Marzo";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo();
                                break;
                            case 3:
                                mesNombre = "Abril";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril();
                                break;
                            case 4:
                                mesNombre = "Mayo";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo();
                                break;
                            case 5:
                                mesNombre = "Junio";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio();
                                break;
                            case 6:
                                mesNombre = "Julio";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio();
                                break;
                            case 7:
                                mesNombre = "Agosto";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto();
                                break;
                            case 8:
                                mesNombre = "Septiembre";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre();
                                break;
                            case 9:
                                mesNombre = "Octubre";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre() + t.getNAOctubre();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre() + t.getNPOctubre();
                                break;
                            case 10:
                                mesNombre = "Noviembre";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre() + t.getNAOctubre() + t.getNANoviembre();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre() + t.getNPOctubre() + t.getNPNoviembre();
                                break;
                            case 11:
                                mesNombre = "Diciembre";
                                tR = 0 + t.getNAEnero() + t.getNAFebrero() + t.getNAMarzo() + t.getNAAbril() + t.getNAMayo() + t.getNAJunio() + t.getNAJulio() + t.getNAAgosto() + t.getNASeptiembre() + t.getNAOctubre() + t.getNANoviembre() + t.getNADiciembre();
                                tP = 0 + t.getNPEnero() + t.getNPFebrero() + t.getNPMarzo() + t.getNPAbril() + t.getNPMayo() + t.getNPJunio() + t.getNPJulio() + t.getNPAgosto() + t.getNPSeptiembre() + t.getNPOctubre() + t.getNPNoviembre() + t.getNPDiciembre();
                                break;
                        }
                        if (tP != 0) {
                            progra=progra+1;
                        }
                        if (tR != 0 && Objects.equals(tR, tP)) {
                            alca++;
                        }
                    }
                });
                graf.add(new grafica(mesNombre, (alca.doubleValue() / progra.doubleValue()) * 100D));
            }
            avance = (Double.parseDouble(realizadas.toString()) / Double.parseDouble(programadas.toString()) * 100D);
        }
    }

    public List<ActividadesPoa> actividadesFiltradas(List<ActividadesPoa> actividadesPoas) {
        List<ActividadesPoa> actividadesPoasRegistradas = new ArrayList<>();
        actividadesPoasRegistradas.clear();
        if (!actividadesPoas.isEmpty()) {
            actividadesPoas.forEach((t) -> {
                CuadroMandoIntegral cmi = new CuadroMandoIntegral();
                cmi = t.getCuadroMandoInt();
//                if (cmi.getProyecto() != null) {
                    actividadesPoasRegistradas.add(t);
//                }
            });
        }
        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralUniversidad.actividadesFiltradas()"+actividadesPoasRegistradas.size());
        return actividadesPoasRegistradas;
    }

    public void graficaProyecto(Proyectos proyectos, EjesRegistro er) {
        graficaProyecto = new ArrayList<>();
        graficaProyecto.clear();
        List<ActividadesPoa> aps = new ArrayList<>();
        aps = actividadesFiltradas(poaSelectec.getActividadesPoasProyectoGrfica(er, proyectos, ejercicioFiscal));
        calculosCMI(aps);
        graficaProyecto = graf;
    }

    public void cmiPorEje() {
        ejesRegistros = new ArrayList<>();
        cMIs = new ArrayList<>();
        ejesRegistros.clear();
        cMIs.clear();
        ejesRegistros = poaSelectec.mostrarEjesRegistros();
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((t) -> {
                actividadesPoas = new ArrayList<>();
                actividadesPoas.clear();
                actividadesPoas = poaSelectec.mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(ejercicioFiscal, t);
                if (!actividadesPoas.isEmpty()) {
                    List<ActividadesPoa> actividadesPoasFiltradas = new ArrayList<>();
                    actividadesPoasFiltradas.clear();
                    actividadesPoasFiltradas = actividadesFiltradas(actividadesPoas);
                    if (!actividadesPoasFiltradas.isEmpty()) {
                        calculosCMI(actividadesPoasFiltradas);
                        if (programadas != 0) {
                            grafRA = initMeterGaugeModel();
                            grafRA.setTitle(mes);
                            grafRA.setSeriesColors("FF0000,ffff00,66ff33");
                            grafRA.setGaugeLabel(df.format((realizadas.doubleValue() / programadas.doubleValue()) * 100D) + " % avance");
                            grafRA.setGaugeLabelPosition("bottom");
                            grafRA.setShowTickLabels(true);
                            grafRA.setLabelHeightAdjust(10);
                            grafRA.setIntervalOuterRadius(100);
                            cMIs.add(new resultadosCMI(t.getNombre(), programadas, realizadas, avance, graf, grafRA));
                        }
                    }
                }
            });
        }
    }

    public void cmiPorArea() {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        actividadesPoas = poaSelectec.mostrarActividadesPoasUniversidadaEjercicioFiscal(ejercicioFiscal);
        cMIArea = new resultadosCMI(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativaNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
        if (!actividadesPoas.isEmpty()) {            
            List<ActividadesPoa> actividadesPoasFiltradas = new ArrayList<>();
            actividadesPoasFiltradas.clear();
            actividadesPoasFiltradas = actividadesFiltradas(actividadesPoas);
            if (!actividadesPoasFiltradas.isEmpty()) {
                calculosCMI(actividadesPoasFiltradas);
                grafRA = initMeterGaugeModel();
                grafRA.setTitle(mes);
                grafRA.setSeriesColors("FF0000,ffff00,66ff33");
                grafRA.setGaugeLabel(df.format((realizadas.doubleValue() / programadas.doubleValue()) * 100D) + " % avance");
                grafRA.setGaugeLabelPosition("bottom");
                grafRA.setShowTickLabels(true);
                grafRA.setLabelHeightAdjust(10);
                grafRA.setIntervalOuterRadius(100);
                cMIArea = new resultadosCMI(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativaNombre(), programadas, realizadas, avance,graf, grafRA);
            }
        }
    }

    private MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>() {
            {
                add(90.0);
                add(95.0);
                add(115.0);
            }
        };
        return new MeterGaugeChartModel((realizadas.doubleValue()/programadas.doubleValue())*100D, intervals);
    }

    public void actividadesProyecto() {
        listaEstrategiaActividadesesEje = new ArrayList<>();
        proyectosEstrategiases = new ArrayList<>();
        ejeProyectoses = new ArrayList<>();
        estrategiases = new ArrayList<>();
        ejesRegistros = new ArrayList<>();
        proyectoses = new ArrayList<>();
        ejesEsLaAp = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        proyectosEstrategiases.clear();
        ejeProyectoses.clear();
        estrategiases.clear();
        ejesRegistros.clear();
        proyectoses.clear();
        ejesEsLaAp.clear();
        ejesRegistros = poaSelectec.mostrarEjesRegistros();
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((ej) -> {
                proyectoses = new ArrayList<>();
                proyectoses.clear();
                proyectoses = poaSelectec.getProyectos(ej, ejercicioFiscal);
                if (!proyectoses.isEmpty()) {
                    proyectoses.forEach((pr) -> {
                        estrategiases = new ArrayList<>();
                        estrategiases.clear();
                        estrategiases = poaSelectec.getEstrategiaProyectos(pr, ejercicioFiscal);
                        if (!estrategiases.isEmpty()) {
                            estrategiases.forEach((es) -> {
                                List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                                listaActividadesPoasFiltradas.clear();
                                listaActividadesPoasFiltradas = actividadesFiltradas(poaSelectec.getActividadesPoasProyecto(es, ej, pr, ejercicioFiscal));
                                if (!listaActividadesPoasFiltradas.isEmpty()) {
                                    listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(es, listaActividadesPoasFiltradas));
                                    Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                                    calculosCMI(listaActividadesPoasFiltradas);
                                }
                            });
                            if (!listaEstrategiaActividadesesEje.isEmpty()) {
                                proyectosEstrategiases.add(new listaProyectosEstrategias(pr, programadas, realizadas, avance, semaforo(programadas, realizadas), listaEstrategiaActividadesesEje));
                            }
                            listaEstrategiaActividadesesEje = new ArrayList<>();
                            listaEstrategiaActividadesesEje.clear();
                        }
                    });
                    if (!proyectosEstrategiases.isEmpty()) {
                        ejesEsLaAp.add(new listaEjesEsLaAp(ej, proyectosEstrategiases));
                    }
                    proyectosEstrategiases = new ArrayList<>();
                    proyectosEstrategiases.clear();
                }
            });
        }
        Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
    }

    public Integer totalProgramado(ActividadesPoa actividadesPoas) {
        Integer totalPCorte = 0;
        switch (numeroMes) {
            case 0:
                totalPCorte = 0 + actividadesPoas.getNPEnero();
                break;
            case 1:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero();
                break;
            case 2:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo();
                break;
            case 3:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril();
                break;
            case 4:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo();
                break;
            case 5:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo() + actividadesPoas.getNPJunio();
                break;
            case 6:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo() + actividadesPoas.getNPJunio() + actividadesPoas.getNPJulio();
                break;
            case 7:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo() + actividadesPoas.getNPJunio() + actividadesPoas.getNPJulio() + actividadesPoas.getNPAgosto();
                break;
            case 8:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo() + actividadesPoas.getNPJunio() + actividadesPoas.getNPJulio() + actividadesPoas.getNPAgosto() + actividadesPoas.getNPSeptiembre();
                break;
            case 9:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo() + actividadesPoas.getNPJunio() + actividadesPoas.getNPJulio() + actividadesPoas.getNPAgosto() + actividadesPoas.getNPSeptiembre() + actividadesPoas.getNPOctubre();
                break;
            case 10:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo() + actividadesPoas.getNPJunio() + actividadesPoas.getNPJulio() + actividadesPoas.getNPAgosto() + actividadesPoas.getNPSeptiembre() + actividadesPoas.getNPOctubre() + actividadesPoas.getNPNoviembre();
                break;
            case 11:
                totalPCorte = actividadesPoas.getNPEnero() + actividadesPoas.getNPFebrero() + actividadesPoas.getNPMarzo() + actividadesPoas.getNPAbril() + actividadesPoas.getNPMayo() + actividadesPoas.getNPJunio() + actividadesPoas.getNPJulio() + actividadesPoas.getNPAgosto() + actividadesPoas.getNPSeptiembre() + actividadesPoas.getNPOctubre() + actividadesPoas.getNPNoviembre() + actividadesPoas.getNPDiciembre();
                break;
        }
        return totalPCorte;
    }

    public Integer totalAlcanzado(ActividadesPoa actividadesPoas) {
        Integer totalACorte = 0;
        switch (numeroMes) {
            case 0:
                totalACorte = 0 + actividadesPoas.getNAEnero();
                break;
            case 1:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero();
                break;
            case 2:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo();
                break;
            case 3:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril();
                break;
            case 4:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo();
                break;
            case 5:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo() + actividadesPoas.getNAJunio();
                break;
            case 6:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo() + actividadesPoas.getNAJunio() + actividadesPoas.getNAJulio();
                break;
            case 7:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo() + actividadesPoas.getNAJunio() + actividadesPoas.getNAJulio() + actividadesPoas.getNAAgosto();
                break;
            case 8:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo() + actividadesPoas.getNAJunio() + actividadesPoas.getNAJulio() + actividadesPoas.getNAAgosto() + actividadesPoas.getNASeptiembre();
                break;
            case 9:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo() + actividadesPoas.getNAJunio() + actividadesPoas.getNAJulio() + actividadesPoas.getNAAgosto() + actividadesPoas.getNASeptiembre() + actividadesPoas.getNAOctubre();
                break;
            case 10:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo() + actividadesPoas.getNAJunio() + actividadesPoas.getNAJulio() + actividadesPoas.getNAAgosto() + actividadesPoas.getNASeptiembre() + actividadesPoas.getNAOctubre() + actividadesPoas.getNANoviembre();
                break;
            case 11:
                totalACorte = actividadesPoas.getNAEnero() + actividadesPoas.getNAFebrero() + actividadesPoas.getNAMarzo() + actividadesPoas.getNAAbril() + actividadesPoas.getNAMayo() + actividadesPoas.getNAJunio() + actividadesPoas.getNAJulio() + actividadesPoas.getNAAgosto() + actividadesPoas.getNASeptiembre() + actividadesPoas.getNAOctubre() + actividadesPoas.getNANoviembre() + actividadesPoas.getNADiciembre();
                break;
        }
        return totalACorte;
    }

    public String semaforo(Integer totalP, Integer totalA) {
        Double totalPCorte = 0D;
        Double totalACorte = 0D;
        totalACorte = totalP.doubleValue();
        totalPCorte = totalA.doubleValue();
        String semaforoG = "";
        Double porcentejeAlCorte = 0D;
        if (totalPCorte.equals(totalACorte)) {
            porcentejeAlCorte = 100.0;
        } else {
            if (totalPCorte == 0D || totalACorte == 0D) {

                if (totalACorte == 1D) {
                    porcentejeAlCorte = 84.99;
                } else if (totalACorte == 2D) {
                    porcentejeAlCorte = 74.99;
                } else if (totalACorte == 3D) {
                    porcentejeAlCorte = 69.99;
                }

            } else {
                porcentejeAlCorte = (totalPCorte / totalACorte) * 100;
            }
            if (porcentejeAlCorte >= 116 && porcentejeAlCorte <= 165) {
                porcentejeAlCorte = 89.99;
            } else if (porcentejeAlCorte >= 166 && porcentejeAlCorte <= 215) {
                porcentejeAlCorte = 84.99;
            } else if (porcentejeAlCorte >= 216 && porcentejeAlCorte <= 265) {
                porcentejeAlCorte = 79.99;
            } else if (porcentejeAlCorte >= 266 && porcentejeAlCorte <= 315) {
                porcentejeAlCorte = 74.99;
            } else if (porcentejeAlCorte >= 316) {
                porcentejeAlCorte = 69.99;
            }
        }
        if (totalPCorte == 0D && totalACorte == 0D) {
            semaforoG = "NE";
        } else {
            if (porcentejeAlCorte >= 0D && porcentejeAlCorte <= 89.99) {
                semaforoG = "semaforoRojo";
            } else if (porcentejeAlCorte >= 90D && porcentejeAlCorte <= 94.99) {
                semaforoG = "semaforoAmarillo";
            } else if (porcentejeAlCorte >= 95D && porcentejeAlCorte <= 115.99) {
                semaforoG = "semaforoVerde";
            }
        }
        return semaforoG;
    }

    public String porcentaje(Double totalPCorte, Double totalACorte) {
        Double porcentejeAlCorte = 0D;
        if (totalPCorte.equals(totalACorte)) {
            porcentejeAlCorte = 100.0;
        } else {
            if (totalPCorte == 0D || totalACorte == 0D) {
                if (totalACorte == 1D) {
                    porcentejeAlCorte = 84.99;
                } else if (totalACorte == 2D) {
                    porcentejeAlCorte = 74.99;
                } else if (totalACorte == 3D) {
                    porcentejeAlCorte = 69.99;
                }
            } else {
                porcentejeAlCorte = (totalPCorte / totalACorte) * 100;
            }
            if (porcentejeAlCorte >= 116 && porcentejeAlCorte <= 165) {
                porcentejeAlCorte = 89.99;
            } else if (porcentejeAlCorte >= 166 && porcentejeAlCorte <= 215) {
                porcentejeAlCorte = 84.99;
            } else if (porcentejeAlCorte >= 216 && porcentejeAlCorte <= 265) {
                porcentejeAlCorte = 79.99;
            } else if (porcentejeAlCorte >= 266 && porcentejeAlCorte <= 315) {
                porcentejeAlCorte = 74.99;
            } else if (porcentejeAlCorte >= 316) {
                porcentejeAlCorte = 69.99;
            }
        }
        if (totalPCorte == 0D && totalACorte == 0D) {
            return "No evaluable";
        } else {
            return porcentejeAlCorte.toString();
        }
    }

    public String areasNombre(Short calveA) {
        try {
            AreasUniversidad universidad = new AreasUniversidad();
            universidad = areasLogeo.mostrarAreasUniversidad(calveA);
            return universidad.getNombre();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(cuadroMandoIntegralUniversidad.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
        }
    }

    public static class resultadosCMI {

        @Getter        @Setter        private String ejesRegistro;
        @Getter        @Setter        private Integer programadas;
        @Getter        @Setter        private Integer realizadas;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private List<grafica> avanceGraf;
        @Getter        @Setter        private MeterGaugeChartModel gaugeChartModel;

        public resultadosCMI(String ejesRegistro, Integer programadas, Integer realizadas, Double avance, List<grafica> avanceGraf, MeterGaugeChartModel gaugeChartModel) {
            this.ejesRegistro = ejesRegistro;
            this.programadas = programadas;
            this.realizadas = realizadas;
            this.avance = avance;
            this.avanceGraf = avanceGraf;
            this.gaugeChartModel = gaugeChartModel;
        }
    }

    public static class listaEjesEsLaAp {

        @Getter        @Setter        private EjesRegistro ejeA;
        @Getter        @Setter        private List<listaProyectosEstrategias> proyectosEstrategiases;

        public listaEjesEsLaAp(EjesRegistro ejeA, List<listaProyectosEstrategias> proyectosEstrategiases) {
            this.ejeA = ejeA;
            this.proyectosEstrategiases = proyectosEstrategiases;
        }

    }

    public static class listaProyectosEstrategias {

        @Getter        @Setter        private Proyectos proyectos;
        @Getter        @Setter        private Integer programado;
        @Getter        @Setter        private Integer alcanzado;
        @Getter        @Setter        private Double porcentaje;
        @Getter        @Setter        private String semaforo;
        @Getter        @Setter        private List<listaEstrategiaActividades> estrategiaActividadeses;

        public listaProyectosEstrategias(Proyectos proyectos, Integer programado, Integer alcanzado, Double porcentaje, String semaforo, List<listaEstrategiaActividades> estrategiaActividadeses) {
            this.proyectos = proyectos;
            this.programado = programado;
            this.alcanzado = alcanzado;
            this.porcentaje = porcentaje;
            this.semaforo = semaforo;
            this.estrategiaActividadeses = estrategiaActividadeses;
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

    public static class listaEjeProyectos {

        @Getter        @Setter        private EjesRegistro ejess;
        @Getter        @Setter        private List<Proyectos> proyectoses;

        public listaEjeProyectos(EjesRegistro ejess, List<Proyectos> proyectoses) {
            this.ejess = ejess;
            this.proyectoses = proyectoses;
        }

    }

    public static class listaProyectosEstra {

        @Getter        @Setter        private Proyectos proyectos;
        @Getter        @Setter        private List<Estrategias> estrategiases;

        public listaProyectosEstra(Proyectos proyectos, List<Estrategias> estrategiases) {
            this.proyectos = proyectos;
            this.estrategiases = estrategiases;
        }
    }
    
    public class grafica {

        @Getter        @Setter        private String mes;
        @Getter        @Setter        private Double avance;

        public grafica(String mes, Double avance) {
            this.mes = mes;
            this.avance = avance;
        }
    }
    
    public void imprimirValores() {
    }
}
