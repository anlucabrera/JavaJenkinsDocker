package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;

import java.io.Serializable;
import java.util.List;

/**
 * Representa a un Estudiante activo
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "persona")
public class DtoEstudiante implements Serializable {
    @Getter @Setter @NonNull private Persona persona;
    @Getter @Setter @NonNull private Aspirante aspirante;
    @Getter @Setter @NonNull private List<DtoInscripcion> inscripciones;
    @Getter @Setter @NonNull private DtoInscripcion inscripcionActiva;
}
