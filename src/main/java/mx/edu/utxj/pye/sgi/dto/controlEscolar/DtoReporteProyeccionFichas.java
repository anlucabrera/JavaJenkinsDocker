package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoReporteProyeccionFichas {
    @Getter  @Setter private  String nombre;
    @Getter @Setter private  DtoReporteGeneralFichas registradas;
    @Getter @Setter private  DtoReporteGeneralFichas proyectadas;
}
