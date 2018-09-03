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
import mx.edu.utxj.pye.siip.entity.caphum.list.ListaCuerposAcademicos;
import mx.edu.utxj.pye.siip.interfaces.ca.EjbCuerposAcademicos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorCuerposAcademicos implements Serializable{

    private static final long serialVersionUID = 6694975550961361657L;
    
    @Getter RegistrosTipo registrosTipoCA;
    @Getter RegistrosTipo registrosTipoCAP;
    @Getter RegistrosTipo registrosTipoCALI;
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;
    
    @Getter @Setter private ListaCuerposAcademicos listaCuerposAcademicos = new ListaCuerposAcademicos();
    @Getter @Setter private ListaCuerposAcademicos listaCuerposAcademicosIntegrantes = new ListaCuerposAcademicos();
    @Getter @Setter private ListaCuerposAcademicos listaCuerposAcademicosLineas = new ListaCuerposAcademicos();
    
    @EJB
    EjbCuerposAcademicos ejbCuerposAcademicos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        registrosTipoCA = new RegistrosTipo();
        registrosTipoCA.setRegistroTipo((short)38);
        
        registrosTipoCAP = new RegistrosTipo();
        registrosTipoCAP.setRegistroTipo((short)39);
        
        registrosTipoCALI = new RegistrosTipo();
        registrosTipoCALI.setRegistroTipo((short)40);
        
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(3);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public void listaCuerposAcademicosPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                this.rutaArchivo = rutaArchivo;
                listaCuerposAcademicos = ejbCuerposAcademicos.getListaCuerposAcademicos(rutaArchivo);
                listaCuerposAcademicosIntegrantes = ejbCuerposAcademicos.getListaCuerpAcadIntegrantes(rutaArchivo);
                listaCuerposAcademicosLineas = ejbCuerposAcademicos.getListaCuerpAcadLineas(rutaArchivo);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorCuerposAcademicos.class.getName()).log(Level.SEVERE, null, ex);
            if (rutaArchivo != null) {
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaCuerposAcademicos() {
        if (listaCuerposAcademicos != null) {
            try {
                ejbCuerposAcademicos.guardaCuerposAcademicos(listaCuerposAcademicos, registrosTipoCA, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbCuerposAcademicos.guardaCuerpAcadIntegrantes(listaCuerposAcademicosIntegrantes, registrosTipoCAP, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbCuerposAcademicos.guardaCuerpAcadLineas(listaCuerposAcademicosLineas, registrosTipoCALI, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorCuerposAcademicos.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaCuerposAcademicos.getDtoCuerposAcademicosR().clear();
                listaCuerposAcademicosIntegrantes.getDtoCuerpAcadIntegrantes().clear();
                listaCuerposAcademicosLineas.getCuerpAcadLineas().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
        listaCuerposAcademicos.getDtoCuerposAcademicosR().clear();
        listaCuerposAcademicosIntegrantes.getDtoCuerpAcadIntegrantes().clear();
        listaCuerposAcademicosLineas.getCuerpAcadLineas().clear();
        if(this.rutaArchivo != null){
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
}
