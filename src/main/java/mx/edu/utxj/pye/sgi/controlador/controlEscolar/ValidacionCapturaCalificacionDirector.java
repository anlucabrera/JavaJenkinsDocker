package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.ViewScopedRol;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.ValidacionCapturaCalificacionesRolDirector;
import org.omnifaces.cdi.ViewScoped;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Named
@ViewScoped
public class ValidacionCapturaCalificacionDirector extends ViewScopedRol {
    @Getter @Setter private ValidacionCapturaCalificacionesRolDirector rol;

    //@PostConstruct

}
