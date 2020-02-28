package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CargaAcademica;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosFamiliares;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosMedicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EncuestaAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Escolaridad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EspecialidadCentro;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Materia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Ocupacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanEstudioMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Sistema;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoAspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoSangre;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutorFamiliar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Asentamiento;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoReincorporacion {
    
    @Getter @Setter @NonNull Persona persona;
    @Getter @Setter @NonNull MedioComunicacion comunicacion;
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class aspirante{
        @Getter @Setter @NonNull Aspirante aspirante;
        @Getter @Setter @NonNull TipoAspirante tipo;
        @Getter @Setter @NonNull ProcesosInscripcion inscripcion;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class medicos{
        @Getter @Setter @NonNull DatosMedicos periodo;
        @Getter @Setter @NonNull TipoSangre sangre;
        @Getter @Setter @NonNull TipoDiscapacidad discapacidad;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class familiares{
        @Getter @Setter @NonNull DatosFamiliares periodo;
        @Getter @Setter @NonNull tutor tutorF;
        @Getter @Setter @NonNull Ocupacion ocupacionP;
        @Getter @Setter @NonNull Ocupacion ocupacionM;
        @Getter @Setter @NonNull Escolaridad escolaridadP;
        @Getter @Setter @NonNull Escolaridad escolaridadM;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class domicilio{
        @Getter @Setter @NonNull Domicilio domicilio;
        @Getter @Setter @NonNull Estado estadoR;
        @Getter @Setter @NonNull Municipio municipioR;
        @Getter @Setter @NonNull Asentamiento asentamientoR;
        @Getter @Setter @NonNull Estado estadoP;
        @Getter @Setter @NonNull Municipio municipioP;
        @Getter @Setter @NonNull Asentamiento asentamientoP;
    }
  
      
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class academicos{
        @Getter @Setter @NonNull DatosAcademicos academicos;
        @Getter @Setter @NonNull AreasUniversidad universidad1;
        @Getter @Setter @NonNull AreasUniversidad universidadPo;
        @Getter @Setter @NonNull Sistema sistemaPo;
        @Getter @Setter @NonNull AreasUniversidad universidad2;
        @Getter @Setter @NonNull AreasUniversidad universidadSo;
        @Getter @Setter @NonNull Sistema sistemaSo;
        
        @Getter @Setter @NonNull Estado estado;
        @Getter @Setter @NonNull Municipio municipio;
        @Getter @Setter @NonNull Localidad localidad;
        @Getter @Setter @NonNull Iems iems;
        @Getter @Setter @NonNull EspecialidadCentro especialidad;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class encuesta{
        @Getter @Setter @NonNull EncuestaAspirante periodo;
    }
    
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class tutor{
        @Getter @Setter @NonNull TutorFamiliar periodo;
    }
}
