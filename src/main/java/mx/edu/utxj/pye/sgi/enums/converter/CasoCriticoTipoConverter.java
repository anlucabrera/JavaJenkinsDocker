package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoTipo;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CasoCriticoTipoConverter extends AbstractEnumConverter<CasoCriticoTipo>{
    @Override
    public CasoCriticoTipo[] getValues() {
        return CasoCriticoTipo.values();
    }

    @Override
    public String getLabel(CasoCriticoTipo casoCriticoTipo) {
        return casoCriticoTipo.getLabel();
    }

    public static CasoCriticoTipo of(String value){
        return (new CasoCriticoTipoConverter()).convert(value);
    }
}
