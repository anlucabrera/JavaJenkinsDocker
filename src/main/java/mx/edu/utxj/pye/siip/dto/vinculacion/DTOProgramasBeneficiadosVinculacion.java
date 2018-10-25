/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.vinculacion;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasBeneficiadosVinculacion;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoProgramasBeneficiados")
@EqualsAndHashCode
public class DTOProgramasBeneficiadosVinculacion implements Serializable{
    private static final long serialVersionUID = 6203079505349498554L;
    @Getter @Setter @NonNull ProgramasBeneficiadosVinculacion programaBeneficiadoVinculacion;
    @Getter @Setter AreasUniversidad areaUniversidad;
    @Getter @Setter Boolean existe;

    public DTOProgramasBeneficiadosVinculacion(AreasUniversidad areaUniversidad) {
        this.areaUniversidad = areaUniversidad;
    }
    
}
