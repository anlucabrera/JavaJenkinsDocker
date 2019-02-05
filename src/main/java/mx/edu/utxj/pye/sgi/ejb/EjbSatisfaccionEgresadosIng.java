package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaSatisfaccionEgresadosIng;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbSatisfaccionEgresadosIng {
    public Evaluaciones getEvaluacionActiva();
    
    
    public EncuestaSatisfaccionEgresadosIng getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas);
    
    public boolean actualizarResultado(EncuestaSatisfaccionEgresadosIng resultado);
    
    public void actualizarRespuestaPorPregunta(EncuestaSatisfaccionEgresadosIng resultado, String pregunta, String respuesta, Map<String,String> respuestas);
    
    public List<Apartado> getApartados();
    
    public List<SelectItem> getRespuestasPosibles();
    
    /////////////////////////////////////////////////////////////Administracion resultados de la encuesta\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    
    public List<EncuestaSatisfaccionEgresadosIng> resultadosEncuesta();
    
    public Alumnos obtenerAlumnos(String matricula);
    
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion) ;

}
