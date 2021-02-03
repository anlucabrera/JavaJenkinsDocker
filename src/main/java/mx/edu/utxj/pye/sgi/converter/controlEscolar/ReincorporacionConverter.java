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

import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.facade.controlEscolar.FacadeCE;

/**
 *
 * @author UTXJ
 */
@FacesConverter("reincorporacionConverter")
public class ReincorporacionConverter implements Converter{
    
    @EJB
    FacadeCE facadeCE;
    
    @Override
    public Object getAsObject(FacesContext fc, UIComponent component, String value) {
        if(value == null || value.equals("- Seleccione -")){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Debe elegir un valor."));
        }
        try {
            switch (component.getId()) {
                case "ocupacionPadre":
                    Short claveOP = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Ocupacion.class, claveOP);
                case "ocupacionMadre":
                    Short claveOM = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Ocupacion.class, claveOM);
                case "ocupacionFamiliar":
                    Short claveOF = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Ocupacion.class, claveOF);
                case "escolaridadPadre":
                    Short claveEP = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Escolaridad.class, claveEP);
                case "escolaridadMadre":
                    Short claveEM = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Escolaridad.class, claveEM);
                case "escolaridadFamiliar":
                    Short claveEF = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Escolaridad.class, claveEF);
                case "eb":
                    Integer claveEC = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(EspecialidadCentro.class,claveEC);
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
                    Short claveSP = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Sistema.class,claveSP);
                case "sistema2":
                    Short claveSS = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Sistema.class,claveSS);
                case "li":
                    Short claveLi = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(LenguaIndigena.class,claveLi);
                case "md":
                    Short claveMd = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(MedioDifusion.class,claveMd);
                case "turno":
                    Short claveTurn = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(Turno.class,claveTurn);
                case "planEstudio":
                    Integer clavePlan = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(PlanEstudio.class,clavePlan);
                case "matricula":
                    Integer estudiante = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(Estudiante.class,estudiante);
                case "nombreBusqueda":
                    Integer estudianteName = Integer.valueOf(value);
                    return facadeCE.getEntityManager().find(Estudiante.class,estudianteName);
                case "tipoE":
                    Short tipoEs = Short.parseShort(value);
                    return facadeCE.getEntityManager().find(TipoEstudiante.class, tipoEs);
                case "estuR":
                    Integer idEst = Integer.parseInt(value);
                    return facadeCE.getEntityManager().find(Estudiante.class, idEst);
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
            else if(value instanceof TipoEstudiante)
                return ((TipoEstudiante) value).getIdTipoEstudiante().toString();
            else
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "", value.getClass().getName() + " no es una clase válida."));
        }catch(Exception e){
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "",  " no es un elemento válido."));
        }
    }
    
}