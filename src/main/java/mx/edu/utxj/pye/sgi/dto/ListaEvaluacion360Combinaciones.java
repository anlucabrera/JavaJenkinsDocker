/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@EqualsAndHashCode(of = {"clave"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaEvaluacion360Combinaciones {
    @Getter @Setter @NonNull private Integer clave;
    @Getter @Setter private Integer evaluacion;
    @Getter @Setter private String tipo;
    
    @Getter @Setter private Integer nominaEvaluado;
    @Getter @Setter private String nombreEvaluado;
    @Getter @Setter private String aOperativaEvaluado;
    @Getter @Setter private String cOperativaEvaluado;
    
    @Getter @Setter private Integer nominaEvaluador;
    @Getter @Setter private String nombreEvaluador;
    @Getter @Setter private String aOperativaEvaluador;
    @Getter @Setter private String cOperativaEvaluador;
}
