package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.io.Serializable;

@RequiredArgsConstructor @ToString
public class DtoCargaAcademica implements Serializable {
    @Getter @Setter @NonNull CargaAcademica cargaAcademica;
    @Getter @Setter @NonNull PeriodosEscolares periodo;
    @Getter @Setter @NonNull PersonalActivo docente;
    @Getter @Setter @NonNull Grupo grupo;
    @Getter @Setter @NonNull Materia materia;
    @Getter @Setter @NonNull AreasUniversidad programa;
    @Getter @Setter @NonNull PlanEstudio planEstudio;
    @Getter @Setter @NonNull PlanEstudioMateria planEstudioMateria;
}
