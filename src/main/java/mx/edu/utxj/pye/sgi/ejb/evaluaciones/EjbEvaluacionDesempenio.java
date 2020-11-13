/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import edu.mx.utxj.pye.seut.util.util.Cuestionario;
import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonalDesempenioEvaluacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvaluacionDesempenio {
    
    public List<SelectItem> getRespuestasPosibles();
    
    public List<Apartado> getApartados(Cuestionario cuestionario);
    
    public void setApartadosEtiquetas(List<Apartado> apartados);
    
    public List<ListaPersonal> getListaDirectivos();
    
    public List<ListaPersonal> getListaSubordinados(ListaPersonal directivo);
    
    /**
     * Busca la evaluación activa considerando las fechas de inicio y fin de cada evaluación
     * @return Devuelve la evaluación activa del periodo mas reciente, en caso de no haber evaluaciones activas devuelve null.
     */
    public DesempenioEvaluaciones evaluacionActiva();
    
    public void actualizarRespuestaPorPregunta(DesempenioEvaluacionResultados resultado, Float pregunta, String respuesta);
    
    public String obtenerRespuestaPorPregunta(DesempenioEvaluacionResultados resultado, Float pregunta);
    
    /**
     * Obtiene los resultados de evaluación ya almacenados en base de datos y los empaqueta en el objeto desempenioEvaluacion como lista de resultados,
     * a los no almacenados les asigna una instancia de resultado en blanco.<br/>
     * Se recomienda invocar antes de que el usuario asigne valores de evaluación, ya que serán borrados.
     * @param desempenioEvaluacion Se utiliza para empaquetar los resultados y para leer la clave de la evaluacion indispensable para buscar los resultados
     * @param directivo Se utiliza como parámetro de búsqueda de los resultados
     * @param subordinados Se utiliza para crear la lista de claves para buscar los resultados 
     */
    public void cargarResultadosAlmacenados(DesempenioEvaluaciones desempenioEvaluacion, ListaPersonal directivo, List<ListaPersonal> subordinados);
    
    /**
     * Obtiene los resultados de la evaluación, solo de los subordinados ya evaluados, lo que permite conocer a quienes ya se empezaron a evaluar
     * y quienes ya fueron evaluados correctamente.
     * @param desempenioEvaluacion Se utuliza para filtrar los resultados por evaluación
     * @param directivo Se utiliza para filtrar los resultados por directivo.
     * @return Resultados de subordinados evaluados.
     */
    public List<ListaPersonalDesempenioEvaluacion> obtenerListaResultadosPorEvaluacionEvaluador(DesempenioEvaluaciones desempenioEvaluacion, ListaPersonal directivo);
    
    public DesempenioEvaluaciones getUltimaEvaluacionDesempenio();
    
    public PeriodosEscolares getPeriodoDeLaEvaluacionDesempenio(Integer periodo);
}
