/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NotificacionesAreas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoNotificacionesAreas implements Serializable{
    private static final long serialVersionUID = 3329118225186186646L;
    @Getter @Setter @NonNull private NotificacionesAreas notificacionArea;
    @Getter @Setter private AreasUniversidad areaUniversidad;
    @Getter @Setter private Boolean existe;
    
    public DtoNotificacionesAreas(AreasUniversidad areaUniversidad) {
        this.areaUniversidad = areaUniversidad;
    }
    
}
