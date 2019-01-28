
package mx.edu.utxj.pye.sgi.ejb;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.EncuestaServiciosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ListaEncuestaServicios;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEncuestas;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewAlumnos;

@Local
public interface EjbEncuestaServicios extends Serializable {
    
    public Evaluaciones getEvaluacionActiva();
    
    public EncuestaServiciosResultados getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String,String> respuestas);
    
    public boolean actualizarResultado(EncuestaServiciosResultados resultado);
    
    public void actualizarRespuestaPorPregunta(EncuestaServiciosResultados resultado, String pregunta, String respuesta, Map<String,String> respuestas);
    
    public List<Apartado> getApartados();
    
    public List<SelectItem> getRespuestasPosibles();
    
    public Alumnos obtenerAlumnos(String matricula);
    
    public PeriodosEscolares getPeriodo(Evaluaciones evaluacion);
    
    public List<ListaEncuestaServicios> obtenerEncuestasRealizadas();
}
