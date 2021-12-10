/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.SegurosFacultativosEstatus;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SegurosFacultativosEstatusConverter extends AbstractEnumConverter<SegurosFacultativosEstatus>{

    @Override
    public SegurosFacultativosEstatus[] getValues() {
        return SegurosFacultativosEstatus.values();
    }

    @Override
    public String getLabel(SegurosFacultativosEstatus seguroFacultativoEstatus) {
        return seguroFacultativoEstatus.getLabel();
    }
    
    public static SegurosFacultativosEstatus of(String value){
        return (new SegurosFacultativosEstatusConverter().convert(value));
    }
    
}
