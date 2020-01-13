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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AreaConocimiento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"materiaRegistro"})
public class DtoMateriaRegistro implements Serializable{
     @Getter    @Setter    @NonNull    private Materia materia;
    @Getter    @Setter    private AreaConocimiento areaConocimiento;

    public DtoMateriaRegistro(Materia materia, AreaConocimiento areaConocimiento) {
        this.materia = materia;
        this.areaConocimiento = areaConocimiento;
    }    
}
