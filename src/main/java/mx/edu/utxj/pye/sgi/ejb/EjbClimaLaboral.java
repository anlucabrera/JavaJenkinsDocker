package mx.edu.utxj.pye.sgi.ejb;

import java.util.Map;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesClimaLaboralResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

@Local
public interface EjbClimaLaboral extends AbstractEjb{
    public Personal getEvaluador(Integer claveNomina);
    
    public Evaluaciones getEvaluacionActiva();
    
    public EvaluacionesClimaLaboralResultados getResultado(Evaluaciones evaluacion, Personal evaluador);
    
    public boolean actualizarResultado(EvaluacionesClimaLaboralResultados resultado);
    
    public void actualizarRespuestaPorPregunta(EvaluacionesClimaLaboralResultados resultado, String pregunta, String respuesta);
    
    public void vaciarRespuestas(EvaluacionesClimaLaboralResultados resultado, Map<String, String> respuestas);
}
