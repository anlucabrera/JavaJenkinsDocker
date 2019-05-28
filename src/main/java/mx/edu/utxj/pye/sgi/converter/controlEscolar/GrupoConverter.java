/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.converter.controlEscolar;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;


/**
 *
 * @author UTXJ
 */
@FacesConverter("grupoConverter")
public class GrupoConverter implements Converter{
    
    @EJB
    FacadeCE facadeCE;

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
        try {
            Integer clave = Integer.valueOf(value);
            return facadeCE.getEntityManager().find(Grupo.class,clave);
        } catch (Exception e) {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir getAsObject.", value + " no es una clave válida."));
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        if (value == null || value.toString().isEmpty()) {
            return null;
        }

        try{
            return ((Grupo) value).getIdGrupo().toString();
        }catch(Exception e){
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo convertir .", value + " no es un elemento válido."));
        }
    }
    
}
