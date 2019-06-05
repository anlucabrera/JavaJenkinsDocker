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
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbCatalogos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.dto.ca.DtoSesionPsicopedagogia;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbSesionesPsicopedagogia;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.cdi.ViewScoped;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorSesionesPsicopedagogia implements Serializable{
    
    private static final long serialVersionUID = -1607308138502126918L;
    
    @Getter @Setter DtoSesionPsicopedagogia dtoSesionPsicopedagogia;
    
    @EJB    EjbSesionesPsicopedagogia   ejbSesionesPsicopedagogia;
    @EJB    EjbModulos                  ejbModulos;
    @EJB    EjbCatalogos                ejbCatalogos;
    
    @Inject ControladorEmpleado         controladorEmpleado;
    @Inject ControladorModulosRegistro  controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        dtoSesionPsicopedagogia = new DtoSesionPsicopedagogia();
        dtoSesionPsicopedagogia.setArea(ejbModulos.getAreaUniversidadPrincipalRegistro((short)controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa()));
        dtoSesionPsicopedagogia.setLstAreasConflicto(ejbSesionesPsicopedagogia.getListaAreasDeConflicto());
        dtoSesionPsicopedagogia.setLstOtrosTiposSesionesPsicopedagogia(ejbSesionesPsicopedagogia.getListaOtrosTiposSesionesPsicopedagogia());
        dtoSesionPsicopedagogia.setLstProgramasEducativos(ejbCatalogos.getProgramasEducativos());
    }
    
}
