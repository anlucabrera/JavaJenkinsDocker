package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.pye2.*;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoDomicilio {
    @Getter @Setter private Pais pais;
    @Getter @Setter private Estado estado;
    @Getter @Setter private Municipio municipio;
    @Getter @Setter private Localidad localidad;
    @Getter @Setter private Asentamiento asentamiento;
}
