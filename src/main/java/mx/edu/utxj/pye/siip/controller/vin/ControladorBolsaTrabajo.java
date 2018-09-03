/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.pye2.EjesRegistro;
import mx.edu.utxj.pye.sgi.entity.pye2.RegistrosTipo;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaBolsaTrabajo;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbBolsaTrabajo;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorBolsaTrabajo implements Serializable{

    private static final long serialVersionUID = 2000383561708788434L;
     //Variables para almacenar el registro
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    
    @Getter @Setter private ListaBolsaTrabajo listaBolsaTrabajo = new ListaBolsaTrabajo();
    
    @EJB
    EjbBolsaTrabajo ejbBolsaTrabajo;
    
    @Inject 
    ControladorEmpleado controladorEmpleado;
    @Inject
    ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)1);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(4);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();

    }
    
    public void listaBolsaTrabajoPrevia(String rutaArchivo) {
        try {
            listaBolsaTrabajo = ejbBolsaTrabajo.getListaBolsaTrabajo(rutaArchivo);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorBolsaTrabajo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void guardaBolsaTrabajo() {
        try {
            ejbBolsaTrabajo.guardaBolsaTrabajo(listaBolsaTrabajo,registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
            Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorBolsaTrabajo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        listaBolsaTrabajo.getBolsa().clear();
    }
}
