package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.io.Serializable;

/**
 * Dto para el concentrado de registro de fichas de admisión por plan educativo
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoReporteFichaAdmision  implements Serializable {

    @Getter @Setter private AreasUniversidad pe;
    @Getter @Setter private long  totalRegistroSemanal,totalRegistroSabatino,totalRegistroSemanalValido,totalRegistroSabatinoValido,totalRegistros,totalRegistrosValidados;

}
