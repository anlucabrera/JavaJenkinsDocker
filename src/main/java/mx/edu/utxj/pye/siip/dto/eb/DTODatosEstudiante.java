/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.eb;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.ViewMatriculaF911;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTODatosEstudiante implements Serializable{
    private static final long serialVersionUID = 3775714024294956971L;
    @Getter @Setter @NonNull private MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    @Getter @Setter private String carrera;
    @Getter @Setter private String sexo;
            
}
