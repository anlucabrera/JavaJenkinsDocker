package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;

import java.io.Serializable;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@AllArgsConstructor @ToString
public class DtoMateria implements Serializable {
    @Getter @Setter @NonNull private Materia materia;
    @Getter @Setter private DtoCargaAcademica dtoCargaAcademica;
}
