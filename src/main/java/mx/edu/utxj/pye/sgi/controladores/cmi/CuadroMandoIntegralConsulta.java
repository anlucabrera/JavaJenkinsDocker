package mx.edu.utxj.pye.sgi.controladores.cmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbCatalogosPoa;
import mx.edu.utxj.pye.sgi.ejb.poa.EjbRegistroActividades;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.util.UtilidadesPOA;

import javax.inject.Inject;
import com.github.adminfaces.starter.infra.security.LogonMB;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.event.ValueChangeEvent;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.charts.bar.BarChartModel;

@Named
@ViewScoped
public class CuadroMandoIntegralConsulta implements Serializable {
    
    @Getter    @Setter    private List<AreasUniversidad> areasConsulta = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> ejesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> actividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<List<DtoCmi.Grafica>> cumplimientoMensual = new ArrayList<>();
    @Getter    @Setter    private List<List<DtoCmi.Grafica>> cumplimientoAcumuladol = new ArrayList<>();
    @Getter    @Setter    private List<DtoCmi.ConcentradoDatos> resumen;
    @Getter    @Setter    private List<DtoCmi.ConcentradoDatos> resumenAcumulado;
    
    @Getter    @Setter    private DtoCmi.ResultadosCMI cmiGeneral;
    
    @Getter    @Setter    private Double programadas,alcanzadas,noalcanzadas,promedioSp,promedioCP;
    @Getter    @Setter    private Double resumenCprogramadas,resumenCalcanzadas,resumenCnoalcanzadas,resumenCpromedioSp,resumenCpromedioCP;
    @Getter    @Setter    private Integer numeroMes=0,claveEjeR;
    @Getter    @Setter    private Short claveArea=0;
    @Getter    @Setter    private String mes = "",extender="myconfig";
    @Getter    @Setter    private EjerciciosFiscales ejercicioFiscal;
    
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");
    
    
    @Getter    @Setter    BarChartModel mixedModel;
 

    @EJB    EjbRegistroActividades ejbRegistroActividades;
    @EJB    EjbAreasLogeo areasLogeo;
    @EJB    EjbCatalogosPoa ejbCatalogosPoa;
    
