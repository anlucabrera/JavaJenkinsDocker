/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author UTXJ
 */

@EqualsAndHashCode(of = {"matricula"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class ListaEstudiantesDtoTutor {
    @Getter @Setter @NotNull private Integer matricula;
    @Getter @Setter private String nombre;
    @Getter @Setter private Integer cuatrimestre;
    @Getter @Setter private String grupo;
    @Getter @Setter private String carrera;  
}
