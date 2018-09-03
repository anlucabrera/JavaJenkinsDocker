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
import mx.edu.utxj.pye.sgi.entity.pye2.CapacidadInstaladaCiclosEscolares;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOCapacidadInstaladaCiclosEscolares implements Serializable{
    private static final long serialVersionUID = 6042531187306561570L;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private CapacidadInstaladaCiclosEscolares capacidadInstaladaCiclosEscolares;
    @Getter @Setter private Integer totalEspaciosDocentes;
}
