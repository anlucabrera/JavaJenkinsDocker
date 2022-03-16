package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;

import java.io.Serializable;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaEmpleadores;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaSeguimientoEgresados;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.CarrerasCgut;
import mx.edu.utxj.pye.sgi.saiiut.entity.Grupos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Periodos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

/**
 * Representa a un Estudiante activo
 */

public class DtoEstudianteEncuesta implements Serializable {
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "estudiante")
    public static class DtoEstudianteControlEscolar{
        @Getter @Setter @NonNull private Estudiante estudiante;
        @Getter @Setter @NonNull private Grupo grupo;
        @Getter @Setter @NonNull private PeriodosEscolares periodo;
        @Getter @Setter @NonNull private Generaciones generacion;
        @Getter @Setter @NonNull private Persona persona;
        @Getter @Setter @NonNull private Aspirante aspirante;

        public DtoEstudianteControlEscolar(Estudiante estudiante) {
            this.estudiante = estudiante;
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "alumno")
    public static class DtoEstudianteSaiiut{
        @Getter @Setter @NonNull private Alumnos alumno;
        @Getter @Setter @NonNull private Grupos grupo;
        @Getter @Setter @NonNull private CarrerasCgut carrera;
        @Getter @Setter @NonNull private Personas persona;
        
        public DtoEstudianteSaiiut(Alumnos alumno) {
            this.alumno = alumno;
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "matricula")
    public static class DtoEstudianteInformacion{
        @Getter @Setter @NonNull private Integer matricula;
        @Getter @Setter @NonNull private String nombre;
        @Getter @Setter @NonNull private String primerAp;
        @Getter @Setter @NonNull private String segundoAp;
        @Getter @Setter @NonNull private String grupo;
        @Getter @Setter @NonNull private Short grado;
        @Getter @Setter @NonNull private String carrera;

        public DtoEstudianteInformacion() {
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "periodo")
    public static class DtoGeneraciones{
        @Getter @Setter @NonNull private Integer periodo;
        @Getter @Setter @NonNull private Integer anio;
        @Getter @Setter @NonNull private String nivel;

        public DtoGeneraciones() {
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "matricula")
    public static class DtoEstudiante{
        @Getter @Setter @NonNull private Integer matricula;
        @Getter @Setter @NonNull private String nombre;
        @Getter @Setter @NonNull private Short grado;
        @Getter @Setter @NonNull private String grupo;
        @Getter @Setter @NonNull private String carrera;
        @Getter @Setter @NonNull private Integer periodo;
        @Getter @Setter @NonNull private String ubicacion;

        public DtoEstudiante() {
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "evaluador")
    public static class DtoInformacionResultados{
        @Getter @Setter @NonNull private Integer evaluador;
        @Getter @Setter @NonNull private DtoEstudiante dtoEstudiante;
        @Getter @Setter @NonNull private EncuestaSeguimientoEgresados encuesta;

        public DtoInformacionResultados() {
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "evaluador")
    public static class DtoInformacionResultados2{
        @Getter @Setter @NonNull private Integer evaluador;
        @Getter @Setter @NonNull private EncuestaEmpleadores encuesta;

        public DtoInformacionResultados2() {
        }
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode(of = "matricula")
    public static class DtoInformacionExcel{
        @Getter @Setter @NonNull private Integer matricula;
        @Getter @Setter @NonNull private DtoEstudiante dtoEstudiante;
        @Getter @Setter @NonNull private String r1;
        @Getter @Setter @NonNull private String r2;
        @Getter @Setter @NonNull private String r3;
        @Getter @Setter @NonNull private String r4;
        @Getter @Setter @NonNull private String r5;
        @Getter @Setter @NonNull private String r6;
        @Getter @Setter @NonNull private String r7;
        @Getter @Setter @NonNull private String r8;
        @Getter @Setter @NonNull private String r9;
        @Getter @Setter @NonNull private String r10;
        @Getter @Setter @NonNull private String r11;
        @Getter @Setter @NonNull private String r12;
        @Getter @Setter @NonNull private String r13;
        @Getter @Setter @NonNull private String r14;
        @Getter @Setter @NonNull private String r15;
        @Getter @Setter @NonNull private String r16;
        @Getter @Setter @NonNull private String r17;
        @Getter @Setter @NonNull private String r18;
        @Getter @Setter @NonNull private String r19;
        @Getter @Setter @NonNull private String r20;
        @Getter @Setter @NonNull private String r21;
        @Getter @Setter @NonNull private String r22;
        @Getter @Setter @NonNull private String r23;
        @Getter @Setter @NonNull private String r24;
        @Getter @Setter @NonNull private String r25;
        @Getter @Setter @NonNull private String r26;
        @Getter @Setter @NonNull private String r27;
        @Getter @Setter @NonNull private String r28;
        @Getter @Setter @NonNull private String r29;
        @Getter @Setter @NonNull private String r30;
        @Getter @Setter @NonNull private String r31;
        @Getter @Setter @NonNull private String r32;
        @Getter @Setter @NonNull private String r33;
        @Getter @Setter @NonNull private String r34;
        @Getter @Setter @NonNull private String r35;
        @Getter @Setter @NonNull private String r36;
        @Getter @Setter @NonNull private String r37;
        @Getter @Setter @NonNull private String r38;
        @Getter @Setter @NonNull private String r39;
        @Getter @Setter @NonNull private String r40;
        @Getter @Setter @NonNull private String r41;
        @Getter @Setter @NonNull private String r42;
        @Getter @Setter @NonNull private String r43;
        @Getter @Setter @NonNull private String r44;
        @Getter @Setter @NonNull private String r45;
        @Getter @Setter @NonNull private String r46;
        @Getter @Setter @NonNull private String r47;
        @Getter @Setter @NonNull private String r48;
        @Getter @Setter @NonNull private String r49;

        public DtoInformacionExcel() {
        }
    }
}
