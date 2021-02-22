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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MetasPropuestas;

/**
 *
 * @author UTXJ
 */
@ToString
@EqualsAndHashCode(of = {"materiaMetas"})
public class DtoMateriaMetasPropuestas implements Serializable {

   @Getter @Setter @NonNull private MetasPropuestas metasPropuestas;
    @Getter @Setter private Materia materia;

    public DtoMateriaMetasPropuestas(MetasPropuestas metasPropuestas, Materia materia) {
        this.metasPropuestas = metasPropuestas;
        this.materia = materia;
    }

}
