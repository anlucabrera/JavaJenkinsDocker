/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.exception;

import java.util.Date;

/**
 *
 * @author UTXJ
 */
public class EventoRegistroNoExistenteException extends RuntimeException{
    private final Date fecha;

    public EventoRegistroNoExistenteException(Date fecha, Throwable cause) {
        super(cause);
        this.fecha = fecha;
    }

    @Override
    public String getMessage() {
        return String.format("El evento de registro no se encuentra en la base de datos para la fecha: %s.", fecha.toString());
    }
    
    
}
