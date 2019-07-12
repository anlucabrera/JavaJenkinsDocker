/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"planEstudio"})
public class DtoPlanEstudio implements Serializable{
    @Getter @Setter @NonNull private PlanEstudio planEstudio;
    @Getter @Setter private AreasUniversidad programa;

    public DtoPlanEstudio(PlanEstudio planEstudio, AreasUniversidad programa) {
        this.planEstudio = planEstudio;
        this.programa = programa;
    }
}
