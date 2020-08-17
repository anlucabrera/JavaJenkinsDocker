/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.inject.Inject;

import mx.edu.utxj.pye.sgi.controlador.AdministracionEncuesta;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EvaluacionesTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author Taatisz
 */
@Stateful
public class ServicioAdministracionEvTutor  implements EjbAdministracionEvTutor {
    
    @EJB Facade f;
    @EJB EjbEvaluacionTutor2 ejbTutor;
    @EJB EJBAdimEstudianteBase ejbAdminEstudiantes;


    @Override
    public ResultadoEJB<Evaluaciones> getEvaluacionTutorActiva() {
        try {
            //TODO: Obtene la evaluacion activa del ejb de evaluacion tutor
            ResultadoEJB<Evaluaciones> resEv= ejbTutor.getEvaluacionActiva();
            Evaluaciones evalucionTutor= resEv.getValor();
            //System.out.println("Evaluacion activa encontrada -->" + evalucionTutor.getEvaluacion());
            if(evalucionTutor == null){return ResultadoEJB.crearErroneo(3, evalucionTutor, "La evaluacion es nula");}
            else{return ResultadoEJB.crearCorrecto(evalucionTutor, "La evaluacion a tutor se encontro");}
        } catch (Exception e) {
             return ResultadoEJB.crearErroneo(1, "Ocurrio un error en EJBAdministracionEvTutor(getEvaluacionTutorActiva)", e, null);
        }
        
        
    }

