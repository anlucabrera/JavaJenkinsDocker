package mx.edu.utxj.pye.sgi.exception;

/**
 *
 * @author UTXJ
 */
public class PeriodoEscolarNecesarioNoRegistradoException extends Exception{
    private final Integer clave;
    private final String ultimo;

    /**
     * Crea una excepción para un periodo necesario no registrado en la base de datos
     * @param clave Clave del periodo necesario.
     * @param ultimo Regresentación en cadena del último periodo registrado. 
     */
    public PeriodoEscolarNecesarioNoRegistradoException(Integer clave, String ultimo) {
        this.clave = clave;
        this.ultimo = ultimo;
    }

    @Override
    public String getMessage() {
        return String.format("Se necesita el periodo con clave %s ya que el último registrado es %s.", clave.toString(), ultimo);
    }
    
}
