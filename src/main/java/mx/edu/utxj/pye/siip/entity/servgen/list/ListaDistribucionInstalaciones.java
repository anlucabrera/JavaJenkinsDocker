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
import mx.edu.utxj.pye.siip.dto.servgen.DTOCapacidadInstaladaCiclosEscolares;
import mx.edu.utxj.pye.siip.dto.servgen.DTODistribucionAulasCPE;
import mx.edu.utxj.pye.siip.dto.servgen.DTODistribucionLabTallCPE;
/**
 *
 * @author UTXJ
 */
public class ListaDistribucionInstalaciones implements Serializable{

    private static final long serialVersionUID = -6603799909514478761L;
    @Getter @Setter List<DTOCapacidadInstaladaCiclosEscolares> dtoCapacidadInstaladaCiclosEscolares;
    @Getter @Setter List<DTODistribucionAulasCPE> dtoDistribucionAulasCPE;
    @Getter @Setter List<DTODistribucionLabTallCPE> dtoDistribucionLabTallCPE;
    
}
