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
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaExaniResultados;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbPlantillasCAExcel;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbExaniResultados;
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
public class ControladorExaniResultados implements Serializable{
    
    private static final long serialVersionUID = -4590257259678715420L;
     //Variables para almacenar el registro
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    
    @Getter @Setter private ListaExaniResultados listaExaniResultados = new ListaExaniResultados();
    
    @EJB
    EjbExaniResultados ejbExaniResultados;
    
    @EJB
    EjbPlantillasCAExcel ejbPlantillasCAExcel;
    
    @Inject 
    ControladorEmpleado controladorEmpleado;
    @Inject
    ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)11);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(4);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();

    }
    
     public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasCAExcel.getPlantillaExani());
        Faces.sendFile(f, true);
    }
    
    public void listaExaniResultadosPrevia(String rutaArchivo){
        try {
            listaExaniResultados = ejbExaniResultados.getListaExaniResultados(rutaArchivo);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorExaniResultados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardaExaniResultados(){
        try {
            ejbExaniResultados.guardaExaniResultados(listaExaniResultados,registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
            Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorExaniResultados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
        listaExaniResultados.getExanis().clear();
    }
    
}
