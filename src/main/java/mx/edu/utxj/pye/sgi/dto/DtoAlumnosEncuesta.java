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
 * @author Planeación
 */
@EqualsAndHashCode(of = {"matricula"}) @RequiredArgsConstructor @AllArgsConstructor @ToString
public class DtoAlumnosEncuesta {
    @Getter @Setter @NotNull private String matricula;
    @Getter @Setter private Integer periodo;
    @Getter @Setter private String nombre;
    @Getter @Setter private String primerAp;
    @Getter @Setter private String segundoAp;
    @Getter @Setter private String siglas;
    @Getter @Setter private Short grado;
    @Getter @Setter private Integer idgrupo;
    @Getter @Setter private String grupo;
    @Getter @Setter private Integer cveStatus;
    @Getter @Setter private Integer cvetutor;
    @Getter @Setter private String nombreTutor;
    @Getter @Setter private String primerApTutor;
    @Getter @Setter private String segundoApTutor;
    @Getter @Setter private Integer cveDirector;
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoAlumnos{
        @Getter @Setter private String matricula;
        @Getter @Setter private Integer periodo;
        @Getter @Setter private String nombre;
        @Getter @Setter private String primerAp;
        @Getter @Setter private String segundoAp;
        @Getter @Setter private String siglas;
        @Getter @Setter private Short grado;
        @Getter @Setter private Integer idgrupo;
        @Getter @Setter private String grupo;
        @Getter @Setter private Integer cveStatus;
        @Getter @Setter private Integer cveDirector;
        @Getter @Setter private Integer cveTutor;
    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoTutores{
        @Getter @Setter private Integer cveTutor;
        @Getter @Setter private String nombre;
        @Getter @Setter private String primerAp;
        @Getter @Setter private String segundoAp;
    }
    
}
