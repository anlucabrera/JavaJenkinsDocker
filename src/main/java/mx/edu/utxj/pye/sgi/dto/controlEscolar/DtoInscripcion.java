package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.io.Serializable;
import java.util.List;

/**
 * Representa a una inscripcion con sus relaciones
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoInscripcion implements Serializable {
    @Getter @Setter @NonNull Estudiante inscripcion;
    @Getter @Setter @NonNull Grupo grupo;
    @Getter @Setter @NonNull PeriodosEscolares periodo;
    @Getter @Setter @NonNull Generaciones generacion;
    @Getter @Setter @NonNull Boolean activo;
    @Getter @Setter List<CasoCritico> casosCriticosAbiertos;
}
