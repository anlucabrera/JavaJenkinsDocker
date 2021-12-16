package mx.edu.utxj.pye.sgi.dto;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;

@RequiredArgsConstructor @ToString
@EqualsAndHashCode
public class DtoEvaluacion360 {
    @Getter @Setter Personal evaluador;
    @Getter @Setter Personal evaluado;
    @Getter @Setter PersonalCategorias categoria;
    @Getter @Setter String tipo;
}
