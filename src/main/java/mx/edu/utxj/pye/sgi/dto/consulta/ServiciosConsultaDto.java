package mx.edu.utxj.pye.sgi.dto.consulta;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controlador.Evaluacion;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiciosConsultaDto extends AbstractRol {
    @Getter @Setter private Evaluaciones evaluacionSeleccionada;
    @Getter private List<Evaluaciones> evaluaciones;

    @Getter @Setter private Map<Evaluaciones, DtoSatisfaccionServiciosEncuesta> contenedores = new HashMap<>();

    public ServiciosConsultaDto(@NonNull Filter<PersonalActivo> filtro) {
        super(filtro);
    }

    public boolean hayContenedor(Evaluaciones evaluacion){
        return contenedores.containsKey(evaluacion);
    }

    public boolean hayEvaluacion(){
        return evaluacionSeleccionada != null;
    }

    public void setEvaluaciones(List<Evaluaciones> evaluaciones) {
        this.evaluaciones = evaluaciones;
        if(evaluaciones != null){
            if(!evaluaciones.isEmpty()) evaluacionSeleccionada = evaluaciones.get(1);
        }
    }
}
