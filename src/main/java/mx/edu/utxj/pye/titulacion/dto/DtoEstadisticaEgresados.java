/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
/**
 *
 * @author UTXJ
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = "dtoEstadisticaEgresados")
@EqualsAndHashCode
public class DtoEstadisticaEgresados implements Serializable{

    private static final long serialVersionUID = 3068807273104161311L;
    @Getter @Setter private String generacion;
    @Getter @Setter private String programa;
    @Getter @Setter private String periodo;
    @Getter @Setter private Short cuatrimestre;
    @Getter @Setter private Integer inscritos;
    @Getter @Setter private Integer egresados;
    @Getter @Setter private Integer acredEstadia;
    @Getter @Setter private Integer intExp;
    @Getter @Setter private Integer expVal;
    @Getter @Setter private List<MatriculaPeriodosEscolares> listaInscritos;
    
}