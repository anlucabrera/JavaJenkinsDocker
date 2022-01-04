package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionEvidenciaInstrumento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateriaComentario;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa la captura de calificacion de un estudiante mostrado en pantalla
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoCapturaCalificacionAlineacion implements Serializable {
    @Getter @Setter @NonNull private DtoEstudiante dtoEstudiante;
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private DtoUnidadConfiguracionAlineacion dtoUnidadConfiguracion;
    @Getter private DtoCasoCriticoAlineacion dtoCasoCritico;
    @Getter private @NonNull Boolean permisoExtInd;
    @Getter private @NonNull Boolean unidadesValidadas;
    @Getter @Setter private Map<CasoCriticoTipo, DtoCasoCriticoAlineacion> casosCriticosSistema = new HashMap<>();
    @Getter @Setter @NonNull private List<Captura> capturas;
    @Getter @Setter private UnidadMateriaComentario comentarioReprobatorio;
    @Getter @Setter private BigDecimal promedio = BigDecimal.ZERO;
    @Getter @Setter private Boolean tieneCasoCritico = false, tieneCasoCriticoSistema = false, estaAprobado = false, tieneComentarioReprobatorio;

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Captura{
        @Getter @Setter @NonNull private DtoUnidadConfiguracionAlineacion.Detalle detalle;
        @Getter @Setter @NonNull private CalificacionEvidenciaInstrumento calificacion;
    }

    public static Captura crearCaptura(DtoUnidadConfiguracionAlineacion.Detalle detalle, CalificacionEvidenciaInstrumento calificacion){
        return new Captura(detalle, calificacion);
    }

    public void setDtoCasoCritico(DtoCasoCriticoAlineacion dtoCasoCritico) {
        this.dtoCasoCritico = dtoCasoCritico;
        if(dtoCasoCritico == null) {
            tieneCasoCritico = false;
            return;
        }
        tieneCasoCritico = !dtoCasoCritico.getEstado().equals(CasoCriticoEstado.SIN_REGISTRO);
    }
}
