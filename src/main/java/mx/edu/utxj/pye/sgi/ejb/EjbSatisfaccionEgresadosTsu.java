package mx.edu.utxj.pye.sgi.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import javax.faces.model.SelectItem;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.ResultadosEncuestaSatisfaccionTsu;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbSatisfaccionEgresadosTsu {

    public Evaluaciones getEvaluacionActiva();

    public ResultadosEncuestaSatisfaccionTsu getResultado(Evaluaciones evaluacion, Integer evaluador, Map<String, String> respuestas);

    public boolean actualizarResultado(ResultadosEncuestaSatisfaccionTsu resultado);

    public void actualizarRespuestaPorPregunta(ResultadosEncuestaSatisfaccionTsu resultado, String pregunta, String respuesta, Map<String, String> respuestas);

    public List<Apartado> getApartados();

    public List<SelectItem> getRespuestasPosibles();

    /////////////////////////////////////////////////////////////Administracion resultados de la encuesta\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public List<ResultadosEncuestaSatisfaccionTsu> resultadosEncuesta();

    public ResultadosEncuestaSatisfaccionTsu actualizarEstatus(ResultadosEncuestaSatisfaccionTsu updateEncSatTsu);

    public ResultadosEncuestaSatisfaccionTsu obtenerEncuestaPorEvaluador(Integer matricula);
}
