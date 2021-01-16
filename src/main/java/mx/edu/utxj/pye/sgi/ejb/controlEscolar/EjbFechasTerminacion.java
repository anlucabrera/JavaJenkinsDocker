/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.*;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodoEscolarFechas;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbFechasTerminacion")
public class EjbFechasTerminacion {
    
    @EJB EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite obtener el periodo actual
     * @return Resultado del proceso
     */
    public PeriodosEscolares getPeriodoActual() {

        PeriodoEscolarFechas periodoFechas = em.createQuery("SELECT p FROM PeriodoEscolarFechas p WHERE current_timestamp between p.inicio and p.fin", PeriodoEscolarFechas.class)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        
        PeriodosEscolares periodo = new PeriodosEscolares();
        
        if(periodoFechas != null)
        {
            periodo = em.find(PeriodosEscolares.class, periodoFechas.getPeriodo());
        }
        return periodo;
    }
    
      /**
     * Permite validar si el usuario autenticado es personal adscrito a la coordinación de titulación
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarTitulacion(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            filtro.setEntity(p);
            filtro.addParam(PersonalFiltro.AREA_OPERATIVA.getLabel(), String.valueOf(ep.leerPropiedadEntera("personalTitulacion").orElse(60)));
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbFechasTerminacion.validarTitulacion)", e, null);
        }
    }

    /**
     * Permite obtener la lista de generaciones a elegir en el apartado de registro de fechas de terminación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> getGeneracionesExpediente(){
        try{
             List<Short> claves = em.createQuery("SELECT e FROM EventoTitulacion e",  EventoTitulacion.class)
                .getResultStream()
                .map(a -> a.getGeneracion())
                .collect(Collectors.toList());
            
            if (claves.isEmpty()) {
                claves.add(0, getPeriodoActual().getPeriodo().shortValue());
            }
            List<Generaciones> generaciones = em.createQuery("SELECT g FROM Generaciones g WHERE g.generacion IN :claves ORDER BY g.generacion DESC", Generaciones.class)
                    .setParameter("claves", claves)
                    .getResultStream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(generaciones, "Generaciones ordenadas de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones. (EjbFechasTerminacion.getGeneracionesExpediente)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de niveles educativos a elegir en el apartado de registro de fechas de terminación
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> getNivelesExpedientes(){
        try{
             List<String> claves = em.createQuery("SELECT e FROM EventoTitulacion e", EventoTitulacion.class)
                .getResultStream()
                .map(a -> a.getNivel())
                .distinct()
                .collect(Collectors.toList());
        
            if (claves.isEmpty()) {
                claves.add(0, "No existe niveles registrados");
            }
            List<ProgramasEducativosNiveles> niveles = em.createQuery("SELECT p FROM ProgramasEducativosNiveles p WHERE p.nivel IN :claves ORDER BY p.nivel ASC", ProgramasEducativosNiveles.class)
                    .setParameter("claves", claves)
                    .getResultStream()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(niveles, "Niveles educativos ordenados de forma descendente");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos. (EjbFechasTerminacion.getNivelesExpedientes)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de programas educativos que correspondan al nivel seleccionado
     * @param generacion Generación seleccionada en pantalla
     * @param nivel Nivel seleccionado en pantalla
     * @return Resultado del proceso
     */
    public ResultadoEJB<FechaTerminacionTitulacion> obtenerFechasTerminacion(Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            //buscar carga académica del personal docente logeado del periodo seleccionado
            FechaTerminacionTitulacion fechas = em.createQuery("SELECT f FROM FechaTerminacionTitulacion f WHERE f.generacion =:generacion AND f.nivel =:nivel", FechaTerminacionTitulacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            
            if (fechas == null) {
                FechaTerminacionTitulacion fechasN = new FechaTerminacionTitulacion(0, (short)0, "", new Date(), new Date(), new Date());
                return ResultadoEJB.crearCorrecto(fechasN, "Fechas de terminación no encontrada");
            } else {
                return ResultadoEJB.crearCorrecto(fechas, "Fechas de terminación registradas");
            }
            
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener las fechas de terminación. (EjbFechasTerminacion.obtenerFechasTerminacion)", e, null);
        }
    }
    
    /**
     * Permite guardar fechas de terminación de titulación en la base de datos
     * @param generacion
     * @param nivel
     * @param fechaTerminacionTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<FechaTerminacionTitulacion> guardarFechasTerminacionTitulacion(Generaciones generacion, ProgramasEducativosNiveles nivel, FechaTerminacionTitulacion fechaTerminacionTitulacion){
        try{
            FechaTerminacionTitulacion fechaTerminacion = new FechaTerminacionTitulacion();
            fechaTerminacion.setGeneracion(generacion.getGeneracion());
            fechaTerminacion.setNivel(nivel.getNivel());
            fechaTerminacion.setFechaInicio(fechaTerminacionTitulacion.getFechaInicio());
            fechaTerminacion.setFechaFin(fechaTerminacionTitulacion.getFechaFin());
            fechaTerminacion.setActaExencion(fechaTerminacionTitulacion.getActaExencion());
            em.persist(fechaTerminacion);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(fechaTerminacion, "Se guardaron correctamente las fechas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se guardaron las fechas. (EjbFechasTerminacion.guardarFechasTerminacionTitulacion)", e, null);
        }
    }
    
     /**
     * Permite guardar fechas de terminación de titulación en la base de datos
     * @param fechaTerminacionTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<FechaTerminacionTitulacion> actualizarFechasTerminacionTitulacion(FechaTerminacionTitulacion fechaTerminacionTitulacion){
        try{
            em.merge(fechaTerminacionTitulacion);
            em.flush();
            
            return ResultadoEJB.crearCorrecto(fechaTerminacionTitulacion, "Se actualizaron correctamente las fechas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se actualizaron las fechas. (EjbFechasTerminacion.actualizarFechasTerminacionTitulacion)", e, null);
        }
    }
}