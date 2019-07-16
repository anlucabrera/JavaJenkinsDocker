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
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.AsignacionIndicadoresCriteriosRolDocente;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionIndicadoresCriterios;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoConfiguracionUnidadMateria;
import mx.edu.utxj.pye.sgi.dto.vista.DtoAlerta;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.AlertaTipo;
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
     * Permite verificar si hay un periodo abierto para configurar unidades por materia
     * @param docente Personal Docente que va a realizar la configuración, permite funcionar como filtro en caso de un permiso especifico a su área o a su clave
     * @return Evento escolar detectado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEvento(PersonalActivo docente){
        try{
            return ejbEventoEscolar.verificarEventoEnCascada(EventoEscolarTipo.CONFIGURACION_DE_MATERIA, docente);
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo verificar el evento escolar para configuración de indicadores por criterio del personal docente (EjbAsignacionIndicadoresCriterios.verificarEvento)", e, EventoEscolar.class);
        }
    }

    /**
     * Permite obtener la lista de periodos escolares a elegir en el configurador de unidades por materia
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosDescendentes(){
        try{
            final List<PeriodosEscolares> periodos = f.getEntityManager().createQuery("select p from PeriodosEscolares p order by p.periodo desc", PeriodosEscolares.class)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(periodos, "Periodos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares. (EjbUnidadMateriaConfiguracion.getPeriodosDescendentes)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de cargas académicas por docente. (EjbUnidadMateriaConfiguracion.getCargaAcademicaPorDocente)", e, null);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la carga académica (EjbAsignacionAcademica. pack).", e, DtoCargaAcademica.class);
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
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbUnidadMateriaConfiguracion.getConfiguracionUnidadMateria)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe asignación de indicadores de criterios de la carga académica seleccionada
     * @param unidadMateriaConfiguracion Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<UnidadMateriaConfiguracionDetalle>> buscarConfiguracionUnidadMateriaDetalle(UnidadMateriaConfiguracion unidadMateriaConfiguracion){
        try{
            List<UnidadMateriaConfiguracionDetalle> listaUnidMatConf = f.getEntityManager().createQuery("SELECT umcd FROM UnidadMateriaConfiguracionDetalle umcd WHERE umcd.configuracion.configuracion =:configuracion", UnidadMateriaConfiguracionDetalle.class)
                    .setParameter("configuracion", unidadMateriaConfiguracion.getConfiguracion())
                    .getResultList();
            return ResultadoEJB.crearCorrecto(listaUnidMatConf, "Lista de configuración de indicadores por criterios de la materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de indicadores por criterios de la materia del docente. (EjbUnidadMateriaConfiguracion.getConfiguracionUnidadMateria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de configuración de unidad por materia sugerida, de la materia seleccionada previamente
     * @param dtoCargaAcademica Materia de la que se sugerirá configuración
     * @return Resultado del proceso
     */
    public List<DtoConfiguracionUnidadMateria> getConfiguracionUnidadMateriaSugerida(DtoCargaAcademica dtoCargaAcademica){
            List<DtoConfiguracionUnidadMateria> unidadMat = f.getEntityManager().createQuery("SELECT um FROM UnidadMateria um WHERE um.idMateria.idMateria =:idMateria", UnidadMateria.class)
                    .setParameter("idMateria", dtoCargaAcademica.getCargaAcademica().getIdPlanMateria().getIdMateria().getIdMateria())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return unidadMat;
    }
   
    /**
     * Empaqueta una configuración sugeridad de la unidad materia en su DTO Wrapper
     * @param unidadMateria Unidades de la materia a empaquetar
     * @return Configuración sugerida de la unidad materia empaquetada
     */
    public ResultadoEJB<DtoConfiguracionUnidadMateria> pack(UnidadMateria unidadMateria){
        try{
            if(unidadMateria == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar una configuración sugerida de la unidad nula.", DtoConfiguracionUnidadMateria.class);

            UnidadMateria unidadMateriaBD = f.getEntityManager().find(UnidadMateria.class, unidadMateria.getIdUnidadMateria());
            if(unidadMateriaBD == null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar una configuración sugerida de una unidad de materia no registrada previamente en base de datos.", DtoConfiguracionUnidadMateria.class);
         
            UnidadMateriaConfiguracion unidadMateriaConfiguracion = new UnidadMateriaConfiguracion();
            
            DtoConfiguracionUnidadMateria dto = new DtoConfiguracionUnidadMateria(unidadMateriaBD, unidadMateriaConfiguracion);
         
            return ResultadoEJB.crearCorrecto(dto, "Configuración sugerida de la unidad materia empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la configuración sugerida de la unidad materia (EjbUnidadMateriaConfiguracion.pack).", e, DtoConfiguracionUnidadMateria.class);
        }
    }
   
    
    /**
     * Permite guardar la configuración de la unidad materia
     * @param configuracionUnidadMaterias Unidad configuracion materia que se va a guardar
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<List<DtoConfiguracionUnidadMateria>> guardarConfUnidadMateria(List<DtoConfiguracionUnidadMateria> configuracionUnidadMaterias, CargaAcademica cargaAcademica){
        try{            
            if(configuracionUnidadMaterias == null || configuracionUnidadMaterias.isEmpty()) return ResultadoEJB.crearErroneo(2, "La configuración de la unidad materia no debe ser nula.");
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(3, "La carga académica no debe ser nula.");
          
            List<DtoConfiguracionUnidadMateria> l = new ArrayList<>();
           
            configuracionUnidadMaterias.forEach(cum -> {
                try {
                    UnidadMateriaConfiguracion umc = new UnidadMateriaConfiguracion();
                    umc.setFechaInicio(cum.getUnidadMateriaConfiguracion().getFechaInicio());
                    umc.setFechaFin(cum.getUnidadMateriaConfiguracion().getFechaFin());
                    umc.setCarga(cargaAcademica);
                    umc.setIdUnidadMateria(cum.getUnidadMateria());
                    umc.setPorcentaje(cum.getUnidadMateriaConfiguracion().getPorcentaje());
                    f.create(umc);
                    DtoConfiguracionUnidadMateria dto = new DtoConfiguracionUnidadMateria(cum.getUnidadMateria(), umc);
                    l.add(dto);
                } catch (Throwable ex) {
                    Logger.getLogger(EjbConfiguracionUnidadMateria.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return ResultadoEJB.crearCorrecto(l, "La configuración de la unidad materia se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la configuración de unidad materia. (EjbUnidadMateriaConfiguracion.guardarConfUnidadMateria)", e, null);
        }
    }
    
    /**
     * Permite guardar la configuración de la unidad materia
     * @param tareaIntegradora Unidad configuracion materia que se va a guardar
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<TareaIntegradora> guardarTareaIntegradora(TareaIntegradora tareaIntegradora, CargaAcademica cargaAcademica){
        try{            
            if(tareaIntegradora == null) return ResultadoEJB.crearErroneo(2, "La tarea integradora no debe ser nula.");
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(3, "La carga académica no debe ser nula.");
          
            TareaIntegradora ti = new TareaIntegradora();
            ti.setDescripcion(tareaIntegradora.getDescripcion());
            ti.setFechaEntrega(tareaIntegradora.getFechaEntrega());
            ti.setCarga(cargaAcademica);
            ti.setPorcentaje(tareaIntegradora.getPorcentaje());
            f.create(ti);
               
            return ResultadoEJB.crearCorrecto(ti, "La tarea integradora se guardo correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la tarea integradora de la materia. (EjbUnidadMateriaConfiguracion.guardarTareaIntegradora)", e, null);
        }
    }
    
    /**
     * Permite eliminar la configuración de la unidad materia
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<Integer> eliminarConfUnidadMateria(CargaAcademica cargaAcademica){
        try{ 
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "La carga académica no debe ser nula.");
            
            Integer delete = f.getEntityManager().createQuery("DELETE FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:carga", UnidadMateriaConfiguracion.class)
                .setParameter("carga", cargaAcademica.getCarga())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "La configuración de la unidad materia se elimino correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar  la configuración de unidad materia. (EjbUnidadMateriaConfiguracion.eliminarConfUnidadMateria)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de configuración de unidad por materia, de la materia seleccionada previamente
     * @param dtoCargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoConfiguracionUnidadMateria>> getConfiguracionUnidadMateria(DtoCargaAcademica dtoCargaAcademica){
        try{
            List<DtoConfiguracionUnidadMateria> unidadMatConfDto = f.getEntityManager().createQuery("SELECT umc FROM UnidadMateriaConfiguracion umc WHERE umc.carga.carga =:cargaAcademica", UnidadMateriaConfiguracion.class)
                    .setParameter("cargaAcademica", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getResultStream()
                    .map(ca -> pack(ca).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            return ResultadoEJB.crearCorrecto(unidadMatConfDto, "Lista de configuración de la unidad materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de configuración de la materia del docente. (EjbUnidadMateriaConfiguracion.getConfiguracionUnidadMateria)", e, null);
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

            UnidadMateria unidadMateria = f.getEntityManager().find(UnidadMateria.class, unidadMateriaConfiguracion.getIdUnidadMateria().getIdUnidadMateria());
            
            DtoConfiguracionUnidadMateria dto = new DtoConfiguracionUnidadMateria(unidadMateria, unidadMateriaConfiguracion);
         
            return ResultadoEJB.crearCorrecto(dto, "Configuración de la unidad materia empaquetada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar la configuración de la unidad materia (EjbUnidadMateriaConfiguracion.pack).", e, DtoConfiguracionUnidadMateria.class);
        }
    }
    
    /**
     * Permite obtener la lista de configuración de unidad por materia, de la materia seleccionada previamente
     * @param dtoCargaAcademica Materia de la que se obtendrá configuración
     * @return Resultado del proceso
     */
    public ResultadoEJB<TareaIntegradora> getTareaIntegradora(DtoCargaAcademica dtoCargaAcademica){
        try{
            TareaIntegradora tareaIntegradora = f.getEntityManager().createQuery("SELECT ti FROM TareaIntegradora ti WHERE ti.carga.carga =:carga", TareaIntegradora.class)
                    .setParameter("carga", dtoCargaAcademica.getCargaAcademica().getCarga())
                    .getSingleResult();
            return ResultadoEJB.crearCorrecto(tareaIntegradora, "Tarea Integradora de la materia seleccionada.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la tarea integradora de la materia seleccionada. (EjbUnidadMateriaConfiguracion.getTareaIntegradora)", e, null);
        }
    }
    
    /**
     * Permite eliminar la configuración de la unidad materia
     * @param cargaAcademica Carga académica de la que se guardará configuración
     * @return Resultado del proceso generando la instancia de configuración unidad materia obtenida
     */
    public ResultadoEJB<Integer> eliminarTareaIntegradora(CargaAcademica cargaAcademica){
        try{ 
            if(cargaAcademica == null) return ResultadoEJB.crearErroneo(2, "La carga académica no debe ser nula.");
            
            Integer delete = f.getEntityManager().createQuery("DELETE FROM TareaIntegradora t WHERE t.carga.carga =:carga", TareaIntegradora.class)
                .setParameter("carga", cargaAcademica.getCarga())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "La tarea integradora se elimino correctamente.");
        }catch (Throwable e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar  la tarea integradora. (EjbUnidadMateriaConfiguracion.eliminarTareaIntegradora)", e, null);
        }
    }
}
