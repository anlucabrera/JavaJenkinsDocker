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
import mx.edu.utxj.pye.siip.entity.servgen.list.ListaDistribucionInstalaciones;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbDistribucionInstalaciones;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorDistribucionInstalaciones implements Serializable{

    private static final long serialVersionUID = 2179388892040170127L;
    
    @Getter RegistrosTipo registroTipoCI;
    @Getter RegistrosTipo registroTipoDA;
    @Getter RegistrosTipo registroTipoDLT;
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;
    
    @Getter @Setter private ListaDistribucionInstalaciones listaDistribucionInstalaciones = new ListaDistribucionInstalaciones();
    
    @EJB
    EjbDistribucionInstalaciones ejbDistribucionInstalaciones;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipoCI = new RegistrosTipo();
        registroTipoCI.setRegistroTipo((short)18);
        registroTipoDA = new RegistrosTipo();
        registroTipoDA.setRegistroTipo((short)19);
        registroTipoDLT = new RegistrosTipo();
        registroTipoDLT.setRegistroTipo((short)20);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(5);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public void listaDistribucionInstalacionesPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                this.rutaArchivo = rutaArchivo;
                listaDistribucionInstalaciones = ejbDistribucionInstalaciones.getListaDistribucionInstalaciones(rutaArchivo);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorDistribucionInstalaciones.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaDistribucionEquipamiento(){
        if (listaDistribucionInstalaciones != null) {
            try {
                ejbDistribucionInstalaciones.guardaCapacidadIntalada(listaDistribucionInstalaciones, registroTipoCI, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbDistribucionInstalaciones.guardaDistribucionAulas(listaDistribucionInstalaciones, registroTipoDA, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbDistribucionInstalaciones.guardaDistribucionLabTall(listaDistribucionInstalaciones, registroTipoDLT, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorDistribucionInstalaciones.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaDistribucionInstalaciones.getDtoDistribucionLabTallCPE().clear();
                listaDistribucionInstalaciones.getDtoDistribucionAulasCPE().clear();
                listaDistribucionInstalaciones.getDtoCapacidadInstaladaCiclosEscolares().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }

    public void cancelarArchivo() {
        listaDistribucionInstalaciones.getDtoDistribucionLabTallCPE().clear();
        listaDistribucionInstalaciones.getDtoDistribucionAulasCPE().clear();
        listaDistribucionInstalaciones.getDtoCapacidadInstaladaCiclosEscolares().clear();
        if (this.rutaArchivo != null) {
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
    
}
