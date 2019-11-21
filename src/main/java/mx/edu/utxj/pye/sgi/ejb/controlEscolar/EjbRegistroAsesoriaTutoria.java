/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.servlet.http.Part;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsesoriaCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasoCritico;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCasosCriticosPendientes;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudianteComplete;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoListadoTutores;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoParticipantesAsesoriaCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoParticipantesTutoriaGrupalCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPlanAccionTutorial;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoTutoriaIndividualCE;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoUnidadConfiguracion;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Asesoria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.CasoCritico;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EstudiantesPye;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.FuncionesTutor;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.ParticipantesTutoriaGrupalPK;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.PlanAccionTutorial;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.SesionesGrupalesTutorias;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasGrupales;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.TutoriasIndividuales;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.enums.CasoCriticoEstado;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.exception.EvidenciaRegistroExtensionNoValidaException;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;
import mx.edu.utxj.pye.sgi.util.ServicioArchivos;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroAsesoriaTutoria")
public class EjbRegistroAsesoriaTutoria {
    @EJB        EjbPersonalBean             ejbPersonalBean;
    @EJB        EjbValidadorDocente         ejbValidadorDocente;
    @EJB        EjbPeriodoEventoRegistro    ejbPeriodoEventoRegistro;
    @EJB        EjbAsignacionAcademica      ejbAsignacionAcademica;
    @EJB        Facade                      f;
    @EJB        EjbPacker                   pack;
    @EJB        EjbCasoCritico              cc;
    @EJB        EjbPropiedades              ep;
    private     EntityManager               em;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            return ejbValidadorDocente.validarDocente(clave);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar. (EjbRegistroAsesoriaTutoria.validarDocente)", e, null);
        }
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarTutor(Integer clave){
        try {
            return ejbValidadorDocente.validarTutor(clave);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "El docente no se pudo validar como tutor. (EjbRegistroAsesoriaTutoria.validarTutor)", e, null);
        }
    }
    
    public ResultadoEJB<EventosRegistros> verificarEvento(){
        try{
            return ejbPeriodoEventoRegistro.getEventoRegistro();
        }catch(Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura del evento del registro mensual (EjbRegistroAsesoriaTutoria.verificarEvento).", e, EventosRegistros.class);
        }
    }
    
    public ResultadoEJB<PeriodosEscolares> getPeriodoEscolarActivo(){
        try{
            return ejbPeriodoEventoRegistro.getPeriodoEscolarActivo();
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el periodo escolar activo (EjbRegistroAsesoriaTutoria.getPeriodoEscolarActivo).", e, PeriodosEscolares.class);
        }
    }
    
