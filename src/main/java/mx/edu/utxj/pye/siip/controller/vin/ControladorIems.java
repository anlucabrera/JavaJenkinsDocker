/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.siip.controller.vin;

import com.github.adminfaces.starter.infra.security.LogonMB;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.siip.entity.prontuario.list.ListaIemsPrevia;
import mx.edu.utxj.pye.sgi.entity.pye2.ModulosRegistrosUsuarios;
import mx.edu.utxj.pye.sgi.entity.pye2.Iems;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.siip.dto.vin.DtoIems;
import mx.edu.utxj.pye.siip.interfaces.eb.EjbModulos;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbIems;
import mx.edu.utxj.pye.siip.interfaces.vin.EjbPlantillasVINExcel;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */
@Named
@ManagedBean
@ViewScoped
public class ControladorIems implements Serializable{

    private static final long serialVersionUID = 1785268843955234056L;
    
    @Getter @Setter DtoIems dto;
    @Getter @Setter private Integer estado = 0, municipio = 0;
    @Getter @Setter private List<SelectItem> selectEstados, SelectMunicipio;
    @Getter @Setter private List<Iems> listaIems, listaIemsFiltro;
    @Getter @Setter private List<ModulosRegistrosUsuarios> listaReg;
    @Getter @Setter private Integer clavePersonal;
    @Getter @Setter private Short claveRegistro;
   
    @EJB private EjbIems ejbIems;
    @EJB private EJBSelectItems eJBSelectItems;
    @EJB EjbPlantillasVINExcel ejbPlantillasVINExcel;
    @EJB EjbModulos ejbModulos;
    
    @Inject ControladorEmpleado controladorEmpleado;
    
    @Getter @Setter private ListaIemsPrevia listaIemsPrevia = new ListaIemsPrevia();
    
    @Getter @Setter private Iems nuevoIems;
  
    @Inject LogonMB logonMB;
    @Getter private Boolean cargado = false;
    
    @PostConstruct
    public void init() {
        if (!logonMB.getUsuarioTipo().equals(UsuarioTipo.TRABAJADOR)) {
            return;
        }
        cargado = true;
        try {
        dto = new DtoIems();
        selectEstados = eJBSelectItems.itemEstados();
//        registroIems = ejbIems.getRegistroIemsMensual();
//                        listaIems = new ArrayList<>();
//                        System.err.println("entra al init despues de inicializar la lista");
//                        listaIems = ejbIems.getIems();
//                        System.err.println("la lista de iems tiene un tamaño de : " + listaIems.size());
//        seleccionaEstado();

        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
        claveRegistro = 28;
        consultarPermiso();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorIems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void descargarPlantilla() throws IOException, Throwable{
        File f = new File(ejbPlantillasVINExcel.getPlantillaIEMS());
        Faces.sendFile(f, true);
    }
        
    public void seleccionarmunicipio(Integer estado){
        SelectMunicipio = eJBSelectItems.itemMunicipiosByClave(this.estado);
    }
    public void obtenerListaIems(){
        listaIems = ejbIems.filtroIems(estado, municipio);
        if(listaIems == null || listaIems.isEmpty()){
            Messages.addGlobalWarn("No se encuentan IEMS registradas en el municipio seleccionado");
        }
//        System.err.println("el resultado de la busqueda ");
//        listaIems.forEach(System.err::println);
    
    }
    public void listaIemsGuardar(String rutaArchivo) {
        try {
            municipio = 0;
            listaIemsPrevia = ejbIems.getListaIemsPrevia(rutaArchivo);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorBolsaTrabajo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
   
    public void guardaIems() {
         
        try {
            ejbIems.guardaIems(listaIemsPrevia);
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cancelarArchivo(){
       listaIemsPrevia.getDTOIems().clear();
    }
    
    public void cambiarStatusIEMS(Integer iemsID) {
     
        try {
            ejbIems.cambiarStatusIEMS(iemsID);
            obtenerListaIems();
            Ajax.update("formIEMSMesActual");
            Messages.addGlobalInfo("La información se ha actualizado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorIems.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public List<Iems> consultarStatus(Integer iemsID){
        return ejbIems.getListaStatusPorRegistro(iemsID);
    }
    
     public void consultarPermiso(){
        listaReg = ejbModulos.getListaPermisoPorRegistro(clavePersonal, claveRegistro);
        if(listaReg == null || listaReg.isEmpty()){
            Messages.addGlobalWarn("Usted no cuenta con permiso para visualizar este apartado");
        }
    }
     
    public void editarRegistro(Iems registro){
        dto.setIemsSeleccionada(registro);
        nuevoIems = dto.getIemsSeleccionada();
        Ajax.update("frmModalEdicion");
        Ajax.oncomplete("skin();");
        dto.setForzarAperturaDialogo(Boolean.TRUE);
        forzarAperturaEdicionDialogo();
    }
    
    public void forzarAperturaEdicionDialogo(){
        if(dto.getForzarAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicion').show();");
            dto.setForzarAperturaDialogo(Boolean.FALSE);
        }
    }
    
    public void guardarEdicion() {
         try {
            nuevoIems = ejbIems.actualizarIems(nuevoIems);
            Ajax.update("formIEMSMesActual");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorIems.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
