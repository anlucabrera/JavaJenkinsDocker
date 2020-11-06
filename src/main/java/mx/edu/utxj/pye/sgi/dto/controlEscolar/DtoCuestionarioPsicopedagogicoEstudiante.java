package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CuestionarioPsicopedagogicoResultados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCuestionarioPsicopedagogicoEstudiante {
    @Getter @Setter @NonNull Estudiante estudianteA;
    @Getter @Setter @NonNull CuestionarioPsicopedagogicoResultados cuestionario;
    @Getter @Setter @NonNull String personalEvaluador;
    @Getter @Setter @NonNull AreasUniversidad pe;
    @Getter @Setter @NonNull String tutor;
    @Getter @Setter @NonNull Personal director;
    @Getter @Setter @NonNull Personal tutorP;
    @Getter @Setter @NonNull boolean ingresoAlSistema;
}
