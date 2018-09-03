/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vinculacion;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.BolsaTrabajoEntrevistas;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOBolsaEntrevistas implements Serializable{

    private static final long serialVersionUID = 2799823249847985278L;
    @Getter @Setter private String generacion;
    @Getter @Setter private AreasUniversidad areasUniversidad;
    @Getter @Setter private BolsaTrabajoEntrevistas bolsaTrabajoEntrevistas;
}
