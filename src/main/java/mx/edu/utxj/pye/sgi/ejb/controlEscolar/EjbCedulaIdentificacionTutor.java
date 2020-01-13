package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;

/**
 * @author Taatisz :)
 */
@Stateless (name = "EjbCedulaIdentificacionTutor")
public class EjbCedulaIdentificacionTutor {

    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbCedulaIdentificacion cedulaIdentificacion;
    @EJB EjbRegistroPlanEstudio ejbPlanEstudio;
    private EntityManager em;

    @PostConstruct
    public void init(){em = f.getEntityManager(); }




}
