package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.List;


public class CedulaIdentificacionRolSE  extends AbstractRol {

    @Getter @NonNull private PersonalActivo serviciosEscolares;

    @Getter @NonNull private AreasUniversidad programa;

    // Apartados de la cedula de identificación(Informacion completa del estudiante)
    @Getter @Setter private List<Apartado> apartados;
    //Lista general de Estudiantes
    @Getter @Setter private List<Estudiante> estudiantes;
    @Getter @Setter String matricula;
    //TODO: Representa al estudiante que esta buscando
    @Getter @Setter Estudiante estudiante;
    //TODO: Cedula de identificacion del estudiante
    @Getter @Setter DtoCedulaIdentificacion cedulaIdentificacion;
    @Getter @Setter String pwdNueva;

    public CedulaIdentificacionRolSE(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad programa) {
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
