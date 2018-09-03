/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.servgen.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.servgen.DTOEquiposComputoCPE;
import mx.edu.utxj.pye.siip.dto.servgen.DTOEquiposComputoInternetCPE;

/**
 *
 * @author UTXJ
 */
public class ListaDistribucionEquipamiento implements Serializable{

    private static final long serialVersionUID = 4265063038654835091L;
    @Getter @Setter List<DTOEquiposComputoCPE> dtoEquiposComputoCPE;
    @Getter @Setter List<DTOEquiposComputoInternetCPE> dtoEquiposComputoInternetCPE;
}
