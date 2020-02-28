package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@ToString
@EqualsAndHashCode(of = {"paseListaAcademica"})
public class DtoPaseListaAcademica implements Serializable {

    @Getter    @Setter    private List<DtoPaseLista> dtoPaseListas;
    @Getter    @Setter    private Date fehDate;

    public DtoPaseListaAcademica(List<DtoPaseLista> dtoPaseListas, Date fehDate) {
        this.dtoPaseListas = dtoPaseListas;
        this.fehDate = fehDate;
    }
    
}
