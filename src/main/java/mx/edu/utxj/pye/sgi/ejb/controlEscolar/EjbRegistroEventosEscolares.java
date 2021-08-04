/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoCalendarioEventosEscolares;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEventosEscolares;
import mx.edu.utxj.pye.sgi.ejb.prontuario.EjbPropiedades;
import mx.edu.utxj.pye.sgi.entity.ch.Personal;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEscolar;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbRegistroEventosEscolares")
public class EjbRegistroEventosEscolares {
    @EJB mx.edu.utxj.pye.sgi.ejb.EjbPersonalBean ejbPersonalBean;
    @EJB EjbPropiedades ep;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @EJB EjbPermisoAperturaExtemporanea ejbPermisoAperturaExtemporanea;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite obtener la lista de periodos escolares registradas a partir de Septiembre - Diciembre 2019
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<PeriodosEscolares>> getPeriodosEscolares(){
        try{
            List<PeriodosEscolares> periodos = em.createQuery("SELECT p FROM PeriodosEscolares p WHERE p.periodo  >=:periodo ORDER BY p.periodo DESC", PeriodosEscolares.class)
                    .setParameter("periodo", (short) 52)
                    .getResultList();
            
            return ResultadoEJB.crearCorrecto(periodos, "Lista de periodos escolar para eventos escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de periodos escolares para eventos escolares. (EjbRegistroEventosEscolares.getPeriodosEscolares)", e, null);
        }
    }
    
