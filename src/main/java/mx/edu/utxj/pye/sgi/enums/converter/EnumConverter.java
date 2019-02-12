package mx.edu.utxj.pye.sgi.enums.converter;

import java.io.Serializable;
import java.util.stream.Stream;

/**
 * Representa la acción de conversión de un enum
 * @param <T>
 */
@FunctionalInterface
public interface EnumConverter<T> extends Serializable {
    public T convert(String value);

    /*public static <T> T convert(String value, T[] values){
        return Stream.of(values).filter(e -> ((EnumConverter)e).getLabel().equalsIgnoreCase(value.trim())).findFirst().orElse(null);
    }*/

    /*public static <T> T of(String value){
        return convert(value);
    }*/
}
