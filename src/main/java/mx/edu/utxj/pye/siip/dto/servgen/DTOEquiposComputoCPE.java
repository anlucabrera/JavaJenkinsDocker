/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.servgen;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.EquiposComputoCicloPeriodoEscolar;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOEquiposComputoCPE implements Serializable{
    private static final long serialVersionUID = 2009116103397310212L;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private Integer total;
    @Getter @Setter private Integer escritorio;
    @Getter @Setter private Integer portatiles;
    @Getter @Setter private EquiposComputoCicloPeriodoEscolar equiposComputoCicloPeriodoEscolar;
}
