package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;


@Local
public interface EjbPersonalEvaluaciones {
    public Personal getPersonal(Integer clave);
    
    public List<PeriodosEscolares> getPeriodos(Personal personal);
    
    public Map<PeriodosEscolares, List<Evaluaciones360Resultados>> getEvaluaciones360PorPeriodo(Personal personal, List<PeriodosEscolares> periodos);
    
    public Map<PeriodosEscolares, DesempenioEvaluacionResultados> getEvaluacionesDesempenioPorPeriodo(Personal personal, List<PeriodosEscolares> periodos);
    
    public Double calcularPromedio360(List<Evaluaciones360Resultados> resultados);
    
    public Double calcularPromedioDesempenio(DesempenioEvaluacionResultados resultados);
    
    public List<ListaEvaluaciones> empaquetar(Personal personal, List<PeriodosEscolares> periodos, Map<PeriodosEscolares, List<Evaluaciones360Resultados>> resultados360, Map<PeriodosEscolares, DesempenioEvaluacionResultados> resultadosDesempenio);
}
