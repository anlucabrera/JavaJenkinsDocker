/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.OtrosTiposSesionesPsicopedagogia;
import mx.edu.utxj.pye.sgi.entity.pye2.SesionIndividualMensualPsicopedogia;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DTOSesionesPsicopedagogia;
import mx.edu.utxj.pye.siip.dto.ca.DtoSesionPsicopedagogia;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbSesionesPsicopedagogia;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorSesionesPsicopedagogia implements Serializable{
    
    private static final long serialVersionUID = -1607308138502126918L;
    
    @Getter @Setter DtoSesionPsicopedagogia dto;
    
    @EJB    EjbSesionesPsicopedagogia   ejbSesionesPsicopedagogia;
    @EJB    EjbModulos                  ejbModulos;
    @EJB    EjbCatalogos                ejbCatalogos;
    
    @Inject ControladorEmpleado         controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dto = new DtoSesionPsicopedagogia();
        dto.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short)controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dto.setLstAreasConflicto(ejbSesionesPsicopedagogia.getListaAreasDeConflicto());
        dto.setLstOtrosTiposSesionesPsicopedagogia(ejbSesionesPsicopedagogia.getListaOtrosTiposSesionesPsicopedagogia());
        dto.setLstProgramasEducativos(ejbCatalogos.getProgramasEducativos());
        
        Faces.setSessionAttribute("areasConflicto", dto.getLstAreasConflicto());
        Faces.setSessionAttribute("otroTipoSesion", dto.getLstOtrosTiposSesionesPsicopedagogia());
        Faces.setSessionAttribute("programasEducativos", dto.getLstProgramasEducativos());
        filtros();
    }
    
    public void filtros(){
        dto.setAniosConsulta(ejbModulos.getEjercicioRegistros(dto.getRegistros(), dto.getArea()));
        if(!dto.getAniosConsulta().isEmpty()){
            dto.setAnioConsulta((short)dto.getAniosConsulta().get(dto.getAniosConsulta().size()-1));
        }
        dto.setMesesConsulta(ejbModulos.getMesesRegistros(dto.getAnioConsulta(),dto.getRegistros(),dto.getArea()));
        if(!dto.getMesesConsulta().isEmpty()){
            dto.setMesConsulta(dto.getMesesConsulta().stream()
                .filter(t -> ejbModulos.getEventoRegistro().getMes().equals(t))
                .findAny()
                .orElse(dto.getMesesConsulta().get(dto.getMesesConsulta().size()-1)));
        }
        buscaSesionesIndividuales();
    }
    
    public void actualizarMeses(ValueChangeEvent e){
        dto.setAnioConsulta((short)e.getNewValue());
        dto.setMesesConsulta(ejbModulos.getMesesRegistros(dto.getAnioConsulta(), dto.getRegistros(), dto.getArea()));
        buscaSesionesIndividuales();
    }
    
    public void buscaSesionesIndividuales(){
        dto.setLstSesionesPsicopedagogia(ejbSesionesPsicopedagogia.getFiltroSesionesIndividualesPorAreaEjercicioMesArea(dto.getArea(), dto.getAnioConsulta(), dto.getMesConsulta()));
        dto.getLstSesionesPsicopedagogia().stream().forEach((sp) -> {
            sp.getSesionIndividualMensualPsicopedogia().setRegistros(ejbModulos.buscaRegistroPorClave(sp.getSesionIndividualMensualPsicopedogia().getRegistro()));
        });
        Ajax.update("formMuestraDatosActivos");
    }
    
    public void forzarAperturaSesionPsicopedagogia(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalRegistroSesionPsicopedagogia').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void actualizaInterfazSesionPsicopedagogia(){
        Ajax.update("frmRegistroSesionPsicopedagogia");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaSesionPsicopedagogia(); 
    }
    
    public void abrirNuevoRegistroSesionPsicopedagogia(){
        if (dto.getArea()!= null) {
            dto.setNuevoRegistro(Boolean.TRUE);
            DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
            dtoSP.setSesionIndividualMensualPsicopedogia(new SesionIndividualMensualPsicopedogia());
            dto.setDtoSesionPsicopedagogia(dtoSP);
            dto.setMensaje("");
            actualizaInterfazSesionPsicopedagogia();
        }else{
            Messages.addGlobalWarn("Debes seleccionar un área para poder dar de alta un nuevo registro");
        }
    }
    
    public void abrirEdicionSesionPsicopedagogia(DTOSesionesPsicopedagogia sesionPsicopedagogia) {
        dto.setNuevoRegistro(Boolean.FALSE);
        dto.setDtoSesionPsicopedagogia(sesionPsicopedagogia);
        accionProgramaEducativo(dto.getDtoSesionPsicopedagogia().getSesionIndividualMensualPsicopedogia().getOtroTipoSesion());
        if(dto.getHabilitaProgramaEducativo()){
            dto.setProgramaEducativo(dto.getDtoSesionPsicopedagogia().getProgramaEducativo());
        }else{
            dto.setProgramaEducativo(null);
        }
        dto.setMensaje("");
        actualizaInterfazSesionPsicopedagogia();
    }   
    
    public void guardarSesionPsipedagogia(SesionIndividualMensualPsicopedogia sesionPsicopedagogia) {
        sesionPsicopedagogia.setMes(controladorModulosRegistro.getEventosRegistros().getMes());
        sesionPsicopedagogia.setProgramaEducativo(dto.getProgramaEducativo().getArea());
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia).isEmpty()) {
            dto.setMensaje("Los datos que ha ingresado corresponden con un registro previamente registrado");
            actualizaInterfazSesionPsicopedagogia();
        } else {
            ejbSesionesPsicopedagogia.guardaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia, dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
            dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
            DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
            dtoSP.setSesionIndividualMensualPsicopedogia(new SesionIndividualMensualPsicopedogia());
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }
    
    public void guardarSesionPsipedagogiaSPE(SesionIndividualMensualPsicopedogia sesionPsicopedagogia) {
        sesionPsicopedagogia.setMes(controladorModulosRegistro.getEventosRegistros().getMes());
        sesionPsicopedagogia.setProgramaEducativo(null);
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogiaSPE(sesionPsicopedagogia).isEmpty()) {
            dto.setMensaje("Los datos que ha ingresado corresponden con un registro previamente registrado");
            actualizaInterfazSesionPsicopedagogia();
        } else {
            ejbSesionesPsicopedagogia.guardaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia, dto.getRegistroTipo(), dto.getEjesRegistro(), dto.getArea().getArea(), controladorModulosRegistro.getEventosRegistros());
            dto.setMensaje("El registro ha sido guardado con exito en la base de datos");
            DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
            dtoSP.setSesionIndividualMensualPsicopedogia(new SesionIndividualMensualPsicopedogia());
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }

    public void editaSesionPsicopedagogia(SesionIndividualMensualPsicopedogia sesionPsicopedagogia){
        DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
        sesionPsicopedagogia.setProgramaEducativo(dto.getProgramaEducativo().getArea());
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogiaParaEdicion(sesionPsicopedagogia).isEmpty()) {
            dto.setMensaje("Los datos que ha ingresado corresponden con un registro previo");
            actualizaInterfazSesionPsicopedagogia();
        }else{
            System.err.println("Registro: " + sesionPsicopedagogia.getRegistro());
            System.err.println("Área de Conflicto: " + sesionPsicopedagogia.getAreaConflicto());
            System.err.println("Otro Tipo de Sesión: " + sesionPsicopedagogia.getOtroTipoSesion().getDescripcion());
            System.err.println("Programa Educativo: " + sesionPsicopedagogia.getProgramaEducativo());
            System.err.println("Hombres: " + sesionPsicopedagogia.getHombres());
            System.err.println("Mujeres: " + sesionPsicopedagogia.getMujeres());
            System.err.println("Mes: " + sesionPsicopedagogia.getMes());
            dto.setMensaje(ejbSesionesPsicopedagogia.editaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia));
            dtoSP.setSesionIndividualMensualPsicopedogia(sesionPsicopedagogia);
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }
    
    public void editaSesionPsicopedagogiaSPE(SesionIndividualMensualPsicopedogia sesionPsicopedagogia){
        DTOSesionesPsicopedagogia dtoSP = new DTOSesionesPsicopedagogia();
        if (!ejbSesionesPsicopedagogia.buscaSesionIndividualMensualPsicopedagogiaSPEParaEdicion(sesionPsicopedagogia).isEmpty()) {
            dto.setMensaje("Los datos que ha ingresado corresponden con un registro previo");
            actualizaInterfazSesionPsicopedagogia();
        }else{
            dto.setMensaje(ejbSesionesPsicopedagogia.editaSesionIndividualMensualPsicopedagogia(sesionPsicopedagogia));
            dtoSP.setSesionIndividualMensualPsicopedogia(sesionPsicopedagogia);
            dto.setDtoSesionPsicopedagogia(dtoSP);
            actualizaInterfazSesionPsicopedagogia();
        }
        buscaSesionesIndividuales();
        Ajax.update("mensaje");
    }
    
    public void accionSesionPsicopedagogia(SesionIndividualMensualPsicopedogia sesionIndividualPsicopedagogia){
        if(dto.getNuevoRegistro()){
            if(dto.getHabilitaProgramaEducativo()){
                guardarSesionPsipedagogia(sesionIndividualPsicopedagogia);
            }else{
                guardarSesionPsipedagogiaSPE(sesionIndividualPsicopedagogia);
            }
        }else{
            if(dto.getHabilitaProgramaEducativo()){
                editaSesionPsicopedagogia(sesionIndividualPsicopedagogia);
            }else{
                editaSesionPsicopedagogiaSPE(sesionIndividualPsicopedagogia);
            }
            
        }
    }
    
    public void habilitaProgramaEducativo(ValueChangeEvent e){
        accionProgramaEducativo((OtrosTiposSesionesPsicopedagogia)e.getNewValue());
    }
    
    public void accionProgramaEducativo(OtrosTiposSesionesPsicopedagogia otroTipoSesionPsicopedagogia){
        if((otroTipoSesionPsicopedagogia.getOtroTipoSesionPsicopedagogia() == 1) || (otroTipoSesionPsicopedagogia.getOtroTipoSesionPsicopedagogia() == 3)){
            dto.setHabilitaProgramaEducativo(Boolean.FALSE);
        }else{
            dto.setHabilitaProgramaEducativo(Boolean.TRUE);
        }
        dto.setMensaje("");
        Ajax.update("mensaje");
    }
    
}
