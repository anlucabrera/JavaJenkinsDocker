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
@ToString(of = "dtoEstudianteParaReporte")
@EqualsAndHashCode
public class DtoEstudianteParaReporte implements Serializable{
    
    private static final long serialVersionUID = -2350761936753405071L;
    @Getter @Setter private String generacion;
    @Getter @Setter private String progEducativo;
    @Getter @Setter private String matricula;
    @Getter @Setter private String nombre;
    @Getter @Setter private String genero;
    @Getter @Setter private String status;
    @Getter @Setter private String expValidado;
    
}
