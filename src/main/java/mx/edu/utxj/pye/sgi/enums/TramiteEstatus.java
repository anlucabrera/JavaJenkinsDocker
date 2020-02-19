package mx.edu.utxj.pye.sgi.enums;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Represante a los diferentes estados de un trámite de fiscalización
 * @author UTXJ
 */
@RequiredArgsConstructor
public enum TramiteEstatus {
//    COMISIONANDO("Comisionando"),
    SOLICITADO("Solicitado"), 
    PROCESO("Proceso"), 
    COMPLETADO("Completado"),
    CANCELADO("Cancelado"),
    CADUCADO("Caducado");
    @Getter @NonNull private final String label;
}
