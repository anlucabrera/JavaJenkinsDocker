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
@EqualsAndHashCode(of = {"DTOciclos"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class listaDTOCiclosEscolares {
    
    @Getter @Setter @NotNull private Integer clave;
    @Getter @Setter private String ciclo;
    
}
