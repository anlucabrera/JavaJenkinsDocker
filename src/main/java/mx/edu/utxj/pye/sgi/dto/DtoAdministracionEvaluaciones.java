package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.io.Serializable;

@RequiredArgsConstructor
@ToString
@EqualsAndHashCode

public class DtoAdministracionEvaluaciones implements Serializable {

    @Getter
    @Setter
    @NonNull Evaluaciones evaluacion;
    @Getter
    @Setter
    @NonNull PeriodosEscolares periodo;
    @Getter
    @Setter
    @NonNull Boolean activa;
}
