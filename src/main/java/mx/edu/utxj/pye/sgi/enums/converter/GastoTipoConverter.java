package mx.edu.utxj.pye.sgi.enums.converter;

import mx.edu.utxj.pye.sgi.enums.GastoTipo;

public class GastoTipoConverter extends AbstractEnumConverter<GastoTipo> {
    @Override
    public GastoTipo[] getValues() {
        return GastoTipo.values();
    }

    @Override
    public String getLabel(GastoTipo gastoTipo) {
        return gastoTipo.getLabel();
    }

    public static GastoTipo of(String value){
        return (new GastoTipoConverter()).convert(value);
    }
}
