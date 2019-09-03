package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.enums.EventoEscolarTipo;
import mx.edu.utxj.pye.sgi.facade.Facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * Permite la interacción con las aperturas de los eventos escolares
 */
@Stateless(name = "EjbEventoEscolarEJB")
public class EjbEventoEscolar {
    @EJB Facade f;
    private EntityManager em;

    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    /**
     * Permite verificar si un evento escolar de un tipo especificado se encuentra activo, no contempla aperturas especificas del evento.
     * Para eventos de captura de información para estudiantes, se debe utilizar este medio.
     * @param tipo Tipo del evento a verificar su apertura
     * @return Regresa el evento escolar aperturado si hay apertura o NULL  de lo contrario.
     */
    public ResultadoEJB<EventoEscolar> verificarEventoAperturado(EventoEscolarTipo tipo){
        try{
            //verificar apertura del evento
            EventoEscolar eventoEscolar = em.createQuery("select e from EventoEscolar e where e.tipo=:tipo and current_timestamp between e.inicio and e.fin", EventoEscolar.class)
                    .setParameter("tipo", tipo.getLabel())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(eventoEscolar == null){
                return ResultadoEJB.crearErroneo(2,eventoEscolar, "No existe evento aperturado del tipo solicitado.");// .crearCorrecto(map.entrySet().iterator().next(), "Evento aperturado.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura de evento escolar (EjbEventoEscolar.verificarEventoAperturado).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite verificar si un evento escolar de un tipo especificado se encuentra activo para un area correspondiente.
     * @param tipo Tipo del evento a verificar su apertura
     * @param area Área de las cual se quiere conocer si tiene una apertura especifica del evento
     * @return Regresa una entrada de mapa que especifica el evento escolar aperturado  y  TRUE si hay apertura o NULL y FALSE de lo contrario.
     */
    public ResultadoEJB<EventoEscolar> verificarAventoAperturadoPorArea(EventoEscolarTipo tipo, AreasUniversidad area){
        try{
            //verificar apertura del evento contemplando al area como filtro
            EventoEscolar eventoEscolar = em.createQuery("select e from EventoEscolar e inner join e.eventoEscolarDetalleList ed where e.tipo=:tipo and ed.area=:area and current_timestamp between ed.inicio and ed.fin", EventoEscolar.class)
                    .setParameter("tipo", tipo.getLabel())
                    .setParameter("area", area.getArea())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(eventoEscolar == null){
                return ResultadoEJB.crearErroneo(2, eventoEscolar, "No existe evento aperturado para el tipo y área solicitados.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura de evento escolar (EjbEventoEscolar.verificarAventoAperturadoPorArea).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite verificar si un evento escolar de un tipo especificado se encuentra activo para una persona correspondiente.
     * @param tipo Tipo del evento a verificar su apertura
     * @param persona Persona de la que se quiere conocer la apertura especifica del evento
     * @return Regresa una entrada de mapa que especifica el evento escolar aperturado  y  TRUE si hay apertura o NULL y FALSE de lo contrario.
     */
    public ResultadoEJB<EventoEscolar> verificarEventoAperturadoPorPersona(EventoEscolarTipo tipo, PersonalActivo persona){
        try{
            //TODO: verificar apertura del evento contemplando al area como filtro
            EventoEscolar eventoEscolar = em.createQuery("select e from EventoEscolar e inner join e.eventoEscolarDetalleList ed where e.tipo=:tipo and ed.persona=:persona and current_time between ed.inicio and ed.fin", EventoEscolar.class)
                    .setParameter("tipo", tipo.getLabel())
                    .setParameter("persona", persona.getPersonal().getClave())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if(eventoEscolar == null){
                return ResultadoEJB.crearErroneo(2, eventoEscolar, "No existe evento aperturado para el tipo y persona solicitados.");
            }else{
                return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento aperturado.");
            }
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar la apertura de evento escolar (EjbEventoEscolar.verificarAventoAperturadoPorPersona).", e, EventoEscolar.class);
        }
    }

    /**
     * Permite verificar si un evento está aperturado desde la perspectiva de una persona, primero verifica si está abierto a nivel global, luego por área y al último por persona
     * @param tipo Tipo del evento a verificar
     * @param persona Persona que interesa saber si tiene el evento habilitado
     * @return Evento aperturado o null de lo contrario
     */
    public ResultadoEJB<EventoEscolar> verificarEventoEnCascada(EventoEscolarTipo tipo, PersonalActivo persona){
        ResultadoEJB<EventoEscolar> res = verificarEventoAperturado(tipo);
        if(!res.getCorrecto()) res = verificarAventoAperturadoPorArea(tipo, persona.getAreaPOA());
        if(!res.getCorrecto()) res = verificarEventoAperturadoPorPersona(tipo, persona);

        return res;
    }
}
