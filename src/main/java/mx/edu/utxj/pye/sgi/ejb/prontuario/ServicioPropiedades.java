/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.util.OptionalInt;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.exception.PropiedadNoEncontradaException;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioPropiedades implements EjbPropiedades {

    private static final long serialVersionUID = 2060090907401662434L;
    @EJB Facade f;

    @Override
    public ConfiguracionPropiedades leerPropiedad(String clave) {
        return f.getEntityManager().find(ConfiguracionPropiedades.class, clave);
    }

    @Override
    public OptionalInt leerPropiedadEntera(String clave) {
        ConfiguracionPropiedades p = leerPropiedad(clave);
        if(p != null){
            if(p.getValorEntero() != null){
                return OptionalInt.of(p.getValorEntero());
            }
        }
        
        throw new PropiedadNoEncontradaException(clave);
    }
}
