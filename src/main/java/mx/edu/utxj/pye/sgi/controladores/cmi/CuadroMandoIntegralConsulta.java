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
import java.io.File;
import java.io.IOException;
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
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.model.chart.MeterGaugeChartModel;

@Named
@ViewScoped
public class CuadroMandoIntegralConsulta implements Serializable {
    
    @Getter    @Setter    private List<AreasUniversidad> areasConsulta = new ArrayList<>();
    @Getter    @Setter    private List<EjesRegistro> ejesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> actividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<List<DtoCmi.Grafica>> cumplimientoMensual = new ArrayList<>();
    @Getter    @Setter    private List<List<DtoCmi.Grafica>> cumplimientoAcumuladol = new ArrayList<>();
    @Getter    @Setter    private List<DtoCmi.ConcentradoDatos> resumen;
    @Getter    @Setter    private List<DtoCmi.ReporteCuatrimestralAreas> reporte;
    @Getter    @Setter    private List<Integer> actividadesYaContadas;
    
    @Getter    @Setter    private DtoCmi.ResultadosCMI cmiGeneral;
    @Getter    @Setter    private EjerciciosFiscales ejercicioFiscal;
    
    @Getter    @Setter    private DecimalFormat df = new DecimalFormat("#.00");
    
    @Getter    @Setter    private Integer numeroMes=0,claveEjeR,numeroCuatrimestre=0;
    @Getter    @Setter    private Integer programadoA=0,cumplidoA=0,noCumplidoA=0;
    
    @Getter    @Setter    private Double proEje1=0D,proEje2=0D,proEje3=0D,proEje4=0D;
    @Getter    @Setter    private Double alcEje1=0D,alcEje2=0D,alcEje3=0D,alcEje4=0D;
    @Getter    @Setter    private Double cumEje1=0D,cumEje2=0D,cumEje3=0D,cumEje4=0D;
    @Getter    @Setter    private Double ct=0D;
    @Getter    @Setter    private String eje1="S",eje2="S",eje3="S",eje4="S";
    
    @Getter    @Setter    private Double programadas,alcanzadas,noalcanzadas,promedioSp,promedioCP;
    @Getter    @Setter    private Double programadasAcumuladas,alcanzadasAcumuladas;
    @Getter    @Setter    private Double resumenCnoalcanzadas,resumenCpromedioSp,resumenCpromedioCP;
    @Getter    @Setter    private Double resumenCacumuladasProgramadas,resumenCacumuladasAlcanzadas;
    
    @Getter    @Setter    private Short claveArea=0;
    @Getter    @Setter    private String mes = "",extender="myconfig";
    
    @Getter    @Setter    private Boolean mostrar=Boolean.TRUE,mostrarConcnetrado=Boolean.FALSE,imprimir=Boolean.FALSE;
    
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
            actividadesYaContadas= new ArrayList<>();
            consultrAreasEvaluacion();
            consultaejes();
            reseteador();
            generaCmiGeneral();
        }
    }
