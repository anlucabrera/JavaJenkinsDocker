/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.dto.AbstractRol;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;

/**
 *
 * @author UTXJ
 */
public class AdministracionCatalogosDocumentosProcesoRolVarios extends AbstractRol{
    /**
     * Representa la referencia hacia el personal con acceso al apartado
    */
    @Getter @NonNull private PersonalActivo personal;
    
     /**
     * Área a la que pertence el personal logueado
     */
    @Getter @NonNull private String areaPersonal;
    
     /**
     * Periodo escolar seleccionado para registro
     */
    @Getter @NonNull private PeriodosEscolares periodoActivo;
    
    /**
     *Proceso seleccionado
     */
    @Getter @NonNull private String proceso;

    /**
     * Lista de procesos para seleccionar
     */
    @Getter @NonNull private List<String> procesos;
    
    /**
     * Lista de documentos del proceso seleccionado
     */
    @Getter @NonNull private List<DocumentoProceso> listaDocumentosProceso;
    
     /**
     * Habilitar o deshabilitar opciones para agregar documento
     */
    @Getter @NonNull private Boolean agregarDocumento;
    
    /**
     * Nuevo documento
     */
    @Getter private String nuevoDocumento; 
    
    /**
     * Especificaciones del nuevo documento
     */
    @Getter private String especificacionesDocumento; 
    
    /**
     * Nomenclatura del nuevo documento
     */
    @Getter private String nomenclaturaDocumento; 
    
    /**
     * Habilitar o deshabilitar opciones para agregar documento a un proceso
     */
    @Getter @NonNull private Boolean agregarDocumentoProceso;
       
    /**
     * Documento seleccionado para agregar a un proceso
     */
    @Getter @NonNull private Documento documentoSeleccionado;
    
    /**
     * Lista de documentos disponibles para relacionar con un proceso
     */
    @Getter @NonNull private List<Documento> documentosDisponibles;
    
     /**
     * Lista de documentos registrados
     */
    @Getter @NonNull private List<Documento> listaDocumentos;
    
     /**
     * Número de pestaña activa del tab
     */
    @Getter @NonNull private Integer pestaniaActiva; 
    
    public AdministracionCatalogosDocumentosProcesoRolVarios(Filter<PersonalActivo> filtro, PersonalActivo personal) {
        super(filtro);
        this.personal = personal;
    }

    public void setPersonal(PersonalActivo personal) {
        this.personal = personal;
    }

    public void setAreaPersonal(String areaPersonal) {
        this.areaPersonal = areaPersonal;
    }

    public void setPeriodoActivo(PeriodosEscolares periodoActivo) {
        this.periodoActivo = periodoActivo;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public void setProcesos(List<String> procesos) {
        this.procesos = procesos;
    }

    public void setListaDocumentosProceso(List<DocumentoProceso> listaDocumentosProceso) {
        this.listaDocumentosProceso = listaDocumentosProceso;
    }

    public void setAgregarDocumento(Boolean agregarDocumento) {
        this.agregarDocumento = agregarDocumento;
    }

    public void setNuevoDocumento(String nuevoDocumento) {
        this.nuevoDocumento = nuevoDocumento;
    }

    public void setEspecificacionesDocumento(String especificacionesDocumento) {
        this.especificacionesDocumento = especificacionesDocumento;
    }

    public void setNomenclaturaDocumento(String nomenclaturaDocumento) {
        this.nomenclaturaDocumento = nomenclaturaDocumento;
    }
    
    public void setAgregarDocumentoProceso(Boolean agregarDocumentoProceso) {
        this.agregarDocumentoProceso = agregarDocumentoProceso;
    }
    
    public void setDocumentoSeleccionado(Documento documentoSeleccionado) {
        this.documentoSeleccionado = documentoSeleccionado;
    }

    public void setDocumentosDisponibles(List<Documento> documentosDisponibles) {
        this.documentosDisponibles = documentosDisponibles;
    }

    public void setListaDocumentos(List<Documento> listaDocumentos) {
        this.listaDocumentos = listaDocumentos;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }    
}
