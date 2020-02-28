package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UnidadMateriaValidacionEstado {
    NO_NECESARIO(0D, ""),
    VALIDACION_SOLICITADA_POR_DOCENTE(1D, ""),
    VALIDADA_POR_TUTOR(2D, "");
    @Getter @NonNull private Double nivel;
    @Getter @NonNull private String label;
}
