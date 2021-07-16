package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AspiranteTipoIng {
    ASPIRANTE_ING(1,"Aspirante Ingeniería"),
    ASPPIRANTE_ING_OTRA_GENERACION(2,"Aspirante Ingeniería otra generación"),
    ASPPIRANTE_ING_OTRA_UT(3,"Aspirante Ingeniería otra UT")
    ;

    @Getter @NonNull private final Integer numero;
    @Getter @NonNull private final String label;

}
