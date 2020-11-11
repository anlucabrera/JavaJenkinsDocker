/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.EstudiantesClaves;
import mx.edu.utxj.pye.sgi.enums.Operacion;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvaluacionTutor2 {


    //Estos ya no los utilizan solo en la administracion que hizo Carlos para CH

    public List<VistaEvaluacionesTutores> getListaTutores();


    public VistaEvaluacionesTutores getEstudianteTutor(Integer periodo, String matricula);
    public EvaluacionTutoresResultados getResultados(Evaluaciones evaluacion, VistaEvaluacionesTutores estudiante);

    public EvaluacionTutoresResultados getSoloResultados(Evaluaciones evaluacion, Integer estudiante);

    public void actualizar(String id, String valor, EvaluacionTutoresResultados resultados);

    public void guardar(EvaluacionTutoresResultados resultados);
    //DE AQUI SON NUEVOS

    public List<SelectItem> getRespuestasPosibles();

    /**
     * Lista de preguntas de 1 - 9
     * @return
     */
    public List<Apartado> getApartados();



    /**
     * Regresa la evaluación activa segun la fecha de inicio y fin programada en base de datos
     * @return Regresa nulo si no hay una evaluación activa.
     */

    ResultadoEJB<Evaluaciones> getEvaluacionActiva();


    /**
     * Obtiene los resultados de la avaluacion al tutor activa  por clave del alumno, si no existen aun resultados los crea
     * @param evaluacion Evaluación activa
     * @param estudiante Clave del Estudiante evaluador
     * @return  Resultado del proceso
     *
     */
    public ResultadoEJB<EvaluacionTutoresResultados> getResultadosEvaluacionTutorEstudiante(Evaluaciones evaluacion, dtoEstudiantesEvalauciones estudiante);

    /**
     * Actuliza / guarda (Segun la Operacion que recibe) los resultados de la evaluacion a tutor por clave del estudiante.
     * @param id id de la pregunta
     * @param valor Valor a guardar
     * @param resultados Entidad a guardar
     * @return Resultado del proceso (guardar /actuliazar)
     */

    public ResultadoEJB<EvaluacionTutoresResultados> cargarResultadosEstudianteClave(String id, String valor, EvaluacionTutoresResultados resultados,Operacion operacion);
    /**
     * Obtiene el tutor del alumno por la clave de persona que tienen en Sauitt
     * @param tutor Clave de la persona(tutor) a buscar
     * @return  Resultado del proceso de la busqueda del tutor
     */
    public ResultadoEJB<Personal> getTutor (AlumnosEncuestas tutor);

}
