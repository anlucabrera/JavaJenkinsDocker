package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.NoAdeudoEstudiante;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Viewregalumnosnoadeudo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.pye2.BecasPeriodosEscolares;

import java.util.List;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public  class DtoNoAdeudoEstudiante {

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class  NoAdeudoEstudianteGeneral{
        @Getter     @Setter     @NonNull Estudiante estudiante;
        @Getter     @Setter     @NonNull AreasUniversidad pe;
        @Getter     @Setter     @NonNull    Boolean acreditadoEstadia;
        @Getter     @Setter     @NonNull    Boolean  aceditadoVinculacion;
        @Getter     @Setter     @NonNull Generaciones generacion;
        @Getter     @Setter     @NonNull    Boolean estadia;
        @Getter     @Setter     @NonNull    DireccionCarrera direccionCarrera;
        @Getter     @Setter     @NonNull    Biblioteca biblioteca;
        @Getter     @Setter     @NonNull    IyE iye;
        @Getter     @Setter     @NonNull    CordinacionEstadia cordinacionEstadia;
        @Getter     @Setter     @NonNull    SeguimientoEgresados seguimientoEgresados;
        @Getter     @Setter     @NonNull    ServiciosMateriales serviciosMateriales;
        @Getter     @Setter     @NonNull    ServiciosEscolares servciosEscolares;
        @Getter     @Setter     @NonNull    Titulacion titulacion;
        @Getter     @Setter     @NonNull    Finanzas finanzas;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static class DireccionCarrera{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante adeudoDirrecionCarrera;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class Biblioteca{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoBiblioteca;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    Boolean pagoDonacionLibro;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;

    }
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class IyE{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoIyE;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    Boolean encuestaSatisfaccionIng;
        @Getter     @Setter     @NonNull    Boolean encuestaSatisfaccionTsu;
        @Getter     @Setter     @NonNull    Boolean encuestaServicios;
        @Getter     @Setter     @NonNull    Boolean evaluacionEstadia;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;

    }
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class CordinacionEstadia{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoEstadia;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;


    }
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class SeguimientoEgresados{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoEgresados;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    Boolean estudioEgresados;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;


    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class ServiciosMateriales{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoServiciosMateriales;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    Boolean becado;
        @Getter     @Setter     @NonNull    List<BecasPeriodosEscolares> becas;
        @Getter     @Setter     @NonNull    Integer totalBecas;
        @Getter     @Setter     @NonNull    Integer totalHorasServicio;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;

    }
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class ServiciosEscolares{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoServiciosEscolares;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    Boolean entregoFotografias;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;

    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class Titulacion{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoTitulacion;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    Boolean integroExpediente;
        @Getter     @Setter     @NonNull    Boolean expedienteValidado;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;

    }
    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class Finanzas{
        @Getter     @Setter     @NonNull NoAdeudoEstudiante aduedoFinanzas;
        @Getter     @Setter     @NonNull DtoDomicilio domicilio;
        @Getter     @Setter     @NonNull Personal personalReviso;
        @Getter     @Setter     @NonNull    Boolean liberado;
        @Getter     @Setter     @NonNull    List<Viewregalumnosnoadeudo> pagos;
        @Getter     @Setter     @NonNull    Boolean liberoTitulacion;
        @Getter     @Setter     @NonNull    Boolean pagoLibro;
        @Getter     @Setter     @NonNull    String statusRev;
        @Getter     @Setter     @NonNull    String correo;

    }

}
