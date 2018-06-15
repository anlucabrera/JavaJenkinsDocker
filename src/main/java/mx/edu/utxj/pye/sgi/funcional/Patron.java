package mx.edu.utxj.pye.sgi.funcional;

import java.io.Serializable;

/**
 *
 * @author UTXJ
 */
@FunctionalInterface
public interface Patron extends Serializable{
    public boolean coincide(String cadena);
}
