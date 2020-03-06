package mx.edu.utxj.pye.sgi.dto.consulta;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.*;
import mx.edu.utxj.pye.sgi.controlador.controlEscolar.FichaAdmision;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.SatisfaccionHistorico;
import mx.edu.utxj.pye.sgi.enums.SatisfaccionServiciosApartado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RequiredArgsConstructor @EqualsAndHashCode(of = "evaluacion") @ToString
public class DtoSatisfaccionServiciosEncuesta implements Serializable {
    @Getter @Setter @NonNull private Evaluaciones evaluacion;
    @Getter @Setter @NonNull private PeriodosEscolares periodosEscolares;
    @Getter @Setter @NonNull private CiclosEscolares ciclosEscolares;
    @Getter @Setter @NonNull private DtoSatisfaccionServiciosCuestionario cuestionario;
    @Getter private List<DtoSatisfaccionServiciosEstudiante> satisfaccionServiciosEstudiantes;
    @Getter @Setter private List<AreasUniversidad> programas;
    @Getter @Setter private Map<Pregunta, FilaInstitucional> preguntaFilaInstitucionalMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<FilaProgramaPK, FilaPrograma> preguntaFilaProgramaMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<FilaProgramaPK, FilaPrograma> preguntaFilaAreaMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<Apartado, FilaInstitucionalApartado> apartadoFilaInstitucionalMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<FilaProgramaApartadoPK, FilaProgramaApartado> apartadoFilaProgramaMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<FilaProgramaApartadoPK, FilaProgramaApartado> apartadoFilaAreaMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<Apartado, FilaProgramaApartado> apartadoprogramaMap = new ConcurrentHashMap<>();
    @Getter @Setter private FilaInstitucionalGeneral filaInstitucionalGeneral;
    @Getter @Setter private Map<AreasUniversidad, FilaProgramaGeneral> filaProgramaGeneralMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<GraficaSerieAreaHistoricoPK, GraficaSerieAreaHistorico> graficaSerieAreaHistoricoMap = new ConcurrentHashMap<>();
    @Getter @Setter private Map<SatisfaccionServiciosApartado, GraficaSerieInstitucionalHistorico> graficaSerieInstitucionalHistoricoMap = new ConcurrentHashMap<>();

    public static Comparator<AreasUniversidad> areasUniversidadComparator;
    public static Comparator<FilaProgramaApartado> filaProgramaApartadoComparator;

    static {
        areasUniversidadComparator = Comparator.comparing(AreasUniversidad::getAreaSuperior).thenComparing(AreasUniversidad::getSiglas);
        Comparator<FilaProgramaApartado> comparing = Comparator.comparing(filaProgramaApartado -> filaProgramaApartado.getPk().getPrograma().getAreaSuperior());
        filaProgramaApartadoComparator = comparing.thenComparing(filaProgramaApartado -> filaProgramaApartado.getPk().getPrograma().getSiglas());
    }

    public void setSatisfaccionServiciosEstudiantes(List<DtoSatisfaccionServiciosEstudiante> satisfaccionServiciosEstudiantes) {
        this.satisfaccionServiciosEstudiantes = satisfaccionServiciosEstudiantes;
    }

    public List<FilaProgramaApartado> getNivelSatisfaccionProgramaPorApartado(Apartado apartado){
        return apartadoFilaProgramaMap.values()
                .stream()
                .filter(filaProgramaApartado -> Objects.equals(filaProgramaApartado.getPk().apartado, apartado))
                .sorted(DtoSatisfaccionServiciosEncuesta.filaProgramaApartadoComparator)
                .collect(Collectors.toList());
    }

    public List<FilaProgramaApartado> getNivelSatisfaccionAreaPorApartado(Apartado apartado){
        return apartadoFilaAreaMap.values()
                .stream()
                .filter(filaProgramaApartado -> Objects.equals(filaProgramaApartado.getPk().apartado, apartado))
                .sorted(DtoSatisfaccionServiciosEncuesta.filaProgramaApartadoComparator)
                .collect(Collectors.toList());
    }

