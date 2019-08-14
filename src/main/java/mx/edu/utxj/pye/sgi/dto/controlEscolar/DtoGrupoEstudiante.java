package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Calificacion;

import java.io.Serializable;
import java.util.List;

/**
 * Representa a un grupo d eestudiantes que corresponden a una carga academica registrada
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoGrupoEstudiante implements Serializable {
    @Getter @Setter @NonNull private DtoCargaAcademica dtoCargaAcademica;
    @Getter @Setter @NonNull private List<DtoCapturaCalificacion> estudiantes;

    public boolean actualizarCalificacion(Calificacion calificacion){
        if(calificacion == null || estudiantes == null) return false;

        DtoCapturaCalificacion dtoCapturaCalificacion = estudiantes.stream()
                .filter(dto -> dto.getCapturas()!=null)
                .filter(dto -> dto.getCapturas().contains(calificacion))
                .findFirst()
                .orElse(null);

        if(dtoCapturaCalificacion == null) return false;

        dtoCapturaCalificacion.getCapturas().stream()
                .filter(dto -> dto.getCalificacion().equals(calificacion))
                .forEach(dto -> dto.setCalificacion(calificacion));

        return true;
    }
}
