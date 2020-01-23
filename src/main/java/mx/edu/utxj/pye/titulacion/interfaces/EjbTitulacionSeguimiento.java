/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.interfaces;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.finanzascarlos.Viewregalumnosnoadeudo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
//import mx.edu.utxj.pye.sgi.entity.titulacion.ListaExpedientes;
import mx.edu.utxj.pye.titulacion.dto.dtoExpedientesActuales;
import mx.edu.utxj.pye.titulacion.dto.dtoExpedienteMatricula;
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DomiciliosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.FechasDocumentos;
import mx.edu.utxj.pye.titulacion.dto.dtoPagosFinanzas;
import mx.edu.utxj.pye.titulacion.dto.dtoProcesosIntegracion;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbTitulacionSeguimiento {
    
//     /**
//     * Consulta expedientes registrados actualmente
//     * @return Lista de una vista llamada ListaExpedientes
//     */
//    public List<ListaExpedientes> consultaListaExpedientes(); 
    
     /**
     * Consulta expedientes registrados actualmente
     * @return Lista de una vista llamada ListaExpedientes
     */
    public List<dtoExpedientesActuales> consultaListaExpedientes();  
    
     /**
     * Muestra expediente completo de titulación del estudiante
     * @param expediente
     * @return dtoExpedienteMatricula
     * @throws java.lang.Throwable
     */
    public dtoExpedienteMatricula mostrarExpediente(Integer expediente) throws Throwable;

    /**
     * Actualiza la información del documento del expediente
     * @param documentoExp
     * @return entity DocumentosExpediente
     * @throws java.lang.Throwable
     */
    public DocumentosExpediente actualizarDocExpediente(DocumentosExpediente documentoExp) throws Throwable;

    /**
     * Obtiene status de los documentos del expediente
     * @param claveDoc
     * @return Lista de entity DocumentosExpediente
     */
    public List<DocumentosExpediente> getListaStatusPorDocumento(Integer claveDoc);

     /**
     * Buscar si el expediente contiene fotografía
     * @param expediente
     * @return Ruta de la fotografía
     * @throws java.lang.Throwable
     */
    public String buscarFotografiaING(Integer expediente) throws Throwable;
    
     /**
     * Buscar si el expediente contiene fotografía
     * @param expediente
     * @return Ruta de la fotografía
     * @throws java.lang.Throwable
     */
    public String buscarFotografiaTSU(Integer expediente) throws Throwable;

    /**
     * Obtiene la lista de generaciones con registros en expedientes de titulación
     * @return Lista de entity Generaciones
     */
    public List<Generaciones> getGeneracionesConregistroING();
    
    /**
     * Obtiene la lista de generaciones con registros en expedientes de titulación
     * @return Lista de entity Generaciones
     */
    public List<Generaciones> getGeneracionesConregistroTSU();

    /**
     * Obtiene la lista de programas educativos por generación
     * @param generacion
     * @return Lista de entity AreasUniversidad
     */
    public List<AreasUniversidad> getExpedientesPorGeneracionesING(Generaciones generacion);
    
    /**
     * Obtiene la lista de programas educativos por generación
     * @param generacion
     * @return Lista de entity AreasUniversidad
     */
    public List<AreasUniversidad> getExpedientesPorGeneracionesTSU(Generaciones generacion);
    
     /**
     * Obtiene la lista de expedientes correspondiente a la generación y programa educativo seleccionado.
     * @param programaSeleccionado
     * @param generacion
     * @return Lista de dtoExpedienteMatricula
     */
    public List<dtoExpedienteMatricula> getListaExpedientesPorProgramaGeneracion(AreasUniversidad programaSeleccionado, Generaciones generacion);
    
     /**
     * Busca datos de titulación registrados del estudiante
     * @param expediente
     * @return entity DatosTitulacion
     * @throws java.lang.Throwable
     */
    public DatosTitulacion buscarDatosTitulacion (Integer expediente) throws Throwable;
    
     /**
     * Guarda datos de titulación del estudiante de la vista "seguimientoExpMatricula"
     * @param datosTitulacion
     * @param expediente
     * @return dtoExpedienteMatricula
     * @throws java.lang.Throwable
     */
    public dtoExpedienteMatricula guardarDatosTitulacion(DatosTitulacion datosTitulacion, Integer expediente) throws Throwable;
    
     /**
     * Guarda antecedentes académicos del estudiante de la vista "seguimientoExpMatricula"
     * @param antecedentesAcademicos
     * @param expediente
     * @return dtoExpedienteMatricula
     * @throws java.lang.Throwable
     */
    public dtoExpedienteMatricula guardarAntecedentesAcad(AntecedentesAcademicos antecedentesAcademicos, Integer expediente) throws Throwable;
    
     /**
     * Busca información del expediente de titulación
     * @param expediente
     * @return entity ExpedientesTitulacion
     * @throws java.lang.Throwable
     */
    public ExpedientesTitulacion buscarExpedienteTitulacion(Integer expediente) throws Throwable;
    
    /**
     * Obtiene el status del expediente correspondiente.
     * @param expediente
     * @return Status del expediente
    */
    public List<ExpedientesTitulacion> getListaStatusPorExpediente(Integer expediente);
    
    /**
     * Cambiar el status del expediente
     * @param expediente
     * @param clavePersonal
     * @throws java.lang.Throwable
     */   
    public void validarExpediente(Integer expediente, Integer clavePersonal) throws Throwable;
    
    /**
    * Genera reporte de expedientes por generación 
    * @param generacion
     * @param nivel
    * @return Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteGeneracionTSU(Generaciones generacion, Integer nivel) throws Throwable;
    
    /**
    * Genera reporte de expedientes por generación 
    * @param generacion
     * @param nivel
    * @return Ruta del archivo del reporte
    * @throws Throwable
    */
    public String getReporteGeneracionING(Generaciones generacion, Integer nivel) throws Throwable;
    
    /**
     * Obtiene nombre del personal que validó o invalidó el expediente
     * @param clavePersonal
     * @return nombre
     * @throws java.lang.Throwable
     */
    public String buscarPersonalValido (Integer clavePersonal) throws Throwable;
    
     /**
     * Obtiene la lista de expedientes correspondiente a la generación y programa educativo seleccionado.
     * @param generacion
     * @param nivel
     * @return Lista de dtoExpedienteMatricula
     */
    public List<dtoExpedienteMatricula> getListaExpedientesPorGeneracion(Generaciones generacion, Integer nivel);
    
     /**
     * Obtiene la lista de pagos de finanzas del egresado seleccionado
     * @param matricula
     * @return Lista de dtoPagosFinanzas
     */
    public List<dtoPagosFinanzas> getListaDtoPagosFinanzas(String matricula);
    
     /**
     * Obtiene dtoPagosFinanzas nivel T.S.U.
     * @param listaDtoPagosFinanzas
     * @return dtoPagosFinanzas
     */
    public dtoPagosFinanzas getDtoPagosFinanzasTSU(List<dtoPagosFinanzas> listaDtoPagosFinanzas);
    
    /**
     * Obtiene dtoPagosFinanzas nivel Ing. y Lic.
     * @param listaDtoPagosFinanzas
     * @return dtoPagosFinanzas
     */
    public dtoPagosFinanzas getDtoPagosFinanzasING(List<dtoPagosFinanzas> listaDtoPagosFinanzas);
    
    /**
     * Obtiene status del egresado en SAIIUT
     * @param expediente
     * @return status académico
     */
    public String obtenerStatusSAIIUT(ExpedientesTitulacion expediente);
    
     /**
     * Guarda antecedentes académicos del estudiante de la vista "seguimientoExpGeneracionInd"
     * @param antecedentesAcademicos
     * @param expediente
     * @return entity AntecedentesAcademicos
     * @throws java.lang.Throwable
     */
    public AntecedentesAcademicos guardarAntAcadInd(AntecedentesAcademicos antecedentesAcademicos, Integer expediente) throws Throwable;
    
     /**
     * Busca la información del expediente de titulación
     * @param matricula
     * @return entity ExpedientesTitulacion
     * @throws java.lang.Throwable
     */
    public ExpedientesTitulacion buscarExpedienteMatricula(String matricula) throws Throwable;
    
     /**
     * Guarda datos de titulación del estudiante de la vista "seguimientoExpGeneracionInd"
     * @param datosTitulacion
     * @param expediente
     * @return entity DatosTitulacion
     * @throws java.lang.Throwable
     */
    public DatosTitulacion guardarDatTitInd(DatosTitulacion datosTitulacion, Integer expediente) throws Throwable;
  
     /**
     * Busca fechas de documentos registrados
     * @param expediente
     * @param programa
     * @return entity FechasDocumentos
     * @throws java.lang.Throwable
     */
    public FechasDocumentos buscarFechasDocumentos (ExpedientesTitulacion expediente, AreasUniversidad programa) throws Throwable;
    
     /**
     * Consulta documentos pendientes de subir al expediente por Servicios Escolares
     * @param expediente
     * @return número de documentos
     * @throws java.lang.Throwable
     */
    public Integer consultarDocsEscolares (Integer expediente) throws Throwable;
    
     /**
     * Obtener descripción de la situación actual de la Carta de No Adeuda del Nivel de T.S.U. e Ing/Lic
     * @param validada
     * @return descripción del status
     */
    public String obtenerSituacionCartaNoAdeudo (Boolean validada);
    
     /**
     * Guarda nuevo expediente de titulación (egresado)
     * @param egresado
     * @return 
     * @throws java.lang.Throwable
     */
    public Egresados guardarEgresado(Egresados egresado) throws Throwable;
    
     /**
     * Guarda nuevo expediente de titulación (expedienteTitulacion)
     * @param expedienteTitulacion
     * @param procesoIntegracion
     * @param egresado
     * @param progEdu
     * @param generacion
     * @return 
     * @throws java.lang.Throwable
     */
    public ExpedientesTitulacion guardarExpedienteTitulacion(ExpedientesTitulacion expedienteTitulacion, dtoProcesosIntegracion procesoIntegracion, Egresados egresado, AreasUniversidad progEdu, Generaciones generacion) throws Throwable;
    
     /**
     * Guarda datos de contacto (datosContacto)
     * @param datContacto
     * @param expediente
     * @return entity DatosContacto
     * @throws java.lang.Throwable
     */
    public DatosContacto guardarDatosContacto(DatosContacto datContacto, ExpedientesTitulacion expediente) throws Throwable;
    
     /**
     * Guarda domicilio (domicilioExpediente)
     * @param domExpediente
     * @param expediente
     * @return entity DatosContacto
     * @throws java.lang.Throwable
     */
    public DomiciliosExpediente guardarDomicilio(DomiciliosExpediente domExpediente, ExpedientesTitulacion expediente) throws Throwable;
    
     /**
     * Guarda datos de titulación (datosTitulacion)
     * @param datTitulacion
     * @param expediente
     * @return entity DatosContacto
     * @throws java.lang.Throwable
     */
    public DatosTitulacion guardarDatosTitulacion(DatosTitulacion datTitulacion, ExpedientesTitulacion expediente) throws Throwable;
    
     /**
     * Obtiene el número de expediente de titulación
     * @return número de expediente
     * @throws java.lang.Throwable
     */
    public Integer obtenerNumeroExpediente() throws Throwable;
    
     /**
     * Obtiene lista de procesos de integración de expedientes de titulación
     * @return lista de dtoProcesosIntegracion
     * @throws java.lang.Throwable
     */
    public List<dtoProcesosIntegracion> obtenerListaProcesos() throws Throwable;
    
     /**
     * Obtiene lista de programas educativos
     * @param nivel
     * @return lista de entity AreasUniversidad
     * @throws java.lang.Throwable
     */
    public List<AreasUniversidad> obtenerProgramasEducativos(String nivel) throws Throwable;
    
     /**
     * Obtiene lista de generaciones
     * @param nivel
     * @return lista de entity AreasUniversidad
     * @throws java.lang.Throwable
     */
    public List<Generaciones> obtenerGeneraciones(String nivel) throws Throwable;
    
     /**
     * Obtiene nivel del programa educativo
     * @param progEdu
     * @return número de expediente
     * @throws java.lang.Throwable
     */
    public Integer obtenerNivelEducativo(AreasUniversidad progEdu) throws Throwable;
}
