package mx.edu.utxj.pye.sgi.ejb.evaluaciones;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacion360Combinaciones;
import mx.edu.utxj.pye.sgi.dto.ListaEvaluacionDesempenio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluacionResultados;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones360Resultados;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.entity.prontuario.view.Listaperiodosescolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

@Local
public interface EjbEvaluacion360Combinaciones {

    /**
     * Genera las combinaciones de evaluadores, evaluados a partir de la
     * evaluación activa
     *
     * @param evaluacion Evaluación activa, es indispensable para poder
     * determinar las combinaciones en evaluaciones anteriors
     * @return Lista de combinaciones.
     */
    public List<Evaluaciones360Resultados> generar(Evaluaciones360 evaluacion);

    /**
     * Verifica si la configuración ya fue realizada
     *
     * @param evaluacion Evaluación de la que se requiere verificar
     * configuración
     * @return Regresa TRUE si la configuración ya fué realizada, FALSE de loc
     * contrario.
     */
    public Boolean detectarConfiguracion(Evaluaciones360 evaluacion);

    /**
     * Lee el personal activo al momento.
     *
     * @return Lista de personal activo
     */
    public List<Personal> getPersonalActivo();

    /**
     * Genera una lista de ramas de árbol del organigrama con directivos y
     * operativos
     *
     * @param plantilla
     * @return Lista de ramas de directivo y operativos
     */
    public Map<Personal, List<Personal>> getDirectivosOperativos(List<Personal> plantilla);

    /**
     * Obtiene un mapa ordenado por periodo de los evaluadores anteriores de
     * tipo igual
     *
     * @param personal Personal del cual se requieres su lista de evaluadores
     * @param evaluacionActiva evaluación que se está aplicando al momento para
     * determinar las de periodos anteriores
     * @return Mapa de evaluadores por periodo
     */
    public Map<PeriodosEscolares, Personal> getEvaluadoresAnteriores(Personal personal, Evaluaciones360 evaluacionActiva);

    public void guardarCombinaciones(List<Evaluaciones360Resultados> resultados);

    public PeriodosEscolares getPeriodoActual();

    public Listaperiodosescolares getPeriodoEscolar(Integer periodo);

    /*
    Se obtiene la listga de evaluaciones 360 y desempeño(desempeño en este EBJ)
     */
    public List<Evaluaciones360> getEvaluaciones360();

    public List<DesempenioEvaluaciones> getEvaluacionesDesempenio();

    public List<Evaluaciones360Resultados> getResultados360(Integer evaluacion, Integer evaluado);

    public Personal getPersonalEvaluador(Integer evaluador);

    public AreasUniversidad getAreaPorClave(Short area);

    public PersonalCategorias getCategoriaPorClave(Integer categoria);

    /*
    para crear una nueva combinación
     */
    public List<PersonalCategorias> getCategorias();

    public Evaluaciones360 editaEvaluacion360(Evaluaciones360 evaluacion, Date fi, Date ff);

    public DesempenioEvaluaciones editaDesempenioEvaluaciones(DesempenioEvaluaciones evaluacion, Date fi, Date ff);

    public Evaluaciones360Resultados getCombinacion(Integer evaluacion, Integer evaluador, Integer evaluado);
    
    public DesempenioEvaluacionResultados getCombinacionDesempenio(Integer evaluacion, Integer evaluador, Integer evaluado);

    public Evaluaciones360Resultados editaCombinacion360(Evaluaciones360Resultados evaluacion, Integer evaluado, Integer evaluador, Short categoria, String tipo);
    
    public DesempenioEvaluacionResultados editaCombinacionDesempenio(DesempenioEvaluacionResultados evaluacion, Integer evaluado, Integer evaluador);
    
    public Evaluaciones360Resultados nuevaCombinacion360(Integer evaluacion, Integer evaluado, Integer evaluador, Short categoria, String tipo);
    
    public DesempenioEvaluacionResultados nuevaCo0mbinacionDesempenio(Integer evaluacion, Integer evaluado, Integer evaluador);
    
    public Evaluaciones360Resultados eliminaCombinacion360(ListaEvaluacion360Combinaciones combinacion);
    
    public DesempenioEvaluacionResultados eliminaCombinacionDes(ListaEvaluacionDesempenio combinacion);
}
