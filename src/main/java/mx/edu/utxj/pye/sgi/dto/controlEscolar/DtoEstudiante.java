package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Inscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;

import java.io.Serializable;
import java.util.Map;

public class DtoEstudiante implements Serializable {
    @Getter @Setter @NonNull private Persona persona;
    @Getter @Setter @NonNull private Aspirante aspirante;
    @Getter @Setter @NonNull private Map<Inscripcion, Grupo> inscripciones;
}
