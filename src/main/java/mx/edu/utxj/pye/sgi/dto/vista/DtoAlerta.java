package mx.edu.utxj.pye.sgi.dto.vista;

import lombok.*;
import mx.edu.utxj.pye.sgi.enums.AlertaTipo;

import java.io.Serializable;

@RequiredArgsConstructor @ToString
public class DtoAlerta  implements Serializable {
    @Getter @Setter @NonNull private String mensaje;
    @Getter @Setter @NonNull private AlertaTipo tipo;
}
