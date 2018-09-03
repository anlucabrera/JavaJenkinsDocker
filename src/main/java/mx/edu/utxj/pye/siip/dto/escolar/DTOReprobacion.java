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
import mx.edu.utxj.pye.sgi.entity.prontuario.Materias;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.pye2.DesercionReprobacionMaterias;

/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DTOReprobacion implements Serializable{

    private static final long serialVersionUID = -5162564017256453585L;
    @Getter @Setter private String matricula;
    @Getter @Setter private Materias materias;
    @Getter @Setter private Personal personal;
    @Getter @Setter private DesercionReprobacionMaterias desercionReprobacionMaterias;
}