/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NumeroParticipantesAFI implements Serializable{
    private static final long serialVersionUID = 8643441386716760842L;
    @Getter @Setter public String areaAcad;
    @Getter @Setter public String progEdu;
    @Getter @Setter public String cuatrimestre;
    @Getter @Setter public Integer EPH;
    @Getter @Setter public Integer EPM;
    @Getter @Setter public Integer EPHLI;
    @Getter @Setter public Integer EPMLI;
    @Getter @Setter public Integer EPHD;
    @Getter @Setter public Integer EPMD;
}
