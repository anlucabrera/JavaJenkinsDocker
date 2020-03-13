package mx.edu.utxj.pye.sgi.dto.consulta;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Categorias;

import java.io.Serializable;

@RequiredArgsConstructor @EqualsAndHashCode
public class DtoAreaUniversidadCategoria implements Serializable {
    @Getter @Setter @NonNull private AreasUniversidad area;
    @Getter @Setter @NonNull private Categorias categoria;
}
