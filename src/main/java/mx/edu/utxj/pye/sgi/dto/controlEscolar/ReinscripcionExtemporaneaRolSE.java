package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

import java.util.List;

public class ReinscripcionExtemporaneaRolSE extends AbstractRol {

    /**
     * Representa la referencia al personal de servicios escolares
     */
    @Getter
    @NonNull
    private PersonalActivo serviciosEscolares;
    /**
     * Representa el area que pertenece
     */
    @Getter @NonNull private AreasUniversidad programa;
    //Ultimo eveento escolar de tipo reinscripcion
    @Getter EventoEscolar eventoEscolar;
    //Lista de estudiantes
    @Getter List<DtoEstudianteReinscripcion> listEstudiantesReinscritos, listEstudiantesNoReinscritos;
    @Getter List<DtoMatariaPromedio> promediosMateriasSelec ;

    @Getter List<Estudiante> listEstudiantesPeriodoAnterior;

    public ReinscripcionExtemporaneaRolSE(Filter<PersonalActivo> filtro, PersonalActivo serviciosEscolares, AreasUniversidad programa) {
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

    public void setEventoEscolar(EventoEscolar eventoEscolar) { this.eventoEscolar = eventoEscolar; }

    public void setListEstudiantesReinscritos(List<DtoEstudianteReinscripcion> listEstudiantesReinscritos) {
        this.listEstudiantesReinscritos = listEstudiantesReinscritos;
    }

    public void setListEstudiantesNoReinscritos(List<DtoEstudianteReinscripcion> listEstudiantesNoReinscritos) {
        this.listEstudiantesNoReinscritos = listEstudiantesNoReinscritos;
    }
    public void setListEstudiantesPeriodoAnterior(List<Estudiante> listEstudiantesPeriodoAnterior) {
        this.listEstudiantesPeriodoAnterior = listEstudiantesPeriodoAnterior;
    }

    public void setPromediosMateriasSelec(List<DtoMatariaPromedio> promediosMateriasSelec) {
        this.promediosMateriasSelec = promediosMateriasSelec;
    }
}
