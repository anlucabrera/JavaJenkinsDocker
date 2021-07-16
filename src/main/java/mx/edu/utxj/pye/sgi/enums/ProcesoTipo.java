package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ProcesoTipo {
    ADMISION(1,"Admision"),
    INSCRIPCION(2,"Inscripcion"),
    TITULACION_TSU(3,"TitulacionTSU"),
    ESTADIA_TSU(4,"EstadiaTSU"),
    INSCRIPCION_ING_ENT(5,"InscripcionIngENT"),
    INSCRIPCION_ING_ET(6,"InscripcionIngET")
    ;

    @Getter @NonNull private final Integer numero;
    @Getter @NonNull private final String label;

}
