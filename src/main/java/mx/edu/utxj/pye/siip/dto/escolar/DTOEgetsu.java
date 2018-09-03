/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.dto.escolar;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.EgetsuResultadosGeneraciones;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOEgetsu implements Serializable{
    
     private static final long serialVersionUID = 2701990608371591903L;
    
    @Getter @Setter private String generacion;
    @Getter @Setter private EgetsuResultadosGeneraciones egetsuResultadosGeneraciones;
}
