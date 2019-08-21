package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CasoCriticoEstadoConverter extends AbstractEnumConverter<CasoCriticoEstado> {
    @Override
    public CasoCriticoEstado[] getValues() {
        return CasoCriticoEstado.values();
    }

    @Override
    public String getLabel(CasoCriticoEstado casoCriticoEstado) {
        return casoCriticoEstado.getLabel();
    }

    public static CasoCriticoEstado of(String value){
        return (new CasoCriticoEstadoConverter()).convert(value);
    }
}
