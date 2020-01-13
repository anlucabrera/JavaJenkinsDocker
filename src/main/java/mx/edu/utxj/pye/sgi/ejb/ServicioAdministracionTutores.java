/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Periodos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioAdministracionTutores implements EjbAdministracionTutores {

    @EJB Facade f;
    @EJB Facade2 f2;
    @Getter @Setter private Integer periodo;
    
    
    @Override
    public PeriodosEscolares getPeriodoActual() {
        TypedQuery<PeriodosEscolares> q = f.getEntityManager().createQuery("SELECT MAX(p.periodo) FROM PeriodosEscolares AS p ", PeriodosEscolares.class);
        return q.getSingleResult();
    }

    @Override
    public List<Grupos> esTutor(Integer maestro, Integer periodo) {
        Short grado = 11;
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionTutores.esTutor() esta es la clave del maestro obtenida : --- > " + maestro);
        TypedQuery<Grupos> q = f2.getEntityManager().createQuery("SELECT g FROM Grupos AS g WHERE g.cveMaestro = :cveMaestro AND g.gruposPK.cvePeriodo = :cvePeriodo AND g.grado = :grado", Grupos.class);
        q.setParameter("cveMaestro", maestro);
        q.setParameter("cvePeriodo", periodo);
        q.setParameter("grado", grado);
        List<Grupos> lg = q.getResultList();
        if (lg.isEmpty()) {
            return null;
        } else {
            return q.getResultList();
        }
    }

    @Override
    public List<Alumnos> getListaDeAlumnosPorDocente(Integer grupo) {
        TypedQuery<Alumnos> q = f2.getEntityManager().createQuery(" SELECT a FROM Alumnos AS a WHERE a.grupos.gruposPK.cveGrupo = :grupo ", Alumnos.class);
        q.setParameter("grupo", grupo);
//        q.setParameter("periodo", periodo);
        List<Alumnos> la = q.getResultList();
        if (la == null || la.isEmpty()) {
            return null;
        } else {
//            for (int i = la.size()-1; i >= la.size()-1; i--) {
//                System.out.println(" alumno " + la);
//            }
            return la;

        }

    }


    @Override
    public List<VistaAlumnosPye> findAllByMatricula(String matricula) {
//        List<VistaAlumnosPye> salida;
        Query q = f2.getEntityManager().createNativeQuery("SELECT * from [saiiut].[vista_alumnos_pye] as v  where v.matricula = ?1 and v.cve_periodo is not null", VistaAlumnosPye.class);
        //TypedQuery<VistaAlumnosPye> q =  //em.createNamedQuery("VistaAlumnosPye.findByMatricula", VistaAlumnosPye.class);
        q.setParameter(1, matricula);
//        salida = q.getResultList();
        return q.getResultList();
    }
    

}
