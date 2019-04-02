package mx.edu.utxj.pye.sgi.controladores.cmi;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import mx.edu.utxj.pye.sgi.ejb.poa.EjbPoaSelectec;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.util.POAUtilidades;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.model.chart.MeterGaugeChartModel;

@Named
@SessionScoped
public class cuadroMandoIntegralPlaneacion implements Serializable {

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>();
    @Getter    @Setter    private List<Grafica> graf = new ArrayList<>();

    @Getter    @Setter    private AreasUniversidad au = new AreasUniversidad();
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

    @Getter    @Setter    private Integer programadas = 0, realizadas = 0, incremento = 0;
    @Getter    @Setter    private Integer alca = 0, progra = 0, numeroMes = 0, numeroEje = 0;
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Double avance = 0D;
    @Getter    @Setter    private String mes = "", mesNombre = "", valores = "";
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");

    @EJB    EjbPoaSelectec poaSelectec;
    @EJB    EjbAreasLogeo areasLogeo;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    POAUtilidades poau;

    @PostConstruct
    public void init() {
        au = new AreasUniversidad(Short.parseShort("0"), "Institucional", "Institucional", "1", false);
        numeroMes = fechaActual.getMonth();
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
        buscarAreasPOA();
        cmiEnGeneral();
        cmiPorEje();
    }

    public void reseteador() {
        cmiGeneral = new ResultadosCMI("", 0, 0, 0.0, new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe1 = new ResultadosCMI("", 0, 0, 0.0, new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe2 = new ResultadosCMI("", 0, 0, 0.0, new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe3 = new ResultadosCMI("", 0, 0, 0.0, new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe4 = new ResultadosCMI("", 0, 0, 0.0, new ArrayList<>(), new MeterGaugeChartModel(), false);
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
        cmiGeneral = new ResultadosCMI(au.getNombre(), 0, 0, 0.0, graf, new MeterGaugeChartModel(), false);
        if (!actividadesPoas.isEmpty()) {
            List<ActividadesPoa> actividadesPoasFiltradas = new ArrayList<>();
            actividadesPoasFiltradas.clear();
            actividadesPoasFiltradas = actividadesFiltradas(actividadesPoas);
            if (!actividadesPoasFiltradas.isEmpty()) {
                calculosCMI(actividadesPoasFiltradas);
                grafRA = initMeterGaugeModel();
                grafRA.setTitle(mes);
                grafRA.setSeriesColors("666666,808080,a0a0a0");
                grafRA.setGaugeLabel(df.format((realizadas.doubleValue() / programadas.doubleValue()) * 100D) + " % avance");
                grafRA.setGaugeLabelPosition("bottom");
                grafRA.setShowTickLabels(true);
                grafRA.setLabelHeightAdjust(10);
                grafRA.setIntervalOuterRadius(100);
                cmiGeneral = new ResultadosCMI(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativaNombre(), programadas, realizadas, avance, graf, grafRA, true);
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
        if (au.getArea() != 0) {
            actividadesPoas = poaSelectec.mostrarActividadesPoasAreaEjeyEjercicioFiscal(au.getArea(), ejercicioFiscal, er);
        } else {
            actividadesPoas = poaSelectec.mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(ejercicioFiscal, er);
        }
        List<ActividadesPoa> actividadesPoasFiltradas = new ArrayList<>();
        actividadesPoasFiltradas.clear();
        actividadesPoasFiltradas = actividadesFiltradas(actividadesPoas);
        calculosCMI(actividadesPoasFiltradas);
        grafRA = initMeterGaugeModel();
        grafRA.setTitle(mes);
        grafRA.setSeriesColors("666666,808080,a0a0a0");
        grafRA.setGaugeLabelPosition("bottom");
        grafRA.setShowTickLabels(true);
        grafRA.setLabelHeightAdjust(10);
        grafRA.setIntervalOuterRadius(100);
        grafRA.setGaugeLabel(avance + " % avance");
        return new ResultadosCMI(er.getNombre(), programadas, realizadas, avance, graf, grafRA, true);
    }

    public void calculosCMI(List<ActividadesPoa> actividadesPoa) {
        programadas = 0;
        realizadas = 0;
        avance = 0D;
        graf = new ArrayList<>();
        graf.clear();

        if (actividadesPoa.isEmpty()) {
            for (incremento = 0; incremento <= numeroMes; incremento++) {
                graf.add(new Grafica(poau.obtenerMesNombre(incremento), 0D));
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
                        programadas = programadas + 1;
                    }
                    if (tR != 0 && Objects.equals(tR, tP)) {
                        realizadas = realizadas + 1;
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
                            progra = progra + 1;
                        }

                        if (tR != 0 && Objects.equals(tR, tP)) {
                            alca = alca + 1;
                        }
                    }
                });
                if (progra != 0) {
                    graf.add(new Grafica(poau.obtenerMesNombre(incremento), (alca.doubleValue() / progra.doubleValue()) * 100D));
                } else {
                    graf.add(new Grafica(poau.obtenerMesNombre(incremento), 0D));
                }
            }
        }
        if (programadas != 0) {
            avance = (Double.parseDouble(realizadas.toString()) / Double.parseDouble(programadas.toString()) * 100D);
        } else {
            avance = 0D;
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
        return new MeterGaugeChartModel(avance, intervals);
    }

    public void areaSeleccionada() {
        try {
            reseteador();
            cmiEnGeneral();
            cmiPorEje();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(cuadroMandoIntegralPlaneacion.class.getName()).log(Level.SEVERE, null, ex);
        }
        Faces.refresh();
    }

    public String datosGraica(List<Grafica> er) {
        valores = "";
        er.forEach((t) -> {
            valores = valores + "---- mes: " + t.getMes() + " avance: " + t.getAvance() + "----";
        });
        return valores;
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class ResultadosCMI {

        @Getter        @Setter        private String ejesRegistro;
        @Getter        @Setter        private Integer programadas;
        @Getter        @Setter        private Integer realizadas;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private List<Grafica> avanceGraf;
        @Getter        @Setter        private MeterGaugeChartModel gaugeChartModel;
        @Getter        @Setter        private Boolean renderizar;

        public ResultadosCMI(String ejesRegistro, Integer programadas, Integer realizadas, Double avance, List<Grafica> avanceGraf, MeterGaugeChartModel gaugeChartModel, Boolean renderizar) {
            this.ejesRegistro = ejesRegistro;
            this.programadas = programadas;
            this.realizadas = realizadas;
            this.avance = avance;
            this.avanceGraf = avanceGraf;
            this.gaugeChartModel = gaugeChartModel;
            this.renderizar = renderizar;
        }

        private ResultadosCMI() {

        }
    }

    public class Grafica {

        @Getter        @Setter        private String mes;
        @Getter        @Setter        private Double avance;

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
