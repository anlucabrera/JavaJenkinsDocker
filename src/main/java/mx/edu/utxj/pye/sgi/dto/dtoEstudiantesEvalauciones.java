/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.entity.ch.EstudiantesClaves;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.MatriculaPeriodosEscolares;
import mx.edu.utxj.pye.sgi.saiiut.entity.AlumnosEvaluacionTutor;

import java.util.List;

/**
 *Dto para identificar en las evaluaciones (Tutor- Docente) si estan registrados en Sauiit o en Control Escolar
 * @author Taatisz
 */
public class dtoEstudiantesEvalauciones {
    @Getter @Setter EstudiantesClaves estudiantesClaves;
    //Representa el registro por periodo
    @Getter  @Setter MatriculaPeriodosEscolares matriculaPeriodosEscolares;
    //Representa si es un estudiante registrado en Control Escolar
    @Getter @Setter Estudiante estudianteCE;
    //Representa si es un estudiante registrado en Sauiit
    @Getter @Setter AlumnosEvaluacionTutor estudianteSaiiut;
    //Tutor
    @Getter @Setter Personal tutor;
    //Director
    @Getter @Setter Personal director;
    //Carrera
    @Getter @Setter AreasUniversidad carrera;
    //Datos del estudiante
    @Getter @Setter String matricula;
    @Getter @Setter int registro;
    @Getter @Setter int claveEstudiante;
    @Getter @Setter String nombreCEstudiante;
    @Getter @Setter int grado;
    @Getter @Setter String grupo;
    @Getter @Setter int clavePE;
    @Getter @Setter String nombrePE;
    @Getter @Setter int claveTutor;
    @Getter @Setter String nombreTutor;
    @Getter @Setter int claveDirector;
    public dtoEstudiantesEvalauciones() {
    }

    @Override
    public String toString() {
        return "dtoEstudiantesEvalauciones{" + "matricula=" + matricula + ", registro=" + registro + ", claveEstudiante=" + claveEstudiante + ", nombreCEstudiante=" + nombreCEstudiante + ", grado=" + grado + ", grupo=" + grupo + ", clavePE=" + clavePE + ", nombrePE=" + nombrePE + ", claveTutor=" + claveTutor + ", nombreTutor=" + nombreTutor + ", claveDirector=" + claveDirector + '}';
    }

    
    
    public dtoEstudiantesEvalauciones(String matricula, int registro, int claveEstudiante, String nombreCEstudiante, int grado, String grupo, int clavePE, String nombrePE, int claveTutor, String nombreTutor, int claveDirector) {
        this.matricula = matricula;
        this.registro = registro;
        this.claveEstudiante = claveEstudiante;
        this.nombreCEstudiante = nombreCEstudiante;
        this.grado = grado;
        this.grupo = grupo;
        this.clavePE = clavePE;
        this.nombrePE = nombrePE;
        this.claveTutor = claveTutor;
        this.nombreTutor = nombreTutor;
        this.claveDirector = claveDirector;
    }
    
    
    
    
    
    
    
            
    
}
