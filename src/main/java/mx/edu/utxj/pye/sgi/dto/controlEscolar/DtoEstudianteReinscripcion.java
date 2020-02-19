package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoEstudianteReinscripcion {
    @Getter @Setter @NonNull private Estudiante estudiante;
    @Getter @Setter @NonNull private AreasUniversidad carrera;
    @Getter @Setter @NonNull private Grupo grupoActual;
    @Getter @Setter @NonNull private Grupo nuevoGrupo;
    @Getter @Setter @NonNull private String nombreTutor;
    @Getter @Setter @NonNull private Boolean aprobo;
    @Getter @Setter @NonNull private Integer materiasReprobadas;
    @Getter @Setter @NonNull private List<DtoMatariaPromedio> promedioMateria;
    @Getter @Setter @NonNull private  Personal trabajadorIncribe;
    public DtoEstudianteReinscripcion() {
    }
}
