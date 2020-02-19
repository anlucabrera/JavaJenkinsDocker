/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import java.util.Date;
import javax.validation.constraints.NotNull;
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
@EqualsAndHashCode(of = {"evaluacion"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class listadoEvaluacionesDTO {
    @Getter @Setter @NotNull private Integer evaluacion;
    @Getter @Setter private String periodo;
    @Getter @Setter private Date fechaI;
    @Getter @Setter private Date fechaF;
    @Getter @Setter private String status;
}
