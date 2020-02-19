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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpoAreasAcademicas;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOCuerpoAreasAcademicas implements Serializable{
    
    private static final long serialVersionUID = -6213757204906475036L;
    @Getter @Setter @NonNull private CuerpoAreasAcademicas cuerpoAreasAcademicas;
    @Getter @Setter private AreasUniversidad areaUniversidad;
    @Getter @Setter private Boolean existe;

    public DTOCuerpoAreasAcademicas(AreasUniversidad areaUniversidad) {
        this.areaUniversidad = areaUniversidad;
    }
    
}
