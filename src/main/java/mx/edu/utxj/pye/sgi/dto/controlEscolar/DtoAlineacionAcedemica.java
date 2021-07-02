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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"alineacionAcademica"})
public class DtoAlineacionAcedemica implements Serializable {

    @Getter    @Setter    @NonNull    private Integer ide;
    @Getter    @Setter    private String clave;
    @Getter    @Setter    private String descripcion;   
    @Getter    @Setter    private String nivelA;  
    @Getter    @Setter    private Double meta;   
    @Getter    @Setter    private PlanEstudio planEstudio;
    @Getter    @Setter    private PlanEstudioMateria planEstudioMateria;

    public DtoAlineacionAcedemica(Integer ide, String clave, String descripcion, String nivelA, Double meta, PlanEstudio planEstudio, PlanEstudioMateria planEstudioMateria) {
        this.ide = ide;
        this.clave = clave;
        this.descripcion = descripcion;
        this.nivelA = nivelA;
        this.meta = meta;
        this.planEstudio = planEstudio;
        this.planEstudioMateria = planEstudioMateria;
    }
}
