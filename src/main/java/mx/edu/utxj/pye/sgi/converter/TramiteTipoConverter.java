package mx.edu.utxj.pye.sgi.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.enums.TramiteTipo;

/**
 *
 * @author UTXJ
 */
@FacesConverter("tramiteTipoConverter")
public class TramiteTipoConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value.trim().equals(TramiteTipo.COMISION.getLabel())){
            return TramiteTipo.COMISION;
        }else if(value.trim().equals(TramiteTipo.PRODUCTO_SERVICIO.getLabel())){
            return TramiteTipo.PRODUCTO_SERVICIO;
        }else{
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir el tipo de trámite.", value + " no es un valor de tipo válido."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value instanceof TramiteTipo){
            return ((TramiteTipo)value).getLabel();
        }else{
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "El objecto recibido no es un tipo de trámite.", value + " no es un valor de tipo válido."));
        }
    }

   
}
