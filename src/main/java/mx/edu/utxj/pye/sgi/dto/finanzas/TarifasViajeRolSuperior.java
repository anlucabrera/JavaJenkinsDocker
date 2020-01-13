package mx.edu.utxj.pye.sgi.dto.finanzas;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.finanzas.Tarifas;

import java.io.Serializable;
import java.util.List;

public class TarifasViajeRolSuperior implements Serializable {
    /**
     * Destino al que se comisionaría
     */
    @Getter @Setter private String destino = "";

    /**
     * Sugerencias de tarifas al ir tecleando el destino
     */
    @Getter @Setter private List<Tarifas> autocompleteTarifas;
    @Getter @Setter private List<Tarifas> tarifas;
    @Getter @Setter private Tarifas tarifa;
}
