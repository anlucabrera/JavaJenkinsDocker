/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.ca;

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
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaServiciosEnfermeriaCicloPeriodos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbServiciosEnfermeriaCicloPeriodos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named 
@ManagedBean
@ViewScoped
public class ControladorServiciosEnfermeriaCicloPeriodos implements Serializable{

    private static final long serialVersionUID = 8477182284194275657L;
    
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;
    
    @Getter @Setter private ListaServiciosEnfermeriaCicloPeriodos listaServiciosEnfermeriaCicloPeriodos = new ListaServiciosEnfermeriaCicloPeriodos();
    
    @EJB
    EjbServiciosEnfermeriaCicloPeriodos ejbServiciosEnfermeriaCicloPeriodos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)7);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(3);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public void listaServiciosEnfermeriaCicloPeriodoPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                this.rutaArchivo = rutaArchivo;
                listaServiciosEnfermeriaCicloPeriodos = ejbServiciosEnfermeriaCicloPeriodos.getListaServiciosEnfermeriaCicloPeriodos(rutaArchivo);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorServiciosEnfermeriaCicloPeriodos.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaServiciosEnfermeriaCicloPeriodo(){
        if (listaServiciosEnfermeriaCicloPeriodos != null) {
            try {
                ejbServiciosEnfermeriaCicloPeriodos.guardaServiciosEnfermeriaCicloPeriodos(listaServiciosEnfermeriaCicloPeriodos, registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorServiciosEnfermeriaCicloPeriodos.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaServiciosEnfermeriaCicloPeriodos.getServiciosEnfermeriaCicloPeriodos().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
        listaServiciosEnfermeriaCicloPeriodos.getServiciosEnfermeriaCicloPeriodos().clear();
        if (this.rutaArchivo != null) {
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
    
}
