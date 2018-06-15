/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.converter;

import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.entity.ch.PersonalCategorias;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@FacesConverter("personalCategoriaConverter")
public class PersonalCategoriasConverter implements Converter{
    @EJB Facade f;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
//            System.out.println("mx.edu.utxj.pye.sgi.converter.PersonalCategoriasConverter.getAsObject() entra al if");
            try {
                List<PersonalCategorias> l = Faces.getSessionAttribute("personalCategoria");
                l.stream().forEach(System.out::println);
//                System.out.println("mx.edu.utxj.pye.sgi.converter.PersonalConverter.getAsObject(" + f + ") value: " + value);
                PersonalCategorias p = l.get(l.indexOf(new PersonalCategorias(Short.parseShort(value))));
                if(p!=null){
                    return p;
                }else{
                    throw new Exception();
                }
            } catch(Exception  e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir la caegoria", value + " no es una clave de categoria válida."));
            }
        }
        else {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir la categoria", value + " no es una clave de categoria válida."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value != null) {
            return ((PersonalCategorias)value).getCategoria().toString();
        }
        else {
            return null;
        }
    }
    
}
