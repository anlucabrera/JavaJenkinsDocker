/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.titulacion.interfaces;

import java.util.List;
import javax.ejb.Local;
import mx.edu.utxj.pye.sgi.entity.titulacion.Egresados;
import mx.edu.utxj.pye.sgi.entity.titulacion.ExpedientesTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.DatosContacto;
import mx.edu.utxj.pye.sgi.entity.titulacion.Documentos;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.DocumentosNivel;
import mx.edu.utxj.pye.sgi.entity.titulacion.DomiciliosExpediente;
import mx.edu.utxj.pye.sgi.entity.titulacion.AntecedentesAcademicos;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosIntexp;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.entity.titulacion.TituloExpediente;
import mx.edu.utxj.pye.titulacion.dto.DtoDatosTitulacion;
import mx.edu.utxj.pye.titulacion.dto.DtoNivelyPE;
import mx.edu.utxj.pye.sgi.saiiut.entity.Alumnos;
import mx.edu.utxj.pye.sgi.saiiut.entity.Comunicaciones;
import mx.edu.utxj.pye.sgi.saiiut.entity.Domicilios;
import mx.edu.utxj.pye.sgi.saiiut.entity.Personas;

/**
 *
 * @author UTXJ
 */
@Local
public interface EjbEstudianteRegistro {
    
     /**
     * Obtiene datos del estudiante de SAIIUT
     * @param matricula
     * @return entity Alumnos
     */
    public Alumnos obtenerInformacionAlumno(String matricula);
    
    /**
     * Verifica si el estudiante ha validado sus datos personales
     * @param matricula valor para realizar búsqueda
     * @return valor 1 o 0
     */
    public Integer getValidacionDatPer(String matricula);
   
    /**
     * Obtiene datos personales del estudiante de SAIIUT
     * @param estudiante
     * @return entity Personas
     * @throws java.lang.Throwable
     */
    public Personas mostrarDatosPersonalesSAIIUT(Alumnos estudiante) throws Throwable;
    
    /**
     * Obtiene datos académicos del estudiante de SAIIUT
     * @param estudiante
     * @return DtoDatosTitulacion
     * @throws java.lang.Throwable
     */
    public DtoDatosTitulacion obtenerDatosAcadSAIIUT(Alumnos estudiante) throws Throwable;
    
    /**
     * Muestra datos personales del estudiante guardados en la base de Titulación
     * @param matricula
     * @return entity Egresados
     */
    public Egresados mostrarDatosPersonales(String matricula);
   
    /**
     * Verifica si ya se encuentra registrado el estudiante en la base de Titulación
     * @param matricula
     * @return entity Egresados
     */
    public Egresados verificarRegistroEgresado(String matricula);
    
    /**
     * Muestra datos de contacto del estudiante guardados en la base de Titulación
     * @param estudiante
     * @return entity DatosContacto
     */
    public DatosContacto mostrarRegistroDatosContacto(Alumnos estudiante);
    
    /**
     * Guarda datos personales del estudiante en la base de Titulación
     * @param nuevoOBJegresado
     * @return entity Egresados
     * @throws java.lang.Throwable
     */
    public Egresados guardarDatosPersonales(Egresados nuevoOBJegresado) throws Throwable;
    
     /**
     * Actualiza datos personales del estudiante en la base de Titulación
     * @param nuevoOBJegresado
     * @return entity Egresados
     * @throws java.lang.Throwable
     */
    public Egresados actualizarDatosPersonales(Egresados nuevoOBJegresado) throws Throwable;
 
    /**
     * Obtiene nivel y programa educativo del estudiante de SAIIUT
     * @param estudiante
     * @return DtoNivelyPE
     */
    public DtoNivelyPE obtenerNivelyProgEgresado (Alumnos estudiante);
    
     /**
     * Obtiene clave del Expediente de titulación del estudiante
     * @param estudiante
     * @return entity ExpedientesTitulacion
     */
    public ExpedientesTitulacion obtenerClaveExpediente (Alumnos estudiante);
    
     /**
     * Guarda información del expediente de titulación del estudiante
     * @param nuevoOBJexpediente
     * @return entity ExpedientesTitulacion
     * @throws java.lang.Throwable
     */
    public ExpedientesTitulacion guardarExpedienteTitulacion(ExpedientesTitulacion nuevoOBJexpediente) throws Throwable;
    
     /**
     * Muestra número celular del estudiante registrado en SAIIUT
     * @param estudiante
     * @return entity Comunicaciones
     * @throws java.lang.Throwable
     */
    public Comunicaciones mostrarCelularSAIIUT(Alumnos estudiante) throws Throwable;
    
     /**
     * Muestra email del estudiante registrado en SAIIUT
     * @param estudiante
     * @return entity Comunicaciones
     * @throws java.lang.Throwable
     */
    public Comunicaciones mostrarEmailSAIIUT(Alumnos estudiante) throws Throwable;
     
     /**
     * Guarda datos de contacto del estudiante
     * @param nuevoOBJdatosCont
     * @return entity DatosContacto
     * @throws java.lang.Throwable
     */
    public DatosContacto guardarDatosContacto(DatosContacto nuevoOBJdatosCont) throws Throwable;
    
     /**
     * Actualiza datos de contacto del estudiante
     * @param nuevoOBJdatosCont
     * @return entity DatosContacto
     * @throws java.lang.Throwable
     */
    public DatosContacto actualizarDatosContacto(DatosContacto nuevoOBJdatosCont) throws Throwable;
    
     /**
     * Extraer números de una cadena de texto
     * @param celularSAIIUT
     * @return números
     * @throws java.lang.Throwable
     */
    public String extraerNumeros(String celularSAIIUT) throws Throwable;

