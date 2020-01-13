package mx.edu.utxj.pye.sgi.enums.converter;

import mx.edu.utxj.pye.sgi.enums.TramiteTipo;

public class TramiteTipoConverter extends AbstractEnumConverter<TramiteTipo> {
    @Override
    public TramiteTipo[] getValues() {
        return TramiteTipo.values();
    }

    @Override
    public String getLabel(TramiteTipo tramiteTipo) {
        return tramiteTipo.getLabel();
    }

    public static TramiteTipo of(String value){
        return (new TramiteTipoConverter()).convert(value);
    }
}
