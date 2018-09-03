/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaMovilidadEstudiante;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbMovilidadEstudiante;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;
/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorMovilidadEstudiante implements Serializable{

    private static final long serialVersionUID = 1L;
    //Variables para almacenar el registro
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    
    @Getter @Setter private ListaMovilidadEstudiante listaMovilidadEstudiante = new ListaMovilidadEstudiante();
    
    @EJB
    EjbMovilidadEstudiante ejbMovilidadEstudiante;
    
    @Inject 
    ControladorEmpleado controladorEmpleado;
    @Inject
    ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)32);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(3);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();

    }
    
    public void listaMovilidadEstudiantePrevia(String rutaArchivo) {
      try {
            listaMovilidadEstudiante = ejbMovilidadEstudiante.getListaMovilidadEstudiante(rutaArchivo);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorMovilidadEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    public void guardaMovilidadEstudiante() {
        try {
            ejbMovilidadEstudiante.guardaMovilidadEstudiante(listaMovilidadEstudiante,registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
            Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorMovilidadEstudiante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        
        listaMovilidadEstudiante.getMovilidadestudiante().clear();
    }
    
}
