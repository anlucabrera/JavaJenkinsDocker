/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.administrador;

import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAdministradorSistemas")
public class EjbAdministrador {
    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pro_ejb_1.0PU")
    private EntityManager em;
    @EJB
    Facade facade; 
    
    @PostConstruct
    public void init(){
        em = facade.getEntityManager();
    }

    public List<ConfiguracionPropiedades> buscarConfiguracionPropiedadeses() {
        TypedQuery<ConfiguracionPropiedades> q = em.createQuery("SELECT c FROM ConfiguracionPropiedades c", ConfiguracionPropiedades.class);
        List<ConfiguracionPropiedades> pr = q.getResultList();
        return pr;
    }

    public ConfiguracionPropiedades crearNuevoConfiguracionPropiedades(ConfiguracionPropiedades nuevoConfiguracionPropiedades){
        em.persist(nuevoConfiguracionPropiedades);
        return nuevoConfiguracionPropiedades;
    }

    public ConfiguracionPropiedades actualizarConfiguracionPropiedades(ConfiguracionPropiedades nuevoConfiguracionPropiedades){
        facade.setEntityClass(ConfiguracionPropiedades.class);
        facade.edit(nuevoConfiguracionPropiedades);
        facade.flush();
        return nuevoConfiguracionPropiedades;
    }

    public ConfiguracionPropiedades eliminarConfiguracionPropiedades(ConfiguracionPropiedades nuevoConfiguracionPropiedades){
        em.remove(nuevoConfiguracionPropiedades);
        em.flush();
        return nuevoConfiguracionPropiedades;
    }
}
