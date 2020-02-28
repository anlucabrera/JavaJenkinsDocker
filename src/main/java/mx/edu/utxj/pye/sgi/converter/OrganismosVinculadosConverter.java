/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.converter;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@FacesConverter("organismoVinculadoConverter")
public class OrganismosVinculadosConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0){
            try {
                List<OrganismosVinculados> l = Faces.getSessionAttribute("organismosVinculados");
                OrganismosVinculados o = l.get(l.indexOf(new OrganismosVinculados(Integer.parseInt(value))));
                if(o != null){
                    return o;
                }else{
                    throw new Exception();
                }
            } catch (Exception e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir el organismo vinculado", value + " no es una clave de organismo vinculado válida."));
            }
        }else{
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir el organismo vinculado", value + " no es una clave de organismo vinculado válida."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value != null) {
            return ((OrganismosVinculados)value).getRegistro().toString();
        }
        else {
            return null;
        }
    }
    
}
