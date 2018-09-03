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
import mx.edu.utxj.pye.siip.dto.prontuario.DTOMatriculaPeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class ListaMatriculaPeriodosEscolares implements Serializable{

    private static final long serialVersionUID = 3061432154003750036L;
    @Getter @Setter private List<DTOMatriculaPeriodosEscolares> dtoMatriculaPeriodosEscolares;
    
}
