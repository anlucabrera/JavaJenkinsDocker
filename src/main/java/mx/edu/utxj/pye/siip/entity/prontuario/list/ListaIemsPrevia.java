/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.prontuario.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.prontuario.DTOIems;

/**
 *
 * @author UTXJ
 */
public class ListaIemsPrevia implements Serializable {

    private static final long serialVersionUID = -4060999371957971564L;
    @Getter @Setter private List<DTOIems> dTOIems;
}