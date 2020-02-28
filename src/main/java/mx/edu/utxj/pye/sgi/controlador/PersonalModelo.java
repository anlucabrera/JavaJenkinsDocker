/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.controlador;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.enterprise.inject.Model;
import mx.edu.utxj.pye.sgi.datamodel.ch.PersonalData;
import mx.edu.utxj.pye.sgi.datamodel.ch.PersonalDataModel;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;

/**
 *
 * @author UTXJ
 */
@Model
public class PersonalModelo {
    public PersonalDataModel getPersonalData(){
        List<Personal> data = IntStream.range(0, 500)
                .mapToObj(i -> new Personal())
                .collect(Collectors.toList());
        
        return new PersonalDataModel(new PersonalData(data));
    }
}
