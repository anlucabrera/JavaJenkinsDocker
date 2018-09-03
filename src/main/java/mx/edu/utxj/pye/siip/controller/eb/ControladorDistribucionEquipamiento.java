/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

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
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.entity.servgen.list.ListaDistribucionEquipamiento;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionEquipamiento;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorDistribucionEquipamiento implements Serializable{

    private static final long serialVersionUID = -4519318290264813424L;
    
    @Getter RegistrosTipo registroTipo;
    @Getter RegistrosTipo registroTipoInternet;
    @Getter RegistrosTipo registroTipoInt;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;
    
    @Getter @Setter private ListaDistribucionEquipamiento listaDistribucionEquipamiento = new ListaDistribucionEquipamiento();
    
    @EJB
    EjbDistribucionEquipamiento ejbDistribucionEquipamiento;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)12);
        registroTipoInternet = new RegistrosTipo();
        registroTipoInternet.setRegistroTipo((short)13);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(5);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();

    }
    
    public void listaDistribucionEquipamientoPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                this.rutaArchivo = rutaArchivo;
                listaDistribucionEquipamiento = ejbDistribucionEquipamiento.getListaDistribucionEquipamiento(rutaArchivo);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDistribucionEquipamiento.class.getName()).log(Level.SEVERE, null, ex);
            if (rutaArchivo != null) {
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaDistribucionEquipamiento(){
        if (listaDistribucionEquipamiento != null) {
            try {
                ejbDistribucionEquipamiento.guardaEquipoComputoCPE(listaDistribucionEquipamiento, registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbDistribucionEquipamiento.guardaEquipoComputoInternetCPE(listaDistribucionEquipamiento, registroTipoInternet, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorDistribucionEquipamiento.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaDistribucionEquipamiento.getDtoEquiposComputoCPE().clear();
                listaDistribucionEquipamiento.getDtoEquiposComputoInternetCPE().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
        listaDistribucionEquipamiento.getDtoEquiposComputoCPE().clear();
        listaDistribucionEquipamiento.getDtoEquiposComputoInternetCPE().clear();
        if (this.rutaArchivo != null) {
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
    
}
