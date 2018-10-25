package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum EvidenciaCategoria {
    UNICA("Única"), MULTIPLE("Múltiple");
    @Getter @Setter @NonNull private String label;
}
