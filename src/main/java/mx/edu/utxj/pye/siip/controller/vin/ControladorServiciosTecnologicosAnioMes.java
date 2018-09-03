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
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.siip.controller.eb.ControladorModulosRegistro;
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaServiciosTecnologicosAnioMes;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbServiciosTecnologicosAnioMes;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorServiciosTecnologicosAnioMes implements Serializable{

    private static final long serialVersionUID = 4748011023304316735L;
    @Getter RegistrosTipo registroTipoST;
    @Getter RegistrosTipo registroTipoSTP;
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;
    
    @Getter @Setter private ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMes = new ListaServiciosTecnologicosAnioMes();
    @Getter @Setter private ListaServiciosTecnologicosAnioMes listaServiciosTecnologicosAnioMesParticipantes = new ListaServiciosTecnologicosAnioMes();
    
    @EJB
    EjbServiciosTecnologicosAnioMes ejbServiciosTecnologicosAnioMes;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        registroTipoST = new RegistrosTipo();
        registroTipoST.setRegistroTipo((short)36);
        
        registroTipoSTP = new RegistrosTipo();
        registroTipoSTP.setRegistroTipo((short)37);
        
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(4);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public void listaServiciosTecnologicosAnioMesPrevia(String rutaArchivo){
        try {
             if(rutaArchivo != null){
                this.rutaArchivo = rutaArchivo;
            listaServiciosTecnologicosAnioMes = ejbServiciosTecnologicosAnioMes.getListaServiciosTecnologicosAnioMes(rutaArchivo);
            listaServiciosTecnologicosAnioMesParticipantes = ejbServiciosTecnologicosAnioMes.getListaServiciosTecnologicosParticipantes(rutaArchivo);
           }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaServiciosTecnologicos() {
        if (listaServiciosTecnologicosAnioMes != null) {
            try {
                if (listaServiciosTecnologicosAnioMes != null) {
                    ejbServiciosTecnologicosAnioMes.guardaServiciosTecnologicosAnioMes(listaServiciosTecnologicosAnioMes, registroTipoST, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                    ejbServiciosTecnologicosAnioMes.guardaServiciosTecnologicosParticipantes(listaServiciosTecnologicosAnioMesParticipantes, registroTipoSTP, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                } else {
                    Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
                }
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorServiciosTecnologicosAnioMes.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaServiciosTecnologicosAnioMes.getServiciosTecnologicosAnioMes().clear();
                listaServiciosTecnologicosAnioMesParticipantes.getDtoServiciosTecnologicosParticipantes().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }

    public void cancelarArchivo() {
        listaServiciosTecnologicosAnioMes.getServiciosTecnologicosAnioMes().clear();
        listaServiciosTecnologicosAnioMesParticipantes.getDtoServiciosTecnologicosParticipantes().clear();
        if (this.rutaArchivo != null) {
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
 
}
