/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.SeguroFacultativoValidacion;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SeguroFacultativoValidacionConverter extends AbstractEnumConverter<SeguroFacultativoValidacion>{

    @Override
    public SeguroFacultativoValidacion[] getValues() {
        return SeguroFacultativoValidacion.values();
    }

    @Override
    public String getLabel(SeguroFacultativoValidacion seguroFacultativoValidacion) {
        return seguroFacultativoValidacion.getLabel();
    }
    
    public static SeguroFacultativoValidacion of(String value){
        return (new SeguroFacultativoValidacionConverter().convert(value));
    }
    
}
