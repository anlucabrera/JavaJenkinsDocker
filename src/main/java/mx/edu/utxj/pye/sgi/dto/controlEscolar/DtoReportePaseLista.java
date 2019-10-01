package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;

import java.io.Serializable;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@ToString
@EqualsAndHashCode(of = {"paseListaDoc"})
public class DtoReportePaseLista implements Serializable {

    @Getter    @Setter    private UnidadMateria unidadMateria;
    @Getter    @Setter    private Integer totalPlista;
    @Getter    @Setter    private Integer totalAsis;
    @Getter    @Setter    private Double porAsis;
    @Getter    @Setter    private Boolean casoC;

    public DtoReportePaseLista(UnidadMateria unidadMateria, Integer totalPlista, Integer totalAsis, Double porAsis, Boolean casoC) {
        this.unidadMateria = unidadMateria;
        this.totalPlista = totalPlista;
        this.totalAsis = totalAsis;
        this.porAsis = porAsis;
        this.casoC = casoC;
    }
    
}
