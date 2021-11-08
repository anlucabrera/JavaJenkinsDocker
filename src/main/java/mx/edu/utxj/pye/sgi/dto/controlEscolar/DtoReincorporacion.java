package mx.edu.utxj.pye.sgi.dto.controlEscolar;


import java.util.List;
import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
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
    public static class RegistroEstudiante {     
        @Getter        @Setter        @NonNull       EstudianteR esact;    
        @Getter        @Setter        @NonNull       List<RcontactoEmergencia> res;
        @Getter        @Setter        @NonNull       AcademicosCR dac;
        @Getter        @Setter        @NonNull       RegDatosLaborales labo;
        @Getter        @Setter        @NonNull       SosioeconomicosR sr;
        @Getter        @Setter        @NonNull       VocacionalR vr;
        @Getter        @Setter        @NonNull       List<Familia> fs;
        @Getter        @Setter        @NonNull       CuestionarioPsicopedagogicoResultados cp;
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
        @Getter        @Setter        @NonNull        Ocupacion ocupacion;
        @Getter        @Setter        @NonNull        Escolaridad escolaridad;
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
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class RcontactoEmergencia {
        @Getter        @Setter        @NonNull        ContactoEmergenciasEstudiante emergenciasEstudiante;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull        Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class Familia {
        @Getter        @Setter        @NonNull        IntegrantesFamilia familia;
        @Getter        @Setter        @NonNull        Ocupacion ocupacion;
        @Getter        @Setter        @NonNull        Escolaridad escolaridad;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull        Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class AcademicosCR {
        @Getter        @Setter        @NonNull        DatosAcademicosComplementarios complementarios;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class SosioeconomicosR {
        @Getter        @Setter        @NonNull        DatosSocioeconomicos socioeconomicos;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull       Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class RegDatosLaborales {
        @Getter        @Setter        @NonNull        DatosLaborales datosLaborales;
        @Getter        @Setter        @NonNull        Operacion operacion;  
        @Getter        @Setter        @NonNull        Boolean econtrado; 
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode    
    public static class Ubicaciones {
        @Getter        @Setter        @NonNull        Pais pais;
        @Getter        @Setter        @NonNull        Estado estado;
        @Getter        @Setter        @NonNull        Municipio municipio;
        @Getter        @Setter        Localidad localidad;
        @Getter        @Setter        Asentamiento asentamiento;
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public static class VocacionalR {
        @Getter        @Setter        @NonNull        EncuestaVocacional encuestaAspirante;
        @Getter        @Setter        @NonNull        AreasUniversidad carreraSelect;
        @Getter        @Setter        @NonNull        Operacion operacion;
        @Getter        @Setter        @NonNull        Boolean econtrado;
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public static class HistorialTsu {
        @Getter        @Setter        @NonNull        Persona persona;
        @Getter        @Setter        @NonNull        Aspirante aspirante;
        @Getter        @Setter        @NonNull        Estudiante estudiante;
        @Getter        @Setter        @NonNull        UniversidadEgresoAspirante egresoAspirante;
        @Getter        @Setter        @NonNull        Boolean universidadEncontrada;
        @Getter        @Setter        @NonNull        EstudianteHistorialTsu historialTsu;
        @Getter        @Setter        @NonNull        PlanesEstudioExternos externos;
        @Getter        @Setter        @NonNull        List<CalificacionesHistorialTsuOtrosPe> calificacionesHistorialTsuOtrosPes;
        @Getter        @Setter        @NonNull        List<CalificacionesHistorialTsu> CalificacionesHistorialTsu;
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public static class PlanesDeEstudioConsulta {
        @Getter        @Setter        @NonNull        String ideCompuesto;
        @Getter        @Setter        @NonNull        Integer id;
        @Getter        @Setter        @NonNull        String nombre;
        @Getter        @Setter        @NonNull        Short anioplan;
        @Getter        @Setter        @NonNull        String tipo;
    }
    
    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public static class ReporteReincorporaciones {
        @Getter        @Setter        @NonNull        Persona persona;
        @Getter        @Setter        @NonNull        Aspirante aspirante;
        @Getter        @Setter        @NonNull        Estudiante estudiante;
        @Getter        @Setter        @NonNull        PlanEstudio planEstudio;
        @Getter        @Setter        @NonNull        Integer calificacionesRegistradas;
        @Getter        @Setter        @NonNull        Integer calificacionesEsperadas;
        @Getter        @Setter        @NonNull        String estatusUltimoregistro;
        @Getter        @Setter        @NonNull        Boolean completo;
    }
}