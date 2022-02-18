/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.utxj.pye.sgi.ejb.controlEscolar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import mx.edu.utxj.pye.sgi.dto.ResultadoEJB;
import mx.edu.utxj.pye.sgi.dto.controlEscolar.DtoDatosEstudiante;
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
    public ResultadoEJB<Estudiante> validarEstudianteFotografias(Integer matricula){
        try{
            List<Integer> grados = new ArrayList<>();  grados.add(5); grados.add(6); grados.add(10); grados.add(11);
            Estudiante e = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula =:matricula AND e.grupo.grado IN :grados ORDER BY e.periodo DESC",  Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("grados", grados)
                    .getResultStream().findFirst().orElse(null);
                    
            List<Generaciones> listaGeneraciones = listaGeneracionesFotografias(matricula, listaEventosEntregaFotografias().getValor()).getValor();
            if((e.getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("1")) || e.getTipoEstudiante().getIdTipoEstudiante().equals(Short.parseShort("4"))) && !listaGeneraciones.isEmpty()){
                return ResultadoEJB.crearCorrecto(e, "El usuario ha sido comprobado como estudiante y con seguimiento de estadía registrado.");
            }else {
                return ResultadoEJB.crearErroneo(2, "El estudiante encontrado no tiene una inscripcion activa o no tiene registro de seguimiento de estadía.", Estudiante.class);
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
            
            List<Integer> tiposEst = new ArrayList<>(); tiposEst.add(1); tiposEst.add(4);
            
            listaEventosFotografias.forEach(evento -> {
                
                List<Integer> grados = new ArrayList<>();   
                
                if(!evento.getNivel().equals("TSU")){
                    grados.add(10); grados.add(11);
                }else{
                    grados.add(5); grados.add(6);
                }
               
                Estudiante estadiaEst = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula=:matricula AND e.grupo.generacion =:generacion AND e.grupo.grado IN :grados AND e.tipoEstudiante.idTipoEstudiante IN :tiposEst",  Estudiante.class)
                        .setParameter("matricula", matricula)
                        .setParameter("generacion", evento.getGeneracion())
                        .setParameter("grados", grados) 
                        .setParameter("tiposEst", tiposEst)     
                        .getResultStream().findFirst().orElse(null);
                    
                if(estadiaEst != null){
                    Generaciones generacion = em.find(Generaciones.class,estadiaEst.getGrupo().getGeneracion());
                    listaGeneraciones.add(generacion);
                }
            });
            
             List<Generaciones> listaGeneracionesDistintas = listaGeneraciones.stream()
                    .distinct()
                    .sorted(Comparator.comparing(Generaciones::getGeneracion).reversed()) 
                    .collect(Collectors.toList());
            
            return ResultadoEJB.crearCorrecto(listaGeneracionesDistintas, "Lista de generaciones con evento de estadía para entrega de fotografías para el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener la lista de generaciones con evento de estadía para entrega de fotografías para el estudiante. (EjbLetreroFotografias.listaGeneracionesFotografias)", e, null);
        }
    }
    
     /**
     * Permite verificar el nivel educativo de la generación seleccionada con entrega de fotografías que apliquen para el estudiante
     * @param matricula
     * @param generacion
     * @param listaEventosFotografias
     * @return Resultado del proceso
     */
    public ResultadoEJB<ProgramasEducativosNiveles> getNivelGeneracionesFotografias(Integer matricula, Generaciones generacion, List<EventoEstadia> listaEventosFotografias){
        try{
            
           Estudiante estudiante = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula =:matricula AND e.grupo.generacion=:generacion", Estudiante.class)
                    .setParameter("matricula", matricula)
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream().findFirst().orElse(null);

                AreasUniversidad carrera = em.find(AreasUniversidad.class, estudiante.getGrupo().getIdPe());
                ProgramasEducativosNiveles nivel = em.find(ProgramasEducativosNiveles.class, carrera.getNivelEducativo().getNivel());
            
             
            return ResultadoEJB.crearCorrecto(nivel, "Nivel educativo con evento de estadía para entrega de fotografías para el estudiante.");
        }catch (Exception e){
            return ResultadoEJB.crearErroneo(1, "No se pudo obtener el nivel educativo con evento de estadía para entrega de fotografías para el estudiante. (EjbLetreroFotografias.getNivelGeneracionesFotografias)", e, null);
        }
    }
    
    /**
     * Permite obtener la información del estudiante
     * @param generacion
     * @param nivelEducativo
     * @param estudiante
     * @return Resultado del proceso
     */
    public ResultadoEJB<DtoDatosEstudiante> getInformacionEstudiante(Generaciones generacion, ProgramasEducativosNiveles nivelEducativo, Estudiante estudiante){
        try{
            Estudiante estudianteGeneracion = em.createQuery("SELECT e FROM Estudiante e WHERE e.matricula =:matricula AND e.grupo.generacion=:generacion ORDER BY e.periodo DESC", Estudiante.class)
                    .setParameter("matricula", estudiante.getMatricula())
                    .setParameter("generacion", generacion.getGeneracion())
                    .getResultStream().findFirst().orElse(null);
            
            AreasUniversidad programaEducativo = em.find(AreasUniversidad.class, estudianteGeneracion.getGrupo().getIdPe());
            PeriodosEscolares periodoEscolar = em.find(PeriodosEscolares.class, estudianteGeneracion.getPeriodo());
            DtoDatosEstudiante dtoDatosEstudiante = new DtoDatosEstudiante(estudianteGeneracion, programaEducativo, periodoEscolar);
            
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
