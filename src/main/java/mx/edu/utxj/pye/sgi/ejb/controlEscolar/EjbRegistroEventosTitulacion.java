/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import com.github.adminfaces.starter.infra.model.Filter;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosTitulacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneracionesPK;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosGeneraciones;
import mx.edu.utxj.pye.sgi.entity.titulacion.ProcesosIntexp;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoTitulacion;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroEventosTitulacion")
public class EjbRegistroEventosTitulacion {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB Facade f;
    private EntityManager em;
    
    @EJB EjbCarga ejbCarga;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
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
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbRegistroEventosTitulacion.validarTitulacion)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos de titulación registrados generaciones de control escolar
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEventosTitulacion.GeneracionesControlEscolar>> getEventosTitulacionControlEscolar(){
        try{
            List<DtoEventosTitulacion.GeneracionesControlEscolar> listaEventosRegistrados = em.createQuery("SELECT e FROM EventoTitulacion e ORDER BY e.generacion, e.nivel ASC", EventoTitulacion.class)
                    .getResultStream()
                    .map(evento -> packEventoRegistradoCE(evento).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEventosRegistrados, "Lista de eventos de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de titulación. (EjbRegistroEventosTitulacion.getEventosTitulacionControlEscolar)", e, null);
        }
    }
    
     /**
     * Empaqueta un evento de titulación control escolar en su DTO Wrapper
     * @param eventoTitulacion
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoEventosTitulacion.GeneracionesControlEscolar> packEventoRegistradoCE(EventoTitulacion  eventoTitulacion){
        try{
            if(eventoTitulacion == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un evento nulo.", DtoEventosTitulacion.GeneracionesControlEscolar.class);
            if(eventoTitulacion.getEvento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un evento con clave nula.", DtoEventosTitulacion.GeneracionesControlEscolar.class);

            EventoTitulacion eventoTitulacionBD = em.find(EventoTitulacion.class, eventoTitulacion.getEvento());
            if(eventoTitulacionBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un evento no registrado previamente en base de datos.", DtoEventosTitulacion.GeneracionesControlEscolar.class);
            
            Generaciones generacion = em.find(Generaciones.class, eventoTitulacionBD.getGeneracion());
            ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, eventoTitulacionBD.getNivel());
            
            DtoEventosTitulacion.GeneracionesControlEscolar dto = new DtoEventosTitulacion.GeneracionesControlEscolar(eventoTitulacion, generacion, nivel);
            return ResultadoEJB.crearCorrecto(dto, "Evento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el evento (EjbRegistroEventosTitulacion.packEventoRegistradoCE).", e, DtoEventosTitulacion.GeneracionesControlEscolar.class);
        }
    }
    
     /**
     * Permite verificar si existe evento de titulación activo de la actividad en el periodo escolar seleccionado
     * @param eventoSeleccionado
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEventoActivo(EventoTitulacion eventoSeleccionado){
        try{
            EventoTitulacion eventoReg = em.createQuery("SELECT e FROM EventoTitulacion e WHERE e.evento=:evento AND current_date between e.inicio and e.fin", EventoTitulacion.class)
                    .setParameter("evento", eventoSeleccionado.getEvento())
                    .getResultStream().findFirst().orElse(null);
           return ResultadoEJB.crearCorrecto(eventoReg != null, "Evento de titulación activo encontrado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
     /**
     * Permite actualizar las fechas de inicio y fin de un evento de titulación registrado previamente de control escolar
     * @param dtoEventosTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoTitulacion> actualizarEventoRegistradoCE(DtoEventosTitulacion.GeneracionesControlEscolar dtoEventosTitulacion){
        try{
            EventoTitulacion eventoTitBD = em.find(EventoTitulacion.class, dtoEventosTitulacion.getEventoTitulacion().getEvento());
                eventoTitBD.setFechaInicio(dtoEventosTitulacion.getEventoTitulacion().getFechaInicio());
                eventoTitBD.setFechaFin(dtoEventosTitulacion.getEventoTitulacion().getFechaFin());
                em.merge(eventoTitBD);
                em.flush();
            
            return ResultadoEJB.crearCorrecto(eventoTitBD, "Se actualizó correctamente la información del evento de titulación seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información del evento de titulación seleccionado. (EjbRegistroEventosTitulacion.actualizarEventoRegistradoCE)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos de titulación registrados generaciones de saiiut
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEventosTitulacion.GeneracionesSAIIUT>> getEventosTitulacionSAIIUT(){
        try{
            List<DtoEventosTitulacion.GeneracionesSAIIUT> listaEventosRegistrados = em.createQuery("SELECT p FROM ProcesosGeneraciones p ORDER BY p.procesosGeneracionesPK.proceso ASC", ProcesosGeneraciones.class)
                    .getResultStream()
                    .map(evento -> packEventoRegistradoSAIIUT(evento).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEventosRegistrados, "Lista de eventos de titulación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de titulación. (EjbRegistroEventosTitulacion.getEventosTitulacionSAIIUT)", e, null);
        }
    }
    
     /**
     * Empaqueta un evento de titulación saiiut en su DTO Wrapper
     * @param procesosGeneraciones
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoEventosTitulacion.GeneracionesSAIIUT> packEventoRegistradoSAIIUT(ProcesosGeneraciones  procesosGeneraciones){
        try{
            if(procesosGeneraciones == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un evento nulo.", DtoEventosTitulacion.GeneracionesSAIIUT.class);
            if(procesosGeneraciones.getProcesosGeneracionesPK()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un evento con clave nula.", DtoEventosTitulacion.GeneracionesSAIIUT.class);
            
            ProcesosGeneraciones procesosGeneracionesBD = em.find(ProcesosGeneraciones.class, procesosGeneraciones.getProcesosGeneracionesPK());
            if(procesosGeneracionesBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un evento no registrado previamente en base de datos.", DtoEventosTitulacion.GeneracionesSAIIUT.class);
            
            ProcesosIntexp procesoInt = em.createQuery("SELECT p FROM ProcesosIntexp p WHERE p.proceso=:proceso ", ProcesosIntexp.class)
                    .setParameter("proceso", procesosGeneracionesBD.getProcesosGeneracionesPK().getProceso())
                    .getResultStream().findFirst().orElse(null);
            
            Generaciones generacion = em.find(Generaciones.class, procesosGeneracionesBD.getProcesosGeneracionesPK().getGeneracion());
            PeriodosEscolares periodo = em.find(PeriodosEscolares.class,  procesosGeneracionesBD.getProcesosGeneracionesPK().getPeriodo());
            
            DtoEventosTitulacion.GeneracionesSAIIUT dto = new DtoEventosTitulacion.GeneracionesSAIIUT(procesosGeneracionesBD, generacion, periodo, procesoInt.getFechaInicio(), procesoInt.getFechaFin());
            return ResultadoEJB.crearCorrecto(dto, "Evento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el evento (EjbRegistroEventosTitulacion.packEventoRegistradoSAIIUT).", e, DtoEventosTitulacion.GeneracionesSAIIUT.class);
        }
    }
    
     /**
     * Permite actualizar las fechas de inicio y fin de un evento de titulación registrado previamente de saiiut
     * @param dtoEventosTitulacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProcesosIntexp> actualizarEventoRegistradoSAIIUT(DtoEventosTitulacion.GeneracionesSAIIUT dtoEventosTitulacion){
        try{
            ProcesosIntexp procesoIntBD = em.find(ProcesosIntexp.class, dtoEventosTitulacion.getEventoTitulacion().getProcesosGeneracionesPK().getProceso());
                procesoIntBD.setFechaInicio(dtoEventosTitulacion.getFechaInicio());
                procesoIntBD.setFechaFin(dtoEventosTitulacion.getFechaFin());
                em.merge(procesoIntBD);
                em.flush();
            
            return ResultadoEJB.crearCorrecto(procesoIntBD, "Se actualizó correctamente la información del evento de titulación seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información del evento de titulación seleccionado. (EjbRegistroEventosTitulacion.actualizarEventoRegistradoSAIIUT)", e, null);
        }
    }
    
}
