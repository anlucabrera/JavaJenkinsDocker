/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import static com.github.adminfaces.starter.util.Utils.addDetailMessage;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbPersonal;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Aspirante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import java.util.*;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;


/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbCargaDocumentosAspirante")
public class EjbCargaDocumentosAspirante {
    @EJB Facade f;
    @EJB EjbPersonal ejbPersonal;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbProcesoInscripcion ejbProcesoInscripcion;
    @EJB EjbAreasLogeo ejbAreasLogeo;

    private EntityManager em;

    @PostConstruct
    public void init() {
        em = f.getEntityManager();
    }
    
    
    /* MÉTODOS UTILIZADOS PARA EL MÓDULO DE ASPIRANTES */
    
    
    /**
     * Verifica que el usuario autenticado sea de tipo aspirante
     * @param tipoUsuarioAu
     * @return
     */
    public ResultadoEJB<Boolean> verficaAcceso(UsuarioTipo tipoUsuarioAu) {
        try {
            if (tipoUsuarioAu == null) {
                return ResultadoEJB.crearErroneo(2, new Boolean(false), "El tipo de usuario no debe ser nulo");
            }
            if (tipoUsuarioAu.getLabel().equals("ASPIRANTE")) {
                return ResultadoEJB.crearCorrecto(true, "Verificado como aspirante");
            } else {
                return ResultadoEJB.crearErroneo(3, new Boolean(false), "El usuario autenticado no es Aspirante");
            }

        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision (EjbCargaDocumentosAspirante.verificaEvento).", e, Boolean.class);
        }
    }

