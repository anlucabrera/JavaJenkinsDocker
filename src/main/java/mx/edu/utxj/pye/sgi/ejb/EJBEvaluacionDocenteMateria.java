/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionDocentesMateriaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionDocenteMateriaPye;

/**
 *
 * @author Carlos Alfredo Vargas Galindo
 */
@Local
public interface EJBEvaluacionDocenteMateria {

    /**
     * obtiene el periodo de la evaluacion activa
     *
     * @param evaluacion
     * @return
     */
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion);

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
     *Comprueba si el resultado de la evaluacion es satisfactoria
     * @param resultado
     */
    public void comprobarResultado(EvaluacionDocentesMateriaResultados resultado);

}
