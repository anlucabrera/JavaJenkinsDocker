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
@ToString(of = "dtoExpedientesActuales")
@EqualsAndHashCode
public class DtoExpedientesActuales1 implements Serializable{

    private static final long serialVersionUID = 1904185014812357415L;
    @Getter @Setter private String matricula;
    @Getter @Setter private String nombre;
    @Getter @Setter private String programa;
    @Getter @Setter private Integer expediente;
}