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
 * @author Planeación
 */
@EqualsAndHashCode(of = {"clave"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListadoEvaluacionEgresados {
    @Getter @Setter @NotNull private Integer clave;
    @Getter @Setter private String nombre;
    @Getter @Setter private String siglas;
    @Getter @Setter private Short grado;
    @Getter @Setter private String grupo;
    @Getter @Setter private String tutor;

}
