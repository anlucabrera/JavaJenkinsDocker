/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoEstudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.Estudiante;
import mx.edu.utxj.pye.sgi.entity.controlEscolar.EventoEstadia;
import mx.edu.utxj.pye.sgi.entity.prontuario.AreasUniversidad;
import mx.edu.utxj.pye.sgi.entity.prontuario.Generaciones;
import mx.edu.utxj.pye.sgi.entity.prontuario.PeriodosEscolares;
import mx.edu.utxj.pye.sgi.entity.prontuario.ProgramasEducativosNiveles;
import mx.edu.utxj.pye.sgi.facade.Facade;

/**
 *
 * @author UTXJ
 */
@Stateless(name = "EjbLetreroFotografias")
public class EjbLetreroFotografias {
    @EJB EjbAsignacionRolesEstadia ejbAsignacionRolesEstadia;
    @EJB EjbPacker ejbPacker;
    @EJB Facade f;
    private EntityManager em;
    
    @PostConstruct
    public  void init(){
        em = f.getEntityManager();
    }
    
     /**
     * Permite validar si el usuario logueado es del tipo estudiante y deba visualizar el letro de fotografías
     * @param matricula
     * @return Regresa la instancia del estudiante si este cumple con lo mencionado
     */
    public ResultadoEJB<DtoEstudiante> validarEstudianteFotografias(Integer matricula){
        try{
            DtoEstudiante e = ejbPacker.packEstudiante(matricula).getValor();
            List<Generaciones> listaGeneraciones = listaGeneracionesFotografias(matricula, listaEventosEntregaFotografias().getValor()).getValor();
            if((e.getInscripcionActiva().getInscripcion().getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1"))) && !listaGeneraciones.isEmpty()){
                return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante y con seguimiento de estadía registrado.");
            }else {
                return ResultadoEJB.crearErroneo(2, "El estudiante encontrado no tiene una inscripcion activa o no tiene registro de seguimiento de estadía.", DtoEstudiante.class);
            }

        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "El estudiante no se pudo validar. (EjbLetreroFotografias.validarEstudianteAsignado)", e, null);
        }
    }
    
     /**
     * Permite verificar eventos registrados para entrega de fotografías
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<EventoEstadia>> listaEventosEntregaFotografias(){
        try{
            List<EventoEstadia> listaEventos = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.actividad =:actividad",  EventoEstadia.class)
                    .setParameter("actividad", "Entrega de fotografías")
                    .getResultList();
            
            
            return ResultadoEJB.crearCorrecto(listaEventos, "Lista de eventos de estadía para entrega de fotografías registradas.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe eventos de estadía para entrega de fotografías. (EjbLetreroFotografias.listaEventosEntregaFotografias)", e, null);
        }
    }
    
     /**
     * Permite verificar las generaciones con entrega de fotografías que apliquen para el estudiante
     * @param matricula
     * @param listaEventosFotografias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<Generaciones>> listaGeneracionesFotografias(Integer matricula, List<EventoEstadia> listaEventosFotografias){
        try{
            List<Generaciones> listaGeneraciones = new ArrayList<>();
            
            listaEventosFotografias.forEach(evento -> {
                
                List<Estudiante> listaEstudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula AND e.grupo.generacion =:generacion",  Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("generacion", evento.getGeneracion())
                    .getResultList();
                
                listaEstudiante.forEach(estudiante -> {
                    Generaciones generacion = em.find(Generaciones.class,estudiante.getGrupo().getGeneracion());
                    listaGeneraciones.add(generacion);
                });
            });
            
             List<Generaciones> listaGeneracionesDistintas = listaGeneraciones.stream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaGeneracionesDistintas, "Lista de generaciones con evento de estadía para entrega de fotografías para el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones con evento de estadía para entrega de fotografías para el estudiante. (EjbLetreroFotografias.listaGeneracionesFotografias)", e, null);
        }
    }
    
     /**
     * Permite verificar las niveles educativos de la generación seleccionada con entrega de fotografías que apliquen para el estudiante
     * @param matricula
     * @param generacion
     * @param listaEventosFotografias
     * @return Resultado del proceso
     */
    public ResultadoEJB<List<ProgramasEducativosNiveles>> listaNivelesGeneracionesFotografias(Integer matricula, Generaciones generacion, List<EventoEstadia> listaEventosFotografias){
        try{
            List<ProgramasEducativosNiveles> listaNiveles = new ArrayList<>();
             
            List<Estudiante> listaEstudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula =:matricula AND e.grupo.generacion=:generacion", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultList();

            listaEstudiante.forEach(estudiante -> {
                AreasUniversidad carrera = em.find(AreasUniversidad.class, estudiante.getCarrera());
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, carrera.getNivelEducativo().getNivel());
                listaNiveles.add(nivel);
            });
            
             List<ProgramasEducativosNiveles> listaNivelesDistintos = listaNiveles.stream()
                    .distinct()
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaNivelesDistintos, "Lista de niveles educativos con evento de estadía para entrega de fotografías para el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de niveles educativos con evento de estadía para entrega de fotografías para el estudiante. (EjbLetreroFotografias.listaNivelesGeneracionesFotografias)", e, null);
        }
    }
    
    /**
     * Permite obtener la información del estudiante
     * @param generacion
     * @param nivelEducativo
     * @param dtoEstudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoDatosEstudiante> getInformacionEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, DtoEstudiante dtoEstudiante){
        try{
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, dtoEstudiante.getInscripcionActiva().getInscripcion().getCarrera());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, dtoEstudiante.getInscripcionActiva().getInscripcion().getPeriodo());
            DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(dtoEstudiante.getInscripcionActiva().getInscripcion(), programaEducativo, periodoEscolar);
            
            return ResultadoEJB.crearCorrecto(dtoDatosEstudiante, "Información del estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la información del estudiante. (EjbLetreroFotografias.getInformacionEstudiante)", e, null);
        }
    }
    
     /**
     * Permite verificar si existe evento activo para entrega de fotografías
     * @param eventoEstadia
     * @return Devuelve el evento de estadía, código de error 2 si no la tiene y código de error 1 para un error desconocido.
     */
    public ResultadoEJB<Boolean> buscarEventoActivo(EventoEstadia eventoEstadia){
       try{
            Boolean valor;
            EventoEstadia eventoActivo = em.createQuery("SELECT e FROM EventoEstadia e WHERE e.evento=:evento AND current_date between e.fechaInicio and e.fechaFin", EventoEstadia.class)
                    .setParameter("evento", eventoEstadia.getEvento())
                    .getResultStream().findFirst().orElse(null);
            if(eventoActivo == null)
            {
                valor= false;
            }else{
                valor = true;
            }
            return ResultadoEJB.crearCorrecto(valor, "Existe o no evento activo para entrega de fotografías");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo verificar si existe o no evento activo para entrega de fotografías. (EjbLetreroFotografias.buscarEventoActivo)", e, null);
        }
    }
    
}
