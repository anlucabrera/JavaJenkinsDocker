package mx.edu.utxj.pye.sgi.controladores.cmi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearTicks;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.line.LineChartDataSet;

@Named
@ViewScoped
public class CuadroMandoIntegralConsulta implements Serializable {
    
    @Getter    @Setter    private List<EjesRegistro> ejesRegistros = new ArrayList<>();
    @Getter    @Setter    private List<ActividadesPoa> actividadesPoas = new ArrayList<>();
    @Getter    @Setter    private List<List<Number>> valores;
    
    @Getter    @Setter    private DtoCmi.ResultadosCMI cmiGeneral;
    
    @Getter    @Setter    private Double actividadesProgramadas,promedio,actividadesSP,actividadesP,actividadesNoEvaluadas;
    
    
    
    
    @Getter    @Setter    private Short ejercicioFiscal = 0;
    @Getter    @Setter    private Double maximoValor=0D,promedioSP, promerdioP,actividadSinProgrmacion;
    @Getter    @Setter    private Double promedioMSP, promerdioMP,actividadesMSP,actividadesMP,actividadSinProgrmacionM;
    @Getter    @Setter    private Integer incremento = 0, numeroMes=0;
    @Getter    @Setter    private String mes = "";
    
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
            numeroMes = poau.obtenerMesNumero(controladorEmpleado.getCalendarioevaluacionpoa().getMesEvaluacion());
            mes="Avance al mes de: "+controladorEmpleado.getCalendarioevaluacionpoa().getMesEvaluacion();
            ejercicioFiscal = controladorEmpleado.getCalendarioevaluacionpoa().getEjercicioFiscal();
            consultaCatalogos();
        }
        createMixedModel();
    }
    
    public void consultaCatalogos() {
        ejesRegistros = new ArrayList<>();
        ejesRegistros.clear();
        ejesRegistros = ejbCatalogosPoa.mostrarEjesRegistrosAreas(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal);
        actividadesPoas = new ArrayList<>();
        actividadesPoas.clear();
        actividadesPoas = ejbRegistroActividades.mostrarActividadesPoasTotalArea(controladorEmpleado.getProcesopoa().getArea(), ejercicioFiscal).stream().filter(t ->t.getBandera().equals("y")).collect(Collectors.toList());
    }


    public void imprimirValores() {
    }
    
    public void createMixedModel() {
        mixedModel = new BarChartModel();
        valores = new ArrayList<>();
        valores = promedios();
        
        ChartData data = new ChartData();
        BarChartDataSet sinpe = new BarChartDataSet();
        sinpe.setData(valores.get(0));
        sinpe.setLabel("Cumplimiento sin penalización");
        sinpe.setBorderColor("rgb(255, 99, 132)");
        sinpe.setBackgroundColor("rgba(255, 99, 132, 0.2)");
        
        BarChartDataSet conpe = new BarChartDataSet();
        conpe.setData(valores.get(1));
        conpe.setLabel("Cumplimiento con penalización");
        conpe.setBorderColor("rgb(255, 159, 64)");
        conpe.setBackgroundColor("rgba(255, 159, 64, 0.2)");
        
        BarChartDataSet noeva = new BarChartDataSet();
        noeva.setData(valores.get(2));
        noeva.setLabel("No Evaluadas");
        noeva.setBorderColor("rgb(153, 102, 255)");
        noeva.setBackgroundColor("rgba(153, 102, 255, 0.2)");

        LineChartDataSet dataSet2 = new LineChartDataSet();
        List<Number> values2 = new ArrayList<>();
        values2.add(50);
        values2.add(30);
        values2.add(60);
        values2.add(70);
        dataSet2.setData(values2);
        dataSet2.setLabel("Cumplimiento Acumulado");
        dataSet2.setFill(false);
        dataSet2.setBorderColor("rgb(54, 162, 235)");

        data.addChartDataSet(conpe);
        data.addChartDataSet(sinpe);
        data.addChartDataSet(noeva);
        data.addChartDataSet(dataSet2);

        
        
        data.setLabels(titulos());

        mixedModel.setData(data);

        //Options
        BarChartOptions options = new BarChartOptions();
        CartesianScales cScales = new CartesianScales();
        CartesianLinearAxes linearAxes = new CartesianLinearAxes();
        linearAxes.setOffset(true);
        CartesianLinearTicks ticks = new CartesianLinearTicks();
        ticks.setBeginAtZero(true);
        linearAxes.setTicks(ticks);

        cScales.addYAxesData(linearAxes);
        options.setScales(cScales);
        mixedModel.setOptions(options);
    }
    
    public List<String> titulos() {
        List<String> labels = new ArrayList<>();
        for (int i = 0; i <=numeroMes; i++) {
            switch(i){
            case 0: labels.add("Enero"); break;
            case 1: labels.add("Febrero"); break;
            case 2: labels.add("Marzo"); break;
            case 3: labels.add("Abril"); break;
            case 4: labels.add("Mayo"); break;
            case 5: labels.add("Junio"); break;
            case 6: labels.add("Julio"); break;
            case 7: labels.add("Agosto"); break;
            case 8: labels.add("Septiembre"); break;
            case 9: labels.add("Octubre"); break;
            case 10: labels.add("Noviembre"); break;
            case 11: labels.add("Diciembre"); break;
            }
        }
        return labels;
    }
    
    public List<List<Number>> promedios() {
        List<List<Number>> valores = new ArrayList<>();
        List<Number> sinPenalizacion = new ArrayList<>();
        List<Number> conPenalizacion = new ArrayList<>();
        List<Number> noEvaluadas = new ArrayList<>();
        for (int i = 0; i <= numeroMes; i++) {
            // -------------------------------------------------------------------------------------------
            actividadesProgramadas = 0D;
            actividadesNoEvaluadas = 0D;
            promedio = 0D;
            actividadesSP = 0D;
            actividadesP = 0D;
            switch (i) {
                case 0:
                    actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPEnero() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPEnero() != 0 && t.getNAEnero() != 0) {
                            promedio = (t.getNAEnero() * 100D) / t.getNPEnero();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPEnero() == 0 && t.getNAEnero() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPEnero()!= 0 && t.getNAEnero() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                    sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
                break;
            case 1:actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPFebrero() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPFebrero() != 0 && t.getNAFebrero() != 0) {
                            promedio = (t.getNAFebrero() * 100D) / t.getNPFebrero();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPFebrero() == 0 && t.getNAFebrero() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPFebrero() != 0 && t.getNAFebrero() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 2: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPMarzo() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPMarzo() != 0 && t.getNAMarzo() != 0) {
                            promedio = (t.getNAMarzo() * 100D) / t.getNPMarzo();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPMarzo() == 0 && t.getNAMarzo() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPMarzo() != 0 && t.getNAMarzo() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 3:actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPAbril() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPAbril()!= 0 && t.getNAAbril() != 0) {
                            promedio = (t.getNAAbril() * 100D) / t.getNPAbril();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPAbril() == 0 && t.getNAAbril() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPAbril() != 0 && t.getNAAbril() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 4: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPMayo() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPMayo() != 0 && t.getNAMayo() != 0) {
                            promedio = (t.getNAMayo() * 100D) / t.getNPMayo();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPMayo() == 0 && t.getNAMayo() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPMayo() != 0 && t.getNAMayo() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 5: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPJunio() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPJunio() != 0 && t.getNAJunio() != 0) {
                            promedio = (t.getNAJunio() * 100D) / t.getNPJunio();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPJunio() == 0 && t.getNAJunio() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPJunio() != 0 && t.getNAJunio() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 6: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPJulio() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPJulio() != 0 && t.getNAJulio() != 0) {
                            promedio = (t.getNAJulio() * 100D) / t.getNPJulio();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPJulio() == 0 && t.getNAJulio() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPJulio() != 0 && t.getNAJulio() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 7: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPAgosto() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPAgosto() != 0 && t.getNAAgosto() != 0) {
                            promedio = (t.getNAAgosto() * 100D) / t.getNPAgosto();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPAgosto() == 0 && t.getNAAgosto() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPAgosto() != 0 && t.getNAAgosto() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 8: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPSeptiembre() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPSeptiembre() != 0 && t.getNASeptiembre() != 0) {
                            promedio = (t.getNASeptiembre() * 100D) / t.getNPSeptiembre();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPSeptiembre() == 0 && t.getNASeptiembre() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPSeptiembre() != 0 && t.getNASeptiembre() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 9: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPOctubre() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPOctubre() != 0 && t.getNAOctubre() != 0) {
                            promedio = (t.getNAOctubre() * 100D) / t.getNPOctubre();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPOctubre() == 0 && t.getNAOctubre() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPOctubre() != 0 && t.getNAOctubre() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 10: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPNoviembre() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPNoviembre() != 0 && t.getNANoviembre() != 0) {
                            promedio = (t.getNANoviembre() * 100D) / t.getNPNoviembre();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPNoviembre() == 0 && t.getNANoviembre() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPNoviembre() != 0 && t.getNANoviembre() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            case 11: actividadesPoas.forEach((t) -> {
                        promedio = 0D;
                        if (t.getNPDiciembre() != 0) {
                            actividadesProgramadas = actividadesProgramadas + 1D;
                        }
                        if (t.getNPDiciembre() != 0 && t.getNADiciembre() != 0) {
                            promedio = (t.getNADiciembre() * 100D) / t.getNPDiciembre();
                            if (promedio <= 115) {
                                actividadesSP = actividadesSP + 1D;
                            }else{
                                actividadesP = actividadesP + 1D;
                            }
                        }else if(t.getNPDiciembre() == 0 && t.getNADiciembre() != 0){
                            actividadesP = actividadesP + 1D;
                        } else if (t.getNPDiciembre() != 0 && t.getNADiciembre() == 0) {
                            actividadesNoEvaluadas = actividadesNoEvaluadas + 1D;
                        }
                    });
                     sinPenalizacion.add(((actividadesSP*100D)/actividadesProgramadas));
                    conPenalizacion.add(((actividadesP*100D)/actividadesProgramadas));
                    noEvaluadas.add(((actividadesNoEvaluadas*100D)/actividadesProgramadas));
            }
            System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.CuadroMandoIntegralConsulta.promedios()" + i);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.CuadroMandoIntegralConsulta.promedios()" + actividadesProgramadas);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.CuadroMandoIntegralConsulta.promedios(Sp)" + actividadesSP);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.CuadroMandoIntegralConsulta.promedios(Cp)" + actividadesP);
            System.out.println("mx.edu.utxj.pye.sgi.controladores.cmi.CuadroMandoIntegralConsulta.promedios(Ne)" + actividadesNoEvaluadas);
        }
        valores.add(sinPenalizacion);
        valores.add(conPenalizacion);
        valores.add(noEvaluadas);
        return valores;
    }

}
