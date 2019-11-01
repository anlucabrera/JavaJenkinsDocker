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
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.Proyectos;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Messages;
import org.primefaces.model.chart.MeterGaugeChartModel;

@Named
@SessionScoped
public class cuadroMandoIntegralArea implements Serializable {

    @Getter    @Setter    private List<Grafica> grafSP = new ArrayList<>();
    @Getter    @Setter    private List<Grafica> grafCP = new ArrayList<>();
    @Getter    @Setter    private ResultadosCMI cmiGeneral;
    @Getter    @Setter    private ResultadosCMI cmiEJe1;
    @Getter    @Setter    private ResultadosCMI cmiEJe2;
    @Getter    @Setter    private ResultadosCMI cmiEJe3;
    @Getter    @Setter    private ResultadosCMI cmiEJe4;
    @Getter    @Setter    private MeterGaugeChartModel grafRA;
    @Getter    @Setter    private Date fechaActual = new Date();
    /////////////////////////////////////////////////////////////////////////////////////////////

    @Getter    @Setter    private List<ActividadesPoa> actividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> ejesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<String> resultados = new ArrayList<>();

    @Getter    @Setter    private Integer programadasCorte = 0, realizadasCorte = 0, incremento = 0,prograRepoCorte = 0, realRepoCorte = 0;
    @Getter    @Setter    private Integer programadasMensual = 0, realizadasMensual = 0, numeroMes = 0,prograRepoMensual = 0, realRepoMensual = 0, numeroEje = 0;
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Double avance = 0D,avanceSp = 0D;
    @Getter    @Setter    private String mes = "", valores = "";
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Getter    @Setter    private List<Proyectos> proyectoses = new ArrayList<>();
    @Getter    @Setter    private List<Estrategias> estrategiases = new ArrayList<>();

    @Getter    @Setter    private List<listaEjesEsLaAp> ejesEsLaAp = new ArrayList<>();
    @Getter    @Setter    private List<listaProyectosEstrategias> proyectosEstrategiases = new ArrayList<>();
    @Getter    @Setter    private List<listaEjeProyectos> ejeProyectoses = new ArrayList<>();
    @Getter    @Setter    private List<listaEstrategiaActividades> listaEstrategiaActividadesesEje = new ArrayList<>();

    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo areasLogeo;
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA poau;

    @PostConstruct
    public void init() {
        numeroMes = fechaActual.getMonth();
        ejercicioFiscal = controladorEmpleado.getProcesopoa().getEjercicioFiscalEtapa2();
        
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
            case 11:                mes = "Avance al mes de: Diciembre del 20" + (fechaActual.getYear() - 101);                break;
        }
        reseteador();
        cmiEnGeneral();
        cmiPorEje();
    }
    
