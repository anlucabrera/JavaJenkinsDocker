/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.caphum;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.ProgramasEstimulos;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOProgramasEstimulos implements Serializable{

    private static final long serialVersionUID = 1679413082638910682L;
    @Getter @Setter private Personal personal;
    @Getter @Setter private String puesto;
    @Getter @Setter private String area;
    @Getter @Setter private ProgramasEstimulos programasEstimulos;
}
