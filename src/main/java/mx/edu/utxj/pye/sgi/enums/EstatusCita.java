package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public enum EstatusCita {
    CONFIRMADA("Confirmada"),
    ATENDIDO("Atendido"),
    REAGENDADA("Reagendada"),
    SINCONFIRMAR("Sin confirmar")
    ;
    @Getter @Setter @NonNull private String label;
}
