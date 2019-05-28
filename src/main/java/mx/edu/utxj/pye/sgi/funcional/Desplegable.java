package mx.edu.utxj.pye.sgi.funcional;

import mx.edu.utxj.pye.sgi.dto.PersonalActivo;

import java.io.Serializable;

public interface Desplegable  extends Serializable {
    /**
     * Permite definir si un controlador definido por rol que representa a un bloque de menú tiene elementos disponibles según el usuario logueado
     * @return Resultado de la comparación
     */
    Boolean tieneElementos();
}
