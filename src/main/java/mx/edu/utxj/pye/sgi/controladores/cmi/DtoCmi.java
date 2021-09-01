package mx.edu.utxj.pye.sgi.controladores.cmi;

import java.util.List;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import org.primefaces.model.chart.MeterGaugeChartModel;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCmi {
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Cmi {
        @Getter        @Setter        @NonNull       EjesRegistro EjeR; 
        @Getter        @Setter        @NonNull       List<ActividadesEs> ActividadesE;    
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ActividadesEs {
        @Getter        @Setter        @NonNull        ActividadesPoa Pricipal;
        @Getter        @Setter        @NonNull       List<ActividadesPoa> hijas;  
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ResultadosCMI {

        @Getter        @Setter        private String tituloG;
        @Getter        @Setter        private Integer programadas;  
        @Getter        @Setter        private Integer alcanzadas;  
        @Getter        @Setter        private Integer noAlcanzadas;
        @Getter        @Setter        private Double avanceCP;
        @Getter        @Setter        private Double avanceSP;
        @Getter        @Setter        private String extender;
        @Getter        @Setter        private List<ConcentradoDatos> resumen;
        @Getter        @Setter        private List<ConcentradoDatos> resumenAcumulado;
        @Getter        @Setter        private List<Grafica> cumplimientoMensualSp;
        @Getter        @Setter        private List<Grafica> cumplimientoMensualCp;
        @Getter        @Setter        private List<Grafica> cumplimientoAcumuladoSp;
        @Getter        @Setter        private List<Grafica> cumplimientoAcumuladoCp;
        @Getter        @Setter        private MeterGaugeChartModel gaugeChartModel;
        @Getter        @Setter        private Boolean renderizar;

        public ResultadosCMI(String tituloG, Integer programadas, Integer alcanzadas, Integer noAlcanzadas, Double avanceCP, Double avanceSP, String extender, List<ConcentradoDatos> resumen, List<ConcentradoDatos> resumenAcumulado, List<Grafica> cumplimientoMensualSp, List<Grafica> cumplimientoMensualCp, List<Grafica> cumplimientoAcumuladoSp, List<Grafica> cumplimientoAcumuladoCp, MeterGaugeChartModel gaugeChartModel, Boolean renderizar) {
            this.tituloG = tituloG;
            this.programadas = programadas;
            this.alcanzadas = alcanzadas;
            this.noAlcanzadas = noAlcanzadas;
            this.avanceCP = avanceCP;
            this.avanceSP = avanceSP;
            this.extender = extender;
            this.resumen = resumen;
            this.resumenAcumulado = resumenAcumulado;
            this.cumplimientoMensualSp = cumplimientoMensualSp;
            this.cumplimientoMensualCp = cumplimientoMensualCp;
            this.cumplimientoAcumuladoSp = cumplimientoAcumuladoSp;
            this.cumplimientoAcumuladoCp = cumplimientoAcumuladoCp;
            this.gaugeChartModel = gaugeChartModel;
            this.renderizar = renderizar;
        }
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Grafica {

        @Getter        @Setter        private String mes;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private Double avanceOF;

        public Grafica(String mes, Double avance, Double avanceOF) {
            this.mes = mes;
            this.avance = avance;
            this.avanceOF = avanceOF;
        }
    }
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ConcentradoDatos {

        @Getter        @Setter        private String mes;
        @Getter        @Setter        private Double programados;
        @Getter        @Setter        private Double alcanzadas;
        @Getter        @Setter        private Double noAlcanzadas;
        @Getter        @Setter        private Double promedioSp;
        @Getter        @Setter        private Double promedioCp;
        @Getter        @Setter        private String samaforoSp;
        @Getter        @Setter        private String semaforoCp;
        @Getter        @Setter        private Boolean reporteC;

        public ConcentradoDatos(String mes, Double programados, Double alcanzadas, Double noAlcanzadas, Double promedioSp, Double promedioCp, String samaforoSp, String semaforoCp, Boolean reporteC) {
            this.mes = mes;
            this.programados = programados;
            this.alcanzadas = alcanzadas;
            this.noAlcanzadas = noAlcanzadas;
            this.promedioSp = promedioSp;
            this.promedioCp = promedioCp;
            this.samaforoSp = samaforoSp;
            this.semaforoCp = semaforoCp;
            this.reporteC = reporteC;
        }
    }
}