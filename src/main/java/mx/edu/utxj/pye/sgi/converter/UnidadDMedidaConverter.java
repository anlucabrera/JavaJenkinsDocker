package mx.edu.utxj.pye.sgi.converter;

import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.ejb.poa.FacadePoa;
import mx.edu.utxj.pye.sgi.entity.pye2.UnidadMedidas;

/**
 *
 * @author jonny
 */
@FacesConverter("unidadDMConverter")
public class UnidadDMedidaConverter implements Converter {

    @EJB
    FacadePoa f;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        Short clave = Short.parseShort(value);
        return f.getEntityManager().find(UnidadMedidas.class, clave);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((UnidadMedidas) value).getUnidadMedida().toString();
    }

}
