package mx.edu.utxj.pye.sgi.dto.controlEscolar;


import java.util.List;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionPromedio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosFamiliares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documentosentregadosestudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Escolaridad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EspecialidadCentro;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.LenguaIndigena;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Ocupacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Sistema;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutorFamiliar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.enums.Operacion;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoReincorporacion {
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class General {
        @Getter        @Setter        @NonNull       PersonaR pr; 
        @Getter        @Setter        @NonNull       AspiranteR ar; 
        @Getter        @Setter        @NonNull       MedicosR mr; 
        @Getter        @Setter        @NonNull       FamiliaresR fr; 
        @Getter        @Setter        @NonNull       TutorR tr; 
        @Getter        @Setter        @NonNull       DomicilioR dr; 
        @Getter        @Setter        @NonNull       AcademicosR ac; 
        @Getter        @Setter        @NonNull       EncuestaR er;     
        @Getter        @Setter        @NonNull       List<EstudianteR> ers;    
        @Getter        @Setter        @NonNull       List<AlineacionCalificaciones> acs;        
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class PersonaR {
        @Getter        @Setter        @NonNull        Persona persona;
        @Getter        @Setter        @NonNull        MedioComunicacion medioComunicacion;
        @Getter        @Setter        @NonNull        Pais paisOr;
        @Getter        @Setter        @NonNull        Operacion operacionGeneral;
        @Getter        @Setter        @NonNull        Operacion operacionMC;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class AspiranteR {
        @Getter        @Setter        @NonNull        Aspirante aspirante;
        @Getter        @Setter        @NonNull        TipoAspirante tipo;
        @Getter        @Setter        @NonNull        ProcesosInscripcion procesosInscripcion;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class MedicosR {
        @Getter        @Setter        @NonNull        DatosMedicos datosMedicos;
        @Getter        @Setter        @NonNull        TipoSangre sangre;
        @Getter        @Setter        @NonNull        TipoDiscapacidad discapacidad;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class TutorR {
        @Getter        @Setter        @NonNull        TutorFamiliar tutorFamiliar;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class FamiliaresR {
        @Getter        @Setter        @NonNull        DatosFamiliares datosFamiliares;
        @Getter        @Setter        @NonNull        TutorR tutorR ;
        @Getter        @Setter        @NonNull        Ocupacion ocupacionP;
        @Getter        @Setter        @NonNull        Ocupacion ocupacionM;
        @Getter        @Setter        @NonNull        Escolaridad escolaridadP;
        @Getter        @Setter        @NonNull        Escolaridad escolaridadM;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class DomicilioR {
        @Getter        @Setter        @NonNull        Domicilio domicilio;
        @Getter        @Setter        @NonNull        Boolean igualD;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class AcademicosR {
        @Getter        @Setter        @NonNull        DatosAcademicos academicos;
        @Getter        @Setter        @NonNull        AreasUniversidad universidad1;
        @Getter        @Setter        @NonNull        AreasUniversidad universidad2;
        @Getter        @Setter        @NonNull        Sistema sistemaPo;
        @Getter        @Setter        @NonNull        Sistema sistemaSo;
        @Getter        @Setter        @NonNull        Estado estado;
        @Getter        @Setter        @NonNull        Municipio municipio;
        @Getter        @Setter        @NonNull        Localidad localidad;
        @Getter        @Setter        @NonNull        Iems iems;
        @Getter        @Setter        @NonNull        EspecialidadCentro especialidad;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class EncuestaR {
        @Getter        @Setter        @NonNull        EncuestaAspirante encuestaAspirante;
        @Getter        @Setter        @NonNull        LenguaIndigena lenguaIndigena;
        @Getter        @Setter        @NonNull        MedioDifusion medioDifusion;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull        Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class EstudianteR {
        @Getter        @Setter        @NonNull        Estudiante estudiante;
        @Getter        @Setter        @NonNull        Documentosentregadosestudiante documentosentregadosestudiante;
        @Getter        @Setter        @NonNull        String nombrePe;
        @Getter        @Setter        @NonNull        TipoEstudiante tipoEstudiante;
        @Getter        @Setter        @NonNull        Grupo grupo;
        @Getter        @Setter        @NonNull        Boolean primeraOpcion; 
        @Getter        @Setter        @NonNull        Boolean editable; 
        @Getter        @Setter        @NonNull        Operacion operacion;  
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class AlineacionCalificaciones {
        @Getter        @Setter        @NonNull        Estudiante estudiante;
        @Getter        @Setter        @NonNull        Grupo grupo;
        @Getter        @Setter        @NonNull        List<CalificacionesR> calificacionesReincorporacions;        
        @Getter        @Setter        @NonNull        Boolean puedeValidar; 
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull        Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class CalificacionesR {        
        @Getter        @Setter        @NonNull        CargaAcademica academica;        
        @Getter        @Setter        @NonNull        PlanEstudioMateria estudioMateria;
        @Getter        @Setter        @NonNull        Materia materia;
        @Getter        @Setter        @NonNull        Personal docente;
        @Getter        @Setter        @NonNull        CalificacionPromedio calificacionPromedio;
        @Getter        @Setter        @NonNull        Boolean editable;             
        @Getter        @Setter        @NonNull        Boolean ordinaria;         
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull        Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class ProcesoInscripcionRein {        
        @Getter        @Setter        @NonNull        String primeraOp;  
        @Getter        @Setter        @NonNull        String SegundaOP;  
        @Getter        @Setter        @NonNull        Integer matricula;  
        @Getter        @Setter        @NonNull        Boolean opcionIncripcion;
        @Getter        @Setter        @NonNull        Integer trabajadorInscribe;
        @Getter        @Setter        @NonNull        String tipoRegistro;  
        @Getter        @Setter        @NonNull        List<Grupo> grupos;
        @Getter        @Setter        @NonNull        Documentosentregadosestudiante documentosentregadosestudiante; 
    }
}