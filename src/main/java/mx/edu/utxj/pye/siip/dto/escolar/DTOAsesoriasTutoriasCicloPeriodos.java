/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.escolar;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.AsesoriasTutoriasCicloPeriodos;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOAsesoriasTutoriasCicloPeriodos implements Serializable{
    private static final long serialVersionUID = 6896177529612063988L;
    @Getter @Setter private AsesoriasTutoriasCicloPeriodos asesoriasTutoriasCicloPeriodos;
    @Getter @Setter private String periodoEscolar;
    @Getter @Setter private AreasUniversidad areasUniversidad;
}
