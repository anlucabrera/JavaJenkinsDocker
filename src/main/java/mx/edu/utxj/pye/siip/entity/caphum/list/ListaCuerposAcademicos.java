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
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadIntegrantes;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerpacadLineas;
import mx.edu.utxj.pye.sgi.entity.pye2.CuerposAcademicosRegistro;
import mx.edu.utxj.pye.siip.dto.caphum.DTOCuerpAcadIntegrantes;
import mx.edu.utxj.pye.siip.dto.caphum.DTOCuerposAcademicosR;

/**
 *
 * @author UTXJ
 */
public class ListaCuerposAcademicos implements Serializable{
    private static final long serialVersionUID = 789240774557627129L;
    @Getter @Setter private List<DTOCuerposAcademicosR> dtoCuerposAcademicosR;
    @Getter @Setter private List<DTOCuerpAcadIntegrantes> dtoCuerpAcadIntegrantes;
    @Getter @Setter private List<CuerpacadLineas> cuerpAcadLineas;
}
