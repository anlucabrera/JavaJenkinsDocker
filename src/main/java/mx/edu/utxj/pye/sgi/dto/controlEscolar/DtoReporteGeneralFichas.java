package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoReporteGeneralFichas implements Serializable {
    @Getter @Setter private Integer totalSemanal, totalSabatino, total;
    @Getter @Setter private String tipo;

}
