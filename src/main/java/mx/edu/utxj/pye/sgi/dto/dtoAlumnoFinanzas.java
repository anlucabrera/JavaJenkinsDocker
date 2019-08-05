package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Personafinanzas;

@EqualsAndHashCode(of = {"matricula"}) @RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class dtoAlumnoFinanzas {
    @Getter @Setter Integer matricula;
    @Getter @Setter Integer periodo;
    @Getter @Setter String siglas;
    @Getter @Setter String curp;
    @Getter @Setter Personafinanzas personafinanzas;
    @Getter  @Setter  Estudiante estudianteCE;

}
