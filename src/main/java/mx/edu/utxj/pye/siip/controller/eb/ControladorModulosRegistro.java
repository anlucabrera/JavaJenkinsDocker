/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.eb;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.siip.controller.ca.ControladorActFormacionIntegral;
//import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistroUsuario;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@SessionScoped
public class ControladorModulosRegistro implements Serializable {
    
    private static final long serialVersionUID = 3085151133434299827L;
    @Getter @Setter private Integer personal;
    
    //Variables para la almacenar los criterios de los registros
    @Getter EventosRegistros eventosRegistros;
    
    @Inject
    ControladorEmpleado controladorEmpleado;
    
    @EJB
    private EjbModulos ejbModulos;

    @Getter @Setter private List<ModulosRegistrosUsuarios> listaEjes = new ArrayList<>();
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaModulosRegistroEB = new ArrayList<>();
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaModulosRegistroCH = new ArrayList<>();
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaModulosRegistroPA = new ArrayList<>();
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaModulosRegistroVIN = new ArrayList<>();
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaModulosRegistroCA = new ArrayList<>();
    
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
            personal = controladorEmpleado.getEmpleadoLogeado();
            mostrarEjes();
            mostrarRegistros();
            iniciarEventoRegistro();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorModulosRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarEjes() {
        try {
            this.listaEjes.clear();
            this.listaEjes = ejbModulos.getEjesRectores(personal);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorModulosRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mostrarRegistros() {
        try {
            this.listaModulosRegistroEB.clear();
            this.listaModulosRegistroEB = ejbModulos.getModulosRegistroUsuario(5, personal);
            this.listaModulosRegistroCH = ejbModulos.getModulosRegistroUsuario(1, personal);
            this.listaModulosRegistroPA = ejbModulos.getModulosRegistroUsuario(2, personal);
            this.listaModulosRegistroCA = ejbModulos.getModulosRegistroUsuario(3, personal);
            this.listaModulosRegistroVIN = ejbModulos.getModulosRegistroUsuario(4, personal);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorModulosRegistro.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void iniciarEventoRegistro(){
        eventosRegistros = new EventosRegistros();
        eventosRegistros = ejbModulos.getEventoRegistro();
    }
   
    public AreasUniversidad consultaAreaRegistro(Short claveModuloRegistroEspecifico) {
        try {
            ResultadoEJB<List<ModulosRegistrosUsuarios>> mr = ejbModulos.getListaPermisoPorRegistro(personal, claveModuloRegistroEspecifico);
            if(mr.getCorrecto()){
                if(mr.getValor().get(0).getAreaRegistro()!= null){
                    ResultadoEJB<AreasUniversidad> resArea = ejbModulos.getAreaUniversidadPrincipalRegistro((short) mr.getValor().get(0).getAreaRegistro());
                    if(resArea.getCorrecto()){
                        return resArea.getValor();
                    }else{
//                        Messages.addGlobalFatal(resArea.getMensaje());
                        return null;
                    }
                }else{
//                    Messages.addGlobalFatal(mr.getMensaje());
                    return null;
                }
            }else{
//                Messages.addGlobalFatal(mr.getMensaje());
                return null;
            }
        } catch (Throwable ex) {
            Logger.getLogger(ControladorActFormacionIntegral.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
