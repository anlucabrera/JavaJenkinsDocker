/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.escolar.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.escolar.DTOEgetsu;

/**
 *
 * @author UTXJ
 */
public class ListaEgetsuResultados implements Serializable{

    private static final long serialVersionUID = 3933205758224692952L;
    @Getter @Setter private List<DTOEgetsu> egetsus;
    
}
