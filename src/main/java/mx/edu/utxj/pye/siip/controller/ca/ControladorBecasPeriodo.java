/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

import java.io.File;
import java.io.IOException;
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
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaBecasPeriodo;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbBecasPeriodo;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;


/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorBecasPeriodo implements Serializable{

    private static final long serialVersionUID = 7599304225528608438L;
    //Variables para almacenar el registro
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    
    @Getter @Setter private ListaBecasPeriodo listaBecasPeriodo = new ListaBecasPeriodo();
    
    @EJB
    EjbBecasPeriodo ejbBecasPeriodo;
    
    @Inject 
    ControladorEmpleado controladorEmpleado;
    @Inject
    ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)9);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(4);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();

    }
    
  
    public void listaBecasPeriodoPrevia(String rutaArchivo){
        try {
            listaBecasPeriodo = ejbBecasPeriodo.getListaBecasPeriodo(rutaArchivo);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorBecasPeriodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardaBecasPeriodo(){
        try {
            ejbBecasPeriodo.guardaBecasPeriodo(listaBecasPeriodo,registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
            Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorBecasPeriodo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        listaBecasPeriodo.getBecasPeriodosEscolares().clear();
    }
}
