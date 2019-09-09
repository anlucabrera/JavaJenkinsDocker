package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@ToString
@EqualsAndHashCode(of = {"paseListaDoc"})
public class DtoPaseListaReporte implements Serializable {

    @Getter    @Setter    private String mes;
    @Getter    @Setter    private Integer dias;

    public DtoPaseListaReporte(String mes, Integer dias) {
        this.mes = mes;
        this.dias = dias;
    }

}
