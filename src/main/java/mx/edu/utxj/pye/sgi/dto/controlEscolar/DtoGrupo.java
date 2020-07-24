package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor @RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoGrupo implements Serializable {
    @Getter @Setter @NonNull Grupo grupo;
    @Getter @Setter @NonNull AreasUniversidad pe;
    @Getter @Setter  List<Estudiante> estudiantes;
    @Getter @Setter @NonNull Boolean lleno;
    @Getter @Setter @NonNull int capMax,capDispo,totalGrupo;
    @Getter @Setter @NonNull Personal tutor;
    @Getter @Setter @NonNull String nombreTutor;
}
