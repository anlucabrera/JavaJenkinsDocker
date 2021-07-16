package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum UsuarioTipo {
    ESTUDIANTE("ESTUDIANTE"),
    ESTUDIANTE19("ESTUDIANTE19"),
    ADMINISTRATIVO("ADMINISTRATIVO"),
    DIRECTIVO("DIRECTIVO"),
    DOCENTE("DOCENTE"),
    TRABAJADOR("TRABAJADOR"),
    INVITADO("INVITADO"),
    ASPIRANTE("ASPIRANTE"),
    ASPIRANTE_ING("ASPIRANTEING")
    ;
    
    @Getter @NonNull private final String label;
}
