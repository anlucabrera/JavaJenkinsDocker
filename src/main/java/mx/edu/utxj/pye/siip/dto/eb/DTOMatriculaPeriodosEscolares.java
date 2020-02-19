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
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DTOMatriculaPeriodosEscolares implements Serializable{
    
    private static final long serialVersionUID = -6323157824759923908L;
    @Getter @Setter @NonNull private MatriculaPeriodosEscolares matricula;
    @Getter @Setter private String periodo;
    @Getter @Setter private AreasUniversidad programaEducativo;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOMatriculaPeriodosEscolares(MatriculaPeriodosEscolares matricula, String periodo, AreasUniversidad programaEducativo) {
        this.matricula = matricula;
        this.periodo = periodo;
        this.programaEducativo = programaEducativo;
    }
    
}
