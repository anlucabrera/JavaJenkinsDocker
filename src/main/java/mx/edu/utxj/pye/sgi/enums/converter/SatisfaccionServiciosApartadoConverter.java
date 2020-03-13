package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.NonNull;
import mx.edu.utxj.pye.sgi.enums.SatisfaccionServiciosApartado;

import java.util.Arrays;

public class SatisfaccionServiciosApartadoConverter  extends AbstractEnumConverter<SatisfaccionServiciosApartado> {
    @Override
    public SatisfaccionServiciosApartado[] getValues() {
        return SatisfaccionServiciosApartado.values();
    }

    @Override
    public String getLabel(SatisfaccionServiciosApartado satisfaccionServiciosApartado) {
        return satisfaccionServiciosApartado.getTema_corto();
    }

    public static SatisfaccionServiciosApartado of(@NonNull Double apartado){
        SatisfaccionServiciosApartadoConverter converter = new SatisfaccionServiciosApartadoConverter();
        return Arrays.stream(converter.getValues()).filter(satisfaccionServiciosApartado -> satisfaccionServiciosApartado.getApartado().doubleValue() == apartado.doubleValue()).findFirst().orElse(null);
    }
}
