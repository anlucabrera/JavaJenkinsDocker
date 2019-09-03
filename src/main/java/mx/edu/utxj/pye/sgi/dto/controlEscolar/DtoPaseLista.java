package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;

import java.io.Serializable;
import java.util.Date;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaalumnosca;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@ToString
@EqualsAndHashCode(of = {"paseListaDoc"})
public class DtoPaseLista implements Serializable {

    @Getter    @Setter    private Listaalumnosca listaalumnosca;
    @Getter    @Setter    private Estudiante porcInicio;
    @Getter    @Setter    private CargaAcademica cargaAcademica;

    public DtoPaseLista(Listaalumnosca listaalumnosca, Estudiante porcInicio, CargaAcademica cargaAcademica) {
        this.listaalumnosca = listaalumnosca;
        this.porcInicio = porcInicio;
        this.cargaAcademica = cargaAcademica;
    }
       
    @Override
    public String toString() {
        return listaalumnosca + "" + porcInicio+""+cargaAcademica;
    }
}
