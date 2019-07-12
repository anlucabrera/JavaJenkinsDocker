/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Competencia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"reporteFinal"})
public class DtoVistaReportePlanEstudio implements Serializable {

    @Getter    @Setter    private PlanEstudioMateria planEstudioMateria;
    @Getter    @Setter    private Materia materia;
    @Getter    @Setter    private Competencia competencia;
    @Getter    @Setter    private UnidadMateria unidadMateria;

    public DtoVistaReportePlanEstudio(PlanEstudioMateria planEstudioMateria, Materia materia, Competencia competencia, UnidadMateria unidadMateria) {
        this.planEstudioMateria = planEstudioMateria;
        this.materia = materia;
        this.competencia = competencia;
        this.unidadMateria = unidadMateria;
    }
}
