/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.caphum.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.caphum.DTOReconocimientoProdep;
/**
 *
 * @author UTXJ
 */
public class ListaReconocimientoProdep implements Serializable{

    private static final long serialVersionUID = 9204762646933407722L;
    @Getter @Setter private List<DTOReconocimientoProdep> reconocimientos;
    
}
