package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum PremioTipo {
    ACADEMIA        ("Academia"),
    ADMINISTRATIVO  ("Administrativo/Directivo"),
    SECRETARIAL     ("Secretarial");

    @Getter @Setter @NonNull private String label;
}
