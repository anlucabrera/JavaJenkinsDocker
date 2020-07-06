package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.dto.consulta.DtoSatisfaccionServiciosEncuesta;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.Operacion;

import java.io.Serializable;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCitaAspirante implements Serializable {
    @Getter @Setter @NonNull Aspirante aspirante;
    @Getter @Setter @NonNull DatosAcademicos datosAcademicos;
    @Getter @Setter @NonNull AreasUniversidad pePrimeraO,peSegundaO;
    @Getter @Setter @NonNull TramitesEscolares tramite;
    @Getter @Setter @NonNull CitasAspirantes citasAspirantes;
    @Getter @Setter @NonNull CitasAspirantesPK citasAspirantesPK;
    @Getter @Setter @NonNull Operacion operacion;
    @Getter @Setter @NonNull Boolean  tieneCita;
}
