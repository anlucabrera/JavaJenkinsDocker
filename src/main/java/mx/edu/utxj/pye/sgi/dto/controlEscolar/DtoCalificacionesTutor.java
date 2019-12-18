package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionNivelacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@AllArgsConstructor 
@ToString 
@EqualsAndHashCode
public class DtoCalificacionesTutor implements Serializable {   
    @Getter @Setter private Estudiante estudiante;
    @Getter @Setter private List<DtoCalificacionEstudiante.CalificacionePorUnidad> calificacionePorUnidad;
    @Getter @Setter private List<DtoCalificacionEstudiante.CalificacionePorMateria> calificacionePorMateria, calificacionesFinalesPorMateria;
    @Getter @Setter private List<DtoCalificacionEstudiante.TareaIntegradoraPresentacion> tareaIntegradoraPresentacion;
    @Getter @Setter private List<DtoCalificacionEstudiante.CalificacionesNivelacionPorMateria> calificacionesNivelacionPorMateria;
    @Getter @Setter private BigDecimal promedioF;
 
    public DtoCalificacionesTutor() {
    }

    
}
