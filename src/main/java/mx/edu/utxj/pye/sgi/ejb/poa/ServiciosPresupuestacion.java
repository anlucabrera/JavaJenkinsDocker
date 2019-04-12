package mx.edu.utxj.pye.sgi.ejb.poa;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateful
public class ServiciosPresupuestacion implements EjbPresupuestacion {

    @PersistenceContext(unitName = "mx.edu.utxj.pye_sgi-ejb-pye2_ejb_1.0PU")
    private EntityManager em;

    @EJB
    FacadePoa facadePoa;

}
