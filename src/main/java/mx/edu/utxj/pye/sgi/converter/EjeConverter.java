package mx.edu.utxj.pye.sgi.converter;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@FacesConverter("ejeConverter")
public class EjeConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try{
            List<EjesRegistro> ejes = Faces.getSessionAttribute("ejes");
            EjesRegistro eje = new EjesRegistro(Integer.valueOf(value));
            
            return ejes.get(ejes.indexOf(eje));
        }catch(Exception e){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir el eje.", value + " no es una clave válida."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null){
            return null;
        }
//        System.out.println("mx.edu.utxj.pye.sgi.converter.EjeConverter.getAsString(" + (value!=null?value.getClass().getName():null) + ") value: " + value);
        try{     
            return ((EjesRegistro)value).getEje().toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir el eje.", value + " no es un elemento válido."));
        }
    }
    
}