public void reseteador() {
        cmiGeneral = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe1 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe2 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe3 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe4 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
    }

    public void cmiEnGeneral() {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasTotalArea(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        cmiGeneral = new ResultadosCMI(poau.obtenerAreaNombre(controladorEmpleado.getProcesopoa().getArea()), 0, 0, 0D,0D, grafSP,grafCP, new MeterGaugeChartModel(), false);
        if (!actividadesPoas.isEmpty()) {
            List<ActividadesPoa> actividadesPoasFiltradas = new ArrayList<>();
            actividadesPoasFiltradas.clear();
            actividadesPoasFiltradas = actividadesFiltradas(actividadesPoas);
            if (!actividadesPoasFiltradas.isEmpty()) {
                calculosCMI(actividadesPoasFiltradas);
                grafRA = initMeterGaugeModel();
                grafRA.setTitle(mes);
                grafRA.setSeriesColors("FF0000,ffff00,66ff33");
                grafRA.setGaugeLabel(df.format(avanceSp) + " % avance");
                grafRA.setGaugeLabelPosition("bottom");
                grafRA.setShowTickLabels(true);
                grafRA.setLabelHeightAdjust(10);
                grafRA.setIntervalOuterRadius(100);
                cmiGeneral = new ResultadosCMI(poau.obtenerAreaNombre(controladorEmpleado.getProcesopoa().getArea()), programadasCorte, realizadasCorte, avance,avanceSp, grafSP,grafCP, grafRA, true);
            }
        }
    }

    public void cmiPorEje() {
        ejesRegistros = new ArrayList<>();
        ejesRegistros.clear();
        ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((t) -> {
                numeroEje = t.getEje();
                switch (numeroEje) {
                    case 1:                        cmiEJe1 = cmiEje(t);                        break;
                    case 2:                        cmiEJe2 = cmiEje(t);                        break;
                    case 3:                        cmiEJe3 = cmiEje(t);                        break;
                    case 4:                        cmiEJe4 = cmiEje(t);                        break;
                }
            });
        }
    }

    public ResultadosCMI cmiEje(EjesRegistro er) {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasEje(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal, er);
        List<ActividadesPoa> actividadesPoasFiltradas = new ArrayList<>();
        actividadesPoasFiltradas.clear();
        actividadesPoasFiltradas = actividadesFiltradas(actividadesPoas);
        calculosCMI(actividadesPoasFiltradas);
        grafRA = initMeterGaugeModel();
        grafRA.setTitle(mes);
        grafRA.setSeriesColors("FF0000,ffff00,66ff33");
        grafRA.setGaugeLabelPosition("bottom");
        grafRA.setShowTickLabels(true);
        grafRA.setLabelHeightAdjust(10);
        grafRA.setIntervalOuterRadius(100);
        grafRA.setGaugeLabel(df.format(avanceSp) + " % avance");
        return new ResultadosCMI(er.getNombre(), programadasCorte, realizadasCorte, avance,avanceSp, grafSP,grafCP, grafRA, true);
    }

    public void calculosCMI(List<ActividadesPoa> actividadesPoa) {
        programadasCorte = 0;
        realizadasCorte = 0;
        avance = 0D;
        prograRepoCorte = 0;
        realRepoCorte = 0;
        grafSP = new ArrayList<>();
        grafCP = new ArrayList<>();
        grafSP.clear();
        grafCP.clear();   
        if (actividadesPoa.isEmpty()) {
            for (incremento = 0; incremento <= numeroMes; incremento++) {
                grafSP.add(new Grafica(poau.obtenerMesNombre(incremento), 0D,0D));
                grafCP.add(new Grafica(poau.obtenerMesNombre(incremento), 0D,0D));
            }
        } else {
            actividadesPoa.forEach((t) -> {
                if (t.getBandera().equals("y")) {
                    Integer tP = 0;
                    Integer tR = 0;
                    switch (numeroMes) {
                        case 11:                            tR = tR + t.getNADiciembre();                            tP = tP + t.getNPDiciembre();
                        case 10:                            tR = tR + t.getNANoviembre();                            tP = tP + t.getNPNoviembre();
                        case 9:                            tR = tR + t.getNAOctubre();                            tP = tP + t.getNPOctubre();
                        case 8:                            tR = tR + t.getNASeptiembre();                            tP = tP + t.getNPSeptiembre();
                        case 7:                            tR = tR + t.getNAAgosto();                            tP = tP + t.getNPAgosto();
                        case 6:                            tR = tR + t.getNAJulio();                            tP = tP + t.getNPJulio();
                        case 5:                            tR = tR + t.getNAJunio();                            tP = tP + t.getNPJunio();
                        case 4:                            tR = tR + t.getNAMayo();                            tP = tP + t.getNPMayo();
                        case 3:                            tR = tR + t.getNAAbril();                            tP = tP + t.getNPAbril();
                        case 2:                            tR = tR + t.getNAMarzo();                            tP = tP + t.getNPMarzo();
                        case 1:                            tR = tR + t.getNAFebrero();                            tP = tP + t.getNPFebrero();
                        case 0:                            tR = tR + t.getNAEnero();                            tP = tP + t.getNPEnero();                            break;
                    }
                    if (tP != 0) {
                        programadasCorte = programadasCorte + 1;
                        prograRepoCorte = prograRepoCorte + 1;
                    }
                    if (tR != 0) {
                        realizadasCorte = realizadasCorte + 1;
                    }
                    if (tR != 0 && Objects.equals(tR, tP)) {                        
                        realRepoCorte = realRepoCorte + 1;
                    }
                }
            });
            for (incremento = 0; incremento <= numeroMes; incremento++) {
                programadasMensual = 0;
                realizadasMensual = 0;
                prograRepoMensual = 0;
                realRepoMensual = 0;
                actividadesPoa.forEach((t) -> {
                    if (t.getBandera().equals("y")) {
                        Integer tP = 0;
                        Integer tR = 0;
                        switch (incremento) {
                            case 11:                            tR = tR + t.getNADiciembre();                            tP = tP + t.getNPDiciembre();
                            case 10:                            tR = tR + t.getNANoviembre();                            tP = tP + t.getNPNoviembre();
                            case 9:                            tR = tR + t.getNAOctubre();                            tP = tP + t.getNPOctubre();
                            case 8:                            tR = tR + t.getNASeptiembre();                            tP = tP + t.getNPSeptiembre();
                            case 7:                            tR = tR + t.getNAAgosto();                            tP = tP + t.getNPAgosto();
                            case 6:                            tR = tR + t.getNAJulio();                            tP = tP + t.getNPJulio();
                            case 5:                            tR = tR + t.getNAJunio();                            tP = tP + t.getNPJunio();
                            case 4:                            tR = tR + t.getNAMayo();                            tP = tP + t.getNPMayo();
                            case 3:                            tR = tR + t.getNAAbril();                            tP = tP + t.getNPAbril();
                            case 2:                            tR = tR + t.getNAMarzo();                            tP = tP + t.getNPMarzo();
                            case 1:                            tR = tR + t.getNAFebrero();                            tP = tP + t.getNPFebrero();
                            case 0:                            tR = tR + t.getNAEnero();                            tP = tP + t.getNPEnero();                            break;
                        }
                        if (tP != 0) {
                            programadasMensual = programadasMensual + 1;
                            prograRepoMensual = prograRepoMensual + 1;
                        }
                        if (tR != 0) {
                            realizadasMensual = realizadasMensual + 1;
                        }
                        if (tR != 0 && Objects.equals(tR, tP)) {
                            realRepoMensual = realRepoMensual + 1;
                        }
                    }
                }); 
                Double promSP=0D,promCP=0D;
                
                if (prograRepoMensual != 0) {
                    promCP= (Double.parseDouble(realRepoMensual.toString()) / Double.parseDouble(prograRepoMensual.toString()) * 100D);
                } else {
                    promCP= 0D;
                }

                if (programadasMensual != 0 && realizadasMensual != 0) {
                    promSP=(Double.parseDouble(realizadasMensual.toString()) / Double.parseDouble(programadasMensual.toString()) * 100D);
                } else if (programadasMensual == 0 && realizadasMensual != 0) {
                    promSP= realizadasMensual * 100D;
                } else if (programadasMensual == 0 && realizadasMensual == 0) {
                    promSP= 0D;
                }
                
                    grafSP.add(new Grafica(poau.obtenerMesNombre(incremento), promSP,promSP));                    
                    grafCP.add(new Grafica(poau.obtenerMesNombre(incremento), promCP,promCP));
            }
        }
        if (prograRepoCorte != 0) {
            avance = (Double.parseDouble(realRepoCorte.toString()) / Double.parseDouble(prograRepoCorte.toString()) * 100D);
        } else {
            avance = 0D;
        }
        
        if (programadasCorte != 0 && realizadasCorte != 0) {
            avanceSp = (Double.parseDouble(realizadasCorte.toString()) / Double.parseDouble(programadasCorte.toString()) * 100D);
        } else if (programadasCorte == 0 && realizadasCorte != 0) {
            avanceSp = realizadasCorte * 100D;
        } else if (programadasCorte == 0 && realizadasCorte == 0) {
            avanceSp = 0D;
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
        return actividadesPoasRegistradas;
    }

    private MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>() {
            {
                add(90.0);
                add(95.0);
                add(115.0);
            }
        };
        return new MeterGaugeChartModel(avanceSp, intervals);
    }

    public String datosGraica(List<cuadroMandoIntegralPlaneacion.Grafica> er) {
        valores = "";
        er.forEach((t) -> {
            valores = valores + "---- mes: " + t.getMes() + " avance: " + t.getAvance() + "----";
        });
        return valores;
    }

    public void actividadesProyecto() {
        listaEstrategiaActividadesesEje = new ArrayList<>();
        proyectosEstrategiases = new ArrayList<>();
        ejeProyectoses = new ArrayList<>();
        estrategiases = new ArrayList<>();
        proyectoses = new ArrayList<>();
        ejesEsLaAp = new ArrayList<>();
        listaEstrategiaActividadesesEje.clear();
        proyectosEstrategiases.clear();
        ejeProyectoses.clear();
        estrategiases.clear();
        proyectoses.clear();
        ejesEsLaAp.clear();

        ejesRegistros = new ArrayList<>();
        ejesRegistros.clear();
        ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((ej) -> {
                proyectoses = new ArrayList<>();
                proyectoses.clear();
                proyectoses = ejbCatalogosPoa.getProyectosPorEje(ej, ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea());
                if (!proyectoses.isEmpty()) {
                    proyectoses.forEach((pr) -> {
                        estrategiases = new ArrayList<>();
                        estrategiases.clear();
                        estrategiases = ejbCatalogosPoa.getEstrategiaPorProyectos(pr, ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea());
                        if (!estrategiases.isEmpty()) {
                            estrategiases.forEach((es) -> {
                                List<ActividadesPoa> listaActividadesPoasFiltradas = new ArrayList<>();
                                listaActividadesPoasFiltradas.clear();
                                listaActividadesPoasFiltradas = actividadesFiltradas(ejbRegistroActividades.getActividadesPoasporProyecto(es, ej, pr, ejercicioFiscal, controladorEmpleado.getProcesopoa().getArea()));
                                if (!listaActividadesPoasFiltradas.isEmpty()) {
                                    Collections.sort(listaActividadesPoasFiltradas, (x, y) -> (x.getNumeroP() + "." + x.getNumeroS()).compareTo(y.getNumeroP() + "." + y.getNumeroS()));
                                    listaEstrategiaActividadesesEje.add(new listaEstrategiaActividades(es, listaActividadesPoasFiltradas));
                                    Collections.sort(listaEstrategiaActividadesesEje, (x, y) -> Short.compare(x.getEstrategias().getEstrategia(), y.getEstrategias().getEstrategia()));
                                    calculosCMI(listaActividadesPoasFiltradas);
                                }
                            });
                            if (!listaEstrategiaActividadesesEje.isEmpty()) {
                                proyectosEstrategiases.add(new listaProyectosEstrategias(pr, programadasCorte, realizadasCorte, avance, semaforo(programadasCorte, realizadasCorte), listaEstrategiaActividadesesEje));
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
        } else {
            ejesEsLaAp = new ArrayList<>();
            ejesEsLaAp.clear();
            Messages.addGlobalError("no hay registros");
        }
        if (!ejesEsLaAp.isEmpty()) {
            Collections.sort(ejesEsLaAp, (x, y) -> Integer.compare(x.getEjeA().getEje(), y.getEjeA().getEje()));
        }
    }

    public Integer totalProgramado(ActividadesPoa actividadesPoas) {
        Integer totalPCorte = 0;
        switch (numeroMes) {
            case 11:                totalPCorte = totalPCorte + actividadesPoas.getNPDiciembre();
            case 10:                totalPCorte = totalPCorte + actividadesPoas.getNPNoviembre();
            case 9:                totalPCorte = totalPCorte + actividadesPoas.getNPOctubre();
            case 8:                totalPCorte = totalPCorte + actividadesPoas.getNPSeptiembre();
            case 7:                totalPCorte = totalPCorte + actividadesPoas.getNPAgosto();
            case 6:                totalPCorte = totalPCorte + actividadesPoas.getNPJulio();
            case 5:                totalPCorte = totalPCorte + actividadesPoas.getNPJunio();
            case 4:                totalPCorte = totalPCorte + actividadesPoas.getNPMayo();
            case 3:                totalPCorte = totalPCorte + actividadesPoas.getNPAbril();
            case 2:                totalPCorte = totalPCorte + actividadesPoas.getNPMarzo();
            case 1:                totalPCorte = totalPCorte + actividadesPoas.getNPFebrero();
            case 0:                totalPCorte = totalPCorte + actividadesPoas.getNPEnero();                break;
        }        
        return totalPCorte;
    }

    public Integer totalAlcanzado(ActividadesPoa actividadesPoas) {
        Integer totalACorte = 0;
        switch (numeroMes) {
            case 11:                totalACorte = totalACorte + actividadesPoas.getNADiciembre();
            case 10:                totalACorte = totalACorte + actividadesPoas.getNANoviembre();
            case 9:                totalACorte = totalACorte + actividadesPoas.getNAOctubre();
            case 8:                totalACorte = totalACorte + actividadesPoas.getNASeptiembre();
            case 7:                totalACorte = totalACorte + actividadesPoas.getNAAgosto();
            case 6:                totalACorte = totalACorte + actividadesPoas.getNAJulio();
            case 5:                totalACorte = totalACorte + actividadesPoas.getNAJunio();
            case 4:                totalACorte = totalACorte + actividadesPoas.getNAMayo();
            case 3:                totalACorte = totalACorte + actividadesPoas.getNAAbril();
            case 2:                totalACorte = totalACorte + actividadesPoas.getNAMarzo();
            case 1:                totalACorte = totalACorte + actividadesPoas.getNAFebrero();
            case 0:                totalACorte = totalACorte + actividadesPoas.getNAEnero();                break;
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
            Logger.getLogger(cuadroMandoIntegralArea.class.getName()).log(Level.SEVERE, null, ex);
            return " ";
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
    
 public static class ResultadosCMI {

        @Getter        @Setter        private String ejesRegistro;
        @Getter        @Setter        private Integer programadas;
        @Getter        @Setter        private Integer realizadas;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private Double avanceSP;
        @Getter        @Setter        private List<Grafica> avanceGrafSP;
        @Getter        @Setter        private List<Grafica> avanceGrafCP;
        @Getter        @Setter        private MeterGaugeChartModel gaugeChartModel;
        @Getter        @Setter        private Boolean renderizar;

        public ResultadosCMI(String ejesRegistro, Integer programadas, Integer realizadas, Double avance, Double avanceSP, List<Grafica> avanceGrafSP, List<Grafica> avanceGrafCP, MeterGaugeChartModel gaugeChartModel, Boolean renderizar) {
            this.ejesRegistro = ejesRegistro;
            this.programadas = programadas;
            this.realizadas = realizadas;
            this.avance = avance;
            this.avanceSP = avanceSP;
            this.avanceGrafSP = avanceGrafSP;
            this.avanceGrafCP = avanceGrafCP;
            this.gaugeChartModel = gaugeChartModel;
            this.renderizar = renderizar;
        }

        private ResultadosCMI() {

        }
    }

    public class Grafica {

        @Getter        @Setter        private String mes;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private Double avanceOF;

        public Grafica(String mes, Double avance, Double avanceOF) {
            this.mes = mes;
            this.avance = avance;
            this.avanceOF = avanceOF;
        }

        @Override
        public String toString() {
            return mes + "" + avance;
        }

    }

    public void imprimirValores() {
    }

}
