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
import mx.edu.utxj.pye.sgi.entity.titulacion.ListaExpedientes;
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
    @Getter @Setter private ExpedientesTitulacion expedientesTitulacion;
    
    @EJB private EjbTitulacionSeguimiento ejbTitulacionSeguimiento;
    
    
    // Para consulta por Generación y Programa Educativo 
    @Getter @Setter private Generaciones generacion;
    @Getter @Setter private AreasUniversidad programaSeleccionado;
    @Getter @Setter private List<Generaciones> generaciones;
    @Getter @Setter private List<AreasUniversidad> programasPorGeneracion;
    @Getter @Setter private List<dtoExpedienteMatricula> lista;
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
        initFiltros();
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
    
    public void initFiltros(){
        generaciones = ejbTitulacionSeguimiento.getGeneracionesConregistro();
        programasPorGeneracion = ejbTitulacionSeguimiento.getExpedientesPorGeneraciones(generaciones.get(0));
        cargarListaPorGenProg();
    }
   
     public void actualizarProgramas(ValueChangeEvent e){
        generacion = (Generaciones)e.getNewValue();
        programasPorGeneracion = ejbTitulacionSeguimiento.getExpedientesPorGeneraciones(generacion);
        cargarListaPorGenProg();
    }
     
    public void cargarListaPorGenProg(){
      
        if(generacion == null && programaSeleccionado == null){
            
            generacion= generaciones.get(0);
            programaSeleccionado = programasPorGeneracion.get(0);
        
        }
      
      lista = ejbTitulacionSeguimiento.getListaExpedientesPorProgramaGeneracion(programaSeleccionado, generacion); 
      
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
            controladorFotoExpediente.mostrarFotografia(expediente);
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
            cargarListaPorGenProg();
            Ajax.update("formMuestraDatosActivos");
            consultarExpediente(expediente);
            Ajax.update("formMuestraExpediente");
            Messages.addGlobalInfo("La información se ha actualizado de manera correcta");
        } catch (Throwable ex) {
            Messages.addGlobalFatal("Ocurrió un error (" + (new Date()) + "): " + ex.getCause().getMessage());
            Logger.getLogger(ControladorTitSegGeneracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void descargarReporteGeneracion() throws IOException, Throwable{
        File f = new File(ejbTitulacionSeguimiento.getReporteGeneracion(generacion));
        Faces.sendFile(f, true);
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
