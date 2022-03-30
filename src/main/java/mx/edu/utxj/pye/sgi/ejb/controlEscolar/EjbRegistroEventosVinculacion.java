/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;
import com.github.adminfaces.starter.infra.model.Filter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosVinculacion;
import mx.edu.utxj.pye.sgi.ejb.ch.EjbCarga;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoProceso;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.DocumentoSeguimientoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoVinculacion;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Grupo;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.enums.PersonalFiltro;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroEventosVinculacion")
public class EjbRegistroEventosVinculacion {
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
     * Permite validar si el usuario autenticado es personal adscrito a la coordinación de estadías
     * @param clave Número de nómina del usuario autenticado
     * @return Resultado del proceso
     */
    public ResultadoEJB<Filter<PersonalActivo>> validarCoordinacionEstadia(Integer clave){
        try{
            PersonalActivo p = ejbPersonalBean.pack(clave);
            Filter<PersonalActivo> filtro = new Filter<>();
            if (p.getPersonal().getCategoriaOperativa().getCategoria() == 15 && p.getPersonal().getStatus()!='B') {
                filtro.setEntity(p);
                filtro.addParam(PersonalFiltro.CLAVE.getLabel(), String.valueOf(clave));
            }
            return ResultadoEJB.crearCorrecto(filtro, "El usuario ha sido comprobado como personal con rol asignado en el proceso de estadía.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El personal no se pudo validar. (EjbRegistroEventosVinculacion.validarCoordinacionEstadia)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos de vinculación registrados
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEventosVinculacion>> getEventosVinculacion(){
        try{
            List<DtoEventosVinculacion> listaEventosRegistrados = em.createQuery("SELECT e FROM EventoVinculacion e ORDER BY e.generacion, e.nivel ASC", EventoVinculacion.class)
                    .getResultStream()
                    .map(evento -> packEventoRegistrado(evento).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEventosRegistrados, "Lista de eventos de vinculación.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de vinculación. (EjbRegistroEventosVinculacion.getEventosVinculacion)", e, null);
        }
    }
    
    /**
     * Empaqueta un evento de vinculación en su DTO Wrapper
     * @param eventoVinculacion
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoEventosVinculacion> packEventoRegistrado(EventoVinculacion eventoVinculacion){
        try{
            if(eventoVinculacion == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un evento nulo.", DtoEventosVinculacion.class);
            if(eventoVinculacion.getEvento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un evento con clave nula.", DtoEventosVinculacion.class);

            EventoVinculacion eventoVinculacionBD = em.find(EventoVinculacion.class, eventoVinculacion.getEvento());
            if(eventoVinculacionBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un evento no registrado previamente en base de datos.", DtoEventosVinculacion.class);
            
            Generaciones generacion = em.find(Generaciones.class, eventoVinculacionBD.getGeneracion());
            ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, eventoVinculacionBD.getNivel());
            
            DtoEventosVinculacion dto = new DtoEventosVinculacion(eventoVinculacion, generacion, nivel);
            return ResultadoEJB.crearCorrecto(dto, "Evento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el evento (EjbRegistroEventosVinculacion.packEventoRegistradoCE).", e, DtoEventosVinculacion.class);
        }
    }
    
     /**
     * Permite obtener la lista de actividades disponibles para registrar dependiendo de los eventos registrados de la generación y nivel educativo seleccionado
     * @param generacion
     * @param nivel
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getListaActividadesDisponibles(Generaciones generacion, ProgramasEducativosNiveles nivel){
        try{
            List<String> listaActividadesDisponibles = new ArrayList<>();
            listaActividadesDisponibles.add("Entrega Carta Responsiva");
            listaActividadesDisponibles.add("Entrega Constancia Curso IMSS");
            
            List<String> listaEventosRegistrados = em.createQuery("SELECT e FROM EventoVinculacion e WHERE e.generacion=:generacion AND e.nivel=:nivel", EventoVinculacion.class)
                    .setParameter("generacion", generacion.getGeneracion())
                    .setParameter("nivel", nivel.getNivel())
                    .getResultStream()
                    .map(p->p.getActividad())
                    .collect(Collectors.toList());
            
            listaActividadesDisponibles.removeAll(listaEventosRegistrados);
            
            return ResultadoEJB.crearCorrecto(listaActividadesDisponibles, "Lista de actividades disponibles para registrar de la generación y nivel educativo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de actividades disponibles para registrar de la generación y nivel educativo seleccionado. (EjbRegistroEventosVinculacion.getListaActividadesDisponibles)", e, null);
        }
    }
    
     /**
     * Permite actualizar las fechas de inicio y fin de un evento de vinculación registrado previamente
     * @param dtoEventosVinculacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoVinculacion> actualizarEventoRegistrado(DtoEventosVinculacion dtoEventosVinculacion){
        try{
            EventoVinculacion eventoVincBD = em.find(EventoVinculacion.class, dtoEventosVinculacion.getEventoVinculacion().getEvento());
                eventoVincBD.setFechaInicio(dtoEventosVinculacion.getEventoVinculacion().getFechaInicio());
                eventoVincBD.setFechaFin(dtoEventosVinculacion.getEventoVinculacion().getFechaFin());
                em.merge(eventoVincBD);
                em.flush();
            
            return ResultadoEJB.crearCorrecto(eventoVincBD, "Se actualizó correctamente la información del evento de vinculación seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información del evento de vinculación seleccionado. (EjbRegistroEventosVinculacion.actualizarEventoRegistrado)", e, null);
        }
    }
    
     /**
     * Permite registrar el evento de vinculación de la generación y nivel educativo seleccionados
     * @param generacion
     * @param nivel
     * @param actividad
     * @param fechaInicio
     * @param fechaFin
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoVinculacion> agregarEventoVinculacion(Generaciones generacion, ProgramasEducativosNiveles nivel, String actividad, Date fechaInicio, Date fechaFin){
        try{
            
            EventoVinculacion eventoVinculacion = new EventoVinculacion();
            eventoVinculacion.setGeneracion(generacion.getGeneracion());
            eventoVinculacion.setNivel(nivel.getNivel());
            eventoVinculacion.setActividad(actividad);
            if(actividad.equals("Entrega Carta Responsiva")){
                DocumentoProceso documentoCR = em.find(DocumentoProceso.class, (int)63);
                eventoVinculacion.setDocumentoProceso(documentoCR);
            }else{
                DocumentoProceso documentoCIMSS = em.find(DocumentoProceso.class, (int)64);
                eventoVinculacion.setDocumentoProceso(documentoCIMSS);
            }
            eventoVinculacion.setFechaInicio(fechaInicio);
            eventoVinculacion.setFechaFin(fechaFin);
            em.persist(eventoVinculacion);
            em.flush();
             
            return ResultadoEJB.crearCorrecto(eventoVinculacion, "Evento de vinculación registrado correctamente.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el evento de vinculación correctamente. (EjbRegistroEventosVinculacion.agregarEventoVinculacion)", e, null);
        }
    }
    
    /**
     * Permite eliminar el evento de vinculación seleccionado
     * @param eventoVinculacion
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarEventoVinculacion(EventoVinculacion eventoVinculacion){
        try{
            Integer delete = em.createQuery("DELETE FROM EventoVinculacion e WHERE e.evento=:evento", EventoVinculacion.class)
                .setParameter("evento", eventoVinculacion.getEvento())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el evento de vinculación seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el evento de vinculación seleccionado. (EjbRegistroEventosVinculacion.eliminarEventoVinculacion)", e, null);
        }
    }
    
    
     /**
     * Permite verificar si existen expedientes de vinculación registrados en el evento seleccionado
     * @param eventoVinculacion
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> getExistenDocumentosEvento(EventoVinculacion eventoVinculacion){
        try{
            List<DocumentoSeguimientoVinculacion> documentosEvento = em.createQuery("SELECT d FROM DocumentoSeguimientoVinculacion d WHERE d.evento.evento=:evento", DocumentoSeguimientoVinculacion.class)
                    .setParameter("evento", eventoVinculacion.getEvento())
                    .getResultStream().collect(Collectors.toList());
            
           return ResultadoEJB.crearCorrecto(!documentosEvento.isEmpty(), "Documentos del evento de vinculación registrados.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
}
