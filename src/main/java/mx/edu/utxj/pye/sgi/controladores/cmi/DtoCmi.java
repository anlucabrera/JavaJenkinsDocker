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
        @Getter        @Setter        private List<Grafica> cumplimientoMensualSp;
        @Getter        @Setter        private List<Grafica> cumplimientoMensualCp;
        @Getter        @Setter        private List<Grafica> cumplimientoAcumuladoSp;
        @Getter        @Setter        private List<Grafica> cumplimientoAcumuladoCp;
        @Getter        @Setter        private MeterGaugeChartModel gaugeChartModel;
        @Getter        @Setter        private Boolean renderizar;

        public ResultadosCMI(String tituloG, Integer programadas, Integer alcanzadas, Integer noAlcanzadas, Double avanceCP, Double avanceSP, String extender, List<ConcentradoDatos> resumen, List<Grafica> cumplimientoMensualSp, List<Grafica> cumplimientoMensualCp, List<Grafica> cumplimientoAcumuladoSp, List<Grafica> cumplimientoAcumuladoCp, MeterGaugeChartModel gaugeChartModel, Boolean renderizar) {
            this.tituloG = tituloG;
            this.programadas = programadas;
            this.alcanzadas = alcanzadas;
            this.noAlcanzadas = noAlcanzadas;
            this.avanceCP = avanceCP;
            this.avanceSP = avanceSP;
            this.extender = extender;
            this.resumen = resumen;
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
        @Getter        @Setter        private Integer programados;
        @Getter        @Setter        private Integer alcanzadas;
        @Getter        @Setter        private Integer noAlcanzadas;
        @Getter        @Setter        private Double promedioSp;
        @Getter        @Setter        private Double promedioCp;
        @Getter        @Setter        private String samaforoSp;
        @Getter        @Setter        private String semaforoCp;
        @Getter        @Setter        private Integer acumuladoP;
        @Getter        @Setter        private Integer acumuladoA;
        @Getter        @Setter        private Boolean reporteC;

        public ConcentradoDatos(String mes, Integer programados, Integer alcanzadas, Integer noAlcanzadas, Double promedioSp, Double promedioCp, String samaforoSp, String semaforoCp, Integer acumuladoP, Integer acumuladoA, Boolean reporteC) {
            this.mes = mes;
            this.programados = programados;
            this.alcanzadas = alcanzadas;
            this.noAlcanzadas = noAlcanzadas;
            this.promedioSp = promedioSp;
            this.promedioCp = promedioCp;
            this.samaforoSp = samaforoSp;
            this.semaforoCp = semaforoCp;
            this.acumuladoP = acumuladoP;
            this.acumuladoA = acumuladoA;
            this.reporteC = reporteC;
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class ReporteCuatrimestralAreas {

        @Getter        @Setter        private String area;
        @Getter        @Setter        private Integer eje1P;
        @Getter        @Setter        private Integer eje1A;
        @Getter        @Setter        private Double eje1PC;
        @Getter        @Setter        private String renderEje1;
        @Getter        @Setter        private Integer eje2P;
        @Getter        @Setter        private Integer eje2A;
        @Getter        @Setter        private Double eje2PC;
        @Getter        @Setter        private String renderEje2;
        @Getter        @Setter        private Integer eje3P;
        @Getter        @Setter        private Integer eje3A;
        @Getter        @Setter        private Double eje3PC;
        @Getter        @Setter        private String renderEje3;
        @Getter        @Setter        private Integer eje4P;
        @Getter        @Setter        private Integer eje4A;
        @Getter        @Setter        private Double eje4PC;
        @Getter        @Setter        private String renderEje4;
        @Getter        @Setter        private Double porcentajeFSP;
        @Getter        @Setter        private Double porcentajeFCP;

        public ReporteCuatrimestralAreas(String area, Integer eje1P, Integer eje1A, Double eje1PC, String renderEje1, Integer eje2P, Integer eje2A, Double eje2PC, String renderEje2, Integer eje3P, Integer eje3A, Double eje3PC, String renderEje3, Integer eje4P, Integer eje4A, Double eje4PC, String renderEje4, Double porcentajeFSP, Double porcentajeFCP) {
            this.area = area;
            this.eje1P = eje1P;
            this.eje1A = eje1A;
            this.eje1PC = eje1PC;
            this.renderEje1 = renderEje1;
            this.eje2P = eje2P;
            this.eje2A = eje2A;
            this.eje2PC = eje2PC;
            this.renderEje2 = renderEje2;
            this.eje3P = eje3P;
            this.eje3A = eje3A;
            this.eje3PC = eje3PC;
            this.renderEje3 = renderEje3;
            this.eje4P = eje4P;
            this.eje4A = eje4A;
            this.eje4PC = eje4PC;
            this.renderEje4 = renderEje4;
            this.porcentajeFSP = porcentajeFSP;
            this.porcentajeFCP = porcentajeFCP;
        }
    }
}