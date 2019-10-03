package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@ToString
@EqualsAndHashCode(of = {"paseListaReportes"})
public class DtoPaseListaReportes implements Serializable {

    @Getter    @Setter    private Listaalumnosca listaalumnosca;
    @Getter    @Setter    private List<DtoReportePaseLista> drpls;

    public DtoPaseListaReportes(Listaalumnosca listaalumnosca, List<DtoReportePaseLista> drpls) {
        this.listaalumnosca = listaalumnosca;
        this.drpls = drpls;
    }
}
