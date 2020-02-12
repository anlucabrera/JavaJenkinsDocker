package mx.edu.utxj.pye.sgi.dto.consulta;

import edu.mx.utxj.pye.seut.util.preguntas.Pregunta;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor @EqualsAndHashCode(of = "dtoEstudiantePeriodo")
public class
DtoSatisfaccionServiciosEstudiante implements Serializable {
    @Getter @Setter @NonNull private DtoEstudiantePeriodo dtoEstudiantePeriodo;
    @Getter @Setter @NonNull private EncuestaServiciosResultados encuestaServiciosResultado;
    @Getter @Setter private Map<Pregunta, BigDecimal> preguntaValorMap = new HashMap<>();

    public List<DtoSatisfaccionServiciosEncuesta.Dato> generarDatos(){
        if(preguntaValorMap == null) return Collections.EMPTY_LIST;

        List<DtoSatisfaccionServiciosEncuesta.Dato> lista = preguntaValorMap.entrySet()
                .stream()
                .map(preguntaBigDecimalEntry -> new DtoSatisfaccionServiciosEncuesta.Dato(dtoEstudiantePeriodo.getPrograma(), preguntaBigDecimalEntry.getKey(), preguntaBigDecimalEntry.getValue()))
                .collect(Collectors.toList());

        return lista;
    }

    public List<DtoSatisfaccionServiciosEncuesta.DatoInstitucional> generarDatosInstitucional(){
        if(preguntaValorMap == null) return Collections.EMPTY_LIST;

        List<DtoSatisfaccionServiciosEncuesta.DatoInstitucional> lista = preguntaValorMap.entrySet()
                .stream()
                .map(preguntaBigDecimalEntry -> new DtoSatisfaccionServiciosEncuesta.DatoInstitucional(preguntaBigDecimalEntry.getKey(), preguntaBigDecimalEntry.getValue()))
                .collect(Collectors.toList());

        return lista;
    }
}
