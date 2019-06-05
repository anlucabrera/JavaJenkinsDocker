package mx.edu.utxj.pye.sgi.converter.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.Turno;
import org.omnifaces.util.Faces;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.List;

@FacesConverter("turnoConverter")
public class TurnoConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
        try {
            List<Turno> turnoList = Faces.getSessionAttribute("listTurno");
            Turno turno = new Turno(Short.valueOf(value));
            return turnoList.get(turnoList.indexOf(turno));
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
            return ((Turno) value).getIdTurno().toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
//            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir.", value + " no es un elemento válido."));
        }
    }
}
