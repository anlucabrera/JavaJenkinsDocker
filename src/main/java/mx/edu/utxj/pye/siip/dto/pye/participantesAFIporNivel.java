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
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "ParticipantesAFIporNivel")
@EqualsAndHashCode
public class ParticipantesAFIporNivel implements Serializable{

    private static final long serialVersionUID = 8569654802003406126L;
    @Getter @Setter @NonNull private String nivel;
    @Getter @Setter private Long partH;
    @Getter @Setter private Long partM;
    
}
