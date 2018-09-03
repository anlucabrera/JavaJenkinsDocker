/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.pye.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.pye.DTOProgramasPertCalidad;
/**
 *
 * @author UTXJ
 */
public class ListaProgramasPertCalidad implements Serializable{

    private static final long serialVersionUID = -207629853757443862L;
    @Getter @Setter private List<DTOProgramasPertCalidad> programasPertCalidad;
}
