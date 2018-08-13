package mx.edu.utxj.pye.sgi.exception;

/**
 *
 * @author UTXJ
 */
public class DocumentoInternoNoSoportadoException extends RuntimeException{
    private final Class className;

    public DocumentoInternoNoSoportadoException(Class className) {
        this.className = className;
    }

    @Override
    public String getMessage() {
        return String.format("El documento interno del tipo %s no es soportado aún por el sistema.", className.getName());
    }
    
}
