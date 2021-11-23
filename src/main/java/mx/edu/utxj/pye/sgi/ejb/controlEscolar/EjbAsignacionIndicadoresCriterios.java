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
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaindicadoresporcriterioporconfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Reporteplaneacioncuatrimestralareaacademica;
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
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsigEvidenciasInstrumentosEval;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoAsignadosIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoInformePlaneaciones;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbAreasLogeo;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Informeplaneacioncuatrimestraldocenteprint;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Listaindicadoresporcriterioporconfiguracion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.view.Reporteplaneacioncuatrimestralareaacademica;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbAsignacionIndicadoresCriterios")
public class EjbAsignacionIndicadoresCriterios {
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    @EJB EjbEventoEscolar ejbEventoEscolar;
    @EJB EjbAreasLogeo ejbAreasLogeo;
    @Getter @Setter List<Reporteplaneacioncuatrimestralareaacademica> listaUnidadMatConfDet = new ArrayList<>();
    @Getter @Setter List<DtoInformePlaneaciones> planeacioneses = new ArrayList<>();
    @Getter @Setter Integer integradoras = 0;
    private EntityManager em;

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
     * Permite verificar si hay un periodo abierto para asignación de indicadores por criterio
     * @param docente Personal Docente que va a realizar la configuración, permite funcionar como filtro en caso de un permiso especifico a su área o a su clave
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.ASIGNACION_INDICADORES_CRITERIOS, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para asignación de indicadores por criterio del personal docente (EjbAsignacionIndicadoresCriterios.verificarEvento)", e, EventoEscolar.class);
        }
    }
    
    /**
     * Permite obtener la lista de periodos escolares a elegir en el apartado de asignación de indicadores por criterio
     * @param docente
     * @param periodoActivo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosCargaAcademica(PersonalActivo docente, Integer periodoActivo){
        try{
             List<Integer> claves = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.docente=:docente AND c.evento.periodo <=:periodo", CargaAcademica.class)
                .setParameter("docente", docente.getPersonal().getClave())
                .setParameter("periodo", periodoActivo)
                .getResultStream()
                .map(a -> a.getEvento().getPeriodo())
                .collect(Collectors.toList());
        
            if (claves.isEmpty()) {
                claves.add(0, ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.ASIGNACION_INDICADORES_CRITERIOS).getValor().getPeriodo());
            }
            List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p where p.periodo IN :periodos order by p.periodo desc", PeriodosEscolares.class)
                    .setParameter("periodos", claves)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionIndicadoresCriterios.getPeriodosDescendentes)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de periodos escolares a elegir en el apartado de asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
             List<Integer> claves = em.createQuery("SELECT c FROM CargaAcademica c", CargaAcademica.class)
                .getResultStream()
                .map(a -> a.getEvento().getPeriodo())
                .collect(Collectors.toList());
        
            if (claves.isEmpty()) {
                claves.add(0, ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.ASIGNACION_INDICADORES_CRITERIOS).getValor().getPeriodo());
            }
            List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p where p.periodo IN :periodos order by p.periodo desc", PeriodosEscolares.class)
                    .setParameter("periodos", claves)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionIndicadoresCriterios.getPeriodosDescendentes)", e, null);
        }
    }
    
    public PeriodosEscolares getPeriodoActual() {

        StoredProcedureQuery spq = f.getEntityManager().createStoredProcedureQuery("pye2.periodoEscolarActual", PeriodosEscolares.class);
        List<PeriodosEscolares> l = spq.getResultList();

        if (l == null || l.isEmpty()) {
            return new PeriodosEscolares();
        } else {
            return l.get(0);
        }
    
    }
    
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosTutor(PersonalActivo tuto){
        try{
             List<Integer> claves = em.createQuery("SELECT c FROM Grupo c WHERE c.tutor=:tutor", Grupo.class)
                .setParameter("tutor", tuto.getPersonal().getClave())
                .getResultStream()
                .map(a -> a.getPeriodo())
                .collect(Collectors.toList());
        
            if (claves.isEmpty()) {
                claves.add(0, ejbEventoEscolar.verificarEventoAperturado(EventoEscolarTipo.ASIGNACION_INDICADORES_CRITERIOS).getValor().getPeriodo());
            }
            List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p where p.periodo IN :periodos order by p.periodo desc", PeriodosEscolares.class)
                    .setParameter("periodos", claves)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbAsignacionIndicadoresCriterios.getPeriodosDescendentes)", e, null);
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
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);
            //buscar carga académica del personal docente logeado del periodo seleccionado
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT c FROM CargaAcademica c WHERE c.docente =:docente AND c.evento.periodo =:periodo AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbAsignacionIndicadoresCriterios.getCargaAcademicaPorDocente)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbAsignacionIndicadoresCriterios.pack).", e, DtoCargaAcademica.class);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbAsignacionIndicadoresCriterios.buscarConfiguracionUnidadMateria)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe asignación de indicadores por criterio de la carga académica seleccionada
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
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la materia del docente. (EjbAsignacionIndicadoresCriterios.getConfiguracionUnidadMateria)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores por criterio. (EjbAsignacionIndicadoresCriterios.getIndicadoresCriterioParaAsignar)", e, null);
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
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2, l, "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Ser")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "Lista de indicadores del criterio SER se obtuvo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores del criterio SER. (EjbAsignacionIndicadoresCriterios.getIndicadoresCriterioSer)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores del criterio SABER. (EjbAsignacionIndicadoresCriterios.getIndicadoresCriterioSaber)", e, null);
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
            List<Listaindicadoresporcriterioporconfiguracion> li = Collections.EMPTY_LIST;
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2, li, "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Saber hacer")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "Lista de indicadores del criterio SABER - HACER se obtuvo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores del criterio SABER - HACER. (EjbAsignacionIndicadoresCriterios.getIndicadoresCriterioSaberHacer)", e, null);
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
            if(listaSer == null || listaSer.isEmpty()) return ResultadoEJB.crearErroneo(2, li, "La lista de indicadores SER no debe ser nula.");
            
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SER. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSer)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SABER. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSaber)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SABER - HACER. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSaberHacer)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbAsignacionIndicadoresCriterios.getConfiguracionUnidadMateria)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la configuración de la unidad materia (EjbAsignacionIndicadoresCriterios.pack).", e, DtoConfiguracionUnidadMateria.class);
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
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la unidad seleccionada. (EjbAsignacionIndicadoresCriterios.buscarAsignacionIndicadoresUnidad)", e, null);
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
                return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "Asignación de indicadores por criterio de la carga académica seleccionada.");
            }
            else{
                return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "No existe asignación de indicadores por criterio de la carga académica seleccionada.");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la carga académica seleccionada. (EjbAsignacionIndicadoresCriterios.buscarAsignacionIndicadoresCargaAcademica)", e, null);
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
            Logger.getLogger(EjbAsignacionIndicadoresCriterios.class.getName()).log(Level.SEVERE, null, ex);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la asignación de indicadores por criterio. (EjbAsignacionIndicadoresCriterios.eliminarAsignacionIndicadores)", e, null);
        }
    }
    // Consulta para la generacion del formato de Planeacion Cuatrimestral
    /**
     * Permite verificar si existe asignación de indicadores por criterio de carga académica seleccionada
     * @param dtoCargaAcademica Configuración de la que se buscará asignación de indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Informeplaneacioncuatrimestraldocenteprint>> buscarInforme(DtoCargaAcademica dtoCargaAcademica) {
        try {
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - entro");
            Boolean asigInd = compararUnidadesConfiguradasConTotales(dtoCargaAcademica);
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - valor " + asigInd);
            List<Informeplaneacioncuatrimestraldocenteprint> listaUnidadMatConfDet = new ArrayList<>();
            if (asigInd == true) {
                listaUnidadMatConfDet = em.createQuery("SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.carga =:cargaAcademica", Informeplaneacioncuatrimestraldocenteprint.class)
                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                        .getResultList();
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - listaConsulta " + listaUnidadMatConfDet.size());
            } else {
                listaUnidadMatConfDet.clear();
                }
            if (dtoCargaAcademica.getCargaAcademica().getTareaIntegradora() != null && !listaUnidadMatConfDet.isEmpty()) {
                Informeplaneacioncuatrimestraldocenteprint i = new Informeplaneacioncuatrimestraldocenteprint();
                i.setConfiguracionDetalle(0);
                i.setCarga(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getCarga().getCarga());
                i.setConfiguracion(0);
                i.setUnidad(dtoCargaAcademica.getMateria().getUnidadMateriaList().size() + 1);
                i.setNombreUnidad("T.I.");
                i.setObjetivo(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getDescripcion());
                i.setFechaInicio(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getFechaEntrega());
                i.setFechaFin(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getFechaEntrega());
                i.setPorUnidad(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getPorcentaje());
                i.setIdCriterio(0);
                i.setCriterio("");
                i.setPorCriterio(0);
                i.setIdIndicador(0);
                i.setIndicador("");
                i.setPorcentaje(0);
                listaUnidadMatConfDet.add(i);

            }
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - listaFinal " + listaUnidadMatConfDet.size());
            return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "Asignación de indicadores por criterio de la carga académica seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la carga académica seleccionada. (EjbAsignacionIndicadoresCriterios.buscarAsignacionIndicadoresCargaAcademica)", e, null);
        }
    }
    
    public ResultadoEJB<List<DtoInformePlaneaciones>> buscarUnidadMateriaConfiguracionDetalle(DtoCargaAcademica dtoCargaAcademica,Integer periodo) {
        try {
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - entro");
            Boolean asigInd =Boolean.FALSE;
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - valor " + asigInd);
            planeacioneses = new ArrayList<>();
            if (periodo <= 56) {
                List<UnidadMateriaConfiguracionDetalle> configuracionDetalle = em.createQuery("SELECT i FROM UnidadMateriaConfiguracionDetalle i INNER JOIN i.configuracion c INNER JOIN c.carga ca WHERE ca.carga =:cargaAcademica ORDER BY i.criterio.criterio", UnidadMateriaConfiguracionDetalle.class)
                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                        .getResultList();
                if (!configuracionDetalle.isEmpty()) {
                    configuracionDetalle.forEach((t) -> {                        
                        DtoInformePlaneaciones dip = new DtoInformePlaneaciones();
                        dip.setConfiguracionDetalle(t.getConfiguracionDetalle());
                        dip.setCarga(dtoCargaAcademica.getCargaAcademica().getCarga());
                        dip.setConfiguracion(t.getConfiguracion().getConfiguracion());
                        dip.setUnidad(t.getConfiguracion().getIdUnidadMateria().getNoUnidad());
                        dip.setNombreUnidad(t.getConfiguracion().getIdUnidadMateria().getNombre());
                        dip.setObjetivo(t.getConfiguracion().getIdUnidadMateria().getObjetivo());
                        dip.setFechaInicio(t.getConfiguracion().getFechaInicio());
                        dip.setFechaFin(t.getConfiguracion().getFechaFin());
                        dip.setPorUnidad(t.getConfiguracion().getPorcentaje());
                        dip.setIdCriterio(t.getCriterio().getCriterio());
                        dip.setCriterio(t.getCriterio().getTipo());
                        dip.setPorCriterio(t.getCriterio().getPorcentajeRecomendado());
                        dip.setIdIndicador(t.getIndicador().getIndicador());
                        dip.setIndicador(t.getIndicador().getNombre());
                        dip.setPorcentaje(t.getPorcentaje());
                        dip.setMeta(0D);
                        dip.setEvidencia("");
                        if(t.getConfiguracion().getDirector()!=null){
                        dip.setValidadoD(Boolean.TRUE);
                        }else{
                        dip.setValidadoD(Boolean.FALSE);
                        }
                        planeacioneses.add(dip);
                    });
                }
            } else {
                List<UnidadMateriaConfiguracionEvidenciaInstrumento> evidenciaInstrumento = em.createQuery("SELECT i FROM UnidadMateriaConfiguracionEvidenciaInstrumento i INNER JOIN i.configuracion c INNER JOIN c.carga ca WHERE ca.carga =:cargaAcademica ORDER BY i.evidencia.criterio.criterio", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                        .getResultList();
                if (!evidenciaInstrumento.isEmpty()) {
                    evidenciaInstrumento.forEach((t) -> {
                        DtoInformePlaneaciones dip = new DtoInformePlaneaciones();
                        dip.setConfiguracionDetalle(t.getConfiguracionEvidenciaInstrumento());
                        dip.setCarga(dtoCargaAcademica.getCargaAcademica().getCarga());
                        dip.setConfiguracion(t.getConfiguracion().getConfiguracion());
                        dip.setUnidad(t.getConfiguracion().getIdUnidadMateria().getNoUnidad());
                        dip.setNombreUnidad(t.getConfiguracion().getIdUnidadMateria().getNombre());
                        dip.setObjetivo(t.getConfiguracion().getIdUnidadMateria().getObjetivo());
                        dip.setFechaInicio(t.getConfiguracion().getFechaInicio());
                        dip.setFechaFin(t.getConfiguracion().getFechaFin());
                        dip.setPorUnidad(t.getConfiguracion().getPorcentaje());
                        dip.setIdCriterio(t.getEvidencia().getCriterio().getCriterio());
                        dip.setCriterio(t.getEvidencia().getCriterio().getTipo());
                        dip.setPorCriterio(t.getEvidencia().getCriterio().getPorcentajeRecomendado());
                        dip.setIdIndicador(t.getInstrumento().getInstrumento());
                        dip.setIndicador(t.getInstrumento().getDescripcion());
                        dip.setPorcentaje(t.getPorcentaje());
                        dip.setMeta(t.getMetaInstrumento());
                        dip.setEvidencia(t.getEvidencia().getDescripcion());
                        if(t.getConfiguracion().getDirector()!=null){
                        dip.setValidadoD(Boolean.TRUE);
                        }else{
                        dip.setValidadoD(Boolean.FALSE);
                        }
                        planeacioneses.add(dip);
                    });
                }
            }
//            if (asigInd == true) {
//                listaUnidadMatConfDet = em.createQuery("SELECT i FROM Informeplaneacioncuatrimestraldocenteprint i WHERE i.carga =:cargaAcademica", Informeplaneacioncuatrimestraldocenteprint.class)
//                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
//                        .getResultList();
////            System.err.println("buscarAsignacionIndicadoresCargaAcademica - listaConsulta " + listaUnidadMatConfDet.size());
//            } else {
//                listaUnidadMatConfDet.clear();
//            }
            if (dtoCargaAcademica.getCargaAcademica().getTareaIntegradora() != null && !planeacioneses.isEmpty()) {
                DtoInformePlaneaciones dip = new DtoInformePlaneaciones();                
                dip.setConfiguracionDetalle(0L);
                dip.setCarga(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getCarga().getCarga());
                dip.setConfiguracion(0);
                dip.setUnidad(dtoCargaAcademica.getMateria().getUnidadMateriaList().size() + 1);
                dip.setNombreUnidad("T.I.");
                dip.setObjetivo(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getDescripcion());
                dip.setFechaInicio(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getFechaEntrega());
                dip.setFechaFin(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getFechaEntrega());
                dip.setPorUnidad(dtoCargaAcademica.getCargaAcademica().getTareaIntegradora().getPorcentaje());
                dip.setIdCriterio(0);
                dip.setCriterio("");
                dip.setPorCriterio(0D);
                dip.setIdIndicador(0);
                dip.setIndicador("");
                dip.setPorcentaje(0D);
                dip.setMeta(0D);
                dip.setEvidencia("");
                dip.setValidadoD(Boolean.TRUE);
                planeacioneses.add(dip);

            }
//            System.err.println("buscarAsignacionIndicadoresCargaAcademica - listaFinal " + planeacioneses.size());
            return ResultadoEJB.crearCorrecto(planeacioneses, "Asignación de indicadores por criterio de la carga académica seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la carga académica seleccionada. (EjbAsignacionIndicadoresCriterios.buscarUnidadMateriaConfiguracionDetalle)", e, null);
        }
    }

    public List<Reporteplaneacioncuatrimestralareaacademica> buscarReporteAreaAcademica(Short area) {
        try {
            List<AreasUniversidad> aus = new ArrayList<>();
            List<PlanEstudioMateria> pems = new ArrayList<>();
            List<CargaAcademica> academicas = new ArrayList<>();
            listaUnidadMatConfDet = new ArrayList<>();

            aus = ejbAreasLogeo.mostrarAreasUniversidadSubordinadas(area);

            if (aus.isEmpty()) {                return new ArrayList<>();            }

            aus.forEach((t) -> {
                List<PlanEstudioMateria> list = new ArrayList<>();
                list = em.createQuery("SELECT p FROM PlanEstudioMateria p INNER JOIN p.idPlan pe WHERE pe.idPe =:idPe ", PlanEstudioMateria.class)
                        .setParameter("idPe", t.getArea())
                        .getResultList();
                if (!list.isEmpty()) {
                    pems.addAll(list);
                }
                list.clear();
            });

            if (pems.isEmpty()) {                return new ArrayList<>();            }

            pems.forEach((p) -> {
                List<CargaAcademica> list = new ArrayList<>();
                list = em.createQuery("SELECT c FROM CargaAcademica c INNER JOIN c.idPlanMateria pe WHERE pe.idPlanMateria =:idPlanMateria ", CargaAcademica.class)
                        .setParameter("idPlanMateria", p.getIdPlanMateria())
                        .getResultList();
                if (!list.isEmpty()) {
                    academicas.addAll(list);
                }
                list.clear();
            });

            if (academicas.isEmpty()) {
                return new ArrayList<>();
            }

            academicas.forEach((c) -> {
                List<Reporteplaneacioncuatrimestralareaacademica> rs = new ArrayList<>();
                Reporteplaneacioncuatrimestralareaacademica r = new Reporteplaneacioncuatrimestralareaacademica();
                rs.clear();
                rs = em.createQuery("SELECT r FROM Reporteplaneacioncuatrimestralareaacademica r WHERE r.carga =:carga", Reporteplaneacioncuatrimestralareaacademica.class)
                        .setParameter("carga", c.getCarga())
                        .getResultList();
                if (!rs.isEmpty()) {
                    r = rs.get(0);
                    if (c.getTareaIntegradora() != null && !rs.isEmpty()) {
                        Reporteplaneacioncuatrimestralareaacademica i=new Reporteplaneacioncuatrimestralareaacademica();
                        i.setConfiguracionDetalle(integradoras);
                        i.setCarga(c.getCarga());
                        i.setDescripcion(c.getIdPlanMateria().getIdPlan().getDescripcion());
                        i.setDocente(r.getDocente());
                        i.setGrado(c.getCveGrupo().getGrado());
                        i.setLiteral(c.getCveGrupo().getLiteral());
                        i.setClaveMateria(c.getIdPlanMateria().getClaveMateria());                        
                        i.setMateria(c.getIdPlanMateria().getIdMateria().getNombre());
                        i.setNoUnidad(c.getIdPlanMateria().getIdMateria().getUnidadMateriaList().size() + 1);
                        i.setNombreUnidad("T.I.");
                        i.setObjetivo(c.getTareaIntegradora().getDescripcion());
                        i.setFechaInicio(c.getTareaIntegradora().getFechaEntrega());
                        i.setFechaFin(c.getTareaIntegradora().getFechaEntrega());
                        i.setPorUnidad(c.getTareaIntegradora().getPorcentaje());
                        i.setCriterio("");
                        i.setPorCriterio(0);
                        i.setIndicador("");
                        i.setPorcentajeInd(0);
                        i.setAreaSuperior(r.getAreaSuperior());
                        rs.add(i);
                        integradoras--;
                    }
                    listaUnidadMatConfDet.addAll(rs);
                    r = new Reporteplaneacioncuatrimestralareaacademica();
                    rs.clear();
                }

            });

            if (listaUnidadMatConfDet.isEmpty()) {
                return new ArrayList<>();
            } else {
                return listaUnidadMatConfDet;
            }
        } catch (Throwable e) {
            return new ArrayList<>();
        }
    }

    
    /**
     * Permite guardar la asignación de indicadores del criterio SER
     * (MASIVAMENTE)
     *
     * @param listaSer Lista de indicadores que se guardarán
     * @param listaUnidadMateriaConfiguracion Obtener la lista de claves de
     * configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSerMasiva(List<Listaindicadoresporcriterioporconfiguracion> listaSer, List<UnidadMateriaConfiguracion> listaUnidadMateriaConfiguracion) {
        try {
            if (listaSer == null || listaSer.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, ResultadoEJB.getListaTipo(Listaindicadoresporcriterioporconfiguracion.class),"La lista de indicadores SER no debe ser nula.");
            }

            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();

            listaSer.forEach(ser -> {
                try {
                    if (ser.getPorcentajeIndicador() != 0) {
                        listaUnidadMateriaConfiguracion.forEach(unidad -> {
                            UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                            UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, unidad.getConfiguracion());
                            umcd.setConfiguracion(umc);
                            Criterio criterio = em.find(Criterio.class, ser.getClaveCriterio());
                            umcd.setCriterio(criterio);
                            Indicador indicador = em.find(Indicador.class, ser.getClaveIndicador());
                            umcd.setIndicador(indicador);
                            umcd.setPorcentaje(ser.getPorcentajeIndicador());
                            em.persist(umcd);
//                            em.persist(umcd);
                            Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                            l.add(vistaLista);
                        });
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La asignación de indicadores del criterio SER se guardo correctamente.");
        } catch (Throwable e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SER. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSerMasiva)", e, null);
        }
    }

    /**
     * Permite guardar la asignación de indicadores del criterio SABER
     * (MASIVAMENTE)
     *
     * @param listaSaber Lista de indicadores que se guardarán
     * @param listaUnidadMateriaConfiguracion Obtener la lista de claves de
     * configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaberMasiva(List<Listaindicadoresporcriterioporconfiguracion> listaSaber, List<UnidadMateriaConfiguracion> listaUnidadMateriaConfiguracion) {
        try {
            if (listaSaber == null || listaSaber.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, ResultadoEJB.getListaTipo(Listaindicadoresporcriterioporconfiguracion.class), "La configuración de la unidad materia no debe ser nula.");
            }

            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();

            listaSaber.forEach(saber -> {
                try {
                    if (saber.getPorcentajeIndicador() != 0) {
                        listaUnidadMateriaConfiguracion.forEach(unidad -> {
                            UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                            UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, unidad.getConfiguracion());
                            umcd.setConfiguracion(umc);
                            Criterio criterio = em.find(Criterio.class, saber.getClaveCriterio());
                            umcd.setCriterio(criterio);
                            Indicador indicador = em.find(Indicador.class, saber.getClaveIndicador());
                            umcd.setIndicador(indicador);
                            umcd.setPorcentaje(saber.getPorcentajeIndicador());
                            em.persist(umcd);
//                            em.persist(umcd);
                            Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                            l.add(vistaLista);
                        });
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La asignación de indicadores del criterio SABER se guardo correctamente.");
        } catch (Throwable e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SABER. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSaberMasiva)", e, null);
        }
    }

    /**
     * Permite guardar la asignación de indicadores del criterio SABER - HACER
     * (MASIVAMENTE)
     *
     * @param listaSaberHacer Lista de indicadores que se guardarán
     * @param listaUnidadMateriaConfiguracion Obtener la lista de claves de
     * configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaberHacerMasiva(List<Listaindicadoresporcriterioporconfiguracion> listaSaberHacer, List<UnidadMateriaConfiguracion> listaUnidadMateriaConfiguracion) {
        try {
            if (listaSaberHacer == null || listaSaberHacer.isEmpty()) {
                return ResultadoEJB.crearErroneo(2, ResultadoEJB.getListaTipo(Listaindicadoresporcriterioporconfiguracion.class), "La configuración de la unidad materia no debe ser nula.");
            }

            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();

            listaSaberHacer.forEach(sabhac -> {
                try {
                    if (sabhac.getPorcentajeIndicador() != 0) {
                        listaUnidadMateriaConfiguracion.forEach(unidad -> {
                            UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                            UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, unidad.getConfiguracion());
                            umcd.setConfiguracion(umc);
                            Criterio criterio = em.find(Criterio.class, sabhac.getClaveCriterio());
                            umcd.setCriterio(criterio);
                            Indicador indicador = em.find(Indicador.class, sabhac.getClaveIndicador());
                            umcd.setIndicador(indicador);
                            umcd.setPorcentaje(sabhac.getPorcentajeIndicador());
                            em.persist(umcd);
//                            em.persist(umcd);
                            Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                            l.add(vistaLista);
                        });
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La asignación de indicadores del criterio SABER - HACER se guardo correctamente.");
        } catch (Throwable e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la asignación del criterio SABER - HACER. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSaberHacerMasiva)", e, null);
        }
    }
    
    public ResultadoEJB<EventoEscolar> verificarEventoValidacion(PersonalActivo director){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.VALIDACION_ASIGNACION_INDICADORES_CRITERIOS, director);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para asignación de indicadores por criterio del personal docente (EjbAsignacionIndicadoresCriterios.verificarEvento)", e, EventoEscolar.class);
        }
    }
    
    public void validadConfigunracionUnidad(Integer confiInteger, Boolean validado, Integer personaV) {

        UnidadMateriaConfiguracion configuracion = em.createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.configuracion =:configuracion", UnidadMateriaConfiguracion.class)
                .setParameter("configuracion", confiInteger)
                .getResultList().get(0);
        if (!validado) {
            configuracion.setDirector(null);
        } else {
            configuracion.setDirector(personaV);
        }
        em.merge(configuracion);
        f.flush();
    }
    
    /**
     * Permite verificar si la configuracion se encuentra validada por el director de carrera
     * @param cargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<UnidadMateriaConfiguracion> verificarValidacionConfiguracion(CargaAcademica cargaAcademica){
        try {
            UnidadMateriaConfiguracion unidadMateriaConfiguracion = new UnidadMateriaConfiguracion();
            
            List<UnidadMateriaConfiguracion> listaConfiguracion = em.createQuery("SELECT u FROM UnidadMateriaConfiguracion u WHERE u.carga.carga =:carga", UnidadMateriaConfiguracion.class)
                    .setParameter("carga", cargaAcademica.getCarga())
                    .getResultList();
            
            if(listaConfiguracion.size() >0 && !listaConfiguracion.isEmpty()){
            
                unidadMateriaConfiguracion = listaConfiguracion.get(0);
            } else{
            
                unidadMateriaConfiguracion = null;
            }
            return ResultadoEJB.crearCorrecto(unidadMateriaConfiguracion, "Configuración encontrada de la materia seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la configuracioón de la materia seleccionada. (EjbAsignacionIndicadoresCriterios.verificarValidacionConfiguracion)", e, null);
        }
       
    }
    
    // MÉTODOS PARA ASIGNACIÓN DE EVIDENCIAS E INSTRUMENTOS DE EVALUACIÓN
    
    /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación que deberá de registrar de manera obligatoria
     * @param dtoCargaAcademica Materia de la que se buscará la lista de evaluación sugerida por unidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAsigEvidenciasInstrumentosEval>> getEvidenciasInstrumentosSugeridos(DtoCargaAcademica dtoCargaAcademica){
        try {
            
            List<UnidadMateriaConfiguracion> listaUnidadesConfiguradas = buscarConfiguracionUnidadMateria(dtoCargaAcademica).getValor();
            
            List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasInstrumentos = new ArrayList<>();
            
            if(!listaUnidadesConfiguradas.isEmpty()){
                
              Boolean categoriasIncompletas = getCategoriasIncompletasMateria(listaUnidadesConfiguradas).getValor();
                
              if(!categoriasIncompletas){
                 
                listaUnidadesConfiguradas.forEach(unidadConfigurada -> {
                
                    List<EvaluacionSugerida> listaEvaluacionesSugeridas = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idUnidadMateria=:unidad AND e.activo=:valor", EvaluacionSugerida.class)
                            .setParameter("unidad", unidadConfigurada.getIdUnidadMateria().getIdUnidadMateria())
                            .setParameter("valor", Boolean.TRUE)
                            .getResultStream()
                            .collect(Collectors.toList());
                
                        listaEvaluacionesSugeridas.forEach(evaluacionSug -> {
                            Integer valorPorcentual = 0;
                            DtoAsigEvidenciasInstrumentosEval dtoAsigEvidenciasInstrumentosEval = new DtoAsigEvidenciasInstrumentosEval(unidadConfigurada, evaluacionSug.getEvidencia(), evaluacionSug.getInstrumento(), valorPorcentual, evaluacionSug.getMetaInstrumento(), Boolean.TRUE);
                            listaEvidenciasInstrumentos.add(dtoAsigEvidenciasInstrumentosEval);
                        });
                });
              }
            }
            return ResultadoEJB.crearCorrecto(listaEvidenciasInstrumentos, "Lista de evidencias e instrumentos de evaluación sugeridos.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias e instrumentos de evaluación sugeridos. (EjbAsignacionIndicadoresCriterios.getEvidenciasInstrumentosSugeridos)", e, null);
        }
    }
    
    /**
     * Permite verificar si la evaluación sugerida de la materia está incompleta
     * @param listaUnidadesConfiguradas
     * @return Resultado del proceso
     */
    public ResultadoEJB<Boolean> getCategoriasIncompletasMateria(List<UnidadMateriaConfiguracion> listaUnidadesConfiguradas){
        try {
            Boolean categoriasIncompletas = false;
            
            List<Integer> unidadesIncompletas = new ArrayList<>();
            
            List<Integer> unidadesMateria = listaUnidadesConfiguradas.stream().map(p->p.getIdUnidadMateria().getIdUnidadMateria()).collect(Collectors.toList());
            
            List<EvaluacionSugerida> evaluacionesSugeridas = em.createQuery("SELECT e FROM EvaluacionSugerida e WHERE e.unidadMateria.idUnidadMateria IN :unidades AND e.activo=:valor", EvaluacionSugerida.class)
                            .setParameter("unidades", unidadesMateria)
                            .setParameter("valor", Boolean.TRUE)
                            .getResultStream()
                            .collect(Collectors.toList());
            
            evaluacionesSugeridas.forEach(evaluacion -> {
                List<String> conteoCriterios = evaluacionesSugeridas.stream().filter(p->Objects.equals(p.getUnidadMateria().getIdUnidadMateria(), evaluacion.getUnidadMateria().getIdUnidadMateria())).map(p->p.getEvidencia().getCriterio().getTipo()).distinct().collect(Collectors.toList());
                
                if(conteoCriterios.size() < 3){
                   unidadesIncompletas.add(evaluacion.getUnidadMateria().getIdUnidadMateria());
                }
            });
            
            if(!unidadesIncompletas.isEmpty()){
                categoriasIncompletas = true;
            }
            
            return ResultadoEJB.crearCorrecto(categoriasIncompletas, "Se verificó si la evaluación sugerida de la materia está incompleta.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si la evaluación sugerida de la materia está incompleta. (EjbAsignacionIndicadoresCriterios.getCategoriasIncompletasMateria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación que deberá de registrar de manera obligatoria
     * @param dtoCargaAcademica Materia de la que se buscará la lista de evaluación sugerida por unidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateriaConfiguracionEvidenciaInstrumento>> buscarAsignacionEvidenciasInstrumentos(DtoCargaAcademica dtoCargaAcademica){
        try {
            
            List<UnidadMateriaConfiguracion> listaUnidadesConfiguradas = buscarConfiguracionUnidadMateria(dtoCargaAcademica).getValor();
            
            List<UnidadMateriaConfiguracionEvidenciaInstrumento>  listaEvidenciasInstrumentos = new ArrayList<>();
            
            if(!listaUnidadesConfiguradas.isEmpty()){
           
            listaEvidenciasInstrumentos = em.createQuery("SELECT u FROM UnidadMateriaConfiguracionEvidenciaInstrumento u WHERE u.configuracion  IN :lista ORDER BY u.configuracion.idUnidadMateria.noUnidad, u.evidencia.criterio.criterio, u.evidencia.descripcion ASC", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                    .setParameter("lista", listaUnidadesConfiguradas)
                    .getResultStream()
                    .collect(Collectors.toList());
            }
            
            return ResultadoEJB.crearCorrecto(listaEvidenciasInstrumentos, "Lista de evidencias e instrumentos de evaluación registrados.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias e instrumentos de evaluación registrados. (EjbAsignacionIndicadoresCriterios.buscarAsignacionEvidenciasInstrumentos)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación que deberá de registrar de manera obligatoria
     * @param dtoCargaAcademica Materia de la que se buscará la lista de evaluación sugerida por unidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Criterio>> getCategoriasNivel(DtoCargaAcademica dtoCargaAcademica){
        try {
            
            List<Criterio> listaCategoriasNivel = em.createQuery("SELECT c FROM Criterio c WHERE c.nivel=:nivel ORDER BY c.criterio ASC", Criterio.class)
                    .setParameter("nivel", dtoCargaAcademica.getPrograma().getNivelEducativo().getNivel())
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaCategoriasNivel, "Lista de categorías de evaluación del nivel educativo que corresponde.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de categorías de evaluación del nivel educativo que corresponde. (EjbAsignacionIndicadoresCriterios.getCategoriasNivel)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación que deberá de registrar de manera obligatoria
     * @param categoria Materia de la que se buscará la lista de evaluación sugerida por unidad
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EvidenciaEvaluacion>> getEvidenciasCategoria(Criterio categoria){
        try {
            
            List<EvidenciaEvaluacion> listaEvidenciasCategoria = em.createQuery("SELECT e FROM EvidenciaEvaluacion e WHERE e.criterio.criterio=:criterio AND e.activo=:valor ORDER BY e.descripcion ASC", EvidenciaEvaluacion.class)
                    .setParameter("criterio", categoria.getCriterio())
                    .setParameter("valor", Boolean.TRUE)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEvidenciasCategoria, "Lista de evidencias de evaluación de la categoría correspondiente.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias de evaluación de la categoría correspondiente. (EjbAsignacionIndicadoresCriterios.getEvidenciasCategoria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de evidencias e instrumentos de evaluación que deberá de registrar de manera obligatoria
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<InstrumentoEvaluacion>> getInstrumentosEvaluacion(){
        try {
            
            List<InstrumentoEvaluacion> listaInstrumentoEvaluacion = em.createQuery("SELECT i FROM InstrumentoEvaluacion i WHERE i.activo=:valor ORDER BY i.descripcion ASC", InstrumentoEvaluacion.class)
                    .setParameter("valor", Boolean.TRUE)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaInstrumentoEvaluacion, "Lista de instrumentos de evaluación activos.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de instrumentos de evaluación activos. (EjbAsignacionIndicadoresCriterios.getInstrumentosEvaluacion)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe evento activo de la actividad y usuario seleccionado
     * @param listaEvidenciasSugeridas
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEvidenciaListaSugerida(List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasSugeridas, EvidenciaEvaluacion evidencia){
        try{
           List<DtoAsigEvidenciasInstrumentosEval> listaCoincidencias = listaEvidenciasSugeridas.stream().filter(p-> Objects.equals(p.getEvidenciaEvaluacion().getEvidencia(), evidencia.getEvidencia())).collect(Collectors.toList());
           
           return ResultadoEJB.crearCorrecto(listaCoincidencias.isEmpty(), "Resultado búsqueda de evidencia en el listado de evidencias sugeridas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existe evento activo de la actividad y usuario seleccionado
     * @param dtoConfiguracionUnidadMateria
     * @param listaEvidenciasSugeridas
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEvidenciaUnidadListaSugerida(DtoConfiguracionUnidadMateria dtoConfiguracionUnidadMateria, List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasSugeridas, EvidenciaEvaluacion evidencia){
        try{
           List<DtoAsigEvidenciasInstrumentosEval> listaCoincidencias = listaEvidenciasSugeridas.stream().filter(p-> Objects.equals(p.getUnidadMateriaConfiguracion().getIdUnidadMateria().getIdUnidadMateria(), dtoConfiguracionUnidadMateria.getUnidadMateria().getIdUnidadMateria()) && p.getEvidenciaEvaluacion().getEvidencia() == evidencia.getEvidencia()).collect(Collectors.toList());
           
           return ResultadoEJB.crearCorrecto(listaCoincidencias.isEmpty(), "Resultado búsqueda de evidencia en la unidad seleccionada en el listado de evidencias sugeridas");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite verificar si existe evento activo de la actividad y usuario seleccionado
     * @param listaEvidenciasSugeridas
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<List<DtoAsigEvidenciasInstrumentosEval>> agregarEvidenciaListaSugerida(List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasSugeridas, EvidenciaEvaluacion evidencia, InstrumentoEvaluacion instrumento, Integer metaInstrumento){
        try{
           List<UnidadMateriaConfiguracion> listaUnidadesConf = listaEvidenciasSugeridas.stream().map(p->p.getUnidadMateriaConfiguracion()).distinct().collect(Collectors.toList());
           
           listaUnidadesConf.forEach(unidadConfigurada -> {
                    Integer valorPorcentual =0;
                    DtoAsigEvidenciasInstrumentosEval dtoAsigEvidenciasInstrumentosEval = new DtoAsigEvidenciasInstrumentosEval(unidadConfigurada, evidencia, instrumento, valorPorcentual, metaInstrumento, Boolean.FALSE);
                    listaEvidenciasSugeridas.add(dtoAsigEvidenciasInstrumentosEval);
                });
           
            return ResultadoEJB.crearCorrecto(listaEvidenciasSugeridas.stream().sorted(DtoAsigEvidenciasInstrumentosEval::compareTo).collect(Collectors.toList()), "Lista de evidencias sugeridas con nueva evidencia agregada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias sugeridas con nueva evidencia agregada. (EjbAsignacionIndicadoresCriterios.agregarEvidenciaListaSugerida)", e, null);
        }
    }
    
    /**
     * Permite verificar si existe evento activo de la actividad y usuario seleccionado
     * @param listaEvidenciasSugeridas
     * @param evidencia
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<List<DtoAsigEvidenciasInstrumentosEval>> agregarEvidenciaUnidadListaSugerida(List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasSugeridas, DtoConfiguracionUnidadMateria dtoConfiguracionUnidadMateria, EvidenciaEvaluacion evidencia, InstrumentoEvaluacion instrumento, Integer metaInstrumento){
        try{
           UnidadMateriaConfiguracion unidadesConf = listaEvidenciasSugeridas.stream().filter(p->p.getUnidadMateriaConfiguracion().getIdUnidadMateria().getIdUnidadMateria().equals(dtoConfiguracionUnidadMateria.getUnidadMateria().getIdUnidadMateria())).map(p->p.getUnidadMateriaConfiguracion()).findFirst().orElse(null);
          
            Integer valorPorcentual = 0;
            DtoAsigEvidenciasInstrumentosEval dtoAsigEvidenciasInstrumentosEval = new DtoAsigEvidenciasInstrumentosEval(unidadesConf, evidencia, instrumento, valorPorcentual, metaInstrumento, Boolean.FALSE);
            listaEvidenciasSugeridas.add(dtoAsigEvidenciasInstrumentosEval);
             
            return ResultadoEJB.crearCorrecto(listaEvidenciasSugeridas.stream().sorted(DtoAsigEvidenciasInstrumentosEval::compareTo).collect(Collectors.toList()), "Lista de evidencias sugeridas con nueva evidencia agregada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias sugeridas con nueva evidencia agregada. (EjbAsignacionIndicadoresCriterios.agregarEvidenciaListaSugerida)", e, null);
        }
    }
    
    /**
     * Permite eliminar evidencia e instrumento de evaluación que no es obligatorio de lista sugerida
     * @param listaEvidenciasSugeridas
     * @param dtoAsigEvidenciasInstrumentosEval
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoAsigEvidenciasInstrumentosEval>> eliminarEvidenciaUnidadListaSugerida(List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasSugeridas, DtoAsigEvidenciasInstrumentosEval dtoAsigEvidenciasInstrumentosEval){
        try{
            listaEvidenciasSugeridas.remove(dtoAsigEvidenciasInstrumentosEval);
             
            return ResultadoEJB.crearCorrecto(listaEvidenciasSugeridas.stream().sorted(DtoAsigEvidenciasInstrumentosEval::compareTo).collect(Collectors.toList()), "Lista de evidencias sugeridas sin evidencia seleccionada.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de evidencias sugeridas sin evidencia seleccionada. (EjbAsignacionIndicadoresCriterios.eliminarEvidenciaUnidadListaSugerida)", e, null);
        }
    }
    
     /**
     * Permite guardar la asignación de indicadores del criterio SABER
     * @param listaEvidenciasInstrumentos Lista de indicadores que se guardarán
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateriaConfiguracionEvidenciaInstrumento>> guardarListaEvidenciasInstrumentos(List<DtoAsigEvidenciasInstrumentosEval> listaEvidenciasInstrumentos){
        try{
            List<UnidadMateriaConfiguracionEvidenciaInstrumento> li = new ArrayList<>();
            
            if(listaEvidenciasInstrumentos == null || listaEvidenciasInstrumentos.isEmpty()) return ResultadoEJB.crearErroneo(2, li, "La lista de evidencias e instrumentos de evaluación no puede ser nula o vacía.");
          
            List<UnidadMateriaConfiguracionEvidenciaInstrumento> l = new ArrayList<>();
           
            listaEvidenciasInstrumentos.forEach(evidenciaInstrumento -> {
                try {
                    if(evidenciaInstrumento.getValorPorcentual()!= 0){
                    UnidadMateriaConfiguracionEvidenciaInstrumento umcei = new UnidadMateriaConfiguracionEvidenciaInstrumento();
                    UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, evidenciaInstrumento.getUnidadMateriaConfiguracion().getConfiguracion());
                    umcei.setConfiguracion(umc);
                    umcei.setEvidencia(evidenciaInstrumento.getEvidenciaEvaluacion());
                    umcei.setInstrumento(evidenciaInstrumento.getInstrumentoEvaluacion());
                    umcei.setPorcentaje(evidenciaInstrumento.getValorPorcentual());
                    umcei.setMetaInstrumento(evidenciaInstrumento.getMetaInstrumento());
                    em.persist(umcei);
                    l.add(umcei);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbAsignacionIndicadoresCriterios.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "El registro de evidencias e instrumentos de evaluación se ha guardado correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar las evidencias e instrumentos de evaluación se ha guardado correctamente. (EjbAsignacionIndicadoresCriterios.guardarListaEvidenciasInstrumentos)", e, null);
        }
    }
    
    /**
     * Permite eliminar la asignación de indicadores por criterio
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<Integer> eliminarEvidenciasInstrumentos(CargaAcademica cargaAcademica){
        try{
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "La carga académica no debe ser nula.", Integer.TYPE);

            Integer delete = em.createQuery("DELETE FROM UnidadMateriaConfiguracionEvidenciaInstrumento umcei WHERE umcei.configuracion.carga.carga=:carga", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                .setParameter("carga", cargaAcademica.getCarga())
                .executeUpdate();

            return ResultadoEJB.crearCorrecto(delete, "Se han eliminado correctamente las evidencias e instrumentos de evaluación registrados.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se eliminaron las evidencias e instrumentos de evaluación registrados. (EjbAsignacionIndicadoresCriterios.eliminarEvidenciasInstrumentos)", e, null);
        }
    }
    
    
    public ResultadoEJB<List<DtoCargaAcademica>> getCargaAcademicasPorGrupo(Grupo g, PeriodosEscolares periodo){
        try{
            List<Integer> grados = new ArrayList<>();
            grados.add(6);
            grados.add(11);
            //buscar carga académica del personal docente logeado del periodo seleccionado
            List<DtoCargaAcademica> cargas = em.createQuery("SELECT c FROM CargaAcademica c INNER JOIN c.cveGrupo g WHERE g.idGrupo =:idGrupo AND c.evento.periodo =:periodo AND c.cveGrupo.grado NOT IN :grados", CargaAcademica.class)
                    .setParameter("idGrupo", g.getIdGrupo())
                    .setParameter("periodo", periodo.getPeriodo())
                    .setParameter("grados", grados)
                    .getResultStream()
                    .distinct()
                    .map(cargaAcademica -> pack(cargaAcademica))
                    .filter(res -> res.getCorrecto())
                    .map(ResultadoEJB::getValor)
                    .sorted(DtoCargaAcademica::compareTo)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(cargas, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbAsignacionIndicadoresCriterios.getCargaAcademicaPorDocente)", e, null);
        }
    }
    
    public ResultadoEJB<Boolean> getConparativoConfiguracionPlaneacionSugerida(CargaAcademica ca){
        try{
            Boolean coincide= Boolean.FALSE;
            
            List<UnidadMateriaConfiguracionDetalle> detalles = em.createQuery("SELECT c FROM UnidadMateriaConfiguracionDetalle c INNER JOIN c.configuracion g INNER JOIN g.carga ca WHERE ca.carga =:carga", UnidadMateriaConfiguracionDetalle.class)
                    .setParameter("carga", ca.getCarga()).getResultList();
            
            List<UnidadMateriaConfiguracionEvidenciaInstrumento> instrumentos = em.createQuery("SELECT c FROM UnidadMateriaConfiguracionEvidenciaInstrumento c INNER JOIN c.configuracion g INNER JOIN g.carga ca WHERE ca.carga =:carga", UnidadMateriaConfiguracionEvidenciaInstrumento.class)
                    .setParameter("carga", ca.getCarga()).getResultList();
            
            List<EvaluacionSugerida> sugeridas = em.createQuery("SELECT c FROM EvaluacionSugerida c INNER JOIN c.unidadMateria g INNER JOIN g.idMateria ca WHERE ca.idMateria =:idMateria", EvaluacionSugerida.class)
                    .setParameter("idMateria", ca.getIdPlanMateria().getIdMateria().getIdMateria()).getResultList();
            
            if (detalles.size() >= sugeridas.size()) {
                coincide = Boolean.TRUE;
            } else if (instrumentos.size() >= sugeridas.size()) {
                coincide = Boolean.TRUE;
            } else {
                coincide = Boolean.FALSE;
            }
            
            return ResultadoEJB.crearCorrecto(coincide, "Lista de cargas académicas por docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbAsignacionIndicadoresCriterios.getCargaAcademicaPorDocente)", e, null);
        }
    }
}