    /**
     * Permite obtener el último periodo escolar registrada en eventos escolares
     * @return Resultado del proceso
     */
    public ResultadoEJB<PeriodosEscolares> getUltimoPeriodoRegistrado(){
        try{
            List<Integer> listaClaves = em.createQuery("SELECT e FROM EventoEscolar e ORDER BY e.periodo DESC", EventoEscolar.class)
                    .getResultStream()
                    .map(p->p.getPeriodo())
                    .distinct()
                    .collect(Collectors.toList());
            
            List<PeriodosEscolares> listaPeriodos = new ArrayList<>();
            listaClaves.forEach(per -> {
                PeriodosEscolares periodo = em.find(PeriodosEscolares.class, per);
                listaPeriodos.add(periodo);
            });
            
            return ResultadoEJB.crearCorrecto(listaPeriodos.get(0), "Último periodo escolar registrado en eventos escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la último periodo escolar registrado en eventos escolares. (EjbRegistroEventosEscolares.getUltimoPeriodoRegistrado)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe eventos escolares registrados en el periodo escolar seleccionado
     * @param periodo
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> verificarExistenEventos(PeriodosEscolares periodo){
        try{
            EventoEscolar eventoRegistrado = em.createQuery("SELECT e FROM EventoEscolar e WHERE e.periodo=:periodo", EventoEscolar.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream().findFirst().orElse(null);
            
            Boolean valor;
            if(eventoRegistrado==null)
            {
                valor = Boolean.FALSE;
            }else{
                valor = Boolean.TRUE;
            }
           return ResultadoEJB.crearCorrecto(valor, "Resultado de eventos escolares del periodo escolar seleccionado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se encontraron eventos escolares del periodo escolar seleccionado", e, Boolean.TYPE);
        }
    }
    
     /**
     * Permite obtener la lista de eventos escolares registrados en el periodo escolar seleccionado
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCalendarioEventosEscolares>> getCalendarioEventosEscolares(PeriodosEscolares periodo){
        try{
            List<DtoCalendarioEventosEscolares> listaEventosRegistrados = em.createQuery("SELECT e FROM EventoEscolar e WHERE e.periodo=:periodo ORDER BY e.evento ASC", EventoEscolar.class)
                    .setParameter("periodo", periodo.getPeriodo())
                    .getResultStream()
                    .map(evento -> packEventoRegistrado(evento).getValor())
                    .filter(dto -> dto != null)
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaEventosRegistrados, "Lista de eventos escolares.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos escolares. (EjbRegistroEventosEscolares.getCalendarioEventosEscolares)", e, null);
        }
    }
    
     /**
     * Empaqueta un evento escolar del proceso en su DTO Wrapper
     * @param eventoEscolar
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<DtoCalendarioEventosEscolares> packEventoRegistrado(EventoEscolar eventoEscolar){
        try{
            if(eventoEscolar == null) return ResultadoEJB.crearErroneo(2, "No se puede empaquetar un evento nulo.", DtoCalendarioEventosEscolares.class);
            if(eventoEscolar.getEvento()== null) return ResultadoEJB.crearErroneo(3, "No se puede empaquetar un evento con clave nula.", DtoCalendarioEventosEscolares.class);

            EventoEscolar eventoEscolarBD = em.find(EventoEscolar.class, eventoEscolar.getEvento());
            if(eventoEscolarBD == null) return ResultadoEJB.crearErroneo(4, "No se puede empaquetar un evento no registrado previamente en base de datos.", DtoCalendarioEventosEscolares.class);
            
            Boolean eventoActivo = buscarEventoActivo(eventoEscolarBD, eventoEscolarBD.getTipo()).getValor();
            
            String situacion = "";
            if (eventoActivo) {
                situacion = "circuloVerde";
            }else{
                situacion = "circuloRojo";
            }
                    
            DtoCalendarioEventosEscolares dto = new DtoCalendarioEventosEscolares(eventoEscolarBD, situacion);
            return ResultadoEJB.crearCorrecto(dto, "Evento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el evento (EjbRegistroEventosEscolares.packEventoRegistrado).", e, DtoCalendarioEventosEscolares.class);
        }
    }
    
     /**
     * Permite verificar si existe evento escolar activo de la actividad en el periodo escolar seleccionado
     * @param eventoSeleccionado
     * @param actividad
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<Boolean> buscarEventoActivo(EventoEscolar eventoSeleccionado, String actividad){
        try{
            EventoEscolar eventoReg = em.createQuery("SELECT e FROM EventoEscolar e WHERE e.periodo=:periodo AND e.tipo=:actividad AND current_date between e.inicio and e.fin", EventoEscolar.class)
                    .setParameter("periodo", eventoSeleccionado.getPeriodo())
                    .setParameter("actividad", actividad)
                    .getResultStream().findFirst().orElse(null);
           return ResultadoEJB.crearCorrecto(eventoReg != null, "Evento escolar activo encontrado");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "", e, Boolean.TYPE);
        }
    }
    
    /**
     * Permite obtener la lista de eventos escolares dependiendo del periodo escolar seleccionado que se deberán registrar
     * @param periodo
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoEventosEscolares>> getEventosEscolares(PeriodosEscolares periodo, Personal personal){
        try{
            
            Map<Integer, String> listaActividadesPeriodo = new HashMap<Integer, String>();
            
            switch (periodo.getMesInicio().getNumero()) {
                case 9:
                    listaActividadesPeriodo = getListaActividadesSepDic().getValor();
                    break;
                case 1:
                    listaActividadesPeriodo = getListaActividadesEneAbr().getValor();
                    break;
                case 5:
                    listaActividadesPeriodo = getListaActividadesMayAgo().getValor();
                    break;
                default:
                    break;
            }
            
            List<DtoEventosEscolares> listaEventosEscolares = getDtoEventosEscolares(listaActividadesPeriodo, personal).getValor();
            
            return ResultadoEJB.crearCorrecto(listaEventosEscolares, "Lista de eventos escolares para registrar.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos escolares para registrar. (EjbRegistroEventosEscolares.getEventosEscolares)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de opciones de eventos escolares que se pueden agregar
     * @param listaEventosRegistrados
     * @param personal
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<String>> getListaOpcionesEventosEscolares(List<DtoCalendarioEventosEscolares> listaEventosRegistrados){
        try{
            List<String> listaEventos = listaEventosRegistrados.stream().map(p->p.getEventoEscolar().getTipo()).collect(Collectors.toList());
            
            Map<Integer, String> listaActividadesGeneral = getListaActividadesGeneral().getValor();
            
            List<String> listaOpcionesEventos = new ArrayList<>();
            
            for (Map.Entry<Integer, String> entry : listaActividadesGeneral.entrySet()) {
             
             Long coincidencia = listaEventos.stream().filter(p->p.equals(entry.getValue())).count();
             if(coincidencia == 0){
                listaOpcionesEventos.add(entry.getValue());
             }
            }
            
            return ResultadoEJB.crearCorrecto(listaOpcionesEventos, "Lista de eventos escolares para registrar.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos escolares para registrar. (EjbRegistroEventosEscolares.getEventosEscolares)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos escolares para periodo septiembre - diciembre
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<Integer, String>> getListaActividadesGeneral(){
        try{
            
            Map<Integer, String> listaActividadesPeriodo = new HashMap<Integer, String>();
            listaActividadesPeriodo.put(1, "Registro_citas");
            listaActividadesPeriodo.put(2, "Registro_fichas_admision");
            listaActividadesPeriodo.put(3, "Registro_fichas_admision_ingeniería");
            listaActividadesPeriodo.put(4, "Validacion_ficha_ingeniería");
            listaActividadesPeriodo.put(5, "Generacion_de_grupos");
            listaActividadesPeriodo.put(6, "Inscripciones");
            listaActividadesPeriodo.put(7, "Inscripción_ingeniería");
            listaActividadesPeriodo.put(8, "Reincorporaciones");
            listaActividadesPeriodo.put(9, "Reinscripción_autonóma");
            listaActividadesPeriodo.put(10, "Asignación_de_tutores");
            listaActividadesPeriodo.put(11, "Carga_académica");
            listaActividadesPeriodo.put(12, "Configuración_de_materia");
            listaActividadesPeriodo.put(13, "Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(14, "Validacion_Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(15, "Fusión_de_grupos");
            listaActividadesPeriodo.put(16, "Captura_de_calificaciones");
            listaActividadesPeriodo.put(17, "Captura_tarea_integradora");
            listaActividadesPeriodo.put(18, "Bajas_causas_no_academicas");

            return ResultadoEJB.crearCorrecto(listaActividadesPeriodo, "Lista de eventos escolares general.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos escolares general. (EjbRegistroEventosEscolares.getListaActividadesGeneral)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos escolares para periodo septiembre - diciembre
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<Integer, String>> getListaActividadesSepDic(){
        try{
            
            Map<Integer, String> listaActividadesPeriodo = new HashMap<Integer, String>();
            listaActividadesPeriodo.put(1, "Registro_fichas_admision_ingeniería");
            listaActividadesPeriodo.put(2, "Validacion_ficha_ingeniería");
            listaActividadesPeriodo.put(3, "Generacion_de_grupos");
            listaActividadesPeriodo.put(4, "Inscripciones");
            listaActividadesPeriodo.put(5, "Inscripción_ingeniería");
            listaActividadesPeriodo.put(6, "Reincorporaciones");
            listaActividadesPeriodo.put(7, "Reinscripción_autonóma");
            listaActividadesPeriodo.put(8, "Asignación_de_tutores");
            listaActividadesPeriodo.put(9, "Carga_académica");
            listaActividadesPeriodo.put(10, "Configuración_de_materia");
            listaActividadesPeriodo.put(11, "Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(12, "Validacion_Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(13, "Fusión_de_grupos");
            listaActividadesPeriodo.put(14, "Captura_de_calificaciones");
            listaActividadesPeriodo.put(15, "Captura_tarea_integradora");
            listaActividadesPeriodo.put(16, "Bajas_causas_no_academicas");

            return ResultadoEJB.crearCorrecto(listaActividadesPeriodo, "Lista de eventos escolares del periodo sep-dic.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos escolares del periodo sep-dic. (EjbRegistroEventosEscolares.getListaActividadesSepDic)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos escolares para periodo enero - abril
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<Integer, String>> getListaActividadesEneAbr(){
        try{
            
            Map<Integer, String> listaActividadesPeriodo = new HashMap<Integer, String>();
            listaActividadesPeriodo.put(1, "Registro_fichas_admision");
            listaActividadesPeriodo.put(2, "Registro_citas");
            listaActividadesPeriodo.put(3, "Generacion_de_grupos");
            listaActividadesPeriodo.put(4, "Reincorporaciones");
            listaActividadesPeriodo.put(5, "Reinscripción_autonóma");
            listaActividadesPeriodo.put(6, "Asignación_de_tutores");
            listaActividadesPeriodo.put(7, "Carga_académica");
            listaActividadesPeriodo.put(8, "Configuración_de_materia");
            listaActividadesPeriodo.put(9, "Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(10, "Validacion_Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(11, "Fusión_de_grupos");
            listaActividadesPeriodo.put(12, "Captura_de_calificaciones");
            listaActividadesPeriodo.put(13, "Captura_tarea_integradora");
            listaActividadesPeriodo.put(14, "Bajas_causas_no_academicas");


            return ResultadoEJB.crearCorrecto(listaActividadesPeriodo, "Lista de eventos escolares del periodo ene-abr.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos escolares del periodo ene-abr. (EjbRegistroEventosEscolares.getListaActividadesEneAbr)", e, null);
        }
    }
    
    /**
     * Permite obtener la lista de eventos escolares para periodo mayo - agosto
     * @return Resultado del proceso
     */
    public ResultadoEJB<Map<Integer, String>> getListaActividadesMayAgo(){
        try{
            
            Map<Integer, String> listaActividadesPeriodo = new HashMap<Integer, String>();
            listaActividadesPeriodo.put(1, "Registro_citas");
            listaActividadesPeriodo.put(2, "Generacion_de_grupos");
            listaActividadesPeriodo.put(3, "Reincorporaciones");
            listaActividadesPeriodo.put(4, "Reinscripción_autonóma");
            listaActividadesPeriodo.put(5, "Asignación_de_tutores");
            listaActividadesPeriodo.put(6, "Carga_académica");
            listaActividadesPeriodo.put(7, "Configuración_de_materia");
            listaActividadesPeriodo.put(8, "Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(9, "Validacion_Asignación_indicadores_criterios");
            listaActividadesPeriodo.put(10, "Fusión_de_grupos");
            listaActividadesPeriodo.put(11, "Captura_de_calificaciones");
            listaActividadesPeriodo.put(12, "Captura_tarea_integradora");
            listaActividadesPeriodo.put(13, "Bajas_causas_no_academicas");


            return ResultadoEJB.crearCorrecto(listaActividadesPeriodo, "Lista de eventos escolares del periodo may-ago.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de eventos escolares del periodo may-ago. (EjbRegistroEventosEscolares.getListaActividadesMayAgo)", e, null);
        }
    }
    
    /**
     * Empaqueta un evento de estadía del proceso en su DTO Wrapper
     * @param listaActividaes
     * @param personal
     * @return Dto del documento empaquetado
     */
    public ResultadoEJB<List<DtoEventosEscolares>> getDtoEventosEscolares(Map<Integer, String> listaActividaes, Personal personal){
        try{
            List<DtoEventosEscolares> listaEventosEscolares = new ArrayList<>();
            
            for (Map.Entry<Integer, String> entry : listaActividaes.entrySet()) {
             DtoEventosEscolares dto = new DtoEventosEscolares(entry.getKey(), entry.getValue(), personal, new Date(), new Date(), true);
             listaEventosEscolares.add(dto);
            }
            return ResultadoEJB.crearCorrecto(listaEventosEscolares, "Evento empaquetado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo empaquetar el evento (EjbRegistroEventosEscolares.packEventoEscolar).", e, null);
        }
    }
    
     /**
     * Permite actualizar las fechas de inicio y fin de un evento escolar registrado previamente
     * @param dtoCalendarioEventosEscolares
     * @return Resultado del proceso
     */
    public ResultadoEJB<EventoEscolar> actualizarEventoRegistrado(DtoCalendarioEventosEscolares dtoCalendarioEventosEscolares){
        try{
            EventoEscolar evenEscBD  = em.find(EventoEscolar.class, dtoCalendarioEventosEscolares.getEventoEscolar().getEvento());
                evenEscBD.setInicio(dtoCalendarioEventosEscolares.getEventoEscolar().getInicio());
                evenEscBD.setFin(dtoCalendarioEventosEscolares.getEventoEscolar().getFin());
                em.merge(evenEscBD);
                em.flush();
            
            return ResultadoEJB.crearCorrecto(evenEscBD, "Se actualizó correctamente la información del evento escolar seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo actualizar la información del evento escolar seleccionado. (EjbRegistroEventosEscolares.actualizarEventoRegistrado)", e, null);
        }
    }
    
     /**
     * Permite guardar la lista de eventos escolares en el periodo seleccionado
     * @param periodo
     * @param listaEventosEscolares
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<DtoCalendarioEventosEscolares>> guardarEventosEscolares(PeriodosEscolares periodo, List<DtoEventosEscolares> listaEventosEscolares, Personal personal){
        try{
            
            listaEventosEscolares.forEach(evento -> {
                Date fechaFinCompleta = ejbPermisoAperturaExtemporanea.obtenerFechaFin(evento.getFechaFin());
                EventoEscolar eventoEscolar = new EventoEscolar();
                eventoEscolar.setPeriodo(periodo.getPeriodo());
                eventoEscolar.setTipo(evento.getActividad());
                eventoEscolar.setInicio(evento.getFechaInicio());
                eventoEscolar.setFin(fechaFinCompleta);
                eventoEscolar.setCreador(personal.getClave());
                em.persist(eventoEscolar);
                em.flush();
            });
            
            List<DtoCalendarioEventosEscolares> lista = getCalendarioEventosEscolares(periodo).getValor();
            
            return ResultadoEJB.crearCorrecto(lista, "Se registró correctamente la lista de eventos escolares en el periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar la lista de eventos escolares en el periodo seleccionado. (EjbRegistroEventosEscolares.guardarEventosEscolares)", e, null);
        }
    }
    
    /**
     * Permite eliminar la lista de eventos escolares del periodo seleccionado
     * @param periodo
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarEventosEscolares(PeriodosEscolares periodo){
        try{
            Integer delete = em.createQuery("DELETE FROM EventoEscolar e WHERE e.periodo=:periodo", EventoEscolar.class)
                .setParameter("periodo", periodo.getPeriodo())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente la lista de eventos escolares del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar la lista de eventos escolares del periodo seleccionado. (EjbRegistroEventosEscolares.eliminarEventosEscolares)", e, null);
        }
    }
    
     /**
     * Permite registrar el evento escolar en el periodo escolar seleccionado
     * @param periodo
     * @param evento
     * @param fechaInicio
     * @param fechaFin
     * @param personal
     * @return Verdadero o Falso según sea el caso
     */
    public ResultadoEJB<EventoEscolar> agregarEventoEscolar(PeriodosEscolares periodo, String evento, Date fechaInicio, Date fechaFin, Personal personal){
        try{
            
            EventoEscolar eventoEscolar = new EventoEscolar();
            eventoEscolar.setPeriodo(periodo.getPeriodo());
            eventoEscolar.setTipo(evento);
            eventoEscolar.setInicio(fechaInicio);
            eventoEscolar.setFin(fechaFin);
            eventoEscolar.setCreador(personal.getClave());
            em.persist(eventoEscolar);
             
            return ResultadoEJB.crearCorrecto(eventoEscolar, "Evento escolar registrado en el periodo seleccionado.");
        } catch (Exception e) {
            return ResultadoEJB.crearErroneo(1, "No se pudo registrar el evento escolar en el periodo seleccionado. (EjbRegistroEventosEscolares.agregarEventoEscolar)", e, null);
        }
    }
    
    /**
     * Permite eliminar el evento escolar del periodo seleccionado
     * @param eventoEscolar
     * @return Resultado del proceso
     */
    public ResultadoEJB<Integer> eliminarEventoEscolar(EventoEscolar eventoEscolar){
        try{
            Integer delete = em.createQuery("DELETE FROM EventoEscolar e WHERE e.evento=:evento", EventoEscolar.class)
                .setParameter("evento", eventoEscolar.getEvento())
                .executeUpdate();
            
            return ResultadoEJB.crearCorrecto(delete, "Se eliminó correctamente el evento escolar del periodo seleccionado.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo eliminar el evento escolar del periodo seleccionado. (EjbRegistroEventosEscolares.eliminarEventoEscolar)", e, null);
        }
    }
    
}
