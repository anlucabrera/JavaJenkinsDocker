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
import javax.persistence.TypedQuery;
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
            final List<PeriodosEscolares> periodos = f.getEntityManager().createQuery("select p from PeriodosEscolares p order by p.periodo desc", PeriodosEscolares.class)
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
            List<DtoCargaAcademica> cargas = f.getEntityManager().createQuery("SELECT c FROM CargaAcademica c WHERE c.docente =:docente AND c.evento.periodo =:periodo", CargaAcademica.class)
                    .setParameter("docente", docente.getPersonal().getClave())
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
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

            CargaAcademica cargaAcademicaBD = f.getEntityManager().find(CargaAcademica.class, cargaAcademica.getCarga());
            if(cargaAcademicaBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar una carga académica no registrada previamente en base de datos.", DtoCargaAcademica.class);
            
            Grupo grupo = f.getEntityManager().find(Grupo.class, cargaAcademicaBD.getCveGrupo().getIdGrupo());
            PeriodosEscolares periodo = f.getEntityManager().find(PeriodosEscolares.class, cargaAcademicaBD.getEvento().getPeriodo());
            PlanEstudioMateria planEstudioMateria = f.getEntityManager().find(PlanEstudioMateria.class, cargaAcademicaBD.getIdPlanMateria().getIdPlanMateria());
            PlanEstudio planEstudio = f.getEntityManager().find(PlanEstudio.class, planEstudioMateria.getIdPlan().getIdPlanEstudio());
            Materia materia = f.getEntityManager().find(Materia.class, planEstudioMateria.getIdMateria().getIdMateria());
            PersonalActivo docente = ejbPersonalBean.pack(cargaAcademicaBD.getDocente());
            AreasUniversidad programa = f.getEntityManager().find(AreasUniversidad.class, planEstudioMateria.getIdPlan().getIdPe());
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
            List<UnidadMateriaConfiguracion> listaUnidMatConf = f.getEntityManager().createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica", UnidadMateriaConfiguracion.class)
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
            UnidadMateriaConfiguracionDetalle unidadMatConfDet = f.getEntityManager().createQuery("SELECT umcd FROM UnidadMateriaConfiguracionDetalle umcd JOIN umcd.configuracion conf WHERE conf.carga.carga =:cargaAcademica", UnidadMateriaConfiguracionDetalle.class)
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
//            System.err.println("getIndicadoresCriterioParaAsignar - dtoCargaAcademica " + dtoCargaAcademica.getCargaAcademica().getCarga());
            Integer periodo = getPeriodoActivoIndicadores(dtoCargaAcademica); 
//            System.err.println("getIndicadoresCriterioParaAsignar - periodo " + periodo);
            Integer configuracion = getConfiguracion(dtoCargaAcademica); 
//            System.err.println("getIndicadoresCriterioParaAsignar - configuracion " + configuracion);
       
            List<Listaindicadoresporcriterioporconfiguracion> listaIndicadores = f.getEntityManager().createQuery("SELECT l FROM Listaindicadoresporcriterioporconfiguracion l WHERE l.cargaAcademica =:cargaAcademica AND l.listaindicadoresporcriterioporconfiguracionPK.periodo =:periodo AND l.listaindicadoresporcriterioporconfiguracionPK.configuracion =:configuracion", Listaindicadoresporcriterioporconfiguracion.class)
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
        TypedQuery<Integer> v = (TypedQuery<Integer>) f.getEntityManager().createQuery("SELECT MAX(l.listaindicadoresporcriterioporconfiguracionPK.periodo) FROM Listaindicadoresporcriterioporconfiguracion l");
        
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
        TypedQuery<Integer> v = (TypedQuery<Integer>) f.getEntityManager().createQuery("SELECT MAX(l.listaindicadoresporcriterioporconfiguracionPK.configuracion) FROM Listaindicadoresporcriterioporconfiguracion l");
        
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
     * Permite guardar la configuración de la unidad materia
     * @param listaSer Unidad configuracion materia que se va a guardar
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSer(List<Listaindicadoresporcriterioporconfiguracion> listaSer){
        try{            
            if(listaSer == null || listaSer.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
          
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
           
            listaSer.forEach(ser -> {
                try {
                    if(ser.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = f.getEntityManager().find(UnidadMateriaConfiguracion.class, ser.getListaindicadoresporcriterioporconfiguracionPK().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = f.getEntityManager().find(Criterio.class, ser.getListaindicadoresporcriterioporconfiguracionPK().getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = f.getEntityManager().find(Indicador.class, ser.getListaindicadoresporcriterioporconfiguracionPK().getClaveIndicador());
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbUnidadMateriaConfiguracion.guardarIndicadoresSer)", e, null);
        }
    }
    
     /**
     * Permite guardar la configuración de la unidad materia
     * @param listaSaber Unidad configuracion materia que se va a guardar
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaber(List<Listaindicadoresporcriterioporconfiguracion> listaSaber){
        try{            
            if(listaSaber == null || listaSaber.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
          
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
           
            listaSaber.forEach(saber -> {
                try {
                    if(saber.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = f.getEntityManager().find(UnidadMateriaConfiguracion.class, saber.getListaindicadoresporcriterioporconfiguracionPK().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = f.getEntityManager().find(Criterio.class, saber.getListaindicadoresporcriterioporconfiguracionPK().getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = f.getEntityManager().find(Indicador.class, saber.getListaindicadoresporcriterioporconfiguracionPK().getClaveIndicador());
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbUnidadMateriaConfiguracion.guardarIndicadoresSaber)", e, null);
        }
    }
    
     /**
     * Permite guardar la configuración de la unidad materia
     * @param listaSaberHacer Unidad configuracion materia que se va a guardar
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<Listaindicadoresporcriterioporconfiguracion>> guardarIndicadoresSaberHacer(List<Listaindicadoresporcriterioporconfiguracion> listaSaberHacer){
        try{            
            if(listaSaberHacer == null || listaSaberHacer.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
          
            List<Listaindicadoresporcriterioporconfiguracion> l = new ArrayList<>();
           
            listaSaberHacer.forEach(sabhac -> {
                try {
                    if(sabhac.getPorcentajeIndicador() != 0){
                    UnidadMateriaConfiguracionDetalle umcd = new UnidadMateriaConfiguracionDetalle();
                    UnidadMateriaConfiguracion umc = f.getEntityManager().find(UnidadMateriaConfiguracion.class, sabhac.getListaindicadoresporcriterioporconfiguracionPK().getConfiguracion());
                    umcd.setConfiguracion(umc);
                    Criterio criterio = f.getEntityManager().find(Criterio.class, sabhac.getListaindicadoresporcriterioporconfiguracionPK().getClaveCriterio());
                    umcd.setCriterio(criterio);
                    Indicador indicador = f.getEntityManager().find(Indicador.class, sabhac.getListaindicadoresporcriterioporconfiguracionPK().getClaveIndicador());
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
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbUnidadMateriaConfiguracion.guardarIndicadoresSaberHacer)", e, null);
        }
    }
}
