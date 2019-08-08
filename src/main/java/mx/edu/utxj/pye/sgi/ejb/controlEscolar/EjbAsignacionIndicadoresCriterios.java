/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import com.sun.javafx.scene.control.skin.VirtualFlow;
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
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Listaindicadoresporcriterioporconfiguracion;
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
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.ACTIIVIDAD.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalDocenteActividad").orElse(3)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal docente.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal docente no se pudo validar. (EjbAsignacionIndicadoresCriterios.validarDocente)", e, null);
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
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
            final List<PeriodosEscolares> periodos = em.createQuery("select p from PeriodosEscolares p order by p.periodo desc", PeriodosEscolares.class)
                    .getResultList();
            
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
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbAsignacionIndicadoresCriterios. pack).", e, DtoCargaAcademica.class);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbAsignacionIndicadoresCriterios.getConfiguracionUnidadMateria)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe asignación de indicadores por criterio de la carga académica seleccionada
     * @param dtoCargaAcademica Configuración de la que se buscará asignación de indicadores por criterio
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
     * @param dtoCargaAcademica Nivel del que se obtendrán los indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioParaAsignar(DtoCargaAcademica dtoCargaAcademica){ 
        try {
            Integer periodo = getPeriodoActivoIndicadores(dtoCargaAcademica); 
            Integer configuracion = getConfiguracion(dtoCargaAcademica); 
            List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores = em.createQuery("SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.cargaAcademica =:cargaAcademica AND l.listaindicadoresporcriterioporconfiguracionPK.periodo =:periodo AND l.listaindicadoresporcriterioporconfiguracionPK.configuracion =:configuracion", Listaindicadoresporcriterioporconfiguracion.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .setParameter("periodo", periodo)
                    .setParameter("configuracion", configuracion)
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaIndicadores, "Lista de indicadores por criterio.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de indicadores por criterio. (EjbAsignacionIndicadoresCriterios.getIndicadoresCriterioParaAsignar)", e, null);
        }
    }
    
    public Integer getPeriodoActivoIndicadores(DtoCargaAcademica dtoCargaAcademica)
    {
        int periodo = 0;
        TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(l.listaindicadoresporcriterioporconfiguracionPK.periodo) FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.cargaAcademica =:cargaAcademica")
                                                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga());
        if (v.getSingleResult() == 0) {
            periodo = 0;
        } else {
            periodo = v.getSingleResult();
        }
        return periodo;
    }
    
    public Integer getConfiguracion(DtoCargaAcademica dtoCargaAcademica)
    {
        int configuracion = 0;
        TypedQuery<Integer> v = (TypedQuery<Integer>) em.createQuery("SELECT MAX(l.listaindicadoresporcriterioporconfiguracionPK.configuracion) FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.cargaAcademica =:cargaAcademica")
                                                        .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga());
        
        if (v.getSingleResult() == 0) {
            configuracion = 0;
        } else {
            configuracion = v.getSingleResult();
        }
        return configuracion;
    }
    
    /**
     * Permite obtener la lista de indicadores por criterio de la materia para realizar la asignación posteriormente
     * @param listaIndicadores Nivel del que se obtendrán los indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioSer(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores){ 
        try{ 
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2, "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Ser")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbAsignacionIndicadoresCriterios.guardarConfUnidadMateria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de indicadores por criterio de la materia para realizar la asignación posteriormente
     * @param listaIndicadoresSer Nivel del que se obtendrán los indicadores por criterio
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
     * Permite obtener la lista de indicadores por criterio de la materia para realizar la asignación posteriormente
     * @param listaIndicadores Nivel del que se obtendrán los indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioSaber(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores){ 
        try{ 
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2, "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Saber")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbAsignacionIndicadoresCriterios.guardarConfUnidadMateria)", e, null);
        }
    }
    
     /**
     * Permite obtener la lista de indicadores por criterio de la materia para realizar la asignación posteriormente
     * @param listaIndicadoresSaber Nivel del que se obtendrán los indicadores por criterio
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
     * Permite obtener la lista de indicadores por criterio de la materia para realizar la asignación posteriormente
     * @param listaIndicadores Nivel del que se obtendrán los indicadores por criterio
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> getIndicadoresCriterioSaberHacer(List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores){ 
        try{ 
            if(listaIndicadores == null || listaIndicadores.isEmpty()) return ResultadoEJB.crearErroneo(2, "La lista de indicadores por criterio no debe ser nula.");
          
            Iterator<Listaindicadoresporcriterioporconfiguracion> it = listaIndicadores.iterator();

            while (it.hasNext()) {
                if (!it.next().getCriterio().equals("Saber hacer")) {
                    it.remove();
                }
            }
            return ResultadoEJB.crearCorrecto(listaIndicadores, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbAsignacionIndicadoresCriterios.guardarConfUnidadMateria)", e, null);
        }
    }
    
      /**
     * Permite obtener la lista de indicadores por criterio de la materia para realizar la asignación posteriormente
     * @param listaIndicadoresSaber Nivel del que se obtendrán los indicadores por criterio
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
    
    public Integer validarSumaPorcentajesIndicadores(List<Listaindicadoresporcriterioporconfiguracion> lista)
    {
            Integer valor = 0;
            
            Double totalPorcentajes = lista.stream().mapToDouble(Listaindicadoresporcriterioporconfiguracion::getPorcentajeIndicador).sum();
            
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
     * Permite guardar la configuración de la unidad materia
     * @param listaSer Unidad configuracion materia que se va a guardar
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSer(List<Listaindicadoresporcriterioporconfiguracion> listaSer, DtoConfiguracionUnidadMateria dtoConfUnidadMateria){
        try{            
            if(listaSer == null || listaSer.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
            
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
            
            eliminarConfUnidDetalles(dtoConfUnidadMateria);
                   
            listaSer.forEach(ser -> {
                try {
                    if(ser.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, dtoConfUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = em.find(Criterio.class, ser.getListaindicadoresporcriterioporconfiguracionPK().getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = em.find(Indicador.class, ser.getListaindicadoresporcriterioporconfiguracionPK().getClaveIndicador());
                    umcd.setIndicador(indicador);
                    umcd.setPorcentaje(ser.getPorcentajeIndicador());
                    f.create(umcd);
                    Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                    l.add(vistaLista);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSer)", e, null);
        }
    }
    
     /**
     * Permite guardar la configuración de la unidad materia
     * @param listaSaber Unidad configuracion materia que se va a guardar
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaber(List<Listaindicadoresporcriterioporconfiguracion> listaSaber, DtoConfiguracionUnidadMateria dtoConfUnidadMateria){
        try{            
            if(listaSaber == null || listaSaber.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
          
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
           
            listaSaber.forEach(saber -> {
                try {
                    if(saber.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, dtoConfUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = em.find(Criterio.class, saber.getListaindicadoresporcriterioporconfiguracionPK().getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = em.find(Indicador.class, saber.getListaindicadoresporcriterioporconfiguracionPK().getClaveIndicador());
                    umcd.setIndicador(indicador);
                    umcd.setPorcentaje(saber.getPorcentajeIndicador());
                    f.create(umcd);
                    Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                    l.add(vistaLista);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSaber)", e, null);
        }
    }
    
     /**
     * Permite guardar la configuración de la unidad materia
     * @param listaSaberHacer Unidad configuracion materia que se va a guardar
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaberHacer(List<Listaindicadoresporcriterioporconfiguracion> listaSaberHacer, DtoConfiguracionUnidadMateria dtoConfUnidadMateria){
        try{            
            if(listaSaberHacer == null || listaSaberHacer.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
          
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
           
            listaSaberHacer.forEach(sabhac -> {
                try {
                    if(sabhac.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = em.find(UnidadMateriaConfiguracion.class, dtoConfUnidadMateria.getUnidadMateriaConfiguracion().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = em.find(Criterio.class, sabhac.getListaindicadoresporcriterioporconfiguracionPK().getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = em.find(Indicador.class, sabhac.getListaindicadoresporcriterioporconfiguracionPK().getClaveIndicador());
                    umcd.setIndicador(indicador);
                    umcd.setPorcentaje(sabhac.getPorcentajeIndicador());
                    f.create(umcd);
                    Listaindicadoresporcriterioporconfiguracion vistaLista = new Listaindicadoresporcriterioporconfiguracion();
                    l.add(vistaLista);
                    }
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbAsignacionIndicadoresCriterios.guardarIndicadoresSaberHacer)", e, null);
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
    public ResultadoEJB<List<Listaasignacionindicadorescargaacademica>> buscarAsignacionIndicadoresCargaAcademica(DtoCargaAcademica dtoCargaAcademica){
        try{
            Boolean asigInd = compararUnidadesConfiguradasConTotales(dtoCargaAcademica);
            List<Listaasignacionindicadorescargaacademica> listaUnidadMatConfDet = new ArrayList<>();
            if(asigInd == true){
            listaUnidadMatConfDet = em.createQuery("SELECT l FROM Listaasignacionindicadorescargaacademica l WHERE l.carga =:cargaAcademica", Listaasignacionindicadorescargaacademica.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultList();
            }else{
                listaUnidadMatConfDet.clear();
            }
            return ResultadoEJB.crearCorrecto(listaUnidadMatConfDet, "Asignación de indicadores por criterio de la carga académica seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se obtuvo asignación de indicadores por criterio de la carga académica seleccionada. (EjbAsignacionIndicadoresCriterios.buscarAsignacionIndicadoresCargaAcademica)", e, null);
        }
    }
    
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
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "La carga académica no debe ser nula.");
            
            Integer delete = em.createQuery("DELETE FROM UnidadMateriaConfiguracionDetalle umcd WHERE umcd.configuracion.carga.carga =:carga", UnidadMateriaConfiguracionDetalle.class)
                .setParameter("carga", cargaAcademica.getCarga())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "La asignación de indicadores por criterio se eliminó correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la asignación de indicadores por criterio. (EjbAsignacionIndicadoresCriterios.eliminarAsignacionIndicadores)", e, null);
        }
    }
}
