/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Persona;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ProcesosInscripcion;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.enums.UsuarioTipo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoAspirante;
import java.util.*;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoAspiranteProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;


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
    
    /**
     * Verifica que el usuario autenticado sea de tipo aspirante
     *
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
     * Verifica que haya un evento aperturado para el registro de fichas de admision
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
     * Permite verificar si existen coinciden los datos ingresados (curp y folio de admsión)
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
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<DtoDocumentoAspirante> listaDocumentos = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso =:proceso", DocumentoProceso.class)
                    .setParameter("proceso", "Inscripción")
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
            String periodoInscripcion = periodoEscolarBD.getMesInicio().getAbreviacion().concat("-").concat(periodoEscolarBD.getMesFin().getAbreviacion()).concat(Integer.toString(periodoEscolarBD.getAnio()));
            
            DtoDocumentoAspirante dto = new DtoDocumentoAspirante();
            dto.setDocumentoAspiranteProceso(documentoAspirante);
            dto.setDocumentoProceso(documentoProcesoBD);
            dto.setPeriodoInscripcion(periodoInscripcion);
            return ResultadoEJB.crearCorrecto(dto, "Documento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el documento (EjbCargaDocumentosAspirante. pack).", e, DtoDocumentoAspirante.class);
        }
    }
    
    public ResultadoEJB<DocumentoAspiranteProceso> guardarDocumentoAspirante(DocumentoAspiranteProceso documentoAspiranteProceso){
        System.out.println("guardarDocumentoAspirante - documento" + documentoAspiranteProceso.getDocumento().getDocumento());
        try{
            em.persist(documentoAspiranteProceso);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(documentoAspiranteProceso, "Se guardó correctamente el documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el documento. (EjbCargaDocumentosAspirante.guardarDocumentoAspirante)", e, null);
        }
    }
    
}
