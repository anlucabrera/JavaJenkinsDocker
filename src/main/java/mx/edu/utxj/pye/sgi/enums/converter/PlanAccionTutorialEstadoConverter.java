/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.PlanAccionTutorialEstado;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlanAccionTutorialEstadoConverter extends AbstractEnumConverter<PlanAccionTutorialEstado>{

    @Override
    public PlanAccionTutorialEstado[] getValues() {
        return PlanAccionTutorialEstado.values();
    }

    @Override
    public String getLabel(PlanAccionTutorialEstado planAccionTutorialEstado) {
        return planAccionTutorialEstado.getLabel();
    }
    
    public static PlanAccionTutorialEstado of(String value){
        return (new PlanAccionTutorialEstadoConverter().convert(value));
    }
    
}