     /**
     * Muestra domicilio del estudiante registrado en SAIIUT
     * @param estudiante
     * @return entity Domicilios
     * @throws java.lang.Throwable
     */
    public Domicilios mostrarDomicilioSAIIUT(Alumnos estudiante) throws Throwable;
    
     /**
     * Muestra domicilio del estudiante registrado
     * @param estudiante
     * @return entity DomiciliosExpediente
     */
    public DomiciliosExpediente mostrarDomicilio(Alumnos estudiante);
    
     /**
     * Guarda el domicilio del estudiante
     * @param nuevoOBJdomicilio
     * @return entity DomiciliosExpediente
     * @throws java.lang.Throwable
     */
    public DomiciliosExpediente guardarDomicilio(DomiciliosExpediente nuevoOBJdomicilio) throws Throwable;
    
     /**
     * Actualiza el domicilio del estudiante
     * @param nuevoOBJdomicilio
     * @return entity DomiciliosExpediente
     * @throws java.lang.Throwable
     */
    public DomiciliosExpediente actualizarDomicilio(DomiciliosExpediente nuevoOBJdomicilio) throws Throwable;
  
     /**
     * Obtiene lista de documento que debe contener el expediente dependiendo del nivel
     * @param estudiante
     * @return Lista de la entity DocumentosNivel
     */
    public List<DocumentosNivel> getListaDocsPorNivel(Alumnos estudiante);
    
     /**
     * Obtiene información del documento
     * @param claveDoc
     * @return entity Documentos
     */
    public Documentos obtenerInformacionDocumento (Integer claveDoc);
    
     /**
     * Guarda un documento en el expediente del estudiante
     * @param nuevoOBJdocExp
     * @return entity DocumentosExpediente
     * @throws java.lang.Throwable
     */
    public DocumentosExpediente guardarDocumentoExpediente(DocumentosExpediente nuevoOBJdocExp) throws Throwable;
    
     /**
     * Obtiene año de inicio y fin de la generación en Prontuario
     * @param generacion
     * @return generación
     */
    public String obtenerGeneracionProntuario (Short generacion);
    
     /**
     * Obtiene lista de documentos que contiene el expediente del estudiante
     * @param exp
     * @return Lista de la entity DocumentosExpediente
     */
    public List<DocumentosExpediente> getListaDocumentosPorRegistro(ExpedientesTitulacion exp);
   
     /**
     * Eliminar documento del expediente del estudiante
     * @param docsExp
     * @return boolean
     */
    public Boolean eliminarDocumentosEnExpediente(DocumentosExpediente docsExp);
   
     /**
     * Muestra los antecedentes académicos del estudiante registrados
     * @param matricula
     * @return entity AntecedentesAcademicos
     */
    public AntecedentesAcademicos mostrarAntecedentesAcademicos(String matricula);
    
     /**
     * Guarda antecedentes académicos del estudiante
     * @param nuevoOBJantAcad
     * @return entity AntecedentesAcademicos
     * @throws java.lang.Throwable
     */
    public AntecedentesAcademicos guardarAntecedentesAcad(AntecedentesAcademicos nuevoOBJantAcad) throws Throwable;
    
     /**
     * Actualiza antecedentes académicos del estudiante
     * @param nuevoOBJantAcad
     * @return entity AntecedentesAcademicos
     * @throws java.lang.Throwable
     */
    public AntecedentesAcademicos actualizarAntecedentesAcad(AntecedentesAcademicos nuevoOBJantAcad) throws Throwable;
 
     /**
     * Consulta el status del expediente de titulación del estudiante
     * @param matricula
     * @return entity ExpedientesTitulacion
     */
    public ExpedientesTitulacion consultarStatusExpediente(String matricula);

     /**
     * Obtiene clave del proceso de integración de expediente
     * @param estudiante
     * @return entity ProcesosIntexp
     */
    public ProcesosIntexp obtenerClaveProcesoIntExp(Alumnos estudiante);
    
     /**
     * Obtiene generación del estudiante dependiendo del proceso de integración de expedientes
     * @param proceso
     * @return entity ProcesosGeneraciones
     */
    public ProcesosGeneraciones obtenerGeneracionProcIntExp(Integer proceso);
    
     /**
     * Verifica si el tipo de documento ya existe en el expediente del estudiante
     * @param tipoDocumento
     * @param expediente
     * @return entity DocumentosExpediente
     */
    public DocumentosExpediente docExisteEnExpediente(Integer tipoDocumento, Integer expediente);
    
     /**
     * Obtiene datos de nivel TSU del estudiante inscrito en Ingeniería 
     * @param matricula
     * @return entity Alumnos
     */
    public Alumnos obtenerInformacionTSUAlumno(String matricula);
    
     /**
     * Guarda titulo en el expediente del estudiante
     * @param nuevoOBJtitExp
     * @return entity DocumentosExpediente
     * @throws java.lang.Throwable
     */
    public TituloExpediente guardarTituloExpediente(TituloExpediente nuevoOBJtitExp) throws Throwable;
    
     /**
     * Eliminar titulo del expediente del estudiante
     * @param titExp
     * @return boolean
     */
    public Boolean eliminarTituloExpediente(TituloExpediente titExp);
    
     /**
     * Busca si el egresado tiene titulo cargado
     * @param expedienteTitulacion
     * @return entity TituloExpediente
     */
    public TituloExpediente buscarTituloRegistrado(ExpedientesTitulacion expedienteTitulacion);
    
     /**
     * Actualiza fecha de emisión de título desde la interfaz de descarga de título
     * @param tituloExpediente
     * @return entity TituloExpediente
     * @throws java.lang.Throwable
     */
    public TituloExpediente actualizarFechaEmision(TituloExpediente tituloExpediente) throws Throwable;
}