// Eventos
    public void numeroAnioAsiganado(ValueChangeEvent event) {
        reseteador();
        ejercicioFiscal = new EjerciciosFiscales();
        ejercicioFiscal = ejercicioFiscal = poau.obtenerAnioRegistro(Short.parseShort(event.getNewValue().toString()));
        if (ejercicioFiscal.getEjercicioFiscal() == controladorEmpleado.getCalendarioevaluacionpoa().getEjercicioFiscal()) {
            numeroMes = poau.obtenerMesNumero(controladorEmpleado.getCalendarioevaluacionpoa().getMesEvaluacion());
            mes="Avance al mes de: "+controladorEmpleado.getCalendarioevaluacionpoa().getMesEvaluacion()+" del "+ejercicioFiscal.getAnio();
        } else if (ejercicioFiscal.getEjercicioFiscal() <= controladorEmpleado.getCalendarioevaluacionpoa().getEjercicioFiscal()) {
            numeroMes = 11;
            mes="Avance al mes de: Diciembre del "+ejercicioFiscal.getAnio();
        }else{
            numeroMes = 0;
            mes="Avance al mes de: Enero del "+ejercicioFiscal.getAnio();
        }
        claveEjeR = 0;
        consultaejes();
        generaCmiGeneral();
    }

    public void asignarAreaEvaluada(ValueChangeEvent event) {
        reseteador();
        mostrarConcnetrado=Boolean.FALSE;
        numeroCuatrimestre=0;
        claveEjeR = 0;
        claveArea = Short.parseShort(event.getNewValue().toString());
        consultaejes();
        if(claveArea == 0){
            mostrarConcnetrado=Boolean.TRUE;
            creaReporteCuatrimestralAcumulaado();
        }
        generaCmiGeneral();
    }

    public void asignarEjeEvaluado(ValueChangeEvent event) {
        reseteador();
        claveEjeR = Integer.parseInt(event.getNewValue().toString());
        if (claveEjeR == 0) {
            generaCmiGeneral();
        } else {
            ejeSeleccionado();
        }
    }
    
    public void asignarCuatrimestre(ValueChangeEvent event) {
        reseteador();
        numeroCuatrimestre = Integer.parseInt(event.getNewValue().toString());
        creaReporteCuatrimestralAcumulaado();
        generaCmiGeneral();
        mostrarConcnetrado = Boolean.TRUE;
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

    public void imprimirValores() {
    }
    
    
    public String nombreAre(){
        return areasConsulta.stream().filter(t -> t.getArea()==claveArea).collect(Collectors.toList()).get(0).getNombre();
    }
    
    public void descargarPlantillaCatalogos() throws IOException, Throwable{
        File f = new File(ejbCatalogosPoa.getReporteCuadroMandoPOA(ejercicioFiscal.getEjercicioFiscal(),reporte));
        Faces.sendFile(f, true);
    }
// Inicializadores
    public void reseteador() {
        cmiGeneral = new DtoCmi.ResultadosCMI("", 0, 0, 0, 0D, 0D, extender, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new MeterGaugeChartModel(), Boolean.FALSE);
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
    
    public void reseteaConteoCuatrimestral() {
        resumenCpromedioCP = 0D;
        resumenCpromedioSp = 0D;
        resumenCnoalcanzadas = 0D;
        resumenCacumuladasProgramadas = 0D;
        resumenCacumuladasAlcanzadas = 0D;
        actividadesYaContadas = new ArrayList<>();
    }
    
// Catalogos
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
        if (controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 148 || controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 30 || controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 284 || controladorEmpleado.getNuevoOBJListaPersonal().getClave() == 613) {
            try {
                areasConsulta.add(new AreasUniversidad(Short.parseShort("0"), "Institucional", mes, extender, true));
                areasConsulta.addAll(areasLogeo.getAreasUniversidadConPoa());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
                Logger.getLogger(ControladorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
            }
            imprimir=Boolean.TRUE;
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
    
//  Presentacion de informacion

    public void generaCmiGeneral() {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        if (claveArea != 0) {
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasTotalArea(claveArea, ejercicioFiscal.getEjercicioFiscal()).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        } else {
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasUniversidadaEjercicioFiscal(ejercicioFiscal.getEjercicioFiscal()).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        }
        cmiGeneral = new DtoCmi.ResultadosCMI(poau.obtenerAreaNombre(controladorEmpleado.getProcesopoa().getArea()), 0, 0, 0, 0D, 0D, extender, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new MeterGaugeChartModel(), Boolean.FALSE);
        if (!actividadesPoas.isEmpty()) {
            mostrar = Boolean.TRUE;
            if (numeroCuatrimestre == 1 || numeroCuatrimestre == 0) {
                cumplimientoMensual = promediosMensuales(0);
                cumplimientoAcumuladol = promediosAcumulados(0);
            } else if (numeroCuatrimestre == 2) {
                cumplimientoMensual = promediosMensuales(4);
                cumplimientoAcumuladol = promediosAcumulados(4);
            } else {
                cumplimientoMensual = promediosMensuales(8);
                cumplimientoAcumuladol = promediosAcumulados(8);
            }
//            cmiGeneral = new DtoCmi.ResultadosCMI(poau.obtenerAreaNombre(controladorEmpleado.getProcesopoa().getArea()), programadas.intValue(), alcanzadas.intValue(), noalcanzadas.intValue(), promedioCP, promedioSp, extender, resumen, cumplimientoMensual.get(0), cumplimientoMensual.get(1), cumplimientoAcumuladol.get(0), cumplimientoAcumuladol.get(1), generaGraficaResumen(promedioSp), Boolean.TRUE);
            Double alc=programadasAcumuladas - alcanzadasAcumuladas;
            cmiGeneral = new DtoCmi.ResultadosCMI(poau.obtenerAreaNombre(controladorEmpleado.getProcesopoa().getArea()), programadasAcumuladas.intValue(), alcanzadasAcumuladas.intValue(), alc.intValue(), promedioCP, promedioSp, extender, resumen, cumplimientoMensual.get(0), cumplimientoMensual.get(1), cumplimientoAcumuladol.get(0), cumplimientoAcumuladol.get(1), generaGraficaResumen(promedioSp), Boolean.TRUE);
        } else {
            mostrar = Boolean.FALSE;
        }
    }

    public DtoCmi.ResultadosCMI generaCmiPorEje(EjesRegistro eje) {
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        if (claveArea != 0) {
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasEje(claveArea, ejercicioFiscal.getEjercicioFiscal(), eje).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        } else {
            actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(ejercicioFiscal.getEjercicioFiscal(), eje).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
        }
        DtoCmi.ResultadosCMI cmiEje = new DtoCmi.ResultadosCMI(eje.getNombre(), 0, 0, 0, 0D, 0D, extender, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new MeterGaugeChartModel(), Boolean.FALSE);
        if (!actividadesPoas.isEmpty()) {
            mostrar = Boolean.TRUE;
            if (numeroCuatrimestre == 1 || numeroCuatrimestre == 0) {
                cumplimientoMensual = promediosMensuales(0);
                cumplimientoAcumuladol = promediosAcumulados(0);
            } else if (numeroCuatrimestre == 2) {
                cumplimientoMensual = promediosMensuales(4);
                cumplimientoAcumuladol = promediosAcumulados(4);
            } else {
                cumplimientoMensual = promediosMensuales(8);
                cumplimientoAcumuladol = promediosAcumulados(8);
            }
//            cmiGeneral = new DtoCmi.ResultadosCMI(eje.getNombre(), programadas.intValue(), alcanzadas.intValue(), noalcanzadas.intValue(), promedioCP, promedioSp, extender,resumen, cumplimientoMensual.get(0), cumplimientoMensual.get(1),cumplimientoAcumuladol.get(0),cumplimientoAcumuladol.get(1), generaGraficaResumen(promedioSp), Boolean.TRUE);
            System.out.println("programadasAcumuladas" + programadasAcumuladas);
            System.out.println("alcanzadasAcumuladas" + alcanzadasAcumuladas);            
            Double alc=programadasAcumuladas - alcanzadasAcumuladas;
            cmiGeneral = new DtoCmi.ResultadosCMI(eje.getNombre(), programadasAcumuladas.intValue(), alcanzadasAcumuladas.intValue(), alc.intValue(), promedioCP, promedioSp, extender, resumen, cumplimientoMensual.get(0), cumplimientoMensual.get(1), cumplimientoAcumuladol.get(0), cumplimientoAcumuladol.get(1), generaGraficaResumen(promedioSp), Boolean.TRUE);
        } else {
            mostrar = Boolean.FALSE;
        }
        return cmiEje;
    }
    
    public List<List<DtoCmi.Grafica>> promediosMensuales(Integer mesInicio) {
        List<List<DtoCmi.Grafica>> cumplimiento = new ArrayList<>();
        List<DtoCmi.Grafica> sp = new ArrayList<>();
        List<DtoCmi.Grafica> cp = new ArrayList<>();
        resumen = new ArrayList<>();
        reseteaConteoCuatrimestral();
        for (int i = mesInicio; i <= numeroMes; i++) {
            programadas = 0D;
            alcanzadas = 0D;
            noalcanzadas = 0D;
            promedioSp = 0D;
            promedioSp = 0D;
            programadasAcumuladas = 0D;
            alcanzadasAcumuladas = 0D;
            switch (i) {
                case 11:actividadesPoas.forEach((t) -> {conteo(t.getNPDiciembre(), t.getNADiciembre(), t.getActividadPoa());});break;
                case 10:actividadesPoas.forEach((t) -> {conteo(t.getNPNoviembre(), t.getNANoviembre(), t.getActividadPoa());});break;
                case 9:actividadesPoas.forEach((t) -> {conteo(t.getNPOctubre(), t.getNAOctubre(), t.getActividadPoa());});break;
                case 8:actividadesPoas.forEach((t) -> {conteo(t.getNPSeptiembre(), t.getNASeptiembre(), t.getActividadPoa());});break;
                case 7:actividadesPoas.forEach((t) -> {conteo(t.getNPAgosto(), t.getNAAgosto(), t.getActividadPoa());});break;
                case 6:actividadesPoas.forEach((t) -> {conteo(t.getNPJulio(), t.getNAJulio(), t.getActividadPoa());});break;
                case 5:actividadesPoas.forEach((t) -> {conteo(t.getNPJunio(), t.getNAJunio(), t.getActividadPoa());});break;
                case 4:actividadesPoas.forEach((t) -> {conteo(t.getNPMayo(), t.getNAMayo(), t.getActividadPoa());});break;
                case 3:actividadesPoas.forEach((t) -> {conteo(t.getNPAbril(), t.getNAAbril(), t.getActividadPoa());});break;
                case 2:actividadesPoas.forEach((t) -> {conteo(t.getNPMarzo(), t.getNAMarzo(), t.getActividadPoa());});break;
                case 1:actividadesPoas.forEach((t) -> {conteo(t.getNPFebrero(), t.getNAFebrero(), t.getActividadPoa());});break;
                case 0:actividadesPoas.forEach((t) -> {conteo(t.getNPEnero(), t.getNAEnero(), t.getActividadPoa());});break;
            }
            promediosAcumuladosRm(i);
            resumenCacumuladasProgramadas = programadasAcumuladas;
            resumenCacumuladasAlcanzadas = alcanzadasAcumuladas;
//            if (programadas == 0) {
//                promedioSp = (alcanzadas * 100D);
//            } else {
//                promedioSp = (alcanzadas * 100D) / programadas;
//            }
//            promedioCP = promedioPenalizado(promedioSp);
            
            if (programadasAcumuladas == 0) {
                promedioSp = (alcanzadasAcumuladas * 100D);
            } else {
                promedioSp = (alcanzadasAcumuladas * 100D) / programadasAcumuladas;
            }
            promedioCP = promedioPenalizado(promedioSp);
            resumen.add(new DtoCmi.ConcentradoDatos(poau.obtenerMesNombre(i), programadas.intValue(), alcanzadas.intValue(), noalcanzadas.intValue(), promedioSp, promedioCP, poau.obtenerSemaforo(promedioSp), poau.obtenerSemaforo(promedioCP),resumenCacumuladasProgramadas.intValue(),resumenCacumuladasAlcanzadas.intValue(), Boolean.FALSE));
//            cp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), promedioCP, promedioCP));
//            sp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), promedioSp, promedioSp));
            cp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), alcanzadasAcumuladas, alcanzadasAcumuladas));
            sp.add(new DtoCmi.Grafica(poau.obtenerMesNombre(i), programadasAcumuladas, programadasAcumuladas));
            agregaInformeC(i);
            
        }
        cumplimiento.add(sp);
        cumplimiento.add(cp);
        return cumplimiento;
    }
    
    public void conteo(Short programado, Short alcanzado, Integer claveActividad) {
        if (programado != 0 && !actividadesYaContadas.contains(claveActividad)) {
            programadas = programadas + 1D;
//            actividadesYaContadas.add(claveActividad);
            if (alcanzado == 0) {
                noalcanzadas = noalcanzadas + 1D;
            }
        }
        if (alcanzado != 0) {
            if (alcanzado >= programado) {
                alcanzadas = alcanzadas + 1D;
            } else {
                noalcanzadas = noalcanzadas + 1D;
            }
        }
    }
    
    
    public void promediosAcumuladosRm(Integer mesC) {
        programadasAcumuladas = 0D;
        alcanzadasAcumuladas = 0D;
        actividadesPoas.forEach((t) -> {
            programadoA = 0;
            cumplidoA = 0;
            switch (mesC) {
                case 11:programadoA = programadoA + t.getNPDiciembre();cumplidoA = cumplidoA + t.getNADiciembre();
                case 10:programadoA = programadoA + t.getNPNoviembre();cumplidoA = cumplidoA + t.getNANoviembre();
                case 9:programadoA = programadoA + t.getNPOctubre();cumplidoA = cumplidoA + t.getNAOctubre();
                case 8:programadoA = programadoA + t.getNPSeptiembre();cumplidoA = cumplidoA + t.getNASeptiembre();
                case 7:programadoA = programadoA + t.getNPAgosto();cumplidoA = cumplidoA + t.getNAAgosto();
                case 6:programadoA = programadoA + t.getNPJulio();cumplidoA = cumplidoA + t.getNAJulio();
                case 5:programadoA = programadoA + t.getNPJunio();cumplidoA = cumplidoA + t.getNAJunio();
                case 4:programadoA = programadoA + t.getNPMayo();cumplidoA = cumplidoA + t.getNAMayo();
                case 3:programadoA = programadoA + t.getNPAbril();cumplidoA = cumplidoA + t.getNAAbril();
                case 2:programadoA = programadoA + t.getNPMarzo();cumplidoA = cumplidoA + t.getNAMarzo();
                case 1:programadoA = programadoA + t.getNPFebrero();cumplidoA = cumplidoA + t.getNAFebrero();
                case 0:programadoA = programadoA + t.getNPEnero();cumplidoA = cumplidoA + t.getNAEnero();break;
            }
            if (programadoA != 0) {programadasAcumuladas = programadasAcumuladas + 1D;}
            if (cumplidoA != 0) {if (cumplidoA >= programadoA) {alcanzadasAcumuladas = alcanzadasAcumuladas + 1D;} 
            }
        });
    }
    
    public void promediosAcumuladosRCuatrimestral(Integer mesC) {
        programadasAcumuladas = 0D;
        alcanzadasAcumuladas = 0D;
        actividadesPoas.forEach((t) -> {
            programadoA = 0;
            cumplidoA = 0;
            switch (mesC) {
                case 11:programadoA = programadoA + t.getNPDiciembre();cumplidoA = cumplidoA + t.getNADiciembre();
                case 10:programadoA = programadoA + t.getNPNoviembre();cumplidoA = cumplidoA + t.getNANoviembre();
                case 9:programadoA = programadoA + t.getNPOctubre();cumplidoA = cumplidoA + t.getNAOctubre();
                case 8:programadoA = programadoA + t.getNPSeptiembre();cumplidoA = cumplidoA + t.getNASeptiembre();break;
                case 7:programadoA = programadoA + t.getNPAgosto();cumplidoA = cumplidoA + t.getNAAgosto();
                case 6:programadoA = programadoA + t.getNPJulio();cumplidoA = cumplidoA + t.getNAJulio();
                case 5:programadoA = programadoA + t.getNPJunio();cumplidoA = cumplidoA + t.getNAJunio();
                case 4:programadoA = programadoA + t.getNPMayo();cumplidoA = cumplidoA + t.getNAMayo();break;
                case 3:programadoA = programadoA + t.getNPAbril();cumplidoA = cumplidoA + t.getNAAbril();
                case 2:programadoA = programadoA + t.getNPMarzo();cumplidoA = cumplidoA + t.getNAMarzo();
                case 1:programadoA = programadoA + t.getNPFebrero();cumplidoA = cumplidoA + t.getNAFebrero();
                case 0:programadoA = programadoA + t.getNPEnero();cumplidoA = cumplidoA + t.getNAEnero();break;
            }
            if (programadoA != 0) {
                programadasAcumuladas = programadasAcumuladas + 1D;
                if (cumplidoA == 0) {
                    resumenCnoalcanzadas=resumenCnoalcanzadas + 1D;
                }
                        
            }
            if (cumplidoA != 0) {
                if (cumplidoA >= programadoA) {
                    alcanzadasAcumuladas = alcanzadasAcumuladas + 1D;
                }else{
                    resumenCnoalcanzadas=resumenCnoalcanzadas + 1D;
                }
            }
        });
    }
    
    public void promediosAcumuladosResumenGeneral(Integer mesC) {
        programadasAcumuladas = 0D;
        alcanzadasAcumuladas = 0D;
        actividadesPoas.forEach((t) -> {
            programadoA = 0;
            cumplidoA = 0;
            switch (mesC) {
                case 11:programadoA = programadoA + t.getNPDiciembre();cumplidoA = cumplidoA + t.getNADiciembre();
                case 10:programadoA = programadoA + t.getNPNoviembre();cumplidoA = cumplidoA + t.getNANoviembre();
                case 9:programadoA = programadoA + t.getNPOctubre();cumplidoA = cumplidoA + t.getNAOctubre();
                case 8:programadoA = programadoA + t.getNPSeptiembre();cumplidoA = cumplidoA + t.getNASeptiembre();
                case 7:programadoA = programadoA + t.getNPAgosto();cumplidoA = cumplidoA + t.getNAAgosto();
                case 6:programadoA = programadoA + t.getNPJulio();cumplidoA = cumplidoA + t.getNAJulio();
                case 5:programadoA = programadoA + t.getNPJunio();cumplidoA = cumplidoA + t.getNAJunio();
                case 4:programadoA = programadoA + t.getNPMayo();cumplidoA = cumplidoA + t.getNAMayo();
                case 3:programadoA = programadoA + t.getNPAbril();cumplidoA = cumplidoA + t.getNAAbril();
                case 2:programadoA = programadoA + t.getNPMarzo();cumplidoA = cumplidoA + t.getNAMarzo();
                case 1:programadoA = programadoA + t.getNPFebrero();cumplidoA = cumplidoA + t.getNAFebrero();
                case 0:programadoA = programadoA + t.getNPEnero();cumplidoA = cumplidoA + t.getNAEnero();
            }
            if (programadoA != 0) {
                programadasAcumuladas = programadasAcumuladas + 1D;
                if (cumplidoA == 0) {
                    resumenCnoalcanzadas=resumenCnoalcanzadas + 1D;
                }
                        
            }
            if (cumplidoA != 0) {
                if (cumplidoA >= programadoA) {
                    alcanzadasAcumuladas = alcanzadasAcumuladas + 1D;
                }else{
                    resumenCnoalcanzadas=resumenCnoalcanzadas + 1D;
                }
            }
        });
    }
    
    public List<List<DtoCmi.Grafica>> promediosAcumulados(Integer mesInicio) {
        List<List<DtoCmi.Grafica>> cumplimiento = new ArrayList<>();
        List<DtoCmi.Grafica> sp = new ArrayList<>();
        List<DtoCmi.Grafica> cp = new ArrayList<>();
        for (int i = mesInicio; i <= numeroMes; i++) {
            promediosAcumuladosRm(i);
            if(programadasAcumuladas==0){
                promedioSp=(alcanzadasAcumuladas*100D);
            }else{
                promedioSp=(alcanzadasAcumuladas*100D)/programadasAcumuladas;
            }
            promedioCP=promedioPenalizado(promedioSp);
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
        if ((numeroMes >= 0 && numeroMes <= 3) || m == 3) {
            if ((Objects.equals(m, numeroMes)) || m == 3) {
                promediosAcumuladosRCuatrimestral(m);
                if (programadasAcumuladas == 0) {
                    resumenCpromedioSp = (alcanzadasAcumuladas * 100D);
                } else {
                    resumenCpromedioSp = (alcanzadasAcumuladas * 100D) / programadasAcumuladas;
                }
                resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
                DtoCmi.ConcentradoDatos cd= new DtoCmi.ConcentradoDatos("Cuatrimestre Enero - Abril", programadasAcumuladas.intValue(), alcanzadasAcumuladas.intValue(), resumenCnoalcanzadas.intValue(), resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP), 0,0, Boolean.TRUE);
                promediosAcumuladosRm(m);
                cd.setAcumuladoP(programadasAcumuladas.intValue());
                cd.setAcumuladoA(alcanzadasAcumuladas.intValue());
                resumen.add(cd);
                reseteaConteoCuatrimestral();
            }
        } else if ((numeroMes >= 4 && numeroMes <= 7) || m == 7) {
            if ((Objects.equals(m, numeroMes)) || m == 7) {
                promediosAcumuladosRCuatrimestral(m);
                if (programadasAcumuladas == 0) {
                    resumenCpromedioSp = (alcanzadasAcumuladas * 100D);
                } else {
                    resumenCpromedioSp = (alcanzadasAcumuladas * 100D) / programadasAcumuladas;
                }
                resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
                DtoCmi.ConcentradoDatos cd= new DtoCmi.ConcentradoDatos("Cuatrimestre Mayo - Agosto", programadasAcumuladas.intValue(), alcanzadasAcumuladas.intValue(), resumenCnoalcanzadas.intValue(), resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP), 0,0, Boolean.TRUE);
                promediosAcumuladosRm(m);
                cd.setAcumuladoP(programadasAcumuladas.intValue());
                cd.setAcumuladoA(alcanzadasAcumuladas.intValue());
                resumen.add(cd);
                reseteaConteoCuatrimestral();
            }
        } else if (numeroMes >= 8 && numeroMes <= 11) {
            if (Objects.equals(m, numeroMes)) {
                promediosAcumuladosRCuatrimestral(m);
                if (programadasAcumuladas == 0) {
                    resumenCpromedioSp = (alcanzadasAcumuladas * 100D);
                } else {
                    resumenCpromedioSp = (alcanzadasAcumuladas * 100D) / programadasAcumuladas;
                }
                resumenCpromedioCP = promedioPenalizado(resumenCpromedioSp);
                DtoCmi.ConcentradoDatos cd= new DtoCmi.ConcentradoDatos("Cuatrimestre Septiembre - Diciembre", programadasAcumuladas.intValue(), alcanzadasAcumuladas.intValue(), resumenCnoalcanzadas.intValue(), resumenCpromedioSp, resumenCpromedioCP, poau.obtenerSemaforo(resumenCpromedioSp), poau.obtenerSemaforo(resumenCpromedioCP), 0,0, Boolean.TRUE);
                promediosAcumuladosRm(m);
                cd.setAcumuladoP(programadasAcumuladas.intValue());
                cd.setAcumuladoA(alcanzadasAcumuladas.intValue());
                resumen.add(cd);
                reseteaConteoCuatrimestral();
            }
        }
    }
    
    public void creaReporteCuatrimestralAcumulaado() {
        reporte=new ArrayList<>();
        switch (numeroCuatrimestre) {
            case 0:
                numeroMes = poau.obtenerMesNumero(controladorEmpleado.getCalendarioevaluacionpoa().getMesEvaluacion()); 
                break;
            case 1:
                numeroMes = 3;
                break;
            case 2:
                numeroMes = 7;
                break;
            case 3:
                numeroMes = 11;
                break;
        }
        areasConsulta.forEach((a) -> {
            proEje1 = 0D;proEje2 = 0D;proEje2 = 0D;proEje2 = 0D;

            alcEje1 = 0D;alcEje2 = 0D;alcEje3 = 0D;alcEje4 = 0D;
            
            cumEje1 = 0D;cumEje2 = 0D;cumEje3 = 0D;cumEje4 = 0D;
            
            eje1="S";eje2="S";eje3="S";eje4="S";
            
            ct=0D;
            if(a.getArea()!=0){
                ejesRegistros.forEach((e) -> {
                actividadesPoas = new ArrayList<>();
                actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasEje(a.getArea(), ejercicioFiscal.getEjercicioFiscal(), e).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
                switch (e.getEje()) {
                    case 1:if (actividadesPoas.isEmpty()) {eje1 = "N";proEje1 = 0D;alcEje1 = 0D;} else {eje1 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje1 = programadasAcumuladas;alcEje1 = alcanzadasAcumuladas;}break;
                    case 2:if (actividadesPoas.isEmpty()) {eje2 = "N";proEje2 = 0D;alcEje2 = 0D;} else {eje2 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje2 = programadasAcumuladas;alcEje2 = alcanzadasAcumuladas;}break;
                    case 3:if (actividadesPoas.isEmpty()) {eje3 = "N";proEje3 = 0D;alcEje3 = 0D;} else {eje3 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje3 = programadasAcumuladas;alcEje3 = alcanzadasAcumuladas;}break;
                    case 4:if (actividadesPoas.isEmpty()) {eje4 = "N";proEje4 = 0D;alcEje4 = 0D;} else {eje4 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje4 = programadasAcumuladas;alcEje4 = alcanzadasAcumuladas;}break;
                }
            });
            
                if (eje1.equals("S")) {if (proEje1 == 0) {cumEje1 = (alcEje1 * 100D);} else {cumEje1 = (alcEje1 * 100D) / proEje1;}}
                if (eje2.equals("S")) {if (proEje2 == 0) {cumEje2 = (alcEje2 * 100D);} else {cumEje2 = (alcEje2 * 100D) / proEje2;}}
                if (eje3.equals("S")) {if (proEje3 == 0) {cumEje3 = (alcEje3 * 100D);} else {cumEje3 = (alcEje3 * 100D) / proEje3;}}
                if (eje4.equals("S")) {if (proEje4 == 0) {cumEje4 = (alcEje4 * 100D);} else {cumEje4 = (alcEje4 * 100D) / proEje4;}}
                Double al = alcEje1 + alcEje2 + alcEje3 + alcEje4;
                Double pr = proEje1 + proEje2 + proEje3 + proEje4;
                if (eje1.equals("S") || eje2.equals("S") || eje3.equals("S") || eje4.equals("S")) {
                    ct = (al * 100D) / (pr);
                    reporte.add(new DtoCmi.ReporteCuatrimestralAreas(a.getNombre(), proEje1.intValue(), alcEje1.intValue(), cumEje1, eje1, proEje2.intValue(), alcEje2.intValue(), cumEje2, eje2, proEje3.intValue(), alcEje3.intValue(), cumEje3, eje3, proEje4.intValue(), alcEje4.intValue(), cumEje4, eje4, ct, promedioPenalizado(ct)));
                }
            }
        });
        
        ejesRegistros.forEach((e) -> {
                actividadesPoas = new ArrayList<>();
                actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasUniversidadaEjeyEjercicioFiscal(ejercicioFiscal.getEjercicioFiscal(), e).stream().filter(t -> t.getBandera().equals("y")).collect(Collectors.toList());
                switch (e.getEje()) {
                    case 1:if (actividadesPoas.isEmpty()) {eje1 = "N";proEje1 = 0D;alcEje1 = 0D;} else {eje1 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje1 = programadasAcumuladas;alcEje1 = alcanzadasAcumuladas;}break;
                    case 2:if (actividadesPoas.isEmpty()) {eje2 = "N";proEje2 = 0D;alcEje2 = 0D;} else {eje2 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje2 = programadasAcumuladas;alcEje2 = alcanzadasAcumuladas;}break;
                    case 3:if (actividadesPoas.isEmpty()) {eje3 = "N";proEje3 = 0D;alcEje3 = 0D;} else {eje3 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje3 = programadasAcumuladas;alcEje3 = alcanzadasAcumuladas;}break;
                    case 4:if (actividadesPoas.isEmpty()) {eje4 = "N";proEje4 = 0D;alcEje4 = 0D;} else {eje4 = "S";if(numeroCuatrimestre == 0){promediosAcumuladosResumenGeneral(numeroMes);} else {promediosAcumuladosRm(numeroMes);}proEje4 = programadasAcumuladas;alcEje4 = alcanzadasAcumuladas;}break;
                }
            });
            if (eje1.equals("S")) {if (proEje1 == 0) {cumEje1 = (alcEje1 * 100D);} else {cumEje1 = (alcEje1 * 100D) / proEje1;}}
            if (eje2.equals("S")) {if (proEje2 == 0) {cumEje2 = (alcEje2 * 100D);} else {cumEje2 = (alcEje2 * 100D) / proEje2;}}
            if (eje3.equals("S")) {if (proEje3 == 0) {cumEje3 = (alcEje3 * 100D);} else {cumEje3 = (alcEje3 * 100D) / proEje3;}}
            if (eje4.equals("S")) {if (proEje4 == 0) {cumEje4 = (alcEje4 * 100D);} else {cumEje4 = (alcEje4 * 100D) / proEje4;}}
            
            Double al = alcEje1 + alcEje2 + alcEje3 + alcEje4;
                Double pr = proEje1 + proEje2 + proEje3 + proEje4;
                ct=(al * 100D)/ (pr);
            
            reporte.add(new DtoCmi.ReporteCuatrimestralAreas("Total de Actividades", proEje1.intValue(), alcEje1.intValue(), cumEje1, eje1, proEje2.intValue(), alcEje2.intValue(), cumEje2, eje2, proEje3.intValue(), alcEje3.intValue(), cumEje3, eje3, proEje4.intValue(), alcEje4.intValue(), cumEje4, eje4, ct, promedioPenalizado(ct)));
        

    }
    
    public String background(Double promedioSP) {
        if (promedioSP >=1 && promedioSP < 90) {
            return "164, 0, 0, 0.7";
        } else if (promedioSP >= 90 && promedioSP < 95) {
            return "255, 255, 0, 0.7";
        } else if (promedioSP >= 95 && promedioSP <116) {
            return "0, 176, 80, 0.7";
        }  else if (promedioSP >= 116) {
            return "255, 102, 0, 0.7";
        }else{
            return "0, 0, 0, 0";
        }
    }
}
