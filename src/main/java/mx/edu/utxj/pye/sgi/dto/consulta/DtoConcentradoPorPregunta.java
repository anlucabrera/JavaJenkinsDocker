package mx.edu.utxj.pye.sgi.dto.consulta;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor @EqualsAndHashCode
public class DtoConcentradoPorPregunta implements Serializable {
    @Getter @Setter @NonNull private Pregunta pregunta;
    @Getter @Setter @NonNull private short areaAcademicaCategoria;
    @Getter @Setter @NonNull private short programaEducativoCategoria;
    @Getter @Setter @NonNull private List<DtoAreaUniversidadCategoria> programas;
    @Getter @Setter @NonNull private List<DtoAreaUniversidadCategoria> areasAcademicas;
    @Getter @Setter private Map<DtoAreaUniversidadCategoria, BigDecimal> valoresMap = new HashMap<>();

    public void agregarValor(@NonNull DtoAreaUniversidadCategoria areaOPrograma, BigDecimal valor){
        if(programas.contains(areaOPrograma) || areasAcademicas.contains(areaOPrograma)){
            valoresMap.put(areaOPrograma, valor);
        }
        /*if(areaOPrograma.getCategoria().getCategoria() == areaAcademicaCategoria){
            va
        }else if(areaOPrograma.getCategoria().getCategoria() == programaEducativoCategoria){
            //
        }*/
    }
}
