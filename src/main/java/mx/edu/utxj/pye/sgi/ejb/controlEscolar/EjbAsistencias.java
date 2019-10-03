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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import lombok.Getter;
import lombok.Setter;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsignadosIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoPaseLista;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaalumnosca;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAsistencias")
public class EjbAsistencias {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    private EntityManager em;
    final List<DtoCargaAcademica> cargas = new ArrayList<>();
    @Getter @Setter Integer grupos = 0;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    /**
     * Permite validar si el usuario autenticado es personal docente
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarDocente(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbAsistencias.validarDocente)", e, null);
        }
    }
    
//    public ResultadoEJB<PersonalActivo> validarTutor(Integer clave) {
//        try {
//            PersonalActivo p = ejbPersonalBean.pack(clave);
//            List<Grupo> gruposes = em.createQuery("select g from Grupo g WHERE g.tutor=:tutor", Grupo.class)
//                    .setParameter("tutor", clave)
//                    .getResultList();
//            if (!gruposes.isEmpty()) {
//                return ResultadoEJB.crearCorrecto(p, "El usuario ha sido comprobado como personal tutor.");
//            }
//        } catch (Exception e) {
//            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbAsistencias.validarDocente)", e, null);
//        }
//    }

    /**
     * Permite obtener la lista de periodos escolares a elegir en el apartado de asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
            final List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p order by p.periodo desc", PeriodosEscolares.class)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsistencias.getPeriodosDescendentes)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de cargas académicas de un docente, en todos los programas educativos que participe
     * @param docente Docente de quien se quiere obtener la lista
     * @param periodo Periodo seleccionado en pantalla
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicaDocente(PersonalActivo docente, PeriodosEscolares periodo){
        try{
            //buscar carga académica del personal docente logeado del periodo seleccionado
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.docente =:docente AND c.evento.periodo =:periodo", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbAsistencias.getCargaAcademicaPorDocente)", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicasPorTutor(Integer docente, PeriodosEscolares periodo) {
        try {
            //buscar carga académica del personal docente logeado del periodo seleccionado
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT c FROM CargaAcademica c INNER JOIN c.cveGrupo g WHERE g.tutor =:tutor AND c.evento.periodo =:periodo", CargaAcademica.class)
                    .setParameter("tutor", docente)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por Tutor. (EjbAsignacionIndicadoresCriterios.getCargaAcademicaPorDocente)", e, null);
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
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class, cargaAcademicaBD.getEvento().getPeriodo());
            PlanEstudioMateria planEstudioMateria = em.find(PlanEstudioMateria.class, cargaAcademicaBD.getIdPlanMateria().getIdPlanMateria());
            PlanEstudio planEstudio = em.find(PlanEstudio.class, planEstudioMateria.getIdPlan().getIdPlanEstudio());
            Materia materia = em.find(Materia.class, planEstudioMateria.getIdMateria().getIdMateria());
            PersonalActivo docente = ejbPersonalBean.pack(cargaAcademicaBD.getDocente());
            AreasUniversidad programa = em.find(AreasUniversidad.class, planEstudioMateria.getIdPlan().getIdPe());
            DtoCargaAcademica dto = new DtoCargaAcademica(cargaAcademicaBD, periodo, docente, grupo, materia, programa, planEstudio, planEstudioMateria);
            return ResultadoEJB.crearCorrecto(dto, "Carga académica empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbAsistencias.pack).", e, DtoCargaAcademica.class);
        }
    }
    
    /**
     * Permite verificar si existe configuración de las unidades materia de la carga académica seleccionada
     * @param dtoCargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateriaConfiguracion>> buscarConfiguracionUnidadMateria(DtoCargaAcademica dtoCargaAcademica){
        try{
            List<UnidadMateriaConfiguracion> listaUnidMatConf = em.createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica", UnidadMateriaConfiguracion.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaUnidMatConf, "Lista de configuración de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbAsistencias.buscarConfiguracionUnidadMateria)", e, null);
        }
    }
    
    public ResultadoEJB<List<UnidadMateriaConfiguracion>> buscarConfiguracionUnidadesMateriasGrupoT(DtoCargaAcademica dtoCargaAcademica){
        try{
            List<UnidadMateriaConfiguracion> listaUnidMatConf = em.createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.cveGrupo.idGrupo =:idGrupo", UnidadMateriaConfiguracion.class)
                    .setParameter("idGrupo", dtoCargaAcademica.getGrupo().getIdGrupo())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaUnidMatConf, "Lista de configuración de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbAsistencias.buscarConfiguracionUnidadMateria)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe Pase de lista y seguimiento por docente y tutor
     * @param dtoCargaAcademica Materia de la que se buscará asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<UnidadMateriaConfiguracionDetalle> buscarConfiguracionUnidadMateriaDetalle(DtoCargaAcademica dtoCargaAcademica){
        try{
            UnidadMateriaConfiguracionDetalle unidadMatConfDet = em.createQuery("SELECT umcd FROM UnidadMateriaConfiguracionDetalle umcd JOIN umcd.configuracion conf WHERE conf.carga.carga =:cargaAcademica", UnidadMateriaConfiguracionDetalle.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getSingleResult();
            return ResultadoEJB.crearCorrecto(unidadMatConfDet, "Existe asignación de indicadores por criterio para la materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la materia del docente. (EjbAsistencias.getConfiguracionUnidadMateria)", e, null);
        }
    }
       
    /**
     * Permite obtener la lista de indicadores por criterio de la materia para realizar la asignación posteriormente
     * @param dtoCargaAcademica Materia de la que se buscará la lista de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioParaAsignar(DtoCargaAcademica dtoCargaAcademica){ 
        try {
            Integer periodo = getPeriodoActivoIndicadores(dtoCargaAcademica); 
            Integer configuracion = getConfiguracion(dtoCargaAcademica); 
            
            StoredProcedureQuery q = em.createStoredProcedureQuery("control_escolar.indicadoresPorCriterioPorConfiguracion", Listaindicadoresporcriterioporconfiguracion.class).
                    registerStoredProcedureParameter("cargaAcademica", Integer.class, ParameterMode.IN).setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga()).
                    registerStoredProcedureParameter("periodo", Integer.class, ParameterMode.IN).setParameter("periodo", periodo).
                    registerStoredProcedureParameter("configuracion", Integer.class, ParameterMode.IN).setParameter("configuracion", configuracion);
           
            List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores = q.getResultList();
            
//            List<DtoIndicadoresPorCriterioConfiguracion> listaIndicadores = em.createNativeQuery("{call indicadoresPorCriterioPorConfiguracion(?,?,?)}", DtoAsignadosIndicadoresCriterios.class)
//                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
//                    .setParameter("periodo", periodo)
//                    .setParameter("configuracion", configuracion)
//                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaIndicadores, "Lista de indicadores por criterio.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores por criterio. (EjbAsistencias.getIndicadoresCriterioParaAsignar)", e, null);
        }
    }
    
     /**
     * Permite obtener periodo más actual para obtener la lista de indicadores por criterio vigente
     * @param dtoCargaAcademica Materia de la que se buscará la lista de indicadores por criterio
     * @return Resultado del proceso
     */
    public Integer getPeriodoActivoIndicadores(DtoCargaAcademica dtoCargaAcademica)
    {
        int periodo = 0;
        
        TypedQuery<Integer> criterio = (TypedQuery<Integer>) em.createQuery("SELECT MAX(umcc.unidadMateriaConfiguracionCriterioPK.criterio) FROM UnidadMateriaConfiguracionCriterio umcc JOIN umcc.unidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica")
                                                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga());
        if (criterio.getSingleResult() == 0) {
            periodo = 0;
        } else {
            TypedQuery<Integer> periodoCriterio = (TypedQuery<Integer>) em.createQuery("SELECT MAX(cip.criterioIndicadorPeriodoPK.periodo) FROM CriterioIndicadorPeriodo cip WHERE cip.criterioIndicadorPeriodoPK.criterio=:criterio")
                    .setParameter("criterio", criterio.getSingleResult());
            if (periodoCriterio.getSingleResult() == 0) {
                periodo = 0;
            } else {
                periodo = periodoCriterio.getSingleResult();
            }
        }

        
        return periodo;
    }
    
     /**
     * Permite obtener configuración de la materia para obtener la lista de indicadores por criterio vigente
     * @param dtoCargaAcademica Materia de la que se buscará la lista de indicadores por criterio
     * @return Resultado del proceso
     */
    public Integer getConfiguracion(DtoCargaAcademica dtoCargaAcademica)
    {
        int configuracion = 0;
        TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(umc.configuracion) FROM UnidadMateriaConfiguracionCriterio umcc JOIN umcc.unidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica")
                                                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga());
        
        if (v.getSingleResult() == 0) {
            configuracion = 0;
        } else {
            configuracion = v.getSingleResult();
        }
        return configuracion;
    }
    
    /**
     * Permite obtener la lista de indicadores del criterio SER de la materia
     * @param listaIndicadores Lista de indicadores general
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioSer(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores){ 
        try{
            List<Listaindicadoresporcriterioporconfiguracion> l = Collections.EMPTY_LIST;
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2, l,  "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Ser")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "Lista de indicadores del criterio SER se obtuvo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores del criterio SER. (EjbAsistencias.getIndicadoresCriterioSer)", e, null);
        }
    }
    
    /**
     * Permite obtener el valor porcentual del criterio SER
     * @param listaIndicadoresSer Lista de indicadores del criterio SER
     * @return Resultado del proceso
     */
    public Double getPorcentajeSer(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadoresSer){ 
        try{            
            if(listaIndicadoresSer == null || listaIndicadoresSer.isEmpty()) return null;
            Listaindicadoresporcriterioporconfiguracion obtenerPor = listaIndicadoresSer.get(0);
            Double porcentajeSer = obtenerPor.getPorcentaje();
            
            return porcentajeSer;
        }catch (Throwable e){
            return null;
        }
    }
   
   /**
     * Permite obtener la lista de indicadores del criterio SABER de la materia
     * @param listaIndicadores Lista de indicadores general
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioSaber(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores){ 
        try{
            List<Listaindicadoresporcriterioporconfiguracion> l = Collections.EMPTY_LIST;
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2, l, "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Saber")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "Lista de indicadores del criterio SABER se obtuvo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores del criterio SABER. (EjbAsistencias.getIndicadoresCriterioSaber)", e, null);
        }
    }
    
     /**
     * Permite obtener el valor porcentual del criterio SABER
     * @param listaIndicadoresSaber Lista de indicadores del criterio SABER
     * @return Resultado del proceso
     */
    public Double getPorcentajeSaber(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadoresSaber){
        try{
            if(listaIndicadoresSaber == null || listaIndicadoresSaber.isEmpty()) return null;
            Listaindicadoresporcriterioporconfiguracion obtenerPor = listaIndicadoresSaber.get(0);
            Double porcentajeSaber = obtenerPor.getPorcentaje();

            return porcentajeSaber;
        }catch (Throwable e){
            return null;
        }
    }

   /**
     * Permite obtener la lista de indicadores del criterio SABER - HACER de la materia
     * @param listaIndicadores Lista de indicadores general
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioSaberHacer(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores){ 
        try{
            List<Listaindicadoresporcriterioporconfiguracion> l = Collections.EMPTY_LIST;
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2,l,  "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Saber hacer")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "Lista de indicadores del criterio SABER - HACER se obtuvo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores del criterio SABER - HACER. (EjbAsistencias.getIndicadoresCriterioSaberHacer)", e, null);
        }
    }

     /**
     * Permite obtener el valor porcentual del criterio SABER - HACER
     * @param listaIndicadoresSaberHacer Lista de indicadores del criterio SABER - HACER
     * @return Resultado del proceso
     */
    public Double getPorcentajeSaberHacer(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadoresSaberHacer){
        try{
            if(listaIndicadoresSaberHacer == null || listaIndicadoresSaberHacer.isEmpty()) return null;
            Listaindicadoresporcriterioporconfiguracion obtenerPor = listaIndicadoresSaberHacer.get(0);
            Double porcentajeSaberHacer = obtenerPor.getPorcentaje();

            return porcentajeSaberHacer;
        }catch (Throwable e){
            return null;
        }
    }
    
     /**
     * Permite eliminar la asignación de indicadores por criterio registrada
     * @param dtoConfiguracionUnidadMateria Materia de la que se eliminará asignación
     */
    public void eliminarConfUnidDetalles(DtoConfiguracionUnidadMateria dtoConfiguracionUnidadMateria){
        List<UnidadMateriaConfiguracionDetalle> listaConfDetalles = em.createQuery("SELECT u FROM UnidadMateriaConfiguracionDetalle u WHERE u.configuracion.configuracion =:configuracion", UnidadMateriaConfiguracionDetalle.class)
            .setParameter("configuracion", dtoConfiguracionUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion())
            .getResultList();
      
        if (listaConfDetalles.size() > 0) {

            Integer delete = em.createQuery("DELETE FROM UnidadMateriaConfiguracionDetalle umcd WHERE umcd.configuracion.configuracion =:configuracion", UnidadMateriaConfiguracionDetalle.class)
                    .setParameter("configuracion", dtoConfiguracionUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion())
                    .executeUpdate();
        }
      
    }
    
    /**
     * Permite validar la suma de los porcentajes ingresados por indicador
     * @param lista Lista de indicadores por criterio
     * @return Resultado del proceso
     */
    public Integer validarSumaPorcentajesIndicadores(List<Listaindicadoresporcriterioporconfiguracion> lista)
    {
            Integer valor = 0;
            
            Double totalPorcentajes = lista.stream().mapToDouble(Listaindicadoresporcriterioporconfiguracion::getPorcentajeIndicador).sum();
//            System.err.println("validarSumaPorcentajesIndicadores - totalPorcentajes " + totalPorcentajes);
            
            if(totalPorcentajes > 100.00){
                valor = 1;
            }
            else if(totalPorcentajes < 100.00){
                valor = 2;
            }
            else{
                valor = 0;
            }
            
            return valor;
    }
   
     /**
     * Permite guardar la asignación de indicadores del criterio SER
     * @param listaSer Lista de indicadores que se guardarán
     * @param dtoConfUnidadMateria Materia de la que guardará asignación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSer(List<Listaindicadoresporcriterioporconfiguracion> listaSer, DtoConfiguracionUnidadMateria dtoConfUnidadMateria){
        try{
            List<Listaindicadoresporcriterioporconfiguracion> li = Collections.EMPTY_LIST;
            if(listaSer == null || listaSer.isEmpty()) return ResultadoEJB.crearErroneo(2, li,  "La lista de indicadores SER no debe ser nula.");
            
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
            
            eliminarConfUnidDetalles(dtoConfUnidadMateria);
                   
            listaSer.forEach(ser -> {
                try {
                    if(ser.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, dtoConfUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = em.find(Criterio.class, ser.getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = em.find(Indicador.class, ser.getClaveIndicador());
                    umcd.setIndicador(indicador);
                    umcd.setPorcentaje(ser.getPorcentajeIndicador());
                    em.persist(umcd);
//                    em.persist(umcd);
                    Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                    l.add(vistaLista);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La asignación de indicadores del criterio SER se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SER. (EjbAsistencias.guardarIndicadoresSer)", e, null);
        }
    }
    
     /**
     * Permite guardar la asignación de indicadores del criterio SABER
     * @param listaSaber Lista de indicadores que se guardarán
     * @param dtoConfUnidadMateria Materia de la que guardará asignación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaber(List<Listaindicadoresporcriterioporconfiguracion> listaSaber, DtoConfiguracionUnidadMateria dtoConfUnidadMateria){
        try{
            List<Listaindicadoresporcriterioporconfiguracion> li = Collections.EMPTY_LIST;
            if(listaSaber == null || listaSaber.isEmpty()) return ResultadoEJB.crearErroneo(2, li, "La configuración de la unidad materia no debe ser nula.");
          
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
           
            listaSaber.forEach(saber -> {
                try {
                    if(saber.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, dtoConfUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = em.find(Criterio.class, saber.getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = em.find(Indicador.class, saber.getClaveIndicador());
                    umcd.setIndicador(indicador);
                    umcd.setPorcentaje(saber.getPorcentajeIndicador());
                    em.persist(umcd);
//                    em.persist(umcd);
                    Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                    l.add(vistaLista);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La asignación de indicadores del criterio SABER se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SABER. (EjbAsistencias.guardarIndicadoresSaber)", e, null);
        }
    }
    
     /**
     * Permite guardar la asignación de indicadores del criterio SABER - HACER
     * @param listaSaberHacer Lista de indicadores que se guardarán
     * @param dtoConfUnidadMateria Materia de la que guardará asignación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaberHacer(List<Listaindicadoresporcriterioporconfiguracion> listaSaberHacer, DtoConfiguracionUnidadMateria dtoConfUnidadMateria){
        try{
            List<Listaindicadoresporcriterioporconfiguracion> li = Collections.EMPTY_LIST;
            if(listaSaberHacer == null || listaSaberHacer.isEmpty()) return ResultadoEJB.crearErroneo(2, li, "La configuración de la unidad materia no debe ser nula.");
          
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
           
            listaSaberHacer.forEach(sabhac -> {
                try {
                    if(sabhac.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, dtoConfUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = em.find(Criterio.class, sabhac.getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = em.find(Indicador.class, sabhac.getClaveIndicador());
                    umcd.setIndicador(indicador);
                    umcd.setPorcentaje(sabhac.getPorcentajeIndicador());
                    em.persist(umcd);
//                    em.persist(umcd);
                    Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                    l.add(vistaLista);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La asignación de indicadores del criterio SABER - HACER se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SABER - HACER. (EjbAsistencias.guardarIndicadoresSaberHacer)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de configuración de unidad por materia, de la materia seleccionada previamente
     * @param dtoCargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoConfiguracionUnidadMateria>> getConfiguracionUnidadMateria(DtoCargaAcademica dtoCargaAcademica){
        try{
            List<DtoConfiguracionUnidadMateria> unidadMatConfDto = em.createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica", UnidadMateriaConfiguracion.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(unidadMatConfDto, "Lista de configuración de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbAsistencias.getConfiguracionUnidadMateria)", e, null);
        }
    }
    
    /**
     * Empaqueta una configuración sugeridad de la unidad materia en su DTO Wrapper
     * @param unidadMateriaConfiguracion Unidades de la materia a empaquetar
     * @return Configuración sugerida de la unidad materia empaquetada
     */
    public ResultadoEJB<DtoConfiguracionUnidadMateria> pack(UnidadMateriaConfiguracion unidadMateriaConfiguracion){
        try{
            if(unidadMateriaConfiguracion == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una configuración de la unidad nula.", DtoConfiguracionUnidadMateria.class);

            UnidadMateria unidadMateria = em.find(UnidadMateria.class, unidadMateriaConfiguracion.getIdUnidadMateria().getIdUnidadMateria());
            
            DtoConfiguracionUnidadMateria dto = new DtoConfiguracionUnidadMateria(unidadMateria, unidadMateriaConfiguracion);
         
            return ResultadoEJB.crearCorrecto(dto, "Configuración de la unidad materia empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la configuración de la unidad materia (EjbAsistencias.pack).", e, DtoConfiguracionUnidadMateria.class);
        }
    }
    
     /**
     * Permite verificar si existe asignación de indicadores por criterio de la unidad seleccionada
     * @param dtoConfiguracionUnidadMateria Configuración de la que se buscará asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateriaConfiguracionDetalle>> buscarAsignacionIndicadoresUnidad(DtoConfiguracionUnidadMateria dtoConfiguracionUnidadMateria){
        try{
            List<UnidadMateriaConfiguracionDetalle> listaUnidadMatConfDet = em.createQuery("SELECT umcd FROM UnidadMateriaConfiguracionDetalle umcd WHERE umcd.configuracion.configuracion =:configuracion ORDER BY umcd.criterio.tipo, umcd.indicador.nombre ASC", UnidadMateriaConfiguracionDetalle.class)
                    .setParameter("configuracion", dtoConfiguracionUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "Existe asignación de indicadores por criterio para la unidad seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la unidad seleccionada. (EjbAsistencias.buscarAsignacionIndicadoresUnidad)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe asignación de indicadores por criterio de carga académica seleccionada
     * @param dtoCargaAcademica Configuración de la que se buscará asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAsignadosIndicadoresCriterios>> buscarAsignacionIndicadoresCargaAcademica(DtoCargaAcademica dtoCargaAcademica){
        try {
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - entro");
            Boolean asigInd = compararUnidadesConfiguradasConTotales(dtoCargaAcademica);
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - valor " + asigInd);
            List<DtoAsignadosIndicadoresCriterios> listaUnidadMatConfDet = new ArrayList<>();
            if (asigInd == true) {

                List<UnidadMateriaConfiguracionDetalle> listaAsignaciones = em.createQuery("SELECT umcd FROM UnidadMateriaConfiguracionDetalle umcd WHERE umcd.configuracion.carga.carga =:cargaAcademica", UnidadMateriaConfiguracionDetalle.class)
                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                        .getResultList();

                //construir la lista de dto's para mostrar en tabla
                listaAsignaciones.forEach(asignacion -> {
                    
                    UnidadMateriaConfiguracion configuracion = em.find(UnidadMateriaConfiguracion.class, asignacion.getConfiguracion().getConfiguracion());
                    UnidadMateria unidadMateria = em.find(UnidadMateria.class, configuracion.getIdUnidadMateria().getIdUnidadMateria());
                    Criterio criterio = em.find(Criterio.class, asignacion.getCriterio().getCriterio());
                    Indicador indicador = em.find(Indicador.class, asignacion.getIndicador().getIndicador());
                    
                    DtoAsignadosIndicadoresCriterios dto = new DtoAsignadosIndicadoresCriterios(asignacion, configuracion, unidadMateria, criterio, indicador);
                    
                    listaUnidadMatConfDet.add(dto);

                });
                return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "Pase de lista y seguimiento por docente y tutor.");
            }
            else{
                return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "No existe Pase de lista y seguimiento por docente y tutor.");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo Pase de lista y seguimiento por docente y tutor. (EjbAsistencias.buscarAsignacionIndicadoresCargaAcademica)", e, null);
        }
    }
    
     /**
     * Permite comparar asignación de indicadores por unidad registradas con el total de unidades de la materia
     * @param dtoCargaAcademica Configuración de la que se buscará asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public Boolean compararUnidadesConfiguradasConTotales(DtoCargaAcademica dtoCargaAcademica)
    {
        try {
            List<UnidadMateriaConfiguracion> listaConfiguracion = em.createQuery("SELECT c FROM UnidadMateriaConfiguracion c WHERE c.carga.carga =:cargaAcademica", UnidadMateriaConfiguracion.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultList();
            List<UnidadMateriaConfiguracionDetalle> listaUnidadMatConfDet = em.createQuery("SELECT DISTINCT(cd.configuracion.configuracion) FROM UnidadMateriaConfiguracionDetalle cd WHERE cd.configuracion.carga.carga =:cargaAcademica", UnidadMateriaConfiguracionDetalle.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultList();

            if (listaConfiguracion.size() == listaUnidadMatConfDet.size()) {
                return true;
            } else {
                return false;
            }
        } catch (Throwable ex) {
            Logger.getLogger(EjbAsistencias.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

     /**
     * Permite eliminar la asignación de indicadores por criterio
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<Integer> eliminarAsignacionIndicadores(CargaAcademica cargaAcademica){
        try{
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "La carga académica no debe ser nula.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM UnidadMateriaConfiguracionDetalle umcd WHERE umcd.configuracion.carga.carga =:carga", UnidadMateriaConfiguracionDetalle.class)
                .setParameter("carga", cargaAcademica.getCarga())
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "La asignación de indicadores por criterio se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la asignación de indicadores por criterio. (EjbAsistencias.eliminarAsignacionIndicadores)", e, null);
        }
    }
    // Consulta para la generacion del formato de Planeacion Cuatrimestral
    /**
     * Permite verificar si existe asignación de indicadores por criterio de carga académica seleccionada
     * @param dtoCargaAcademica Configuración de la que se buscará asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaalumnosca>> buscarListaGrupos(DtoCargaAcademica dtoCargaAcademica) {
        try {
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - valor " + asigInd);
            List<Listaalumnosca> listaUnidadMatConfDet = new ArrayList<>();
            listaUnidadMatConfDet = em.createQuery("SELECT lac FROM Listaalumnosca lac WHERE lac.carga =:cargaAcademica AND lac.tipoEstudiante=:tipoEstudiante", Listaalumnosca.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .setParameter("tipoEstudiante", 1)
                    .getResultList();
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - listaConsulta " + listaUnidadMatConfDet.size());

//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - listaFinal " + listaUnidadMatConfDet.size());
            return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "Pase de lista y seguimiento por docente y tutor.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo Pase de lista y seguimiento por docente y tutor. (EjbAsistencias.buscarListaGrupos)", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoPaseLista>> agregarPaseLista(List<DtoPaseLista> dpls,Date d) {
        if (!dpls.isEmpty()) {
            Asistencias a = new Asistencias();
            a.setFechaHora(d);
            a.setTipoAsistencia("Calses");
            em.persist(a);
            dpls.forEach((t) -> {
                Asistenciasacademicas ac = new Asistenciasacademicas();
                ac.setAsistencia(a);
                ac.setEstudiante(t.getPorcInicio());
                ac.setCargaAcademica(t.getCargaAcademica());
                ac.setTipoAsistenciaA(t.getListaalumnosca().getAsistenciaT());
                em.persist(ac);
            });
        }
        return ResultadoEJB.crearCorrecto(dpls, "Pase de lista y seguimiento por docente y tutor.");
    }
    
    public ResultadoEJB<Asistenciasacademicas> actualizarPaseLista(Asistenciasacademicas asistenciasacademicas) {
        em.merge(asistenciasacademicas);
        return ResultadoEJB.crearCorrecto(asistenciasacademicas, "Pase de lista y seguimiento por docente y tutor.");
    }
    
    public ResultadoEJB<Asistencias> actualizarSesionesPaseLista(Asistencias asistencias) {
        em.merge(asistencias);
        return ResultadoEJB.crearCorrecto(asistencias, "Pase de lista y seguimiento por docente y tutor.");
    }

    public void eliminarPaseDeListaSesion(Asistencias asistencias) {
        List<Asistenciasacademicas> asistenciasacademicases = em.createQuery("SELECT u FROM Asistenciasacademicas u INNER JOIN u.asistencia asi WHERE asi.asistencia =:asistencia", Asistenciasacademicas.class)
                .setParameter("asistencia", asistencias.getAsistencia())
                .getResultList();
        if (!asistenciasacademicases.isEmpty()) {
            asistenciasacademicases.forEach((t) -> {
                Asistenciasacademicas a = em.find(Asistenciasacademicas.class, t.getAcademica());
                em.remove(a);
                em.flush();
            });
        }
        if (!em.contains(asistencias)) {
            asistencias = em.merge(asistencias);
        }

        em.remove(asistencias);
        em.flush();
    }

    public ResultadoEJB<Estudiante> buscaEstudiante(Integer matricula) {
        try {
            Estudiante e = new Estudiante();
            e = em.createQuery("SELECT es FROM Estudiante es WHERE es.matricula =:matricula", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .getResultList().get(0);
            return ResultadoEJB.crearCorrecto(e, "Pase de lista y seguimiento por docente y tutor.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo Pase de lista y seguimiento por docente y tutor. (EjbAsistencias.buscaEstudiante)", e, null);
        }
    }
    
    public ResultadoEJB<CargaAcademica> buscaCargaAcademica(Integer carga) {
        try {
            CargaAcademica c = new CargaAcademica();
            c = em.createQuery("SELECT ca FROM CargaAcademica ca WHERE ca.carga =:carga", CargaAcademica.class)
                    .setParameter("carga", carga)
                    .getResultList().get(0);
            return ResultadoEJB.crearCorrecto(c, "Pase de lista y seguimiento por docente y tutor.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo Pase de lista y seguimiento por docente y tutor. (EjbAsistencias.buscaCargaAcademica)", e, null);
        }
    }
    
    public ResultadoEJB<List<Asistenciasacademicas>> buscarAsistenciasacademicas(CargaAcademica ca,Integer matricula) {
        try {
            List<Asistenciasacademicas> as = new ArrayList<>();
            as = em.createQuery("SELECT ca FROM Asistenciasacademicas ca INNER JOIN ca.asistencia asis INNER JOIN ca.cargaAcademica cg INNER JOIN ca.estudiante es WHERE cg.carga=:carga AND es.matricula=:matricula", Asistenciasacademicas.class)
                    .setParameter("carga", ca.getCarga())
                    .setParameter("matricula", matricula)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(as, "Pase de lista y seguimiento por docente y tutor.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo Pase de lista y seguimiento por docente y tutor. (EjbAsistencias.buscarAsistenciasacademicas)", e, null);
        }
    }
    
    public ResultadoEJB<List<Asistenciasacademicas>> buscarAsistenciasacademicasFechasMes(CargaAcademica ca) {
        try {
            List<Asistenciasacademicas> as = new ArrayList<>();
            as = em.createQuery("SELECT ca FROM Asistenciasacademicas ca INNER JOIN ca.asistencia asis INNER JOIN ca.cargaAcademica cg WHERE cg.carga=:carga GROUP BY asis.fechaHora", Asistenciasacademicas.class)
                    .setParameter("carga", ca.getCarga())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(as, "Pase de lista y seguimiento por docente y tutor.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo Pase de lista y seguimiento por docente y tutor. (EjbAsistencias.buscarAsistenciasacademicasFechasMes)", e, null);
        }
    }
}
