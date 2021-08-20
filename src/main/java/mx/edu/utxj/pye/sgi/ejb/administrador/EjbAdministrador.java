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
import mx.edu.utxj.pye.sgi.entity.ch.Calendarioevaluacionpoa;
import mx.edu.utxj.pye.sgi.entity.ch.Permisosevaluacionpoaex;
import mx.edu.utxj.pye.sgi.entity.ch.Procesopoa;
import mx.edu.utxj.pye.sgi.entity.prontuario.ConfiguracionPropiedades;
import mx.edu.utxj.pye.sgi.entity.pye2.EjerciciosFiscales;

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
    
    public Calendarioevaluacionpoa agregarCalendarioPoa(Calendarioevaluacionpoa calendarioevaluacionpoa){
        facade.setEntityClass(Calendarioevaluacionpoa.class);
        facade.create(calendarioevaluacionpoa);
        facade.flush();
        return calendarioevaluacionpoa;
    }
    
    public Calendarioevaluacionpoa actualizarCalendarioPoa(Calendarioevaluacionpoa calendarioevaluacionpoa){
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador.actualizarCalendarioPoa()"+calendarioevaluacionpoa);
        facade.setEntityClass(Calendarioevaluacionpoa.class);
        facade.edit(calendarioevaluacionpoa);
        facade.flush();
        return calendarioevaluacionpoa;
    }
    
    public Procesopoa actualizarPorcesoPoa(Procesopoa procesopoa){
        facade.setEntityClass(Procesopoa.class);
        facade.edit(procesopoa);
        facade.flush();
        return procesopoa;
    }
    
    public Permisosevaluacionpoaex crearPermisosevaluacionpoaex(Permisosevaluacionpoaex p){
        facade.setEntityClass(Permisosevaluacionpoaex.class);
        em.persist(p);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador.crearPermisosevaluacionpoaex(A)");
        facade.flush();
        return p;
    }
    
    public Permisosevaluacionpoaex actualizarPermisosevaluacionpoaex(Permisosevaluacionpoaex p){
        facade.setEntityClass(Permisosevaluacionpoaex.class);
        em.merge(p);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador.actualizarPermisosevaluacionpoaex(B)"+p);
        facade.flush();
        return p;
    }
     
    public Permisosevaluacionpoaex eliminarPermisosevaluacionpoaex(Permisosevaluacionpoaex p) {
        facade.setEntityClass(Permisosevaluacionpoaex.class);
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador.eliminarPermisosevaluacionpoaex(C)" + p);
        if (!em.contains(p)) {
            p = em.merge(p);
        }
        em.remove(p);
        em.flush();
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.administrador.EjbAdministrador.eliminarPermisosevaluacionpoaex(C)");
        return p;
    }

    public EjerciciosFiscales crearEjerciciosFiscales(EjerciciosFiscales p){
        facade.setEntityClass(EjerciciosFiscales.class);
        facade.create(p);
        facade.flush();
        return p;
    }
}
