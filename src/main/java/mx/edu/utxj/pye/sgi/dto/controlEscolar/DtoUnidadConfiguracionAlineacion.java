package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Representa la configuracion de  una unidad que realizó un docente
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoUnidadConfiguracionAlineacion implements Serializable {
    @Getter @Setter @NonNull private UnidadMateria unidadMateria;
    @Getter @Setter @NonNull private UnidadMateriaConfiguracion unidadMateriaConfiguracion;
    @Getter @Setter @NonNull private Map<Criterio, List<Detalle>> unidadMateriaConfiguracionDetalles;
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private Boolean activaPorFecha;
    @Getter @Setter @NonNull private Boolean activaPorAperturaExtemporanea;

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class Detalle{
        @Getter @Setter @NonNull  private UnidadMateriaConfiguracionEvidenciaInstrumento detalle;
        @Getter @Setter @NonNull  private Criterio criterio;
        @Getter @Setter @NonNull  private EvidenciaEvaluacion evidencia;
        @Getter @Setter @NonNull  private InstrumentoEvaluacion instrumento;
    }
}
