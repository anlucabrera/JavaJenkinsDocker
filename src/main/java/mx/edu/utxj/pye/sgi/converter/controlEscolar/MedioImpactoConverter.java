/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.converter.controlEscolar;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioDifusion;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@FacesConverter("medioDifusionConverter")
public class MedioImpactoConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        try {
            List<MedioDifusion> medioDifusionList = Faces.getSessionAttribute("listaMedioDifusion");
            MedioDifusion md = new MedioDifusion(Short.valueOf(value));
            return medioDifusionList.get(medioDifusionList.indexOf(md));
        }catch (Exception e){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir.", value + " no es una clave válida."));
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        if(value == null){
            return null;
        }

        try{
            return ((MedioDifusion) value).getIdMedioDifusion().toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
//            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir.", value + " no es un elemento válido."));
        }
    }
    
}
