package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.*;
import mx.edu.utxj.pye.sgi.enums.Operacion;

@RequiredArgsConstructor @ToString @EqualsAndHashCode
public class DtoAspirante {
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
        @Getter        @Setter        @NonNull       Boolean econtrado;

    }
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class PersonaR {
        @Getter        @Setter        @NonNull      Persona persona;
        @Getter        @Setter        @NonNull      MedioComunicacion medioComunicacion;
        @Getter        @Setter        @NonNull      Pais paisOr;
        @Getter        @Setter        @NonNull      Operacion operacionGeneral;
        @Getter        @Setter        @NonNull      Operacion operacionMC;
        @Getter        @Setter        @NonNull      Boolean econtrado;
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public static class AspiranteR {
        @Getter        @Setter        @NonNull      Aspirante aspirante;
        @Getter        @Setter        @NonNull      TipoAspirante tipo;
        @Getter        @Setter        @NonNull      ProcesosInscripcion procesosInscripcion;
        @Getter        @Setter        @NonNull      Operacion operacion;
        @Getter        @Setter        @NonNull      Boolean econtrado;
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
    public  static class AcademicosR {
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
        @Getter        @Setter        @NonNull        Boolean econtrado;
    }

    @RequiredArgsConstructor    @ToString    @EqualsAndHashCode
    public static class EncuestaR {
        @Getter        @Setter        @NonNull        EncuestaAspirante encuestaAspirante;
        @Getter        @Setter        @NonNull        LenguaIndigena lenguaIndigena;
        @Getter        @Setter        @NonNull        MedioDifusion medioDifusion;
        @Getter        @Setter        @NonNull        Operacion operacion;
        @Getter        @Setter        @NonNull        Boolean econtrado;
    }


}
