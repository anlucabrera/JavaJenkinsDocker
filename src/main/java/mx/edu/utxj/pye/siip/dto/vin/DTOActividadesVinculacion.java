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
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesVinculacion;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoActividadesVinculacion")
@EqualsAndHashCode
public class DTOActividadesVinculacion implements Serializable{
    private static final long serialVersionUID = 8797351183253485738L;
    
    @Getter @Setter @NonNull private ActividadesVinculacion actividadVinculacion;
    @Getter @Setter private OrganismosVinculados organismoVinculado;
    @Getter @Setter private Boolean existe;

    public DTOActividadesVinculacion(ActividadesVinculacion actividadVinculacion) {
        this.actividadVinculacion = actividadVinculacion;
    }

    public DTOActividadesVinculacion(ActividadesVinculacion actividadVinculacion, OrganismosVinculados organismoVinculado) {
        this.actividadVinculacion = actividadVinculacion;
        this.organismoVinculado = organismoVinculado;
    }
    
    
    
}
