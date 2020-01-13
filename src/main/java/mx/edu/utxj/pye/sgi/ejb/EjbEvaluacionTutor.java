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
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvaluacionTutor extends AbstractEjb{
    public List<SelectItem> getRespuestasPosibles();
    
    public List<Apartado> getApartados();
    
    public Evaluaciones evaluacionActiva();
    
    public List<VistaEvaluacionesTutores> getListaEstudiantes(Integer periodo, String matricula);
    
    public List<EvaluacionTutoresResultados> getListaTutores(VistaEvaluacionesTutores estudianteEvaluador);
    
    public void cargarResultadosAlmacenados(Evaluaciones evaluacion, VistaEvaluacionesTutores evaluador, List<EvaluacionTutoresResultados> evaluados);
    
    public String obtenerRespuestaPorPregunta(EvaluacionTutoresResultados resultado, Float pregunta);
    
    public List<EvaluacionTutoresResultados> obtenerListaResultadosPorEvaluacionEvaluador(Evaluaciones evaluacion, VistaEvaluacionesTutores evaluador);
    
    public void actualizarRespuestaPorPregunta(EvaluacionTutoresResultados resultado, Float pregunta, String respuesta);
}
