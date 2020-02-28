package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;

import java.io.Serializable;
import java.util.Date;

/**
 * Wrapper que representa a una materia y posiblemente a su carga academica si se cumple la relacion de materia-grupo-docente
 */
@ToString
@EqualsAndHashCode(of = {"cronogramaGraf"})
public class DtoGraficaCronograma implements Serializable {

    @Getter    @Setter    private String unidatTematica;
    @Getter    @Setter    private Double porcInicio;
    @Getter    @Setter    private Double porcUtili;
    @Getter    @Setter    private Double porcRes;

    public DtoGraficaCronograma(String unidatTematica, Double porcInicio, Double porcUtili, Double porcRes) {
        this.unidatTematica = unidatTematica;
        this.porcInicio = porcInicio;
        this.porcUtili = porcUtili;
        this.porcRes = porcRes;
    }
    
    @Override
    public String toString() {
        return unidatTematica + "" + porcInicio+""+porcUtili+""+porcRes;
    }
}
