package mx.edu.utxj.pye.sgi.converter.controlEscolar;

import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoDiscapacidad;

@FacesConverter("tipoDiscapacidadConverter")
public class TipoDiscapacidadConverter implements Converter {

    @EJB
    FacadeCE facadeCE;

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        try {
            Short clave = Short.parseShort(value);
            return facadeCE.getEntityManager().find(TipoDiscapacidad.class, clave);
        }catch (Exception e){
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir getAsObject.", value + " no es una clave válida."));
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {
        if (value == null || value.toString().isEmpty()) {
            return "";
        }

        try{
            return ((TipoDiscapacidad) value).getIdTipoDiscapacidad().toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir .", value + " no es un elemento válido."));
        }
    }
}
