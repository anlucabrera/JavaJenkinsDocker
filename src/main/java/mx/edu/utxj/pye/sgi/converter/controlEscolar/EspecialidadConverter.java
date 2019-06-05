package mx.edu.utxj.pye.sgi.converter.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.EspecialidadCentro;
import org.omnifaces.util.Faces;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.List;

@FacesConverter("especialidadConverter")
public class EspecialidadConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        try {
            List<EspecialidadCentro> especialidadCentroList = Faces.getSessionAttribute("listaEspecialidad");
            EspecialidadCentro especilaidad = new EspecialidadCentro(Integer.valueOf(value));
            return especialidadCentroList.get(especialidadCentroList.indexOf(especilaidad));
        }catch (Exception e){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir.", value + " no es una clave válida."));
        }
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object value) {

        if(value == null){
            return null;
        }

        try{
            return ((EspecialidadCentro) value).getIdEspecialidadCentro().toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
//            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir.", value + " no es un elemento válido."));
        }
    }
}
