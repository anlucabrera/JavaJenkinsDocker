/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.text.SimpleDateFormat;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCargaAcademica;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import java.util.*;
import javax.persistence.StoredProcedureQuery;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAperturaExtPorEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPermisoCapturaExtemporanea;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoValidarPermisoCapExt;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbPermisoAperturaExtemporanea")
public class EjbPermisoAperturaExtemporanea {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }

    /**
     * Permite crear el filtro para validar si el usuario autenticado es administrador del sistema
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarAdministrador(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalAdministrador").orElse(9)));
            return ResultadoEJB.crearCorrecto(filtro, "El filtro del usuario ha sido preparado como personal administrador del Sistema.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbPermisoAperturaExtemporanea.validarAdministrador)", e, null);
        }
    }

    /**
     * Permite identificar a una lista de posibles docentes para asignar la materia
     * @param pista Contenido que la vista que puede incluir parte del nombre, nùmero de nómina o área operativa del docente que se busca
     * @return Resultado del proceso con docentes ordenador por nombre
     */
    public ResultadoEJB<List<PersonalActivo>> buscarDocente(String pista){
        try{
            //buscar lista de docentes operativos por nombre, nùmero de nómina o área  operativa segun la pista y ordener por nombre del docente
            List<PersonalActivo> docentes = em.createQuery("select p from Personal p where p.estado <> 'B' and concat(p.nombre, p.clave) like concat('%',:pista,'%')  ", Personal.class)
                    .setParameter("pista", pista)
                    .getResultStream()
                    .map(p -> ejbPersonalBean.pack(p))
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(docentes, "Lista para mostrar en autocomplete");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo localizar la lista de docentes activos. (EjbPermisoAperturaExtemporanea.buscarDocente)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicaPorDocente(PersonalActivo docente){
        try{
            List<DtoCargaAcademica> cargas = em.createQuery("select c from CargaAcademica c inner join c.cveGrupo g inner join c.idPlanMateria pem inner join pem.idMateria mat inner join pem.idPlan plan where c.docente=:docente order by g.idPe, g.grado, g.literal, mat.nombre", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbPermisoAperturaExtemporanea.getCargaAcademicaPorDocente)", e, null);
        }
    }
    
    /**
     * Empaqueta una carga académica en su DTO Wrapper
     * @param cargaAcademica Carga académica que se va a empaquetar
     * @return Carga académica empaquetada
     */
    public ResultadoEJB<DtoCargaAcademica> pack(CargaAcademica cargaAcademica){
        try{
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una carga académica nula.", DtoCargaAcademica.class);
            if(cargaAcademica.getCarga() == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar una carga académica con clave nula.", DtoCargaAcademica.class);

            CargaAcademica cargaAcademicaBD = em.find(CargaAcademica.class, cargaAcademica.getCarga());
            if(cargaAcademicaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar una carga académica no registrada previamente en base de datos.", DtoCargaAcademica.class);

            Grupo grupo = em.find(Grupo.class, cargaAcademicaBD.getCveGrupo().getIdGrupo());
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, grupo.getPeriodo());
            PlanEstudioMateria planEstudioMateria = cargaAcademicaBD.getIdPlanMateria();
            PlanEstudio planEstudio = planEstudioMateria.getIdPlan();
            Materia materia = em.find(Materia.class, planEstudioMateria.getIdMateria().getIdMateria());
            PersonalActivo docente = ejbPersonalBean.pack(cargaAcademicaBD.getDocente());
            AreasUniversidad programa = em.find(AreasUniversidad.class, planEstudio.getIdPe());
            DtoCargaAcademica dto = new DtoCargaAcademica(cargaAcademicaBD, periodo, docente, grupo, materia, programa, planEstudio, planEstudioMateria);

            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbPermisoAperturaExtemporanea.pack).", e, DtoCargaAcademica.class);
        }
    }
    
    /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    }
    
     /**
     * Permite obtener la lista de periodos en los que el docente seleccionada tiene carga académica asignada
     * @param cargas Cargas Académicas del docente
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosCargas(List<DtoCargaAcademica> cargas){
        try{
            List<PeriodosEscolares> periodos = new ArrayList<>();
            cargas.forEach(carga -> {
                EventoEscolar evento = em.createQuery("SELECT e FROM CargaAcademica c JOIN c.evento e WHERE c.carga =:carga", EventoEscolar.class)
                .setParameter("carga", carga.getCargaAcademica().getCarga())
                .getSingleResult();
                
                if (evento != null) {
                    PeriodosEscolares periodo = em.find(PeriodosEscolares.class, evento.getPeriodo());
                    periodos.add(periodo);
                } 
                
            });
            List<PeriodosEscolares> listaPeriodos = periodos.stream()
                    .distinct()
                    .sorted(Comparator.comparingInt(PeriodosEscolares::getPeriodo).reversed())
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaPeriodos, "Lista de periodo escolares en los que el docente tiene cargas académicas asignadas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos en los que el docente tiene cargas académicas. (EjbPermisoAperturaExtemporanea.getPeriodosCargas)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo Periodo del que se quiere obtener la lista
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicaPorPeriodo(PersonalActivo docente, PeriodosEscolares periodo){
        try{
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT c FROM CargaAcademica c INNER JOIN c.cveGrupo g INNER JOIN c.idPlanMateria pem INNER JOIN pem.idMateria mat INNER JOIN pem.idPlan plan INNER JOIN c.evento e WHERE c.docente=:docente and e.periodo=:periodo ORDER BY g.idPe, g.grado, g.literal, mat.nombre", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbPermisoAperturaExtemporanea.getCargaAcademicaPorPeriodo)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de tipos de evaluación activas para solicitar permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getTiposEvaluaciones(){
        try{
            List<String> tiposEvaluaciones = Stream.of("Ordinaria", "Nivelación Final").collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(tiposEvaluaciones, "Lista de tipos de evaluaciones.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos de evaluaciones. (EjbPermisoAperturaExtemporanea.getTiposEvaluaciones)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de unidades que contiene la materia seleccionada
     * @param cargaAcademica Carga Académica seleccionada para registrar permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateria>> getUnidadesMateria(DtoCargaAcademica cargaAcademica){
        try{
            Materia materia = em.createQuery("SELECT m FROM CargaAcademica c INNER JOIN c.idPlanMateria pm INNER JOIN pm.idMateria m WHERE c.carga =:carga", Materia.class)
                    .setParameter("carga", cargaAcademica.getCargaAcademica().getCarga())
                    .getSingleResult();
            
            List<UnidadMateria> unidadesMateria = em.createQuery("SELECT u FROM UnidadMateria u WHERE u.idMateria.idMateria =:materia", UnidadMateria.class)
                    .setParameter("materia", materia.getIdMateria())
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(unidadesMateria, "Lista de unidades de la materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de unidades de la materia. (EjbPermisoAperturaExtemporanea.getUnidadesMateria)", e, null);
        }
    }
    
     /**
     * Permite obtener el rango de fechas dependiendo de la fecha de inicio y fin de la configuración de la unidad
     * @param cargaAcademica Carga Académica seleccionada para registrar permiso
     * @param unidadMateria Unidad materia seleccionada para registrar permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoRangoFechasPermiso> getRangoFechasPermisoOrdinarias(DtoCargaAcademica cargaAcademica, UnidadMateria unidadMateria){
        try{
            UnidadMateriaConfiguracion unidadMateriaConfiguracion = em.createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:carga AND umc.idUnidadMateria.idUnidadMateria =:unidadMateria", UnidadMateriaConfiguracion.class)
                    .setParameter("carga", cargaAcademica.getCargaAcademica().getCarga())
                    .setParameter("unidadMateria", unidadMateria.getIdUnidadMateria())
                    .getSingleResult();
            
            Calendar fechaInicio = Calendar.getInstance();
            fechaInicio.setTime(unidadMateriaConfiguracion.getFechaFin());
            fechaInicio.add(Calendar.DAY_OF_YEAR, 7);
            Date rangoFechaInicio=fechaInicio.getTime();
            
            PeriodoEscolarFechas fechasPeriodos = em.createQuery("SELECT pef FROM PeriodoEscolarFechas pef WHERE pef.periodosEscolares.periodo =:periodo", PeriodoEscolarFechas.class)
                    .setParameter("periodo", unidadMateriaConfiguracion.getCarga().getEvento().getPeriodo())
                    .getSingleResult();
           
            Date rangoFechaFin = fechasPeriodos.getFin();
            
            if(rangoFechaInicio.compareTo(rangoFechaFin) > 0){
                Calendar fecha = Calendar.getInstance();
                fecha.setTime(unidadMateriaConfiguracion.getFechaFin());
                rangoFechaInicio=fecha.getTime();
                
            }
            
            DtoRangoFechasPermiso dtoRangoFechasPermiso = new DtoRangoFechasPermiso(rangoFechaInicio, rangoFechaFin);
            
            return ResultadoEJB.crearCorrecto(dtoRangoFechasPermiso, "Rango de fechas obtenidas para la unidad seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el rango de fechas de la unidad seleccionada. (EjbPermisoAperturaExtemporanea.getRangoFechasPermisoOrdinarias)", e, null);
        }
    }
    
     /**
     * Permite obtener el rango de fechas para nivelación final
     * @param cargaAcademica Carga Académica seleccionada para registrar permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoRangoFechasPermiso> getRangoFechasPermisoNivFinal(DtoCargaAcademica cargaAcademica){
        try{
            PeriodoEscolarFechas fechasPeriodos = em.createQuery("SELECT pef FROM PeriodoEscolarFechas pef WHERE pef.periodosEscolares.periodo =:periodo", PeriodoEscolarFechas.class)
                    .setParameter("periodo", cargaAcademica.getCargaAcademica().getEvento().getPeriodo())
                    .getSingleResult();
           
            Calendar fechaInicio = Calendar.getInstance();
            fechaInicio.setTime(fechasPeriodos.getFin());
            fechaInicio.add(Calendar.DAY_OF_YEAR, -5);
            Date rangoFechaInicio=fechaInicio.getTime();
            
            Date rangoFechaFin = fechasPeriodos.getFin();
            
            DtoRangoFechasPermiso dtoRangoFechasPermiso = new DtoRangoFechasPermiso(rangoFechaInicio, rangoFechaFin);
            
            return ResultadoEJB.crearCorrecto(dtoRangoFechasPermiso, "Rango de fechas obtenidas para nivelación final.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el rango de fechas para nivelación final. (EjbPermisoAperturaExtemporanea.getRangoFechasPermisoNivFinal)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de justificaciones activas para solicitar el permiso de captura extemporánea
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<JustificacionPermisosExtemporaneos>> getJustificacionesPermisoCaptura(){
        try{
            List<JustificacionPermisosExtemporaneos> justificacionPermisosExtemporaneoses = em.createQuery("SELECT j FROM JustificacionPermisosExtemporaneos j WHERE j.activo =:activo", JustificacionPermisosExtemporaneos.class)
                    .setParameter("activo", true)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(justificacionPermisosExtemporaneoses, "Lista de justificaciones para permiso de apertura extemporánea.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de justificaciones para permiso de apertura extemporánea. (EjbPermisoAperturaExtemporanea.getJustificacionesPermisoCaptura)", e, null);
        }
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea ordinaria
     * @param cargaAcademica Carga Académica para obtener parametros como: periodo, grupo, plan materia y docente.
     * @param unidadMateria Unidad materia que se registrará con el permiso
     * @param tipoEvaluacion Tipo de evaluación a registrar en este caso Nivelación Final
     * @param fechaInicio Fecha inicio para el permiso de captura
     * @param fechaFin Fecha fin para el permiso de captura
     * @param justificacion Justificación por la que el docente solicita el permiso
     * @param administrador Personal administrador que registra el permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<PermisosCapturaExtemporaneaGrupal> guardarPermisoCapturaOrdinaria(DtoCargaAcademica cargaAcademica, UnidadMateria unidadMateria, String tipoEvaluacion, Date fechaInicio, Date fechaFin, JustificacionPermisosExtemporaneos justificacion, PersonalActivo administrador){
        try{   
            Date fechaFinCompleta = obtenerFechaFin(fechaFin);
            
            PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal = new PermisosCapturaExtemporaneaGrupal();
            permisosCapturaExtemporaneaGrupal.setPeriodo(cargaAcademica.getCargaAcademica().getEvento().getPeriodo());
            permisosCapturaExtemporaneaGrupal.setIdGrupo(cargaAcademica.getGrupo());
            permisosCapturaExtemporaneaGrupal.setIdPlanMateria(cargaAcademica.getPlanEstudioMateria());
            permisosCapturaExtemporaneaGrupal.setDocente(cargaAcademica.getDocente().getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setTipoEvaluacion(tipoEvaluacion);
            permisosCapturaExtemporaneaGrupal.setIdUnidadMateria(unidadMateria);
            permisosCapturaExtemporaneaGrupal.setFechaInicio(fechaInicio);
            permisosCapturaExtemporaneaGrupal.setFechaFin(fechaFinCompleta);
            permisosCapturaExtemporaneaGrupal.setJustificacionPermiso(justificacion);
            permisosCapturaExtemporaneaGrupal.setPersonalGrabaPermiso(administrador.getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setFechaGrabaPermiso(new Date());
            permisosCapturaExtemporaneaGrupal.setTipoApertura("Planeacion");
            permisosCapturaExtemporaneaGrupal.setValidada(1);
            em.persist(permisosCapturaExtemporaneaGrupal);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(permisosCapturaExtemporaneaGrupal, "El permiso de captura extemporánea ordinaria se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el permiso de captura extemporánea ordinaria. (EjbPermisoAperturaExtemporanea.guardarPermisoCapturaOrdinaria)", e, null);
        }
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea para nivelación final
     * @param cargaAcademica Carga Académica para obtener parametros como: periodo, grupo, plan materia y docente.
     * @param tipoEvaluacion Tipo de evaluación a registrar en este caso Nivelación Final
     * @param fechaInicio Fecha inicio para el permiso de captura
     * @param fechaFin Fecha fin para el permiso de captura
     * @param justificacion Justificación por la que el docente solicita el permiso
     * @param administrador Personal administrador que registra el permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<PermisosCapturaExtemporaneaGrupal> guardarPermisoCapturaNivFinal(DtoCargaAcademica cargaAcademica, String tipoEvaluacion, Date fechaInicio, Date fechaFin, JustificacionPermisosExtemporaneos justificacion, PersonalActivo administrador){
        try{   
            
            Date fechaFinCompleta = obtenerFechaFin(fechaFin);
            
            PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal = new PermisosCapturaExtemporaneaGrupal();
            permisosCapturaExtemporaneaGrupal.setPeriodo(cargaAcademica.getCargaAcademica().getEvento().getPeriodo());
            permisosCapturaExtemporaneaGrupal.setIdGrupo(cargaAcademica.getGrupo());
            permisosCapturaExtemporaneaGrupal.setIdPlanMateria(cargaAcademica.getPlanEstudioMateria());
            permisosCapturaExtemporaneaGrupal.setDocente(cargaAcademica.getDocente().getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setTipoEvaluacion(tipoEvaluacion);
            permisosCapturaExtemporaneaGrupal.setIdUnidadMateria(null);
            permisosCapturaExtemporaneaGrupal.setFechaInicio(fechaInicio);
            permisosCapturaExtemporaneaGrupal.setFechaFin(fechaFinCompleta);
            permisosCapturaExtemporaneaGrupal.setJustificacionPermiso(justificacion);
            permisosCapturaExtemporaneaGrupal.setPersonalGrabaPermiso(administrador.getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setFechaGrabaPermiso(new Date());
            permisosCapturaExtemporaneaGrupal.setTipoApertura("Planeacion");
            permisosCapturaExtemporaneaGrupal.setValidada(1);
            em.persist(permisosCapturaExtemporaneaGrupal);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(permisosCapturaExtemporaneaGrupal, "El permiso de captura extemporánea de nivelación se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el permiso de captura extemporánea de nivelación. (EjbPermisoAperturaExtemporanea.guardarPermisoCapturaNivFinal)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de permiso de captura extemporánea vigentes
     * @param docente Docente de quien se quiere obtener la lista
     * @param fechaActual Fecha Actual del sistema
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoPermisoCapturaExtemporanea>> buscarPermisosCapturaVigentes(PersonalActivo docente, Date fechaActual){
        try{
            List<DtoPermisoCapturaExtemporanea> listaDtoPermisoCapturaExtemporanea = new ArrayList<>();
            //buscar lista de permiso de captura extemporánea vigentes
            List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupal = em.createQuery("SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.docente =:docente AND ((:fechaActual BETWEEN p.fechaInicio AND p.fechaFin) OR :fechaActual <= p.fechaInicio)", PermisosCapturaExtemporaneaGrupal.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("fechaActual", fechaActual)
                    .getResultList();
            
            permisosCapturaExtemporaneaGrupal.forEach(permiso -> {
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, permiso.getIdGrupo().getIdPe());
                Personal personal = em.find(Personal.class, permiso.getDocente());
                DtoPermisoCapturaExtemporanea dtoPermisoCapturaExtemporanea = new DtoPermisoCapturaExtemporanea(permiso, programaEducativo, personal);
                listaDtoPermisoCapturaExtemporanea.add(dtoPermisoCapturaExtemporanea);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoPermisoCapturaExtemporanea, "Lista de permisos de captura extemporánea vigentes.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener lista de permisos de captura extemporánea. (EjbPermisoAperturaExtemporanea.buscarPermisosCapturaVigentes)", e, null);
        }
    }
    
     /**
     * Permite eliminar un permiso de captura extemporánea que se encuentre vigente
     * @param clavePermiso Clave del registro a eliminar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarPermisoCaptura(Integer clavePermiso){
        try{
            if(clavePermiso == null) return ResultadoEJB.crearErroneo(2, "La clave del permiso no puede ser nula.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM PermisosCapturaExtemporaneaGrupal p WHERE p.permisoGrupal =:permiso", PermisosCapturaExtemporaneaGrupal.class)
                .setParameter("permiso", clavePermiso)
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "El permiso de captura extemporánea se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el permiso de captura extemporánea. (EjbPermisoAperturaExtemporanea.eliminarPermisoCaptura)", e, null);
        }
    }
    
    public void actualizarPermisoCaptura(DtoPermisoCapturaExtemporanea dtoPermisoCapturaExtemporanea) {
        f.setEntityClass(PermisosCapturaExtemporaneaGrupal.class);
        f.edit(dtoPermisoCapturaExtemporanea.getPermisosCapturaExtemporaneaGrupal());
        f.flush();
    }
    
    public Date obtenerFechaFin(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha); 
        calendar.add(Calendar.HOUR, 23); 
        calendar.add(Calendar.MINUTE, 59); 
        calendar.add(Calendar.SECOND, 59);
        return calendar.getTime(); 
    }
    
     /**
     * Permite obtener la lista de justificaciones activas para solicitar el permiso de captura extemporánea
     * @param cargaAcademica
     * @param unidadMateria
     * @param tipoEvaluacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAperturaExtPorEstudiante>> getListaEstudiantes(DtoCargaAcademica cargaAcademica, UnidadMateria unidadMateria, String tipoEvaluacion ){
        try{
            SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
            List<DtoAperturaExtPorEstudiante> listaDtoEstudiantes = new ArrayList<>();
            
            List<Estudiante> listaEstudiantes = em.createQuery("SELECT e FROM Estudiante e WHERE e.grupo.idGrupo=:grupo AND e.tipoEstudiante.idTipoEstudiante =:activo", Estudiante.class)
                    .setParameter("grupo", cargaAcademica.getCargaAcademica().getCveGrupo().getIdGrupo())
                    .setParameter("activo", Short.parseShort("1"))
                    .getResultList();
           
            listaEstudiantes.forEach(estudiante -> {
                PermisosCapturaExtemporaneaEstudiante permisosCapturaExtemporaneaEstudiante = buscarPermisoActivo(estudiante, cargaAcademica, unidadMateria, tipoEvaluacion);
                
                DtoAperturaExtPorEstudiante dtoAperturaExtPorEstudiante = new DtoAperturaExtPorEstudiante();
                dtoAperturaExtPorEstudiante.setEstudiante(estudiante);
                
                if(permisosCapturaExtemporaneaEstudiante == null)
                {
                    dtoAperturaExtPorEstudiante.setPermisosCapturaExtemporaneaEstudiante(null);
                    dtoAperturaExtPorEstudiante.setInformacionApertura("");
                    dtoAperturaExtPorEstudiante.setValidada(null);
                
                }else{
                    dtoAperturaExtPorEstudiante.setPermisosCapturaExtemporaneaEstudiante(permisosCapturaExtemporaneaEstudiante);
                    if (permisosCapturaExtemporaneaEstudiante.getTipoEvaluacion().equals("Ordinaria")) {
                        dtoAperturaExtPorEstudiante.setInformacionApertura(permisosCapturaExtemporaneaEstudiante.getTipoEvaluacion().concat(" ").concat(Integer.toString(permisosCapturaExtemporaneaEstudiante.getIdUnidadMateria().getNoUnidad())).concat(". ").concat(permisosCapturaExtemporaneaEstudiante.getIdUnidadMateria().getNombre()).concat(". Del ").concat(sm.format(permisosCapturaExtemporaneaEstudiante.getFechaInicio())).concat(" al ").concat(sm.format(permisosCapturaExtemporaneaEstudiante.getFechaFin())));
                    } else {
                        dtoAperturaExtPorEstudiante.setInformacionApertura(permisosCapturaExtemporaneaEstudiante.getTipoEvaluacion().concat(". Del ").concat(sm.format(permisosCapturaExtemporaneaEstudiante.getFechaInicio())).concat(" al ").concat(sm.format(permisosCapturaExtemporaneaEstudiante.getFechaFin())));
                    }
                    Boolean validada;
                    if (permisosCapturaExtemporaneaEstudiante.getValidada() == 0) {
                        validada = false;
                    } else {
                        validada = true;
                    }
                    dtoAperturaExtPorEstudiante.setValidada(validada);
                }
               
               
                listaDtoEstudiantes.add(dtoAperturaExtPorEstudiante);
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoEstudiantes, "Lista de estudiantes que integran el grupo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de estudiantes para permiso de apertura extemporánea. (EjbPermisoAperturaExtemporanea.getListaEstudiantes)", e, null);
        }
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea ordinaria de un estudiante
     * @param cargaAcademica Carga Académica para obtener parametros como: periodo, grupo, plan materia y docente.
     * @param estudiante Estudiante seleccionado
     * @param unidadMateria Unidad materia que se registrará con el permiso
     * @param tipoEvaluacion Tipo de evaluación a registrar en este caso Nivelación Final
     * @param fechaInicio Fecha inicio para el permiso de captura
     * @param fechaFin Fecha fin para el permiso de captura
     * @param justificacion Justificación por la que el docente solicita el permiso
     * @param administrador Personal administrador que registra el permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<PermisosCapturaExtemporaneaEstudiante> guardarPermisoCapturaOrdinariaEstudiante(DtoCargaAcademica cargaAcademica, Estudiante estudiante, UnidadMateria unidadMateria, String tipoEvaluacion, Date fechaInicio, Date fechaFin, JustificacionPermisosExtemporaneos justificacion, PersonalActivo administrador){
        try{
            if(cargaAcademica == null || estudiante == null || unidadMateria == null || tipoEvaluacion == null || fechaInicio == null || fechaFin == null || justificacion == null || administrador == null) return ResultadoEJB.crearErroneo(2, "No se guardar el permiso de apertura porque los datos están incompletos.", PermisosCapturaExtemporaneaEstudiante.class);
            
            Date fechaFinCompleta = obtenerFechaFin(fechaFin);
            
            PermisosCapturaExtemporaneaEstudiante permisosCapturaExtemporaneaEstudiante = new PermisosCapturaExtemporaneaEstudiante();
            permisosCapturaExtemporaneaEstudiante.setPeriodo(cargaAcademica.getCargaAcademica().getEvento().getPeriodo());
            permisosCapturaExtemporaneaEstudiante.setEstudiante(estudiante);
            permisosCapturaExtemporaneaEstudiante.setIdGrupo(cargaAcademica.getGrupo());
            permisosCapturaExtemporaneaEstudiante.setIdPlanMateria(cargaAcademica.getPlanEstudioMateria());
            permisosCapturaExtemporaneaEstudiante.setDocente(cargaAcademica.getDocente().getPersonal().getClave());
            permisosCapturaExtemporaneaEstudiante.setTipoEvaluacion(tipoEvaluacion);
            if (tipoEvaluacion.equals("Ordinaria")) {
                permisosCapturaExtemporaneaEstudiante.setIdUnidadMateria(unidadMateria);
            } else {
                permisosCapturaExtemporaneaEstudiante.setIdUnidadMateria(null);
            }
            permisosCapturaExtemporaneaEstudiante.setFechaInicio(fechaInicio);
            permisosCapturaExtemporaneaEstudiante.setFechaFin(fechaFinCompleta);
            permisosCapturaExtemporaneaEstudiante.setJustificacionPermiso(justificacion);
            permisosCapturaExtemporaneaEstudiante.setPersonalGrabaPermiso(administrador.getPersonal().getClave());
            permisosCapturaExtemporaneaEstudiante.setFechaGrabaPermiso(new Date());
            permisosCapturaExtemporaneaEstudiante.setTipoApertura("Planeación");
            permisosCapturaExtemporaneaEstudiante.setValidada(1);
            em.persist(permisosCapturaExtemporaneaEstudiante);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(permisosCapturaExtemporaneaEstudiante, "El permiso de captura extemporánea ordinaria del estudiante se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el permiso de captura extemporánea ordinaria del estudiante. (EjbPermisoAperturaExtemporanea.guardarPermisoCapturaOrdinariaEstudiante)", e, null);
        }
    }
    
     /**
     * Permite eliminar un permiso de captura extemporánea que se encuentre vigente
     * @param clavePermiso Clave del registro a eliminar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarPermisoCapturaEstudiante(Integer clavePermiso){
        try{
            if(clavePermiso == null) return ResultadoEJB.crearErroneo(2, "La clave del permiso no puede ser nula.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.permisoEstudiante =:permiso", PermisosCapturaExtemporaneaEstudiante.class)
                .setParameter("permiso", clavePermiso)
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "El permiso de captura extemporánea se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el permiso de captura extemporánea. (EjbPermisoAperturaExtemporanea.eliminarPermisoCapturaEstudiante)", e, null);
        }
    }
    
    /**
     * Permite verificar si existe permiso de apertrua extemporanea activo
     * @param estudiante
     * @param cargaAcademica
     * @param unidadMateria
     * @param tipoEvaluacion
     * @return Resultado del proceso
     */
    public PermisosCapturaExtemporaneaEstudiante buscarPermisoActivo(Estudiante estudiante, DtoCargaAcademica cargaAcademica, UnidadMateria unidadMateria, String tipoEvaluacion) {
        
        try{
            
            Date fechaActual = new Date();
            PermisosCapturaExtemporaneaEstudiante permisosCapturaExtemporaneaEstudiante = new PermisosCapturaExtemporaneaEstudiante();
                    
            if (tipoEvaluacion.equals("Ordinaria")) {
                permisosCapturaExtemporaneaEstudiante = em.createQuery("SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.estudiante.idEstudiante =:estudiante AND p.idGrupo.idGrupo =:grupo AND p.idUnidadMateria.idUnidadMateria =:unidadMateria AND p.tipoEvaluacion =:tipoEvaluacion AND ((:fechaActual BETWEEN p.fechaInicio AND p.fechaFin) OR :fechaActual <= p.fechaInicio)", PermisosCapturaExtemporaneaEstudiante.class)
                        .setParameter("estudiante", estudiante.getIdEstudiante())
                        .setParameter("grupo", cargaAcademica.getGrupo().getIdGrupo())
                        .setParameter("unidadMateria", unidadMateria.getIdUnidadMateria())
                        .setParameter("tipoEvaluacion", tipoEvaluacion)
                        .setParameter("fechaActual", fechaActual)
                        .getResultStream().findFirst().orElse(null);
            } else {
                permisosCapturaExtemporaneaEstudiante = em.createQuery("SELECT p FROM PermisosCapturaExtemporaneaEstudiante p WHERE p.estudiante.idEstudiante =:estudiante AND p.idGrupo.idGrupo =:grupo AND p.tipoEvaluacion =:tipoEvaluacion AND ((:fechaActual BETWEEN p.fechaInicio AND p.fechaFin) OR :fechaActual <= p.fechaInicio)", PermisosCapturaExtemporaneaEstudiante.class)
                        .setParameter("estudiante", estudiante.getIdEstudiante())
                        .setParameter("grupo", cargaAcademica.getGrupo().getIdGrupo())
                        .setParameter("tipoEvaluacion", tipoEvaluacion)
                        .setParameter("fechaActual", fechaActual)
                        .getResultStream().findFirst().orElse(null);
            }
            return permisosCapturaExtemporaneaEstudiante;
        } catch (NullPointerException ne) {
            return null;
        }
    }
    
    // Apertura Extemporanea solicitada por el docente
    
    /**
     * Permite validar si el usuario autenticado es personal docente
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getActividad().equals(3)) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            }
            else if (!p.getPersonal().getActividad().equals(3) && !p.getCargaAcademicas().isEmpty()) {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbUnidadMateriaConfiguracion.validarDocente)", e, null);
        }
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea ordinaria
     * @param cargaAcademica Carga Académica para obtener parametros como: periodo, grupo, plan materia y docente.
     * @param unidadMateria Unidad materia que se registrará con el permiso
     * @param tipoEvaluacion Tipo de evaluación a registrar en este caso Nivelación Final
     * @param fechaInicio Fecha inicio para el permiso de captura
     * @param fechaFin Fecha fin para el permiso de captura
     * @param justificacion Justificación por la que el docente solicita el permiso
     * @param administrador Personal administrador que registra el permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<PermisosCapturaExtemporaneaGrupal> guardarPermisoCapturaOrdinariaDocente(DtoCargaAcademica cargaAcademica, UnidadMateria unidadMateria, String tipoEvaluacion, Date fechaInicio, Date fechaFin, JustificacionPermisosExtemporaneos justificacion, PersonalActivo administrador){
        try{   
            Date fechaFinCompleta = obtenerFechaFin(fechaFin);
            
            PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal = new PermisosCapturaExtemporaneaGrupal();
            permisosCapturaExtemporaneaGrupal.setPeriodo(cargaAcademica.getCargaAcademica().getEvento().getPeriodo());
            permisosCapturaExtemporaneaGrupal.setIdGrupo(cargaAcademica.getGrupo());
            permisosCapturaExtemporaneaGrupal.setIdPlanMateria(cargaAcademica.getPlanEstudioMateria());
            permisosCapturaExtemporaneaGrupal.setDocente(cargaAcademica.getDocente().getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setTipoEvaluacion(tipoEvaluacion);
            permisosCapturaExtemporaneaGrupal.setIdUnidadMateria(unidadMateria);
            permisosCapturaExtemporaneaGrupal.setFechaInicio(fechaInicio);
            permisosCapturaExtemporaneaGrupal.setFechaFin(fechaFinCompleta);
            permisosCapturaExtemporaneaGrupal.setJustificacionPermiso(justificacion);
            permisosCapturaExtemporaneaGrupal.setPersonalGrabaPermiso(0);
            permisosCapturaExtemporaneaGrupal.setFechaGrabaPermiso(null);
            permisosCapturaExtemporaneaGrupal.setTipoApertura("Docente");
            permisosCapturaExtemporaneaGrupal.setValidada(0);
            em.persist(permisosCapturaExtemporaneaGrupal);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(permisosCapturaExtemporaneaGrupal, "El permiso de captura extemporánea ordinaria se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el permiso de captura extemporánea ordinaria. (EjbPermisoAperturaExtemporanea.guardarPermisoCapturaOrdinaria)", e, null);
        }
    }
    
     /**
     * Permite guardar el permiso de captura extemporánea para nivelación final
     * @param cargaAcademica Carga Académica para obtener parametros como: periodo, grupo, plan materia y docente.
     * @param tipoEvaluacion Tipo de evaluación a registrar en este caso Nivelación Final
     * @param fechaInicio Fecha inicio para el permiso de captura
     * @param fechaFin Fecha fin para el permiso de captura
     * @param justificacion Justificación por la que el docente solicita el permiso
     * @param administrador Personal administrador que registra el permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<PermisosCapturaExtemporaneaGrupal> guardarPermisoCapturaNivFinalDocente(DtoCargaAcademica cargaAcademica, String tipoEvaluacion, Date fechaInicio, Date fechaFin, JustificacionPermisosExtemporaneos justificacion, PersonalActivo administrador){
        try{   
            
            Date fechaFinCompleta = obtenerFechaFin(fechaFin);
            
            PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal = new PermisosCapturaExtemporaneaGrupal();
            permisosCapturaExtemporaneaGrupal.setPeriodo(cargaAcademica.getCargaAcademica().getEvento().getPeriodo());
            permisosCapturaExtemporaneaGrupal.setIdGrupo(cargaAcademica.getGrupo());
            permisosCapturaExtemporaneaGrupal.setIdPlanMateria(cargaAcademica.getPlanEstudioMateria());
            permisosCapturaExtemporaneaGrupal.setDocente(cargaAcademica.getDocente().getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setTipoEvaluacion(tipoEvaluacion);
            permisosCapturaExtemporaneaGrupal.setIdUnidadMateria(null);
            permisosCapturaExtemporaneaGrupal.setFechaInicio(fechaInicio);
            permisosCapturaExtemporaneaGrupal.setFechaFin(fechaFinCompleta);
            permisosCapturaExtemporaneaGrupal.setJustificacionPermiso(justificacion);
            permisosCapturaExtemporaneaGrupal.setPersonalGrabaPermiso(0);
            permisosCapturaExtemporaneaGrupal.setFechaGrabaPermiso(null);
            permisosCapturaExtemporaneaGrupal.setTipoApertura("Docente");
            permisosCapturaExtemporaneaGrupal.setValidada(0);
            em.persist(permisosCapturaExtemporaneaGrupal);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(permisosCapturaExtemporaneaGrupal, "El permiso de captura extemporánea de nivelación se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el permiso de captura extemporánea de nivelación. (EjbPermisoAperturaExtemporanea.guardarPermisoCapturaNivFinal)", e, null);
        }
    }
    
    /**
     * Permite guardar el permiso de captura extemporánea ordinaria de un estudiante
     * @param cargaAcademica Carga Académica para obtener parametros como: periodo, grupo, plan materia y docente.
     * @param estudiante Estudiante seleccionado
     * @param unidadMateria Unidad materia que se registrará con el permiso
     * @param tipoEvaluacion Tipo de evaluación a registrar en este caso Nivelación Final
     * @param fechaInicio Fecha inicio para el permiso de captura
     * @param fechaFin Fecha fin para el permiso de captura
     * @param justificacion Justificación por la que el docente solicita el permiso
     * @param administrador Personal administrador que registra el permiso
     * @return Resultado del proceso
     */
    public ResultadoEJB<PermisosCapturaExtemporaneaEstudiante> guardarPermisoCapturaOrdinariaEstudianteDocente(DtoCargaAcademica cargaAcademica, Estudiante estudiante, UnidadMateria unidadMateria, String tipoEvaluacion, Date fechaInicio, Date fechaFin, JustificacionPermisosExtemporaneos justificacion, PersonalActivo administrador){
        try{   
            if(cargaAcademica == null || estudiante == null || unidadMateria == null || tipoEvaluacion == null || fechaInicio == null || fechaFin == null || justificacion == null || administrador == null) return ResultadoEJB.crearErroneo(2, "No se guardar el permiso de apertura porque los datos están incompletos.", PermisosCapturaExtemporaneaEstudiante.class);
            
            Date fechaFinCompleta = obtenerFechaFin(fechaFin);
            
            PermisosCapturaExtemporaneaEstudiante permisosCapturaExtemporaneaEstudiante = new PermisosCapturaExtemporaneaEstudiante();
            permisosCapturaExtemporaneaEstudiante.setPeriodo(cargaAcademica.getCargaAcademica().getEvento().getPeriodo());
            permisosCapturaExtemporaneaEstudiante.setEstudiante(estudiante);
            permisosCapturaExtemporaneaEstudiante.setIdGrupo(cargaAcademica.getGrupo());
            permisosCapturaExtemporaneaEstudiante.setIdPlanMateria(cargaAcademica.getPlanEstudioMateria());
            permisosCapturaExtemporaneaEstudiante.setDocente(cargaAcademica.getDocente().getPersonal().getClave());
            permisosCapturaExtemporaneaEstudiante.setTipoEvaluacion(tipoEvaluacion);
            if (tipoEvaluacion.equals("Ordinaria")) {
                permisosCapturaExtemporaneaEstudiante.setIdUnidadMateria(unidadMateria);
            } else {
                permisosCapturaExtemporaneaEstudiante.setIdUnidadMateria(null);
            }
            permisosCapturaExtemporaneaEstudiante.setFechaInicio(fechaInicio);
            permisosCapturaExtemporaneaEstudiante.setFechaFin(fechaFinCompleta);
            permisosCapturaExtemporaneaEstudiante.setJustificacionPermiso(justificacion);
            permisosCapturaExtemporaneaEstudiante.setPersonalGrabaPermiso(0);
            permisosCapturaExtemporaneaEstudiante.setFechaGrabaPermiso(null);
            permisosCapturaExtemporaneaEstudiante.setTipoApertura("Docente");
            permisosCapturaExtemporaneaEstudiante.setValidada(0);
            em.persist(permisosCapturaExtemporaneaEstudiante);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(permisosCapturaExtemporaneaEstudiante, "El permiso de captura extemporánea ordinaria del estudiante se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el permiso de captura extemporánea ordinaria del estudiante. (EjbPermisoAperturaExtemporanea.guardarPermisoCapturaOrdinariaEstudiante)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de periodos en los que se han registrado solicitudes de apertura extemporanea
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PermisosCapturaExtemporaneaGrupal>> getPermisosCapturaExtemporaneaGrupal(){
        try{
            List<PermisosCapturaExtemporaneaGrupal> listaPermisosCapturaGrupal = em.createQuery("SELECT pg FROM PermisosCapturaExtemporaneaGrupal pg ORDER BY pg.periodo DESC",  PermisosCapturaExtemporaneaGrupal.class)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(listaPermisosCapturaGrupal, "Lista de periodo escolares en los que se han registrado permisos.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos en los que se han registrado bajas. (EjbPermisoAperturaExtemporanea.getPermisosCapturaExtemporaneaGrupal)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de periodos en los que se han registrado solicitudes de apertura extemporanea
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PermisosCapturaExtemporaneaEstudiante>> getPermisosCapturaExtemporaneaEstudiante(){
        try{
            List<PermisosCapturaExtemporaneaEstudiante> listaPermisosCapturaEstudiante = em.createQuery("SELECT pe FROM PermisosCapturaExtemporaneaEstudiante pe ORDER BY pe.periodo DESC",  PermisosCapturaExtemporaneaEstudiante.class)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaPermisosCapturaEstudiante, "Lista de periodo escolares en los que se han registrado permisos.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos en los que se han registrado bajas. (EjbPermisoAperturaExtemporanea.getPermisosCapturaExtemporaneaEstudiante)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de periodos en los que se han registrado solicitudes de apertura extemporanea
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosPermisosSolicitados(){
        try{
            List<PeriodosEscolares> listaPeriodos = new ArrayList<>();
            
            List<PermisosCapturaExtemporaneaGrupal> periodosPermisoGrupal = getPermisosCapturaExtemporaneaGrupal().getValor();
            
            periodosPermisoGrupal.forEach(periodoGrupal -> {
                if (periodoGrupal.getTipoApertura().equals("Docente")) {
                    PeriodosEscolares periodo = em.find(PeriodosEscolares.class, periodoGrupal.getPeriodo());
                    listaPeriodos.add(periodo);
                }
            });
          
            List<PermisosCapturaExtemporaneaEstudiante> periodosPermisoEstudiante = getPermisosCapturaExtemporaneaEstudiante().getValor();
          
            periodosPermisoEstudiante.forEach(periodoEstudiante -> {
                if (periodoEstudiante.getTipoApertura().equals("Docente")) {
                    PeriodosEscolares periodo = em.find(PeriodosEscolares.class, periodoEstudiante.getPeriodo());
                    listaPeriodos.add(periodo);
                }
            });
            
             List<PeriodosEscolares> listaPeriodosDistintos = listaPeriodos.stream()
                    .distinct()
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaPeriodosDistintos, "Lista de periodo escolares en los que se han registrado permisos.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos en los que se han registrado bajas. (EjbPermisoAperturaExtemporanea.getPeriodosPermisosSolicitados)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de programas educativos que tienen registradas solicitudes de apertura extemporanea
     * @param periodo
     * @param bajas Lista de bajas registradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<AreasUniversidad>> getProgramasEducativosPermisosSolicitados(PeriodosEscolares periodo){
         try{
            List<AreasUniversidad> listaProgramasEducativos = new ArrayList<>();
            
            List<PermisosCapturaExtemporaneaGrupal> areasPermisoGrupal = getPermisosCapturaExtemporaneaGrupal().getValor();
          
            areasPermisoGrupal.forEach(areaGrupal -> {
                if (areaGrupal.getPeriodo() == periodo.getPeriodo() && areaGrupal.getTipoApertura().equals("Docente")) {
                    AreasUniversidad area = em.find(AreasUniversidad.class, areaGrupal.getIdGrupo().getIdPe());
                    listaProgramasEducativos.add(area);
                }
            });
            
            List<PermisosCapturaExtemporaneaEstudiante> areasPermisoEstudiante = getPermisosCapturaExtemporaneaEstudiante().getValor();
          
            areasPermisoEstudiante.forEach(areaEstudiante -> {
                if (areaEstudiante.getPeriodo() == periodo.getPeriodo() && areaEstudiante.getTipoApertura().equals("Docente")) {
                    AreasUniversidad area = em.find(AreasUniversidad.class, areaEstudiante.getIdGrupo().getIdPe());
                    listaProgramasEducativos.add(area);
                }
            });
            
             List<AreasUniversidad> listaProgramasDistintos = listaProgramasEducativos.stream()
                    .distinct()
                    .sorted(Comparator.comparing(AreasUniversidad::getNombre))
                    .collect(Collectors.toList());
             
            return ResultadoEJB.crearCorrecto(listaProgramasDistintos, "Lista de programas educativos del periodo seleccionado en el que se han registrado solicitudes.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de programas educativos en los que se ga registrado solicitudes. (EjbPermisoAperturaExtemporanea.getProgramasEducativosPermisosSolicitados)", e, null);
        }
    }
   
   
     /**
     * Permite obtener lista de solicitudes registradas del periodo escolar y programa educativo seleccionado
     * @param periodo Periodo escolar seleccionado
     * @param programaEducativo Programa Educativo del que se hará al consulta
     * @return Resultado del proceso ordenado 
     */
    public ResultadoEJB<List<DtoValidarPermisoCapExt>> obtenerListaPermisosPeriodoProgramaEducativo(PeriodosEscolares periodo, AreasUniversidad programaEducativo){
        try{
            SimpleDateFormat sm = new SimpleDateFormat("dd-MM-yyyy");
            List<DtoValidarPermisoCapExt> listaPermisosApertura = new ArrayList<>();
            
            List<PermisosCapturaExtemporaneaGrupal> permisosGrupales = getPermisosCapturaExtemporaneaGrupal().getValor();
          
            permisosGrupales.forEach(permisoGrupo -> {
                if (permisoGrupo.getPeriodo() == periodo.getPeriodo() && permisoGrupo.getIdGrupo().getIdPe() == programaEducativo.getArea()) {
                    Personal docente = em.find(Personal.class, permisoGrupo.getDocente());
                    String rangoFechas = sm.format(permisoGrupo.getFechaInicio()).concat(" al ").concat(sm.format(permisoGrupo.getFechaFin()));
                    DtoValidarPermisoCapExt dtoValidarPermisoCapExt = new DtoValidarPermisoCapExt();
                    dtoValidarPermisoCapExt.setClavePermiso(permisoGrupo.getPermisoGrupal());
                    dtoValidarPermisoCapExt.setTipoApertura(permisoGrupo.getTipoApertura());
                    dtoValidarPermisoCapExt.setTipoSolicitud("Grupal");
                    dtoValidarPermisoCapExt.setEstudiante("No Aplica");
                    dtoValidarPermisoCapExt.setGrupo(permisoGrupo.getIdGrupo());
                    dtoValidarPermisoCapExt.setPlanMateria(permisoGrupo.getIdPlanMateria());
                    if (permisoGrupo.getTipoEvaluacion().equals("Ordinaria")) {
                        dtoValidarPermisoCapExt.setUnidadMateria(permisoGrupo.getIdUnidadMateria());
                    } else {
                        dtoValidarPermisoCapExt.setUnidadMateria(null);
                    }
                    dtoValidarPermisoCapExt.setDocente(docente);
                    dtoValidarPermisoCapExt.setTipoEvaluacion(permisoGrupo.getTipoEvaluacion());
                    dtoValidarPermisoCapExt.setRangoFechas(rangoFechas);
                    dtoValidarPermisoCapExt.setJustificacion(permisoGrupo.getJustificacionPermiso().getDescripcion());
                    if (permisoGrupo.getValidada() == 0) {
                        dtoValidarPermisoCapExt.setPersonaValido(null);
                        dtoValidarPermisoCapExt.setFechaValidacion(null);
                        dtoValidarPermisoCapExt.setValidado(false);
                    } else {
                        Personal personalValido = em.find(Personal.class, permisoGrupo.getPersonalGrabaPermiso());
                        dtoValidarPermisoCapExt.setPersonaValido(personalValido);
                        dtoValidarPermisoCapExt.setFechaValidacion(permisoGrupo.getFechaGrabaPermiso());
                        dtoValidarPermisoCapExt.setValidado(true);
                    }
                    listaPermisosApertura.add(dtoValidarPermisoCapExt);
                }
            });
            
            List<PermisosCapturaExtemporaneaEstudiante> permisosEstudiante = getPermisosCapturaExtemporaneaEstudiante().getValor();
          
            permisosEstudiante.forEach(permisoEstudiante -> {
                if (permisoEstudiante.getPeriodo() == periodo.getPeriodo() && permisoEstudiante.getIdGrupo().getIdPe() == programaEducativo.getArea()) {
                    Personal docente = em.find(Personal.class, permisoEstudiante.getDocente());
                    String rangoFechas = sm.format(permisoEstudiante.getFechaInicio()).concat(" al ").concat(sm.format(permisoEstudiante.getFechaFin()));
                    DtoValidarPermisoCapExt dtoValidarPermisoCapExt = new DtoValidarPermisoCapExt();
                    dtoValidarPermisoCapExt.setClavePermiso(permisoEstudiante.getPermisoEstudiante());
                    dtoValidarPermisoCapExt.setTipoApertura(permisoEstudiante.getTipoApertura());
                    dtoValidarPermisoCapExt.setTipoSolicitud("Individual");
                    dtoValidarPermisoCapExt.setEstudiante(Integer.toString(permisoEstudiante.getEstudiante().getMatricula()).concat(" - ").concat(permisoEstudiante.getEstudiante().getAspirante().getIdPersona().getApellidoPaterno()).concat(" ").concat(permisoEstudiante.getEstudiante().getAspirante().getIdPersona().getApellidoMaterno()).concat(" ").concat(permisoEstudiante.getEstudiante().getAspirante().getIdPersona().getNombre()));
                    dtoValidarPermisoCapExt.setGrupo(permisoEstudiante.getIdGrupo());
                    dtoValidarPermisoCapExt.setPlanMateria(permisoEstudiante.getIdPlanMateria());
                    if (permisoEstudiante.getTipoEvaluacion().equals("Ordinaria")) {
                        dtoValidarPermisoCapExt.setUnidadMateria(permisoEstudiante.getIdUnidadMateria());
                    } else {
                        dtoValidarPermisoCapExt.setUnidadMateria(null);
                    }
                    dtoValidarPermisoCapExt.setDocente(docente);
                    dtoValidarPermisoCapExt.setTipoEvaluacion(permisoEstudiante.getTipoEvaluacion());
                    dtoValidarPermisoCapExt.setRangoFechas(rangoFechas);
                    dtoValidarPermisoCapExt.setJustificacion(permisoEstudiante.getJustificacionPermiso().getDescripcion());
                    if (permisoEstudiante.getValidada() == 0) {
                        dtoValidarPermisoCapExt.setPersonaValido(null);
                        dtoValidarPermisoCapExt.setFechaValidacion(null);
                        dtoValidarPermisoCapExt.setValidado(false);
                    } else {
                        Personal personalValido = em.find(Personal.class, permisoEstudiante.getPersonalGrabaPermiso());
                        dtoValidarPermisoCapExt.setPersonaValido(personalValido);
                        dtoValidarPermisoCapExt.setFechaValidacion(permisoEstudiante.getFechaGrabaPermiso());
                        dtoValidarPermisoCapExt.setValidado(true);
                    }
                    listaPermisosApertura.add(dtoValidarPermisoCapExt);
                }
            });
          
            return ResultadoEJB.crearCorrecto(listaPermisosApertura, "Lista de bajas registradas en el periodo escolar y programa educativo seleccionado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de bajas registradas en el periodo escolar y programa educativo seleccionado. (EjbPermisoAperturaExtemporanea.obtenerListaBajasProgramaEducativo)", e, null);
        }
    }
    
     /**
     * Permite obtener lista de solicitudes registradas del periodo escolar y programa educativo seleccionado
     * @param periodo Periodo escolar seleccionado
     * @param programaEducativo Programa Educativo del que se hará al consulta
     * @return Resultado del proceso ordenado 
     */
    public ResultadoEJB<List<DtoValidarPermisoCapExt>> obtenerListaSolicitudesPeriodoProgramaEducativo(PeriodosEscolares periodo, AreasUniversidad programaEducativo){
        try{
            List<DtoValidarPermisoCapExt> listaPermisosApertura = obtenerListaPermisosPeriodoProgramaEducativo(periodo, programaEducativo).getValor();
          
            List<DtoValidarPermisoCapExt> listaPermisosSolicitados = new ArrayList<>();
            
            listaPermisosApertura.forEach(permiso -> {
                if (permiso.getTipoApertura().equals("Docente")) {
                    listaPermisosSolicitados.add(permiso);
                }
            });
            
            return ResultadoEJB.crearCorrecto(listaPermisosSolicitados, "Lista de bajas registradas en el periodo escolar y programa educativo seleccionado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de bajas registradas en el periodo escolar y programa educativo seleccionado. (EjbPermisoAperturaExtemporanea.obtenerListaBajasProgramaEducativo)", e, null);
        }
    }
    
      /**
     * Permite validar o invalidar el permiso solicitidado
     * @param permiso Permiso solicitado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> validarPermiso(DtoValidarPermisoCapExt permiso, PersonalActivo personal){
        try{
            String mensaje;
            Integer validar;
            
            if (permiso.getTipoSolicitud().equals("Grupal")) {

                if (permiso.getValidado()) {
                    validar = em.createQuery("UPDATE PermisosCapturaExtemporaneaGrupal pg set pg.validada =:valor, pg.fechaGrabaPermiso =:fecha, pg.personalGrabaPermiso =:personal where pg.permisoGrupal =:permiso").setParameter("valor", (int) 0).setParameter("permiso", permiso.getClavePermiso()).setParameter("fecha", null).setParameter("personal", null)
                            .executeUpdate();
                    mensaje = "El permiso grupal se ha invalidado correctamente";
                } else {
                    validar = em.createQuery("UPDATE PermisosCapturaExtemporaneaGrupal pg set pg.validada =:valor, pg.fechaGrabaPermiso =:fecha, pg.personalGrabaPermiso =:personal where pg.permisoGrupal =:permiso").setParameter("valor", (int) 1).setParameter("permiso", permiso.getClavePermiso()).setParameter("fecha", new Date()).setParameter("personal", personal.getPersonal().getClave())
                            .executeUpdate();
                    mensaje = "El permiso grupal se ha validado correctamente";
                }
            } 
            
            else {
                if (permiso.getValidado()) {
                    validar = em.createQuery("UPDATE PermisosCapturaExtemporaneaEstudiante pe set pe.validada =:valor, pe.fechaGrabaPermiso =:fecha, pe.personalGrabaPermiso =:personal where pe.permisoEstudiante =:permiso").setParameter("valor", (int) 0).setParameter("permiso", permiso.getClavePermiso()).setParameter("fecha", null).setParameter("personal", null)
                            .executeUpdate();

                    mensaje = "El permiso por estudiante se ha invalidado correctamente";
                } else {
                    validar = em.createQuery("UPDATE PermisosCapturaExtemporaneaEstudiante pe set pe.validada =:valor, pe.fechaGrabaPermiso =:fecha, pe.personalGrabaPermiso =:personal where pe.permisoEstudiante =:permiso").setParameter("valor", (int) 1).setParameter("permiso", permiso.getClavePermiso()).setParameter("fecha", new Date()).setParameter("personal", personal.getPersonal().getClave())
                            .executeUpdate();
                    mensaje = "El permiso por estudiante se ha validado correctamente";
                }
            }
            
                       
            return ResultadoEJB.crearCorrecto(validar, mensaje);
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo validar o invalidar el permiso seleccionado. (EjbPermisoAperturaExtemporanea.validarPermiso)", e, null);
        }
    }
    
//      /**
//     * Permite eliminar el permiso solicitado
//     * @param permiso Registro de baja
//     * @return Resultado del proceso
//     */
//    public ResultadoEJB<Integer> cambiarStatusEstudiante(DtoValidarPermisoCapExt permiso){
//       try{
//           Integer delete;
//                   
//           if (registro.getTipoBaja() == 1) {
//
//               TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short) 2);
//
//               delete = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
//                       .setParameter("tipoEstudiante", tipoEstudiante)
//                       .setParameter("estudiante", registro.getEstudiante().getIdEstudiante())
//                       .executeUpdate();
//           } else {
//
//               TipoEstudiante tipoEstudiante = em.find(TipoEstudiante.class, (short) 3);
//
//               delete = em.createQuery("update Estudiante e set e.tipoEstudiante =:tipoEstudiante where e.idEstudiante =:estudiante")
//                       .setParameter("tipoEstudiante", tipoEstudiante)
//                       .setParameter("estudiante", registro.getEstudiante().getIdEstudiante())
//                       .executeUpdate();
//           }
//                       
//            return ResultadoEJB.crearCorrecto(delete, "Se ha cambiado la situación académica del estudiante (Baja)");
//        }catch (Throwable e){
//            return ResultadoEJB.crearErroneo(1, "No se pudo cambiar la situación académica del estudiante. (EjbRegistroBajas.cambiarStatusEstudiante)", e, null);
//        }
//    
//    }
}
