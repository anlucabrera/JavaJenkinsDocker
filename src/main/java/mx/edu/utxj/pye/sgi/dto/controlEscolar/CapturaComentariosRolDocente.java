package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;

public class CapturaComentariosRolDocente extends CapturaCalificacionesRolDocente {

    public CapturaComentariosRolDocente(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
    }
}
