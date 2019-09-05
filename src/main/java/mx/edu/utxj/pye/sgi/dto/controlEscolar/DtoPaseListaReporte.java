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
public class DtoPaseListaReporte implements Serializable {

    @Getter    @Setter    private String header;
    @Getter    @Setter    private String property;

    public DtoPaseListaReporte(String header, String property) {
        this.header = header;
        this.property = property;
    }
}
