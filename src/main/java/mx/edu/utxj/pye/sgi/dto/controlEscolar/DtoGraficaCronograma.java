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
    @Getter    @Setter    private Integer numeroSemaInicio;
    @Getter    @Setter    private Integer numeroSemanas;

    public DtoGraficaCronograma(String unidatTematica, Integer numeroSemaInicio, Integer numeroSemanas) {
        this.unidatTematica = unidatTematica;
        this.numeroSemaInicio = numeroSemaInicio;
        this.numeroSemanas = numeroSemanas;
    }    

    @Override
    public String toString() {
        return unidatTematica + "" + numeroSemaInicio+""+numeroSemanas;
    }
}
