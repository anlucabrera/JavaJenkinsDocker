/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVariasRegistro;

/**
 *
 * @author Planeacion
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "actividadVaria")
@EqualsAndHashCode
public class DTOActividadVaria implements Serializable{

    private static final long serialVersionUID = -3894021097958471097L;
    
    @Getter @Setter @NonNull private ActividadesVariasRegistro actividadVaria;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    
}
