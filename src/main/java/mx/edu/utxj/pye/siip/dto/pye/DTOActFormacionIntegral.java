/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesFormacionIntegral;


/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOActFormacionIntegral implements Serializable{

    private static final long serialVersionUID = 0L;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private ActividadesFormacionIntegral actividadesFormacionIntegral;

}
