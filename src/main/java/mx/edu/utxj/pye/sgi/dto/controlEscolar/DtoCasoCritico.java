package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;

import java.io.Serializable;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCasoCritico implements Serializable {
    @Getter @Setter @NonNull private CasoCritico casoCritico;
    @Getter @NonNull private CasoCriticoTipo tipo;
    @Getter @Setter @NonNull private CasoCriticoEstado estado;
    @Getter @Setter @NonNull private DtoEstudiante dtoEstudiante;
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private DtoUnidadConfiguracion dtoUnidadConfiguracion;

    public void setTipo(CasoCriticoTipo tipo) {
        this.tipo = tipo;
        casoCritico.setTipo(tipo.getLabel());
    }
}
