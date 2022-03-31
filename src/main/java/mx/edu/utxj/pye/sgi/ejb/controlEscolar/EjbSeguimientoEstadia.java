/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEmpresaComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadiaReporte;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoSeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorAcademicoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.RelacionDocumentoEstadiaEvento;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.entity.pye2.OrganismosVinculados;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalendarioEventosEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEmpresaSeguimientosEstadia;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEvaluacionEstadiaEstudiante;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AsesorEmpresarialEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CalificacionCriterioEstadiaPK;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CoordinadorAreaEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CriterioEvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EvaluacionEstadiaDescripcion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FolioAcreditacionEstadia;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TipoEstudiante;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import net.sf.jxls.transformer.XLSTransformer;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbSeguimientoEstadia")
public class EjbSeguimientoEstadia {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbRegistroBajas ejbRegistroBajas;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @EJB EjbCarga ejbCarga;
    public static final String ACTUALIZADO_ESTADIA_VINCULACION = "seguimientoEstadiaVinculacion.xlsx";
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /** MÓDULO SEGUIMIENTO DE ESTADÍA DEL ESTUDIANTE"
    
     /**
     * Permite validar si el usuario logueado es del tipo estudiante
     * @param matricula
     * @return Regresa la instancia del estudiante si este cumple con lo mencionado
     */
    public ResultadoEJB<Estudiante> validarEstudianteAsignado(Integer matricula){
        try{
            List<Integer> grados = new ArrayList<>(); grados.add(6); grados.add(11);
            Estudiante e = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula =:matricula AND e.grupo.grado IN :grados ORDER BY e.periodo DESC",  Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("grados", grados)
                    .getResultStream().findFirst().orElse(null);
            
            List<SeguimientoEstadiaEstudiante> listaSeguimiento = asignacionesEstudianteEstadia(matricula).getValor();
            if((e.getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1")) || e.getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("4"))) && !listaSeguimiento.isEmpty()){
                return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante y con seguimiento de estadía registrado.");
            }else {
                return ResultadoEJB.crearErroneo(2, "El estudiante encontrado no tiene una inscripcion activa o no tiene registro de seguimiento de estadía.", Estudiante.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbSeguimientoEstadia.validarEstudianteAsignado)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de seguimiento de estadía del estudainte 
     * @param matricula
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<SeguimientoEstadiaEstudiante>> asignacionesEstudianteEstadia(Integer matricula){
        try{
            List<SeguimientoEstadiaEstudiante> listaSeguimiento = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.estudiante.matricula=:matricula AND s.activo=:activo ORDER BY s.evento.evento DESC",  SeguimientoEstadiaEstudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("activo", true)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(listaSeguimiento, "Lista de seguimiento de estadía registrados del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de seguimiento de estadía registrados del estudiante. (EjbSeguimientoEstadia.asignacionesEstudianteEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de generaciones en la que existen seguimiento de estadía del estudiane
     * @param matricula
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesSeguimientoEstudiante(Integer matricula){
        try{
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            
            List<SeguimientoEstadiaEstudiante> listaSeguimiento = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.estudiante.matricula=:matricula AND s.activo=:activo ORDER BY s.evento.generacion DESC",  SeguimientoEstadiaEstudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("activo", true)
                    .getResultList();
            
            listaSeguimiento.forEach(seguimiento -> {
                Generaciones generacion = em.find(Generaciones.class, seguimiento.getEvento().getGeneracion());
                listaGeneraciones.add(generacion);
            });
            
            List<Generaciones> listaGeneracionesDistintas = listaGeneraciones.stream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaGeneracionesDistintas, "Lista de generaciones en que las estudiante tiene seguimiento de estadía registrado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones en que las estudiante tiene seguimiento de estadía registrado. (EjbSeguimientoEstadia.getGeneracionesSeguimientoEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener el nivel educativo de la generación seleccionada del seguimiento de estadía del estudiante
     * @param matricula
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProgramasEducativosNiveles> getNivelEducativoSeguimientoEstudiante(Integer matricula, Generaciones generacion){
        try{
            
            SeguimientoEstadiaEstudiante seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.estudiante.matricula=:matricula AND s.evento.generacion=:generacion AND s.activo=:activo",  SeguimientoEstadiaEstudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("activo", true)
                    .getResultStream().findFirst().orElse(null);
            
            ProgramasEducativosNiveles nivelEducativo = em.find(ProgramasEducativosNiveles.class, seguimientoEstadia.getEvento().getNivel());
            
            
            return ResultadoEJB.crearCorrecto(nivelEducativo, "Nivel de educativo de la generación del seguimiento de estadía registrado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el nivel de educativo de la generación del seguimiento de estadía registrado. (EjbSeguimientoEstadia.getNivelEducativoSeguimientoEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la información del seguimiento de estadía del estudiante
     * @param generacion
     * @param nivelEducativo
     * @param matricula
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoSeguimientoEstadiaEstudiante> getSeguimientoEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Integer matricula){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
         
            DtoSeguimientoEstadiaEstudiante seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.matricula=:matricula AND s.activo=:activo", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("matricula", matricula)
                    .setParameter("activo", true)
                    .getResultStream()
                    .map(seg -> packSeguimientoEstudiante(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .findAny().orElse(null);
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Información de seguimiento de estadía del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información de seguimiento de estadía del estudiante. (EjbSeguimientoEstadia.getSeguimientoEstudiante)", e, null);
        }
    }
    
     /**
     * Empaqueta el seguimiento de estadía en un DTO Wrapper para obtener datos del estudiante
     * @param seguimientoEstadiaEstudiante Seguimiento de estadía a empaquetar
     * @param eventoSeleccionado
     * @return Seguimiento de estadía empaquetada}
     */
    public ResultadoEJB<DtoSeguimientoEstadiaEstudiante> packSeguimientoEstudiante(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, EventoEstadia eventoSeleccionado){
        try{
            if(seguimientoEstadiaEstudiante == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar seguimiento nulo.", DtoSeguimientoEstadiaEstudiante.class);
            if(seguimientoEstadiaEstudiante.getSeguimiento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar seguimiento con clave nula.", DtoSeguimientoEstadiaEstudiante.class);

            SeguimientoEstadiaEstudiante segEstBD = em.find(SeguimientoEstadiaEstudiante.class, seguimientoEstadiaEstudiante.getSeguimiento());
            if(segEstBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar seguimiento no registrado previamente en base de datos.", DtoSeguimientoEstadiaEstudiante.class);

            DtoDatosEstudiante dtoDatosEstudiante = packEstudiante(seguimientoEstadiaEstudiante, eventoSeleccionado).getValor();
            
            List<DtoDocumentoEstadiaEstudiante> listaDocumentosEstadia = getDocumentosEstadia(seguimientoEstadiaEstudiante).getValor();
            
            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String fechaRegistro = sm.format(segEstBD.getFechaRegistro());
            
            DtoSeguimientoEstadiaEstudiante dtoSeguimientoEstadiaEstudiante = new DtoSeguimientoEstadiaEstudiante(seguimientoEstadiaEstudiante, dtoDatosEstudiante, fechaRegistro, listaDocumentosEstadia);
            
            return ResultadoEJB.crearCorrecto(dtoSeguimientoEstadiaEstudiante, "Seguimiento de estadía del estudiante empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el seguimiento de estadía del estudiante seleccionado. (EjbSeguimientoEstadia.packSeguimientoEstudiante).", e, DtoSeguimientoEstadiaEstudiante.class);
        }
    }
    
    /**
     * Permite obtener la lista de documentos que debe contener el seguimiento de estadía del estudiante
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoDocumentoEstadiaEstudiante>> getDocumentosEstadia(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            String proceso = "EstadiaTSU";
            
            if(!seguimientoEstadiaEstudiante.getEvento().getNivel().equals("TSU")){
                proceso= "EstadiaIngLic";
            }
            
            List<DtoDocumentoEstadiaEstudiante> listaDocumentos = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso = :proceso AND d.obligatorio = :valor", DocumentoProceso.class)
                    .setParameter("proceso", proceso)
                    .setParameter("valor", true)
                    .getResultStream()
                    .map(doc -> packDocumento(doc, seguimientoEstadiaEstudiante).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaDocumentos, "Lista de documentos que debe contener el seguimiento de estadía del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de documentos que debe contener el seguimiento de estadía del estudiante. (EjbSeguimientoEstadia.getDocumentosEstadia)", e, null);
        }
    }
    
     /**
     * Empaqueta un documento del proceso en su DTO Wrapper
     * @param documentoProceso
     * @param seguimientoEstadiaEstudiante
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoDocumentoEstadiaEstudiante> packDocumento(DocumentoProceso documentoProceso, SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            if(documentoProceso == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un documento nulo.", DtoDocumentoEstadiaEstudiante.class);
            if(documentoProceso.getDocumentoProceso()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un documento con clave nula.", DtoDocumentoEstadiaEstudiante.class);

            DocumentoProceso documentoProcesoBD = em.find(DocumentoProceso.class, documentoProceso.getDocumentoProceso());
            if(documentoProcesoBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un documento no registrado previamente en base de datos.", DtoDocumentoEstadiaEstudiante.class);

            DocumentoSeguimientoEstadia documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia.seguimiento=:seguimiento AND d.documento =:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimiento", seguimientoEstadiaEstudiante.getSeguimiento())
                    .setParameter("documento", documentoProcesoBD.getDocumento())
                    .getResultStream()
                    .findFirst()
                    .orElse(new DocumentoSeguimientoEstadia());
            
            Generaciones generacionBD = em.find(Generaciones.class, seguimientoEstadiaEstudiante.getEvento().getGeneracion());
            String generacion = Short.toString(generacionBD.getInicio()).concat("-").concat(Short.toString(generacionBD.getFin()));
            
            DtoDocumentoEstadiaEstudiante dto = new DtoDocumentoEstadiaEstudiante(documentoProceso, documentoSeguimientoEstadia, generacion);
            return ResultadoEJB.crearCorrecto(dto, "Documento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el documento (EjbSeguimientoEstadia.packDocumento).", e, DtoDocumentoEstadiaEstudiante.class);
        }
    }
    
    /**
     * Permite guardar el documento del estudiante en la base de datos
     * @param documentoSeguimientoEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoEstadia> guardarDocumentoSeguimientoEstadia(DocumentoSeguimientoEstadia documentoSeguimientoEstadia){
        try{
            em.persist(documentoSeguimientoEstadia);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(documentoSeguimientoEstadia, "Se guardó correctamente el documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el documento. (EjbSeguimientoEstadia.guardarDocumentoSeguimientoEstadia)", e, null);
        }
    }
    
    /**
     * Permite eliminar un documento del seguimiento de estadía del estudiante
     * @param documentoSeguimientoEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarDocumentoSeguimiento(DocumentoSeguimientoEstadia documentoSeguimientoEstadia){
        try{
            if(documentoSeguimientoEstadia.getDocumentoSeguimiento()== null) return ResultadoEJB.crearErroneo(2, "La clave del documento en el seguimiento no puede ser nula.", Integer.TYPE);

            Integer id = documentoSeguimientoEstadia.getDocumentoSeguimiento();
            ServicioArchivos.eliminarArchivo(documentoSeguimientoEstadia.getRuta());
            
            Integer delete = em.createQuery("DELETE FROM DocumentoSeguimientoEstadia d WHERE d.documentoSeguimiento =:documento", DocumentoSeguimientoEstadia.class)
                .setParameter("documento", id)
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "El documento se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el documento correctamente. (EjbSeguimientoEstadia.eliminarDocumentoSeguimiento)", e, null);
        }
    }
    
    /**
     * Permite consultar si el documento ha sido registrado por el estudiante
     * @param documento
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> consultarDocumento(Documento documento, SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            DocumentoSeguimientoEstadia documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia =:seguimientoEstadia AND d.documento =:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimientoEstadia", seguimientoEstadiaEstudiante)
                    .setParameter("documento", documento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            Boolean valor;
            if (documentoSeguimientoEstadia == null) {
                valor = false;
            } else {
                valor= true; 
            }
            return ResultadoEJB.crearCorrecto(valor, "Se ha realizado correctamente la consulta.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se realizó correctamente la consulta del documento. (EjbSeguimientoEstadia.consultarDocumento)", e, null);
        }
    }
    
    /**
     * Permite verificar evento activo de la actividad y usuario seleccionado
     * @param dtoSeguimientoEstadiaEstudiante
     * @param dtoDocumentoEstadiaEstudiante
     * @param usuario
     * @return Devueleve el seguimiento de estadía del estudiante, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<EventoEstadia> buscarEventoActivoDocumento(DtoSeguimientoEstadiaEstudiante dtoSeguimientoEstadiaEstudiante, DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante, String usuario){
        try{
            RelacionDocumentoEstadiaEvento relDocEvento = em.createQuery("SELECT r FROM RelacionDocumentoEstadiaEvento r WHERE r.documentoEstadia=:documento AND r.usuario=:usuario", RelacionDocumentoEstadiaEvento.class)
                    .setParameter("documento", dtoDocumentoEstadiaEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                    .setParameter("usuario", usuario)
                    .getResultStream().findFirst().orElse(null);
            
            if(relDocEvento == null) return ResultadoEJB.crearErroneo(2, "buscarEventoActivoDocumento: No se encontró relación del documento en la base de datos.", EventoEstadia.class);
            
            EventoEstadia eventoReg = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.actividad=:actividad AND e.usuario=:usuario AND current_date between  e.fechaInicio and e.fechaFin", EventoEstadia.class)
                .setParameter("generacion", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getGeneracion())
                .setParameter("nivel", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getNivel())
                .setParameter("actividad", relDocEvento.getActividad())
                .setParameter("usuario", relDocEvento.getUsuario())
                .getResultStream().findFirst().orElse(null);
            
           return ResultadoEJB.crearCorrecto(eventoReg, "Evento activo del documento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo encontrar evento activo del documento seleccionado. (EjbSeguimientoEstadia.buscarEventoActivoDocumento).", e, EventoEstadia.class);
        }
    }
    
    /**
     * Permite verificar apertura extemporánea activa y valdiada de la actividad y usuario seleccionado
     * @param dtoSeguimientoEstadiaEstudiante
     * @param dtoDocumentoEstadiaEstudiante
     * @param usuario
     * @return Devueleve el seguimiento de estadía del estudiante, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<EventoEstadia> buscarAperturaExtemporaneaDocumento(DtoSeguimientoEstadiaEstudiante dtoSeguimientoEstadiaEstudiante, DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante, String usuario){
        try{
            RelacionDocumentoEstadiaEvento relDocEvento = em.createQuery("SELECT r FROM RelacionDocumentoEstadiaEvento r WHERE r.documentoEstadia=:documento AND r.usuario=:usuario", RelacionDocumentoEstadiaEvento.class)
                    .setParameter("documento", dtoDocumentoEstadiaEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                    .setParameter("usuario", usuario)
                    .getResultStream().findFirst().orElse(null);
            
            if(relDocEvento == null) return ResultadoEJB.crearErroneo(2, "No se encontró relación del documento en la base de datos.", EventoEstadia.class);
            
            EventoEstadia eventoReg = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel AND a.evento.actividad=:actividad AND a.evento.usuario=:usuario AND a.seguimiento.seguimiento=:seguimiento AND a.validada=:valor AND current_date between a.fechaInicio and a.fechaFin", AperturaExtemporaneaEventoEstadia.class)
                .setParameter("generacion", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getGeneracion())
                .setParameter("nivel", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getNivel())
                .setParameter("actividad", relDocEvento.getActividad())
                .setParameter("usuario", relDocEvento.getUsuario())
                .setParameter("seguimiento", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getSeguimiento())
                .setParameter("valor", Boolean.TRUE)
                .getResultStream()
                .map(p-> p.getEvento())
                .findFirst().orElse(null);
            
           return ResultadoEJB.crearCorrecto(eventoReg, "Apertura extemporánea del evento y estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la apertura extemporánea del evento y estudiante seleccionado (EjbSeguimientoEstadia.buscarAperturaExtemporaneaDocumento).", e, EventoEstadia.class);
        }
    }
    
     /**
     * Permite verificar apertura extemporánea activa y valdiada de la actividad y usuario seleccionado
     * @param dtoSeguimientoEstadiaEstudiante
     * @param dtoDocumentoEstadiaEstudiante
     * @param usuario
     * @return Devueleve el seguimiento de estadía del estudiante, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<AperturaExtemporaneaEventoEstadia> aperturaExtemporaneaDocumento(DtoSeguimientoEstadiaEstudiante dtoSeguimientoEstadiaEstudiante, DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante, String usuario){
        try{
            RelacionDocumentoEstadiaEvento relDocEvento = em.createQuery("SELECT r FROM RelacionDocumentoEstadiaEvento r WHERE r.documentoEstadia=:documento AND r.usuario=:usuario", RelacionDocumentoEstadiaEvento.class)
                    .setParameter("documento", dtoDocumentoEstadiaEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                    .setParameter("usuario", usuario)
                    .getResultStream().findFirst().orElse(null);
            
            if(relDocEvento == null) return ResultadoEJB.crearErroneo(2, "aperturaExtemporaneaDocumento: No se encontró relación del documento en la base de datos.", AperturaExtemporaneaEventoEstadia.class);
            
            AperturaExtemporaneaEventoEstadia aperturaExtemporanea = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoEstadia a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel AND a.evento.actividad=:actividad AND a.evento.usuario=:usuario AND a.seguimiento.seguimiento=:seguimiento AND a.validada=:valor AND current_date between a.fechaInicio and a.fechaFin", AperturaExtemporaneaEventoEstadia.class)
                .setParameter("generacion", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getGeneracion())
                .setParameter("nivel", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getNivel())
                .setParameter("actividad", relDocEvento.getActividad())
                .setParameter("usuario", relDocEvento.getUsuario())
                .setParameter("seguimiento", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getSeguimiento())
                .setParameter("valor", Boolean.TRUE)
                .getResultStream()
                .findFirst().orElse(null);
            
           return ResultadoEJB.crearCorrecto(aperturaExtemporanea, "Apertura extemporánea del evento y estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la apertura extemporánea del evento y estudiante seleccionado (EjbSeguimientoEstadia.AperturaExtemporaneaEventoEstadia).", e, AperturaExtemporaneaEventoEstadia.class);
        }
    }
    
    /**
     * Permite verificar evento registrado de la actividad y usuario seleccionado
     * @param dtoSeguimientoEstadiaEstudiante
     * @param dtoDocumentoEstadiaEstudiante
     * @return Devueleve el seguimiento de estadía del estudiante, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<EventoEstadia> buscarEventoDocumento(DtoSeguimientoEstadiaEstudiante dtoSeguimientoEstadiaEstudiante, DtoDocumentoEstadiaEstudiante dtoDocumentoEstadiaEstudiante){
        try{
            RelacionDocumentoEstadiaEvento relDocEvento = em.createQuery("SELECT r FROM RelacionDocumentoEstadiaEvento r WHERE r.documentoEstadia=:documento", RelacionDocumentoEstadiaEvento.class)
                    .setParameter("documento", dtoDocumentoEstadiaEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                    .getResultStream().findFirst().orElse(null);
            
            if(relDocEvento == null) return ResultadoEJB.crearErroneo(1, "buscarEventoDocumento: No se encontró relación del documento en la base de datos.", EventoEstadia.class);
            
            EventoEstadia eventoReg = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.actividad=:actividad AND e.usuario=:usuario", EventoEstadia.class)
                .setParameter("generacion", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getGeneracion())
                .setParameter("nivel", dtoSeguimientoEstadiaEstudiante.getSeguimientoEstadiaEstudiante().getEvento().getNivel())
                .setParameter("actividad", relDocEvento.getActividad())
                .setParameter("usuario", relDocEvento.getUsuario())
                .getResultStream().findFirst().orElse(null);
           
           return ResultadoEJB.crearCorrecto(eventoReg, "Documento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el documento (EjbSeguimientoEstadia.buscarEventoDocumento).", e, EventoEstadia.class);
        }
    }
    
    /**
     * Permite buscar los documentos entregados del estudiante en su inscripción
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
//    public ResultadoEJB<Documentosentregadosestudiante> buscarDocumentosEntregados(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
//        try{
//            Grupo grupo = em.createQuery("SELECT g FROM Grupo g WHERE g.generacion=:generacion AND g.idPe=:programa ORDER BY g.periodo ASC",  Grupo.class)
//                    .setParameter("generacion",  seguimientoEstadiaEstudiante.getEvento().getGeneracion())
//                    .setParameter("programa",  seguimientoEstadiaEstudiante.getEstudiante().getGrupo().getIdPe())
//                    .getResultStream().findFirst().orElse(null);
//            
//            
//            Documentosentregadosestudiante documentosEntregados = em.createQuery("SELECT d FROM Documentosentregadosestudiante d WHERE d.estudiante1.matricula=:matricula AND d.estudiante1.periodo=:periodo", Documentosentregadosestudiante.class)
//                    .setParameter("matricula",  seguimientoEstadiaEstudiante.getEstudiante().getMatricula())
//                    .setParameter("periodo", grupo.getPeriodo())
//                    .getResultStream().findFirst().orElse(null);
//            
//            return ResultadoEJB.crearCorrecto(documentosEntregados, "Documentos entregados por el estudiante");
//        }catch (Exception e){
//            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existen documentos entregados por el estudiante (EjbSeguimientoEstadia.buscarDocumentosEntregados).", e, Documentosentregadosestudiante.class);
//        }
//    }
    
    /* MÓDULO DE SEGUIMIENTO DE ESTADÍA POR ASESOR ACADÉMICO */
    
    /**
     * Permite obtener la lista de generaciones en la que existen eventos de estadía registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesSeguimientoRegistrados(){
        try{
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            
            List<SeguimientoEstadiaEstudiante> listaSeguimiento = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s ORDER BY s.evento.generacion DESC",  SeguimientoEstadiaEstudiante.class)
                    .getResultList();
          
            listaSeguimiento.forEach(seguimiento -> {
                Generaciones generacion = em.find(Generaciones.class, seguimiento.getEvento().getGeneracion());
                listaGeneraciones.add(generacion);
            });
            
            List<Generaciones> listaGeneracionesDistintas = listaGeneraciones.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaGeneracionesDistintas, "Lista de generaciones en que existen eventos de estadía registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones en los que existen eventos de estadía registrados. (EjbSeguimientoEstadia.getGeneracionesSeguimientoRegistrados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de estudiantes asignados al asesor académico seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoSeguimientoEstadia>> getListaEstudiantesSeguimiento(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Personal personal){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            EventoEstadia eventoAsesor = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion coordinador asesor estadia").getValor();
            AsesorAcademicoEstadia asesorAcademico = ejbAsignacionRolesEstadia.buscarAsesorAcademico(personal, eventoAsesor).getValor();
            
            List<DtoSeguimientoEstadia> seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.asesor.asesorEstadia=:asesor", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("asesor", asesorAcademico.getAsesorEstadia())
                    .getResultStream()
                    .map(seg -> packSeguimiento(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoSeguimientoEstadia::compareTo)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Lista de estudiantes asignados al asesor académico seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes asignados al asesor académico seleccionado. (EjbSeguimientoEstadia.getListaEstudiantesSeguimiento)", e, null);
        }
    }
    
    /**
     * Empaqueta el seguimiento de estadía en un DTO Wrapper para obtener datos del estudiante
     * @param seguimientoEstadiaEstudiante Seguimiento de estadía a empaquetar
     * @param eventoSeleccionado
     * @return Seguimiento de estadía empaquetada
     */
    public ResultadoEJB<DtoSeguimientoEstadia> packSeguimiento(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, EventoEstadia eventoSeleccionado){
        try{
            if(seguimientoEstadiaEstudiante == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar seguimiento nulo.", DtoSeguimientoEstadia.class);
            if(seguimientoEstadiaEstudiante.getSeguimiento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar seguimiento con clave nula.", DtoSeguimientoEstadia.class);

            SeguimientoEstadiaEstudiante segEstBD = em.find(SeguimientoEstadiaEstudiante.class, seguimientoEstadiaEstudiante.getSeguimiento());
            if(segEstBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar seguimiento no registrado previamente en base de datos.", DtoSeguimientoEstadia.class);

            DtoDatosEstudiante dtoDatosEstudiante = packEstudiante(seguimientoEstadiaEstudiante, eventoSeleccionado).getValor();
            
                Personal asesorEstadia = em.find(Personal.class, seguimientoEstadiaEstudiante.getAsesor().getPersonal());

                AreasUniversidad area = em.find(AreasUniversidad.class, dtoDatosEstudiante.getProgramaEducativo().getAreaSuperior());

                Personal director = em.find(Personal.class, area.getResponsable());

                OrganismosVinculados empresa = new OrganismosVinculados();

            if(seguimientoEstadiaEstudiante.getEmpresa() == null){
                    empresa = null;
            }else{
              empresa = em.createQuery("SELECT o FROM OrganismosVinculados o WHERE o.empresa=:clave",  OrganismosVinculados.class)
                    .setParameter("clave",  seguimientoEstadiaEstudiante.getEmpresa())
                            .getResultStream().findFirst().orElse(null);
                }

                Double semanas = null;
            if(seguimientoEstadiaEstudiante.getFechaInicio() != null && seguimientoEstadiaEstudiante.getFechaFin() != null){
                    semanas = calcularSemanasEstadia(seguimientoEstadiaEstudiante.getFechaInicio(), seguimientoEstadiaEstudiante.getFechaFin()).getValor();
                }

            DtoSeguimientoEstadia dtoSeguimientoEstadia = new DtoSeguimientoEstadia(seguimientoEstadiaEstudiante, dtoDatosEstudiante, asesorEstadia, area, director, empresa, semanas);
            
            return ResultadoEJB.crearCorrecto(dtoSeguimientoEstadia, "Seguimiento de estadía empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el seguimiento de estadía. (EjbSeguimientoEstadia.packSeguimiento).", e, DtoSeguimientoEstadia.class);
        }
    }
    
     /**
     * Empaqueta la información de un estudiante en un DTO Wrapper para obtener datos del estudiante
     * @param seguimientoEstadiaEstudiante 
     * @param eventoSeleccionado
     * @return Seguimiento de estadía empaquetada
     */
    public ResultadoEJB<DtoDatosEstudiante> packEstudiante(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, EventoEstadia eventoSeleccionado){
        try{
            Estudiante estudiante = em.find(Estudiante.class, seguimientoEstadiaEstudiante.getEstudiante().getIdEstudiante());
            
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudiante.getPeriodo());
            DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(estudiante, programaEducativo,periodoEscolar);
            
            return ResultadoEJB.crearCorrecto(dtoDatosEstudiante, "Información del estudiante empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la información del estudiante. (EjbSeguimientoEstadia.packEstudiante).", e, DtoDatosEstudiante.class);
        }
    }
    
     /**
     * Permite verificar si existe evento activo de la actividad y usuario seleccionado
     * @param eventoSeleccionado
     * @param actividad
     * @param usuario
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEventoActivo(EventoEstadia eventoSeleccionado, String actividad, String usuario){
        try{
            EventoEstadia eventoReg = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.actividad=:actividad AND e.usuario=:usuario AND current_date between  e.fechaInicio and e.fechaFin", EventoEstadia.class)
                    .setParameter("generacion", eventoSeleccionado.getGeneracion())
                    .setParameter("nivel", eventoSeleccionado.getNivel())
                    .setParameter("actividad", actividad)
                    .setParameter("usuario", usuario)
                    .getResultStream().findFirst().orElse(null);
           return ResultadoEJB.crearCorrecto(eventoReg != null, "Evento activo de estadía encontrado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
   
    /**
     * Permite buscar el documento registrado por el estudiante
     * @param seguimientoEstadiaEstudiante
     * @param documento
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoEstadia> buscarDocumentoEstudiante(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, Integer documento){
        try{
            DocumentoSeguimientoEstadia documentoReg = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia.seguimiento=:seguimiento AND d.documento.documento=:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimiento",  seguimientoEstadiaEstudiante.getSeguimiento())
                    .setParameter("documento", documento)
                    .getResultStream().findFirst().orElse(new DocumentoSeguimientoEstadia());
            
            return ResultadoEJB.crearCorrecto(documentoReg, "Documento registrado del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe documento registrado del estudiante (EjbSeguimientoEstadia.buscarDocumentoEstudiante).", e, DocumentoSeguimientoEstadia.class);
        }
    }
    
     /**
     * Permite identificar a una lista de empresas sugeridas según los datos de búsqueda ingresados
     * @param pista Contenido que la vista que puede incluir nombre de la empresa, localidad, municipio y ciudad en donde se localiza
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<DtoEmpresaComplete>> buscarEmpresa(String pista){
        try{
            List<OrganismosVinculados> empresas = em.createQuery("select o from OrganismosVinculados o INNER JOIN o.localidad l WHERE o.estatus=:valor AND concat(o.nombre, l.nombre, l.municipio.nombre, l.municipio.estado.nombre) like concat('%',:pista,'%') ORDER BY o.nombre, l.nombre, l.municipio.nombre, l.municipio.estado.nombre DESC", OrganismosVinculados.class)
                    .setParameter("valor", Boolean.TRUE)
                    .setParameter("pista", pista)
                    .getResultList();
            
            List<DtoEmpresaComplete> listaDtoEmpresas = new ArrayList<>();
            
            empresas.forEach(empresa -> {
                String datosComplete = empresa.getNombre()+" - "+ empresa.getLocalidad().getNombre()+" , "+ empresa.getLocalidad().getMunicipio().getNombre()+ " , " +empresa.getLocalidad().getMunicipio().getEstado().getNombre() ;
                DtoEmpresaComplete  dtoEmpresaComplete = new DtoEmpresaComplete(empresa, datosComplete);
                listaDtoEmpresas.add(dtoEmpresaComplete);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEmpresas, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbSeguimientoEstadia.buscarEmpresa)", e, null);
        }
    }
    
     /**
     * Permite guardar la información del proceso de estadía del estudiante
     * @param dtoEmpresaComplete
     * @param dtoSeguimientoEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> guardarInformacionEstadia(DtoEmpresaComplete dtoEmpresaComplete, DtoSeguimientoEstadia dtoSeguimientoEstadia, AsesorEmpresarialEstadia asesorEmpresarialEstadia){
        try{
            SeguimientoEstadiaEstudiante segEstBD  = em.find(SeguimientoEstadiaEstudiante.class, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getSeguimiento());
            
            if(dtoEmpresaComplete !=null){
                segEstBD.setEmpresa(dtoEmpresaComplete.getEmpresa().getEmpresa());
                segEstBD.setProyecto(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getProyecto());
                segEstBD.setFechaInicio(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaInicio());
                segEstBD.setFechaFin(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaFin());
                em.merge(segEstBD);
                em.flush();
            }else{
                segEstBD.setProyecto(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getProyecto());
                segEstBD.setFechaInicio(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaInicio());
                segEstBD.setFechaFin(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaFin());
                em.merge(segEstBD);
                em.flush();
            }
            
            actualizarAsesorEmpresarial(asesorEmpresarialEstadia).getValor();
            
            return ResultadoEJB.crearCorrecto(segEstBD, "Se guardó correctamente la información de estadía del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar la información de estadía del estudiante. (EjbSeguimientoEstadia.guardarInformacionEstadia)", e, null);
        }
    }
    
     /**
     * Permite actualizar la información del proceso de estadía del estudiante
     * @param edicionEmpresa
     * @param dtoEmpresaComplete
     * @param dtoSeguimientoEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> actualizarInformacionEstadia(Boolean edicionEmpresa, DtoEmpresaComplete dtoEmpresaComplete, DtoSeguimientoEstadia dtoSeguimientoEstadia, AsesorEmpresarialEstadia asesorEmpresarialEstadia){
        try{
            SeguimientoEstadiaEstudiante segEstBD  = em.find(SeguimientoEstadiaEstudiante.class, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getSeguimiento());
            
            if(edicionEmpresa && dtoEmpresaComplete !=null){
                segEstBD.setEmpresa(dtoEmpresaComplete.getEmpresa().getEmpresa());
                segEstBD.setProyecto(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getProyecto());
                segEstBD.setFechaInicio(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaInicio());
                segEstBD.setFechaFin(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaFin());
                em.merge(segEstBD);
                em.flush();
            }else{
                segEstBD.setProyecto(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getProyecto());
                segEstBD.setFechaInicio(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaInicio());
                segEstBD.setFechaFin(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getFechaFin());
                em.merge(segEstBD);
                em.flush();
            }
            
            actualizarAsesorEmpresarial(asesorEmpresarialEstadia).getValor();
            
            return ResultadoEJB.crearCorrecto(segEstBD, "Se actualizó correctamente la información de estadía del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información de estadía del estudiante. (EjbSeguimientoEstadia.actualizarInformacionEstadia)", e, null);
        }
    }
    
    /**
     * Permite actualizar la información del asesor empresarial de estadía del estudiante
     * @param asesorEmpresarialEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<AsesorEmpresarialEstadia> actualizarAsesorEmpresarial(AsesorEmpresarialEstadia asesorEmpresarialEstadia){
        try{
            em.merge(asesorEmpresarialEstadia);
            em.flush();
           
            return ResultadoEJB.crearCorrecto(asesorEmpresarialEstadia, "Se actualizó correctamente la información del asesor empresarial de estadía del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información del asesor empresarial de estadía del estudiante. (EjbSeguimientoEstadia.actualizarAsesorEmpresarial)", e, null);
        }
    }
    
     /**
     * Permite calcular el número semanas entre la fecha de inicio y fin de la estadía
     * @param fechaInicio
     * @param fechaFin
     * @return Resultado del proceso
     */
    public ResultadoEJB<Double> calcularSemanasEstadia(Date fechaInicio, Date fechaFin ){
        try{
            LocalDate fechaInicial = ejbRegistroBajas.convertirDateALocalDate(fechaInicio);
            LocalDate fechaFinal = ejbRegistroBajas.convertirDateALocalDate(fechaFin);
            
            long dias = DAYS.between(fechaInicial, fechaFinal);
            double semanas = (double) dias / 7;
            
            return ResultadoEJB.crearCorrecto(semanas, "Se cálculo correctamente el número de semanas.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo calcular el número de semanas. (EjbSeguimientoEstadia.calcularSemanasEstadia)", e, null);
        }
    }
    
     /**
     * Permite buscar el asesor empresarial del seguimiento de estadía
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<AsesorEmpresarialEstadia> buscarAsesorEmpresarial(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
           AsesorEmpresarialEstadia asesorEmpresarialEstadia = em.createQuery("SELECT a FROM AsesorEmpresarialEstadia a WHERE a.seguimiento =:seguimientoEstadia", AsesorEmpresarialEstadia.class)
                    .setParameter("seguimientoEstadia", seguimientoEstadiaEstudiante)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            return ResultadoEJB.crearCorrecto(asesorEmpresarialEstadia, "Asesor empresarial del seguimiento de estadía.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo encontrar asesor empresarial del seguimiento de estadía. (EjbSeguimientoEstadia.buscarAsesorEmpresarial)", e, null);
        }
    }
    
    /**
     * Permite consultar si el documento ha sido en el seguimiento del estudiante
     * @param documento
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> consultarClaveDocumento(Integer documento, SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            DocumentoSeguimientoEstadia documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia =:seguimientoEstadia AND d.documento.documento =:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimientoEstadia", seguimientoEstadiaEstudiante)
                    .setParameter("documento", documento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            Boolean valor;
            if (documentoSeguimientoEstadia == null) {
                valor = false;
            } else {
                valor= true; 
            }
            return ResultadoEJB.crearCorrecto(valor, "Se ha realizado correctamente la consulta.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se realizó correctamente la consulta del documento. (EjbSeguimientoEstadia.consultarClaveDocumento)", e, null);
        }
    }
    
    /**
     * Permite consultar los comentarios realizado al documento del estudiante
     * @param documento
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoEstadia> consultarComentarioDocumento(Integer documento, SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            DocumentoSeguimientoEstadia documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia =:seguimientoEstadia AND d.documento.documento =:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimientoEstadia", seguimientoEstadiaEstudiante)
                    .setParameter("documento", documento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            return ResultadoEJB.crearCorrecto(documentoSeguimientoEstadia, "Se ha realizado correctamente la consulta.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se realizó correctamente la consulta del documento. (EjbSeguimientoEstadia.consultarComentarioDocumento)", e, null);
        }
    }
    
    /**
     * Permite registrar comentarios a un documento del estudiante
     * @param comentario
     * @param claveDocumento
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoEstadia> guardarComentarioDocumento(String comentario, Integer claveDocumento, SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            DocumentoSeguimientoEstadia documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia =:seguimientoEstadia AND d.documento.documento =:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimientoEstadia", seguimientoEstadiaEstudiante)
                    .setParameter("documento", claveDocumento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
           
            documentoSeguimientoEstadia.setObservaciones(comentario);
            em.merge(documentoSeguimientoEstadia);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(documentoSeguimientoEstadia, "Se registró correctamente el comentario al documento del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el comentario al documento del estudiante. (EjbSeguimientoEstadia.guardarComentarioDocumento)", e, null);
        }
    }
    
    /**
     * Permite consultar si el documento registrado por el estudiante ha sido validado
     * @param documento
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> buscarValidacionDocumento(Integer documento, SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            DocumentoSeguimientoEstadia documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia =:seguimientoEstadia AND d.documento.documento =:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimientoEstadia", seguimientoEstadiaEstudiante)
                    .setParameter("documento", documento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            Boolean valor;
            if (documentoSeguimientoEstadia == null) {
                valor = false;
            } else {
                if(documentoSeguimientoEstadia.getValidado()){
                valor= true; 
                }else{
                valor = false;
                }
            }
            return ResultadoEJB.crearCorrecto(valor, "Se ha realizado correctamente la consulta.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se realizó correctamente la consulta del documento. (EjbSeguimientoEstadia.buscarValidacionDocumento)", e, null);
        }
    }
    
     /**
     * Permite validar o invalidar un documento del seguimiento de estadía del estudiante
     * @param documento
     * @param seguimientoEstadiaEstudiante
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoEstadia> validarDocumento(Integer documento, SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, Personal personal){
        try{
            DocumentoSeguimientoEstadia documentoSeguimientoEstadia = em.createQuery("SELECT d FROM DocumentoSeguimientoEstadia d WHERE d.seguimientoEstadia =:seguimientoEstadia AND d.documento.documento =:documento", DocumentoSeguimientoEstadia.class)
                    .setParameter("seguimientoEstadia", seguimientoEstadiaEstudiante)
                    .setParameter("documento", documento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            if(documentoSeguimientoEstadia == null) return ResultadoEJB.crearErroneo(2, "El documento no puede ser nulo.", DocumentoSeguimientoEstadia.class);
                
            String mensaje;
            if (documentoSeguimientoEstadia.getValidado()) {
                documentoSeguimientoEstadia.setValidado(Boolean.FALSE);
                documentoSeguimientoEstadia.setFechaValidacion(new Date());
                documentoSeguimientoEstadia.setPersonalValidacion(personal.getClave());
                em.merge(documentoSeguimientoEstadia);
                f.flush();
                mensaje="El documento se ha invalidado correctamente.";
            } else {
                documentoSeguimientoEstadia.setValidado(Boolean.TRUE);
                documentoSeguimientoEstadia.setFechaValidacion(new Date());
                documentoSeguimientoEstadia.setPersonalValidacion(personal.getClave());
                em.merge(documentoSeguimientoEstadia);
                f.flush();
                mensaje="El documento se ha validado correctamente.";
            }
            
                       
            return ResultadoEJB.crearCorrecto(documentoSeguimientoEstadia, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar o invalidar el documento seleccionado. (EjbSeguimientoEstadia.validarDocumento)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de criterios de evaluación de estadía registrada del estudiante seleccionado
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CalificacionCriterioEstadia>> getListaEvaluacionEstadiaRegistrada(SeguimientoEstadiaEstudiante  seguimientoEstadiaEstudiante){
        try{
            List<CalificacionCriterioEstadia> listaCalificacionesEstadia = em.createQuery("SELECT c FROM CalificacionCriterioEstadia c WHERE c.seguimientoEstadiaEstudiante=:seguimiento ORDER BY c.criterioEvaluacionEstadia.criterio ASC", CalificacionCriterioEstadia.class)
                    .setParameter("seguimiento", seguimientoEstadiaEstudiante)
                    .getResultStream()
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(listaCalificacionesEstadia, "Lista de criterios de evaluación de estadía registrada del estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de criterios de evaluación de estadía registrada del estudiante seleccionado. (EjbSeguimientoEstadia.getListaEvaluacionEstadiaRegistrada)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de criterios para evaluar estadía del estudiante seleccionado
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEvaluacionEstadiaEstudiante>> getListaEvaluacionEstadia(SeguimientoEstadiaEstudiante  seguimientoEstadiaEstudiante){
        try{
            
            List<DtoEvaluacionEstadiaEstudiante> listaCriteriosEvaluar = new ArrayList<>();
            
            EventoEstadia eventoEvaluacion = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.actividad=:actividad", EventoEstadia.class)
                    .setParameter("generacion", seguimientoEstadiaEstudiante.getEvento().getGeneracion())
                    .setParameter("nivel",  seguimientoEstadiaEstudiante.getEvento().getNivel())
                    .setParameter("actividad", "Registro cedula evaluacion empresarial")
                    .getResultStream()
                    .findFirst().orElse(null);
            
            
            if(eventoEvaluacion != null){
            EvaluacionEstadia evaluacionEstadia = em.createQuery("SELECT e FROM EvaluacionEstadia e WHERE e.evento.evento=:evento", EvaluacionEstadia.class)
                    .setParameter("evento", eventoEvaluacion.getEvento())
                    .getResultStream()
                    .findFirst().orElse(null);
            
            EvaluacionEstadiaDescripcion evaluacionEstadiaDescripcion = em.find(EvaluacionEstadiaDescripcion.class, evaluacionEstadia.getEvaluacion().getEvaluacion());
            
                if(evaluacionEstadiaDescripcion != null){
                     listaCriteriosEvaluar = em.createQuery("SELECT c FROM CriterioEvaluacionEstadia c WHERE c.evaluacion.evaluacion=:evaluacion ORDER BY c.criterio ASC", CriterioEvaluacionEstadia.class)
                        .setParameter("evaluacion", evaluacionEstadiaDescripcion.getEvaluacion())
                        .getResultStream()
                        .map(criterio -> packCriterio(criterio).getValor())
                        .filter(dto -> dto != null)
                        .collect(Collectors.toList());
                }
                    
            }
            return ResultadoEJB.crearCorrecto(listaCriteriosEvaluar, "Lista de criterios de evaluación de estadía del estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de criterios de evaluación de estadía del estudiante seleccionado. (EjbSeguimientoEstadia.getListaEvaluacionEstadia)", e, null);
        }
    }
    
     /**
     * Empaqueta el criterio de evaluación en un DTO Wrapper
     * @param criterioEvaluacionEstadia Criterio de evaluación a empaquetar
     * @return Criterio de evaluación de estadía empaquetado
     */
    public ResultadoEJB<DtoEvaluacionEstadiaEstudiante> packCriterio(CriterioEvaluacionEstadia criterioEvaluacionEstadia){
        try{
            if(criterioEvaluacionEstadia == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar criterio nulo.", DtoEvaluacionEstadiaEstudiante.class);
            if(criterioEvaluacionEstadia.getCriterio()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar criterio con clave nula.", DtoEvaluacionEstadiaEstudiante.class);

            CriterioEvaluacionEstadia critEvalBD = em.find(CriterioEvaluacionEstadia.class, criterioEvaluacionEstadia.getCriterio());
            if(critEvalBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar seguimiento no registrado previamente en base de datos.", DtoEvaluacionEstadiaEstudiante.class);

            Double calificacion = 0.0;
            DtoEvaluacionEstadiaEstudiante dtoEvaluacionEstadiaEstudiante = new DtoEvaluacionEstadiaEstudiante(critEvalBD, calificacion);
            
            return ResultadoEJB.crearCorrecto(dtoEvaluacionEstadiaEstudiante, "Criterio de evaluación de estadía empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el criterio de evaluación de estadía. (EjbSeguimientoEstadia.packCriterio).", e, DtoEvaluacionEstadiaEstudiante.class);
        }
    }
    
    /**
     * Permite actualizar la evaluación de estadia de un estudiante
     * @param seguimientoEstadiaEstudiante
     * @param listaCalificaciones
     * @param promedioAsesorInterno
     * @param promedioAsesorExterno
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CalificacionCriterioEstadia>> actualizarEvaluacionEstadia(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, List<CalificacionCriterioEstadia> listaCalificaciones, Double promedioAsesorInterno, Double promedioAsesorExterno){
        try{
            
            List<CalificacionCriterioEstadia> listaCalificacionesReg = new ArrayList<>();
            
            listaCalificaciones.forEach(cal -> {
                    
                CalificacionCriterioEstadiaPK calificacionCriterioEstadiaPK = new CalificacionCriterioEstadiaPK(cal.getCalificacionCriterioEstadiaPK().getSeguimiento(), cal.getCalificacionCriterioEstadiaPK().getCriterio());
                CalificacionCriterioEstadia calificacionCriterioEstadia = em.find(CalificacionCriterioEstadia.class, calificacionCriterioEstadiaPK);
                calificacionCriterioEstadia.setCalificacion(cal.getCalificacion());
                em.merge(calificacionCriterioEstadia);
                listaCalificacionesReg.add(calificacionCriterioEstadia);
            });
            
            SeguimientoEstadiaEstudiante seguimientoEstadiaBD = em.find(SeguimientoEstadiaEstudiante.class, seguimientoEstadiaEstudiante.getSeguimiento());
            seguimientoEstadiaBD.setPromedioAsesorInterno(promedioAsesorInterno);
            seguimientoEstadiaBD.setPromedioAsesorExterno(promedioAsesorExterno);
            em.merge(seguimientoEstadiaBD);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(listaCalificacionesReg, "Se actualizó correctamente la evaluación de estadia del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la evaluación de estadia  del estudiante. (EjbSeguimientoEstadia.actualizarEvaluacionEstadia)", e, null);
        }
    }
    
    /**
     * Permite registrar la evaluación de estadia de un estudiante
     * @param seguimientoEstadiaEstudiante
     * @param listaCalificaciones
     * @param promedioAsesorInterno
     * @param promedioAsesorExterno
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CalificacionCriterioEstadia>> guardarEvaluacionEstadia(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, List<DtoEvaluacionEstadiaEstudiante> listaCalificaciones, Double promedioAsesorInterno, Double promedioAsesorExterno){
        try{
            
            List<CalificacionCriterioEstadia> listaCalificacionesReg = new ArrayList<>();
            
            listaCalificaciones.forEach(cal -> {
                    
                CalificacionCriterioEstadiaPK calificacionCriterioEstadiaPK = new CalificacionCriterioEstadiaPK(seguimientoEstadiaEstudiante.getSeguimiento(), cal.getCriterioEvaluacionEstadia().getCriterio());
                CalificacionCriterioEstadia calificacionCriterioEstadia = new CalificacionCriterioEstadia();
                calificacionCriterioEstadia.setCalificacionCriterioEstadiaPK(calificacionCriterioEstadiaPK);
                calificacionCriterioEstadia.setCalificacion(cal.getCalificacion());
                em.persist(calificacionCriterioEstadia);
                listaCalificacionesReg.add(calificacionCriterioEstadia);
            });
            
            SeguimientoEstadiaEstudiante seguimientoEstadiaBD = em.find(SeguimientoEstadiaEstudiante.class, seguimientoEstadiaEstudiante.getSeguimiento());
            seguimientoEstadiaBD.setPromedioAsesorInterno(promedioAsesorInterno);
            seguimientoEstadiaBD.setPromedioAsesorExterno(promedioAsesorExterno);
            em.merge(seguimientoEstadiaBD);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(listaCalificacionesReg, "Se registró correctamente la evaluación de estadia del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la evaluación de estadia  del estudiante. (EjbSeguimientoEstadia.guardarEvaluacionEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener el folio de acreditación de estadía del estudiante seleccionado
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<FolioAcreditacionEstadia> getFolioAcreditacionEstadia(SeguimientoEstadiaEstudiante  seguimientoEstadiaEstudiante){
        try{
           FolioAcreditacionEstadia folioAcreditacionEstadia = em.createQuery("SELECT f FROM FolioAcreditacionEstadia f WHERE f.seguimiento=:seguimiento", FolioAcreditacionEstadia.class)
                    .setParameter("seguimiento", seguimientoEstadiaEstudiante)
                    .getResultStream()
                    .findFirst().orElse(null);
            return ResultadoEJB.crearCorrecto(folioAcreditacionEstadia, "Folio de acreditación de estadía del estudiante seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el folio de acreditación de estadía del estudiante seleccionado. (EjbSeguimientoEstadia.getFolioAcreditacionEstadia)", e, null);
        }
    }
    
    /**
     * Permite asignar folio de acreditación de estadía para el estudiante seleccionado
     * @param dtoSeguimientoEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<FolioAcreditacionEstadia> asignarFolioAcreditacionEstadia(DtoSeguimientoEstadia dtoSeguimientoEstadia){
        try{
            Integer numero = buscarUltimoFolioAsignado(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante()).getValor()+1;
            String folio =  generarFolio(numero).getValor();
            Generaciones generacion = em.find(Generaciones.class, dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante().getEvento().getGeneracion());
            String anio = Character.toString(Short.toString(generacion.getFin()).charAt(2)).concat(Character.toString(Short.toString(generacion.getFin()).charAt(3)));
            String periodo =  Integer.toString(dtoSeguimientoEstadia.getDtoEstudiante().getPeriodoEscolar().getPeriodo());
            
            FolioAcreditacionEstadia folioAcreditacionEstadia = new FolioAcreditacionEstadia();
            folioAcreditacionEstadia.setSeguimiento(dtoSeguimientoEstadia.getSeguimientoEstadiaEstudiante());
            folioAcreditacionEstadia.setNumero(numero);
            folioAcreditacionEstadia.setFolioCompleto("UTXJ".concat("/").concat(dtoSeguimientoEstadia.getDtoEstudiante().getProgramaEducativo().getSiglas()).concat("/").concat("ESTADÍA").concat("/").concat(folio).concat("/").concat(anio));
            folioAcreditacionEstadia.setCodigoQr("UTXJ".concat(dtoSeguimientoEstadia.getDtoEstudiante().getProgramaEducativo().getSiglas()).concat(periodo).concat(folio).concat(anio));
            em.persist(folioAcreditacionEstadia);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(folioAcreditacionEstadia, "Folio de acreditación de estadía del estudiante registrado correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el folio de acreditación de estadía del estudiante seleccionado. (EjbSeguimientoEstadia.asignarFolioAcreditacionEstadia)", e, null);
        }
    }
    
     /**
     * Permite obtener el último folio de acreditación de estadía registrado de la carrera y generación que corresponda
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> buscarUltimoFolioAsignado(SeguimientoEstadiaEstudiante  seguimientoEstadiaEstudiante){
        try{
             List<FolioAcreditacionEstadia> listaFolios = em.createQuery("SELECT f FROM FolioAcreditacionEstadia f WHERE f.seguimiento.estudiante.grupo.idPe=:programa AND f.seguimiento.evento.evento=:evento", FolioAcreditacionEstadia.class)
                     .setParameter("programa", seguimientoEstadiaEstudiante.getEstudiante().getGrupo().getIdPe())
                     .setParameter("evento", seguimientoEstadiaEstudiante.getEvento().getEvento())
                     .getResultStream()
                     .sorted(Comparator.comparingInt(FolioAcreditacionEstadia::getNumero).reversed())
                     .collect(Collectors.toList());
             
             Integer ultimoFolio;
            
             if(listaFolios.isEmpty()){
                 ultimoFolio = 0;
             }else{
                 FolioAcreditacionEstadia folioAcreditacionEstadia = listaFolios.get(0);
                 ultimoFolio = folioAcreditacionEstadia.getNumero();
             }
             
            return ResultadoEJB.crearCorrecto(ultimoFolio, "Último folio de acreditación de estadía registrado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el último folio de acreditación de estadía registrado. (EjbSeguimientoEstadia.buscarUltimoFolioAsignado)", e, null);
        }
    }
    
    /**
     * Permite obtener el folio completo para realizar la asignación
     * @param folio
     * @return Resultado del proceso
     */
    public ResultadoEJB<String> generarFolio(Integer folio){
        try{
             String numFolio = Integer.toString(folio);
             String folioDefinitivo="";
             switch(numFolio.length()){
                 case 1:
                     folioDefinitivo= "0000".concat(numFolio);
                     break;
                 case 2:
                     folioDefinitivo= "000".concat(numFolio);
                     break;
                 case 3:
                     folioDefinitivo= "00".concat(numFolio);
                     break;
                 case 4:
                     folioDefinitivo= "0".concat(numFolio);
                     break;
                 case 5:
                     folioDefinitivo= numFolio;
                     break;
                 default:
                     break;
             }
            
            return ResultadoEJB.crearCorrecto(folioDefinitivo, "Folio completo de acreditación de estadía generado correctamente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo generar el folio completo de acreditación de estadía. (EjbSeguimientoEstadia.generarFolio)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe evento activo de la actividad y usuario seleccionado
     * @param seguimientoEstadiaEstudiante
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarAproboEstadia(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            SeguimientoEstadiaEstudiante estadiaAprobada = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.seguimiento=:seguimiento AND s.promedioAsesorInterno>=:valor AND s.promedioAsesorExterno>=:valor", SeguimientoEstadiaEstudiante.class)
                    .setParameter("seguimiento", seguimientoEstadiaEstudiante.getSeguimiento())
                    .setParameter("valor", (double)8)
                    .getResultStream().findFirst().orElse(null);
            
            Boolean valor;
            if(estadiaAprobada==null)
            {
                valor = Boolean.FALSE;
            }else{
                valor = Boolean.TRUE;
            }
           return ResultadoEJB.crearCorrecto(valor, "Resultados de evaluaciones de estadía");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron evaluaciones para el seguimiento de estadía", e, Boolean.TYPE);
        }
    }
    
     /**
     * Permite verificar si un director de carrera tiene o tuvo rol de asesor o coordinador de estadía de área académica
     * @param personal
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarEsDirector(Personal personal){
        try{
            List<Short> categorias = new ArrayList<>(); categorias.add((short)18); categorias.add((short)48);
            
            Personal personalDir = em.createQuery("SELECT p FROM Personal p WHERE p.clave=:clave AND p.categoriaOperativa.categoria IN :categorias", Personal.class)
                    .setParameter("clave", personal.getClave())
                    .setParameter("categorias", categorias)
                    .getResultStream().findFirst().orElse(null);
            
            Boolean valor;
            if(personalDir==null)
            {
                valor = Boolean.FALSE;
            }else{
                valor = Boolean.TRUE;
            }
           return ResultadoEJB.crearCorrecto(valor, "El director tiene o tuvo rol de asesor o coordinador de estadía de área académica");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontró rol de asesor o coordinador de estadía de área académica", e, Boolean.TYPE);
        }
    }
    
     /* MÓDULO SEGUIMIENTO COORDINADOR DE ÁREA ACADÉMICA */
    
     /**
     * Permite validar si el usuario autenticado es personal docente con rol de coordinador académico del área
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocenteCoordinadorEstadia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            List<CoordinadorAreaEstadia> listaAsignaciones = asignacionesCoordinadorEstadia(clave).getValor();
            if (!listaAsignaciones.isEmpty()) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal docente con asignaciones de coordinador de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar con asignaciones de coordinador de estadía. (EjbSeguimientoEstadia.validarDocenteCoordinadorEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de asignaciones como asesor de estadía del personal docente
     * @param clave
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<CoordinadorAreaEstadia>> asignacionesCoordinadorEstadia(Integer clave){
        try{
            List<CoordinadorAreaEstadia> listaAsignaciones = em.createQuery("SELECT c FROM CoordinadorAreaEstadia c WHERE c.personal=:personal ORDER BY c.evento.evento DESC",  CoordinadorAreaEstadia.class)
                    .setParameter("personal", clave)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(listaAsignaciones, "Lista de asignaciones como coordinador de estadía del personal docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de asignaciones como coordinador de estadía del personal docente. (EjbSeguimientoEstadia.asignacionesCoordinadorEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de estudiantes del área académica que representa el coordinador de estadía
     * @param generacion
     * @param nivelEducativo
     * @param programaEducativo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoSeguimientoEstadia>> getListaEstudiantesSeguimientoCoordinadorArea(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, AreasUniversidad programaEducativo, Personal personal){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            EventoEstadia eventoCoordinador = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion coordinador asesor estadia").getValor();
            
            Short area = personal.getAreaSuperior();
            
            if(verificarEsDirector(personal).getValor()){
                area = personal.getAreaOperativa();
            }
            
            List<AsesorAcademicoEstadia> listaAsesores = getListaAsesoresEstadiaArea(eventoCoordinador, area).getValor();
            
            List<DtoSeguimientoEstadia> seguimientoEstadia = new ArrayList();
            
            if(listaAsesores != null && !listaAsesores.isEmpty()){
                seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.grupo.idPe=:programa AND s.asesor IN :lista", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("programa", programaEducativo.getArea())
                    .setParameter("lista", listaAsesores)
                    .getResultStream()
                    .map(seg -> packSeguimiento(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoSeguimientoEstadia::compareTo)
                    .collect(Collectors.toList());
            }
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Lista de estudiantes del área a la que pertenece el coordinador.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes del área a la que pertenece el coordinador. (EjbSeguimientoEstadia.getListaEstudiantesSeguimientoCoordinadorArea)", e, null);
        }
    }
    
     /**
     * Permite buscar lista de asesores de estadía asignados de la misma área que el coordinador
     * @param eventoCoordinador
     * @param areaSuperior
     * @return Resultado del proceso..
     */
    public ResultadoEJB<List<AsesorAcademicoEstadia>> getListaAsesoresEstadiaArea(EventoEstadia eventoCoordinador, Short areaSuperior){
        try{
           
            List<AsesorAcademicoEstadia> listaAsesores = em.createQuery("SELECT a FROM AsesorAcademicoEstadia a WHERE a.evento.evento=:evento AND a.programaEducativo=:areaSuperior", AsesorAcademicoEstadia.class)
                    .setParameter("evento", eventoCoordinador.getEvento())
                    .setParameter("areaSuperior", areaSuperior)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(listaAsesores, "Lista de asesores de estadía del área del coordinador");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de asesores de estadía del área del coordinador. (EjbSeguimientoEstadia.getListaAsesoresEstadiaArea)", e, null);
        }
    }
    
    public ResultadoEJB<SeguimientoEstadiaEstudiante> actualizarComentariosCoordinador(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante) {
        try{
            SeguimientoEstadiaEstudiante segEstBD = em.find(SeguimientoEstadiaEstudiante.class,  seguimientoEstadiaEstudiante.getSeguimiento());
            segEstBD.setComentariosCoordinador(seguimientoEstadiaEstudiante.getComentariosCoordinador());
            em.merge(segEstBD);
            f.flush();
                       
            return ResultadoEJB.crearCorrecto(seguimientoEstadiaEstudiante, "Se registraron correctamente los comentarios del coordinador.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se registraron correctamente los comentarios del coordinador. (EjbSeguimientoEstadia.actualizarComentariosCoordinador)", e, null);
        }
    }
    
    /* MÓDULO SEGUIMIENTO DIRECCIÓN DE CARRERA */
    
    /**
     * Permite obtener la lista de estudiantes del área académica que representa el coordinador de estadía
     * @param generacion
     * @param nivelEducativo
     * @param programaEducativo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoSeguimientoEstadia>> getListaEstudiantesSeguimientoArea(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, AreasUniversidad programaEducativo, Personal personal){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            EventoEstadia eventoRoles = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion coordinador asesor estadia").getValor();
            List<AsesorAcademicoEstadia> listaAsesores = getListaAsesoresEstadiaArea(eventoRoles, personal.getAreaOperativa()).getValor();
           
            List<DtoSeguimientoEstadia> seguimientoEstadia = new ArrayList();
            
            if(listaAsesores != null && !listaAsesores.isEmpty()){
                seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.grupo.idPe=:programa AND s.asesor IN :lista", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("programa", programaEducativo.getArea())
                    .setParameter("lista", listaAsesores)
                    .getResultStream()
                    .map(seg -> packSeguimiento(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoSeguimientoEstadia::compareTo)
                    .collect(Collectors.toList());
            }
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Lista de estudiantes del área a la que pertenece el director de carrera.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes del área a la que pertenece el director de carrera. (EjbSeguimientoEstadia.getListaEstudiantesSeguimientoArea)", e, null);
        }
    }
    
     /**
     * Permite validar o invalidar el seguimiento de estadía del estudiante
     * @param seguimientoEstadiaEstudiante
     * @param rol
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> validarInformacionEstadia(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, String rol){
        try{
            SeguimientoEstadiaEstudiante segEstBD = em.find(SeguimientoEstadiaEstudiante.class,  seguimientoEstadiaEstudiante.getSeguimiento());
            String mensaje = "";
            
            switch (rol) {
                case "coordinadorArea":
                    if (segEstBD.getValidacionCoordinador()) {
                        segEstBD.setValidacionCoordinador(Boolean.FALSE);
                        segEstBD.setFechaValidacionCoordinador(new Date());
                        em.merge(segEstBD);
                        f.flush();
                        mensaje = "El seguimiento de estadía se ha invalidado correctamente por el coordinador.";
                    } else {
                        segEstBD.setValidacionCoordinador(Boolean.TRUE);
                        segEstBD.setFechaValidacionCoordinador(new Date());
                        em.merge(segEstBD);
                        f.flush();
                        mensaje = "El seguimiento de estadía se ha validado correctamente por el coordinador.";
                    }
                    break;
                case "directorArea":
                    if (segEstBD.getValidacionDirector()) {
                        segEstBD.setValidacionDirector(Boolean.FALSE);
                        segEstBD.setFechaValidacionDirector(new Date());
                        em.merge(segEstBD);
                        f.flush();
                        mensaje = "El seguimiento de estadía se ha invalidado correctamente por el director.";
                    } else {
                        segEstBD.setValidacionDirector(Boolean.TRUE);
                        segEstBD.setFechaValidacionDirector(new Date());
                        em.merge(segEstBD);
                        f.flush();
                        mensaje = "El seguimiento de estadía se ha validado correctamente por el director.";
                    }
                    break;
                    
                case "vinculacion":
                    if (segEstBD.getValidacionVinculacion()) {
                        segEstBD.setValidacionVinculacion(Boolean.FALSE);
                        segEstBD.setFechaValidacionVinculacion(new Date());
                        em.merge(segEstBD);
                        f.flush();
                        mensaje = "El seguimiento de estadía se ha invalidado correctamente por el coordinador de estadía institucional.";
                    } else {
                        segEstBD.setValidacionVinculacion(Boolean.TRUE);
                        segEstBD.setFechaValidacionVinculacion(new Date());
                        em.merge(segEstBD);
                        f.flush();
                        mensaje = "El seguimiento de estadía se ha validado correctamente por el coordinador de estadía institucional.";
                    }
                    break;
               
                default:

                    break;
            }
            
                       
            return ResultadoEJB.crearCorrecto(seguimientoEstadiaEstudiante, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar o invalidar el seguimiento de estadía. (EjbSeguimientoEstadia.validarEstadiaDirector)", e, null);
        }
    }
    
     /**
     * Permite buscar el documento que se desea cargar del estudiante seleccionado
     * @param nombreDocumento
     * @return Resultado del proceso
     */
     public ResultadoEJB<Documento> getDocumentoEstadia(String nombreDocumento) {
        try{
            Documento documento = em.createQuery("SELECT d FROM Documento d WHERE d.descripcion = :descripcion AND d.activo=:valor",Documento.class)
                    .setParameter("descripcion", nombreDocumento)
                    .setParameter("valor", Boolean.TRUE)
                    .getResultStream().findFirst().orElse(null);
                       
            return ResultadoEJB.crearCorrecto(documento, "Resultado de la búsqueda del documento de estadía.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda correctamente del documento de estadía. (EjbSeguimientoEstadia.buscarDocumentoEstadia)", e, null);
        }
    }
     
      /**
     * Permite buscar el documento relacionado con el proceso que se desea cargar del estudiante seleccionado
     * @param documento
     * @return Resultado del proceso
     */
     public ResultadoEJB<DocumentoProceso> getDocumentoProcesoEstadia(Documento documento) {
        try{
            DocumentoProceso documentoProceso = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.documento=:documento",DocumentoProceso.class)
                    .setParameter("documento", documento)
                    .getResultStream().findFirst().orElse(null);
                       
            return ResultadoEJB.crearCorrecto(documentoProceso, "Resultado de la búsqueda del documento relacionado con el proceso de estadía.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda correctamente del documento relacionado con el proceso de estadía. (EjbSeguimientoEstadia.buscarDocumentoProcesoEstadia)", e, null);
        }
    }
    
    /** MÉTODOS PARA CALENDARIO DE EVENTOS DE ESTADÍA **/
    
      /**
     * Permite validar si el usuario autenticado es personal adscrito al departamento de servicios escolares
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarRolesEstadia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==18) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorCategoriaOperativa").orElse(18)));
            }
            else if (p.getPersonal().getAreaSuperior()== 2 && p.getPersonal().getCategoriaOperativa().getCategoria()==48) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.AREA_SUPERIOR.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorAreaSuperior").orElse(2)));
                filtro.addParam(PersonalFiltro.CATEGORIA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("directorEncargadoCategoriaOperativa").orElse(48)));
            }
            else if (!ejbAsignacionRolesEstadia.asignacionesEstadia(clave).getValor().isEmpty()) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (!asignacionesCoordinadorEstadia(clave).getValor().isEmpty()) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getCategoriaOperativa().getCategoria() == 15 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getCategoriaOperativa().getCategoria()== 18 || p.getPersonal().getAreaOperativa()==5 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            else if (p.getPersonal().getCategoriaOperativa().getCategoria()== 38 || p.getPersonal().getCategoriaOperativa().getCategoria()==43) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal con rol asignado en el proceso de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbSeguimientoEstadia.validarRolesEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos de estadía de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCalendarioEventosEstadia>> getCalendarioEventosEstadia(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            List<DtoCalendarioEventosEstadia> listaEventos = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.generacion=:generacion AND e.nivel=:nivel ORDER BY e.evento ASC", EventoEstadia.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .map(evento -> packEvento(evento).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEventos, "Lista de eventos de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de estadía. (EjbSeguimientoEstadia.getCalendarioEventosEstadia)", e, null);
        }
    }
    
     /**
     * Empaqueta un evento de estadía del proceso en su DTO Wrapper
     * @param eventoEstadia
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoCalendarioEventosEstadia> packEvento(EventoEstadia eventoEstadia){
        try{
            if(eventoEstadia == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un evento nulo.", DtoCalendarioEventosEstadia.class);
            if(eventoEstadia.getEvento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un evento con clave nula.", DtoCalendarioEventosEstadia.class);

            EventoEstadia eventoEstadiaBD = em.find(EventoEstadia.class, eventoEstadia.getEvento());
            if(eventoEstadiaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un evento no registrado previamente en base de datos.", DtoCalendarioEventosEstadia.class);
            
            Boolean eventoActivo = buscarEventoActivo(eventoEstadiaBD, eventoEstadiaBD.getActividad(), eventoEstadiaBD.getUsuario()).getValor();
            
            String situacion = "";
            if (eventoActivo) {
                situacion = "circuloVerde";
            }else{
                situacion = "circuloRojo";
            }
                    
            DtoCalendarioEventosEstadia dto = new DtoCalendarioEventosEstadia(eventoEstadiaBD, situacion);
            return ResultadoEJB.crearCorrecto(dto, "Evento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el evento (EjbSeguimientoEstadia.packEvento).", e, DtoCalendarioEventosEstadia.class);
        }
    }
    
     /* MÓDULO SEGUIMIENTO COORDINADOR DE ESTADÍA DE VINCULACIÓN */
    
     /**
     * Permite validar si el usuario autenticado es coordinador de estadías
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarCoordinadorEstadia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getCategoriaOperativa().getCategoria() == 15 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }else if (p.getPersonal().getCategoriaOperativa().getCategoria() == 18 && p.getPersonal().getAreaOperativa() == 5 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como coordinador de estadías de extensión y vinculación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar como coordinador de estadías de extensión y vinculación. (EjbSeguimientoEstadia.validarCoordinadorEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de estudiantes con seguimiento de estadía de la generación, nivel y programa educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param programaEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoSeguimientoEstadia>> getListaEstudiantesSeguimientoCoordinadorEstadias(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, AreasUniversidad programaEducativo){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
            List<DtoSeguimientoEstadia> seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento AND s.estudiante.grupo.idPe=:programa", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .setParameter("programa", programaEducativo.getArea())
                    .getResultStream()
                    .map(seg -> packSeguimiento(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoSeguimientoEstadia::compareTo)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "Lista de estudiantes con seguimiento de estadía de la generación, nivel y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes con seguimiento de estadía de la generación, nivel y programa educativo seleccionado. (EjbSeguimientoEstadia.getListaEstudiantesSeguimientoCoordinadorEstadias)", e, null);
        }
    }
    
     /**
     * Permite cambiar la situación académica del estudiante seleccionado, de regular a egresado no titulado o viceversa
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Estudiante> cambiarSituacionAcademicaEstudiante(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            
            Estudiante estudiante = em.find(Estudiante.class, seguimientoEstadiaEstudiante.getEstudiante().getIdEstudiante());
            
            if(estudiante == null) return ResultadoEJB.crearErroneo(2, "El estudiante no puede ser nulo.", Estudiante.class);
                
            String mensaje = "La situación académica del estudiante es " + estudiante.getTipoEstudiante().getDescripcion();
            if (estudiante.getTipoEstudiante().getIdTipoEstudiante()==1) {
                TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short)4);
                estudiante.setTipoEstudiante(tipoEstudiante);
                em.merge(estudiante);
                f.flush();
                mensaje="La situación académica del estudiante ha cambiado de REGULAR a EGRESADO NO TITULADO.";
            } else if(estudiante.getTipoEstudiante().getIdTipoEstudiante()==4){
                TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short)1);
                estudiante.setTipoEstudiante(tipoEstudiante);
                em.merge(estudiante);
                f.flush();
                mensaje="La situación académica del estudiante ha cambiado de EGRESADO NO TITULADO a REGULAR.";
            }
            
            return ResultadoEJB.crearCorrecto(estudiante, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar o invalidar el documento seleccionado. (EjbSeguimientoEstadia.validarDocumento)", e, null);
        }
    }
    
    /**
     * Permite generar el archivo de excel el listado de seguimiento de estadía de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     * @throws java.lang.Throwable
     */
    
    public String getReporteSeguimientoEstadia(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo) throws Throwable {
        String gen = generacion.getInicio()+ "-" + generacion.getFin();
        String niv = nivelEducativo.getNivel();
        String rutaPlantilla = "C:\\archivos\\seguimientoEstadia\\seguimientoEstadiaVinculacion.xlsx";
        String rutaPlantillaC = ejbCarga.crearDirectorioReportesEstadia(gen,niv);

        String plantillaC = rutaPlantillaC.concat(ACTUALIZADO_ESTADIA_VINCULACION);
        
        Map beans = new HashMap();
        beans.put("generacion", gen);
        beans.put("nivel", nivelEducativo.getNombre());
        beans.put("segEst", getListaEstudiantesSeguimientoCoordinadorEstadiasReporte(generacion, nivelEducativo).getValor());
        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(rutaPlantilla, beans, plantillaC);

        return plantillaC;
    }
    
    
    /**
     * Permite obtener el reporte de la lista de estudiantes con seguimiento de estadía de la generación, nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoSeguimientoEstadiaReporte>> getListaEstudiantesSeguimientoCoordinadorEstadiasReporte(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
            List<DtoSeguimientoEstadiaReporte> seguimientoEstadia = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .getResultStream()
                    .map(seg -> packSeguimientoReporte(seg, eventoSeleccionado).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoSeguimientoEstadiaReporte::compareTo)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(seguimientoEstadia, "El reporte de la lista de estudiantes con seguimiento de estadía de la generación, nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el reporte de la lista de estudiantes con seguimiento de estadía de la generación, nivel educativo seleccionado. (EjbSeguimientoEstadia.getListaEstudiantesSeguimientoCoordinadorEstadiasReporte)", e, null);
        }
    }
    
    /**
     * Empaqueta el seguimiento de estadía para reporte en un DTO Wrapper para obtener datos del estudiante
     * @param seguimientoEstadiaEstudiante Seguimiento de estadía a empaquetar
     * @param eventoSeleccionado
     * @return Seguimiento de estadía empaquetada
     */
    public ResultadoEJB<DtoSeguimientoEstadiaReporte> packSeguimientoReporte(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante, EventoEstadia eventoSeleccionado){
        try{
            if(seguimientoEstadiaEstudiante == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar seguimiento nulo.", DtoSeguimientoEstadiaReporte.class);
            if(seguimientoEstadiaEstudiante.getSeguimiento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar seguimiento con clave nula.", DtoSeguimientoEstadiaReporte.class);

            SeguimientoEstadiaEstudiante segEstBD = em.find(SeguimientoEstadiaEstudiante.class, seguimientoEstadiaEstudiante.getSeguimiento());
            if(segEstBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar seguimiento no registrado previamente en base de datos.", DtoSeguimientoEstadiaReporte.class);

            DtoDatosEstudiante dtoDatosEstudiante = packEstudiante(seguimientoEstadiaEstudiante, eventoSeleccionado).getValor();
            
            Personal asesorEstadia = em.find(Personal.class, seguimientoEstadiaEstudiante.getAsesor().getPersonal());

            AreasUniversidad area = em.find(AreasUniversidad.class, dtoDatosEstudiante.getProgramaEducativo().getAreaSuperior());

            Personal director = em.find(Personal.class, area.getResponsable());

            String nombreEmpresa = "Sin información", direccionEmpresa = "Sin información", proyecto = "Sin información";
            
            if (seguimientoEstadiaEstudiante.getEmpresa() != null) {
                OrganismosVinculados empresa = em.createQuery("SELECT o FROM OrganismosVinculados o WHERE o.empresa=:clave", OrganismosVinculados.class)
                        .setParameter("clave", seguimientoEstadiaEstudiante.getEmpresa())
                        .getResultStream().findFirst().orElse(null);
                
                if (empresa != null) {
                    nombreEmpresa = empresa.getNombre();
                    direccionEmpresa = empresa.getDireccion();
                }
            }
            
            if(segEstBD.getProyecto()!= null){
                proyecto = segEstBD.getProyecto();
            }
            
            Double semanas = 0.0;
            if(seguimientoEstadiaEstudiante.getFechaInicio() != null && seguimientoEstadiaEstudiante.getFechaFin() != null){
                    semanas = calcularSemanasEstadia(seguimientoEstadiaEstudiante.getFechaInicio(), seguimientoEstadiaEstudiante.getFechaFin()).getValor();
            }
            
            String validacionDireccion = "Sin validar", validacionCartaPresentacion = "Sin validar", comentariosCartaPresentacion = "Sin información", validacionCartaAceptacion = "Sin validar", comentariosCartaAceptacion = "Sin información", validacionLiberacionEmpresarial = "Sin validar", comentariosLiberacionEmpresarial = "Sin información", validacionEvaluacioEmpresarial = "Sin validar", comentariosEvaluacioEmpresarial = "Sin información", validacionCartaAcreditacion = "Sin validar", comentariosCartaAcreditacion = "Sin información", validacionEstadia = "Sin validar";

            if(segEstBD.getValidacionDirector()){
                validacionDireccion = "Validado";
            }
            
            if(segEstBD.getValidacionVinculacion()){
                validacionEstadia = "Validado";
            }
            
            DocumentoSeguimientoEstadia cartaPresentacion = buscarDocumentoEstudiante(segEstBD, 33).getValor();
            if (cartaPresentacion.getDocumentoSeguimiento() != null) {
                if (cartaPresentacion.getValidado()) {
                    validacionCartaPresentacion = "Validado";
                }
                comentariosCartaPresentacion = cartaPresentacion.getObservaciones();
            }
            
            DocumentoSeguimientoEstadia cartaAceptacion = buscarDocumentoEstudiante(segEstBD, 34).getValor();
            if (cartaAceptacion.getDocumento() != null) {
                if (cartaAceptacion.getValidado()) {
                    validacionCartaAceptacion = "Validado";
                }
                comentariosCartaAceptacion = cartaAceptacion.getObservaciones();
            }

            DocumentoSeguimientoEstadia cartaLibEmp = buscarDocumentoEstudiante(segEstBD, 39).getValor();
            if (cartaLibEmp.getDocumento() != null) {
                if (cartaLibEmp.getValidado()) {
                    validacionLiberacionEmpresarial = "Validado";
                }
                comentariosLiberacionEmpresarial = cartaLibEmp.getObservaciones();
            }
            
            DocumentoSeguimientoEstadia evalLib = buscarDocumentoEstudiante(segEstBD, 40).getValor();
            if (evalLib.getDocumento() != null) {
                if (evalLib.getValidado()) {
                    validacionEvaluacioEmpresarial = "Validado";
                }
                comentariosEvaluacioEmpresarial = evalLib.getObservaciones();
            }
            
            DocumentoSeguimientoEstadia cartaAcreditacion = buscarDocumentoEstudiante(segEstBD, 41).getValor();
            if (cartaAcreditacion.getDocumento() != null) {
                if (cartaAcreditacion.getValidado()) {
                    validacionCartaAcreditacion = "Validado";
                }
                comentariosCartaAcreditacion = cartaAcreditacion.getObservaciones();
            }
            
            DtoSeguimientoEstadiaReporte dtoSeguimientoEstadiaReporte = new DtoSeguimientoEstadiaReporte(seguimientoEstadiaEstudiante, dtoDatosEstudiante, asesorEstadia, area, director, nombreEmpresa, direccionEmpresa, proyecto, semanas, validacionDireccion, validacionCartaPresentacion, comentariosCartaPresentacion, validacionCartaAceptacion, comentariosCartaAceptacion, validacionLiberacionEmpresarial, comentariosLiberacionEmpresarial, validacionEvaluacioEmpresarial, comentariosEvaluacioEmpresarial, validacionCartaAcreditacion, comentariosCartaAcreditacion, validacionEstadia);
            
            return ResultadoEJB.crearCorrecto(dtoSeguimientoEstadiaReporte, "Seguimiento de estadía empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el seguimiento de estadía. (EjbSeguimientoEstadia.packSeguimiento).", e, DtoSeguimientoEstadiaReporte.class);
        }
    }
    
    
    
     /* MÓDULO CONSULTA DE EMPRESAS ASIGNADAS PARA COORDINACIÓN DE ESTADÍAS DE VINCULACIÓN */
    
    /**
     * Permite obtener la lista de empresas asignadas a seguimiento de estadía de la generación y nivel seleccionado
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEmpresaSeguimientosEstadia>> getListaEmpresasSeguimientoEstadias(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            EventoEstadia eventoSeleccionado = ejbAsignacionRolesEstadia.buscarEventoSeleccionado(generacion, nivelEducativo, "Asignacion estudiantes").getValor();
            
            List<DtoEmpresaSeguimientosEstadia> empresasEstadia = new ArrayList<>();
            
            List<SeguimientoEstadiaEstudiante> seguimientos = em.createQuery("SELECT s FROM SeguimientoEstadiaEstudiante s WHERE s.evento.evento=:evento", SeguimientoEstadiaEstudiante.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            List<Integer> empresasSeguimiento = seguimientos.stream().map(p -> p.getEmpresa()).filter(p-> p != null).distinct().collect(Collectors.toList());
            
            empresasSeguimiento.forEach(empSeg -> {
                OrganismosVinculados empresaEstadia = em.createQuery("SELECT o FROM OrganismosVinculados o WHERE o.empresa=:clave", OrganismosVinculados.class)
                        .setParameter("clave", empSeg)
                        .getResultStream().findFirst().orElse(null);
                
                if(empresaEstadia != null){
                    Integer cantSeg = (int) seguimientos.stream().filter(p -> Objects.equals(p.getEmpresa(), empSeg)).count();
                    DtoEmpresaSeguimientosEstadia dtoEmpresaSeguimientosEstadia = new DtoEmpresaSeguimientosEstadia(empresaEstadia, cantSeg);
                    empresasEstadia.add(dtoEmpresaSeguimientosEstadia);
                }
            });
            
            return ResultadoEJB.crearCorrecto(empresasEstadia.stream().sorted(DtoEmpresaSeguimientosEstadia::compareTo).collect(Collectors.toList()), "Lista de empresas asignadas a seguimiento de estadía de la generación y nivel seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de empresas asignadas a seguimiento de estadía de la generación y nivel seleccionado. (EjbSeguimientoEstadia.getListaEmpresasSeguimientoEstadias)", e, null);
        }
    }
    
     /**
     * Permite actualizar el nombre o dirección de una empresa
     * @param dtoEmpresaSeguimientosEstadia
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoEmpresaSeguimientosEstadia> actualizarDatosEmpresa(DtoEmpresaSeguimientosEstadia dtoEmpresaSeguimientosEstadia){
        try{
            em.merge(dtoEmpresaSeguimientosEstadia.getEmpresa());
            em.flush();
           
            return ResultadoEJB.crearCorrecto(dtoEmpresaSeguimientosEstadia, "Se actualizó correctamente el nombre o dirección de la empresa seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el nombre o dirección de la empresa seleccionada. (EjbSeguimientoEstadia.actualizarDatosEmpresa)", e, null);
        }
    }
    
    /* MÓDULO SEGUIMIENTO SERVICIOS ESCOLARES */
    
     /**
     * Permite desactivar o activar un seguimiento de estadía de un estudiante con situación académica de baja temporal o definitiva durante el proceso de estadía
     * @param seguimientoEstadiaEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoEstadiaEstudiante> desactivarSeguimientoEstadia(SeguimientoEstadiaEstudiante seguimientoEstadiaEstudiante){
        try{
            if(seguimientoEstadiaEstudiante.getSeguimiento()== null) return ResultadoEJB.crearErroneo(2, "La clave del seguimiento de estadía no puede ser nula.", SeguimientoEstadiaEstudiante.class);

            String mensaje = "La situación del seguimiento de estadía es " + seguimientoEstadiaEstudiante.getActivo();
            if (seguimientoEstadiaEstudiante.getActivo()) {
                seguimientoEstadiaEstudiante.setActivo(false);
                em.merge(seguimientoEstadiaEstudiante);
                f.flush();
                mensaje="El seguimiento de estadía se ha desactivado correctamente.";
            } else if(!seguimientoEstadiaEstudiante.getActivo()){
                seguimientoEstadiaEstudiante.setActivo(true);
                em.merge(seguimientoEstadiaEstudiante);
                f.flush();
                mensaje="El seguimiento de estadía se ha activado correctamente.";
            }

            return ResultadoEJB.crearCorrecto(seguimientoEstadiaEstudiante, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo desactivar o activar el seguimiento de estadía seleccionado. (EjbSeguimientoEstadia.desactivarSeguimientoEstadia)", e, null);
        }
    }
    
}
