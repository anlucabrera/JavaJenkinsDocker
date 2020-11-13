/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import java.util.List;
import javax.ejb.Local;
import javax.faces.model.SelectItem;

import com.github.adminfaces.starter.infra.security.LogonMB;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonal;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonalEvaluacion360;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonalEvaluacion360Promedios;
import mx.edu.utxj.pye.sgi.entity.ch.view.ListaPersonalEvaluacion360Reporte;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvaluacion3601 {
     
    public List<SelectItem> getRespuestasPosibles();
    
    public List<Apartado> getApartados();
    
    public List<ListaPersonal> getListaDirectivos();
    
    public List<ListaPersonal> getListaSubordinados(ListaPersonal directivo);
    
    /**
     * Busca la evaluación activa considerando las fechas de inicio y fin de cada evaluación
     * @return Devuelve la evaluación activa del periodo mas reciente, en caso de no haber evaluaciones activas devuelve null.
     */
    public Evaluaciones360 evaluacionActiva();
    
    public void actualizarRespuestaPorPregunta(Evaluaciones360Resultados resultado, Float pregunta, String respuesta);
    
    public String obtenerRespuestaPorPregunta(Evaluaciones360Resultados resultado, Float pregunta);
    
    /**
     * Obtiene los resultados de evaluación ya almacenados en base de datos y los empaqueta en el objeto desempenioEvaluacion como lista de resultados,
     * a los no almacenados les asigna una instancia de resultado en blanco.<br/>
     * Se recomienda invocar antes de que el usuario asigne valores de evaluación, ya que serán borrados.
     * @param evaluaciones360 Se utiliza para empaquetar los resultados y para leer la clave de la evaluacion indispensable para buscar los resultados
     * @param directivo Se utiliza como parámetro de búsqueda de los resultados
     * @param subordinados Se utiliza para crear la lista de claves para buscar los resultados 
     */
    public void cargarResultadosAlmacenados(Evaluaciones360 evaluaciones360, ListaPersonal directivo, List<ListaPersonal> subordinados);
    
    /**
     * Obtiene los resultados de la evaluación, solo de los subordinados ya evaluados, lo que permite conocer a quienes ya se empezaron a evaluar
     * y quienes ya fueron evaluados correctamente.
     * @param evaluaciones360 Se utuliza para filtrar los resultados por evaluación
     * @param directivo Se utiliza para filtrar los resultados por directivo.
     * @return Resultados de subordinados evaluados.
     */
    public List<ListaPersonalEvaluacion360> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones360 evaluaciones360, ListaPersonal directivo);
    
    public void comprobarResultado(Evaluaciones360Resultados resultado);
    
    /**
     * Obtiene el apartado de las habilidades específicas para cada evaluado
     * @param categoria Clave de la categoría del evaluado a obtener sus habilidades
     * @return Lista de habilidades;
     */
    public Apartado getApartadoHabilidades(Short categoria);
    
    /**
     * Obtiene la lista de los promedios de los resultados de la evaluación para ser mostrados en los ususrios del departamento de personal.
     * @return Lista de promedios
     */
    public List<ListaPersonalEvaluacion360Promedios> getPromediosPorEvaluado();
    
    public List<ListaPersonalEvaluacion360Reporte> getReportesPorEvaluador();
    
    /**
     * Obtiene la evaluacion de 360 dependiendo del periodo
     * @param periodo
     * @return 
     */
//    public Evaluaciones360 getEvaluacionAdministracion(Integer periodo);
    
}
