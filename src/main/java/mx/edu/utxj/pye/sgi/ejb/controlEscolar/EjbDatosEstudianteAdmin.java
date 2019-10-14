/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbDatosAccesoEstudiantes")
public class EjbDatosEstudianteAdmin {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    private EntityManager em;
    final List<DtoCargaAcademica> cargas = new ArrayList<>();
    @Getter @Setter Integer grupos = 0;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    
    }
    
    public List<Estudiante> buscaEstudiantes(Integer periodo) {
        List<Estudiante> es = new ArrayList<>();
        es = em.createQuery("SELECT es FROM Estudiante es WHERE es.periodo =:periodo", Estudiante.class)
                .setParameter("periodo", periodo)
                .getResultList();
        return es;
    }
    public Estudiante buscaEstudiante(Integer matricula) {
        Estudiante e = new Estudiante();
        e = em.createQuery("SELECT es FROM Estudiante es WHERE es.matricula =:matricula", Estudiante.class)
                .setParameter("matricula", matricula)
                .getResultList().get(0);
        return e;

    }        
    public Login actualizarUser(Login login){
        em.merge(login);
        em.flush();
        return login;
    }
}
