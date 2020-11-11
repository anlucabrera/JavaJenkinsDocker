package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import java.util.List;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.entity.ch.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.Periodos;

import javax.ejb.Local;

@Local
public interface EjbAdministracionEvTutor {
    /**
     * Obtiene  la evaluacion a tutor activa
     * @return Resultado del proceso
     */
    
    
    public ResultadoEJB<Evaluaciones> getEvaluacionTutorActiva ();

    /**
     * Obtiene la ultima evaluación al tutor activa
     * @return Resultado del proceso/ ultima evaluacion
     */
    public ResultadoEJB<Evaluaciones> getUltimaEvTutorActiva();
    /**
     * Busca el periodo de la evaluacion
     * @param evaluacion evaluacion
     * @return Resultado del proceso
     */

    /**
     * Obtiene el listado de los periodos en los que se ha apliacado la evaluacion a Tutor
     * @return Resultado del proceso(Lista de periodos)
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEvaluacionTutor();


    public ResultadoEJB<PeriodosEscolares> getPeriodoEvaluacion(Evaluaciones evaluacion);
    /**
     * Obtiene los resultados generales de la evalaucion a tutor 
     * @param evaluacion evaluacion a buscar
     * @return Resultado del proceso (Lista de resultados)
     */
    
    public ResultadoEJB<List<EvaluacionTutoresResultados>> getResultadosEvTutor(Evaluaciones evaluacion);


    /**
     * Busca un resultado de la evalaución por clave de Estudiante
     * @param estudiante Clave del estudiante
     * @return Resultado del proceso( Resultado de la evaluacion o  null)
     */
    public ResultadoEJB<EvaluacionTutoresResultados> getResultadoEvaluacionByEstudiante(dtoEstudiantesEvalauciones estudiante);

    /**
     * Busca un resultado de le evaluacion por matricula y evaluacion
     *
     */
    public ResultadoEJB<EvaluacionTutoresResultados2> getResultadosEvByEstudiante (dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion);

    public ResultadoEJB<EvaluacionTutoresResultados3> getResultados2EvByEstudiante (dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion);
}
