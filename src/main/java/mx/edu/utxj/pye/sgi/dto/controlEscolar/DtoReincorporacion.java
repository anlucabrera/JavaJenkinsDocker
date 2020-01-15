package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosFamiliares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutorFamiliar;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoReincorporacion {
    @Getter @Setter private Persona persona;
    @Getter @Setter private Aspirante aspirante;
    @Getter @Setter private DatosMedicos medicos;
    @Getter @Setter private DatosFamiliares familiares;
    @Getter @Setter private Domicilio domicilio;
    @Getter @Setter private MedioComunicacion comunicacion;
    @Getter @Setter private DatosAcademicos academicos;
    @Getter @Setter private EncuestaAspirante encuesta;
    @Getter @Setter private TutorFamiliar tutor;
}
