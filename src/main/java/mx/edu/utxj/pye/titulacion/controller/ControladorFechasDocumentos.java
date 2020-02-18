/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.ejb.EJBSelectItems;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos;
import mx.edu.utxj.pye.titulacion.dto.DtoFechasDocumentos;
import mx.edu.utxj.pye.titulacion.interfaces.EjbFechasDocumentos;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;

/**
 *
 * @author UTXJ
 */

@Named(value = "controladorFecDocs")
@ManagedBean
@ViewScoped
public class ControladorFechasDocumentos implements Serializable{

   private static final long serialVersionUID = 6878126949579656934L;
   
   @Getter @Setter private List<DtoFechasDocumentos> listaFecDocs;
   @Getter @Setter private DtoFechasDocumentos dtoFecDocs;
   @Getter @Setter private FechasDocumentos fechasDocumentos;
      
   @Getter @Setter private Boolean aperturaDialogoEdicion;
   @Getter @Setter private Boolean aperturaDialogoNuevo;
   
   
   /* Filtrado */
   @Getter @Setter private Generaciones generacionSeleccionada;
   @Getter @Setter private List<Generaciones> generacionesRegistradas;
   
   /* Módulo nuevo registro */
   @Getter @Setter private List<SelectItem> listaGeneraciones, listaProgramasEducativos;
   @Getter @Setter private Short genFecDoc, progEduFecDoc;
   @Getter @Setter private Date fechaInicio, fechaFin, actaExencion;
   @EJB EJBSelectItems eJBSelectItems;
    
   @EJB private EjbFechasDocumentos ejbFechasDocumentos;
  
   @PostConstruct
    public void init() {
//       cargarListaFecDocs();
       aperturaDialogoEdicion = Boolean.FALSE;
       aperturaDialogoNuevo = Boolean.FALSE;
       
       initFiltros();
              
       listaGeneraciones = eJBSelectItems.itemGeneraciones();
       listaProgramasEducativos = eJBSelectItems.itemProgramasEducativos();
    } 
    
    public void initFiltros(){
        generacionesRegistradas = ejbFechasDocumentos.getGeneracionesRegistradas();
        cargarListaFecDocs();
    }
    
    public void cargarListaFecDocs(){
        
        if (generacionSeleccionada == null) {

            generacionSeleccionada = generacionesRegistradas.get(0);

        }
      listaFecDocs = ejbFechasDocumentos.getListaFechasDocumentosGeneracion(generacionSeleccionada); 
      
    }
    
    public void eliminarFecDoc(DtoFechasDocumentos fecDocs){
        Boolean eliminado = ejbFechasDocumentos.eliminarFecDocsGeneracion(fecDocs);
        if(eliminado){ 
            Messages.addGlobalInfo("El documento se eliminó correctamente.");
            cargarListaFecDocs();
        }else Messages.addGlobalError("El documento no ha podido eliminarse.");
    }
    
    public void editarRegistro(DtoFechasDocumentos fecDocs){
        dtoFecDocs = fecDocs;
        Ajax.update("frmModalEdicion");
        Ajax.oncomplete("skin();");
        aperturaDialogoEdicion = Boolean.TRUE;
        forzarAperturaEdicionDialogo();
    }
    
     public void forzarAperturaEdicionDialogo(){
        if(getAperturaDialogoEdicion()){
            Ajax.oncomplete("PF('modalEdicion').show();");
           aperturaDialogoEdicion = Boolean.FALSE;
        }
    }
     
     public void guardarEdicion() {
        try {
            dtoFecDocs = ejbFechasDocumentos.actualizarFecDocumentos(dtoFecDocs);
            cargarListaFecDocs();
            Ajax.update("dtbFechasDocsGen");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorFechasDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     
     public void nuevoRegistro(){
        Ajax.update("frmModalNuevo");
        Ajax.oncomplete("skin();");
        aperturaDialogoNuevo = Boolean.TRUE;
        forzarAperturaNuevoDialogo();
    }
    
     public void forzarAperturaNuevoDialogo(){
        if(getAperturaDialogoNuevo()){
            Ajax.oncomplete("PF('modalNuevo').show();");
            aperturaDialogoNuevo = Boolean.FALSE;
        }
    }
     
     public void guardarNuevo() {
          fechasDocumentos = new FechasDocumentos();
        try {
            fechasDocumentos.setGeneracion(genFecDoc);
            fechasDocumentos.setProgramaEducativo(progEduFecDoc);
            fechasDocumentos.setFechaInicio(fechaInicio);
            fechasDocumentos.setFechaFin(fechaFin);
            fechasDocumentos.setActaExencion(actaExencion);
            ejbFechasDocumentos.guardarFecDocumentos(fechasDocumentos);
            
            initFiltros();
            Ajax.update("dtbFechasDocsGen");
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorFechasDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
