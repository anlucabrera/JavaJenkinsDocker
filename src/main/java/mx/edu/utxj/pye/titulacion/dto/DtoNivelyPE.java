/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoNivelyPE")
@EqualsAndHashCode
public class DtoNivelyPE implements Serializable{

    private static final long serialVersionUID = 2372040210974878217L;
    @Getter @Setter private Integer nivel;
    @Getter @Setter private String programa;
    
}
