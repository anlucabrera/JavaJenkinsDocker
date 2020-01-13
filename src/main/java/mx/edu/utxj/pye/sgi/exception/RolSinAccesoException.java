package mx.edu.utxj.pye.sgi.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.funcional.Rolable;

@RequiredArgsConstructor
public class RolSinAccesoException extends Exception{
    @Getter @NonNull private PersonalActivo personal;
    @Getter @NonNull private Rolable rolable;

    @Override
    public String getMessage() {
        return String.format("El personal con clave %s y nombre %s está intentando acceder al recurso con acceso restringido de tipo %s.", personal.getPersonal().getClave().toString(), personal.getPersonal().getNombre(), rolable.getClass().getCanonicalName());
    }
}
