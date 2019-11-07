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
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.CuadroMandoIntegral;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.model.chart.MeterGaugeChartModel;

@Named
@SessionScoped
public class cuadroMandoIntegralPlaneacion implements Serializable {

    @Getter    @Setter    private List<AreasUniversidad> areasUniversidads = new ArrayList<>();    
    @Getter    @Setter    private List<Grafica> grafSP = new ArrayList<>();
    @Getter    @Setter    private List<Grafica> grafCP = new ArrayList<>();

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

    @Getter    @Setter    private Integer programadasCorte = 0, realizadasCorte = 0, incremento = 0,prograRepoCorte = 0, realRepoCorte = 0;
    @Getter    @Setter    private Integer programadasMensual = 0, realizadasMensual = 0, numeroMes = 0,prograRepoMensual = 0, realRepoMensual = 0, numeroEje = 0;
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Double avance = 0D,avanceSp = 0D;
    @Getter    @Setter    private String mes = "", valores = "",cuatri="";
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");

    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo areasLogeo;
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA poau;

    @PostConstruct
    public void init() {
        au = new AreasUniversidad(Short.parseShort("0"), "Institucional", "Institucional", "1", false);
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
        buscarAreasPOA();
        cmiEnGeneral();
        cmiPorEje();
    }

    public void obtnerMEs(String actual) {
        numeroMes = 0;
        numeroMes = Integer.parseInt(actual);
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
        Faces.refresh();
    }

    public void reseteador() {
        cmiGeneral = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe1 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe2 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe3 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
        cmiEJe4 = new ResultadosCMI("", 0, 0, 0D,0D, new ArrayList<>(),new ArrayList<>(), new MeterGaugeChartModel(), false);
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
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasTotalArea(au.getArea(), ejercicioFiscal);
        } else {
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasUniversidadaEjercicioFiscal(ejercicioFiscal);
        }
        cmiGeneral = new ResultadosCMI(au.getNombre(), 0, 0, 0D,0D, grafSP,grafCP, new MeterGaugeChartModel(), false);
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
                cmiGeneral = new ResultadosCMI(controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativaNombre(), programadasCorte, realizadasCorte, avance,avanceSp, grafSP,grafCP, grafRA, true);
            }
        }
    }

    public void cmiPorEje() {
        ejesRegistros = new ArrayList<>();
        ejesRegistros.clear();
        if (au.getArea() != 0) {
            ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistrosAreas(au.getArea(), ejercicioFiscal);
        } else {
            ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistros();
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
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasEje(au.getArea(), ejercicioFiscal, er);
        } else {
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(ejercicioFiscal, er);
        }
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
        if (avanceSp >= 115) {
            return new MeterGaugeChartModel(115D, intervals);
        } else {
            return new MeterGaugeChartModel(avanceSp, intervals);
        }
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
