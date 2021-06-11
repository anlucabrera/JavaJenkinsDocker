/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import java.util.List;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.logueo.Areas;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.ListaUsuarioClaveNomina;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

import javax.validation.constraints.NotNull;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author Planeación
 */
@EqualsAndHashCode(of = {"matricula"}) @RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class DtoAlumnosEncuesta {
    @Getter @Setter
    @NotNull private String matricula;
    @Getter
    @Setter
    private Integer periodo;
    @Getter
    @Setter
    private String nombre;
    @Getter
    @Setter
    private String primerAp;
    @Getter
    @Setter
    private String segundoAp;
    @Getter
    @Setter
    private String siglas;
    @Getter
    @Setter
    private Short grado;
    @Getter
    @Setter
    private String grupo;
    @Getter
    @Setter
    private Integer cveStatus;
    @Getter
    @Setter
    private Integer cvetutor;
    @Getter
    @Setter
    private String nombreTutor;
    @Getter
    @Setter
    private String primerApTutor;
    @Getter
    @Setter
    private String segundoApTutor;
    @Getter
    @Setter
    private String cveDirector;
    
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoAlumnos{
        @Getter @Setter private Alumnos alumnos;
        @Getter @Setter private Personas personas;
        @Getter @Setter private DtoAlumnosEncuesta.DtoCarrera programaEdcuativo;
    }
    
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoTutores{
        @Getter
        @Setter
        private Integer cveTutor;
        @Getter
        @Setter
        private String nombre;
        @Getter
        @Setter
        private String primerAp;
        @Getter
        @Setter
        private String segundoAp;
    }

    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoAlumnosEncuestaGeneral{
        @Getter @Setter private Alumnos alumnos;
        @Getter @Setter private Personas personas;
        @Getter @Setter private Grupos grupos;
        @Getter @Setter private DtoCarrera carrerasCgut;
        @Getter @Setter private Personas tutor;
        @Getter @Setter private ListaUsuarioClaveNomina dtoDirector;
        @Getter @Setter private String siglas;
        @Getter @Setter private String grupo;
        @Getter @Setter private Short grado;
    }

    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoAlumnosEncuestaGeneralControlEscolar{
        @Getter @Setter private Estudiante alumnos;
        @Getter @Setter private Persona personas;
        @Getter @Setter private Grupo grupos;
        @Getter @Setter private AreasUniversidad areasUniversidad;
        @Getter @Setter private Personal tutor;
        @Getter @Setter private ListaUsuarioClaveNomina dtoDirector;
        @Getter @Setter private String siglas;
        @Getter @Setter private String grupo;
        @Getter @Setter private Short grado;
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoCarrera{
        @Getter
        @Setter
        private Integer cveCarrera;
        @Getter
        @Setter
        private String nombre;
        @Getter
        @Setter
        private String abreviatura;
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoDirectores{
        @Getter @Setter private String nombre;
        @Getter @Setter private String siglas;
        @Getter
        @Setter
        private Integer claveDirector;
        @Getter
        @Setter
        private String nombreDirector;
    }

    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DtoEvaluaciones{
        @Getter @Setter private Integer clave;
        @Getter @Setter private String nombreCompleto;
        @Getter @Setter private String areaOprogramaEducativo;
    }


    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoAlumnosEncuestaSaiiutyCE{
        @Getter @Setter private Integer matricula;
        @Getter @Setter private String nombre;
        @Getter @Setter private Short grado;
        @Getter @Setter private String siglas;
        @Getter @Setter private String grupo;
        @Getter @Setter private String tutor;
        @Getter @Setter private Integer cveMaestro;
        @Getter @Setter private Integer cveDirector;

    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoAlumnosTest{
        @Getter @Setter private Integer matricula;
        @Getter @Setter private String nombre;
        @Getter @Setter private Short grado;
        @Getter @Setter private String grupo;
        @Getter @Setter private String siglas;
    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoGruposTutor{
        @Getter @Setter private Integer idGrupo;
        @Getter @Setter private PeriodosEscolares periodo;
        @Getter @Setter private Integer grado;
        @Getter @Setter private String literal;
        @Getter @Setter private Integer responsable;
    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoResultadoTestEstudiante{
        @Getter @Setter private Integer matricula;
        @Getter @Setter private List<Integer> listaV;
        @Getter @Setter private List<Integer> listaA;
        @Getter @Setter private List<Integer> listaK;
    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoResultadoTestEstudiantePorcentaje{
        @Getter @Setter private Integer matricula;
        @Getter @Setter private Double pV;
        @Getter @Setter private Double pA;
        @Getter @Setter private Double pK;
    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString @EqualsAndHashCode
    public static class DtoProgramaEducativo{
        @Getter @Setter private Short area;
        @Getter @Setter private String nombre;
        @Getter @Setter private String siglas;
        @Getter @Setter private String nivelEducativo;
        @Getter @Setter private String bd;
    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoPeriodoEscolar{
        @Getter @Setter private Integer periodo;
        @Getter @Setter private String periodoEsolar;
        @Getter @Setter private String anio;
        @Getter @Setter private Integer ciclo;
    }
    
    @RequiredArgsConstructor @AllArgsConstructor @ToString
    public static class DtoAvaceTestProgramaEducativo{
        @Getter @Setter private String programa;
        @Getter @Setter private Integer totalMatricula;
        @Getter @Setter private Integer totalCompletos;
        @Getter @Setter private Integer totalIncompleto;
        @Getter @Setter private Double porcentaje;
    }
    
    
}
