/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.ListaExpedientes;
import mx.edu.utxj.pye.titulacion.interfaces.EjbTitulacionSeguimiento;
import mx.edu.utxj.pye.titulacion.dto.dtoExpedienteMatricula;
import mx.edu.utxj.pye.titulacion.dto.dtoPagosFinanzas;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Messages;
import org.omnifaces.cdi.ViewScoped;
/**
 *
 * @author UTXJ
 */
@Named(value = "controladorTitSegMat")
@ManagedBean
@ViewScoped
public class ControladorTitSegMatricula implements Serializable{

    private static final long serialVersionUID = -7349256943810409604L;

    @Getter @Setter private Integer expediente;
    @Getter @Setter private List<ListaExpedientes> nuevaListaExpedientes = new ArrayList<>();
    @Getter @Setter private dtoExpedienteMatricula nuevoDtoExpMat;
    @Getter @Setter private List<DocumentosExpediente> listaDocsExp;
    @Getter @Setter private DocumentosExpediente documentoExp;
    @Getter @Setter private AntecedentesAcademicos antecedentesAcademicos;
    @Getter @Setter private DatosTitulacion datosTitulacion;
    @Getter @Setter private Boolean aperturaDialogo;
    @Getter @Setter private Boolean valorValidacion;
    @Getter @Setter private ArrayList<dtoPagosFinanzas> listaDtoPagosFinanzas;
    @Getter @Setter private dtoPagosFinanzas nuevoDtoPagosFinanzas;
    
    @EJB private EjbTitulacionSeguimiento ejbTitulacionSeguimiento;
   
    // Para registrar clave del Personal que validó el expediente y/o los documentos
    @Getter @Setter private Integer clavePersonal;
    @Inject ControladorEmpleado controladorEmpleado;
    
    // Número de documentos que debe contener el expediente
    @Getter @Setter private Integer numTotalDocs;
    @Getter @Setter private Integer numDocs;
    
    @Inject ControladorFotoExpediente controladorFotoExpediente;
    
    @PostConstruct
    public void init() {
       
        consultaListaExpedientes();
        aperturaDialogo = Boolean.FALSE;
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
    }
   
    public void consultaListaExpedientes() {
        try {
            nuevaListaExpedientes = new ArrayList<>();
            nuevaListaExpedientes.clear();
            nuevaListaExpedientes = ejbTitulacionSeguimiento.consultaListaExpedientes();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegMatricula.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
     public void mostrarExpediente() {
       try {
            nuevoDtoExpMat = new dtoExpedienteMatricula();
            nuevoDtoExpMat = ejbTitulacionSeguimiento.mostrarExpediente(expediente);
            listaDocsExp = ejbTitulacionSeguimiento.mostrarExpediente(expediente).getDocumentosExpediente();
            consultaTotalDocsExp(expediente);
            Integer valor = nuevoDtoExpMat.getExpedientesTitulacion().getNivel();
            switch (valor) {
               case 1:
                   controladorFotoExpediente.mostrarFotografiaTSU(expediente);
                   break;
               case 2:
                   controladorFotoExpediente.mostrarFotografiaING(expediente);
                   break;
               case 4:
                   controladorFotoExpediente.mostrarFotografiaING(expediente);
                   break;
               default:
                   System.err.println("No existe nivel para buscar la fotografía");
                   break;
           }
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegMatricula.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void editarRegistro(DocumentosExpediente documento){
        documentoExp = documento;
        valorValidacion = documento.getValidadoTitulacion();
        Ajax.update("frmModalEdicion");
        Ajax.oncomplete("skin();");
        aperturaDialogo = Boolean.TRUE;
        forzarAperturaEdicionDialogo();
    }
    
     public void forzarAperturaEdicionDialogo(){
        if(getAperturaDialogo()){
            Ajax.oncomplete("PF('modalEdicion').show();");
           aperturaDialogo = Boolean.FALSE;
        }
    }
     
     public void guardarEdicion() {
        try {
            if(documentoExp.getValidadoTitulacion() == true && "Sin revisar".equals(documentoExp.getObservaciones())){
                documentoExp.setObservaciones("Listo");
            }
            if(documentoExp.getValidadoTitulacion() == false && "Sin revisar".equals(documentoExp.getObservaciones())){
                documentoExp.setObservaciones("No válido");
            }
            documentoExp = ejbTitulacionSeguimiento.actualizarDocExpediente(documentoExp);
            mostrarExpediente();
            Ajax.update("tblDocsExp");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTitSegMatricula.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     
    public List<DocumentosExpediente> consultarStatusDocumento(Integer claveDoc){
        return ejbTitulacionSeguimiento.getListaStatusPorDocumento(claveDoc);
    }
    
  
    public void guardarDatosTitulacion() {
        try {
            datosTitulacion = nuevoDtoExpMat.getDatosTitulacion();
            nuevoDtoExpMat = ejbTitulacionSeguimiento.guardarDatosTitulacion(datosTitulacion, expediente);
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTitSegMatricula.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarExpediente();
    }
    
    public void guardarAntAcad() {
        try {
            antecedentesAcademicos = nuevoDtoExpMat.getAntecedentesAcademicos();
            nuevoDtoExpMat = ejbTitulacionSeguimiento.guardarAntecedentesAcad(antecedentesAcademicos, expediente);
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTitSegMatricula.class.getName()).log(Level.SEVERE, null, ex);
        }
        mostrarExpediente();
    }
    
    public void consultarExpediente(Integer expediente) {
       try {
            nuevoDtoExpMat = new dtoExpedienteMatricula();
            nuevoDtoExpMat = ejbTitulacionSeguimiento.mostrarExpediente(expediente);
            listaDocsExp = ejbTitulacionSeguimiento.mostrarExpediente(expediente).getDocumentosExpediente();
            consultaTotalDocsExp(expediente);
            Integer valor = nuevoDtoExpMat.getExpedientesTitulacion().getNivel();
            switch (valor) {
               case 1:
                   controladorFotoExpediente.mostrarFotografiaTSU(expediente);
                   break;
               case 2:
                   controladorFotoExpediente.mostrarFotografiaING(expediente);
                   break;
               case 4:
                   controladorFotoExpediente.mostrarFotografiaING(expediente);
                   break;
               default:
                   System.err.println("No existe nivel para buscar la fotografía");
                   break;
           }
            
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegMatricula.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<ExpedientesTitulacion> consultarStatus(Integer expediente) throws Throwable{
        consultaTotalDocsExp(expediente);
        numDocs = ejbTitulacionSeguimiento.mostrarExpediente(expediente).getDocumentosExpediente().size();
        return ejbTitulacionSeguimiento.getListaStatusPorExpediente(expediente);
    }
    
    public void validarExpediente(Integer expediente) {
     
        try {
            ejbTitulacionSeguimiento.validarExpediente(expediente, clavePersonal);
            mostrarExpediente();
            Ajax.update("formMuestraExpediente");
            Messages.addGlobalInfo("La información se ha actualizado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegMatricula.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void consultaTotalDocsExp(Integer expediente) throws Throwable{
        ExpedientesTitulacion exp = ejbTitulacionSeguimiento.buscarExpedienteTitulacion(expediente);
        
        if ("LTF".equals(exp.getProgramaEducativo())) {
            numTotalDocs = 9;
        } else {
            numTotalDocs = 8;
        }
    }
    
}
