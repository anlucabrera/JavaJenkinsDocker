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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"materiaPlanEstudio"})
public class DtoMateriaPlanEstudio implements Serializable {

    @Getter    @Setter    @NonNull    private PlanEstudioMateria planEstudioMateria;
    @Getter    @Setter    private PlanEstudio planEstudio;
    @Getter    @Setter    private Materia materia;

    public DtoMateriaPlanEstudio(PlanEstudioMateria planEstudioMateria, PlanEstudio planEstudio, Materia materia) {
        this.planEstudioMateria = planEstudioMateria;
        this.planEstudio = planEstudio;
        this.materia = materia;
    }
}
