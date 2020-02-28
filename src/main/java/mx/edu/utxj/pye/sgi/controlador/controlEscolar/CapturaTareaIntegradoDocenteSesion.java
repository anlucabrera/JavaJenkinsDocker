package mx.edu.utxj.pye.sgi.controlador.controlEscolar;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class CapturaTareaIntegradoDocenteSesion implements Serializable {
    @Getter @Setter private PeriodosEscolares periodo;
    @Getter @Setter private DtoCargaAcademica carga;
}
