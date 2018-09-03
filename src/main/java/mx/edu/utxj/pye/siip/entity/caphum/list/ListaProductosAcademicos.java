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
import mx.edu.utxj.pye.siip.dto.caphum.DTOProductosAcademicos;
import mx.edu.utxj.pye.siip.dto.caphum.DTOProductosAcademicosPersonal;

/**
 *
 * @author UTXJ
 */
public class ListaProductosAcademicos implements Serializable{

    private static final long serialVersionUID = -9005910855361251418L;
    @Getter @Setter private List<DTOProductosAcademicos> dtoProductosAcademicos;
    @Getter @Setter private List<DTOProductosAcademicosPersonal> dtoProductosAcademicosPersonal;
    
}