    /**
     * Verifica que haya un evento aperturado para el registro de fichas de admision ya que la carga de documento está relacionado con el
     *
     * @return
     */
    public ResultadoEJB<EventoEscolar> verificaEvento() {
        try {
            return ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.REGISTRO_FICHAS_ADMISION);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para el registro de fichas de admision (EjbCargaDocumentosAspirante.verificaEvento).", e, EventoEscolar.class);
        }
    }

    public ResultadoEJB<ProcesosInscripcion> getProcesosInscripcionActivo() {
        try {
            ProcesosInscripcion procesosInscripcion = new ProcesosInscripcion();
            procesosInscripcion = em.createQuery("select p from ProcesosInscripcion p where :fecha between p.fechaInicio and p.fechaFin", ProcesosInscripcion.class)
                    .setParameter("fecha", new Date())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (procesosInscripcion == null) {
                return ResultadoEJB.crearErroneo(2, procesosInscripcion, "No se encontro proceso de inscripción");
            } else {
                return ResultadoEJB.crearCorrecto(procesosInscripcion, "Proceso de incripción activo");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar el proceso de inscripcion activo(EjbCargaDocumentosAspirante.getProcesosInscripcionActivo).", e, null);
        }
    }
    
    /**
     * Permite verificar si existen coinciden los datos ingresados (curp y folio de admisión)
     * @param curp
     * @param folioAdmision
     * @param procesosInscripcion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Aspirante> validarCurpFolio(String curp, Integer folioAdmision, ProcesosInscripcion procesosInscripcion){
        try{
            Aspirante aspirante = em.createQuery("SELECT a FROM Aspirante a WHERE a.folioAspirante =:folio AND a.idProcesoInscripcion =:proceso AND a.idPersona.curp =:curp", Aspirante.class)
                    .setParameter("folio", folioAdmision)
                    .setParameter("proceso", procesosInscripcion)
                    .setParameter("curp", curp)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            return ResultadoEJB.crearCorrecto(aspirante, "Lista de configuración de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbCargaDocumentosAspirante.validarCurpFolio)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de documento que debe cargar el aspirante
     * @param aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDocumentoAspirante>> getDocumentoAspirante(Aspirante aspirante){
        try{
            List<String> procesos = Arrays.asList("Admision", "Inscripcion");
            
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<DtoDocumentoAspirante> listaDocumentos = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso IN :procesos ORDER BY d.proceso, d.documento.descripcion", DocumentoProceso.class)
                    .setParameter("procesos", procesos)
                    .getResultStream()
                    .map(doc -> pack(doc, aspirante).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaDocumentos, "Lista de documentos por aspirante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de documentos por aspirante. (EjbCargaDocumentosAspirante.getDocumentoAspirante)", e, null);
        }
    }
    
    /**
     * Empaqueta un documento del proceso en su DTO Wrapper
     * @param documentoProceso
     * @param aspirante
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoDocumentoAspirante> pack(DocumentoProceso documentoProceso, Aspirante aspirante){
        try{
            if(documentoProceso == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un documento nulo.", DtoDocumentoAspirante.class);
            if(documentoProceso.getDocumentoProceso()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un documento con clave nula.", DtoDocumentoAspirante.class);

            DocumentoProceso documentoProcesoBD = em.find(DocumentoProceso.class, documentoProceso.getDocumentoProceso());
            if(documentoProcesoBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un documento no registrado previamente en base de datos.", DtoDocumentoAspirante.class);

            DocumentoAspiranteProceso documentoAspirante = em.createQuery("SELECT d FROM DocumentoAspiranteProceso d WHERE d.aspirante =:aspirante AND d.documento =:documento", DocumentoAspiranteProceso.class)
                    .setParameter("aspirante", aspirante)
                    .setParameter("documento", documentoProcesoBD.getDocumento())
                    .getResultStream()
                    .findFirst()
                    .orElse(new DocumentoAspiranteProceso());
            
            PeriodosEscolares periodoEscolarBD = em.find(PeriodosEscolares.class, aspirante.getIdProcesoInscripcion().getIdPeriodo());
            String anioInscripcion = Integer.toString(periodoEscolarBD.getAnio());
            
            DtoDocumentoAspirante dto = new DtoDocumentoAspirante();
            dto.setDocumentoAspiranteProceso(documentoAspirante);
            dto.setDocumentoProceso(documentoProcesoBD);
            dto.setAnioInscripcion(anioInscripcion);
            return ResultadoEJB.crearCorrecto(dto, "Documento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el documento (EjbCargaDocumentosAspirante. pack).", e, DtoDocumentoAspirante.class);
        }
    }
    
     /**
     * Permite guardar el documento del aspirante en la base de datos
     * @param documentoAspiranteProceso
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoAspiranteProceso> guardarDocumentoAspirante(DocumentoAspiranteProceso documentoAspiranteProceso){
        try{
            em.persist(documentoAspiranteProceso);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(documentoAspiranteProceso, "Se guardó correctamente el documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el documento. (EjbCargaDocumentosAspirante.guardarDocumentoAspirante)", e, null);
        }
    }
    
    /**
     * Permite consultar si el documento ha sido registrado por el aspirante
     * @param documento
     * @param aspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> consultarDocumento(Documento documento, Aspirante aspirante){
        try{
            DocumentoAspiranteProceso documentoAspiranteProceso = em.createQuery("SELECT d FROM DocumentoAspiranteProceso d WHERE d.aspirante =:aspirante AND d.documento =:documento", DocumentoAspiranteProceso.class)
                    .setParameter("aspirante", aspirante)
                    .setParameter("documento", documento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            Boolean valor;
            if (documentoAspiranteProceso == null) {
                valor = false;
            } else {
                valor= true; 
            }
            return ResultadoEJB.crearCorrecto(valor, "Se ha realizado correctamente la consulta.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se realizó correctamente la consulta del documento. (EjbCargaDocumentosAspirante.consultarDocumento)", e, null);
        }
    }
    
     /* MÉTODOS UTILIZADOS PARA EL MÓDULO DE SERVICIOS ESCOLARES */   
    
     /**
     * Permite guardar las observaciones del documento correspondiente
     * @param dtoDocumentoAspirante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoAspiranteProceso> guardarObservacionesDocumento(DtoDocumentoAspirante dtoDocumentoAspirante) {
        try {
            
            DocumentoAspiranteProceso documentoAspiranteProceso = dtoDocumentoAspirante.getDocumentoAspiranteProceso();
            documentoAspiranteProceso.setObservaciones(dtoDocumentoAspirante.getDocumentoAspiranteProceso().getObservaciones());
            em.merge(documentoAspiranteProceso);

            return ResultadoEJB.crearCorrecto(documentoAspiranteProceso, "Se guardaron las observaciones del documento.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo guardarar las observaciones del documento. (EjbCargaDocumentosAspirante.guardarObservacionesDocumento)", e, null);
        }
    }
    
//    public void guardarObservacionesDocumento(DtoDocumentoAspirante dtoDocumentoAspirante) {
//            em.merge(dtoDocumentoAspirante.getDocumentoAspiranteProceso());
//            em.flush();
//    }
    
     /**
     * Permite validar o invalidar un documento del expediente del aspirante
     * @param documentoAspiranteProceso
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoAspiranteProceso> validarDocumento(DocumentoAspiranteProceso documentoAspiranteProceso) {
        
        try{
            Date fechaActual = new Date();
            DocumentoAspiranteProceso docAsp = em.find(DocumentoAspiranteProceso.class, documentoAspiranteProceso.getDocumentoAspirante());

            if (docAsp.getValidado()) {

                TypedQuery<DocumentoAspiranteProceso> q1 = em.createQuery("UPDATE DocumentoAspiranteProceso d SET d.validado = false WHERE d.documentoAspirante = :documentoAspirante", DocumentoAspiranteProceso.class);
                q1.setParameter("documentoAspirante", docAsp.getDocumentoAspirante());
                q1.executeUpdate();
                addDetailMessage("Se invalidó el documento seleccionado");
                docAsp.setFechaValidacion(null);

            } else {

                TypedQuery<DocumentoAspiranteProceso> q = em.createQuery("UPDATE DocumentoAspiranteProceso d SET d.validado = true WHERE d.documentoAspirante = :documentoAspirante", DocumentoAspiranteProceso.class);
                q.setParameter("documentoAspirante", docAsp.getDocumentoAspirante());
                q.executeUpdate();
                addDetailMessage("Se validó correctamente el documento seleccionado");
                docAsp.setFechaValidacion(fechaActual);
            }

            em.merge(docAsp);
            
            return ResultadoEJB.crearCorrecto(docAsp, "Se guardó correctamente el documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el documento. (EjbCargaDocumentosAspirante.guardarDocumentoAspirante)", e, null);
        }
    }
    
     /**
     * Permite eliminar un documento del expediente del aspirante
     * @param documentoAspiranteProceso
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarDocumentoAspirante(DocumentoAspiranteProceso documentoAspiranteProceso){
        try{
            if(documentoAspiranteProceso.getDocumentoAspirante() == null) return ResultadoEJB.crearErroneo(2, "La clave del permiso no puede ser nula.", Integer.TYPE);

            Integer id = documentoAspiranteProceso.getDocumentoAspirante();
            ServicioArchivos.eliminarArchivo(documentoAspiranteProceso.getRuta());
            
            Integer delete = em.createQuery("DELETE FROM DocumentoAspiranteProceso d WHERE d.documentoAspirante =:documento", DocumentoAspiranteProceso.class)
                .setParameter("documento", id)
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "El documento se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el documento correctamente. (EjbCargaDocumentosAspirante.eliminarDocumentoAsp)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de documento que no ha cargado el aspirante
     * @param listaDocumentosPendientes
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Documento>> getDocumentosPendientesAspirante(List<DtoDocumentoAspirante> listaDocumentosPendientes){
        try{
            
            System.err.println("getDocumentosPendientesAspirante - lista DocsAsp " + listaDocumentosPendientes.size());
            
//            List<Documento> listaDocumentos = Arrays.asList();
            
            //calcular el total de horas frente a grupo asignadas
             List<DocumentoProceso> listaDocumentosProceso = listaDocumentosPendientes.stream().map(DtoDocumentoAspirante::getDocumentoProceso).collect(Collectors.toList());
             
             List<Documento> listaDocumentos = listaDocumentosProceso.stream().map(DocumentoProceso::getDocumento).collect(Collectors.toList());
            
//            listaDocumentosPendientes.forEach(docP -> {
//                    listaDocumentos.add(docP.getDocumentoProceso().getDocumento());
//            });
             System.err.println("getDocumentosPendientesAspirante - lista " + listaDocumentos.size());
            
            return ResultadoEJB.crearCorrecto(listaDocumentos, "Lista de documentos por aspirante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de documentos por aspirante. (EjbCargaDocumentosAspirante.getDocumentoAspirante)", e, null);
        }
    }
    
    
}
