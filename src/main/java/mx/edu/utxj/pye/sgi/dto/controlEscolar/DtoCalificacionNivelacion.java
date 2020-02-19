package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Indicador;

import java.io.Serializable;

@RequiredArgsConstructor @EqualsAndHashCode
public class DtoCalificacionNivelacion implements Serializable {
    @Getter @Setter @NonNull private CalificacionNivelacion calificacionNivelacion;
    @Getter @Setter @NonNull private Indicador indicador;
}
