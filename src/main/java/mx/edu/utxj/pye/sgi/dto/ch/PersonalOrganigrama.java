package mx.edu.utxj.pye.sgi.dto.ch;

import lombok.*;

@EqualsAndHashCode(of = "clave") @RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class PersonalOrganigrama {    
    @Getter @Setter private Integer id;
    @Getter @Setter private Integer clave;
    @Getter @Setter private Integer padre;
    @Getter @Setter private String titulo;
    @Getter @Setter private String descripcion;
    @Getter @Setter private String color;
    @Getter @Setter private Integer tipo;
    
}
