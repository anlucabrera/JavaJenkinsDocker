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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"planEstudioMateriaCompetencias"})
public class DtoPlanEstudioMateriaCompetencias implements Serializable {

    @Getter    @Setter    @NonNull    private Competencia competencia;
    @Getter    @Setter    private Competencia competenciaNewR;
    @Getter    @Setter    private PlanEstudioMateria planEstudioMateria;    
    @Getter    @Setter    private PlanEstudio planEstudio;

    public DtoPlanEstudioMateriaCompetencias(Competencia competencia, Competencia competenciaNewR, PlanEstudioMateria planEstudioMateria, PlanEstudio planEstudio) {
        this.competencia = competencia;
        this.competenciaNewR = competenciaNewR;
        this.planEstudioMateria = planEstudioMateria;
        this.planEstudio = planEstudio;
    }
}
