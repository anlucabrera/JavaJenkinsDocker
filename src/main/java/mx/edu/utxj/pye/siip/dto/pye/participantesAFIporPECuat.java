/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "ParticipantesAFIporPECuat")
@EqualsAndHashCode
public class ParticipantesAFIporPECuat implements Serializable{

    private static final long serialVersionUID = 3012974059656544216L;
    @Getter @Setter private AreasUniversidad area;
    @Getter @Setter private String progEdu;
    @Getter @Setter private String cuat;
    @Getter @Setter private Long partH;
    @Getter @Setter private Long partM;
    @Getter @Setter private Long partHLI;
    @Getter @Setter private Long partMLI;
    @Getter @Setter private Long partHD;
    @Getter @Setter private Long partMD;
    
    
    
    
}
