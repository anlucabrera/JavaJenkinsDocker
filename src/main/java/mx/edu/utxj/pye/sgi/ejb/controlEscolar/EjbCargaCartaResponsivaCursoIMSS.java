/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCartaResponsivaCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDocumentosCartaRespCursoIMMSEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SeguimientoVinculacionEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.AperturaExtemporaneaEventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Documento;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbCargaCartaResponsivaCursoIMSS")
public class EjbCargaCartaResponsivaCursoIMSS {
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    List<EventoVinculacion> listaEventos = new ArrayList<>(); 
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
    /* MÓDULO CARGA DE DOCUMENTOS DEL ESTUDIANTE */
    
     /**
     * Permite validar si el usuario logueado es del tipo estudiante y deba visualizar el módulo para cargar Carta Responsiva y Curso IMMS
     * @param matricula
     * @return Regresa la instancia del estudiante si este cumple con lo mencionado
     */
    public ResultadoEJB<Estudiante> validarEstudiante(Integer matricula){
        try{
            Estudiante e = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula =:matricula ORDER BY e.periodo DESC",  Estudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultStream().findFirst().orElse(null); 
                
            List<SeguimientoVinculacionEstudiante> listaSeguimientoEstudiante = getSeguimientosVinculacionEstudiante(e).getValor();
            EventoVinculacion eventosActual = getEventoActualVinculacionEstudiante(e).getValor();
            List<EventoVinculacion> listaEventosPasados = getEventosVinculacionEstudiante(e).getValor();
            if(!listaSeguimientoEstudiante.isEmpty() || eventosActual.getEvento() != null || !listaEventosPasados.isEmpty()){
                return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante y con evento de vinculación disponible(s).");
            }else {
                return ResultadoEJB.crearErroneo(2, "El estudiante encontrado no tiene registro de evento de vinculación disponible(s).", Estudiante.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbCargaCartaResponsivaCursoIMSS.validarEstudiante)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de seguimientos de vinculación del estudiante registrados
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<SeguimientoVinculacionEstudiante>> getSeguimientosVinculacionEstudiante(Estudiante estudiante){
        try{
            
            List<SeguimientoVinculacionEstudiante> listaSeguimientoEstudiante = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.estudiante.matricula=:matricula AND s.activo=:valor ",  SeguimientoVinculacionEstudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("valor", true)
                    .getResultStream().collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaSeguimientoEstudiante, "Lista de de seguimientos de vinculación del estudiante registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de seguimientos de vinculación del estudiante registrado. (EjbCargaCartaResponsivaCursoIMSS.getSeguimientosVinculacionEstudiante)", e, null);
        }
    }
    
    /**
     * Permite verificar si existe evento actual disponible para carga de documentos del estudiante
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoVinculacion> getEventoActualVinculacionEstudiante(Estudiante estudiante){
        try{
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
           
            EventoVinculacion eventoVinculacionActual = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.actividad=:actividad",  EventoVinculacion.class)
                    .setParameter("generacion", estudiante.getGrupo().getGeneracion())
                    .setParameter("nivel", programaEducativo.getNivelEducativo().getNivel())
                    .setParameter("actividad", "Entrega Carta Responsiva")
                    .getResultStream().findFirst().orElse(null);
            
            return ResultadoEJB.crearCorrecto(eventoVinculacionActual, "Evento actual disponible para carga de documentos del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe evento actual disponible para carga de documentos del estudiante. (EjbCargaCartaResponsivaCursoIMSS.getEventoActualVinculacionEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos pasados disponible para carga de documentos del estudiante
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EventoVinculacion>> getEventosVinculacionEstudiante(Estudiante estudiante){
        try{
            List<Integer> listaGrados = new ArrayList<>(); listaGrados.add(6); listaGrados.add(11);
            
            List<Estudiante> listaEstudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula AND e.grupo.grado IN :grados AND e.tipoEstudiante.idTipoEstudiante=:tipo", Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("grados", listaGrados)
                    .setParameter("tipo", (int) 1)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            listaEstudiante.forEach(est -> {
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, est.getGrupo().getIdPe());
                
                listaEventos = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.nivel=:nivel AND e.generacion=:generacion AND e.actividad=:actividad", EventoVinculacion.class)
                    .setParameter("nivel", programaEducativo.getNivelEducativo().getNivel())
                    .setParameter("generacion", est.getGrupo().getGeneracion())
                    .setParameter("actividad", "Entrega Carta Responsiva")
                    .getResultStream()
                    .collect(Collectors.toList());
            });
            
            return ResultadoEJB.crearCorrecto(listaEventos, "Lista de eventos pasados disponible para carga de documentos del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos pasados disponible para carga de documentos del estudiante. (EjbCargaCartaResponsivaCursoIMSS.getEventosVinculacionEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos de vinculación disponibles (con seguimiento, actual y pasados sin seguimiento) para el estudiante
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EventoVinculacion>> getEventosEstudiante(Estudiante estudiante){
        try{
            List<EventoVinculacion> listaEventoVinculacionEstudiante = new ArrayList<>();
            
            List<SeguimientoVinculacionEstudiante> listaSeguimientoEstudiante = getSeguimientosVinculacionEstudiante(estudiante).getValor();
            
            if(listaSeguimientoEstudiante.isEmpty()){
                listaSeguimientoEstudiante.forEach(seg -> {
                    EventoVinculacion eventoVinculacion = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.actividad=:actividad",  EventoVinculacion.class)
                        .setParameter("generacion", seg.getEstudiante().getGrupo().getGeneracion())
                        .setParameter("nivel", seg.getNivel())
                        .setParameter("actividad", "Entrega Carta Responsiva")
                        .getResultStream().findFirst().orElse(null);
                    
                    listaEventoVinculacionEstudiante.add(eventoVinculacion);
                });
            }
            
            EventoVinculacion eventosActual = getEventoActualVinculacionEstudiante(estudiante).getValor();
            
            if(eventosActual.getEvento() != null){
               listaEventoVinculacionEstudiante.add(eventosActual);
            }
            
            List<EventoVinculacion> listaEventosPasados = getEventosVinculacionEstudiante(estudiante).getValor();
            
            if(!listaEventosPasados.isEmpty()){
                listaEventosPasados.forEach(evenPas -> {
                    listaEventoVinculacionEstudiante.add(evenPas);
                });
            }
           
            return ResultadoEJB.crearCorrecto(listaEventoVinculacionEstudiante.stream().distinct().sorted(Comparator.comparingInt(EventoVinculacion::getEvento).reversed()).collect(Collectors.toList()), "Lista de eventos de vinculación disponibles (con seguimiento, actual y pasados sin seguimiento) para el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la lista de eventos de vinculación disponibles (con seguimiento, actual y pasados sin seguimiento) para el estudiante. (EjbCargaCartaResponsivaCursoIMSS.listaEventosVinculacion)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de generaciones de los eventos disponibles para el estudiante
     * @param listaEventosVinculacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesEventos(List<EventoVinculacion> listaEventosVinculacion){
        try{
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            
            listaEventosVinculacion.forEach(evento -> {
                Generaciones generacion = em.find(Generaciones.class, evento.getGeneracion());
                listaGeneraciones.add(generacion);
            });
           
            return ResultadoEJB.crearCorrecto(listaGeneraciones.stream().distinct().sorted(Comparator.comparingInt(Generaciones::getGeneracion).reversed()).collect(Collectors.toList()), "Lista de generaciones de los eventos disponibles para el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones de los eventos disponibles para el estudiante. (EjbCargaCartaResponsivaCursoIMSS.getGeneracionesEventos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de niveles educativos de la generación seleccionada de los eventos disponibles para el estudiante
     * @param listaEventosVinculacion
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesGeneracionEventos(List<EventoVinculacion> listaEventosVinculacion, Generaciones generacion){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            
            listaEventosVinculacion.forEach(evento -> {
                if(evento.getGeneracion()== generacion.getGeneracion()){
                    ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, evento.getNivel());
                    listaNiveles.add(nivel);
                }
            });
           
            return ResultadoEJB.crearCorrecto(listaNiveles.stream().sorted(Comparator.comparing(ProgramasEducativosNiveles :: getNombre).reversed()).collect(Collectors.toList()), "Lista de niveles educativos de la generación seleccionada de los eventos disponibles para el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos de la generación seleccionada de los eventos disponibles para el estudiante. (EjbCargaCartaResponsivaCursoIMSS.getNivelesGeneracionEventos)", e, null);
        }
    }
    
    /**
     * Permite buscar si existe seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoVinculacionEstudiante> buscarSeguimientoEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Estudiante estudiante){
        try{
            SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.estudiante.matricula=:matricula AND s.estudiante.grupo.generacion=:generacion AND s.nivel=:nivel AND s.activo=:activo", SeguimientoVinculacionEstudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .setParameter("activo", true)
                    .getResultStream()
                    .findFirst().orElse(null);
            
            if(seguimientoVinculacionEstudiante == null){
                ResultadoEJB<SeguimientoVinculacionEstudiante> segVinEst = guardarSeguimientoVinculacionEstudiante(generacion, nivelEducativo, estudiante);
                if(segVinEst.getCorrecto()){
                    seguimientoVinculacionEstudiante = segVinEst.getValor();
                }
            }
            
            return ResultadoEJB.crearCorrecto(seguimientoVinculacionEstudiante, "Seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado. (EjbCargaCartaResponsivaCursoIMSS.buscarSeguimientoEstudiante)", e, null);
        }
    }
    
    /**
     * Permite guardar el seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoVinculacionEstudiante> guardarSeguimientoVinculacionEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Estudiante estudiante){
        try{
           Estudiante estudianteSeguimiento = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula AND e.grupo.generacion=:generacion AND e.tipoEstudiante.idTipoEstudiante=:tipo ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("tipo", (short) 1)
                    .getResultStream()
                    .findFirst().orElse(null);
           
           SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante = new SeguimientoVinculacionEstudiante();
           seguimientoVinculacionEstudiante.setEstudiante(estudianteSeguimiento);
           seguimientoVinculacionEstudiante.setNivel(nivelEducativo.getNivel());
           seguimientoVinculacionEstudiante.setActivo(true);
           em.persist(seguimientoVinculacionEstudiante);
           em.flush();
            
            
            return ResultadoEJB.crearCorrecto(seguimientoVinculacionEstudiante, "Se ha registrado correctamente el seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar correctamente el seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado. (EjbCargaCartaResponsivaCursoIMSS.guardarSeguimientoVinculacionEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la información del seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado
     * @param seguimientoVinculacionEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoCartaResponsivaCursoIMMSEstudiante> getSeguimientosEstudiante(SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante){
        try{
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, seguimientoVinculacionEstudiante.getEstudiante().getGrupo().getIdPe());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, seguimientoVinculacionEstudiante.getEstudiante().getPeriodo());
            DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(seguimientoVinculacionEstudiante.getEstudiante(), programaEducativo, periodoEscolar);
            
            List<DtoDocumentosCartaRespCursoIMMSEstudiante> listaDocumentos = em.createQuery("SELECT d FROM DocumentoProceso d WHERE d.proceso = :proceso AND d.obligatorio = :valor", DocumentoProceso.class)
                    .setParameter("proceso", "ResponsivaCursosIMSS")
                    .setParameter("valor", true)
                    .getResultStream()
                    .map(doc -> packDocumento(doc, seguimientoVinculacionEstudiante).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante = new DtoCartaResponsivaCursoIMMSEstudiante(seguimientoVinculacionEstudiante, dtoDatosEstudiante, listaDocumentos);
           
            return ResultadoEJB.crearCorrecto(dtoCartaResponsivaCursoIMMSEstudiante, "Información del seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información del seguimiento de vinculación del estudiante de la generación y nivel educativo seleccionado. (EjbCargaCartaResponsivaCursoIMSS.getSeguimientosEstudiante)", e, null);
        }
    }
    
     /**
     * Empaqueta un documento del proceso en su DTO Wrapper
     * @param documentoProceso
     * @param seguimientoVinculacionEstudiante
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoDocumentosCartaRespCursoIMMSEstudiante> packDocumento(DocumentoProceso documentoProceso, SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante){
        try{
            if(documentoProceso == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un documento nulo.", DtoDocumentosCartaRespCursoIMMSEstudiante.class);
            if(documentoProceso.getDocumentoProceso()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un documento con clave nula.", DtoDocumentosCartaRespCursoIMMSEstudiante.class);

            DocumentoProceso documentoProcesoBD = em.find(DocumentoProceso.class, documentoProceso.getDocumentoProceso());
            if(documentoProcesoBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un documento no registrado previamente en base de datos.", DtoDocumentosCartaRespCursoIMMSEstudiante.class);

            DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.seguimiento.seguimientoVinculacion=:seguimiento AND d.documento =:documento", DocumentoSeguimientoVinculacion.class)
                    .setParameter("seguimiento", seguimientoVinculacionEstudiante.getSeguimientoVinculacion())
                    .setParameter("documento", documentoProcesoBD.getDocumento())
                    .getResultStream()
                    .findFirst()
                    .orElse(new DocumentoSeguimientoVinculacion());
            
            Generaciones generacionBD = em.find(Generaciones.class, seguimientoVinculacionEstudiante.getEstudiante().getGrupo().getGeneracion());
            String generacion = Short.toString(generacionBD.getInicio()).concat("-").concat(Short.toString(generacionBD.getFin()));
            
            DtoDocumentosCartaRespCursoIMMSEstudiante dto = new DtoDocumentosCartaRespCursoIMMSEstudiante(documentoProceso, documentoSeguimientoVinculacion, generacion);
            return ResultadoEJB.crearCorrecto(dto, "Documento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el documento (EjbCargaCartaResponsivaCursoIMSS.packDocumento).", e, DtoDocumentosCartaRespCursoIMMSEstudiante.class);
        }
    }
    
    /**
     * Permite consultar si el documento ha sido registrado por el estudiante
     * @param documento
     * @param seguimientoVinculacionEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> consultarDocumento(Documento documento, SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante){
        try{
            DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.seguimiento.seguimientoVinculacion=:seguimiento AND d.documento.documento=:documento", DocumentoSeguimientoVinculacion.class)
                    .setParameter("seguimiento", seguimientoVinculacionEstudiante.getSeguimientoVinculacion())
                    .setParameter("documento", documento.getDocumento())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            Boolean valor;
            if (documentoSeguimientoVinculacion == null) {
                valor = false;
            } else {
                valor= true; 
            }
            return ResultadoEJB.crearCorrecto(valor, "Se ha realizado correctamente la consulta del documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se realizó correctamente la consulta del documento. (EjbCargaCartaResponsivaCursoIMSS.consultarDocumento)", e, null);
        }
    }
    
    /**
     * Permite verificar evento activo de la carga del documento correspondiente
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoVinculacion> buscarEventoActivoDocumento(DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante, DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        try{
            EventoVinculacion eventoReg = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.documentoProceso.documento.documento=:documento AND current_date between e.fechaInicio and e.fechaFin", EventoVinculacion.class)
                .setParameter("generacion", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getGrupo().getGeneracion())
                .setParameter("nivel", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getNivel())
                .setParameter("documento", dtoDocumentosCartaRespCursoIMMSEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                .getResultStream().findFirst().orElse(null);
            
           return ResultadoEJB.crearCorrecto(eventoReg, "Evento activo del documento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo encontrar evento activo del documento seleccionado. (EjbCargaCartaResponsivaCursoIMSS.buscarEventoActivoDocumento).", e, EventoVinculacion.class);
        }
    }
    
    /**
     * Permite verificar evento registrado de la carga del documento correspondiente
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoVinculacion> buscarEventoDocumento(DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante, DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        try{
            
           EventoVinculacion eventoReg = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion AND e.nivel=:nivel AND e.documentoProceso.documento.documento=:documento", EventoVinculacion.class)
                .setParameter("generacion", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getGrupo().getGeneracion())
                .setParameter("nivel", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getNivel())
                .setParameter("documento", dtoDocumentosCartaRespCursoIMMSEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                .getResultStream().findFirst().orElse(null);
           
           return ResultadoEJB.crearCorrecto(eventoReg, "Evento registrado del documento seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo encontrar evento registrado del documento seleccionado (EjbCargaCartaResponsivaCursoIMSS.buscarEventoDocumento).", e, EventoVinculacion.class);
        }
    }
    
    /**
     * Permite obtener evento de la apertura extemporánea activa de la carga del documento correspondiente
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoVinculacion> buscarAperturaExtemporaneaDocumento(DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante, DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        try{
            EventoVinculacion eventoReg = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel AND a.evento.documentoProceso.documento.documento=:documento AND a.seguimiento.seguimientoVinculacion=:seguimiento AND current_date between a.fechaInicio and a.fechaFin", AperturaExtemporaneaEventoVinculacion.class)
                .setParameter("generacion", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getGrupo().getGeneracion())
                .setParameter("nivel", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getNivel())
                .setParameter("documento", dtoDocumentosCartaRespCursoIMMSEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                .setParameter("seguimiento", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getSeguimientoVinculacion())
                .getResultStream()
                .map(p-> p.getEvento())
                .findFirst().orElse(null);
            
           return ResultadoEJB.crearCorrecto(eventoReg, "Evento de la apertura extemporánea de la carga del documento correspondiente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el evento de la apertura extemporánea de la carga del documento correspondiente (EjbCargaCartaResponsivaCursoIMSS.buscarAperturaExtemporaneaDocumento).", e, EventoVinculacion.class);
        }
    }
    
      /**
     * Permite obtener apertura extemporánea activa de la carga del documento correspondiente
     * @param dtoCartaResponsivaCursoIMMSEstudiante
     * @param dtoDocumentosCartaRespCursoIMMSEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<AperturaExtemporaneaEventoVinculacion> aperturaExtemporaneaDocumento(DtoCartaResponsivaCursoIMMSEstudiante dtoCartaResponsivaCursoIMMSEstudiante, DtoDocumentosCartaRespCursoIMMSEstudiante dtoDocumentosCartaRespCursoIMMSEstudiante){
        try{
            AperturaExtemporaneaEventoVinculacion aperturaExtemporanea = em.createQuery("SELECT a FROM AperturaExtemporaneaEventoVinculacion a WHERE a.evento.generacion=:generacion AND a.evento.nivel=:nivel AND a.evento.documentoProceso.documento.documento=:documento AND a.seguimiento.seguimientoVinculacion=:seguimiento AND current_date between a.fechaInicio and a.fechaFin", AperturaExtemporaneaEventoVinculacion.class)
                .setParameter("generacion", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getEstudiante().getGrupo().getGeneracion())
                .setParameter("nivel", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getNivel())
                .setParameter("documento", dtoDocumentosCartaRespCursoIMMSEstudiante.getDocumentoProceso().getDocumento().getDocumento())
                .setParameter("seguimiento", dtoCartaResponsivaCursoIMMSEstudiante.getSeguimientoVinculacionEstudiante().getSeguimientoVinculacion())
                .getResultStream()
                .findFirst().orElse(null);
            
           return ResultadoEJB.crearCorrecto(aperturaExtemporanea, "Apertura extemporánea de la carga del documento correspondiente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la apertura extemporánea de la carga del documento correspondiente (EjbCargaCartaResponsivaCursoIMSS.aperturaExtemporaneaDocumento).", e, AperturaExtemporaneaEventoVinculacion.class);
        }
    }
    
    /**
     * Permite guardar el documento del estudiante en la base de datos
     * @param documentoSeguimientoVinculacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoVinculacion> guardarDocumentoSeguimientoVinculacion(DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion){
        try{
            em.persist(documentoSeguimientoVinculacion);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(documentoSeguimientoVinculacion, "Se guardó correctamente el documento.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar el documento. (EjbCargaCartaResponsivaCursoIMSS.guardarDocumentoSeguimientoVinculacion)", e, null);
        }
    }
    
    /**
     * Permite eliminar un documento del seguimiento de estadía del estudiante
     * @param documentoSeguimientoVinculacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarDocumentoSeguimiento(DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion){
        try{
            if(documentoSeguimientoVinculacion.getDocumentoVinculacion()== null) return ResultadoEJB.crearErroneo(2, "La clave del documento en el seguimiento no puede ser nula.", Integer.TYPE);

            Integer id = documentoSeguimientoVinculacion.getDocumentoVinculacion();
            ServicioArchivos.eliminarArchivo(documentoSeguimientoVinculacion.getRuta());
            
            Integer delete = em.createQuery("DELETE FROM DocumentoSeguimientoVinculacion d WHERE d.documentoVinculacion=:documento", DocumentoSeguimientoVinculacion.class)
                .setParameter("documento", id)
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "El documento se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el documento correctamente. (EjbCargaCartaResponsivaCursoIMSS.eliminarDocumentoSeguimiento)", e, null);
        }
    }
    
    /* MÓDULO SEGUIMIENTO DE CARTA RESPONSIVA Y CURSO IMSS POR COORDINACIÓN DE ESTADÍAS */
    
    /**
     * Permite obtener la lista de generaciones en la que existen eventos de vinculación registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesSeguimientoRegistrados(){
        try{
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            
            List<Short> listaEventos = em.createQuery("SELECT e FROM EventoVinculacion e ORDER BY e.generacion DESC",  EventoVinculacion.class)
                    .getResultStream()
                    .map(p->p.getGeneracion())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaEventos.forEach(evento -> {
                Generaciones generacion = em.find(Generaciones.class, evento);
                listaGeneraciones.add(generacion);
            });
            
            return ResultadoEJB.crearCorrecto(listaGeneraciones.stream().sorted(Comparator.comparingInt(Generaciones::getGeneracion).reversed()).collect(Collectors.toList()), "Lista de generaciones en que existen eventos de vinculación registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones en los que existen eventos de vinculación registrados. (EjbCargaCartaResponsivaCursoIMSS.getGeneracionesSeguimientoRegistrados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de niveles educativos de la generación seleccionada en la que existen eventos de vinculación registrados
     * @param generacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesSeguimientoRegistrados(Generaciones generacion){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
            
            List<String> listaClavesNiveles = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion ORDER BY e.nivel DESC",  EventoVinculacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream()
                    .map(p->p.getNivel())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaClavesNiveles.forEach(clave -> {
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, clave);
                listaNiveles.add(nivel);
            });
            
            return ResultadoEJB.crearCorrecto(listaNiveles.stream().sorted(Comparator.comparing(ProgramasEducativosNiveles::getNivel).reversed()).collect(Collectors.toList()), "Lista de niveles educativos de la generación seleccionada en la que existen eventos de vinculación registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos de la generación seleccionada en la que existen eventos de vinculación registrados. (EjbCargaCartaResponsivaCursoIMSS.getNivelesSeguimientoRegistrados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de programas educativos del nivel y la generación seleccionada en la que existen eventos de vinculación registrados
     * @param generacion
     * @param nivelEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosSeguimientoRegistrados(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo){
        try{
            List<AreasUniversidad> listaProgramas = new ArrayList<>();
            
            List<Short> listaClavesProgramas = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.estudiante.grupo.generacion=:generacion AND s.nivel=:nivel",  SeguimientoVinculacionEstudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .getResultStream()
                    .map(p->p.getEstudiante().getGrupo().getIdPe())
                    .distinct()
                    .collect(Collectors.toList());
          
            listaClavesProgramas.forEach(clave -> {
                AreasUniversidad programa = em.find(AreasUniversidad.class, clave);
                listaProgramas.add(programa);
            });
            
            return ResultadoEJB.crearCorrecto(listaProgramas.stream().sorted(Comparator.comparing(AreasUniversidad::getNombre)).collect(Collectors.toList()), "Lista programas educativos del nivel y la generación seleccionada en la que existen eventos de vinculación registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos del nivel y la generación seleccionada en la que existen eventos de vinculación registrados. (EjbCargaCartaResponsivaCursoIMSS.getProgramasEducativosSeguimientoRegistrados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de seguimientos de vinculación registrados de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivelEducativo
     * @param programaEducativo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCartaResponsivaCursoIMMSEstudiante>> getListaSeguimientosVinculacion(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, AreasUniversidad programaEducativo){
        try{
            List<DtoCartaResponsivaCursoIMMSEstudiante> seguimientoVinculacion = em.createQuery("SELECT s FROM SeguimientoVinculacionEstudiante s WHERE s.estudiante.grupo.generacion=:generacion AND s.nivel=:nivel AND s.estudiante.grupo.idPe=:programa", SeguimientoVinculacionEstudiante.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivelEducativo.getNivel())
                    .setParameter("programa", programaEducativo.getArea())
                    .getResultStream()
                    .map(seg -> getSeguimientosEstudiante(seg).getValor())
                    .filter(dto -> dto != null)
                    .sorted(DtoCartaResponsivaCursoIMMSEstudiante::compareTo)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(seguimientoVinculacion, "Lista de seguimientos de vinculación registrados de la generación, nivel y programa educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de seguimientos de vinculación registrados de la generación, nivel y programa educativo seleccionado. (EjbCargaCartaResponsivaCursoIMSS.getListaSeguimientosVinculacion)", e, null);
        }
    }
    
    /**
     * Permite buscar el documento registrado por el estudiante
     * @param seguimientoVinculacionEstudiante
     * @param documento
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoVinculacion> buscarDocumentoEstudiante(SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante, Integer documento){
        try{
            DocumentoSeguimientoVinculacion documentoReg = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.seguimiento.seguimientoVinculacion=:seguimiento AND d.documento.documento=:documento", DocumentoSeguimientoVinculacion.class)
                    .setParameter("seguimiento",  seguimientoVinculacionEstudiante.getSeguimientoVinculacion())
                    .setParameter("documento", documento)
                    .getResultStream().findFirst().orElse(new DocumentoSeguimientoVinculacion());
            
            return ResultadoEJB.crearCorrecto(documentoReg, "Documento registrado del estudiante");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe documento registrado del estudiante (EjbCargaCartaResponsivaCursoIMSS.buscarDocumentoEstudiante).", e, DocumentoSeguimientoVinculacion.class);
        }
    }
    
     /**
     * Permite validar o invalidar un documento del seguimiento de estadía del estudiante
     * @param documento
     * @param seguimientoVinculacionEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoVinculacion> validarDocumento(Integer documento, SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante){
        try{
            DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.seguimiento.seguimientoVinculacion=:seguimiento AND d.documento.documento =:documento", DocumentoSeguimientoVinculacion.class)
                    .setParameter("seguimiento", seguimientoVinculacionEstudiante.getSeguimientoVinculacion())
                    .setParameter("documento", documento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            if(documentoSeguimientoVinculacion == null) return ResultadoEJB.crearErroneo(2, "El documento no puede ser nulo.", DocumentoSeguimientoVinculacion.class);
                
            String mensaje;
            if (documentoSeguimientoVinculacion.getValidado()) {
                documentoSeguimientoVinculacion.setValidado(Boolean.FALSE);
                documentoSeguimientoVinculacion.setFechaValidacion(new Date());
                em.merge(documentoSeguimientoVinculacion);
                em.flush();
                mensaje="El documento se ha invalidado correctamente.";
            } else {
                documentoSeguimientoVinculacion.setValidado(Boolean.TRUE);
                documentoSeguimientoVinculacion.setFechaValidacion(new Date());
                em.merge(documentoSeguimientoVinculacion);
                em.flush();
                mensaje="El documento se ha validado correctamente.";
            }
            
                       
            return ResultadoEJB.crearCorrecto(documentoSeguimientoVinculacion, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar o invalidar el documento seleccionado. (EjbCargaCartaResponsivaCursoIMSS.validarDocumento)", e, null);
        }
    }
    
    /**
     * Permite registrar comentarios a un documento del estudiante
     * @param comentario
     * @param claveDocumento
     * @param seguimientoVinculacionEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DocumentoSeguimientoVinculacion> guardarComentarioDocumento(String comentario, Integer claveDocumento, SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante){
        try{
            DocumentoSeguimientoVinculacion documentoSeguimientoVinculacion = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.seguimiento.seguimientoVinculacion=:seguimiento AND d.documento.documento =:documento", DocumentoSeguimientoVinculacion.class)
                    .setParameter("seguimiento", seguimientoVinculacionEstudiante.getSeguimientoVinculacion())
                    .setParameter("documento", claveDocumento)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
           
            documentoSeguimientoVinculacion.setObservaciones(comentario);
            em.merge(documentoSeguimientoVinculacion);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(documentoSeguimientoVinculacion, "Se registró correctamente el comentario al documento del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el comentario al documento del estudiante. (EjbCargaCartaResponsivaCursoIMSS.guardarComentarioDocumento)", e, null);
        }
    }
    
     /**
     * Permite validar o invalidar un documento del seguimiento de estadía del estudiante
     * @param seguimientoVinculacionEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<SeguimientoVinculacionEstudiante> validarSeguimiento(SeguimientoVinculacionEstudiante seguimientoVinculacionEstudiante){
        try{
            List<DocumentoSeguimientoVinculacion> listaDocumentos = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.seguimiento.seguimientoVinculacion=:seguimiento AND d.validado=:valor", DocumentoSeguimientoVinculacion.class)
                    .setParameter("seguimiento", seguimientoVinculacionEstudiante.getSeguimientoVinculacion())
                    .setParameter("valor", true)
                    .getResultStream()
                    .collect(Collectors.toList());
            
             
            String mensaje ="";
            if (!seguimientoVinculacionEstudiante.getValidado() && listaDocumentos.size() == 2) {
                  seguimientoVinculacionEstudiante.setValidado(Boolean.TRUE);
                  em.merge(seguimientoVinculacionEstudiante);
                  em.flush();
                  mensaje="El seguimiento se ha validado correctamente.";
            } else if(seguimientoVinculacionEstudiante.getValidado() && listaDocumentos.size() != 2){
                seguimientoVinculacionEstudiante.setValidado(Boolean.FALSE);
                em.merge(seguimientoVinculacionEstudiante);
                em.flush();
                mensaje="El seguimiento se ha invalidado correctamente.";
            }
            
                       
            return ResultadoEJB.crearCorrecto(seguimientoVinculacionEstudiante, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar o invalidar el documento seleccionado. (EjbCargaCartaResponsivaCursoIMSS.validarDocumento)", e, null);
        }
    }
    
}
