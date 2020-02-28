package mx.edu.utxj.pye.sgi.ejb;

import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionesControlInternoResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEvaluacionControlInterno extends AbstractEjb{
    public Personal getEvaluador(Integer claveNomina);
    
    public Evaluaciones getEvaluacionActiva();
    
    public EvaluacionesControlInternoResultados getResultado(Evaluaciones evaluacion, Personal evaluador);
    
    public boolean actualizarResultado(EvaluacionesControlInternoResultados resultado);
    
    public void actualizarRespuestaPorPregunta(EvaluacionesControlInternoResultados resultado, String pregunta, String respuesta);
}
