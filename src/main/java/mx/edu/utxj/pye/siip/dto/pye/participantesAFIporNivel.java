/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.pye;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "nivel")
@EqualsAndHashCode
public class participantesAFIporNivel implements Serializable{

    private static final long serialVersionUID = 1425205940017251672L;
    
    @Id
    @Getter @Setter @NonNull private String nivel; //se declara como llave primaria
    @Getter @Setter private Integer partH;
    @Getter @Setter private Integer partM;

}
