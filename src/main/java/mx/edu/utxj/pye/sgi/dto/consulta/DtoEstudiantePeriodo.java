package mx.edu.utxj.pye.sgi.dto.consulta;

import lombok.*;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.CiclosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;

import java.io.Serializable;

@RequiredArgsConstructor @EqualsAndHashCode(of = "matriculaPeriodosEscolares") @ToString
public class DtoEstudiantePeriodo implements Serializable {
    @Getter @Setter @NonNull private MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    @Getter @Setter @NonNull private AreasUniversidad programa;
    @Getter @Setter @NonNull private PeriodosEscolares periodo;
    @Getter @Setter @NonNull private CiclosEscolares ciclo;
    @Getter @Setter @NonNull private UsuarioTipo usuarioTipo;
//    @Getter @Setter private DtoEstudiante dtoEstudiante;
}
