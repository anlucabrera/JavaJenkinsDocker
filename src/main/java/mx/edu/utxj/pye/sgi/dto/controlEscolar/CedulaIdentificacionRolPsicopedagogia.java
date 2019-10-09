package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.Apartado;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.ch.Generos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.List;

public class CedulaIdentificacionRolPsicopedagogia extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter @NonNull private PersonalActivo personalPsicopedagogia;
    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;

    @Getter @Setter String matricula;

    
    //TODO: Representa la carrera del estudiante
    @Getter @Setter
    AreasUniversidad carrera;
    //TODO: Representa al estudiante que esta buscando
    @Getter @Setter
    Estudiante estudiante;
    @Getter @Setter
    DtoCedulaIdentificacion cedulaIdentificacion;
    
    @Getter @Setter List<Apartado> apartados;

    public CedulaIdentificacionRolPsicopedagogia(Filter<PersonalActivo> filtro, PersonalActivo personalPsicopedagogia, AreasUniversidad programa) {
        super(filtro);
        this.personalPsicopedagogia = personalPsicopedagogia;
        this.programa = programa;
    }
    public void setPersonalPsicopedagogia(PersonalActivo personalPsicopedagogia) {
        this.personalPsicopedagogia = personalPsicopedagogia;
    }


}
