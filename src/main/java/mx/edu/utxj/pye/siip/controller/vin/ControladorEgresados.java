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
import mx.edu.utxj.pye.siip.entity.vinculacion.list.ListaEgresados;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbEgresados;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorEgresados implements Serializable{
    private static final long serialVersionUID = 689975610878160616L;
    
    @Getter @Setter private ListaEgresados listaEgresados = new ListaEgresados();
    
    @Getter RegistrosTipo registroTipoAE;
    @Getter RegistrosTipo registroTipoAEE;
    @Getter RegistrosTipo registroTipoNO;
    @Getter RegistrosTipo registroTipoNI;
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;
    
    @EJB
    EjbEgresados ejbEgresados;
    
    @Inject ControladorEmpleado controladorEmpleado;
    @Inject ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        //        Variables que se obtendrán mediante un método
        registroTipoAE = new RegistrosTipo();
        registroTipoAE.setRegistroTipo((short)21);
        
        registroTipoAEE = new RegistrosTipo();
        registroTipoAEE.setRegistroTipo((short)22);
        
        registroTipoNO = new RegistrosTipo();
        registroTipoNO.setRegistroTipo((short)23);
        
        registroTipoNI = new RegistrosTipo();
        registroTipoNI.setRegistroTipo((short)24);
        
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(4);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public void listaEgresadosPrevia(String rutaArchivo) {
        try {
            if (rutaArchivo != null) {
                this.rutaArchivo = rutaArchivo;
                listaEgresados = ejbEgresados.getListaEgresados(rutaArchivo);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEgresados.class.getName()).log(Level.SEVERE, null, ex);
            if (rutaArchivo != null) {
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }

    public void guardaEgresados() {
        if (listaEgresados != null) {
            try {
                ejbEgresados.guardaActividadEgresadoGeneracion(listaEgresados.getDtoActividadEgresadosGeneracion(), registroTipoAE, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbEgresados.guardaActividadEcnomicaEgresadoG(listaEgresados.getDtoActividadEconomicaEgresadoG(), registroTipoAEE, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbEgresados.guardaNivelOcupacionEgresadoG(listaEgresados.getDtoNivelOcupacionEgresadosG(), registroTipoNO, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                ejbEgresados.guardaNivelIngresoEgresadoG(listaEgresados.getDtoNivelIngresoEgresadosG(), registroTipoNI, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorEgresados.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaEgresados.getDtoActividadEconomicaEgresadoG().clear();
                listaEgresados.getDtoActividadEgresadosGeneracion().clear();
                listaEgresados.getDtoNivelIngresoEgresadosG().clear();
                listaEgresados.getDtoNivelOcupacionEgresadosG().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }

    }

    public void cancelarArchivo() {
        listaEgresados.getDtoActividadEconomicaEgresadoG().clear();
        listaEgresados.getDtoActividadEgresadosGeneracion().clear();
        listaEgresados.getDtoNivelIngresoEgresadosG().clear();
        listaEgresados.getDtoNivelOcupacionEgresadosG().clear();
        if (this.rutaArchivo != null) {
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
}
