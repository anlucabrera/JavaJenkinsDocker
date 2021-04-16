package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionEvidenciaInstrumento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Representa a un grupo d eestudiantes que corresponden a una carga academica registrada
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoGrupoEstudianteAlineacion implements Serializable {
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private List<DtoCapturaCalificacionAlineacion> estudiantes;

    public boolean actualizarCalificacion(CalificacionEvidenciaInstrumento calificacion, BigDecimal promedio){
        if(calificacion == null || estudiantes == null) return false;

        DtoCapturaCalificacionAlineacion dtoCapturaCalificacion = estudiantes.stream()
                .filter(dto -> dto.getCapturas()!=null)
                .filter(dto -> dto.getCapturas().contains(calificacion))
                .findFirst()
                .orElse(null);

        if(dtoCapturaCalificacion == null) return false;

        dtoCapturaCalificacion.setPromedio(promedio);
        dtoCapturaCalificacion.getCapturas().stream()
                .filter(dto -> dto.getCalificacion().equals(calificacion))
                .forEach(dto -> dto.setCalificacion(calificacion));

        return true;
    }
}
