package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoReporteEvaluaciones {
    @Getter @Setter @NonNull private Evaluaciones evaluacion;
    @Getter @Setter @NonNull private PeriodosEscolares periodoEscolar;

}
