/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.StoredProcedureQuery;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.VariablesProntuario;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.facade.Facade2;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbEstudiantesActivos")
public class EjbEstudiantesActivos {
    @EJB Facade f;
    @EJB Facade2 f2;
    private EntityManager em;
    private EntityManager em2;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
        em2 = f2.getEntityManager();
    }
    
    public ResultadoEJB<Alumnos> verificarEstudianteSAIIUT(String matricula){
        try{
            //verificar apertura del evento
            PeriodosEscolares periodo = getPeriodoActual();
            Alumnos alumnos = em2.createQuery("select a from Alumnos as a where a.matricula = :matricula and a.cveStatus = :estatus " +
                    "and a.grupos.gruposPK.cvePeriodo = :periodo", Alumnos.class)
                    .setParameter("estatus", 1)
                    .setParameter("matricula", matricula)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream().findFirst().orElse(null);
            if(alumnos == null){
                return ResultadoEJB.crearErroneo(2,alumnos, "El estudiante de SAIIUT no se encuentra activo en el periodo actual.");
            }else{
                return ResultadoEJB.crearCorrecto(alumnos, "Alumno de SAIIUT encontrado con éxito.");
            }

        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar al estudiante de SAIIUT (EjbEstudiantesActivos.verificarEstudianteSAIIUT).", e, Alumnos.class);
        }
    }
    
    public ResultadoEJB<Estudiante> verificarEstudianteCE(Integer matricula){
        try{
            //verificar apertura del evento
            PeriodosEscolares periodo = getPeriodoActual();
            Estudiante estudiante = em.createQuery("select e from Estudiante as e where e.matricula =:matricula and e.tipoEstudiante.idTipoEstudiante =:estatus and e.periodo =:periodo", Estudiante.class)
                    .setParameter("estatus", 1)
                    .setParameter("matricula", matricula)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream().findFirst().orElse(null);
            if(estudiante == null){
                return ResultadoEJB.crearErroneo(2,estudiante, "El estudiante de Control Escolar no se encuentra activo en el periodo actual.");
            }else{
                return ResultadoEJB.crearCorrecto(estudiante, "Alumno de Control Escolar encontrado con éxito.");
            }

        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar al estudiante de Control Escolar (EjbEstudiantesActivos.verificarEstudianteCE).", e,  Estudiante.class);
        }
    }
    
    /**
     * Permite obtener el periodo activo.
     * @return Regresa la instancia del periodo activo
     */
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }
    
}
