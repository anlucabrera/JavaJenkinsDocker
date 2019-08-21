/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPermisoCapturaExtemporanea;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoRangoFechasPermiso;
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
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
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
//        System.out.println("cargaAcademica = " + cargaAcademica);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbPermisoAperturaExtemporanea. pack).", e, DtoCargaAcademica.class);
        }
    }
    
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
           //System.out.println("mx.edu.utxj.pye.sgi.ejb.ServicioAdministracionEncuestas.getPeriodoActual()" + l.get(0));
            return l.get(0);
        }
    }
    
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosCargas(List<DtoCargaAcademica> cargas){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            
            List<PeriodosEscolares> periodos = new ArrayList<>();
            
            cargas.forEach(carga -> {
                //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
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
                    .collect(Collectors.toList());
            
//            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
//            List<PeriodosEscolares> periodosEscolares = em.createQuery("SELECT p FROM PeriodosEscolares p WHERE p.periodo IN :periodos", PeriodosEscolares.class)
//                    .setParameter("periodos", periodos)
//                    .getResultList();
           
            
            return ResultadoEJB.crearCorrecto(listaPeriodos, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbPermisoAperturaExtemporanea.getCargaAcademicaPorDocente)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicaPorPeriodo(PersonalActivo docente, PeriodosEscolares periodo){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT c FROM CargaAcademica c INNER JOIN c.cveGrupo g INNER JOIN c.idPlanMateria pem INNER JOIN pem.idMateria mat INNER JOIN pem.idPlan plan INNER JOIN c.evento e WHERE c.docente=:docente and e.periodo=:periodo ORDER BY g.idPe, g.grado, g.literal, mat.nombre", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
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
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getTiposEvaluaciones(){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<String> tiposEvaluaciones = Stream.of("Ordinaria", "Nivelación Parcial", "Nivelación Final").collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(tiposEvaluaciones, "Lista de tipos de evaluaciones.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de tipos de evaluaciones. (EjbPermisoAperturaExtemporanea.getTiposEvaluaciones)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param cargaAcademica Docente de quien se quiere obtener la lista
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateria>> getUnidadesMateria(DtoCargaAcademica cargaAcademica){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
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
     * @param cargaAcademica
     * @param unidadMateria
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoRangoFechasPermiso> getRangoFechasPermiso(DtoCargaAcademica cargaAcademica, UnidadMateria unidadMateria){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
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
            
            
            DtoRangoFechasPermiso dtoRangoFechasPermiso = new DtoRangoFechasPermiso(rangoFechaInicio, rangoFechaFin);
            
            return ResultadoEJB.crearCorrecto(dtoRangoFechasPermiso, "Rango de fechas obtenidas para la unidad seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el rango de fechas de la unidad seleccionada. (EjbPermisoAperturaExtemporanea.getRangoFechasPermiso)", e, null);
        }
    }
     /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<JustificacionPermisosExtemporaneos>> getJustificacionesPermisoCaptura(){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<JustificacionPermisosExtemporaneos> justificacionPermisosExtemporaneoses = em.createQuery("SELECT j FROM JustificacionPermisosExtemporaneos j WHERE j.activo =:activo", JustificacionPermisosExtemporaneos.class)
                    .setParameter("activo", true)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(justificacionPermisosExtemporaneoses, "Lista de justificaciones para permiso de apertura extemporanea.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de justificaciones para permiso de apertura extemporanea. (EjbPermisoAperturaExtemporanea.getJustificacionesPermisoCaptura)", e, null);
        }
    }
    
    /**
     * Permite guardar el permiso de captura extemporanea
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<PermisosCapturaExtemporaneaGrupal> guardarPermisoCaptura(DtoCargaAcademica cargaAcademica, UnidadMateria unidadMateria, String tipoEvaluacion, Date fechaInicio, Date fechaFin, JustificacionPermisosExtemporaneos justificacion, PersonalActivo administrador){
        try{   
            PermisosCapturaExtemporaneaGrupal permisosCapturaExtemporaneaGrupal = new PermisosCapturaExtemporaneaGrupal();
            permisosCapturaExtemporaneaGrupal.setPeriodo(cargaAcademica.getCargaAcademica().getEvento().getPeriodo());
            permisosCapturaExtemporaneaGrupal.setIdGrupo(cargaAcademica.getGrupo());
            permisosCapturaExtemporaneaGrupal.setIdPlanMateria(cargaAcademica.getPlanEstudioMateria());
            permisosCapturaExtemporaneaGrupal.setDocente(cargaAcademica.getDocente().getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setTipoEvaluacion(tipoEvaluacion);
            permisosCapturaExtemporaneaGrupal.setIdUnidadMateria(unidadMateria);
            permisosCapturaExtemporaneaGrupal.setFechaInicio(fechaInicio);
            permisosCapturaExtemporaneaGrupal.setFechaFin(fechaFin);
            permisosCapturaExtemporaneaGrupal.setJustificacionPermiso(justificacion);
            permisosCapturaExtemporaneaGrupal.setPersonalGrabaPermiso(administrador.getPersonal().getClave());
            permisosCapturaExtemporaneaGrupal.setFechaGrabaPermiso(new Date());
            f.create(permisosCapturaExtemporaneaGrupal);
            f.flush();
            
            return ResultadoEJB.crearCorrecto(permisosCapturaExtemporaneaGrupal, "El permiso de captura extemporanea se ha registrado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el permiso de captura extemporanea. (EjbPermisoAperturaExtemporanea.guardarPermisoCaptura)", e, null);
        }
    }
     /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoPermisoCapturaExtemporanea>> buscarPermisosCapturaVigentes(PersonalActivo docente, Date fechaActual){
//        System.out.println("docente = [" + docente + "], periodo = [" + periodo + "]");
        try{
            List<DtoPermisoCapturaExtemporanea> listaDtoPermisoCapturaExtemporanea = new ArrayList<>();
            //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
            List<PermisosCapturaExtemporaneaGrupal> permisosCapturaExtemporaneaGrupal = em.createQuery("SELECT p FROM PermisosCapturaExtemporaneaGrupal p WHERE p.docente =:docente AND (:fechaActual BETWEEN p.fechaInicio AND p.fechaFin) OR :fechaActual <= p.fechaInicio", PermisosCapturaExtemporaneaGrupal.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("fechaActual", fechaActual)
                    .getResultList();
            
            permisosCapturaExtemporaneaGrupal.forEach(permiso -> {
                //buscar lista de materias sin asignar que pertenecen al programa y grupo seleccionado
                AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, permiso.getIdGrupo().getIdPe());
                DtoPermisoCapturaExtemporanea dtoPermisoCapturaExtemporanea = new DtoPermisoCapturaExtemporanea(permiso, programaEducativo);
                
                listaDtoPermisoCapturaExtemporanea.add(dtoPermisoCapturaExtemporanea);
                
            });
            
            return ResultadoEJB.crearCorrecto(listaDtoPermisoCapturaExtemporanea, "Lista de permisos de apertura extemporanea vigentes.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener lista de permisos de apertura extemporanea. (EjbPermisoAperturaExtemporanea.buscarPermisosCapturaVigentes)", e, null);
        }
    }
    
     /**
     * Permite eliminar la asignación de indicadores por criterio
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<Integer> eliminarPermisoCaptura(Integer clavePermiso){
        try{
            if(clavePermiso == null) return ResultadoEJB.crearErroneo(2, "La clave del permiso no puede ser nula.");

            Integer delete = em.createQuery("DELETE FROM PermisosCapturaExtemporaneaGrupal p WHERE p.permisoGrupal =:permiso", PermisosCapturaExtemporaneaGrupal.class)
                .setParameter("permiso", clavePermiso)
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "El permiso de captura extemporanea se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el permiso de captura extemporanea. (EjbPermisoAperturaExtemporanea.eliminarPermisoCaptura)", e, null);
        }
    }
}
