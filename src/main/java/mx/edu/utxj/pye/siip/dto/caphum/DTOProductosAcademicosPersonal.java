/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.caphum;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicosPersonal;

/**
 *
 * @author UTXJ
 */
public class DTOProductosAcademicosPersonal implements Serializable{

    private static final long serialVersionUID = 7670173433445571367L;
    @Getter @Setter private ProductosAcademicosPersonal productoAcademicoPersonal;
    @Getter @Setter private Personal personal;
    @Getter @Setter private String prodAcad;
}
