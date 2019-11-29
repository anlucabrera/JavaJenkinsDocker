/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.controller;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.controladores.ch.ControladorEmpleado;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.titulacion.interfaces.EjbTitulacionSeguimiento;
import mx.edu.utxj.pye.titulacion.dto.dtoExpedienteMatricula;
import mx.edu.utxj.pye.titulacion.dto.dtoPagosFinanzas;
import org.omnifaces.util.Ajax;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import java.io.IOException;
import javax.enterprise.context.SessionScoped;
/**
 *
 * @author UTXJ
 */
@Named(value = "controladorTitSegGen")
@ManagedBean
@SessionScoped
public class ControladorTitSegGeneracion implements Serializable{

    private static final long serialVersionUID = 5303936724764561828L;

    @Getter @Setter private Integer expediente;
    @Getter @Setter private dtoExpedienteMatricula nuevoDtoExpMat;
    @Getter @Setter private List<DocumentosExpediente> listaDocsExp;
    @Getter @Setter private DocumentosExpediente documentoExp;
    @Getter @Setter private AntecedentesAcademicos antecedentesAcademicos;
    @Getter @Setter private DatosTitulacion datosTitulacion;
    @Getter @Setter private Boolean aperturaDialogo, aperturaPagos;
    @Getter @Setter private Boolean valorValidacion;
    @Getter @Setter private ArrayList<dtoPagosFinanzas> listaDtoPagosFinanzas;
    @Getter @Setter private dtoPagosFinanzas nuevoDtoPagosFinanzas;
    @Getter @Setter private ExpedientesTitulacion expedientesTitulacion;
    
    @EJB private EjbTitulacionSeguimiento ejbTitulacionSeguimiento;
    
    
    // Para consulta por Generación y Programa Educativo 
    @Getter @Setter private Generaciones generacionTSU, generacionING;
    @Getter @Setter private AreasUniversidad programaSeleccionadoTSU, programaSeleccionadoING;
    @Getter @Setter private List<Generaciones> generacionesING, generacionesTSU;
    @Getter @Setter private List<AreasUniversidad> programasPorGeneracionING, programasPorGeneracionTSU;
    @Getter @Setter private List<dtoExpedienteMatricula> listaING, listaTSU;
    @Getter @Setter private String matricula;
    
    // Para registrar clave del Personal que validó el expediente y/o los documentos
    @Getter @Setter private Integer clavePersonal;
    @Inject ControladorEmpleado controladorEmpleado;
    
    // Número de documentos que debe contener el expediente
    @Getter @Setter private Integer numTotalDocs;
    @Getter @Setter private Integer numDocs, numDocsEsc;
    
    @Inject ControladorFotoExpediente controladorFotoExpediente;
    