    public List<FilaInstitucional> getNivelSatisfaccionInstitucionalPorApartado(Apartado apartado){
        return preguntaFilaInstitucionalMap.values()
                .stream()
                .filter(filaInstitucional -> filaInstitucional.pregunta.getApartado().doubleValue() == apartado.getId().doubleValue())
                .sorted(Comparator.comparing(filaInstitucional -> filaInstitucional.pregunta.getNumero()))
                .collect(Collectors.toList());
    }

    public List<FilaPrograma> getNivelSatisfaccionAreaPreguntaPorAreaApartado(AreasUniversidad area, Apartado apartado){
        return preguntaFilaAreaMap.values()
                .stream()
                .filter(filaPrograma -> filaPrograma.pk.getPregunta().getApartado().doubleValue() == apartado.getId().doubleValue())
                .filter(filaPrograma -> filaPrograma.pk.getPrograma().getArea().shortValue() == area.getArea().shortValue())
                .sorted(Comparator.comparing(filaPrograma -> filaPrograma.pk.pregunta.getNumero()))
                .collect(Collectors.toList());
    }

    public List<FilaPrograma> getNivelSatisfaccionAreaPreguntaPorPregunta(Pregunta pregunta){
        return preguntaFilaAreaMap.values()
                .stream()
                .filter(filaPrograma -> filaPrograma.pk.getPregunta().getNumero().doubleValue() == pregunta.getNumero().doubleValue())
                .sorted(Comparator.comparing(filaPrograma -> filaPrograma.pk.pregunta.getNumero()))
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class Dato{
        @Getter @Setter @NonNull private AreasUniversidad programa;
        @Getter @Setter @NonNull private Pregunta pregunta;
        @Getter @Setter @NonNull private BigDecimal respuesta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class DatoApartado{
        @Getter @Setter @NonNull private AreasUniversidad programa;
        @Getter @Setter @NonNull private Apartado apartado;
        @Getter @Setter @NonNull private BigDecimal respuesta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class DatoGeneral{
        @Getter @Setter @NonNull private AreasUniversidad programa;
        @Getter @Setter @NonNull private BigDecimal respuesta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class DatoInstitucional{
        @Getter @Setter @NonNull private Pregunta pregunta;
        @Getter @Setter @NonNull private BigDecimal respuesta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class DatoInstitucionalApartado{
        @Getter @Setter @NonNull private Apartado apartado;
        @Getter @Setter @NonNull private BigDecimal respuesta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class Conteo{
        @Getter @Setter @NonNull private Dato pk;
        @Getter @Setter private Long frecuencia;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class ConteoApartado{
        @Getter @Setter @NonNull private DatoApartado pk;
        @Getter @Setter private Long frecuencia;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class ConteoGeneral{
        @Getter @Setter @NonNull private DatoGeneral pk;
        @Getter @Setter private Long frecuencia;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class ConteoInstitucional {
        @Getter @Setter @NonNull private DatoInstitucional pk;
        @Getter @Setter private Long frecuencia;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class ConteoInstitucionalApartado {
        @Getter @Setter @NonNull private DatoInstitucionalApartado pk;
        @Getter @Setter private Long frecuencia;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class ConteoInstitucionalGeneral {
        @Getter @Setter @NonNull private BigDecimal respuesta;
        @Getter @Setter private Long frecuencia;
    }

    public static abstract class Fila {
        @Getter @Setter private Long totalG = 0l;
        @Getter @Setter private Long totalH = 0l;
        @Getter @Setter private Long totalJ = 0l;
        @Getter @Setter private BigDecimal totalK = BigDecimal.ZERO;
        @Getter @Setter private BigDecimal totalBase10 = BigDecimal.ZERO;
        @Getter @Setter private BigDecimal porcentajeSatisfechos = BigDecimal.ZERO;
        @Getter @Setter private BigDecimal porcentajeNoSatisfechos = BigDecimal.ZERO;
    }

    @RequiredArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class FilaInstitucional extends Fila{
        @Getter @Setter @NonNull Pregunta pregunta;
        @Getter @Setter private Map<BigDecimal, ConteoInstitucional> datoInstitucionalMap = new ConcurrentHashMap<>();
    }

    @RequiredArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class FilaPrograma extends Fila{
        @Getter @Setter @NonNull FilaProgramaPK pk;
        @Getter @Setter private Map<BigDecimal, Conteo> datoMap = new ConcurrentHashMap<>();
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class FilaProgramaPK{
        @Getter @Setter @NonNull AreasUniversidad programa;
        @Getter @Setter @NonNull Pregunta pregunta;
    }

    @RequiredArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class FilaInstitucionalApartado extends Fila {
        @Getter @Setter @NonNull Apartado apartado;
        @Getter @Setter private Map<BigDecimal, ConteoInstitucionalApartado> datoInstitucionalMap = new ConcurrentHashMap<>();
    }

    @RequiredArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class FilaProgramaApartado extends Fila {
        @Getter @Setter @NonNull FilaProgramaApartadoPK pk;
        @Getter @Setter private Map<BigDecimal, ConteoApartado> datoMap = new ConcurrentHashMap<>();
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class FilaProgramaApartadoPK {
        @Getter @Setter @NonNull AreasUniversidad programa;
        @Getter @Setter @NonNull Apartado apartado;
    }

    @RequiredArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class FilaInstitucionalGeneral extends Fila {
        @Getter @Setter private Map<BigDecimal, ConteoInstitucionalGeneral> datoInstitucionalMap = new ConcurrentHashMap<>();
    }

    @RequiredArgsConstructor @EqualsAndHashCode(callSuper = false) @ToString
    public static class FilaProgramaGeneral extends Fila {
        @Getter @Setter @NonNull AreasUniversidad programa;
        @Getter @Setter private Map<BigDecimal, ConteoGeneral> datoMap = new ConcurrentHashMap<>();
    }

    @RequiredArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
    public static class GraficaSerieAreaHistorico{
        @Getter @Setter @NonNull GraficaSerieAreaHistoricoPK pk;
        @Getter @Setter List<DtoSatisfaccionHistorico> dtoSatisfaccionHistoricos;
    }

    @RequiredArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
    public static class GraficaSerieInstitucionalHistorico{
        @Getter @Setter @NonNull SatisfaccionServiciosApartado satisfaccionServiciosApartado;
        @Getter @Setter List<DtoSatisfaccionHistoricoInstitucional> dtoSatisfaccionHistoricos;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString
    public static class GraficaSerieAreaHistoricoPK{
        @Getter @Setter @NonNull AreasUniversidad area;
        @Getter @Setter @NonNull SatisfaccionServiciosApartado satisfaccionServiciosApartado;
    }

    @RequiredArgsConstructor @EqualsAndHashCode(of = {"ciclo","area"}) @ToString(of = "satisfaccionHistorico")
    public static class DtoSatisfaccionHistorico{
        @Getter @Setter @NonNull CiclosEscolares ciclo;
        @Getter @Setter @NonNull AreasUniversidad area;
        @Getter @Setter @NonNull SatisfaccionServiciosApartado satisfaccionServiciosApartado;
        @Getter @Setter @NonNull SatisfaccionHistorico satisfaccionHistorico;
    }

    @RequiredArgsConstructor @EqualsAndHashCode @ToString @AllArgsConstructor
    public static class DtoSatisfaccionHistoricoInstitucional{
        @Getter @Setter @NonNull CiclosEscolares ciclo;
        @Getter @Setter @NonNull SatisfaccionServiciosApartado satisfaccionServiciosApartado;
        @Getter @Setter Double satisfaccionNivel;
        @Getter @Setter Double satisfaccionPromedio;
    }
}
