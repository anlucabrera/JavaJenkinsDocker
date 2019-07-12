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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.UnidadMateria;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"materiaUnidades"})
public class DtoMateriaUnidades implements Serializable {

   @Getter @Setter @NonNull private UnidadMateria unidadMateria;
    @Getter @Setter private Materia materia;

    public DtoMateriaUnidades(UnidadMateria unidadMateria, Materia materia) {
        this.unidadMateria = unidadMateria;
        this.materia = materia;
    }

}
