package mx.edu.utxj.pye.sgi.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

@EqualsAndHashCode(of = {"periodoEscolar"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaEvaluaciones {
    @Getter @Setter @NonNull private PeriodosEscolares periodoEscolar;
    @Getter @Setter private List<Evaluaciones360Resultados> evaluacion360Resultado;
    @Getter @Setter private DesempenioEvaluacionResultados desempenioEvaluacionResultado;
    @Getter @Setter private Double promedio360;
    @Getter @Setter private Double promedioDesepenio;
}