    @Override
    public ResultadoEJB<Evaluaciones> getUltimaEvTutorActiva() {
        //Obtiene la ultima evaluacion a tutor activa
        try {
            Evaluaciones evaluacion = f.getEntityManager().createQuery("select e from Evaluaciones e where e.tipo=:tipo or e.tipo=:tipo2 order by  e.evaluacion DESC ",Evaluaciones.class)
                    .setParameter("tipo",EvaluacionesTipo.TUTOR.getLabel())
                    .setParameter("tipo2",EvaluacionesTipo.TUTOR_2.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(evaluacion!=null){return ResultadoEJB.crearCorrecto(evaluacion,"Ultima evalución enconrada");}
            else{return  ResultadoEJB.crearErroneo(2,evaluacion,"No se encontro ninguna evaluacion");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrio un error en EJBAdministracionEvTutor(getUltimaEvaTutorActiva)", e, null);
        }

    }

    @Override
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEvaluacionTutor() {
        try {
            List<PeriodosEscolares> periodosEscolaresEvaluacion= new ArrayList<>();
            List<Evaluaciones> evaluaciones = f.getEntityManager().createQuery("select e from Evaluaciones e where e.tipo=:tipo",Evaluaciones.class)
                    .setParameter("tipo","Tutor")
                    .getResultList()
                    ;
            if(evaluaciones.isEmpty()|| evaluaciones ==null){return ResultadoEJB.crearErroneo(2,periodosEscolaresEvaluacion,"No se encontraron periodos");}

        }catch (Exception e){

        }

        return null;
    }

    @Override
    public ResultadoEJB<PeriodosEscolares> getPeriodoEvaluacion(Evaluaciones evaluacion) {
        try {
            //TODO : Busca el periodo de la la evaluacion
             PeriodosEscolares periodoEvaluacion = f.getEntityManager().createQuery("SELECT p FROM PeriodosEscolares p WHERE p.periodo=:periodoEvaluacion", PeriodosEscolares.class)
                     .setParameter("periodoEvaluacion", evaluacion.getPeriodo())
                     .getResultStream()
                     .findFirst()
                     .orElse(null)
                     ;
             if(periodoEvaluacion!=null){return ResultadoEJB.crearCorrecto(periodoEvaluacion, "Periodo de la evalaucion encontrado");}
             else{return ResultadoEJB.crearErroneo(2, periodoEvaluacion, "El periodo de la evaluacion es nula");}
             
             
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Ocurrio un error en EJBAdministracionEvTutor(getPeriodoEvaluacion)", e, null);
        }
        
       
    }

    @Override
    public ResultadoEJB<List<EvaluacionTutoresResultados>> getResultadosEvTutor(Evaluaciones evaluacion) {
        try {
            //TODO: Obtiene los resultados de la evaluacion por evalaucion
            List<EvaluacionTutoresResultados> listResultados = new ArrayList<>();
            listResultados = f.getEntityManager().createQuery("SELECT e FROM EvaluacionTutoresResultados e WHERE e.evaluaciones.evaluacion=:evaluacion", EvaluacionTutoresResultados.class)
                    .setParameter("evaluacion", evaluacion.getEvaluacion())
                    .getResultList()
                    ;
            //System.out.println("Total de resultados de la evalaucion Encontrados!  "+ listResultados.size());
            if(listResultados.isEmpty() || listResultados==null){return ResultadoEJB.crearErroneo(3, listResultados, "La lista de resultados de la evaluacion a tutor es nula");}
            else{return ResultadoEJB.crearCorrecto(listResultados, "Resultados de la evaluacion a tutor encontrados!");}
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "Ocurrio un error en EJBAdministracionEvTutor(getResultadosEvTutor)", e, null);
        }
       
    }
    @Override
    public ResultadoEJB<EvaluacionTutoresResultados> getResultadoEvaluacionByEstudiante(dtoEstudiantesEvalauciones estudiante) {
        try {
            //TODO: Busca en el la tabla de resultados de evalaucione a tutor, algun resultado por la clave de estudiante
            EvaluacionTutoresResultados resultado = f.getEntityManager().createQuery("SELECT e from EvaluacionTutoresResultados e where e.estudiantesClaves.clave=:clave",EvaluacionTutoresResultados.class)
                    .setParameter("clave",estudiante.getClaveEstudiante())
                    .getResultStream()
                    .findFirst()
                    .orElse(null)
                    ;

            if(resultado!=null){return ResultadoEJB.crearCorrecto(resultado,"Se encontro un resultado del estudiante");}
            else{ return  ResultadoEJB.crearErroneo(2,resultado,"No se encontro ningun registro del estudiante");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrio un error en EJBAdministracionEvTutor(getResultadoEvByEstudiante)", e, null);
        }

    }

    @Override
    public ResultadoEJB<EvaluacionTutoresResultados2> getResultadosEvByEstudiante(dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion) {
      try{
          if(estudiante ==null){return ResultadoEJB.crearErroneo(2,new EvaluacionTutoresResultados2(),"El estudiante no debe ser nulo");}
          if( evaluacion ==null){return ResultadoEJB.crearErroneo(3,new EvaluacionTutoresResultados2(),"La evaluación no debe ser nula");}
         // System.out.println("Matricula que recibe " + estudiante.getMatricula());
          EvaluacionTutoresResultados2 resultados = new EvaluacionTutoresResultados2();
          resultados = f.getEntityManager().createQuery("select e from EvaluacionTutoresResultados2  e where  e.evaluacionTutoresResultados2PK.evaluador=:evaluador and e.evaluacionTutoresResultados2PK.evaluacion=:evaluacion",EvaluacionTutoresResultados2.class)
                  .setParameter("evaluador",Integer.parseInt(estudiante.getMatricula()))
                  .setParameter("evaluacion",evaluacion.getEvaluacion())
                  .getResultStream()
                  .findFirst()
                  .orElse(null);
          //System.out.println("Resultados -> "  +resultados);
          if(resultados!=null){return ResultadoEJB.crearCorrecto(resultados,"Se encontro un resultado del estudiante");}
          else {return ResultadoEJB.crearErroneo(4,resultados,"No se econtraron resultados");}
      }catch (Exception e){
          return ResultadoEJB.crearErroneo(1, "Ocurrio un error en EJBAdministracionEvTutor(getResultadosEvByEstudiante)", e, null);
      }
    }

    /**
     * Obtiene los resultados por estudiante
     * Evaluacion Tutor (Cuestionario 2)
     * @param estudiante dtoEstudiante
     * @param evaluacion Ultima evaluacion activa
     * @return Resultado del proceso
     */
    @Override
    public ResultadoEJB<EvaluacionTutoresResultados3> getResultados2EvByEstudiante(dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion) {
        try{
            if(estudiante ==null){return ResultadoEJB.crearErroneo(2,new EvaluacionTutoresResultados3(),"El estudiante no debe ser nulo");}
            if( evaluacion ==null){return ResultadoEJB.crearErroneo(3,new EvaluacionTutoresResultados3(),"La evaluación no debe ser nula");}
            // System.out.println("Matricula que recibe " + estudiante.getMatricula());
            EvaluacionTutoresResultados3 resultados = new EvaluacionTutoresResultados3();
            resultados = f.getEntityManager().createQuery("select e from EvaluacionTutoresResultados3  e where  e.evaluacionTutoresResultados3PK.evaluador=:evaluador and e.evaluacionTutoresResultados3PK.evaluacion=:evaluacion",EvaluacionTutoresResultados3.class)
                    .setParameter("evaluador",Integer.parseInt(estudiante.getMatricula()))
                    .setParameter("evaluacion",evaluacion.getEvaluacion())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            //System.out.println("Resultados -> "  +resultados);
            if(resultados!=null){return ResultadoEJB.crearCorrecto(resultados,"Se encontro un resultado del estudiante");}
            else {return ResultadoEJB.crearErroneo(4,resultados,"No se econtraron resultados");}
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrio un error en EJBAdministracionEvTutor(getResultadosEvByEstudiante)", e, null);
        }
    }
}
