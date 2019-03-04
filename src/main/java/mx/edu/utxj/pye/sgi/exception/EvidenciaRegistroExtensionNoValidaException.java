
package mx.edu.utxj.pye.sgi.exception;

/**
 *
 * @author UTXJ
 */
public class EvidenciaRegistroExtensionNoValidaException extends Exception{
    private String nombreArchivo;

    public EvidenciaRegistroExtensionNoValidaException(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    @Override
    public String getMessage() {
        return String.format("El archivo que intenta almacenar(%s) como evidencia de registro tiene una extensión no válida, solo archivos con extensiones jpg,png y pdf son válidos.", nombreArchivo);
    }
    
}
