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
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonalEvaluacion360;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvaluacion360 {
    /**
     * Obtiene la referencia del evaluador
     * @param clave Clave del evaluador
     * @return Referencia del evaluador
     */
    public ListaPersonal getEvaluador(Integer clave);
    
    /**
     * Obtiene la evaluación activa segun la fecha actual
     * @return Evaluación activa
     */
    public Evaluaciones360 getEvaluacionActiva();
    
    /**
     * Obtiene la lista de apartados con preguntas que serán desplegados en pantalla sin las habilidades
     * @return Lista de apartados
     */
    public List<Apartado> getApartados();
    
    /**
     * Obtiene el apartado de las habilidades específicas para cada evaluado
     * @param categoria Clave de la categoría del evaluado a obtener sus habilidades
     * @return Lista de habilidades;
     */
    public Apartado getApartadoHabilidades(Short categoria);
    
    /**
     * Obtiene la lista de respouestas posibles para todas las preguntas y habilidades
     * @return Lista de respuestas posibles
     */
    public List<SelectItem> getRespuestasPosibles();
    
    /**
     * Obtiene la lista de personal que debe evaluar un evaluador, la carga de combinaciones debe ser previa, de lo contrario no se obtendrá nada.
     * @param evaluador Clave del evaluador
     * @return Lista de personal a evaluar según 360 grados.
     */
    public List<ListaPersonalEvaluacion360> getPersonalEvaluado(Integer evaluador);
    
    public String obtenerRespuestaPorPregunta(Evaluaciones360Resultados resultado, Float pregunta);
    
    public void actualizarRespuestaPorPregunta(Evaluaciones360Resultados resultado, ListaPersonalEvaluacion360 resultadoVista, Float pregunta, String respuesta);
    
    /**
     * Permite actualizar las banderas para saber si el registro está completo o incompleto y el promedio
     * @param resultado 
     * @param resultadoVista 
     */
    public void comprobarResultado(Evaluaciones360Resultados resultado, ListaPersonalEvaluacion360 resultadoVista);
}