    @PostConstruct
    public void init() {
       
        aperturaDialogo = Boolean.FALSE;
        aperturaPagos = Boolean.FALSE;
        initFiltrosING();
        initFiltrosTSU();
        clavePersonal = controladorEmpleado.getNuevoOBJListaPersonal().getClave();
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
            consultarExpediente(documentoExp.getExpediente().getExpediente());
            Ajax.update("tblDocsExp");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTitSegGeneracion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
     
    public List<DocumentosExpediente> consultarStatusDocumento(Integer claveDoc){
        return ejbTitulacionSeguimiento.getListaStatusPorDocumento(claveDoc);
    }
    
    public void initFiltrosING(){
        generacionesING = ejbTitulacionSeguimiento.getGeneracionesConregistroING();
        programasPorGeneracionING = ejbTitulacionSeguimiento.getExpedientesPorGeneracionesING(generacionesING.get(0));
        cargarListaPorGenProgING();
    }
   
     public void actualizarProgramasING(ValueChangeEvent e){
        generacionING = (Generaciones)e.getNewValue();
        programasPorGeneracionING = ejbTitulacionSeguimiento.getExpedientesPorGeneracionesING(generacionING);
        cargarListaPorGenProgING();
    }
     
    public void initFiltrosTSU(){
        generacionesTSU = ejbTitulacionSeguimiento.getGeneracionesConregistroTSU();
        programasPorGeneracionTSU = ejbTitulacionSeguimiento.getExpedientesPorGeneracionesTSU(generacionesTSU.get(0));
        cargarListaPorGenProgTSU();
    }
   
     public void actualizarProgramasTSU(ValueChangeEvent e){
        generacionTSU = (Generaciones)e.getNewValue();
        programasPorGeneracionTSU = ejbTitulacionSeguimiento.getExpedientesPorGeneracionesTSU(generacionTSU);
        cargarListaPorGenProgTSU();
    }
     
    public void cargarListaPorGenProgING(){
      
        if(generacionING == null && programaSeleccionadoING == null){
            
            generacionING= generacionesING.get(0);
            programaSeleccionadoING = programasPorGeneracionING.get(0);
        
        }
      
      listaING = ejbTitulacionSeguimiento.getListaExpedientesPorProgramaGeneracion(programaSeleccionadoING, generacionING); 
      
    }
    
    public void cargarListaPorGenProgTSU(){
      
        if(generacionTSU== null && programaSeleccionadoTSU == null){
            
            generacionTSU= generacionesTSU.get(0);
            programaSeleccionadoTSU = programasPorGeneracionTSU.get(0);
        
        }
      listaTSU = ejbTitulacionSeguimiento.getListaExpedientesPorProgramaGeneracion(programaSeleccionadoTSU, generacionTSU); 
      
    }
    public void guardarDatosTitulacion() {
        try {
            datosTitulacion = nuevoDtoExpMat.getDatosTitulacion();
            datosTitulacion = ejbTitulacionSeguimiento.guardarDatTitInd(datosTitulacion, expediente);
            consultarExpediente(datosTitulacion.getExpediente().getExpediente());
            Ajax.update("formMuestraExpediente");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTitSegGeneracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void guardarAntAcad() {
        try {
            antecedentesAcademicos = nuevoDtoExpMat.getAntecedentesAcademicos();
            antecedentesAcademicos = ejbTitulacionSeguimiento.guardarAntAcadInd(antecedentesAcademicos, expediente);
            expedientesTitulacion = ejbTitulacionSeguimiento.buscarExpedienteMatricula(antecedentesAcademicos.getMatricula().getMatricula());
            consultarExpediente(expedientesTitulacion.getExpediente());
            Ajax.update("formMuestraExpediente");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getMessage());
            Logger.getLogger(ControladorTitSegGeneracion.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            Ajax.update("frmDocsExp");
            Ajax.update("frmDocExp");
            Ajax.update("frmFotoExp");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegGeneracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public List<ExpedientesTitulacion> consultarStatus(Integer expediente) throws Throwable{
        consultaTotalDocsExp(expediente);
        numDocs = ejbTitulacionSeguimiento.mostrarExpediente(expediente).getDocumentosExpediente().size();
        numDocsEsc = ejbTitulacionSeguimiento.consultarDocsEscolares(expediente);
        return ejbTitulacionSeguimiento.getListaStatusPorExpediente(expediente);
    }
    
    public void validarExpediente(Integer expediente) {
     
        try {
            ejbTitulacionSeguimiento.validarExpediente(expediente, clavePersonal);
            cargarListaPorGenProgING();
            cargarListaPorGenProgTSU();
            Ajax.update("formMuestraDatosActivos");
            consultarExpediente(expediente);
            Ajax.update("formMuestraExpediente");
            Messages.addGlobalInfo("La información se ha actualizado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegGeneracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void descargarReporteGeneracionTSU() throws IOException, Throwable{
        Integer nivel = 1;
        File f = new File(ejbTitulacionSeguimiento.getReporteGeneracionTSU(generacionTSU, nivel));
        Faces.sendFile(f, true);
    }
    
    public void descargarReporteGeneracionING() throws IOException, Throwable{
        Integer nivel = 2;
        File f = new File(ejbTitulacionSeguimiento.getReporteGeneracionING(generacionING, nivel));
        Faces.sendFile(f, true);
    }
    
    public void consultaTotalDocsExp(Integer expediente) throws Throwable{
        ExpedientesTitulacion exp = ejbTitulacionSeguimiento.buscarExpedienteTitulacion(expediente);
        Integer tsu = 1;
        
        if ("LTF".equals(exp.getProgramaEducativo())) {
            numTotalDocs = 9;
        }
        else if (tsu.equals(exp.getNivel())) {
            numTotalDocs = 7;
        }
        else {
            numTotalDocs = 8;
        }
    }
    
    public void consultarListaPagos(String matricula) {
       try {
            listaDtoPagosFinanzas = new ArrayList<>();
            listaDtoPagosFinanzas = ejbTitulacionSeguimiento.getListaDtoPagosFinanzas(matricula);
            Ajax.update("frmModalPagos");
            Ajax.oncomplete("skin();");
            aperturaPagos = Boolean.TRUE;
            forzarAperturaPagosDialogo();
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegGeneracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     public void forzarAperturaPagosDialogo(){
        if(getAperturaPagos()){
           Ajax.oncomplete("PF('modalPagos').show();");
           aperturaPagos = Boolean.FALSE;
        }
    }
       
}
