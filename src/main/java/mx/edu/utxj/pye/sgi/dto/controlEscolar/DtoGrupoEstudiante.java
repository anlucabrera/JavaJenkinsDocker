package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * Representa a un grupo d eestudiantes que corresponden a una carga academica registrada
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoGrupoEstudiante implements Serializable {
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private List<DtoCapturaCalificacion> estudiantes;
}
