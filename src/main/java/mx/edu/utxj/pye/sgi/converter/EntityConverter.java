package mx.edu.utxj.pye.sgi.converter;

import com.google.gson.Gson;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.ActividadesPoa;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.Estado;
import mx.edu.utxj.pye.sgi.entity.pye2.Estrategias;
import mx.edu.utxj.pye.sgi.entity.pye2.LineasAccion;
import mx.edu.utxj.pye.sgi.entity.pye2.Localidad;
import mx.edu.utxj.pye.sgi.entity.pye2.LocalidadPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Municipio;
import mx.edu.utxj.pye.sgi.entity.pye2.MunicipioPK;
import mx.edu.utxj.pye.sgi.entity.pye2.Pais;
import mx.edu.utxj.pye.sgi.facade.Facade;
import org.omnifaces.util.Faces;

/**
 *
 * @author UTXJ
 */
@FacesConverter("entidadConverter2")
public class EntityConverter implements Converter{
    @EJB Facade f;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
//        System.out.println("mx.edu.utxj.pye.sgi.converter.EntityConverter.getAsObject() f: " + f);
//        System.out.println("mx.edu.utxj.pye.sgi.converter.EntityConverter.getAsObject() id: " + component.getId());
//        System.out.println("mx.edu.utxj.pye.sgi.converter.EntityConverter.getAsObject() value: " + value);
        if(value == null || value.equals("- Seleccione -")){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Debe elegir un valor."));
        }
        try{
            switch(component.getId()){
                case "eje":
                    List<EjesRegistro> ejes = Faces.getSessionAttribute("ejes");
                    EjesRegistro eje = new EjesRegistro(Integer.valueOf(value));            
                    return ejes.get(ejes.indexOf(eje));
                case "estrategia":
                    List<Estrategias> estrategias = Faces.getSessionAttribute("estrategias");
                    Estrategias estrategia = new Estrategias(Short.valueOf(value));
                    return estrategias.get(estrategias.indexOf(estrategia));
                case "lineaAccion":
                    List<LineasAccion> lineasAccion = Faces.getSessionAttribute("lineasAccion");
                    LineasAccion lineaAccion = new LineasAccion(Short.valueOf(value));            
                    return lineasAccion.get(lineasAccion.indexOf(lineaAccion));
                case "actividad":
                    List<ActividadesPoa> actividades = Faces.getSessionAttribute("actividades");
                    ActividadesPoa actividad = new ActividadesPoa(Integer.valueOf(value));            
                    return actividades.get(actividades.indexOf(actividad));
                case "area":
                    List<AreasUniversidad> areas = Faces.getSessionAttribute("areas");
                    AreasUniversidad area = new AreasUniversidad(Short.valueOf(value));
                    return areas.get(areas.indexOf(area));
                case "pais":
                    List<Pais> paises = Faces.getSessionAttribute("paises");
                    Pais pais = new Pais(Integer.valueOf(value));
                    return paises.get(paises.indexOf(pais));
                case "estado":
                    List<Estado> estados = Faces.getSessionAttribute("estados");
                    Estado estado = new Estado(Integer.valueOf(value));
                    return estados.get(estados.indexOf(estado));
                case "municipio":
                    MunicipioPK pk = (new Gson()).fromJson(value, MunicipioPK.class);
                    List<Municipio> municipios = Faces.getSessionAttribute("municipios");
                    Municipio municipio = municipios.get(municipios.indexOf(new Municipio(pk)));
                    return municipio;
                case "localidad":
                    LocalidadPK pkl = (new Gson()).fromJson(value, LocalidadPK.class);
                    List<Localidad> localidades = Faces.getSessionAttribute("localidades");
                    Localidad localidad = localidades.get(localidades.indexOf(new Localidad(pkl)));
                    return localidad;
                case "ejeParticipante":
                    List<EjesRegistro> ejesParticipantes = Faces.getSessionAttribute("ejes");
                    EjesRegistro ejeParticipante = new EjesRegistro(Integer.valueOf(value));            
                    return ejesParticipantes.get(ejesParticipantes.indexOf(ejeParticipante));
                case "estrategiaParticipante":
                    List<Estrategias> estrategiasParticipantes = Faces.getSessionAttribute("estrategias");
                    Estrategias estrategiaParticipante = new Estrategias(Short.valueOf(value));
                    return estrategiasParticipantes.get(estrategiasParticipantes.indexOf(estrategiaParticipante));
                case "lineaAccionParticipante":
                    List<LineasAccion> lineasAccionParticipantes = Faces.getSessionAttribute("lineasAccion");
                    LineasAccion lineaAccionParticipante = new LineasAccion(Short.valueOf(value));            
                    return lineasAccionParticipantes.get(lineasAccionParticipantes.indexOf(lineaAccionParticipante));
                case "actividadParticipante":
                    List<ActividadesPoa> actividadesParticipantes = Faces.getSessionAttribute("actividades");
                    ActividadesPoa actividadParticipante = new ActividadesPoa(Integer.valueOf(value));            
                    return actividadesParticipantes.get(actividadesParticipantes.indexOf(actividadParticipante));
                case "ejeParticipante2":
                    List<EjesRegistro> ejesParticipantes2 = Faces.getSessionAttribute("ejes");
                    EjesRegistro ejeParticipante2 = new EjesRegistro(Integer.valueOf(value));            
                    return ejesParticipantes2.get(ejesParticipantes2.indexOf(ejeParticipante2));
                case "estrategiaParticipante2":
                    List<Estrategias> estrategiasParticipantes2 = Faces.getSessionAttribute("estrategias");
                    Estrategias estrategiaParticipante2 = new Estrategias(Short.valueOf(value));
                    return estrategiasParticipantes2.get(estrategiasParticipantes2.indexOf(estrategiaParticipante2));
                case "lineaAccionParticipante2":
                    List<LineasAccion> lineasAccionParticipantes2 = Faces.getSessionAttribute("lineasAccion");
                    LineasAccion lineaAccionParticipante2 = new LineasAccion(Short.valueOf(value));            
                    return lineasAccionParticipantes2.get(lineasAccionParticipantes2.indexOf(lineaAccionParticipante2));
                case "actividadParticipante2":
                    List<ActividadesPoa> actividadesParticipantes2 = Faces.getSessionAttribute("actividades");
                    ActividadesPoa actividadParticipante2 = new ActividadesPoa(Integer.valueOf(value));            
                    return actividadesParticipantes2.get(actividadesParticipantes2.indexOf(actividadParticipante2));
                default:
                    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", component.getClientId() + " no es un componente válido."));
            }
            
        }catch(Exception e){
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", value + " no es una clave válida."));
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
//        System.out.println("mx.edu.utxj.pye.sgi.converter.EntityConverter.getAsString() f: " + f);
        if(value == null){
            return null;
        }
//        System.out.println("mx.edu.utxj.pye.sgi.converter.EntityConverter.getAsString() class: " + value.getClass().getName());
        
        try{
            if(value instanceof EjesRegistro)
                return ((EjesRegistro)value).getEje().toString();
            else if(value instanceof Estrategias)
                return ((Estrategias)value).getEstrategia().toString();
            else if(value instanceof LineasAccion)
                return ((LineasAccion)value).getLineaAccion().toString();
            else if(value instanceof ActividadesPoa)
                return ((ActividadesPoa)value).getActividadPoa().toString();
            else if(value instanceof AreasUniversidad)
                return ((AreasUniversidad)value).getArea().toString();
            else if(value instanceof Pais)
                return ((Pais)value).getIdpais().toString();
            else if(value instanceof Estado)
                return ((Estado)value).getIdestado().toString();
            else if(value instanceof Municipio){
                String json = (new Gson()).toJson(((Municipio)value).getMunicipioPK());
                return json;//((Municipio)value).getMunicipioPK().toString();    
            }else if(value instanceof Localidad){
                String jsonLoc = (new Gson()).toJson(((Localidad)value).getLocalidadPK());
                return jsonLoc;
            }else{
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", value.getClass().getName() + " no es una clase válida."));
            }
        }catch(Exception e){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", value + " no es un elemento válido."));
        }
    }
    
}
