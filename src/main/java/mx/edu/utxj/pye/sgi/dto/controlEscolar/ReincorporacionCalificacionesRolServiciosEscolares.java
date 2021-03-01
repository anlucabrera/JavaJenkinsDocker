package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.List;
import lombok.Setter;

public class ReincorporacionCalificacionesRolServiciosEscolares extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personalActivoSe;

    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad areaSe;
    
    @Getter @Setter private String tipoCal;
    @Getter @Setter private Integer cvestudiante;
    
    @Getter @Setter private Estudiante estudiante;
    
    @Getter @Setter @NonNull private List<Estudiante> estudiantesReincorporaciones;
    @Getter @Setter @NonNull private List<DtoReincorporacion.AlineacionCalificaciones> calificacionesR;    

    public ReincorporacionCalificacionesRolServiciosEscolares(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad area) {
        super(filtro);
        this.personalActivoSe = serviciosEscolares;
        this.areaSe = area;
    }

    public void setPersonalActivoSe(PersonalActivo personalActivoSe) {
        this.personalActivoSe = personalActivoSe;
    }

    
}
