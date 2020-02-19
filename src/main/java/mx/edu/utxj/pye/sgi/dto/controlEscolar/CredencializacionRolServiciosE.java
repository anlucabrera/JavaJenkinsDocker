package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

public class CredencializacionRolServiciosE extends AbstractRol {
    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo serviciosEscolares;
    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;

    @Getter @Setter String matricula;

    @Getter @Setter Estudiante estudiante;
    
    @Getter @Setter String nombreC;
    
    @Getter @Setter AreasUniversidad carrera;
    


    public CredencializacionRolServiciosE(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad programa) {
        super(filtro);
        this.serviciosEscolares = serviciosEscolares;
        this.programa = programa;
    }

    public void setServiciosEscolares(PersonalActivo serviciosEscolares) {
        this.serviciosEscolares = serviciosEscolares;
    }

    public void setPrograma(AreasUniversidad programa) {
        this.programa = programa;
    }

}
