/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.dto.controlEscolar;

import java.util.List;
import javax.faces.model.SelectItem;
import lombok.Getter;
import lombok.NonNull;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DatosAcademicos;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Domicilio;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ExpedienteTitulacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.MedioComunicacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.rol.NivelRol;
/**
 *
 * @author UTXJ
 */
public class IntegracionExpedienteTitulacionRolEstudiante{
    
    /**
     * Representa la referencia hacia el estudiante
    */
    @Getter private Estudiante estudiante;
    
    /**
     * Representa la referencia hacia el rol de acceso
    */
    @Getter protected NivelRol nivelRol = NivelRol.OPERATIVO;

    /**
     * Evento de integración de expediente activo
     */
    @Getter private EventoTitulacion eventoActivo;
    
    /**
     * Clave del periodo activo
     */
    @Getter private Integer  periodoActivo;
    
     /**
     * Generación del evento activo
     */
    @Getter private Generaciones generacion;
    
     /**
     * Nivel educativo del evento activo
     */
    @Getter private ProgramasEducativosNiveles  nivelEducativo;
    
    /**
     * Lista de instrucciones para utilizar el módulo
     */
    @Getter private List<String> instrucciones;
    
     /**
     * Expediente de titulación registrado
     */
    @Getter private ExpedienteTitulacion  expedienteRegistrado;
    
    /**
     * Valor existencia del expediente
     */
    @Getter private Boolean  existeExpediente = false;
    
    /**
     * Número de pestaña activa
     */
    @Getter private Integer pestaniaActiva;
    
    /**
     * Número del progreso de integración del expediente
     */
    @Getter private Integer progresoExpediente;
    
     /**
     * Paso en el que se encuentra la integración del expediente
     */
    @Getter private String pasoRegistro;
   
    /**
     * Datos del expediente de titulación
     */
    @Getter private DtoExpedienteTitulacion dtoExpedienteTitulacion;
    
    /**
     * Lista de documentos que contiene el expediente
     */
    @Getter private List<DocumentoExpedienteTitulacion> listaDocExpTit;
    
     /**
     * Valor de existencia del documento seleccionado
     */
    @Getter private Boolean existeDocs = false;
    
     /**
     * Valor de validación del documento seleccionado
     */
    @Getter private Boolean valDoc = false;
    
    /**
     * Medios de comunicaicón registrados
     */
    @Getter private MedioComunicacion medioComunicacion;
    
     /**
     * Domicilio registrado
     */
    @Getter private Domicilio domicilio;
    
     /**
     * Datos académicos registrados
     */
    @Getter private DatosAcademicos datosAcademicos;
    
     /**
     * Representa SelectItem Ubicación de domicilio en que radica
     */
    @Getter private List<SelectItem> listaEstadosDomicilioRadica;
    
    @Getter private List<SelectItem> listaMunicipiosRadica;
    
    @Getter private List<SelectItem> listaAsentamientos;
    
    
     /**
     * Representa SelectItem Ubicación de iems
     */
    @Getter private List<SelectItem> listaEstadosIEMS;
    
    @Getter private List<SelectItem> listaMunicipiosIEMS;
    
    @Getter private List<SelectItem> listaLocalidadesIEMS;
    
    @Getter private List<SelectItem> listaIEMS;
   
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public void setNivelRol(NivelRol nivelRol) {
        this.nivelRol = nivelRol;
    }

    public void setEventoActivo(EventoTitulacion eventoActivo) {
        this.eventoActivo = eventoActivo;
    }

    public void setPeriodoActivo(Integer periodoActivo) {
        this.periodoActivo = periodoActivo;
    }
    
    public void setGeneracion(Generaciones generacion) {
        this.generacion = generacion;
    }
    
    public void setNivelEducativo(ProgramasEducativosNiveles nivelEducativo) {
        this.nivelEducativo = nivelEducativo;
    }

    public void setInstrucciones(List<String> instrucciones) {
        this.instrucciones = instrucciones;
    }
    
    public void setExpedienteRegistrado(ExpedienteTitulacion expedienteRegistrado) {
        this.expedienteRegistrado = expedienteRegistrado;
    }

    public void setExisteExpediente(Boolean existeExpediente) {
        this.existeExpediente = existeExpediente;
    }

    public void setPestaniaActiva(Integer pestaniaActiva) {
        this.pestaniaActiva = pestaniaActiva;
    }

    public void setProgresoExpediente(Integer progresoExpediente) {
        this.progresoExpediente = progresoExpediente;
    }

    public void setPasoRegistro(String pasoRegistro) {
        this.pasoRegistro = pasoRegistro;
    }
     
    public void setDtoExpedienteTitulacion(DtoExpedienteTitulacion dtoExpedienteTitulacion) {
        this.dtoExpedienteTitulacion = dtoExpedienteTitulacion;
    }

    public void setListaDocExpTit(List<DocumentoExpedienteTitulacion> listaDocExpTit) {
        this.listaDocExpTit = listaDocExpTit;
    }

    public void setExisteDocs(Boolean existeDocs) {
        this.existeDocs = existeDocs;
    }

    public void setValDoc(Boolean valDoc) {
        this.valDoc = valDoc;
    }
    
    public void setMedioComunicacion(MedioComunicacion medioComunicacion) {
        this.medioComunicacion = medioComunicacion;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public void setDatosAcademicos(DatosAcademicos datosAcademicos) {
        this.datosAcademicos = datosAcademicos;
    }
    
    public void setListaEstadosDomicilioRadica(List<SelectItem> listaEstadosDomicilioRadica) {
        this.listaEstadosDomicilioRadica = listaEstadosDomicilioRadica;
    }

    public void setListaMunicipiosRadica(List<SelectItem> listaMunicipiosRadica) {
        this.listaMunicipiosRadica = listaMunicipiosRadica;
    }

    public void setListaAsentamientos(List<SelectItem> listaAsentamientos) {
        this.listaAsentamientos = listaAsentamientos;
    }

    public void setListaEstadosIEMS(List<SelectItem> listaEstadosIEMS) {
        this.listaEstadosIEMS = listaEstadosIEMS;
    }

    public void setListaMunicipiosIEMS(List<SelectItem> listaMunicipiosIEMS) {
        this.listaMunicipiosIEMS = listaMunicipiosIEMS;
    }

    public void setListaLocalidadesIEMS(List<SelectItem> listaLocalidadesIEMS) {
        this.listaLocalidadesIEMS = listaLocalidadesIEMS;
    }

    public void setListaIEMS(List<SelectItem> listaIEMS) {
        this.listaIEMS = listaIEMS;
    }

}
