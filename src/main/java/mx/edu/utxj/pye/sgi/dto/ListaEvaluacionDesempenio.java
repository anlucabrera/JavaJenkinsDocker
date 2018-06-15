/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Planeacion
 */
@EqualsAndHashCode(of = {"clave"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaEvaluacionDesempenio {
    @Getter @Setter @NotNull private  Integer clave;
    @Getter @Setter private Integer evaluacion;
    @Getter @Setter private Integer evaluador;
    @Getter @Setter private String nombreEvaluador;
    @Getter @Setter private Integer evaluado;
    @Getter @Setter private String nombreEvaluado;
    @Getter @Setter private Integer area;
    @Getter @Setter private String nombreArea;
}
