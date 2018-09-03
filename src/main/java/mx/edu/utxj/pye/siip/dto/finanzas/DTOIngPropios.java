/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.finanzas;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.IngresosPropiosCaptados;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOIngPropios implements Serializable{

    private static final long serialVersionUID = -6128710920932285236L;
    @Getter @Setter private String cicloEscolar;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private IngresosPropiosCaptados ingresosPropiosCaptados;
}
