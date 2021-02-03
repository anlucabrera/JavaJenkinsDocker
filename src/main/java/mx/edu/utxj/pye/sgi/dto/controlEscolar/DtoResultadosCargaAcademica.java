package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Informeplaneacioncuatrimestraldocenteprint;

/**
 * Representa a una carga academica registrada a un docente grupo y materia
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoResultadosCargaAcademica implements Serializable {
    @Getter @Setter @NonNull Informeplaneacioncuatrimestraldocenteprint informe;
    @Getter @Setter @NonNull Integer estudiantesActivos;
    @Getter @Setter @NonNull Integer estudiantesAlcazanMeta;
    @Getter @Setter @NonNull Double metaAlacanzadaIndicador;
    @Getter @Setter @NonNull Double metaAlacanzadaunidad;
    @Getter @Setter @NonNull String accionesMejora;
    @Getter @Setter @NonNull String semaforo;
    
}