    @Inject    ControladorEmpleado controladorEmpleado;
    @Inject    UtilidadesPOA poau;

    

@Inject LogonMB logonMB;
@Getter private Boolean cargado = false;

    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        if (controladorEmpleado.getProcesopoa().getActivaEtapa2()) {
            extender="myconfig";
            ejercicioFiscal = new EjerciciosFiscales();
            ejercicioFiscal= poau.obtenerAnioRegistro(controladorEmpleado.getCalendarioevaluacionpoa().getEjercicioFiscal());
            numeroMes = poau.obtenerMesNumero(controladorEmpleado.getCalendarioevaluacionpoa().getMesEvaluacion());
            mes="Avance al mes de: "+controladorEmpleado.getCalendarioevaluacionpoa().getMesEvaluacion()+" del "+ejercicioFiscal.getAnio();
            claveArea = controladorEmpleado.getProcesopoa().getArea();
            claveEjeR=0;
            consultrAreasEvaluacion();
            consultaejes();
            reseteador();
            generaCmiGeneral();
        }
    }

    public void reseteador() {
        cmiGeneral = new DtoCmi.ResultadosCMI("", 0, 0, 0, 0D, 0D, extender, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new MeterGaugeChartModel(), Boolean.FALSE);
    }

    public void imprimirValores() {
    }
    
    public void asignarAreaEvaluada(ValueChangeEvent event) {
        reseteador();
        claveArea = Short.parseShort(event.getNewValue().toString());
        consultaejes();
        generaCmiGeneral();
    }

    public void asignarEjeEvaluado(ValueChangeEvent event) {
        reseteador();
        claveEjeR = Integer.parseInt(event.getNewValue().toString());
        ejeSeleccionado();
    }
    
    public void consultaejes() {
        ejesRegistros = new ArrayList<>();
        ejesRegistros.clear();
        if (claveArea == 0) {
            ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistros().stream().filter(t -> t.getEje() <= 4).collect(Collectors.toList());
        } else {
            ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistrosAreas(claveArea, ejercicioFiscal.getEjercicioFiscal());
        }
        ejesRegistros.add(new EjesRegistro(0, "General", "", "", ""));
    }
    
     public void consultrAreasEvaluacion() {
        areasConsulta = new ArrayList<>();
        if (claveArea == 9 || claveArea == 6) {
            try {
                areasConsulta.add(new AreasUniversidad(Short.parseShort("0"), "Institucional", mes, extender, true));
                areasConsulta.addAll(areasLogeo.getAreasUniversidadConPoa());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            areasConsulta.add(new AreasUniversidad(Short.parseShort("0"), "Institucional", mes, extender, true));
            controladorEmpleado.getProcesopoas().forEach((t) -> {
                try {
                    areasConsulta.add(areasLogeo.mostrarAreasUniversidad(t.getArea()));
                } catch (Throwable ex) {
                    Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                    Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public void generaCmiGeneral() {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        if(claveArea != 0){
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasTotalArea(claveArea, ejercicioFiscal.getEjercicioFiscal()).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        }else{
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasUniversidadaEjercicioFiscal(ejercicioFiscal.getEjercicioFiscal()).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        }
        cmiGeneral = new DtoCmi.ResultadosCMI(poau.obtenerAreaNombre(controladorEmpleado.getProcesopoa().getArea()), 0, 0, 0, 0D, 0D, extender, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new MeterGaugeChartModel(), Boolean.FALSE);
        if (!actividadesPoas.isEmpty()) {
            cumplimientoMensual=promediosMensuales();
            cumplimientoAcumuladol=promediosAcumulados();
            cmiGeneral = new DtoCmi.ResultadosCMI(poau.obtenerAreaNombre(controladorEmpleado.getProcesopoa().getArea()), programadas.intValue(), alcanzadas.intValue(), noalcanzadas.intValue(), promedioCP, promedioSp, extender,resumen,resumenAcumulado, cumplimientoMensual.get(0), cumplimientoMensual.get(1),cumplimientoAcumuladol.get(0),cumplimientoAcumuladol.get(1), generaGraficaResumen(promedioSp), Boolean.TRUE);
        }
    }
    
    public DtoCmi.ResultadosCMI generaCmiPorEje(EjesRegistro eje) {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        if(claveEjeR != 0){
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasEje(claveArea, ejercicioFiscal.getEjercicioFiscal(),eje).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        }else{
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(ejercicioFiscal.getEjercicioFiscal(),eje).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        }
        DtoCmi.ResultadosCMI cmiEje = new DtoCmi.ResultadosCMI(eje.getNombre(), 0, 0, 0, 0D, 0D, extender, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new MeterGaugeChartModel(), Boolean.FALSE);
        if (!actividadesPoas.isEmpty()) {
            cumplimientoMensual=promediosMensuales();
            cumplimientoAcumuladol=promediosAcumulados();
            cmiGeneral = new DtoCmi.ResultadosCMI(eje.getNombre(), programadas.intValue(), alcanzadas.intValue(), noalcanzadas.intValue(), promedioCP, promedioSp, extender,resumen,resumenAcumulado, cumplimientoMensual.get(0), cumplimientoMensual.get(1),cumplimientoAcumuladol.get(0),cumplimientoAcumuladol.get(1), generaGraficaResumen(promedioSp), Boolean.TRUE);
        }
        return cmiEje;
    }
    
    public void ejeSeleccionado() {
        if (!ejesRegistros.isEmpty()) {
            ejesRegistros.forEach((t) -> {
                if (Objects.equals(claveEjeR, t.getEje())) {
                    generaCmiPorEje(t);
                }
            });
        }
    }
    
    
    
    public MeterGaugeChartModel generaGraficaResumen(Double avance) {
        List<Number> intervals = new ArrayList<Number>() {
            {
                add(90.0);
                add(95.0);
                add(115.0);
            }
        };
        MeterGaugeChartModel graficaNivel = new MeterGaugeChartModel(avance, intervals);
        graficaNivel.setTitle(mes);
        graficaNivel.setSeriesColors("FF0000,ffff00,66ff33");
        graficaNivel.setGaugeLabel(df.format(avance) + " % avance");
        graficaNivel.setGaugeLabelPosition("bottom");
        graficaNivel.setShowTickLabels(true);
        graficaNivel.setLabelHeightAdjust(10);
        graficaNivel.setIntervalOuterRadius(100);
        return graficaNivel;
    }
    
    
    public List<List<DtoCmi.Grafica>> promediosMensuales() {
        List<List<DtoCmi.Grafica>> cumplimiento = new ArrayList<>();
        List<DtoCmi.Grafica> sp = new ArrayList<>();
        List<DtoCmi.Grafica> cp = new ArrayList<>();
        resumen = new ArrayList<>();
        reseteaConteoCuatrimestral();
        for (int i = 0; i <= numeroMes; i++) {
            programadas = 0D;
            alcanzadas = 0D;
            noalcanzadas = 0D;
            promedioSp = 0D;
            promedioSp = 0D;
            switch (i) {
                case 11:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPDiciembre() != 0) {programadas = programadas + 1D;if(t.getNADiciembre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNADiciembre() != 0) {if (t.getNADiciembre() >= t.getNPDiciembre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 10:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPNoviembre() != 0) {programadas = programadas + 1D;if(t.getNANoviembre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNANoviembre() != 0) {if (t.getNANoviembre() >= t.getNPNoviembre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 9:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPOctubre() != 0) {programadas = programadas + 1D;if(t.getNAOctubre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAOctubre() != 0) {if (t.getNAOctubre() >= t.getNPOctubre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 8:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPSeptiembre() != 0) {programadas = programadas + 1D;if(t.getNASeptiembre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNASeptiembre() != 0) {if (t.getNASeptiembre() >= t.getNPSeptiembre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 7:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPAgosto() != 0) {programadas = programadas + 1D;if(t.getNAAgosto()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAAgosto() != 0) {if (t.getNAAgosto() >= t.getNPAgosto()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 6:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPJulio() != 0) {programadas = programadas + 1D;if(t.getNAJulio()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAJulio() != 0) {if (t.getNAJulio() >= t.getNPJulio()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 5:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPJunio() != 0) {programadas = programadas + 1D;if(t.getNAJunio()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAJunio() != 0) {if (t.getNAJunio() >= t.getNPJunio()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 4:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPMayo() != 0) {programadas = programadas + 1D;if(t.getNAMayo()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAMayo() != 0) {if (t.getNAMayo() >= t.getNPMayo()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 3:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPAbril() != 0) {programadas = programadas + 1D;if(t.getNAAbril()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAAbril() != 0) {if (t.getNAAbril() >= t.getNPAbril()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 2:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPMarzo() != 0) {programadas = programadas + 1D;if(t.getNAMarzo()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAMarzo() != 0) {if (t.getNAMarzo() >= t.getNPMarzo()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 1:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPFebrero() != 0) {programadas = programadas + 1D;if(t.getNAFebrero()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAFebrero() != 0) {if (t.getNAFebrero() >= t.getNPFebrero()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
                case 0:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPEnero() != 0) {programadas = programadas + 1D;if(t.getNAEnero()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAEnero() != 0) {if (t.getNAEnero() >= t.getNPEnero()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
            }
            resumenCprogramadas = resumenCprogramadas + programadas;
            resumenCalcanzadas = resumenCalcanzadas + alcanzadas;
            resumenCnoalcanzadas = resumenCnoalcanzadas + noalcanzadas;
            if (programadas == 0) {
                promedioSp = (alcanzadas * 100D);
            } else {
                promedioSp = (alcanzadas * 100D) / programadas;
            }
            promedioCP = promedioPenalizado(promedioSp);
            resumen.add(new DtoCmi.ConcentradoDatos(poau.obtenerMesNombre(i), programadas, alcanzadas, noalcanzadas, promedioSp, promedioCP, poau.obtenerSemaforo(promedioSp), poau.obtenerSemaforo(promedioCP), Boolean.FALSE));
            cp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), promedioCP, promedioCP));
            sp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), promedioSp, promedioSp));
            agregaInformeC(i);
            
        }
        cumplimiento.add(sp);
        cumplimiento.add(cp);
        return cumplimiento;
    }
    
    public List<List<DtoCmi.Grafica>> promediosAcumulados() {
        List<List<DtoCmi.Grafica>> cumplimiento = new ArrayList<>();
        List<DtoCmi.Grafica> sp = new ArrayList<>();
        List<DtoCmi.Grafica> cp = new ArrayList<>();
        resumenAcumulado = new ArrayList<>();
        for (int i = 0; i <= numeroMes; i++) {
            // -------------------------------------------------------------------------------------------
            programadas = 0D;
            alcanzadas = 0D;
            noalcanzadas = 0D;
            promedioSp = 0D;
            promedioSp = 0D;
            switch (i) {
                case 11:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPDiciembre() != 0) {programadas = programadas + 1D;if(t.getNADiciembre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNADiciembre() != 0) {if (t.getNADiciembre() >= t.getNPDiciembre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 10:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPNoviembre() != 0) {programadas = programadas + 1D;if(t.getNANoviembre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNANoviembre() != 0) {if (t.getNANoviembre() >= t.getNPNoviembre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 9:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPOctubre() != 0) {programadas = programadas + 1D;if(t.getNAOctubre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAOctubre() != 0) {if (t.getNAOctubre() >= t.getNPOctubre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 8:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPSeptiembre() != 0) {programadas = programadas + 1D;if(t.getNASeptiembre()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNASeptiembre() != 0) {if (t.getNASeptiembre() >= t.getNPSeptiembre()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 7:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPAgosto() != 0) {programadas = programadas + 1D;if(t.getNAAgosto()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAAgosto() != 0) {if (t.getNAAgosto() >= t.getNPAgosto()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 6:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPJulio() != 0) {programadas = programadas + 1D;if(t.getNAJulio()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAJulio() != 0) {if (t.getNAJulio() >= t.getNPJulio()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 5:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPJunio() != 0) {programadas = programadas + 1D;if(t.getNAJunio()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAJunio() != 0) {if (t.getNAJunio() >= t.getNPJunio()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 4:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPMayo() != 0) {programadas = programadas + 1D;if(t.getNAMayo()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAMayo() != 0) {if (t.getNAMayo() >= t.getNPMayo()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 3:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPAbril() != 0) {programadas = programadas + 1D;if(t.getNAAbril()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAAbril() != 0) {if (t.getNAAbril() >= t.getNPAbril()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 2:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPMarzo() != 0) {programadas = programadas + 1D;if(t.getNAMarzo()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAMarzo() != 0) {if (t.getNAMarzo() >= t.getNPMarzo()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 1:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPFebrero() != 0) {programadas = programadas + 1D;if(t.getNAFebrero()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAFebrero() != 0) {if (t.getNAFebrero() >= t.getNPFebrero()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                case 0:
                    actividadesPoas.forEach((t) -> {
                        if (t.getNPEnero() != 0) {programadas = programadas + 1D;if(t.getNAEnero()==0){noalcanzadas = noalcanzadas + 1D;}}
                        if (t.getNAEnero() != 0) {if (t.getNAEnero() >= t.getNPEnero()) {alcanzadas = alcanzadas + 1D;}else{ noalcanzadas = noalcanzadas + 1D;}}
                    });
                break;
            }
            if(programadas==0){
                promedioSp=(alcanzadas*100D);
            }else{
                promedioSp=(alcanzadas*100D)/programadas;
            }
            promedioCP=promedioPenalizado(promedioSp);
            resumenAcumulado.add(new DtoCmi.ConcentradoDatos(poau.obtenerMesNombre(i), programadas, alcanzadas, noalcanzadas,promedioSp,promedioCP,poau.obtenerSemaforo(promedioSp),poau.obtenerSemaforo(promedioCP),Boolean.FALSE));
            cp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), promedioCP, promedioCP));
            sp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), promedioSp, promedioSp));
            
        }
        cumplimiento.add(sp);
        cumplimiento.add(cp);
        return cumplimiento;
    }
    
    public Double promedioPenalizado(Double promedioSP) {
        if (promedioSP >= 116 && promedioSP <= 166) {
            return 89.99;
        } else if (promedioSP >= 166 && promedioSP <= 216) {
            return 84.99;
        } else if (promedioSP >= 216 && promedioSP <= 266) {
            return 79.99;
        } else if (promedioSP >= 266 && promedioSP <= 316) {
            return 74.99;
        } else if(promedioSP >= 316){
            return 69.99;
        }else{
            return promedioSP;
        }
    }
    
    public void agregaInformeC(Integer m) {
        resumenCpromedioCP = 0D;
        resumenCpromedioSp = 0D;
        if (numeroMes >= 0 && numeroMes <= 3) {
            if (Objects.equals(m, numeroMes)) {
                resumenCpromedioSp = (resumenCalcanzadas * 100D) / resumenCprogramadas;
                resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
                resumen.add(new DtoCmi.ConcentradoDatos("Cuatrimestre Enero-Abril", resumenCprogramadas, resumenCalcanzadas, resumenCnoalcanzadas, resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP),Boolean.TRUE));
                reseteaConteoCuatrimestral();
            }
        } else if (m == 3) {
            resumenCpromedioSp = (resumenCalcanzadas * 100D) / resumenCprogramadas;
            resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
            resumen.add(new DtoCmi.ConcentradoDatos("Cuatrimestre Enero-Abril", resumenCprogramadas, resumenCalcanzadas, resumenCnoalcanzadas, resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP),Boolean.TRUE));
            reseteaConteoCuatrimestral();
        } else if (numeroMes >= 4 && numeroMes <= 7) {
            if (Objects.equals(m, numeroMes)) {
                resumenCpromedioSp = (resumenCalcanzadas * 100D) / resumenCprogramadas;
                resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
                resumen.add(new DtoCmi.ConcentradoDatos("Cuatrimestre Mayo-Agosto", resumenCprogramadas, resumenCalcanzadas, resumenCnoalcanzadas, resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP),Boolean.TRUE));
                reseteaConteoCuatrimestral();
            }
        } else if (m == 7) {
            resumenCpromedioSp = (resumenCalcanzadas * 100D) / resumenCprogramadas;
            resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
            resumen.add(new DtoCmi.ConcentradoDatos("Cuatrimestre Mayo-Agosto", resumenCprogramadas, resumenCalcanzadas, resumenCnoalcanzadas, resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP),Boolean.TRUE));
            reseteaConteoCuatrimestral();
        } else if (numeroMes >= 8 && numeroMes <= 11) {
            resumenCpromedioSp = (resumenCalcanzadas * 100D) / resumenCprogramadas;
            resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
            resumen.add(new DtoCmi.ConcentradoDatos("Cuatrimestre Septiembre-Diciembre", resumenCprogramadas, resumenCalcanzadas, resumenCnoalcanzadas, resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP),Boolean.TRUE));
            reseteaConteoCuatrimestral();
        }
    }
    public void reseteaConteoCuatrimestral(){
        resumenCpromedioCP = 0D;
        resumenCpromedioSp = 0D;
        resumenCprogramadas = 0D;
        resumenCalcanzadas = 0D;
        resumenCnoalcanzadas = 0D;
        resumenCpromedioSp = 0D;
        resumenCpromedioSp = 0D;
    }
    
    public String nombreAre(){
        return areasConsulta.stream().filter(t -> t.getArea()==claveArea).collect(Collectors.toList()).get(0).getNombre();
    }

}
