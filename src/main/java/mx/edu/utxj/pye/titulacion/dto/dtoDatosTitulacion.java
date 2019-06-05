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
@ToString(of = "dtoDatosTitulacion")
@EqualsAndHashCode
public class dtoDatosTitulacion implements Serializable{

    private static final long serialVersionUID = 5579656348472822201L;
    @Getter @Setter private String gradoAcademico;
    @Getter @Setter private String programaAcademico;
}
