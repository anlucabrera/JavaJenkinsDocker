/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.finanzas.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.finanzas.DTOIngPropios;

/**
 *
 * @author UTXJ
 */
public class ListaIngPropios implements Serializable{

    private static final long serialVersionUID = 5136993942929106292L;
    @Getter @Setter private List<DTOIngPropios> ingPropios;
}
