package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Representa a una carga academica registrada a un docente grupo y materia
 */
@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoInformePlaneaciones implements Serializable {

    @Getter    @Setter    Long configuracionDetalle;
    @Getter    @Setter    Integer carga;
    @Getter    @Setter    Integer configuracion;
    @Getter    @Setter    Integer unidad;
    @Getter    @Setter    String nombreUnidad;
    @Getter    @Setter    String objetivo;
    @Getter    @Setter    Date fechaInicio;
    @Getter    @Setter    Date fechaFin;
    @Getter    @Setter    Double porUnidad;
    @Getter    @Setter    Integer idCriterio;
    @Getter    @Setter    String criterio;
    @Getter    @Setter    Double porCriterio;
    @Getter    @Setter    Integer idIndicador;
    @Getter    @Setter    String indicador;
    @Getter    @Setter    Double porcentaje;
    @Getter    @Setter    String evidencia;
    @Getter    @Setter    Double meta;
    @Getter    @Setter    Boolean validadoD;
    
}
