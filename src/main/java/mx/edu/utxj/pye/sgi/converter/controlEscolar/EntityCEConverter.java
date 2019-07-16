/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.converter.controlEscolar;

import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;
import org.omnifaces.util.Faces;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import java.util.List;

/**
 *
 * @author UTXJ
 */
@FacesConverter("entidadCEConverter")
public class EntityCEConverter implements Converter{
    
    @EJB
    FacadeCE facadeCE;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent component, String value) {
        if(value == null || value.equals("- Seleccione -")){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Debe elegir un valor."));
        }
        try {
            switch(component.getId()){
                case "ocupacionPadre":
                    List<Ocupacion> listaOcupacionP = Faces.getSessionAttribute("listaOcp");
                    Ocupacion ocupacionP = new Ocupacion(Short.valueOf(value));            
                    return listaOcupacionP.get(listaOcupacionP.indexOf(ocupacionP));
                case "ocupacionMadre":
                    List<Ocupacion> listaOcupacionM = Faces.getSessionAttribute("listaOcp");
                    Ocupacion ocupacionM = new Ocupacion(Short.valueOf(value));            
                    return listaOcupacionM.get(listaOcupacionM.indexOf(ocupacionM));
                case "escolaridadPadre":
                    List<Escolaridad> listaEscolaridadP = Faces.getSessionAttribute("listaEsc");
                    Escolaridad escolaridadP = new Escolaridad(Short.valueOf(value));            
                    return listaEscolaridadP.get(listaEscolaridadP.indexOf(escolaridadP));
                case "escolaridadMadre":
                    List<Escolaridad> listaEscolaridad = Faces.getSessionAttribute("listaEsc");
                    Escolaridad escolaridad = new Escolaridad(Short.valueOf(value));            
                    return listaEscolaridad.get(listaEscolaridad.indexOf(escolaridad));
                case "eb":
                    List<EspecialidadCentro> especialidadCentroList = Faces.getSessionAttribute("listaEspecialidad");
                    EspecialidadCentro especilaidad = new EspecialidadCentro(Integer.valueOf(value));
                    return especialidadCentroList.get(especialidadCentroList.indexOf(especilaidad));
                case "grupo":
                    Integer clave = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(Grupo.class,clave);
                case "tipoS":
                    Short claveTS = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(TipoSangre.class,claveTS);
                case "tipoDiscapacidad":
                    Short claveTD = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(TipoDiscapacidad.class, claveTD);
                case "sistema1":
                    List<Sistema> sistemaList = Faces.getSessionAttribute("listaSistema");
                    Sistema turno = new Sistema(Short.valueOf(value));
                    return sistemaList.get(sistemaList.indexOf(turno));
                case "sistema2":
                    List<Sistema> sistemaListSO = Faces.getSessionAttribute("listaSistema");
                    Sistema turnoSO = new Sistema(Short.valueOf(value));
                    return sistemaListSO.get(sistemaListSO.indexOf(turnoSO));
                case "li":
                    List<LenguaIndigena> lenguaIndigenaList = Faces.getSessionAttribute("listaLenguaIndigena");
                    LenguaIndigena ocupacion = new LenguaIndigena(Short.valueOf(value));
                    return lenguaIndigenaList.get(lenguaIndigenaList.indexOf(ocupacion));
                case "md":
                    List<MedioDifusion> medioDifusionList = Faces.getSessionAttribute("listaMedioDifusion");
                    MedioDifusion md = new MedioDifusion(Short.valueOf(value));
                    return medioDifusionList.get(medioDifusionList.indexOf(md));
                case "turno":
                    List<Turno> turnoList = Faces.getSessionAttribute("listTurno");
                    Turno turnoGrupo = new Turno(Short.valueOf(value));
                    return turnoList.get(turnoList.indexOf(turnoGrupo));
                case "planEstudio":
                    Integer clavePlan = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(PlanEstudio.class,clavePlan);
                case "matricula":
                    Integer estudiante = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(Estudiante.class,estudiante);
                case "nombreBusqueda":
                    Integer estudianteName = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(Estudiante.class,estudianteName);
                default:
                    throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", component.getClientId() + " no es un componente válido."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", value + " no es una clave válida."));
        }
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object value) {
        if(value == null){
            return null;
        }
        
        try{
            if(value instanceof Ocupacion)
                return ((Ocupacion)value).getIdOcupacion().toString();
            else if(value instanceof Escolaridad)
                return ((Escolaridad)value).getIdEscolaridad().toString();
            else if(value instanceof EspecialidadCentro)
                return ((EspecialidadCentro)value).getIdEspecialidadCentro().toString();
            else if(value instanceof Grupo)
                return ((Grupo)value).getIdGrupo().toString();
            else if(value instanceof TipoSangre)
                return ((TipoSangre) value).getIdTipoSangre().toString();
            else if(value instanceof  TipoDiscapacidad)
                return ((TipoDiscapacidad) value).getIdTipoDiscapacidad().toString();
            else if(value instanceof  Sistema)
                return ((Sistema) value).getIdSistema().toString();
            else if(value instanceof LenguaIndigena)
                return ((LenguaIndigena) value).getIdLenguaIndigena().toString();
            else if(value instanceof MedioDifusion)
                return ((MedioDifusion) value).getIdMedioDifusion().toString();
            else if(value instanceof Turno)
                return ((Turno) value).getIdTurno().toString();
            else if(value instanceof PlanEstudio)
                return ((PlanEstudio) value).getIdPlanEstudio().toString();
            else if(value instanceof Estudiante)
                return ((Estudiante) value).getIdEstudiante().toString();
            else
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", value.getClass().getName() + " no es una clase válida."));
        }catch(Exception e){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",  " no es un elemento válido."));
        }
    }
    
}
