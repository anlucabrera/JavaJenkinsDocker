/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.prontuario;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.exception.PropiedadNoEncontradaException;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless
public class ServicioPropiedades implements EjbPropiedades {

    private static final long serialVersionUID = 2060090907401662434L;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }
    @Override
    public ConfiguracionPropiedades leerPropiedad(String clave) {
        return em.find(ConfiguracionPropiedades.class, clave);
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
    
    @Override
    public Optional<String> leerPropiedadCadena(String clave) {
        ConfiguracionPropiedades p = leerPropiedad(clave);
        if(p != null){
            if(p.getValorCadena() != null){
                return Optional.of(p.getValorCadena());
            }
        }
        
        return Optional.empty();
//        throw new PropiedadNoEncontradaException(clave);
    }

    @Override
    public OptionalDouble leerPropiedadDecimal(String clave) {
        ConfiguracionPropiedades p = leerPropiedad(clave);
        if(p != null){
            if(p.getValorDecimal() != null){
                return OptionalDouble.of(p.getValorDecimal());
            }
        }
        
        return OptionalDouble.empty();
//        throw new PropiedadNoEncontradaException(clave);
    }

    @Override
    public Optional<LocalDate> leerPropiedadFecha(String clave) {
        ConfiguracionPropiedades p = leerPropiedad(clave);
        if(p != null){
            if(p.getValorFecha() != null){
                return Optional.of(p.getValorFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            }
        }
        
        return Optional.empty();
//        throw new PropiedadNoEncontradaException(clave);
    }

    @Override
    public Map<Integer, String> leerPropiedadMapa(String clave, String valor) {
        return em.createQuery("select cp from ConfiguracionPropiedades cp where cp.clave like concat(:clave, '%') and cp.valorCadena=:valor and cp.tipo='Lista'", ConfiguracionPropiedades.class)
                .setParameter("clave", clave)
                .setParameter("valor", valor)
                .getResultStream()
                .collect(Collectors.toMap(cp -> Integer.parseInt(cp.getClave().replaceAll(clave, "")), cp-> cp.getValorCadena()));
    }

}
