package mx.edu.utxj.pye.sgi.converter;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Faces;

/**
 *
 * @author jonny
 */
@FacesConverter("personalConverter")
public class PersonalConverter implements Converter{
    @EJB Facade f;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                List<Personal> l = Faces.getSessionAttribute("docentesActivos");
                System.out.println("mx.edu.utxj.pye.sgi.converter.PersonalConverter.getAsObject(" + f + ") value: " + value);
                Personal p = l.get(l.indexOf(new Personal(Integer.parseInt(value))));
                if(p!=null){
                    return p;
                }else{
                    throw new Exception();
                }
            } catch(Exception  e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir el personal", value + " no es una clave de personal válida."));
            }
        }
        else {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir el personal", value + " no es una clave de personal válida."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value != null) {
            return ((Personal)value).getClave().toString();
        }
        else {
            return null;
        }
    }
    
}
