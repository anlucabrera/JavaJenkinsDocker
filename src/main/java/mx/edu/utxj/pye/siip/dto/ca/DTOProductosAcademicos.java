/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.ca;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.ProductosAcademicos;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "productosAcademicos")
@EqualsAndHashCode
public class DTOProductosAcademicos implements Serializable{
    
    private static final long serialVersionUID = 5537023223713334899L;
    @Getter @Setter private @NonNull ProductosAcademicos productosAcademicos;
    @Getter @Setter private AreasUniversidad areaUniversidad;
    @Getter @Setter private String prodAcad;
    @Getter @Setter private ActividadesPoa actividadAlineada;
    
    public DTOProductosAcademicos(ProductosAcademicos productoAcademico, AreasUniversidad areaUniversidad){
        this.productosAcademicos = productoAcademico;
        this.areaUniversidad = areaUniversidad;
    }

}
