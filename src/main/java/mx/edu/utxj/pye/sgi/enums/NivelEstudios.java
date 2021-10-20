package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NivelEstudios {
    ING("Ingeniería / Licenciatura"),
    TSU("T.S.U.");
    @Getter @NonNull private final String label;
}
