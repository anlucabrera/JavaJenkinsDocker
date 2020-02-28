/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.entity.vinculacion.list;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.siip.dto.vinculacion.DTOFerias;
/**
 *
 * @author UTXJ
 */
public class ListaFeriasProfesiograficas implements Serializable{

    private static final long serialVersionUID = 0L;
    @Getter @Setter private List<DTOFerias> ferias;
}
