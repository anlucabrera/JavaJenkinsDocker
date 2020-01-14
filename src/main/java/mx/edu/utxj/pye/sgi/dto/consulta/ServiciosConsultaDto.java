package mx.edu.utxj.pye.sgi.dto.consulta;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;

public class ServiciosConsultaDto extends AbstractRol {
    public ServiciosConsultaDto(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);


    }
}
