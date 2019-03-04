/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vin;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;

/**
 *
 * @author Planeacion
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOOrganismoVinculado implements Serializable{

    private static final long serialVersionUID = -8354962265384314375L;
    @Getter @Setter private @NonNull OrganismosVinculados organismoVinculado;
    @Getter @Setter private ActividadesPoa actividadAlineada;
}