//    public ResultadoEJB<List<DtoUnidadConfiguracion>> getUnidadesEnEvaluacion(PersonalActivo docente){
//        try{
//            return ejbValidadorDocente.getUnidadesEnEvaluacion(docente);
//        }catch (Exception e){
//            return ResultadoEJB.crearErroneo(1, "No se pudieron obtener las unidades con evaluación activa por docente según la fecha actual (EjbRegistroAsesoriaTutoria.getUnidadesEnEvaluacion).", e, null);
//        }
//    }
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCapturaCargaAcademica(PersonalActivo docente){
        try{
            return ejbValidadorDocente.getPeriodosConCapturaCargaAcademica(docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que el docente ha tenido carga académica (EjbRegistroAsesoriaTutoria.getPeriodosConCapturaAsesorias).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoCargaAcademica>> getCargasAcademicasPorPeriodo(PersonalActivo docente, PeriodosEscolares periodo){
        try{
            return ejbValidadorDocente.getCargasAcademicasPorPeriodo(docente, periodo);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas cadémicas por docente y periodo (EjbRegistroAsesoriaTutoria.getCargasAcadémicasPorPeriodo).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoUnidadConfiguracion>> getConfiguraciones(DtoCargaAcademica dtoCargaAcademica) {
        try {
            return ejbValidadorDocente.getConfiguraciones(dtoCargaAcademica);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de unidades y configuraciones (EjbRegistroAsesoriaTutoria.getConfiguraciones).", e, null);
        }
    }
    
    public ResultadoEJB<Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>>> comprobarEventoActual(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoRegistroActivo, PersonalActivo docente) throws PeriodoEscolarNecesarioNoRegistradoException {
        try {
            return ejbPeriodoEventoRegistro.comprobarEventoActualDocente(periodos, eventos, eventoRegistroActivo, docente);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de periodos escolares y eventos registros (EjbRegistroAsesoriaTutoria.comprobarEventoActual).", e, null);
        }
    }
    
    public ResultadoEJB<List<EventosRegistros>> getEventosRegistroPorPeriodo(PeriodosEscolares periodo){
        try{
            return ejbPeriodoEventoRegistro.getEventosRegistroPorPeriodo(periodo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de registro por periodo (EjbRegistroAsesoriaTutoria.getEventosRegistroPorPeriodo).", e, null);
        }
    }
    
    public ResultadoEJB<DtoAsesoriaCE> guardaAsesoriaCE(DtoAsesoriaCE dtoAsesoriaCE){
        try{
            if((buscaAsesoriaCEInsercion(dtoAsesoriaCE).getCorrecto())){
                Asesoria asesoriaRegistrada = dtoAsesoriaCE.getAsesoria();
                em.persist(asesoriaRegistrada);
                dtoAsesoriaCE.setAsesoria(asesoriaRegistrada);
                return ResultadoEJB.crearCorrecto(dtoAsesoriaCE, "Registro de Asesoría guardada correctamente en el sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "El registro de esta asesoria ya se encuentra en sistema, favor de verificar la información", DtoAsesoriaCE.class);
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar la asesoria (EjbRegistroAsesoriaTutoria.guardaAsesoriaCE).", e, DtoAsesoriaCE.class);
        }
    }

    public ResultadoEJB<Boolean> editaAsesoria(Asesoria asesoria) {
        try {
            em.merge(asesoria);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La asesoría se ha actualizado correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la asesoria seleccionada (EjbRegistroAsesoriaTutoria.editaAsesoria)", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<Boolean> eliminaAsesoria(Integer idAsesoria) {
        try {
            Asesoria a = em.find(Asesoria.class, idAsesoria);
            if(a.getEstudianteList().isEmpty()){
                em.remove(a);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La asesoría se ha eliminado del sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "No se ha podido eliminar la aesoría debido a que tiene participantes asignados ", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la asesoría (EjbRegistroAsesoriaTutoria.eliminaAsesoria).", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<DtoAsesoriaCE> actualizaAsesoriaCE(DtoAsesoriaCE dtoAsesoriaCE){
        try {
            if(buscaAsesoriaCEEdicion(dtoAsesoriaCE).getResultados().isEmpty()){
                Asesoria asesoriaActualizada = dtoAsesoriaCE.getAsesoria();
                em.merge(asesoriaActualizada);
                dtoAsesoriaCE.setAsesoria(asesoriaActualizada);
                return ResultadoEJB.crearCorrecto(dtoAsesoriaCE, "Edición de Asesoría guardada correctamente en el sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "Los datos que ha registrado corresponden a una asesoría ya existente, favor de verificar su información", DtoAsesoriaCE.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo editar la información de la asesoría (EjbRegistroAsesoriaTutoria.actualizaAsesoriaCE)." ,e, DtoAsesoriaCE.class);
        }
    }
    
    public ResultadoEJB<List<Asesoria>> buscaAsesoriaCEInsercion(DtoAsesoriaCE dtoAsesoriaCE) {
        try {
            List<Asesoria> asesorias = em.createQuery("SELECT a FROM Asesoria a WHERE a.configuracion.configuracion = :configuracion AND a.fechaHora = :fechaHora AND a.tipo = :tipo AND a.eventoRegistro = :eventoRegistro", Asesoria.class)
                    .setParameter("configuracion", dtoAsesoriaCE.getAsesoria().getConfiguracion().getConfiguracion())
                    .setParameter("fechaHora", dtoAsesoriaCE.getAsesoria().getFechaHora())
                    .setParameter("tipo", dtoAsesoriaCE.getAsesoria().getTipo())
                    .setParameter("eventoRegistro", dtoAsesoriaCE.getEventosRegistros().getEventoRegistro())
                    .getResultList();
            if(asesorias.isEmpty()){
                return ResultadoEJB.crearCorrecto(asesorias, "Puede registrar la asesoría, no se han encontrado coincidencias");
            }else{
                return ResultadoEJB.crearErroneo(3, null,"Lista de una posible coincidencia de su registro de asesoría");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la asesoría que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaAsesoriaCEInsercion.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaAsesoriaCEInsercion.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<List<Asesoria>> buscaAsesoriaCEEdicion(DtoAsesoriaCE dtoAsesoriaCE){
        try {
            List<Asesoria> asesorias = em.createQuery("SELECT a FROM Asesoria a WHERE a.configuracion.configuracion = :configuracion AND a.fechaHora = :fechaHora AND a.tipo = :tipo AND a.eventoRegistro = :eventoRegistro AND a.idAsesoria <> :idAsesoria", Asesoria.class)
                    .setParameter("configuracion", dtoAsesoriaCE.getAsesoria().getConfiguracion().getConfiguracion())
                    .setParameter("fechaHora", dtoAsesoriaCE.getAsesoria().getFechaHora())
                    .setParameter("tipo", dtoAsesoriaCE.getAsesoria().getTipo())
                    .setParameter("eventoRegistro", dtoAsesoriaCE.getEventosRegistros().getEventoRegistro())
                    .setParameter("idAsesoria", dtoAsesoriaCE.getAsesoria().getIdAsesoria())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(asesorias, "Lista de una posible coincidencia de su registro de asesoría");
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta actualizar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaAsesoriaCEEdicion.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta actualizar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaAsesoriaCEEdicion.Exception).", e, null);
        }   
    }
    
    public ResultadoEJB<List<Asesoria>> buscaAsesoriasPorUnidadEventoRegistro(Integer configuracion, Integer eventoRegistro){
        try {
            List<Asesoria> asesorias = em.createQuery("SELECT a FROM Asesoria a WHERE a.configuracion.configuracion = :configuracion AND a.eventoRegistro = :eventoRegistro", Asesoria.class)
                    .setParameter("configuracion", configuracion)
                    .setParameter("eventoRegistro", eventoRegistro)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(asesorias, "Asesorías encontradas en la unidad y evento de registro seleccionada");
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesorías (EjbRegistroAsesoriaTutoria.buscaAsesoriasPorUnidad.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesorías, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaAsesoriasPorUnidad.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> verificarParticipanteAsesoria(Asesoria asesoria, Integer estudiante){
        try {
            Asesoria a = em.find(Asesoria.class, asesoria.getIdAsesoria());
            em.refresh(a);
            
            Estudiante est = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.asesoriaList a WHERE a.idAsesoria = :asesoria AND e.idEstudiante = :estudiante", Estudiante.class)
                    .setParameter("asesoria", a.getIdAsesoria())
                    .setParameter("estudiante", estudiante)
                    .getSingleResult();
            
            em.refresh(est);
            
            if (!est.getAsesoriaList().isEmpty()) {
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Verificado");
            } else {
                return ResultadoEJB.crearErroneo(1, Boolean.FALSE, "No se ha encontrado el resultado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la consulta (EjbRegistroAsesoriaTutoria.verificarParticipanteAsesoria.Exception)",e, null);
        }
    }
    
    public ResultadoEJB<Boolean> verificarParticipanteAsesoriaParaTutoria(Integer estudiante, Integer configuracion){
        try {
            Estudiante est = em.createQuery("SELECT e FROM Estudiante e INNER JOIN e.asesoriaList a WHERE a.configuracion.configuracion = :configuracion AND e.idEstudiante = :estudiante", Estudiante.class)
                    .setParameter("configuracion", configuracion)
                    .setParameter("estudiante", estudiante)
                    .getSingleResult();
            
            em.refresh(est);
            
            if (!est.getAsesoriaList().isEmpty()) {
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Verificado");
            } else {
                return ResultadoEJB.crearErroneo(1, Boolean.FALSE, "No se ha encontrado el resultado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la consulta (EjbRegistroAsesoriaTutoria.verificarParticipanteAsesoria.Exception)",e, null);
        }
    }
    
    public ResultadoEJB<Boolean> eliminarParticipanteAsesoria(Asesoria asesoria, Integer estudiante) {
        try {
            Asesoria as = em.find(Asesoria.class, asesoria.getIdAsesoria());
            Estudiante es = em.find(Estudiante.class, estudiante);
            em.refresh(as);
            em.refresh(es);
            if ((verificarParticipanteAsesoria(as, es.getIdEstudiante())).getCorrecto()) {
                as.getEstudianteList().remove(es);
                es.getAsesoriaList().remove(as);
                em.flush();
            }
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha removido el estudiante de la asesoría");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la eliminación del estudiante (EjbRegistroAsesoriaTutoria.eliminarParticipanteAsesoria.Exception)", e, null);
        }
    }

    public ResultadoEJB<Boolean> asignaParticipanteAsesoria(Asesoria asesoria, Integer estudiante){
        try {
            Asesoria a = em.find(Asesoria.class, asesoria.getIdAsesoria());
            Estudiante e = em.find(Estudiante.class, estudiante);
            
            em.refresh(a);
            em.refresh(e);
            
            if(verificarParticipanteAsesoria(asesoria, estudiante).getCorrecto()){
                eliminarParticipanteAsesoria(asesoria, estudiante);
            }else{
                a.getEstudianteList().add(e);
                e.getAsesoriaList().add(a);
                em.flush();
            }
            em.flush();
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha asignado el estudiante a la asesoría");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo asignar este estudiante a la asesoria seleccionada (EjbRegistroAsesoriaTutoria.asignaParticipanteAsesoria.Exception).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoParticipantesAsesoriaCE>> obtenerListaEstudiantes(Grupo grupo){
        try {
            List<DtoParticipantesAsesoriaCE> lista = new ArrayList<>();
            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.grupo.idGrupo = :grupo AND e.tipoEstudiante.idTipoEstudiante = 1 ORDER BY e.aspirante.idPersona.apellidoPaterno, e.aspirante.idPersona.apellidoMaterno, e.aspirante.idPersona.nombre", Estudiante.class)
                    .setParameter("grupo", grupo.getIdGrupo())
                    .getResultList();
            estudiantes.forEach((e) -> {
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, e.getCarrera());
                PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, e.getPeriodo());
                DtoDatosEstudiante dto = new DtoDatosEstudiante(e, programaEducativo, periodoEscolar);
                Boolean participacion = false;
                DtoParticipantesAsesoriaCE dtoPACE = new DtoParticipantesAsesoriaCE(dto, participacion);
                lista.add(dtoPACE);
            });
            return ResultadoEJB.crearCorrecto(lista, "Lista de estudiantes del grupo seleccionado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes del grupo seleccionado", e, null);
        }
    }
    
    /////////////////////////////////////////////////// Acciones de Tutoría //////////////////////////////////////////////////////////////////////////////
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCapturaCargaAcademicaTutor(PersonalActivo docente) {
        try {
            return ejbValidadorDocente.getPeriodosConCapturaCargaAcademicaTutor(docente);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares en los que su grupo ha tenido docentes con carga académica (EjbRegistroAsesoriaTutoria.getPeriodosConCapturaCargaAcademicaTutor).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoCargaAcademica>> getCargasAcademicasPorPeriodoTutor(PersonalActivo docente, PeriodosEscolares periodo) {
        try {
            return ejbValidadorDocente.getCargasAcademicasPorPeriodoTutor(docente, periodo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas cadémicas por docente y periodo (EjbRegistroAsesoriaTutoria.getCargasAcademicasPorPeriodoTutor).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoUnidadConfiguracion>> getConfiguracionesTutor(DtoCargaAcademica dtoCargaAcademica) {
        try {
            return ejbValidadorDocente.getConfiguracionesTutor(dtoCargaAcademica);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de unidades y configuraciones (EjbRegistroAsesoriaTutoria.getConfiguracionesTutor).", e, null);
        }
    }
    
    public ResultadoEJB<Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>>> comprobarEventoActualTutor(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoRegistroActivo, PersonalActivo docente) throws PeriodoEscolarNecesarioNoRegistradoException {
        try {
            return ejbPeriodoEventoRegistro.comprobarEventoActualTutor(periodos, eventos, eventoRegistroActivo, docente);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de periodos escolares y eventos registros (EjbRegistroAsesoriaTutoria.comprobarEventoActualTutor).", e, null);
        }
    }
    
    /**
     * Método que permite la busqueda de los grupos tutorados del docente logueado detectado como tutor
     * @param periodoEscolar
     * @param tutor
     * @return 
     */
    public ResultadoEJB<List<DtoListadoTutores>> listarGruposTutor(PeriodosEscolares periodoEscolar, PersonalActivo tutor){
        try{
            return ejbValidadorDocente.listarGruposTutor(periodoEscolar, tutor);
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el listado de grupos por tutor (EjbRegistroAsesoriaTutoria.listarGruposTutor)", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda del plan de acción tutorial registrado en el grupo que el tutor haya seleccionado, para que automáticamente se vea reflejado en la interfaz de usuario
     * @param grupo
     * @return 
     */
    public ResultadoEJB<List<PlanAccionTutorial>> buscaPlanAccionTutorialExistente(Grupo grupo){
        try {
            List<PlanAccionTutorial> listarPlanAccionTutorial = new ArrayList<>();
            listarPlanAccionTutorial = em.createQuery("SELECT pat FROM PlanAccionTutorial pat WHERE pat.grupo.idGrupo = :grupo", PlanAccionTutorial.class)
                    .setParameter("grupo", grupo.getIdGrupo())
                    .getResultList();
            if(listarPlanAccionTutorial.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null,"Aún no se ha registrado un plan de acción tutorial para el grupo seleccionado");
            }else{
                return ResultadoEJB.crearCorrecto(listarPlanAccionTutorial, "Plan de acción tutorial encontrado");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el plan de acción tutorial del grupo seleccionado (EjbRegistroAsesoriaTutoria.buscaPlanAccionTutorialExistente)", e, null);
        }
    }
    
    /**
     * Método que permite al tutor asignar un plan de acción tutorial.
     * @param planAccionTutorial
     * @return 
     */
    public ResultadoEJB<PlanAccionTutorial> guardaPlanAccionTutorial(PlanAccionTutorial planAccionTutorial){
        try {
            PlanAccionTutorial pat = new PlanAccionTutorial();
            if(!(buscaPlanAccionTutorialExistente(planAccionTutorial.getGrupo()).getCorrecto())){
                pat = planAccionTutorial;
                em.persist(pat);
                return ResultadoEJB.crearCorrecto(pat, "El registro del plan de acción tutorial ha sido guardado correctamente en el sistema");
            }else{
                pat = planAccionTutorial;
                em.merge(pat);
                return ResultadoEJB.crearCorrecto(pat, "El registro del plan de acción tutorial ha sido actualizado correctamente en el sistema");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el plan de acción tutorial del grupo seleccionado (EjbRegistroAsesoriaTutoria.guardaPlanAccionTutorial)", e, null);
        }
    }
    
    /**
     * Método que permite la búsqueda de sesiones grupales asignadas a un plan de acción tutorial. 
     * @param grupo
     * @return 
     */
    public ResultadoEJB<List<SesionesGrupalesTutorias>> buscaSesionesGrupalesXPlanAT(Grupo grupo) {
        try {
            List<SesionesGrupalesTutorias> sesiones = new ArrayList<>();
            sesiones = em.createQuery("SELECT sgt FROM SesionesGrupalesTutorias sgt WHERE sgt.planAccionTutoria.planAccionTutoria = :planAccionTutoria", SesionesGrupalesTutorias.class)
                    .setParameter("planAccionTutoria", grupo.getIdGrupo())
                    .getResultList();
            if (sesiones.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, null, "No se han encontrado sesiones grupales asignadas al Plan de Accion Tutorial");
            } else {
                return ResultadoEJB.crearCorrecto(sesiones, "Lista de sesiones por plan de acción tutorial encontrada");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la busqueda de la sesiones grupales del Plan de Acción Tutorial (EjbRegistroAsesoriaTutoria.buscaSesionesGrupalesXPlanAT)", e, null);
        }
    }

    /**
     * Método que permite la eliminación de un Plan de acción tutorial validando que previamente no existan registros de sesiones grupales ya registradas en el plan que se desee eliminar
     * @param planAccionTutorial
     * @return 
     */
    public ResultadoEJB<Boolean> eliminarPlanAccionTutorial(PlanAccionTutorial planAccionTutorial) {
        try {
            if(planAccionTutorial.getValidacionDirector() == false) {
                if (!(buscaSesionesGrupalesXPlanAT(planAccionTutorial.getGrupo()).getCorrecto())) {
                    PlanAccionTutorial pat = em.find(PlanAccionTutorial.class, planAccionTutorial.getPlanAccionTutoria());
                    em.remove(pat);
                    em.flush();
                    return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El plan de acción tutorial se ha removido del sistema");
                } else {
                    return ResultadoEJB.crearErroneo(2, Boolean.FALSE, "No se ha podido eliminar el plan de acción debido a que ya tiene asignadas Sesiones Grupales");
                }
            } else {
                return ResultadoEJB.crearErroneo(3, Boolean.FALSE, "No se ha podido eliminar el plan de acción tutorial, debido a que ya ha sido validado por el director de carrera");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el plan de acción tutorial seleccionado (EjbRegistroAsesoriaTutoria.eliminarPlanAccionTutorial)", e, Boolean.TYPE);
        }
    }
    
    /**
     * Método que permite la búsqueda de una meta de la función del tutor en su Plan de acción tutorial para evitar el duplicado de la misma.
     * @param funcionTutor
     * @return 
     */
//    public ResultadoEJB<List<FuncionesTutor>> buscaFuncionTutorExistente(FuncionesTutor funcionTutor){
//        try {
//            List<FuncionesTutor> listarFunciones = new ArrayList<>();
//            listarFunciones = em.createQuery("SELECT ft FROM FuncionesTutor ft WHERE ft.planAccionTutoria.planAccionTutoria = :planAccionTutoria AND ft.metaFuncionTutor = :metaFuncionTutor", FuncionesTutor.class)
//                    .setParameter("planAccionTutoria", funcionTutor.getPlanAccionTutoria().getPlanAccionTutoria())
//                    .setParameter("metaFuncionTutor", funcionTutor.getMetaFuncionTutor())
//                    .getResultList();
//            if(listarFunciones.isEmpty()){
//                return ResultadoEJB.crearErroneo(2, null,"No se ha registrado previamente la función del tutor");
//            }else{
//                return ResultadoEJB.crearCorrecto(listarFunciones, "Funcion del tutor encontrada");
//            }
//        }catch (Exception e){
//            return ResultadoEJB.crearErroneo(1, "No se pudo consultar la función del tutor (EjbRegistroAsesoriaTutoria.buscaFuncionTutorExistente)", e, null);
//        }
//    }
//    
    /**
     * Método que permite consultar las metas como funciones de tutor de un plan de acción tutorial en especifico
     * @param planAccionTutorial
     * @return 
     */
    public ResultadoEJB<List<FuncionesTutor>> buscaFuncionesTutor(PlanAccionTutorial planAccionTutorial){
        try {
            List<FuncionesTutor> listarFunciones = new ArrayList<>();
            listarFunciones = em.createQuery("SELECT ft FROM FuncionesTutor ft WHERE ft.planAccionTutoria.planAccionTutoria = :planAccionTutoria", FuncionesTutor.class)
                    .setParameter("planAccionTutoria", planAccionTutorial.getPlanAccionTutoria())
                    .getResultList();
            if(listarFunciones.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null,"No se han registrado previamente funciones del tutor");
            }else{
                return ResultadoEJB.crearCorrecto(listarFunciones, "Funciones del tutor del plan de acción tutorial");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo consultar la función del tutor (EjbRegistroAsesoriaTutoria.buscaFuncionesTutor)", e, null);
        }
    }
    
    /**
     * Método que permite registrar metas como función de tutor en el plan de acción tutorial 
     * @param funcionTutor
     * @return 
     */
    public ResultadoEJB<FuncionesTutor> guardarFuncionTutor(FuncionesTutor funcionTutor){
        try {
            FuncionesTutor ft = new FuncionesTutor();
            ft = funcionTutor;
            short verificarRegistros = 0;
            verificarRegistros = em.createQuery("SELECT MAX(ft.noSesion) FROM FuncionesTutor ft WHERE ft.planAccionTutoria.planAccionTutoria = :planAccionTutoria", Short.class)
                    .setParameter("planAccionTutoria", funcionTutor.getPlanAccionTutoria().getPlanAccionTutoria())
                    .getResultStream()
                    .findFirst()
                    .orElse((short)0);
            if(verificarRegistros == 0){
                ft.setNoSesion((short) 1);
                em.persist(ft);
            }else{
                int verificarInt = verificarRegistros;
                int consecutivo = verificarInt + 1;
                ft.setNoSesion((short)consecutivo);
                em.persist(ft);
            }
            return ResultadoEJB.crearCorrecto(ft, "La función del tutor en el plan de acción tutorial ha sido guardada correctamente en sistema");
        } catch (NonUniqueResultException nure) {
            return ResultadoEJB.crearErroneo(2, "Se ha encontrado mas de un resultado al momento de asignar el consecutivo en la Función del tutor", nure, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la función del tutor del Plan Acción Tutorial (EjbRegistroAsesoriaTutoria.guardarFuncionTutor)", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> editaFuncionTutor(FuncionesTutor funcionTutor) {
        try {
            em.merge(funcionTutor);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La función del tutor se ha actualizado correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la función del tutor seleccionada (EjbRegistroAsesoriaTutoria.editaFuncionTutor)", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<Boolean> eliminarFuncionTutor(Integer funcionTutor){
        try {
            Integer planAccionTutorial = em.find(FuncionesTutor.class, funcionTutor).getPlanAccionTutoria().getPlanAccionTutoria();
            em.remove(em.find(FuncionesTutor.class, funcionTutor));
            FuncionesTutor ft = em.find(FuncionesTutor.class, funcionTutor);
            if(ft == null){
                List<FuncionesTutor> lista = em.createQuery("SELECT ft FROM FuncionesTutor ft WHERE ft.planAccionTutoria.planAccionTutoria = :planAccionTutoria ORDER BY ft.noSesion", FuncionesTutor.class)
                    .setParameter("planAccionTutoria", planAccionTutorial)
                    .getResultList();
                if (!lista.isEmpty()) {
                    IntStream.range(0, lista.size()).forEach(i -> {
                        int noSesion = i+1;
                        lista.get(i).setNoSesion((short)noSesion);
                        em.merge(lista.get(i));
                    });
                }
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La función del tutor ha sido eliminada correctamente del sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "No se podido eliminar la función del tutor, el valor sigue asignado en el plan de acción tutorial", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar la función de tutor del plan de acción tutorial (EjbRegistroAsesoriaTutoria.eliminarFuncionTutor)", e, null);
        }
    }
    
    
//    /**
//     * Método que permite la búsqueda de una sesión grupal en especifico para así evitar la duplicidad de la información
//     * @param sesionGrupalTutoria
//     * @return 
//     */
//    public ResultadoEJB<List<SesionesGrupalesTutorias>> buscaSesionesGrupalExistente(SesionesGrupalesTutorias sesionGrupalTutoria) {
//        try {
//            List<SesionesGrupalesTutorias> sesiones = new ArrayList<>();
//            sesiones = em.createQuery("SELECT sgt FROM SesionesGrupalesTutorias sgt WHERE sgt.planAccionTutoria.planAccionTutoria = :planAccionTutoria AND sgt.noSesion = :noSesion", SesionesGrupalesTutorias.class)
//                    .setParameter("planAccionTutoria", sesionGrupalTutoria.getPlanAccionTutoria())
//                    .setParameter("noSesion", sesionGrupalTutoria.getNoSesion())
//                    .getResultList();
//            if (sesiones.isEmpty()) {
//                return ResultadoEJB.crearErroneo(2, null, "No se ha encontrado la sesión grupal registrada");
//            } else {
//                return ResultadoEJB.crearCorrecto(sesiones, "Se ha encontrado uno o mas resultados de la sesión que ha ingresado");
//            }
//        } catch (Exception e) {
//            return ResultadoEJB.crearErroneo(1, "No se ha podido realizar la busqueda de la sesion grupal del Plan de Acción Tutorial (EjbRegistroAsesoriaTutoria.buscaSesionesGrupalExistente)", e, null);
//        }
//    }
//    
    
    /**
     * Método que permite guardar SesionesGrupalesTutorias así como asignar de manera automática el consecutivo contable de cada Sesión
     * @param sesionGrupal
     * @return 
     */
    public ResultadoEJB<SesionesGrupalesTutorias> guardaSesionGrupalTutoria(SesionesGrupalesTutorias sesionGrupal) {
        try {
            SesionesGrupalesTutorias sgt = new SesionesGrupalesTutorias();
            sgt = sesionGrupal;
            short verificarRegistros = 0;
            verificarRegistros = em.createQuery("SELECT MAX(sgt.noSesion) FROM SesionesGrupalesTutorias sgt WHERE sgt.planAccionTutoria.planAccionTutoria = :planAccionTutoria", Short.class)
                    .setParameter("planAccionTutoria", sgt.getPlanAccionTutoria().getPlanAccionTutoria())
                    .getResultStream()
                    .findFirst()
                    .orElse((short)0);
            if(verificarRegistros == 0){
                sgt.setNoSesion((short) 1);
                em.persist(sgt);
            }else{
                int consecutivo = verificarRegistros + 1;
                sgt.setNoSesion((short)consecutivo);
                em.persist(sgt);
            }
            return ResultadoEJB.crearCorrecto(sgt, "La Sesión Grupal de la tutoría ha sido guardada correctamente en sistema");
        } catch (NonUniqueResultException nure) {
            return ResultadoEJB.crearErroneo(2, "Se ha encontrado mas de un resultado al momento de asignar el consecutivo en la Sesion Grupal", nure, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la Sesión Grupal del Plan Acción Tutorial (EjbRegistroAsesoriaTutoria.guardaSesionGrupalTutoria)", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> editaSesionGrupal(SesionesGrupalesTutorias sesionGrupal) {
        try {
            em.merge(sesionGrupal);
            return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La sesión grupal del tutor se ha actualizado correctamente");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la sesión grupal seleccionada (EjbRegistroAsesoriaTutoria.editaSesionGrupal)", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<Boolean> actualizaValorSesionGrupalCumplimiento(SesionesGrupalesTutorias sesionGrupal) {
        try {
            SesionesGrupalesTutorias sesion = em.find(SesionesGrupalesTutorias.class, sesionGrupal.getSesionGrupal());
            if (!sesion.getCumplimiento()) {
                sesion.setCumplimiento(Boolean.TRUE);
                sesion.setJustificacion("No Aplica");
                em.merge(sesion);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La sesión grupal en el plan de acción tutorial se ha actualizado correctamente");
            } else {
                return ResultadoEJB.crearErroneo(2, Boolean.FALSE, "No es necesario volver a actualizar el valor de la sesión grupal");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el valor de cumplimiento y justificacion de la sesión grupal (EjbRegistroAsesoriaTutoria.actualizaValorSesionGrupalCumplimiento)", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<Boolean> actualizaValorSesionGrupalCumplimientoEliminacion(SesionesGrupalesTutorias sesionGrupal) {
        try {
            List<TutoriasGrupales> tutorias = em.createQuery("SELECT tg FROM TutoriasGrupales tg INNER JOIN tg.sesionGrupal sg WHERE sg.sesionGrupal = :sesionGrupal", TutoriasGrupales.class)
                    .setParameter("sesionGrupal", sesionGrupal.getSesionGrupal())
                    .getResultList();
            if(tutorias.isEmpty()){
                sesionGrupal.setCumplimiento(Boolean.FALSE);
                sesionGrupal.setJustificacion("En espera de registro de tutorías grupales");
                return  ResultadoEJB.crearCorrecto(Boolean.TRUE, "Se ha actualizado correctamente el valor de la sesión grupal");
            }else{
                return ResultadoEJB.crearErroneo(2, Boolean.FALSE, "No es necesario actualizar debido a que aún se encuentran tutorias grupales registradas");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar el valor de cumplimiento y justificacion de la sesión grupal (EjbRegistroAsesoriaTutoria.actualizaValorSesionGrupalCumplimientoEliminacion)", e, Boolean.TYPE);
        }
    }

    public ResultadoEJB<Boolean> eliminarSesionGrupal(Integer sesionGrupal){
        try {
            Integer planAccionTutorial = em.find(SesionesGrupalesTutorias.class, sesionGrupal).getPlanAccionTutoria().getPlanAccionTutoria();
            em.remove(em.find(SesionesGrupalesTutorias.class, sesionGrupal));
            SesionesGrupalesTutorias sg = em.find(SesionesGrupalesTutorias.class, sesionGrupal);
            if(sg == null){
                List<SesionesGrupalesTutorias> lista = em.createQuery("SELECT sgt FROM SesionesGrupalesTutorias sgt WHERE sgt.planAccionTutoria.planAccionTutoria = :planAccionTutoria ORDER BY sgt.noSesion", SesionesGrupalesTutorias.class)
                    .setParameter("planAccionTutoria", planAccionTutorial)
                    .getResultList();
                if (!lista.isEmpty()) {
                    IntStream.range(0, lista.size()).forEach(i -> {
                        int noSesion = i+1;
                        lista.get(i).setNoSesion((short)noSesion);
                        em.merge(lista.get(i));
                    });
                }
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La sesión grupal ha sido eliminada correctamente del sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "No se podido eliminar la sesión grupal, el valor sigue asignado en el plan de acción tutorial", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar sesión grupal del plan de acción tutorial (EjbRegistroAsesoriaTutoria.eliminarSesionGrupal)", e, null);
        }
    }
    
    public ResultadoEJB<List<TutoriasGrupales>> buscaTutoriaGrupalInsercion(TutoriasGrupales tutoriaGrupal){
        try {
            List<TutoriasGrupales> tutoriasGrupales = em.createQuery("SELECT t FROM TutoriasGrupales t WHERE t.sesionGrupal.sesionGrupal = :sesionGrupal AND t.fecha = :fecha AND t.horaInicio = :horaInicio AND t.horaCierre = :horaCierre", TutoriasGrupales.class)
                    .setParameter("sesionGrupal", tutoriaGrupal.getSesionGrupal().getSesionGrupal())
                    .setParameter("fecha", tutoriaGrupal.getFecha())
                    .setParameter("horaInicio", tutoriaGrupal.getHoraInicio())
                    .setParameter("horaCierre", tutoriaGrupal.getHoraCierre())
                    .getResultList();
            if(tutoriasGrupales.isEmpty()){
                return ResultadoEJB.crearCorrecto(tutoriasGrupales, "No se ha encontrado ninguna coincidencia, puede registrar la tutoria");
            }else{
                return ResultadoEJB.crearErroneo(3, null, "Se ha encontrado una tutoría grupal que coincide con los datos registrados, verifique su información");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la tutoría grupal que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaTutoriaGrupal.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaTutoriaGrupal.Exception).", e, null);
        }    
    }
    
    public ResultadoEJB<List<TutoriasGrupales>> buscaTutoriaGrupalEdicion(TutoriasGrupales tutoriaGrupal){
        try {
            List<TutoriasGrupales> tutoriasGrupales = em.createQuery("SELECT t FROM TutoriasGrupales t WHERE t.sesionGrupal.sesionGrupal = :sesionGrupal AND t.fecha = :fecha AND t.horaInicio = :horaInicio AND t.horaCierre = :horaCierre AND t.tutoriaGrupal <> :tutoriaGrupal", TutoriasGrupales.class)
                    .setParameter("sesionGrupal", tutoriaGrupal.getSesionGrupal().getSesionGrupal())
                    .setParameter("fecha", tutoriaGrupal.getFecha())
                    .setParameter("horaInicio", tutoriaGrupal.getHoraInicio())
                    .setParameter("horaCierre", tutoriaGrupal.getHoraCierre())
                    .setParameter("tutoriaGrupal", tutoriaGrupal.getTutoriaGrupal())
                    .getResultList();
            if(tutoriasGrupales.isEmpty()){
                return ResultadoEJB.crearCorrecto(tutoriasGrupales, "No se ha encontrado ninguna coincidencia, puede registrar la tutoria");
            }else{
                return ResultadoEJB.crearErroneo(3, null, "Se ha encontrado una tutoría grupal que coincide con los datos registrados, verifique su información");
            }
        } catch (NoResultException e) {
            return ResultadoEJB.crearErroneo(2, "No se pudo realizar la búsqueda de la tutoría grupal que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaTutoriaGrupal.NoResultException).", e, null);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo realizar la búsqueda de la asesoría que intenta registrar, por ello no es permitido guardar su registro (EjbRegistroAsesoriaTutoria.buscaTutoriaGrupal.Exception).", e, null);
        }    
    }
    
    public ResultadoEJB<TutoriasGrupales> guardaTutoriaGrupal(TutoriasGrupales tutoriaGrupal){
        try {
            if (buscaTutoriaGrupalInsercion(tutoriaGrupal).getCorrecto()) {
                TutoriasGrupales tg = new TutoriasGrupales();
                tg = tutoriaGrupal;
                em.persist(tg);
                actualizaValorSesionGrupalCumplimiento(tg.getSesionGrupal());
                return ResultadoEJB.crearCorrecto(tg, "El registro de la tutoria grupal ha sido guardado correctamente en el sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "El registro de esta tutoria grupal ya se encuentra en sistema, favor de verificar la información", TutoriasGrupales.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo guardar correctamente la tutoria grupal ingresada (EjbRegistroAsesoriaTutoria.guardaTutoriaGrupal)", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> editaTutoriaGrupal(TutoriasGrupales tutoriaGrupal) {
        try {
            if (buscaTutoriaGrupalEdicion(tutoriaGrupal).getCorrecto()) {
                em.merge(tutoriaGrupal);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La tutorial grupal se ha actualizado correctamente");
            }else{
                return ResultadoEJB.crearErroneo(2, "El registro de esta tutoría grupal ya se encuentra en sistema, favor de verificar la información", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la tutoría grupal seleccionada (EjbRegistroAsesoriaTutoria.editaTutoriaGrupal)", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<Boolean> eliminaTutoriaGrupal(Integer idTutoriaGrupal) {
        try {
            TutoriasGrupales t = em.find(TutoriasGrupales.class, idTutoriaGrupal);
            SesionesGrupalesTutorias sesionGrupal = t.getSesionGrupal();
            if(t.getParticipantesTutoriaGrupalList().isEmpty()){
                em.remove(t);
                actualizaValorSesionGrupalCumplimientoEliminacion(sesionGrupal);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La tutoría grupal se ha eliminado del sistema");
            }else{
                return ResultadoEJB.crearErroneo(2, "No se ha podido eliminar la tutoría grupal debido a que tiene participantes asignados ", Boolean.TYPE);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la tutoría grupal (EjbRegistroAsesoriaTutoria.eliminaTutoriaGrupal).", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<TutoriasGrupales> buscaTutoriaGrupal(TutoriasGrupales tutoriaGrupal){
        try {
            TutoriasGrupales tutoriaGrupalActualizada = em.find(TutoriasGrupales.class, tutoriaGrupal.getTutoriaGrupal());
            return ResultadoEJB.crearCorrecto(tutoriaGrupalActualizada, "La tutoría grupal se ha actualzado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la tutoría grupal (EjbRegistroAsesoriaTutoria.buscaTutoriaGrupal).", e, TutoriasGrupales.class);
        }
    }
    
    public ResultadoEJB<Estudiante> buscaEstudiante(Integer estudiante){
        try {
            return ResultadoEJB.crearCorrecto(em.find(Estudiante.class, estudiante), "Estudiante encontrado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo encontrar al estudiante (EjbRegistroAsesoriaTutoria.buscaEstudiante).", e, Estudiante.class);
        }
    }
    
    public ResultadoEJB<List<DtoEstudianteComplete>> buscarEstudiante(String pista, Integer periodo, Integer grado, Character literal, short carrera){
        try{
            List<EstudiantesPye> estudiantes = em.createQuery("select e from EstudiantesPye e where concat(e.aPaterno, e.aMaterno, e.nombre, e.matricula) like concat('%',:pista,'%') AND e.periodo = :periodo AND e.grado = :grado AND e.grupo = :literal AND e.carrera = :carrera", EstudiantesPye.class)
                    .setParameter("pista", pista)
                    .setParameter("periodo", periodo)
                    .setParameter("grado", grado)
                    .setParameter("literal", literal)
                    .setParameter("carrera", carrera)
                    .getResultList();
            
            List<DtoEstudianteComplete> listaDtoEstudiantes = new ArrayList<>();
            
            estudiantes.forEach(estudiante -> {
                String datosComplete = estudiante.getAPaterno() +" "+ estudiante.getAMaterno() +" "+ estudiante.getNombre() + " - " + estudiante.getMatricula();
                DtoEstudianteComplete dtoEstudianteComplete = new DtoEstudianteComplete(estudiante, datosComplete);
                listaDtoEstudiantes.add(dtoEstudianteComplete);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de estudiantes activos. (EjbRegistroAsesoriaTutoria.buscarEstudiante)", e, null);
        }
    }
    
    public ResultadoEJB<List<TutoriasGrupales>> buscaTutoriasGrupalesPorSesionEventoRegistro(SesionesGrupalesTutorias sesionGrupal, EventosRegistros eventoRegistro){
        try {
            List<TutoriasGrupales> listaTutoriasGrupales = new ArrayList<>();
            listaTutoriasGrupales = em.createQuery("SELECT tg FROM TutoriasGrupales tg WHERE tg.sesionGrupal.sesionGrupal = :sesionGrupal AND tg.eventoRegistro = :eventoRegistro", TutoriasGrupales.class)
                    .setParameter("sesionGrupal", sesionGrupal.getSesionGrupal())
                    .setParameter("eventoRegistro", eventoRegistro.getEventoRegistro())
                    .getResultList();
            if(listaTutoriasGrupales.isEmpty()){
                return ResultadoEJB.crearErroneo(2, null,"No se han registrado previamente tutorias grupales");
            }else{
                return ResultadoEJB.crearCorrecto(listaTutoriasGrupales, "Lista de Tutorias Grupales por Sesion Grupal y Evento de Registro");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tutorias grupales por sesión y evento de registro (EjbRegistroAsesoriaTutoria.buscaTutoriasGrupalesPorSesionEventoRegistro)", e, null);
        }
    }
    
    public ResultadoEJB<List<ParticipantesTutoriaGrupal>> buscaParticipanteTutoriaGrupal(Integer tutoriaGrupal, Integer estudiante){
        try {
            List<ParticipantesTutoriaGrupal> listaPartTG = new ArrayList<>();
            listaPartTG = em.createQuery("SELECT p FROM ParticipantesTutoriaGrupal p WHERE p.participantesTutoriaGrupalPK.tutoriaGrupal = :tutoriaGrupal AND p.participantesTutoriaGrupalPK.estudiante = :estudiante", ParticipantesTutoriaGrupal.class)
                    .setParameter("tutoriaGrupal", tutoriaGrupal)
                    .setParameter("estudiante", estudiante)
                    .getResultList();
            if (listaPartTG.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, null, "No se ha registrado previamente a este paticipante en la tutoria grupal");
            } else {
                return ResultadoEJB.crearCorrecto(listaPartTG, "Estudiante encontrado como participante");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la consulta del participante solicitado (EjbRegistroAsesoriaTutoria.buscaParticipanteTutoriaGrupal)", e, null);
        }
    }
    
    public ResultadoEJB<ParticipantesTutoriaGrupal> guardaParticipanteTutoriaGrupal(ParticipantesTutoriaGrupal participante){
        try {
            ParticipantesTutoriaGrupal ptg = new ParticipantesTutoriaGrupal();
            ptg = participante;
            ResultadoEJB<List<ParticipantesTutoriaGrupal>> res = buscaParticipanteTutoriaGrupal(ptg.getParticipantesTutoriaGrupalPK().getTutoriaGrupal(),ptg.getParticipantesTutoriaGrupalPK().getEstudiante());
            if(!res.getCorrecto()){
                em.persist(ptg);
                em.flush();
                return ResultadoEJB.crearCorrecto(ptg, "El participante ha sido guardado correctamente");
            }else{
                em.remove(res.getValor().get(0));
                em.flush();
                return ResultadoEJB.crearCorrecto(ptg, "El participante ha sido removido correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la participación del estudiante en la tutoria grupal", e, ParticipantesTutoriaGrupal.class);
        }
    }
    
    
    public ResultadoEJB<ParticipantesTutoriaGrupal> editaParticipanteTutoriaGrupal(ParticipantesTutoriaGrupal participante){
        try {
            ParticipantesTutoriaGrupal ptg = new ParticipantesTutoriaGrupal();
            ptg = participante;
            if(!(buscaParticipanteTutoriaGrupal(ptg.getParticipantesTutoriaGrupalPK().getTutoriaGrupal(),ptg.getParticipantesTutoriaGrupalPK().getEstudiante()).getCorrecto())){
                return ResultadoEJB.crearErroneo(2, "El participante necesita ser guardado previamente", ParticipantesTutoriaGrupal.class);
            }else{
                em.merge(ptg);
                return ResultadoEJB.crearCorrecto(ptg, "El participante ha sido actualizado correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la participación del estudiante en la tutoria grupal", e, ParticipantesTutoriaGrupal.class);
        }
    }
    
    public ResultadoEJB<Boolean> eliminaParticipante(ParticipantesTutoriaGrupal participante){
        try {
            ParticipantesTutoriaGrupal ptg = new ParticipantesTutoriaGrupal();
            ptg = participante;
            if(!(buscaParticipanteTutoriaGrupal(ptg.getParticipantesTutoriaGrupalPK().getTutoriaGrupal(),ptg.getParticipantesTutoriaGrupalPK().getEstudiante()).getCorrecto())){
                return ResultadoEJB.crearErroneo(2, Boolean.FALSE,"El participante no se ha encontrado, debido a esto no es posible eliminar");
            }else{
                em.remove(ptg);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El participante ha sido removido correctamente");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido eliminar la participación del estudiante en la tutoria grupal", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoParticipantesTutoriaGrupalCE>> obtenerListaEstudiantesTutoriaGrupal(Grupo grupo, TutoriasGrupales tutoriaGrupalIngresada){
        try {
            TutoriasGrupales tutoriaGrupal = buscaTutoriaGrupal(tutoriaGrupalIngresada).getValor();
            List<DtoParticipantesTutoriaGrupalCE> lista = new ArrayList<>();

            List<Estudiante> estudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.grupo.idGrupo = :grupo AND e.tipoEstudiante.idTipoEstudiante = 1 ORDER BY e.aspirante.idPersona.apellidoPaterno, e.aspirante.idPersona.apellidoMaterno, e.aspirante.idPersona.nombre", Estudiante.class)
                    .setParameter("grupo", grupo.getIdGrupo())
                    .getResultList();
            if (tutoriaGrupal.getParticipantesTutoriaGrupalList().isEmpty()) {
                lista = empaquetarDTOParticipanteGuardar(estudiantes, tutoriaGrupal);
            } else {
                lista = empaquetarDTOParticipante(estudiantes, tutoriaGrupal);
            }
            
            return ResultadoEJB.crearCorrecto(lista, "Lista de estudiantes del grupo seleccionado");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes del grupo seleccionado", e, null);
        }
    }
    
    public List<DtoParticipantesTutoriaGrupalCE> empaquetarDTOParticipanteGuardar(List<Estudiante> estudiantes, TutoriasGrupales tutoriaGrupal) {
        List<DtoParticipantesTutoriaGrupalCE> lista = new ArrayList<>();
        estudiantes.forEach((e) -> {
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, e.getCarrera());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, e.getPeriodo());
            DtoDatosEstudiante dto = new DtoDatosEstudiante(e, programaEducativo, periodoEscolar);
            DtoParticipantesTutoriaGrupalCE dtoPTG;
            ResultadoEJB<ParticipantesTutoriaGrupal> resGuardar = guardaParticipanteTutoriaGrupal(new ParticipantesTutoriaGrupal(new ParticipantesTutoriaGrupalPK(tutoriaGrupal.getTutoriaGrupal(), e.getIdEstudiante()), false, "Pendiente de registro"));
            if (resGuardar.getCorrecto()) {
                dtoPTG = new DtoParticipantesTutoriaGrupalCE(dto, resGuardar.getValor(), true);
                lista.add(dtoPTG);
            }
        });
        return lista;
    }
    
    public List<DtoParticipantesTutoriaGrupalCE> empaquetarDTOParticipante(List<Estudiante> estudiantes, TutoriasGrupales tutoriaGrupal) {
        List<DtoParticipantesTutoriaGrupalCE> lista = new ArrayList<>();
        estudiantes.forEach((e) -> {
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, e.getCarrera());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, e.getPeriodo());
            DtoDatosEstudiante dto = new DtoDatosEstudiante(e, programaEducativo, periodoEscolar);
            DtoParticipantesTutoriaGrupalCE dtoPTG;
            ResultadoEJB<List<ParticipantesTutoriaGrupal>> res = buscaParticipanteTutoriaGrupal(tutoriaGrupal.getTutoriaGrupal(), e.getIdEstudiante());
            if (res.getCorrecto()) {
                dtoPTG = new DtoParticipantesTutoriaGrupalCE(dto, res.getValor().get(0), true);
                lista.add(dtoPTG);
            } else {
                ParticipantesTutoriaGrupalPK pk = new ParticipantesTutoriaGrupalPK(tutoriaGrupal.getTutoriaGrupal(), e.getIdEstudiante());
                ParticipantesTutoriaGrupal partTG = new ParticipantesTutoriaGrupal(pk, true, "Pendiente de registro");
                partTG.setEstadoCasoCritico(Boolean.FALSE);
                dtoPTG = new DtoParticipantesTutoriaGrupalCE(dto, partTG, false);
                lista.add(dtoPTG);
            }
        });
        return lista;
    }
    
    public ResultadoEJB<TutoriasIndividuales> guardaTutoriasIndividuales(TutoriasIndividuales tutoriaIndividual, DtoCasoCritico dtoCasoCritico) {
        try {
            TutoriasIndividuales ti = new TutoriasIndividuales();
            ti.setEstudiante(tutoriaIndividual.getEstudiante());
            ti.setFecha(tutoriaIndividual.getFecha());
            ti.setHoraInicio(tutoriaIndividual.getHoraInicio());
            ti.setTiempoInvertido(tutoriaIndividual.getTiempoInvertido());
            ti.setTipoTiempo(tutoriaIndividual.getTipoTiempo());
            ti.setMotivo(tutoriaIndividual.getMotivo());
            ti.setAccionesObservaciones(tutoriaIndividual.getAccionesObservaciones());
            ti.setEventoRegistro(tutoriaIndividual.getEventoRegistro());
            ti.setSesionGrupal(tutoriaIndividual.getSesionGrupal());
            if (tutoriaIndividual.getCasoCritico() == null) {
                ti.setCasoCritico(null);
                em.persist(ti);
                return ResultadoEJB.crearCorrecto(ti, "El registro de la tutoria individual se ha guardado correctamente");
            } else {
//                TODO: Codigo compatible con registro de caso crítico y liberación del mismo (Pendiente, verificar funcionamiento del modulo de caso crítico)
                dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.EN_SEGUMIENTO_TUTOR.getLabel());
                ResultadoEJB<DtoCasoCritico> res = cc.actualizarCasoCritico(dtoCasoCritico);
                if(res.getCorrecto()){
                    ti.setCasoCritico(tutoriaIndividual.getCasoCritico());
                    em.persist(tutoriaIndividual);
                    return ResultadoEJB.crearCorrecto(tutoriaIndividual, "El regisro de la tutoría individual relacionada con el caso crítico se ha guardado correctamente");
                }
                else return ResultadoEJB.crearErroneo(2, "La tutoria individual no se ha podido guardar debido a que el caso critico ya se encuentra cerrado", TutoriasIndividuales.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido guardar la tutoria individual del estudiante", e, TutoriasIndividuales.class);
        }
    }
    
    public ResultadoEJB<TutoriasIndividuales> editaTutoriasIndividuales(TutoriasIndividuales tutoriaIndividual, DtoCasoCritico dtoCasoCritico) {
        try {
            TutoriasIndividuales ti = new TutoriasIndividuales();
            ti.setEstudiante(tutoriaIndividual.getEstudiante());
            ti.setFecha(tutoriaIndividual.getFecha());
            ti.setHoraInicio(tutoriaIndividual.getHoraInicio());
            ti.setTiempoInvertido(tutoriaIndividual.getTiempoInvertido());
            ti.setTipoTiempo(tutoriaIndividual.getTipoTiempo());
            ti.setMotivo(tutoriaIndividual.getMotivo());
            ti.setAccionesObservaciones(tutoriaIndividual.getAccionesObservaciones());
            ti.setEventoRegistro(tutoriaIndividual.getEventoRegistro());
            ti.setSesionGrupal(tutoriaIndividual.getSesionGrupal());
            if (tutoriaIndividual.getCasoCritico() == null) {
                ti.setCasoCritico(null);
                em.merge(ti);
                return ResultadoEJB.crearCorrecto(ti, "El registro de la tutoria individual se ha actualizado correctamente");
            } else {
//                TODO: Codigo compatible con registro de caso crítico y liberación del mismo (Pendiente, verificar funcionamiento del modulo de caso crítico)
                if(!dtoCasoCritico.getCasoCritico().getEstado().equals(CasoCriticoEstado.EN_SEGUIMIENTO_ESPECIALISTA.getLabel())){
                    dtoCasoCritico.getCasoCritico().setEstado(CasoCriticoEstado.EN_SEGUMIENTO_TUTOR.getLabel());
                }
                ResultadoEJB<DtoCasoCritico> res = cc.actualizarCasoCritico(dtoCasoCritico);
                if(res.getCorrecto()){
                    ti.setCasoCritico(tutoriaIndividual.getCasoCritico());
                    em.merge(tutoriaIndividual);
                    return ResultadoEJB.crearCorrecto(tutoriaIndividual, "El regisro de la tutoría individual relacionada con el caso crítico se ha actualizado correctamente");
                }
                else return ResultadoEJB.crearErroneo(2, "La tutoria individual no se ha podido actualizar debido a que el caso critico ya se encuentra cerrado", TutoriasIndividuales.class);
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido actualizar la tutoria individual del estudiante", e, TutoriasIndividuales.class);
        }
    }
    
    public ResultadoEJB<Boolean> eliminaTutoriaIndividual(TutoriasIndividuales tutoriaIndividual, DtoCasoCritico dtoCC) {
        try {
            if(tutoriaIndividual.getCasoCritico() == null){
                TutoriasIndividuales ti = em.find(TutoriasIndividuales.class, tutoriaIndividual.getTutoriaIndividual());
                em.remove(ti);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La tutoría individual se ha eliminado del sistema");
            }else{
                System.err.println("Caso Critico: " + dtoCC.getCasoCritico().getCaso());
                if(dtoCC.getEstado().getNivel() < CasoCriticoEstado.EN_SEGUIMIENTO_ESPECIALISTA.getNivel() && dtoCC.getEstado().getNivel() < CasoCriticoEstado.EN_SEGUMIENTO_TUTOR.getNivel()){
                    TutoriasIndividuales ti = em.find(TutoriasIndividuales.class, tutoriaIndividual.getTutoriaIndividual());
                    eliminarEvidenciaCasoCritico(dtoCC);
                    dtoCC.getCasoCritico().setComentariosTutor(null);
                    cc.actualizarCasoCritico(dtoCC);
                    em.remove(ti);
                    return ResultadoEJB.crearCorrecto(Boolean.TRUE, "La tutoría individual se ha elimiando del sistema");
                }else{
                    return ResultadoEJB.crearErroneo(2, "No se ha podido eliminar la tutoría individual debido a que el caso critico se encuentra en seguimiento", Boolean.TYPE);
                }
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la tutoría individual (EjbRegistroAsesoriaTutoria.eliminaTutoriaIndividual).", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<Boolean> liberarCasoCritico(TutoriasIndividuales tutoriaIndividual, DtoCasoCritico dtoCC) {
        try {
            if(dtoCC.getCasoCritico().getEstado().equals(CasoCriticoEstado.REGISTRADO.getLabel())){    
                return ResultadoEJB.crearErroneo(3, "No se ha podido liberar el caso critico debido a que aún no se le ha dado seguimiento", Boolean.TYPE);
            }   
            dtoCC.getCasoCritico().setEstado(CasoCriticoEstado.CERRADO_TUTOR.getLabel());
            dtoCC.getCasoCritico().setFechaCierre(new Date());
            ResultadoEJB<DtoCasoCritico> res = cc.actualizarCasoCritico(dtoCC);
            if(res.getCorrecto()){
                em.merge(tutoriaIndividual);
                return ResultadoEJB.crearCorrecto(Boolean.TRUE, "El caso crítico del estudiante se ha liberado correctamente");
            }
            else return ResultadoEJB.crearErroneo(2, "No se pudo cerrar el caso crítico del estudiante, debido a una validacion del caso crítico", Boolean.TYPE);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo liberar el caso crítico (EjbRegistroAsesoriaTutoria.liberarCasoCritico).", e, Boolean.TYPE);
        }
    }
    
    public ResultadoEJB<List<DtoTutoriaIndividualCE>> buscaTutoriasIndividuales(SesionesGrupalesTutorias sesionGrupal, EventosRegistros eventoRegistro) {
        try {
            List<TutoriasIndividuales> listaTutoriasIndividuales = new ArrayList<>();
            listaTutoriasIndividuales = em.createQuery("SELECT ti FROM TutoriasIndividuales ti WHERE ti.sesionGrupal.sesionGrupal = :sesionGrupal AND ti.eventoRegistro = :eventoRegistro", TutoriasIndividuales.class)
                    .setParameter("sesionGrupal", sesionGrupal.getSesionGrupal())
                    .setParameter("eventoRegistro", eventoRegistro.getEventoRegistro())
                    .getResultList();
            if (listaTutoriasIndividuales.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, null, "No se han registrado previamente tutorias individuales");
            } else {
                List<DtoTutoriaIndividualCE> listaDto = new ArrayList<>();
                listaTutoriasIndividuales.stream().forEach((ti) -> {
                   if(ti.getCasoCritico() == null){
                       DtoTutoriaIndividualCE dto = new DtoTutoriaIndividualCE(
                               ti
                               ,pack.packEstudiante(ti.getEstudiante()).getValor()
                       );
                       listaDto.add(dto);
                   }else{
                       DtoTutoriaIndividualCE dto = new DtoTutoriaIndividualCE(
                               ti
                               ,pack.packEstudiante(ti.getEstudiante()).getValor()
                               ,cc.identificarPorEsdudianteCaso(ti.getEstudiante(), ti.getCasoCritico().getCaso()).getValor().get(0)
                       );
                       listaDto.add(dto);
                   }
                });
                return ResultadoEJB.crearCorrecto(listaDto, "Lista de tutorias individuales de la sesión y evento seleccionado");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tutorias individuales por sesión y evento de registro (EjbRegistroAsesoriaTutoria.buscaTutoriasIndividuales)", e, null);
        }
    }
    
    public Map.Entry<Boolean, Integer> registrarEvidenciaCasoCritico(DtoCasoCritico dtoCasoCritico, Part archivo) throws Throwable {
        Map<Boolean, Integer> map = new HashMap<>();
        if (dtoCasoCritico == null || archivo == null) {
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        if (dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_ESPECIALISTA.getNivel() && dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_TUTOR.getNivel()) {
            final List<Boolean> res = new ArrayList<>();

            try {
                String rutaAbsoluta = ServicioArchivos.almacenarArchivoEvidenciaTutorCasoCritico(dtoCasoCritico, archivo);
                dtoCasoCritico.getCasoCritico().setEvidenciaTutor(rutaAbsoluta);
                res.add(true);
            } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
            }

            Long correctos = res.stream().filter(r -> r).count();
            Long incorrectos = res.stream().filter(r -> !r).count();

            if (correctos == 0) {
                dtoCasoCritico.getCasoCritico().setEvidenciaTutor(null);
                em.merge(dtoCasoCritico.getCasoCritico());
            } else {
                em.merge(dtoCasoCritico.getCasoCritico());
            }

            map.put(incorrectos == 0, correctos.intValue());
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro(2)");
            return map.entrySet().iterator().next();
        } else {
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
    }
    
    public Map.Entry<Boolean, Integer> registrarEvidenciaEspecialistaCasoCritico(DtoCasoCritico dtoCasoCritico, Part archivo) throws Throwable {
        Map<Boolean, Integer> map = new HashMap<>();
        if (dtoCasoCritico == null || archivo == null) {
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
        if (dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_ESPECIALISTA.getNivel() && dtoCasoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_TUTOR.getNivel()) {
            final List<Boolean> res = new ArrayList<>();

            try {
                String rutaAbsoluta = ServicioArchivos.almacenarArchivoEvidenciaEspecialistaCasoCritico(dtoCasoCritico, archivo);
                dtoCasoCritico.getCasoCritico().setEvidenciaEspecialista(rutaAbsoluta);
                res.add(true);
            } catch (IOException | EvidenciaRegistroExtensionNoValidaException e) {
                res.add(Boolean.FALSE);
                LOG.log(Level.SEVERE, "No se guardó el archivo: " + archivo.getSubmittedFileName(), e);
            }

            Long correctos = res.stream().filter(r -> r).count();
            Long incorrectos = res.stream().filter(r -> !r).count();

            if (correctos == 0) {
                dtoCasoCritico.getCasoCritico().setEvidenciaEspecialista(null);
                em.merge(dtoCasoCritico.getCasoCritico());
            } else {
                em.merge(dtoCasoCritico.getCasoCritico());
            }

            map.put(incorrectos == 0, correctos.intValue());
//        System.out.println("mx.edu.utxj.pye.siip.services.ca.ServiciosAsesoriasTutoriasCiclosPeriodos.registrarEvidenciasARegistro(2)");
            return map.entrySet().iterator().next();
        } else {
            map.put(Boolean.FALSE, 0);
            return map.entrySet().iterator().next();
        }
    }
    
    private static final Logger LOG = Logger.getLogger(EjbRegistroAsesoriaTutoria.class.getName());
    
    public Boolean eliminarEvidenciaCasoCritico(DtoCasoCritico casoCritico) {
        if (casoCritico == null) {
            return false;
        }
        if (casoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_ESPECIALISTA.getNivel() && casoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_TUTOR.getNivel()) {
            try {
                ServicioArchivos.eliminarArchivo(casoCritico.getCasoCritico().getEvidenciaTutor());
                casoCritico.getCasoCritico().setEvidenciaTutor(null);
                em.merge(casoCritico.getCasoCritico());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + casoCritico.getCasoCritico().getEvidenciaTutor(), e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    public Boolean eliminarEvidenciaCasoCriticoEspecialista(DtoCasoCritico casoCritico) {
        if (casoCritico == null) {
            return false;
        }
        if (casoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_ESPECIALISTA.getNivel() && casoCritico.getEstado().getNivel() > CasoCriticoEstado.CERRADO_TUTOR.getNivel()) {
            try {
                ServicioArchivos.eliminarArchivo(casoCritico.getCasoCritico().getEvidenciaEspecialista());
                casoCritico.getCasoCritico().setEvidenciaEspecialista(null);
                em.merge(casoCritico.getCasoCritico());
            } catch (Exception e) {
                LOG.log(Level.SEVERE, "No se eliminó la evidencia: " + casoCritico.getCasoCritico().getEvidenciaEspecialista(), e);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
    public ResultadoEJB<List<PersonalActivo>> buscarDocente(String pista){
        try {
            return ejbAsignacionAcademica.buscarDocente(pista);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de docentes activos. (EjbRegistroAsesoriaTutoria.buscarDocente)", e, null);
        }
    }
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConCasosCriticoRegistrados(PersonalActivo especialista) {
        return ejbValidadorDocente.getPeriodosConCasosCriticoRegistrados(especialista);
    }
    
    public ResultadoEJB<Filter<PersonalActivo>> validarUsuarioEspecialista(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ESPECIALISTA.getLabel(), String.valueOf(ep.leerPropiedad("especialistaCasoCritico")));
            if(getPeriodosConCasosCriticoRegistrados(p).getCorrecto()) return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal especialista");
            else return ResultadoEJB.crearErroneo(2, null,"El usuario no cuenta con casos críticos asignados");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal del área no se pudo validar. (EjbRegistroAsesoriaTutoria.validarUsuarioEspecialista)", e, null);
        }
    }
    
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosCasosCriticosEspecialista(PeriodosEscolares periodoSeleccionado, PersonalActivo especialista){
        try {
            List<AreasUniversidad> areasUniversidad = em.createQuery("SELECT e.grupo.idPe FROM CasoCritico cc INNER JOIN cc.idEstudiante e WHERE cc.especialista = :especialista AND e.grupo.periodo = :periodo", Short.class)
                    .setParameter("especialista", especialista.getPersonal().getClave())
                    .setParameter("periodo", periodoSeleccionado.getPeriodo())
                    .getResultStream()
                    .map(idArea -> em.find(AreasUniversidad.class, idArea))
                    .distinct()
                    .sorted(Comparator.comparingInt(AreasUniversidad::getArea).reversed())
                    .collect(Collectors.toList());
            if(areasUniversidad.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han encontrados programas educativos que contengas casos críticos registados");
            }else return ResultadoEJB.crearCorrecto(areasUniversidad, "Lista de programas educativos con casos críticos registrados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido obtener la lista de programas educativos que tienen registro de caso críticos.(EjbRegistroAsesoriaTutoria.getProgramasEducativosCasosCriticosEspecialista)", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoCasoCritico>> getCasosCriticosParaEspecialista(PeriodosEscolares periodoSeleccionado, AreasUniversidad programaEducativo, PersonalActivo especialista){
        try {
            List<DtoCasoCritico> casosCriticos = em.createQuery("SELECT cc FROM CasoCritico cc INNER JOIN cc.idEstudiante e WHERE e.grupo.periodo = :periodo AND e.grupo.idPe = :idPE AND cc.especialista = :especialista", CasoCritico.class)
                    .setParameter("periodo", periodoSeleccionado.getPeriodo())
                    .setParameter("idPE", programaEducativo.getArea())
                    .setParameter("especialista", especialista.getPersonal().getClave())
                    .getResultStream()
                    .map(casoCritico -> (cc.identificarPorEsdudianteCaso(casoCritico.getIdEstudiante(), casoCritico.getCaso())).getValor().get(0))
                    .collect(Collectors.toList());
            if(casosCriticos.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han encontrados casos críticos registados");
            }else return ResultadoEJB.crearCorrecto(casosCriticos, "Listado de casos críticos canalizados");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se ha podido obtener la lista de programas educativos que tienen registro de caso críticos.(EjbRegistroAsesoriaTutoria.getCasosCriticosParaEspecialista)", e, null);
        }
    }
    
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosConPlanAccionTutorial(PersonalActivo director) {
        try {
            List<AreasUniversidad> areaSuperior = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.responsable = :director", AreasUniversidad.class)
                    .setParameter("director", director.getPersonal().getClave())
                    .getResultList();
            if(areaSuperior.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "El usuario logueado no tiene ninguna área a su cargo");
            }
            List<Short> programasEducativos = em.createQuery("SELECT a.area FROM AreasUniversidad a WHERE a.areaSuperior = :areaSuperior", Short.class)
                    .setParameter("areaSuperior", areaSuperior.get(0).getArea())
                    .getResultList();
            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "El área superior no tiene asignado ninguna programa educativo");
            }
            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT g.periodo FROM PlanAccionTutorial p INNER JOIN p.grupo g WHERE g.idPe IN :programasEducativos", Integer.class)
                    .setParameter("programasEducativos", programasEducativos)
                    .getResultStream()
                    .map(periodo -> em.find(PeriodosEscolares.class, periodo))
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());

            if(periodosEscolares.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "Los grupos aún no contienen planes de accion tutorial registrados");
            }else{
                return ResultadoEJB.crearCorrecto(periodosEscolares, "Lista de periodos escolares registrados con plan de acción tutorial");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares con planes de acción tutorial (EjbRegistroAsesoriaTutoria.getPeriodosConPlanAccionTutorial).", e, null);
        }
    }
    
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosConPlanAccionTutorial(PersonalActivo director, PeriodosEscolares periodoEscolar) {
        try {
            List<AreasUniversidad> areaSuperior = em.createQuery("SELECT a FROM AreasUniversidad a WHERE a.responsable = :director", AreasUniversidad.class)
                    .setParameter("director", director.getPersonal().getClave())
                    .getResultList();
            if(areaSuperior.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST,"El usuario logueado no tiene ninguna área a su cargo");
            }
            List<Short> programasEducativos = em.createQuery("SELECT a.area FROM AreasUniversidad a WHERE a.areaSuperior = :areaSuperior", Short.class)
                    .setParameter("areaSuperior", areaSuperior.get(0).getArea())
                    .getResultList();
            if(programasEducativos.isEmpty()){
                return ResultadoEJB.crearErroneo(3, Collections.EMPTY_LIST, "El área superior no tiene asignado ninguna programa educativo");
            }
            List<AreasUniversidad> programasEducativosPAT = em.createQuery("SELECT g.idPe FROM PlanAccionTutorial p INNER JOIN p.grupo g WHERE g.idPe IN :programasEducativos AND g.periodo = :periodo", Short.class)
                    .setParameter("programasEducativos", programasEducativos)
                    .setParameter("periodo", periodoEscolar.getPeriodo())
                    .getResultStream()
                    .map(pe -> em.find(AreasUniversidad.class, pe))
                    .distinct()
                    .collect(Collectors.toList());
            if(programasEducativosPAT.isEmpty()){
                return ResultadoEJB.crearErroneo(4, Collections.EMPTY_LIST, "Los grupos aún no contienen planes de accion tutorial registrados");
            }else{
                return ResultadoEJB.crearCorrecto(programasEducativosPAT, "Lista de programas educativos registrados con plan de acción tutorial");
            }      
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos con planes de acción tutorial (EjbRegistroAsesoriaTutoria.getProgramasEducativosConPlanAccionTutorial).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoPlanAccionTutorial>> getPlanesAccionTutorialPeriodoArea(PeriodosEscolares periodo, AreasUniversidad area){
        try {
            List<DtoPlanAccionTutorial> listaDto = new ArrayList<>();
            List<PlanAccionTutorial> planesAccionTutorial = em.createQuery("SELECT p FROM PlanAccionTutorial p INNER JOIN p.grupo g WHERE g.periodo = :periodo AND g.idPe = :idPe", PlanAccionTutorial.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("idPe", area.getArea())
                    .getResultList();
            if(planesAccionTutorial.isEmpty()){
                return ResultadoEJB.crearErroneo(2, Collections.EMPTY_LIST, "No se han encontrado planes de acción tutorial registrados");
            }else{
                planesAccionTutorial.stream().forEach((p) -> {
                    DtoPlanAccionTutorial dto = new DtoPlanAccionTutorial(
                            p
                            ,pack.packPersonalActivo(p.getGrupo().getTutor())
                    );
                    listaDto.add(dto);
                });
                return ResultadoEJB.crearCorrecto(listaDto, "Lista de planes de accion tutorial");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de planes de acción tutorial (EjbRegistroAsesoriaTutoria.getPlanesAccionTutorialPeriodoArea).", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoCasosCriticosPendientes>> getCasoCriticosPendientes(Grupo grupo) {
        try {
            List<DtoCasosCriticosPendientes> pendientes = new ArrayList<>();
            List<CasoCriticoEstado> estadosCerrados = Arrays.stream(CasoCriticoEstado.values())
                        .filter(casoCriticoEstado -> casoCriticoEstado.getNivel() < 0D)
                        .collect(Collectors.toList());
            List<Object[]> restultados = em.createQuery("SELECT cc.idEstudiante.grupo.idGrupo,cc.idEstudiante.grupo.grado,cc.idEstudiante.grupo.literal,cc.idEstudiante.grupo.idPe,cc.idEstudiante.grupo.periodo,COUNT(cc.caso) AS casosCriticosPorAtender,cc.tipo,cc.estado,cc.idEstudiante.grupo.tutor FROM CasoCritico cc WHERE cc.idEstudiante.grupo.idGrupo = :grupo AND cc.estado NOT IN :estadosCerrados GROUP BY cc.tipo,cc.estado ORDER BY cc.tipo, cc.estado")
                    .setParameter("grupo", grupo.getIdGrupo())
                    .setParameter("estadosCerrados", estadosCerrados)
                    .getResultList();
            restultados.stream().map((resultado) -> {
                int         idGrupo                     =       ((Number)       resultado[0]).intValue();
                int         grado                       =       ((Number)       resultado[1]).intValue();
                Character   literal                     =       (Character)     resultado[2];
                int         idPE                        =       ((Number)       resultado[3]).intValue();
                int         periodo                     =       ((Number)       resultado[4]).intValue();
                int         casosCriticosPorAtender     =       ((Number)       resultado[5]).intValue();
                String      tipo                        =       (String)        resultado[6];
                String      estado                      =       (String)        resultado[7];
                int         tutor                       =       ((Number)       resultado[8]).intValue();
                DtoCasosCriticosPendientes dto = new DtoCasosCriticosPendientes(idGrupo, grado, literal, idPE, periodo, casosCriticosPorAtender, tipo, estado, tutor);
                return dto;
            }).forEachOrdered((dto) -> {
                pendientes.add(dto);
            });
            return ResultadoEJB.crearCorrecto(pendientes, "Lista de casos críticos pendientes");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de casos críticos pendientes de seguimiento (EjbRegistroAsesoriaTutoria.getCasoCriticosPendientes).", e, null);
        }
    }
}
