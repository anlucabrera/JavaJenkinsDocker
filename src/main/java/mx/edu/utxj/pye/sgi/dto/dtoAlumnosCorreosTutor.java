/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Taatisz :)
 */
@EqualsAndHashCode(of = {"matricula"})  @ToString
public class dtoAlumnosCorreosTutor {
    @Getter @Setter Integer matricula;
    @Getter @Setter String nombre;
    @Getter @Setter Short grado;
    @Getter @Setter String grupo;
    @Getter @Setter String correoinstitucional;

    public dtoAlumnosCorreosTutor() {
    }

    public dtoAlumnosCorreosTutor(Integer matricula, String nombre, Short grado, String grupo, String correoinstitucional) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.grado = grado;
        this.grupo = grupo;
        this.correoinstitucional = correoinstitucional;
    }
    
    
    
}
