package mx.edu.utxj.pye.sgi.enums.converter;

import mx.edu.utxj.pye.sgi.enums.AsignacionTarifaEtapa;

public class AsignacionTarifaEtapaConverter extends AbstractEnumConverter<AsignacionTarifaEtapa> {
    /**
     * Provee el array de todos los posibles valores del enum en java
     *
     * @return Arreglo de valores.
     */
    @Override
    public AsignacionTarifaEtapa[] getValues() {
        return AsignacionTarifaEtapa.values();
    }

    /**
     * Provee el valor en texto del enum que se almacena en la base de datos.
     *
     * @param asignacionTarifaEtapa Enum de java del que se desea conocer su valor en texto.
     * @return Valor en cadena de texto del enum.
     */
    @Override
    public String getLabel(AsignacionTarifaEtapa asignacionTarifaEtapa) {
        return asignacionTarifaEtapa.getLabel();
    }

    public static AsignacionTarifaEtapa of(String label){
        return (new AsignacionTarifaEtapaConverter()).convert(label);
    }
}
