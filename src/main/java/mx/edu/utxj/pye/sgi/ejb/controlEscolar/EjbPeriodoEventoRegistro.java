/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.TypedQuery;
import mx.edu.utxj.pye.sgi.controlador.Caster;
import mx.edu.utxj.pye.sgi.dto.PersonalActivo;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.entity.prontuario.Meses;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.pye2.EventosRegistros;
import mx.edu.utxj.pye.sgi.exception.PeriodoEscolarNecesarioNoRegistradoException;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbPeriodoEventoRegistro")
public class EjbPeriodoEventoRegistro {
    @EJB    EjbValidadorDocente     ejbValidadorDocente;

    @EJB Facade f;
    private EntityManager em;
    
    @Inject Caster caster;
    
    @PostConstruct
    public void init(){
        em = f.getEntityManager();
    }
    
    public ResultadoEJB<Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>>> comprobarEventoActualDocente(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoRegistroActivo, PersonalActivo docente) throws PeriodoEscolarNecesarioNoRegistradoException {
        try {
            if(periodos == null || periodos.isEmpty()) periodos = ejbValidadorDocente.getPeriodosConCapturaCargaAcademica(docente).getValor();
            return comprobarEventoActualResultado(periodos, eventos, eventoRegistroActivo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de periodos escolares y eventos registros (EjbPeriodoEventoRegistro.comprobarEventoActualDocente).", e, null);
        }
    }
    
//    TODO:
    public ResultadoEJB<Map.Entry<List<PeriodosEscolares>, List<EventosRegistros>>> comprobarEventoActualTutor(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoRegistroActivo, PersonalActivo docente) throws PeriodoEscolarNecesarioNoRegistradoException {
        try {
            if(periodos == null || periodos.isEmpty()) periodos = ejbValidadorDocente.getPeriodosConCapturaCargaAcademicaTutor(docente).getValor();
            return comprobarEventoActualResultado(periodos, eventos, eventoRegistroActivo);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de periodos escolares y eventos registros (EjbPeriodoEventoRegistro.comprobarEventoActualTutor).", e, null);
        }
    }
    
    public ResultadoEJB<Map.Entry<List<PeriodosEscolares>,List<EventosRegistros>>> comprobarEventoActualResultado(List<PeriodosEscolares> periodos, List<EventosRegistros> eventos, EventosRegistros eventoRegistroActivo) throws  PeriodoEscolarNecesarioNoRegistradoException{
        try {
            if(periodos == null || periodos.isEmpty()) return null;
            if(eventoRegistroActivo == null) eventoRegistroActivo = getEventoRegistro().getValor();
            if(eventoRegistroActivo == null) return null;

            PeriodosEscolares reciente = periodos.get(0);
            Boolean existe = eventos.contains(eventoRegistroActivo);

            if(!existe){
                if(eventos.size() < 3){
                    eventos = new ArrayList<>(Stream.concat(Stream.of(eventoRegistroActivo), eventos.stream()).collect(Collectors.toList()));
                }else{
                    PeriodosEscolares periodo = em.find(PeriodosEscolares.class, reciente.getPeriodo() + 1);
                    if(periodo == null) throw new PeriodoEscolarNecesarioNoRegistradoException(reciente.getPeriodo() + 1, caster.periodoToString(reciente));
                    periodos = new ArrayList<>(Stream.concat(Stream.of(periodo), periodos.stream()).collect(Collectors.toList()));
                    eventos.clear();
                    eventos.add(eventoRegistroActivo);
                }
            }
            Map<List<PeriodosEscolares>,List<EventosRegistros>> map = new HashMap<>();
            map.put(periodos, eventos);
            
            return ResultadoEJB.crearCorrecto(map.entrySet().iterator().next(), "Periodos escolares y eventos por registro");
        }catch (Exception e){
            return  ResultadoEJB.crearErroneo(1, "No se pudo obtener el mapa de periodos escolares y eventos registros (EjbPeriodoEventoRegistro.comprobarEventoActualDocenteResultado).", e, null);
        }
        
    }
    
    public ResultadoEJB<List<EventosRegistros>> getEventosRegistroPorPeriodo(PeriodosEscolares periodo){
        try {
            if (periodo == null) {
                return null;
            }
            List<String> meses = em.createQuery("SELECT m FROM Meses m where m.numero BETWEEN :inicio AND :fin ORDER BY m.numero", Meses.class)
                    .setParameter("inicio", periodo.getMesInicio().getNumero())
                    .setParameter("fin", periodo.getMesFin().getNumero())
                    .getResultList()
                    .stream()
                    .map(m -> m.getMes())
                    .collect(Collectors.toList());

            if(!meses.isEmpty()){
                List<EventosRegistros> l = em.createQuery("SELECT er from EventosRegistros er INNER JOIN er.ejercicioFiscal ef WHERE ef.anio=:anio AND er.mes in :meses AND er.fechaInicio <= :fecha ORDER BY er.fechaInicio DESC, er.fechaFin DESC", EventosRegistros.class)
                        .setParameter("fecha", new Date())
                        .setParameter("anio", periodo.getAnio())
                        .setParameter("meses", meses)
                        .getResultList();
                return ResultadoEJB.crearCorrecto(l, "Eventos de Registro por periodo escolar");
            }else{
                return ResultadoEJB.crearErroneo(2, null,"No se pudo obtener la lista de eventos de registro debido a que no existen meses");
            }
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos de registro por periodo (EjbPeriodoEventoRegistro.getEventosRegistroPorPeriodo).", e, null);
        }
    }
    
    public ResultadoEJB<EventosRegistros> getEventoRegistro(){
        try {
            TypedQuery<EventosRegistros> query = em.createQuery("SELECT er FROM EventosRegistros er WHERE :fecha BETWEEN er.fechaInicio AND er.fechaFin", EventosRegistros.class);
            query.setParameter("fecha", new Date());
            EventosRegistros eventoRegistro = query.getSingleResult();
            return ResultadoEJB.crearCorrecto(eventoRegistro, "Evento de Registro Actual");
        } catch (NonUniqueResultException nure) {
            return ResultadoEJB.crearErroneo(2, "Se ha encontrado mas de un Periodo Escolar Activo (EjbPeriodoEventoRegistro.getEventoRegistro)", nure, EventosRegistros.class);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el evento de registro actual (EjbPeriodoEventoRegistro.getEventoRegistro).", e, EventosRegistros.class);
        }
    }
    
    public ResultadoEJB<PeriodosEscolares> getPeriodoEscolarActivo(){
        try {
            ResultadoEJB<EventosRegistros> eventoRegistro = getEventoRegistro();
            TypedQuery<PeriodosEscolares> query = em.createQuery("SELECT p FROM PeriodosEscolares p WHERE (p.anio = :anio) AND (:mes BETWEEN p.mesInicio.numero AND p.mesFin.numero)", PeriodosEscolares.class)
                    .setParameter("anio", eventoRegistro.getValor().getEjercicioFiscal().getAnio())
                    .setParameter("mes", getNumeroMes(eventoRegistro.getValor().getMes()));
            PeriodosEscolares periodosEscolares = query.getSingleResult();
            return ResultadoEJB.crearCorrecto(periodosEscolares, "Periodo Escolar Activo");
        } catch (NonUniqueResultException nure){
            return ResultadoEJB.crearErroneo(2, "Se ha encontrado mas de un Periodo Escolar Activo (EjbPeriodoEventoRegistro.getPeriodoEscolarActivo)", nure, PeriodosEscolares.class);
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el Periodo Escolar Activo (EjbPeriodoEventoRegistro.getPeriodoEscolarActivo).", e, PeriodosEscolares.class);
        }
    }
    
    public Integer getNumeroMes(String mes) {
        Integer mesNumero = 0;
        String[] meses = {"0", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        for (int i = 0; i < meses.length; i++) {
            if (meses[i].equals(mes)) {
                mesNumero = i;
            }
        }
        return mesNumero;
    }
}
