package mx.edu.utxj.pye.sgi.funcional;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

import java.io.Serializable;
@FunctionalInterface
public interface Rolable extends Serializable{
    public Boolean tieneAcceso(PersonalActivo personal);
}
