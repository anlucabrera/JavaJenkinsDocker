/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.exception;

import lombok.Getter;

/**
 *
 * @author UTXJ
 */
public class PropiedadNoEncontradaException extends RuntimeException{
    private static final long serialVersionUID = 809796316595373290L;
    @Getter private final String propiedad;

    public PropiedadNoEncontradaException(String propiedad) {
        super("La propiedad :" + propiedad + ": no ha sido localizada en la base de datos");
        this.propiedad = propiedad;
    }

    @Override
    public String getMessage() {
        return "La propiedad :" + propiedad + ": no ha sido localizada en la base de datos";
    }
    
    
}
