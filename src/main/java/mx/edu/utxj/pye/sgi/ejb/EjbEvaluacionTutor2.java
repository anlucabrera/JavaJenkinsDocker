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
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesTutoresResultados;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.VistaEvaluacionesTutores;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvaluacionTutor2 {
    public List<SelectItem> getRespuestasPosibles();
    
    public List<Apartado> getApartados();
    
    /**
     * Regresa la evaluación activa segun la fecha de inicio y fin programada en base de datos
     * @return Regresa nulo si no hay una evaluación activa.
     */
    public Evaluaciones evaluacionActiva();
    
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion); 
    
    public List<VistaEvaluacionesTutores> getListaTutores();
    
    public VistaEvaluacionesTutores getEstudianteTutor(Integer periodo, String matricula);
    
    public EvaluacionesTutoresResultados getResultados(Evaluaciones evaluacion, VistaEvaluacionesTutores estudiante);
    
    public EvaluacionesTutoresResultados getSoloResultados(Evaluaciones evaluacion, Integer estudiante);
    
    public void actualizar(String id, String valor, EvaluacionesTutoresResultados resultados);
    
    public void guardar(EvaluacionesTutoresResultados resultados);
}
