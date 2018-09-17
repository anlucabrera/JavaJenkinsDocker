package mx.edu.utxj.pye.siip.dto.escolar;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "asesoriasTutoriasCicloPeriodos")
@EqualsAndHashCode
public class DTOAsesoriasTutoriasCicloPeriodos implements Serializable{
    private static final long serialVersionUID = 6896177529612063988L;
    @Getter @Setter @NonNull private AsesoriasTutoriasCicloPeriodos asesoriasTutoriasCicloPeriodos; //se declara como llave primaria para interacturar con sus eviedencias
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private ActividadesPoa actividadAlineada;
}
