package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.math.BigDecimal;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoMatariaPromedio {
    @Getter @Setter @NonNull private  DtoCargaAcademica cargaAcademica;
    @Getter @Setter @NonNull private BigDecimal  promedio,promedioOrdinario, nivelacion;
    public DtoMatariaPromedio() {
    }
}
