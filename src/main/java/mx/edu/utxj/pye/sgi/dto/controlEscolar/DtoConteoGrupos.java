package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.io.Serializable;

/**
 * Representa a una inscripcion con sus relaciones
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoConteoGrupos implements Serializable {
    @Getter @Setter @NonNull String siglas;
    @Getter @Setter @NonNull String nombre;
    @Getter @Setter @NonNull Integer totalAspirantes;
    @Getter @Setter @NonNull Integer totalGrupos;
    @Getter @Setter @NonNull Integer gruposExistentes;
    @Getter @Setter @NonNull String sistemaOpcion;

}
