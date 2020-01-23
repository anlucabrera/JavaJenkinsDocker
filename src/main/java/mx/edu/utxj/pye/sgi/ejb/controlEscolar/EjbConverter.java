package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInscripcion;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.Objects;

@Stateless(name = "EjbConverterEJB")
public class EjbConverter {
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }

    public ResultadoEJB<DtoInscripcion> dtoEstudianteToDtoInscripcionPorCargaAcademica(DtoEstudiante dtoEstudiante, DtoCargaAcademica dtoCargaAcademica){
        try{
            //
            DtoInscripcion dtoInscripcion1 = dtoEstudiante.getInscripciones()
                    .stream()
                    .filter(dtoInscripcion -> Objects.equals(dtoInscripcion.getPeriodo(), dtoCargaAcademica.getPeriodo()))
                    .findFirst()
                    .orElse(null);

            if(dtoInscripcion1 == null) return ResultadoEJB.crearErroneo(2, "El estudiante no cuenta con una inscripción con el periodo correspondiente a la carga académica", DtoInscripcion.class);

            return ResultadoEJB.crearCorrecto(dtoInscripcion1, "Inscripción correspondiente a la carga académica");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "Ocurrió un error al intentar obtener la inscripción con el periodo correspondiente a la carga académica (EjbConverter.dtoEstudianteToDtoInscripcionPorCargaAcademica).", e, DtoInscripcion.class);
        }
    }
}
