/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;

import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.dtoEstudianteMateria;
import mx.edu.utxj.pye.sgi.dto.dtoEstudiantesEvalauciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;

/**
 *
 * @author Carlos Alfredo Vargas Galindo edit-->Tatisz xP
 */
@Local
public interface EJBEvaluacionDocenteMateria {

    /**
     * Obtiene la evaluacion a docente activa
     * @return
     */
    public ResultadoEJB<Evaluaciones> getEvDocenteActiva();

    /**
     * Obtiene la ultima evalaucion a Docente Materia activa
     * @return Resultado del Proceso
     */
    public ResultadoEJB<Evaluaciones> getUltimaEvDocenteActiva();

    /**
     * obtiene el periodo de la evaluacion activa
     *
     * @param evaluacion
     * @return
     */
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion);

    /**
     * Obtiene el Periodo de la Evalaución
     * @param evaluacion evaluación a buscar
     * @return Resultado del Proceso
     */
    public ResultadoEJB<PeriodosEscolares>getPeriodoEvaluacion(Evaluaciones evaluacion);



    /**
     * obtiene las posibles respuestas
     *
     * @return
     */
    public List<SelectItem> getRespuestasPosibles();

    /**
     * Genera las preguntas
     *
     * @return
     */
    public List<Apartado> getApartados();

    /**
     * lista de docentes a evaluar
     *
     * @param matricula el parametro con el cual se optienen los docentes a
     * evaluar
     * @return
     */
    public List<VistaEvaluacionDocenteMateriaPye> getDocenteMAteria(String matricula, Integer periodo);

    /**
     * Obtiene la lista de materias que tiene el estudiante por su matricula
     * @param matricula matricula del estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<VistaEvaluacionDocenteMateriaPye>> getDocenteMateriabyMatricula(String matricula);

    /**
     * Obtiene los datos del docente que sera evaluado
     *
     * @param matricula
     * @param materia
     * @param periodo
     * @return
     */
    public VistaEvaluacionDocenteMateriaPye getDatosDocente(String matricula, String materia, Integer periodo);

    /**
     * Obtiene la evaluación activa
     *
     * @return
     */
    public Evaluaciones evaluacionActiva();

    public ResultadoEJB<Evaluacion> getEvaluacionDocenteActiva();
    
    public Evaluaciones ultimaEvaluacionDocenteMaterias();

    /**
     * obtiene los datos del docente y la materia que se evaluara
     *
     * @param matricula
     * @param materia
     * @return
     */
    public VistaEvaluacionDocenteMateriaPye getcveMateria(String matricula, String materia);

    /**
     * actualiza las respuestas por pregunta
     *
     * @param resultado
     * @param pregunta
     * @param respuesta
     */
    public void actualizarRespuestaPorPregunta(EvaluacionDocentesMateriaResultados resultado, Float pregunta, String respuesta);

    public ResultadoEJB<EvaluacionDocentesMateriaResultados> actualizaRespuestaPorPregunta2(EvaluacionDocentesMateriaResultados resultados, String pregunta, String valor);

    /**
     * Obtiene los resultados de las encuestas completas o incompletas
     *
     * @param resultado
     * @param pregunta
     * @return
     */
    public String obtenerRespuestaPorPregunta(EvaluacionDocentesMateriaResultados resultado, Float pregunta);

    /**
     * obtiene los resultados de la evaluación
     *
     * @param evaluacion
     * @param docenteMateriaPye
     * @return
     */
    public EvaluacionDocentesMateriaResultados getResultados(Evaluaciones evaluacion, VistaEvaluacionDocenteMateriaPye docenteMateriaPye);

    /**
     * Carga los resultados de la evaluacion
     *
     * @param evaluacion
     * @param datosEvaluador
     * @param docentesEvaluados
     */
    public void cargarResultadosAlmacenados(Evaluaciones evaluacion, VistaEvaluacionDocenteMateriaPye datosEvaluador, List<VistaEvaluacionDocenteMateriaPye> docentesEvaluados);

    /**
     * obtiene los resultado de la evaluacion por evaluador
     *
     * @param evaluaciones
     * @param docentes
     * @return
     */
    public List<EvaluacionDocentesMateriaResultados> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones evaluaciones, VistaEvaluacionDocenteMateriaPye docentes);

    public List<EvaluacionDocentesMateriaResultados> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones evaluaciones, Integer matricula);

    /**
     * Obtiene lista de resultados generales de la evaluacion activa  por matricula
     * @param evaluacion Evaluacion activa
     * @param matricula matricula del estudiante
     * @return Resultado del proceso , lista de resultados por matricula
     */

    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> getListResultadosDocenteMateriabyMatricula(Evaluaciones evaluacion, int matricula);

    /**
     * Obtiene la lista de resultados  completos de la evalacion por matricula del estudiante,
     * @param evaluacion evaluacion activa
     * @param matricula matricula del estudiante
     * @return Resultado del proceso, y lista de resultados completos
     */

    public ResultadoEJB<List<EvaluacionDocentesMateriaResultados>> getListResultadosDocenteMateriaCompletosbyMatricula(Evaluaciones evaluacion, int matricula);
    /**
     *Comprueba si el resultado de la evaluacion es satisfactoria
     * @param resultado
     */
    public void comprobarResultado(EvaluacionDocentesMateriaResultados resultado);

    /**
     * Idetentifica en que base esta regustrado el estudiante(Control Escolar/Saiiut) y respecto a eso busca el listado de sus materias que esta cursando
     * @param estudiante dtoEstudiante
     * @param evaluacion Evaluación activa
     * @return Resultado del Proceso(Lista de materias)
     */
    public ResultadoEJB<List<dtoEstudianteMateria>> getMateriasbyEstudiante (dtoEstudiantesEvalauciones estudiante, Evaluaciones evaluacion);

    /**
     * Obtiene los resultados de la evaluacion por matricula, evaluacion, evaluado
     * @param evaluador Estudiante
     * @param evaluado Docente
     * @param evaluacion Evaluacion activa
     * @return
     */
    public ResultadoEJB<EvaluacionDocentesMateriaResultados> getResultadobyEvaluadorEvaluado(dtoEstudiantesEvalauciones evaluador,dtoEstudianteMateria evaluado, Evaluaciones evaluacion);

}
