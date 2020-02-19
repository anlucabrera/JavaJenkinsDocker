/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@EqualsAndHashCode(of = {"siglas"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListadoChartEvaluacionDocente {
    @Getter @Setter private String siglas;
    @Getter @Setter private Integer terminadas;
    @Getter @Setter private Integer incompleta;
    @Getter @Setter private Integer total;
}
