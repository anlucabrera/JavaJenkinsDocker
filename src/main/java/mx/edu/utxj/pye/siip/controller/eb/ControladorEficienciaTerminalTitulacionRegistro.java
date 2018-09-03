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
import mx.edu.utxj.pye.siip.entity.escolar.list.ListaEficienciaTerminalTitulacionRegistros;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbEficienciaTerminalTitulacionRegistro;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorEficienciaTerminalTitulacionRegistro implements Serializable{

    private static final long serialVersionUID = -2757897393394368408L;
     
    @Getter RegistrosTipo registroTipo;    
    @Getter EjesRegistro ejesRegistro;
    @Getter Short area;
    @Getter String rutaArchivo;

    @Getter @Setter private ListaEficienciaTerminalTitulacionRegistros listaEficienciaTerminalTitulacionRegistros;
    @Getter private Double promedioEficienciaTerminal, promedioTasaTitulacion, promedioRegistroDGP;
    
    @EJB
    EjbEficienciaTerminalTitulacionRegistro ejbEficienciaTerminalTitulacionRegistro;
    
    @Inject
    ControladorEmpleado controladorEmpleado;
    @Inject
    ControladorModulosRegistro controladorModulosRegistro;
    
    @PostConstruct
    public void init(){
        registroTipo = new RegistrosTipo();
        registroTipo.setRegistroTipo((short)4);
        ejesRegistro = new EjesRegistro();
        ejesRegistro.setEje(5);
        area = (short) controladorEmpleado.getNuevoOBJListaPersonal().getAreaOperativa();
    }
    
    public Double calculaEficienciaTerminal(Integer egresados, Integer nuevoIngreso){
        if(egresados > 0 || nuevoIngreso > 0){
            promedioEficienciaTerminal = ((double)egresados / (double)nuevoIngreso) * (double)100;
        }else{
            promedioEficienciaTerminal = 0.0;
        }
        promedioEficienciaTerminal = Math.round(promedioEficienciaTerminal * 100) / (double)100;
        return promedioEficienciaTerminal;
    }
    public Double calculaTasaTitulacion(Integer titulados, Integer egresados){
        if(titulados > 0 || egresados > 0){
            promedioTasaTitulacion = ((double)titulados / (double)egresados) * (double)100;
        }else{
            promedioTasaTitulacion = 0.0;
        }
        promedioTasaTitulacion = Math.round(promedioTasaTitulacion * 100) / (double)100;
        return promedioTasaTitulacion;
    }
    public Double calculaRegistroDGP(Integer registradosDGP, Integer titulados){
        if(registradosDGP > 0 || titulados > 0){
            promedioRegistroDGP = ((double)registradosDGP / (double)titulados) * (double)100;
        }else{
            promedioRegistroDGP = 0.0;
        }
        promedioRegistroDGP = Math.round(promedioRegistroDGP * 100) / (double)100;
        return promedioRegistroDGP;
    }
    
    public void listaEficienciaTerminalTitulacionRegistroPrevia(String rutaArchivo){
        try {
            if (rutaArchivo != null) {
                this.rutaArchivo = rutaArchivo;
                listaEficienciaTerminalTitulacionRegistros = ejbEficienciaTerminalTitulacionRegistro.getListaEficienciaTerminalTitulacionRegistros(rutaArchivo);
            }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
            if(rutaArchivo != null){
                ServicioArchivos.eliminarArchivo(rutaArchivo);
            }
        }
    }
    
    public void guardaEficienciaTerminalTitulacion(){
        if (listaEficienciaTerminalTitulacionRegistros != null) {
            try {
                ejbEficienciaTerminalTitulacionRegistro.guardaEficienciaTerminalTitulacionRegistros(listaEficienciaTerminalTitulacionRegistros, registroTipo, ejesRegistro, area, controladorModulosRegistro.getEventosRegistros());
                Messages.addGlobalInfo("La información se ha almacenado de manera correcta");
            } catch (Throwable ex) {
                Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
                Logger.getLogger(ControladorEficienciaTerminalTitulacionRegistro.class.getName()).log(Level.SEVERE, null, ex);
                if (this.rutaArchivo != null) {
                    ServicioArchivos.eliminarArchivo(rutaArchivo);
                }
            } finally {
                listaEficienciaTerminalTitulacionRegistros.getEficienciaTerminalTitulacionRegistros().clear();
                this.rutaArchivo = null;
            }
        } else {
            Messages.addGlobalWarn("¡Es necesario cargar un achivo!");
        }
    }
    
    public void cancelarArchivo(){
        listaEficienciaTerminalTitulacionRegistros.getEficienciaTerminalTitulacionRegistros().clear();
        if (this.rutaArchivo != null) {
            ServicioArchivos.eliminarArchivo(this.rutaArchivo);
            this.rutaArchivo = null;
        }
    }
    
}
