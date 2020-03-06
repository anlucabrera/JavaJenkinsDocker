package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.io.Serializable;
import java.util.List;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoAreaAcademica implements Serializable {
    @Getter @Setter @NonNull private AreasUniversidad areaAcademica;
    @Getter @Setter @NonNull private Integer periodoDeConsulta;
    @Getter @Setter private List<AreasUniversidad> programas;
}
