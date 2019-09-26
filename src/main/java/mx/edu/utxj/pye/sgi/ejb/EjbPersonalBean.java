package mx.edu.utxj.pye.sgi.ejb;

import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.controlEscolar.EjbPacker;
import mx.edu.utxj.pye.sgi.ejb.finanzas.EjbFiscalizacion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.finanzas.NivelServidores;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


@Stateless
public class EjbPersonalBean  implements Serializable {
    @EJB Facade f;
    @EJB EjbFiscalizacion ejbFiscalizacion;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    public PersonalActivo pack(Personal personal){
        /*if(personal == null) return null;
        if(personal.getStatus().equals('B')) return null;
        PersonalActivo activo = new PersonalActivo(personal);
        activo.setAreaOficial(em.find(AreasUniversidad.class, personal.getAreaOficial()));
        activo.setAreaOperativa(em.find(AreasUniversidad.class, personal.getAreaOperativa()));
        activo.setAreaPOA(ejbFiscalizacion.getAreaConPOA(personal.getAreaOperativa()));
        activo.setAreaSuperior(em.find(AreasUniversidad.class, personal.getAreaSuperior()));
        return activo;*/
        return ejbPacker.packPersonalActivo(personal);
    }
    
    public PersonalActivo pack(Integer id){
        if(id != null && id >0){
            Personal personal = em.find(Personal.class, id);
            return pack(personal);
        }

        return null;
    }

    public NivelServidores getNivelPersonal(PersonalActivo personal){
        Integer rectorAreaOficial = ep.leerPropiedadEntera("rectorAreaOficial").orElse(1);
        if(personal.getAreaOficial().equals(rectorAreaOficial)) return em.find(NivelServidores.class, 1);

        if(personal.getAreaOperativa().equals(personal.getAreaPOA()) && Arrays.asList(2,4).contains(personal.getPersonal().getActividad().getActividad()))
            return em.find(NivelServidores.class, 2);
        else{
            return em.find(NivelServidores.class, 3);
        }
    }

    public NivelServidores getNivelPersonal(Personal personal){
        return getNivelPersonal(pack(personal));
    }
}
