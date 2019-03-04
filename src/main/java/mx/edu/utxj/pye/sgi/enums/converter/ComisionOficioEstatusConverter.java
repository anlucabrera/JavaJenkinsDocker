package mx.edu.utxj.pye.sgi.enums.converter;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import mx.edu.utxj.pye.sgi.enums.ComisionOficioEstatus;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Representa a un convertidor para el estatus del oficio de comisión y extiende a la clase abstracta que rige la conversión de enums de base de datos a java.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComisionOficioEstatusConverter extends AbstractEnumConverter<ComisionOficioEstatus> {
    /**
     * Provee el array de todos los posibles valores del enum en java
     * @return Arreglo de valores.
     */
    @Override
    public ComisionOficioEstatus[] getValues() {
        return ComisionOficioEstatus.values();
    }

    /**
     * Provee el valor en texto del enum que se almacena en la base de datos.
     * @param comisionOficioEstatus Enum de java del que se desea conocer su valor en texto.
     * @return Valor en cadena de texto del enum.
     */
    @Override
    public String getLabel(ComisionOficioEstatus comisionOficioEstatus) {
        return comisionOficioEstatus.getLabel();
    }

    /**
     * Provee acceso estático al método de conversión para un uso mas simple.
     * @param value Valor en cadena de texto del enum, se recomienda que sea obtenido desde la propiedad del entity que representa a la tabla en la base de datos.
     * @return Enum de java.
     */
    public static ComisionOficioEstatus of(String value){
        return (new ComisionOficioEstatusConverter()).convert(value);
    }
    /*public ComisionOficioEstatus convert(String valor){
        return Stream.of(ComisionOficioEstatus.values()).filter(e -> e.getLabel().equalsIgnoreCase(valor.trim())).findFirst().orElse(null);
    }

    public static ComisionOficioEstatus of(String value){
        ComisionOficioEstatusConverter converter = new ComisionOficioEstatusConverter();
        return converter.convert(value);
    }*/

    public static void main(String[] args) {
        /*ComisionOficioEstatus estatus = ComisionOficioEstatusConverter.of("Impreso_por_comisionado");
        System.out.println("estatus = " + estatus);*/

        ComisionOficioEstatus estatus = ComisionOficioEstatusConverter.of("Impreso_por_comisionado"); //(new ComisionOficioEstatusConverter()).convert("Impreso_por_comisionado"); //ComisionOficioEstatusConverter.of("Impreso_por_comisionado");
        System.out.println("estatus = " + estatus);
    }
}
