/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaAlumnosPye;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateful
public class ServicioAdministracionTutores implements EjbAdministracionTutores {

    @EJB
    Facade f;
    @EJB
    Facade2 f2;

    @Override

    public PeriodosEscolares getPeriodoActual() {
        TypedQuery<PeriodosEscolares> q = f.getEntityManager().createQuery("SELECT MAX(p.periodo) FROM PeriodosEscolares AS p ", PeriodosEscolares.class);
        return q.getSingleResult();
    }

    @Override
    public List<Grupos> esTutor(Integer maestro, Integer periodo) {
//        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionTutores.esTutor() esta es la clave del maestro obtenida : --- > " + maestro);
        TypedQuery<Grupos> q = f2.getEntityManager().createQuery("SELECT g FROM Grupos AS g WHERE g.cveMaestro = :cveMaestro AND g.gruposPK.cvePeriodo = :cvePeriodo", Grupos.class);
        q.setParameter("cveMaestro", maestro);
        q.setParameter("cvePeriodo", periodo);
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

//    @Override
//    public List<String> getResultadosEvaluacion(String matricula, Integer periodo) {
//        Query q = f.getEntityManager().createNativeQuery(
//                //                "SELECT\n"
//                //                + "e.evaluador,\n"
//                //                + "e.cve_materia,\n"
//                //                + "e.evaluado,\n"
//                //                + "e.completo,\n"
//                //                + "e.incompleto,\n"
//                //                + "a.matricula,\n"
//                //                + "a.periodo,\n"
//                //                + "a.grupo,\n"
//                //                + "a.apellido_paterno,\n"
//                //                + "a.apellido_materno\n"
//                //                + "FROM\n"
//                //                + "capital_humano.evaluacion_docentes_materia_resultados AS e\n"
//                //                + "INNER JOIN sescolares.alumno AS a ON e.evaluador = a.matricula\n"
//                //                + "WHERE\n"
//                //                + "e.evaluador = ?1 AND\n"
//                //                + "a.periodo = ?2"
//                "SELECT\n"
//                + "e.evaluacion,\n"
//                + "e.evaluador,\n"
//                + "e.cve_materia,\n"
//                + "e.evaluado,\n"
//                + "e.completo,\n"
//                + "e.incompleto,\n"
//                + "e.promedio,\n"
//                + "a.matricula,\n"
//                + "a.apellido_paterno,\n"
//                + "a.apellido_materno,\n"
//                + "a.nombre,\n"
//                + "CASE(SUM(e.completo)>=0)\n"
//                + "WHEN SUM(e.completo) = COUNT(e.evaluador)\n"
//                + "THEN \"completo\" ELSE 'Incompleto' END AS estatus,\n"
//                + "SUM(e.completo) as evaluados, COUNT(e.evaluador) as porEvaluar\n"
//                + "FROM\n"
//                + "capital_humano.evaluacion_docentes_materia_resultados AS e\n"
//                + "INNER JOIN sescolares.alumno AS a ON e.evaluador = a.matricula\n"
//                + "where   a.matricula = ?1 and a.periodo = ?2\n"
//                + "GROUP BY evaluador"
//        );
//        q.setParameter(1, matricula);
//        q.setParameter(2, periodo);
//
////        TypedQuery<EvaluacionDocentesMateriaResultados> q = f.getEntityManager().createQuery("SELECT e FROM EvaluacionDocentesMateriaResultados e WHERE e.pk.evaluador = :evaluador ", EvaluacionDocentesMateriaResultados.class);
////        q.setParameter("evaluador", matricula);
////        System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionTutores.getResultadosEvaluacion() + " + q);
//        return q.getResultList();
//    }
//
//    @Override
//    public List<String> getDatosSinEvaluar(String matricula, Integer periodo) {
//        Query q = f.getEntityManager().createNativeQuery(
//                "SELECT\n"
//                + "a.matricula,\n"
//                + "a.periodo,\n"
//                + "a.nombre,\n"
//                + "a.apellido_paterno,\n"
//                + "a.apellido_materno\n"
//                + "FROM\n"
//                + "sescolares.alumno AS a\n"
//                + "WHERE\n"
//                + "a.matricula = ?1 AND\n"
//                + "a.periodo = ?2"
//        );
//        q.setParameter(1, matricula);
//        q.setParameter(2, periodo);
//        return q.getResultList();
//    }

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
