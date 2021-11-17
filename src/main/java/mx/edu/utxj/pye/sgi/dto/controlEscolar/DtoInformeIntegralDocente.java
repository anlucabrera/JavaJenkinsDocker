package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import lombok.*;
import mx.edu.utxj.pye.sgi.entity.ch.DesempenioEvaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Evaluaciones;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;

import java.util.List;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DtoInformeIntegralDocente {

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class InformeIntegral {
        @Getter     @Setter     @NonNull Personal docente;
        @Getter     @Setter     @NonNull AreasUniversidad areaAcademica;
        @Getter     @Setter     @NonNull AreasUniversidad areaOperativa;
        @Getter     @Setter     @NonNull    Desempeño    evDesempeño;
        @Getter     @Setter     @NonNull    Pares    evPares;
        @Getter     @Setter     @NonNull    Tutor    evTutor;
        @Getter     @Setter     @NonNull    Docente    evDocente;
        @Getter     @Setter     @NonNull    List<EvaluacionIntegral>    evIntegral;
        @Getter     @Setter     @NonNull    Double    porcetanjeObtenido;

    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class  EvaluacionIntegral{

        @Getter     @Setter     @NonNull    Double valor;
        @Getter     @Setter     @NonNull    String nombreApartado;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class  Desempeño{

        @Getter     @Setter     @NonNull DesempenioEvaluaciones evaluacion;
        @Getter     @Setter     @NonNull    List<ResultadoApartado> resultados;
        @Getter     @Setter     @NonNull    Double              promedio;
        @Getter    @Setter      @NonNull    String style;

    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public  static  class Pares{
        @Getter     @Setter     @NonNull Evaluaciones evaluacion;
        @Getter     @Setter     @NonNull    List<ResultadoApartado> resultados;
        @Getter     @Setter     @NonNull    Double              promedio;
        @Getter    @Setter      @NonNull    String style;

    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public  static  class Tutor{
        @Getter     @Setter     @NonNull Evaluaciones evaluacion;
        @Getter     @Setter     @NonNull    Boolean esTutor;
        @Getter     @Setter     @NonNull    List<ResultadoApartado> resultados;
        @Getter     @Setter     @NonNull    Double              promedio;
        @Getter    @Setter      @NonNull    String style;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public  static  class Docente{
        @Getter     @Setter     @NonNull Evaluaciones evaluacion;
        @Getter     @Setter     @NonNull    List<ResultadosMateria> resultados;
        @Getter     @Setter     @NonNull    Double              promedio;
        @Getter    @Setter      @NonNull    String style;
        @Getter     @Setter     @NonNull    Boolean fueDocente;
    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public  static  class ResultadoPregunta{
        @Getter     @Setter     @NonNull    String pregunta;
        @Getter     @Setter     @NonNull    String numeroPregunta;
        @Getter     @Setter     @NonNull    double promedio;
        @Getter    @Setter      @NonNull    String style;

    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public  static  class ResultadoApartado {
        @Getter    @Setter      @NonNull    String nombreApartado;
        @Getter    @Setter      @NonNull    List<ResultadoPregunta> resultadoPreguntas;
        @Getter    @Setter      @NonNull    double promedioApartado;
        @Getter    @Setter      @NonNull    String style;

    }

    @RequiredArgsConstructor @ToString @EqualsAndHashCode
    public static  class ResultadosMateria{
        @Getter     @Setter     @NonNull    String nombreMateria;
        @Getter     @Setter     @NonNull    String claveMateria;
        @Getter     @Setter     @NonNull    Double promedioMateria;
        @Getter    @Setter      @NonNull    String style;
        @Getter     @Setter     @NonNull    List<ResultadoApartado> resultados;

    }

}
