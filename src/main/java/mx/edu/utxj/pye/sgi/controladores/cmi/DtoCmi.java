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

        @Getter        @Setter        private String ejesRegistro;
        @Getter        @Setter        private Integer programadas;
        @Getter        @Setter        private Integer realizadas;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private Double avanceSP;
        @Getter        @Setter        private String extender;
        @Getter        @Setter        private List<DtoCmi.Grafica> avanceGrafSP;
        @Getter        @Setter        private List<DtoCmi.Grafica> avanceGrafCP;
        @Getter        @Setter        private MeterGaugeChartModel gaugeChartModel;
        @Getter        @Setter        private Boolean renderizar;

        public ResultadosCMI(String ejesRegistro, Integer programadas, Integer realizadas, Double avance, Double avanceSP, String extender, List<DtoCmi.Grafica> avanceGrafSP, List<DtoCmi.Grafica> avanceGrafCP, MeterGaugeChartModel gaugeChartModel, Boolean renderizar) {
            this.ejesRegistro = ejesRegistro;
            this.programadas = programadas;
            this.realizadas = realizadas;
            this.avance = avance;
            this.avanceSP = avanceSP;
            this.extender = extender;
            this.avanceGrafSP = avanceGrafSP;
            this.avanceGrafCP = avanceGrafCP;
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
}