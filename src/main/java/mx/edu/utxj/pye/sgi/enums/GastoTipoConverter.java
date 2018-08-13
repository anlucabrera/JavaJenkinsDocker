package mx.edu.utxj.pye.sgi.enums;

import javax.faces.convert.EnumConverter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author UTXJ
 */
@FacesConverter("gastoTipoConverter")
public class GastoTipoConverter extends EnumConverter{
    public GastoTipoConverter() {
        super(GastoTipo.class);
    }    
}
