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
import mx.edu.utxj.pye.siip.dto.escolar.DTOBecasPeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class ListaBecasPeriodo implements Serializable{

    private static final long serialVersionUID = -3595746734131214217L; 
    
    @Getter @Setter private List<DTOBecasPeriodosEscolares> becasPeriodosEscolares;
}
