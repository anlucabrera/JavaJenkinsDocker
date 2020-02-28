package mx.edu.utxj.pye.sgi.dto;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.EvaluacionConocimientoCodigoEticaResultados;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;

import javax.faces.model.SelectItem;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluacionRolPersonal extends AbstractRol {
    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personal;

    /**
     * Representa la evaluación activa
     */
    @Getter @NonNull private Evaluaciones evaluacion;

    /**
     * Representa el periodo activo de la evaluación
     */
    @Getter @NonNull private Integer periodoActivo;

    @Getter @NonNull private List<Apartado> apartados;
    @Getter @NonNull private List<SelectItem> respuestaPosibles;
    @Getter @NonNull private Map<String, String> respuestas = new HashMap<>();
    @Getter @NonNull private  String valor;
    @Getter @NonNull private EvaluacionConocimientoCodigoEticaResultados resultados;

    public EvaluacionRolPersonal(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setEvaluacion(Evaluaciones evaluacion) {
        this.evaluacion = evaluacion;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setApartados(List<Apartado> apartados) {
        this.apartados = apartados;
    }

    public void setRespuestaPosibles(List<SelectItem> respuestaPosibles) {
        this.respuestaPosibles = respuestaPosibles;
    }

    public void setRespuestas(Map<String, String> respuestas) {
        this.respuestas = respuestas;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setResultados(EvaluacionConocimientoCodigoEticaResultados resultados) {
        this.resultados = resultados;
    }
}
