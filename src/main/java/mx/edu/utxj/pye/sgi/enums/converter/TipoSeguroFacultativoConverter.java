/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.TipoSeguroFacultativo;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TipoSeguroFacultativoConverter extends AbstractEnumConverter<TipoSeguroFacultativo>{

    @Override
    public TipoSeguroFacultativo[] getValues() {
        return TipoSeguroFacultativo.values();
    }

    @Override
    public String getLabel(TipoSeguroFacultativo tipoSeguroFacultativo) {
        return tipoSeguroFacultativo.getLabel();
    }
    
    public static TipoSeguroFacultativo of(String value){
        return (new TipoSeguroFacultativoConverter().convert(value));
    }
    
}
