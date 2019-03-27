package mx.edu.utxj.pye.sgi.controladores.cmi;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class cuadroMandoIntegralPlaneacion implements Serializable {

    @Getter    @Setter    private MeterGaugeChartModel meterGaugeModel1;

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<Grafica> graf = new ArrayList<>();
    
    @Getter    @Setter    private AreasUniversidad au = new AreasUniversidad();
    @Getter    @Setter    private ResultadosCMI cmiGeneral;
    @Getter    @Setter    private ResultadosCMI cmiEjesRegistro;
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

    @Getter    @Setter    private Integer programadas = 0, realizadas = 0, incremento = 0;
    @Getter    @Setter    private Integer alca = 0, progra = 0,numeroMes=0,numeroEje=0;
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Double avance = 0D;
    @Getter    @Setter    private String mes = "", mesNombre = "", valores="";;
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");


    @EJB    EjbPoaSelectec poaSelectec;
    @EJB    EjbAreasLogeo areasLogeo;
    @Inject    ControladorEmpleado controladorEmpleado;

    @PostConstruct
    public void init() {
        System.out.println("cuadroMandoIntegralUniversidad Inicio: " + System.currentTimeMillis());
        au = new AreasUniversidad(Short.parseShort("0"), "Institucional", "Institucional", "1", false);
        
        numeroMes = fechaActual.getMonth();
//        numeroMes=0;
        if (numeroMes == 0) {
            numeroMes = 11;
            ejercicioFiscal = Short.parseShort(String.valueOf(fechaActual.getYear() - 102));
        } else {
            numeroMes = numeroMes - 1;
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
            case 11:                mes = "Avance al mes de: Diciembre del 20" + (fechaActual.getYear() - 101);                break;
        }
        reseteador();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.init(1)");
        buscarAreasPOA();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.init(2)");
        cmiEnGeneral();
        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.init(3)");
        cmiPorEje();
        System.out.println("cuadroMandoIntegralUniversidad Fin: " + System.currentTimeMillis());
        System.out.println("---------------------------------------------------------------------------------------------------");
    }

    public void reseteador() {
         cmiGeneral = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
        cmiEJe1 = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
        cmiEJe2 = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
        cmiEJe3 = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
        cmiEJe4 = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
    }
    
    public void buscarAreasPOA() {
        areasUniversidads.clear();
        areasUniversidads.add(new AreasUniversidad(Short.parseShort("0"), "Institucional", "Institucional", "1", false));
        areasUniversidads.addAll(areasLogeo.getAreasUniversidadConPoa());
    }

    public void cmiEnGeneral() {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        if (au.getArea() != 0) {
            actividadesPoas = poaSelectec.mostrarActividadesPoasReporteArea(au.getArea(), ejercicioFiscal);
        } else {
            actividadesPoas = poaSelectec.mostrarActividadesPoasUniversidadaEjercicioFiscal(ejercicioFiscal);
        }
        cmiGeneral = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
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
                cmiGeneral = new ResultadosCMI(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativaNombre(), programadas, realizadas, avance, graf, grafRA);
            }
        }
    }
    
    public void cmiPorEje() {
        ejesRegistros = new ArrayList<>();
        ejesRegistros.clear();
        if (au.getArea() != 0) {
            ejesRegistros = poaSelectec.mostrarEjesRegistrosAreas(au.getArea(), ejercicioFiscal);
        } else {
            ejesRegistros = poaSelectec.mostrarEjesRegistros();
        }
        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.cmiPorEje()" + ejesRegistros.size());
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((t) -> {
                numeroEje = t.getEje();
                System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.cmiPorEje(numeroEje)"+numeroEje);
                switch (numeroEje) {
                    case 1:
                        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.cmiPorEje(EJE 1)");
                        cmiEJe1 = cmiEje(t);
                        break;
                    case 2:
                        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.cmiPorEje(EJE 2)");
                        cmiEJe2 = cmiEje(t);
                        break;
                    case 3:
                        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.cmiPorEje(EJE 3)");
                        cmiEJe3 = cmiEje(t);
                        break;
                    case 4:
                        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.cmiPorEje(EJE 4)");
                        cmiEJe4 = cmiEje(t);
                        break;

                }
            });
        }
        System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.cuadroMandoIntegralPlaneacion.cmiPorEje(Fin)");
    }
    
    public ResultadosCMI cmiEje(EjesRegistro er) {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        if (au.getArea() != 0) {
            actividadesPoas = poaSelectec.mostrarActividadesPoasAreaEjeyEjercicioFiscal(au.getArea(), ejercicioFiscal, er);
        } else {
            actividadesPoas = poaSelectec.mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(ejercicioFiscal, er);
        }
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
                    cmiEjesRegistro = new ResultadosCMI(er.getNombre(), programadas, realizadas, avance, graf, grafRA);
                }
            }
        } else {
            cmiEjesRegistro = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel());
        }
        return cmiEjesRegistro;
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
                        realizadas=realizadas+1;
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
                            alca=alca+1;
                        }
                    }
                });
                graf.add(new Grafica(mesNombre, (alca.doubleValue() / progra.doubleValue()) * 100D));
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
        return new MeterGaugeChartModel((realizadas.doubleValue() / programadas.doubleValue()) * 100D, intervals);
    }
    
    public void areaSeleccionada(ValueChangeEvent event) {
        try {
            au = new AreasUniversidad();
            Short claveArea = Short.parseShort(event.getNewValue().toString());
            if (claveArea != 0) {
                au = areasLogeo.mostrarAreasUniversidad(claveArea);
            } else {
                au = new AreasUniversidad(Short.parseShort("0"), "Institucional", "Institucional", "1", false);
            }
            reseteador();
            cmiEnGeneral();
            cmiPorEje();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(cuadroMandoIntegralPlaneacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public String datosGraica(List<Grafica> er){        
        valores="";
        er.forEach((t) -> {
            valores=valores+"---- mes:v"+t.getMes()+" avance: "+t.getAvance()+"----";
        });
        return valores;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class ResultadosCMI {

        @Getter
        @Setter
        private String ejesRegistro;
        @Getter
        @Setter
        private Integer programadas;
        @Getter
        @Setter
        private Integer realizadas;
        @Getter
        @Setter
        private Double avance;
        @Getter
        @Setter
        private List<Grafica> avanceGraf;
        @Getter
        @Setter
        private MeterGaugeChartModel gaugeChartModel;

        public ResultadosCMI(String ejesRegistro, Integer programadas, Integer realizadas, Double avance, List<Grafica> avanceGraf, MeterGaugeChartModel gaugeChartModel) {
            this.ejesRegistro = ejesRegistro;
            this.programadas = programadas;
            this.realizadas = realizadas;
            this.avance = avance;
            this.avanceGraf = avanceGraf;
            this.gaugeChartModel = gaugeChartModel;
        }
    }

    public class Grafica {

        @Getter
        @Setter
        private String mes;
        @Getter
        @Setter
        private Double avance;

        public Grafica(String mes, Double avance) {
            this.mes = mes;
            this.avance = avance;
        }

        @Override
        public String toString() {
            return mes + "" + avance;
        }
    
    }

    public void imprimirValores() {
    }
}
