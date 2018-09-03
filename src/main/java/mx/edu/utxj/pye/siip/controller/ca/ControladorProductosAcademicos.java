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
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaProductosAcademicos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbProductosAcademicos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorProductosAcademicos implements Serializable{    

    private static final long serialVersionUID = 7685788092620440196L;
    @Getter @Setter private Integer tabuladorActivo;
    
    @Getter RegistrosTipo registroTipoPA;
    @Getter RegistrosTipo registroTipoPAP;
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;
    
    @Getter @Setter private ListaProductosAcademicos listaProductosAcademicos = new ListaProductosAcademicos();
    @Getter @Setter private ListaProductosAcademicos listaProductosAcademicosPersonal = new ListaProductosAcademicos();
    
    @EJB
    EjbProductosAcademicos ejbProductosAcademicos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        registroTipoPA = new RegistrosTipo();
        registroTipoPA.setRegistroTipo((short)41);
        
        registroTipoPAP = new RegistrosTipo();
        registroTipoPAP.setRegistroTipo((short)42);
        
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(3);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public void listaProductosAcademicosPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                this.rutaArchivo = rutaArchivo;
                if (listaProductosAcademicos != null || listaProductosAcademicosPersonal != null) {
                    listaProductosAcademicos = ejbProductosAcademicos.getListaProductosAcademicos(rutaArchivo);
                    listaProductosAcademicosPersonal = ejbProductosAcademicos.getListaProductosAcademicosPersonal(rutaArchivo);
                }
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorProductosAcademicos.class.getName()).log(Level.SEVERE, null, ex);
            if (rutaArchivo != null) {
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void irATabulador(Integer tabuladorActivo){ 
        this.tabuladorActivo = tabuladorActivo;
    }
    
    public void guardaProductosAcademicos() {
        if (listaProductosAcademicos != null) {
            try {
                ejbProductosAcademicos.guardaProductosAcademicos(listaProductosAcademicos, registroTipoPA, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbProductosAcademicos.guardaProductosAcademicosPersonal(listaProductosAcademicosPersonal, registroTipoPAP, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorProductosAcademicos.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaProductosAcademicos.getDtoProductosAcademicos().clear();
                listaProductosAcademicosPersonal.getDtoProductosAcademicosPersonal().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public boolean verificaPertenencia(String productoAcademico, String productoAcademicoPersonal){
        if(productoAcademico.equals(productoAcademicoPersonal)){
            return true;
        }else{
            return false;
        }
    }
    
    public void cancelarArchivo(){
        listaProductosAcademicos.getDtoProductosAcademicos().clear();
        listaProductosAcademicosPersonal.getDtoProductosAcademicosPersonal().clear();
        if (this.rutaArchivo != null) {
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
    
}
