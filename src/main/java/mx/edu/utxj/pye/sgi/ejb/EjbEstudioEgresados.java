package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionEstudioEgresadosResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.sescolares.Alumno;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

@Local
public interface EjbEstudioEgresados {
    public Evaluaciones geteEvaluacionActiva();
    
    public Evaluaciones getLastEvaluacion();
    
    public Alumnos getAlumnoPorMatricula(String matricula);
    
    public Personas getDatosPersonalesAlumnos(Integer persona);

    public EvaluacionEstudioEgresadosResultados getResultados(Evaluaciones evaluacione, Integer evaluador);
    
    public boolean actualizarResultado(EvaluacionEstudioEgresadosResultados resultado);
    
    public void actualizarRespuestaPorPregunta(EvaluacionEstudioEgresadosResultados resultado, String pregunta, String respuesta);
    
    public void vaciarRespuestas(EvaluacionEstudioEgresadosResultados resultado, Map<String, String> respuestas);
    
    public List<Generaciones> getGeneraciones();
    
    public List<SelectItem> selectItemsProgramasEducativos();
    
    public Alumnos getEstudianteSaiiut(String matricula);
    
    public Personas getPersonaSaiiut(Integer clave);
    
    public EvaluacionEstudioEgresadosResultados getResultadoIndividual(String evaluador);
    /*
     *Generacion de reportes estudio egresados 
     */
    
   // Regresa lista de resultados de la evelaucion activa
    public List<EvaluacionEstudioEgresadosResultados> getResultadosEvActiva(Integer evaluacion);
    public List<EvaluacionEstudioEgresadosResultados> getRestultadosEgresados();
    
    public List<EvaluacionEstudioEgresadosResultados> getResultadosPorGeneracionTSU(String generacion);
    
    public List<EvaluacionEstudioEgresadosResultados> getResultadosPorGeneracionING(String generacion);
    
    public List<EvaluacionEstudioEgresadosResultados> getResultadosPorSilgas(String siglas);
    
}
