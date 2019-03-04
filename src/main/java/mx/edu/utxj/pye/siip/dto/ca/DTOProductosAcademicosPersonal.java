/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicosPersonal;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "productosAcademicosPersonal")
@EqualsAndHashCode
public class DTOProductosAcademicosPersonal implements Serializable{

    private static final long serialVersionUID = 7670173433445571367L;
    @Getter @Setter @NonNull private ProductosAcademicosPersonal productoAcademicoPersonal;
    @Getter @Setter private Personal personal;
    @Getter @Setter private String prodAcad;
    @Getter @Setter private ActividadesPoa actividadAlineada;

    public DTOProductosAcademicosPersonal(ProductosAcademicosPersonal productoAcademicoPersonal, Personal personal) {
        this.productoAcademicoPersonal = productoAcademicoPersonal;
        this.personal = personal;
    }
}
