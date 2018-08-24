/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbPropiedades extends Serializable{
    /**
     * 
     * Intenta leer una propiedad de configuración según su clave
     * @param clave Clave de la propiedad
     * @return Regresa la instancia de la propiedad o NULL si la clave no existe
     */
    public ConfiguracionPropiedades leerPropiedad(String clave);
    
    public OptionalInt leerPropiedadEntera(String clave);
    
    public Optional<String> leerPropiedadCadena(String clave);
    
    public OptionalDouble leerPropiedadDecimal(String clave);
    
    public Optional<LocalDate> leerPropiedadFecha(String clave);
}
