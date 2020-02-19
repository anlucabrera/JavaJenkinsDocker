package mx.edu.utxj.pye.sgi.enums.converter;

import java.util.stream.Stream;

/**
 * Representa al convertidor de valores en cadena de texto obtenidos desde la base de datos a través de un campo de tipo enum para convertirlo al ENUM en java
 * @param <T> Tipo del enum en java al que se convierte
 */
public abstract class AbstractEnumConverter<T> implements EnumConverter<T> {
    T t;

    /**
     * Realiza una conversión genérica a cualquier enum que extienda ésta clase abstracta para evitar que se tenga que repetir la línea de conversión en todos los convertidores.
     * @param value
     * @return
     */
    @Override
    public T convert(String value) {
        return Stream.of(getValues()).filter(e -> getLabel(e).equalsIgnoreCase(value.trim())).findFirst().orElse(null);
    }

    /**
     * Provee el array de todos los posibles valores del enum en java
     * @return Arreglo de valores.
     */
    public abstract T[] getValues();

    /**
     * Provee el valor en texto del enum que se almacena en la base de datos.
     * @param t Enum de java del que se desea conocer su valor en texto.
     * @return Valor en cadena de texto del enum.
     */
    public abstract String getLabel(T t);
}
