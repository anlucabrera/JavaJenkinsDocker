package mx.edu.utxj.pye.sgi.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa el resultado de un proceso EJB especificando:<br/>
 * El mensaje (será vacío si el resultado es correcto.
 * la excepción que se lanzó (será nula si el resultado es correcto),
 * una clave númerica para representar el resultado (será cero si el resultado es correcto y mayor que cero si hay error),
 */
public class ResultadoEJB<T> implements Serializable {
    @Getter @Setter @NonNull private T valor;
    @Getter @Setter @NonNull private String mensaje;
    @Getter @Setter private Throwable exception;
    @Getter @Setter @NonNull private Integer resultado;
    @Getter @Setter private Boolean correcto;
    @Getter private Class tipo;
    @Getter private LocalDateTime momento;
    @Getter private List<ResultadoEJB> resultados = new ArrayList<>();

    private ResultadoEJB(T valor, String mensaje, Throwable exception, Integer resultado) {
        this.valor = valor;
        this.mensaje = mensaje;
        this.exception = exception;
        this.resultado = resultado;
        this.correcto = false;
    }

    @PostConstruct
    public void init(){
        momento = LocalDateTime.now();
        mensaje = mensaje.trim();
        if (resultado < 0) {
            resultado = 0;
        }
        correcto = resultado == 0;
        if (exception != null) {
            tipo = exception.getClass();
        }
    }

    /**
     * Crea una instancia de resultado correcto de proceso EJB.
     * @param t Objeto que representa el valor del resultado.
     * @param <T> Tipo del resultado.
     * @return Instancia de resultado correcto.
     */
    public static <T> ResultadoEJB<T> crearCorrecto(T t, String mensaje){
        ResultadoEJB<T> res = new ResultadoEJB<T>(t, mensaje,null,0);
        res.setCorrecto(Boolean.TRUE);
        return res;
    }

    /**
     * Crea una instancia de resultado erróneo de proceso EJB
     * @param t Resultado alternativo al esperado.
     * @param mensaje Mensaje del error.
     * @param <T> Tipo del resultado
     * @return Instancia de resultado erróneo.
     */
    public static <T> ResultadoEJB<T> crearErroneo(Integer resultado, T t, String mensaje){
        ResultadoEJB<T> res = new ResultadoEJB<T>(t, mensaje,null, resultado);
        res.setCorrecto(Boolean.FALSE);
        return res;
    }

    /**
     *Crea una instancia de resultado erróneo sin excepción.
     * @param resultado Valor del resultado, debe ser mayor que 0.
     * @param mensaje Mensaje del error.
     * @param tipo Tipo el objeto que debería ser el resultado.
     * @param <T> Tipo del resultado
     * @return Instancia de resultado erróneo sin excepción.
     */
    public static <T> ResultadoEJB<T> crearErroneo(Integer resultado, String mensaje, Class<T> tipo){
        return crearErroneo(resultado,mensaje,null,tipo);
    }

    /**
     * Crea una instancia de resultado erróneo con excepción.
     * @param resultado Valor del resultado, debe ser mayor que 0.
     * @param mensaje Mensaje del error.
     * @param ex Excepción que se ha lanzando.
     * @param tipo Tipo del objeto que debería ser el resultado.
     * @param <T> Tipo del resultado
     * @return Intancia del resultado erróneo con excepción.
     */
    public static <T> ResultadoEJB<T> crearErroneo(Integer resultado, String mensaje, Throwable ex, Class<T> tipo){
        try {
            if(resultado <= 0){
                resultado = 1;
            }
            if(ex != null){
                if (mensaje == null) {
                    mensaje = "";
                }
                System.out.println("resultado = [" + resultado + "], mensaje = [" + mensaje + "], ex = [" + ex + "], tipo = [" + tipo + "]");
                mensaje = mensaje.concat(" ").concat(getExceptionMessage(ex));
            }
            T t = null;                    ;
            return new ResultadoEJB<T>(t,mensaje,ex,resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getExceptionMessage(Throwable ex){
        if(ex == null){
            return "Excepción nula.";
        }

        if(ex instanceof NullPointerException){
            return "Objeto nulo.";
        }

        return ex.getCause()!=null?ex.getCause().getMessage():ex.getMessage();
    }
}
