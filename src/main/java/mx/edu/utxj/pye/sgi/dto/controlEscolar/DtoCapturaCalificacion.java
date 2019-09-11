package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Representa la captura de calificacion de un estudiante mostrado en pantalla
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCapturaCalificacion implements Serializable {
    @Getter @Setter @NonNull private DtoEstudiante dtoEstudiante;
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private DtoUnidadConfiguracion dtoUnidadConfiguracion;
    @Getter private DtoCasoCritico dtoCasoCritico;
    @Getter @Setter @NonNull private List<Captura> capturas;
    @Getter @Setter private BigDecimal promedio = BigDecimal.ZERO;
    @Getter @Setter private Boolean tieneCasoCritico = false;

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Captura{
        @Getter @Setter @NonNull private DtoUnidadConfiguracion.Detalle detalle;
        @Getter @Setter @NonNull private Calificacion calificacion;
    }

    public static Captura crearCaptura(DtoUnidadConfiguracion.Detalle detalle, Calificacion calificacion){
        return new Captura(detalle, calificacion);
    }

    public void setDtoCasoCritico(DtoCasoCritico dtoCasoCritico) {
        this.dtoCasoCritico = dtoCasoCritico;
        if(dtoCasoCritico == null) {
            tieneCasoCritico = false;
            return;
        }
        tieneCasoCritico = !dtoCasoCritico.getEstado().equals(CasoCriticoEstado.SIN_REGISTRO);
    }
}
