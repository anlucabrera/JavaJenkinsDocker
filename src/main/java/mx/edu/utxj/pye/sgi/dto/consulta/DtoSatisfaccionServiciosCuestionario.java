package mx.edu.utxj.pye.sgi.dto.consulta;

import edu.mx.utxj.pye.seut.util.util.Cuestionario;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.Apartado;

import java.util.List;

@RequiredArgsConstructor
public class DtoSatisfaccionServiciosCuestionario extends Cuestionario {
    @Getter @Setter @NonNull private List<Apartado> apartados;

    public void ordenar(){
        establecerPosiciones();
    }
}
