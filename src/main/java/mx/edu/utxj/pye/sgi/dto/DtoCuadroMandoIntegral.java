package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;

import java.math.BigDecimal;
import java.util.List;
import org.primefaces.model.chart.MeterGaugeChartModel;

public class DtoCuadroMandoIntegral {

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public static class ResultadosCMI {
        @Getter        @Setter        private String ejesRegistro;
        @Getter        @Setter        private Integer programadas;
        @Getter        @Setter        private Integer realizadas;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private Double avanceSP;
        @Getter        @Setter        private String extender;
        @Getter        @Setter        private List<Grafica> avanceGrafSP;
        @Getter        @Setter        private List<Grafica> avanceGrafCP;
        @Getter        @Setter        private MeterGaugeChartModel gaugeChartModel;
        @Getter        @Setter        private Boolean renderizar;
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public class Grafica {
        @Getter        @Setter        private String mes;
        @Getter        @Setter        private Double avance;
        @Getter        @Setter        private Double avanceOF;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class MapUnidadesTematicas{
        @Getter @Setter @NonNull Integer noUnidad;
        @Getter @Setter @NonNull Integer totalPorUnidad;
    }
}

