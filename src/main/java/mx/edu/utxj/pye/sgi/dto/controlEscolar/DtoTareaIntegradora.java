package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradora;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TareaIntegradoraPromedioPK;

import java.io.Serializable;

@RequiredArgsConstructor @EqualsAndHashCode
public class DtoTareaIntegradora implements Serializable {
    @Getter @Setter @NonNull private TareaIntegradora tareaIntegradora;
    @Getter @Setter private TareaIntegradoraPromedioPK tareaIntegradoraPromedioPK;

}
